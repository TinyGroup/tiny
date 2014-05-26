/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
 *
 *  Licensed under the GPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/gpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tinygroup.tinyioc.impl;

import org.apache.commons.beanutils.BeanUtils;
import org.tinygroup.tinyioc.*;
import org.tinygroup.tinyioc.annotation.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by luoguo on 13-12-27.
 */
public class BeanContainerImpl implements BeanContainer {
    DynamicProxy proxy = new DynamicProxy();
    ThreadLocal threadLocal = new ThreadLocal();
    Map<String, Class> nameMap = new HashMap<String, Class>();
    Map<Class, String> scopeMap = new HashMap<Class, String>();
    List<Class> classList = new ArrayList<Class>();
    Map<Class, Object> objectMap = new HashMap<Class, Object>();
    private Map<Class, TypeConverter> typeConverterMap = new HashMap<Class, TypeConverter>();
    private BeanContainer parent = null;
    private List<BeanContainer> beanContainerList = new ArrayList<BeanContainer>();

    private ClassLoader classLoader;

    public BeanContainerImpl() {

    }

    public BeanContainerImpl(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public <T> void registerClass(Class<T> clazz) {
        Named named = clazz.getAnnotation(Named.class);
        if (named != null && named.value().length() > 0) {
            nameMap.put(named.value(), clazz);
        }
        String scope = "singleton";
        Request request = clazz.getAnnotation(Request.class);
        if (request != null) {
            scope = "request";
        }
        Prototype prototype = clazz.getAnnotation(Prototype.class);
        if (prototype != null) {
            scope = "prototype";
        }

        classList.add(clazz);
        scopeMap.put(clazz, scope);
    }

    public <T> T getBeanByName(String name) {
        Class clazz = nameMap.get(name);
        if (clazz == null) {
            throw new RuntimeException(String.format("bean with name [%s] not found", name));
        }
        try {
            return (T) getBeanByType(clazz);
        } catch (RuntimeException e) {
            if (parent != null) {
                return (T) parent.getBeanByName(name);
            } else {
                throw e;
            }
        }
    }

    public <T> T getBeanByType(String type) {
        Class clazz = null;
        try {
            clazz = Class.forName(type);
            return (T) getBeanByType(clazz);
        } catch (ClassNotFoundException e) {
            if (parent != null) {
                return (T) parent.getBeanByType(type);
            }
            throw new RuntimeException(e);
        }

    }

    public <T> T getBeanByType(Class<T> clazz) {
        String scope = scopeMap.get(clazz);
        T object = null;

        if (scope != null) {
            if (scope.equalsIgnoreCase("singleton")) {
                object = (T) getSingletonObject(clazz);
            } else if (scope.equalsIgnoreCase("prototype")) {
                object = (T) getPrototypeObject(clazz);
            } else if (scope.equalsIgnoreCase("request")) {
                object = (T) getThreadObject(clazz);
            }
        } else {
            //如果是子孙类或实现了接口
            for (Class clz : classList) {
                if (isSubClass(clz, clazz)) {
                    object = (T) getBeanByType(clz);
                }
            }
        }
        if (object != null) {
            return object;
        }
        if (parent != null) {
            return parent.getBeanByType(clazz);
        }
        throw new RuntimeException(clazz.getName() + " not found in container.");
    }

    private <T> void buildObject(T object, Class<T> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            Inject inject = field.getAnnotation(Inject.class);
            //如果属性上有注解
            if (inject != null) {
                String name = null;
                Named named = field.getAnnotation(Named.class);
                Value valueAnnotation = field.getAnnotation(Value.class);
                if (named != null && named.value().length() > 0) {
                    name = named.value();
                }
                Object value = null;
                Type fc = field.getGenericType();
                if (fc instanceof ParameterizedType) {
                    ParameterizedType pt = (ParameterizedType) fc;
                    Class genericClazz = (Class) pt.getActualTypeArguments()[0];
                    value = getCollectionObject(field, genericClazz);
                } else if (valueAnnotation != null && valueAnnotation.value().length() > 0) {
                    //如果是设置的固定值
                    value = getValueObject(field, valueAnnotation.value());
                } else {
                    value = getSingleObject(field, inject, named);

                }
                if (value != null) {
                    try {
                        BeanUtils.setProperty(object, field.getName(), value);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    throw new RuntimeException(String.format("类%s的属性%s对应的Bean找不到!", object.getClass().getName(), field.getName()));
                }
            }
        }
    }

    private Object getSingleObject(Field field, Inject inject, Named named) {
        Object value;
        if (named != null && named.value().length() > 0) {
            //如果是按名字
            value = getBeanByName(named.value());
        } else {
            //自动进行检测
            value = getBeanByType(field.getType());
            if (value == null) {
                value = getBeanByName(field.getName());
            }
        }
        return value;
    }

    private Object getValueObject(Field field, String stringValue) {
        Object value;
        TypeConverter converter = typeConverterMap.get(field.getType());
        if (converter != null) {
            value = converter.convert(stringValue);
        } else {
            value = stringValue;
        }
        return value;
    }

    private Object getCollectionObject(Field field, Class clazz) {
        Object value = null;
        if (field.getType().equals(List.class)) {
            value = getBeanList(clazz);
        } else if (field.getType().equals(Set.class)) {
            value = getBeanSet(clazz);
        } else if (field.getType().equals(Collection.class)) {
            value = getBeanCollection(clazz);
        }
        return value;
    }

    public static boolean isSubClass(Class a, Class b) {
        Type genericSuperclass = a.getGenericSuperclass();
        for (Type type : a.getGenericInterfaces()) {
            if (type.equals(b)) {
                return true;
            }
            boolean is = isSubClass((Class) type, b);
            if (is) {
                return true;
            }
        }
        if (genericSuperclass != null) {
            if (genericSuperclass.equals(b)) {
                return true;
            } else {
                boolean is = isSubClass((Class) genericSuperclass, b);
                if (is) {
                    return true;
                }
            }
        }
        return false;
    }

    private <T> T getThreadObject(Class<T> clazz) {
        Map<Class, Object> threadMap = (Map<Class, Object>) threadLocal.get();
        if (threadMap == null) {
            threadMap = new HashMap<Class, Object>();
            threadLocal.set(threadMap);
        }
        T object = (T) threadMap.get(clazz);
        if (object == null) {
            object = getPrototypeObject(clazz);
            threadMap.put(clazz, object);
        }
        return object;
    }

    private <T> T getPrototypeObject(Class<T> clazz) {
        try {
            T object = null;
            if (!isInterceptorClass(clazz) && proxy.isMatchClassName(clazz.getName())) {
                object = (T) proxy.getProxyObject(clazz);
            } else {
                object = clazz.newInstance();
            }
            buildObject(object, clazz);
            return object;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    boolean isInterceptorClass(Class clz) {
        if (isSubClass(clz, InterceptorAfter.class)) {
            return true;
        }
        if (isSubClass(clz, InterceptorBefore.class)) {
            return true;
        }
        if (isSubClass(clz, InterceptorException.class)) {
            return true;
        }
        return false;
    }

    private <T> T getSingletonObject(Class<T> clazz) {
        T object = (T) objectMap.get(clazz);
        if (object == null) {
            object = getPrototypeObject(clazz);
            objectMap.put(clazz, object);
        }
        return object;
    }

    public <T> List<T> getBeanList(String type) {
        Class typeClazz = null;
        try {
            typeClazz = Class.forName(type);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return getBeanList(typeClazz);
    }

    public <T> List<T> getBeanList(Class<T> clazz) {
        List<T> list = new ArrayList<T>();
        for (Class<?> clz : classList) {
            if (isSubClass(clz, clazz)) {
                T beanByType = (T) getBeanByType(clz);
                list.add(beanByType);
            }
        }
        return list;
    }

    public <T> Set<T> getBeanSet(String type) {
        Class typeClazz = null;
        try {
            typeClazz = Class.forName(type);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return getBeanSet(typeClazz);
    }

    public <T> Set<T> getBeanSet(Class<T> clazz) {
        Set<T> set = new HashSet<T>();
        for (Class<?> clz : classList) {
            if (isSubClass(clz, clazz)) {
                T beanByType = (T) getBeanByType(clz);
                set.add(beanByType);
            }
        }
        return set;
    }

    public <T> Collection<T> getBeanCollection(String type) {
        return getBeanList(type);
    }

    public <T> Collection<T> getBeanCollection(Class<T> clazz) {
        return getBeanList(clazz);
    }

    public boolean isExistBeanByName(String name) {
        return nameMap.containsKey(name);
    }

    public boolean isExistBeanByType(String type) {
        try {
            return isExistBeanByType(Class.forName(type));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isExistBeanByType(Class clazz) {
        return typeConverterMap.containsKey(clazz);
    }

    public void addTypeConverter(TypeConverter typeConverter) {
        typeConverterMap.put(typeConverter.getType(), typeConverter);
    }

    public void addAop(AopDefine aopDefine) {
        proxy.addAop(aopDefine);
    }

    public void setParent(BeanContainer beanContainer) {
        this.parent = beanContainer;
    }

    public void addBeanContainer(BeanContainer beanContainer) {
        if (!beanContainerList.contains(beanContainer)) {
            beanContainerList.add(beanContainer);
            beanContainer.setParent(this);
        }
    }

    public void removeBeanContainer(BeanContainer beanContainer) {
        if (beanContainerList.contains(beanContainer)) {
            beanContainerList.remove(beanContainer);
            beanContainer.setParent(null);
        }
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public ClassLoader getClassLoader() {
        if (classLoader == null) {
            return this.getClass().getClassLoader();
        }
        return classLoader;
    }
}
