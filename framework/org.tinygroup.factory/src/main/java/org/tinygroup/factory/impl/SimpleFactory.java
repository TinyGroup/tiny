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
package org.tinygroup.factory.impl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.tinygroup.factory.Factory;
import org.tinygroup.factory.FactoryAware;
import org.tinygroup.factory.config.Bean;
import org.tinygroup.factory.config.Beans;
import org.tinygroup.factory.config.Entry;
import org.tinygroup.factory.config.Property;
import org.tinygroup.factory.config.Ref;
import org.tinygroup.factory.exception.BeanFactoryRuntimeException;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 
 * 功能说明: 工厂类的默认实现
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-2-25 <br>
 * <br>
 */
public class SimpleFactory implements Factory {
	private static final String BY_TYPE = "byType";
	private static final String BY_NAME = "byName";
	private static final String SINGLETON = "singleton";
	private static Logger logger = LoggerFactory.getLogger(SimpleFactory.class);
	private Map<String, Object> idMap = new HashMap<String, Object>();
	private Map<String, Object> nameMap = new HashMap<String, Object>();
	private Map<String, Object> nameClassMap = new HashMap<String, Object>();
	private Map<Class<?>, Object> classMap = new HashMap<Class<?>, Object>();
	private List<Beans> beansList = new ArrayList<Beans>();
	private Map<Object, Bean> objectBeanMap = new HashMap<Object, Bean>();
	private Map<Object, Boolean> readyMap = new HashMap<Object, Boolean>();

	public void clean() {
		idMap.clear();
		nameMap.clear();
		nameClassMap.clear();
		classMap.clear();
		beansList.clear();
		objectBeanMap.clear();
		readyMap.clear();
	}

	private void addBean(Bean bean, Object object) {
		addBeanWithId(bean, object);
		addBeanWithName(bean, object);
		addBeanWithClass(bean, object);
		addBeanWithNameClass(bean, object);
	}

	private void addBeanWithNameClass(Bean bean, Object object) {
		nameClassMap.put(
				String.format("%s|%s", bean.getName(), bean.getType()), object);
	}

	private void addBeanWithClass(Bean bean, Object object) {
		try {
			classMap.put(Class.forName(bean.getType()), object);
		} catch (ClassNotFoundException e) {
			logger.errorMessage("查找[{0}]类失败， 原因：{0}", e, e.getMessage());
		}
	}

	private void addBeanWithName(Bean bean, Object object) {
		if (bean.getName() != null && bean.getName().length() > 0) {
			String[] names = bean.getName().split(",");
			for (String name : names) {
				if (name.length() > 0) {
					// 如果名字有重复的就被覆盖了
					nameMap.put(name, object);
				}
			}
		}
	}

	private void addBeanWithId(Bean bean, Object object) {
		if (bean.getId() != null && bean.getId().length() > 0) {
			Bean oldBean = objectBeanMap.get(bean.getId());
			if (oldBean != null) {
				logger.logMessage(LogLevel.ERROR,
						"[{}] 类型 bean id [{}] 已经存在，类型为[{}，原有Bean将被覆盖]！",
						bean.getClassName(), bean.getId(),
						oldBean.getClassName());
			}
			objectBeanMap.put(object, bean);
			idMap.put(bean.getId(), object);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T getBean(String name) {
		Object object = getObject(name);
		if (object != null) {
			return checkScope((T) object);
		}
		throw new BeanFactoryRuntimeException(String.format("找不到名字是[%s]的Bean.",
				name));
	}

	private Object getObject(String name) {
		Object object = idMap.get(name);
		if (object == null) {
			object = nameMap.get(name);
		}
		if (object != null) {
			Boolean ready = readyMap.get(object);
			if (ready == null) {
				assembleBean(object);
			}
		}
		return object;
	}

	@SuppressWarnings("unchecked")
	private <T> T checkScope(T object) {
		try {
			if (object != null) {
				Bean bean = objectBeanMap.get(object);
				String scope = bean.getScope();
				if (scope.equals(SINGLETON)) {
					return object;
				} else if (scope.equals("prototype")) {
					T newObject = (T) retriveClass(bean.getClassName())
							.newInstance();
					BeanUtils.copyProperties(newObject, object);// 如果属性是Prototype是不是也创建一个新的？
					return newObject;
				}
			}
			throw new BeanFactoryRuntimeException(String.format("Bean不存在。"));
		} catch (Exception e) {
			throw new BeanFactoryRuntimeException(e);
		}
	}

	private void assembleBean(Object object) {
		if (readyMap.get(object) == null || readyMap.get(object).equals(false)) {
			loadProperties(object);
		}
	}

	private void autoAssembleBean(Bean beanDefine, Object newInstance) {
		for (PropertyDescriptor propertyDescriptor : PropertyUtils
				.getPropertyDescriptors(newInstance)) {
			try {
				if (propertyDescriptor.getPropertyType().equals(Class.class)) {
					continue;
				}
				if (beanDefine.getAutowire().equalsIgnoreCase(BY_NAME)) {
					Object bean = getBean(propertyDescriptor.getName());
					if (bean != null
							&& propertyDescriptor.getPropertyType().isInstance(
									bean)) {
						BeanUtils.setProperty(newInstance,
								propertyDescriptor.getName(), bean);
					}
				} else if (beanDefine.getAutowire().equalsIgnoreCase(BY_TYPE)) {
					Object bean = getBean(propertyDescriptor.getPropertyType());
					if (bean != null) {
						BeanUtils.setProperty(newInstance,
								propertyDescriptor.getName(), bean);
					}
				}
			} catch (Exception e) {
				logger.errorMessage("为属性{0}赋值时出错", e,
						propertyDescriptor.getName());
			}
		}
	}

	private void loadProperties(Object object) {
		Bean bean = objectBeanMap.get(object);
		try {
			if (bean.getAutowire() != null) {
				autoAssembleBean(bean, object);
				return;// 如果是自动装配，做完直接返回
			}
			if (bean.getProperties() != null) {
				for (Property property : bean.getProperties()) {
					if (property.getValue() != null) {
						BeanUtils.setProperty(object, property.getName(),
								property.getValue());
					} else if (property.getRef() != null) {
						BeanUtils.setProperty(object, property.getName(),
								getObject(property.getRef()));
					} else if (property.getList() != null) {
						loadListProperty(object, property);
					} else if (property.getMap() != null) {
						loadMapProperty(object, property);
					}
				}
			}
			readyMap.put(object, true);
		} catch (Exception e) {
			readyMap.put(object, false);
			logger.errorMessage(e.getMessage(), e);
		}
	}

	private void loadMapProperty(Object object, Property property)
			throws IllegalAccessException, InvocationTargetException {
		Map<String, Object> valueMap = new HashMap<String, Object>();
		for (Entry entry : property.getMap().getEntry()) {
			if (entry.getValue() != null) {
				valueMap.put(entry.getKey(), entry.getValue());
			} else if (entry.getValueRef() != null) {
				valueMap.put(entry.getKey(), getObject(entry.getValueRef()));
			}
		}
		BeanUtils.setProperty(object, property.getName(), valueMap);
	}

	private void loadListProperty(Object object, Property property)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		List<Object> valueList = new ArrayList<Object>();
		for (Ref ref : property.getList()) {
			valueList.add(getObject(ref));
		}
		if (PropertyUtils.getPropertyDescriptor(object, property.getName())
				.getPropertyType().isAssignableFrom(List.class)) {
			BeanUtils.setProperty(object, property.getName(), valueList);
		} else if (PropertyUtils
				.getPropertyDescriptor(object, property.getName())
				.getPropertyType().isAssignableFrom(Set.class)) {
			Set<Object> set = new HashSet<Object>();
			set.addAll(valueList);
			BeanUtils.setProperty(object, property.getName(), set);
		}
	}

	private Object getObject(Ref ref) {
		if (ref.getId() != null) {
			return getBean(ref.getId());// 20130130 getObject修改为getBean
		} else if (ref.getName() != null) {
			return getBean(ref.getName());
		} else if (ref.getClassName() != null) {
			return getBean(getType(ref.getClassName()));
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> T getBean(String name, Class<T> clazz) {
		Object object = nameClassMap.get(String.format("%s|%s", name,
				clazz.getName()));
		if (object != null && readyMap.get(object)) {
			return checkScope((T) object);
		}

		throw new BeanFactoryRuntimeException(String.format("找不到类[%s]对应的Bean.",
				clazz.getName()));
	}

	@SuppressWarnings("unchecked")
	public <T> T getBean(Class<T> clazz) {
		Object object = classMap.get(clazz);
		if (object != null) {
			Boolean ready = readyMap.get(object);
			if (ready == null) {
				assembleBean(object);
			}
			ready = readyMap.get(object);
			if (ready) {
				return checkScope((T) object);
			}
		}

		throw new BeanFactoryRuntimeException(String.format("找不到类[%s]对应的Bean.",
				clazz.getName()));

	}

	public boolean containsBean(String name) {
		return nameMap.containsKey(name);
	}

	public Class<?> getType(String name) {
		try {
			return retriveClass(name);
		} catch (ClassNotFoundException e) {
			throw new BeanFactoryRuntimeException(e);
		}
	}

	private Class<?> retriveClass(String name) throws ClassNotFoundException {
		return Class.forName(name);
	}

	public void init() {
		// 初始化时把Bean创建好
		for (Beans beans : beansList) {
			loadBeas(beans);
		}
		for (Object object : objectBeanMap.keySet()) {
			assembleBean(object);
		}
	}

	private void loadBeas(Beans beans) {
		if (beans.getBeanList() != null) {
			for (Bean bean : beans.getBeanList()) {
				loadBean(bean);
			}
		}
	}

	private void loadBean(Bean bean) {
		try {
			if (objectBeanMap.values().contains(bean)) {
				return;
			}
			Object object = Class.forName(bean.getClassName()).newInstance();
			if (object instanceof FactoryAware) {
				FactoryAware factoryAware = (FactoryAware) object;
				factoryAware.setFactory(this);
			}
			addBean(bean, object);
		} catch (Exception e) {
			logger.errorMessage("初始化[{0}]类失败， 原因：{1}", e, bean.getType(),
					e.getMessage());
		}
	}

	public void addBeans(Beans beans) {
		// 如果packageName不为空，则先判断该beans是否已被添加过，若添加过则删除旧的
		if (beans.getPackageName() != null) {
			for (Beans b : beansList) {
				if (b.getPackageName() != null
						&& beans.getPackageName().equals(b.getPackageName())) {
					beansList.remove(b);
					break;
				}
			}
		}
		beansList.add(beans);
	}

	@SuppressWarnings("unchecked")
	public <T> T createBean(String className) {
		try {
			return (T) getType(className).newInstance();
		} catch (Exception e) {
			throw new BeanFactoryRuntimeException(e);
		}
	}

	public void removeBeans(Beans beans) {

		for (Bean bean : beans.getBeanList()) {
			removeBean(bean);
		}

		// 从benasList中删除
		if (beans.getPackageName() != null) {
			for (Beans b : beansList) {
				if (b.getPackageName() != null
						&& beans.getPackageName().equals(b.getPackageName())) {
					beansList.remove(b);
					logger.logMessage(LogLevel.INFO,
							"从beansList移除Beans[package-name:{0}]",
							beans.getPackageName());
					break;
				}
			}
		}

	}

	private void removeBean(Bean bean) {
		removeBeanWithNameClass(bean);
		removeBeanWithClass(bean);
		removeBeanWithName(bean);
		removeBeanWithId(bean);
	}

	private void removeBeanWithNameClass(Bean bean) {
		nameClassMap.remove(String.format("%s|%s", bean.getName(),
				bean.getType()));
	}

	private void removeBeanWithClass(Bean bean) {
		classMap.remove(bean.getClass());
	}

	private void removeBeanWithName(Bean bean) {
		if (bean.getName() != null && bean.getName().length() > 0) {
			String[] names = bean.getName().split(",");
			for (String name : names) {
				if (name.length() > 0) {
					// 如果名字有重复的就被覆盖了
					nameMap.remove(name);
				}
			}
		}
	}

	private void removeBeanWithId(Bean bean) {
		if (bean.getId() != null && bean.getId().length() > 0) {
			Object object = idMap.remove(bean.getId());
			objectBeanMap.remove(object);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T createBean(Class<?> clazz) {
		try {
			return (T) clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}