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
package org.tinygroup.context2object.impl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.tinygroup.context.Context;
import org.tinygroup.context2object.ObjectGenerator;
import org.tinygroup.context2object.TypeConverter;
import org.tinygroup.context2object.TypeCreator;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.springutil.SpringUtil;

public class ClassNameObjectGenerator implements
		ObjectGenerator<Object, String> {
	private Logger logger = LoggerFactory
			.getLogger(ClassNameObjectGenerator.class);
	private List<TypeConverter<?, ?>> typeConverterList = new ArrayList<TypeConverter<?, ?>>();
	private List<TypeCreator<?>> typeCreatorList = new ArrayList<TypeCreator<?>>();
	
	public Object getObject(String varName, String bean, String className,
			Context context) {
		if (className == null || "".equals(className)) {
			return getObject(varName, bean, null, context, null);
		}
		//20130808注释LoaderManagerFactory
//		return getObject(varName, bean, LoaderManagerFactory.getManager()
//				.getClass(className), context, null);
		return getObject(varName, bean, getClazz(className), context, null);
	}

	public Object getObjectArray(String varName, String className,
			Context context) {
		//20130808注释LoaderManagerFactory
//		return buildArrayObjectWithObject(varName, LoaderManagerFactory
//				.getManager().getClass(className), context, null);
		return buildArrayObjectWithObject(varName, getClazz(className), context, null);
	}
	
	private Class<?> getClazz(String className){
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public Collection<Object> getObjectCollection(String varName,
			String collectionName, String className, Context context) {
		//20130808注释LoaderManagerFactory
//		Class<?> collectionClass = LoaderManagerFactory.getManager().getClass(
//				collectionName);
//		Class<?> clazz = LoaderManagerFactory.getManager().getClass(className);
		Class<?> collectionClass = getClazz(collectionName);
		Class<?> clazz = getClazz(className);
		Collection<Object> collection = (Collection<Object>) getObjectInstance(collectionClass);
		buildCollection(varName, collection, clazz, context, null);
		if (collection.size() == 0) {
			return null;
		}
		return collection;
	}

	private Object getObject(String varName, String bean, Class<?> clazz,
			Context context, String preName) {
		if (bean != null) {
			Object o = getInstanceBySpringBean(bean);
			if (o != null) { // 如果不为空则继续进行
				Object result = buildObject(varName, o, context, preName);
				if (result == null) {// 如果解析返回null(表明没有从cotext中获取任何属性，则返回bean)
					return o;
				} else {
					return result; // 否则返回解析结果
				}
			}
		}
		// 当通过beanid获取bean失败时，则按原有逻辑进行
		if (clazz == null || isSimpleType(clazz)) {
			return null;
		} else if (clazz.isArray()) {
			return buildArrayObjectWithArray(varName, clazz, context, preName);
		} else {
			Object o = getObjectInstance(clazz);
			return buildObject(varName, o, context, preName);
		}
	}

	private Object getPerpertyValue(String preName, String objName,
			String propertyName, Context context) {
		String reallyName = getReallyPropertyName(preName, objName,
				propertyName);
		return getPerpertyValue(reallyName, context);
	}

	private Object buildObject(String varName, Object object, Context context,
			String preName) {
		if (object == null) {
			return null;
		}
		Class<?> clazz = object.getClass();

		String objName = varName;
		// 20130424
		// 属性是否全部为空，若全部为空，则返回空对象
		boolean allPropertyNull = true;
		if (isNull(objName)) {
			objName = getObjName(object);
		}
		for (PropertyDescriptor descriptor : PropertyUtils
				.getPropertyDescriptors(object.getClass())) {
			if (descriptor.getPropertyType().equals(Class.class)) {
				continue;
			}
			//201402025修改此处代码，修改propertyName获取逻辑
			//String propertyName = getPropertyName(clazz, descriptor.getName());
			String propertyName = descriptor.getName();
			
			Object propertyValue = getPerpertyValue(preName, objName,
					propertyName, context);// user.name,user_name,name
			if (propertyValue != null) { // 如果值取出来为空，则跳过不处理了
				try {
					if (descriptor.getPropertyType().equals( // 如果类型相同，或者值类型继承(实现)属性类型
							propertyValue.getClass())
							|| implmentInterface(propertyValue.getClass(),
									descriptor.getPropertyType())) {
						BeanUtils.setProperty(object, descriptor.getName(),
								propertyValue);
						allPropertyNull = false;
						continue;
					} else if (isSimpleType(descriptor.getPropertyType())) {// 若是简单类型
						if (isSimpleType(propertyValue.getClass())) { // 若值也是简单类型则赋值，非简单类型，则不处理
							BeanUtils.setProperty(object, descriptor.getName(),
									propertyValue.toString());
							allPropertyNull = false;
						} else {
							logger.logMessage(LogLevel.WARN,
									"参数{0}.{1}赋值失败,期望类型{3},实际类型{4}", objName,
									propertyName, descriptor.getPropertyType(),
									propertyValue.getClass());
						}
						continue;
					}
					// else {
					// }
					// 以上处理均未进入，则该类型为其他类型，需要进行递归
				} catch (Exception e) {
					logger.errorMessage("为属性{0}赋值时出错", e, descriptor.getName());
				}
			}

			// 查找转换器，如果存在转换器，但是值为空，则跳出不处理
			// 若转换器不存在，则继续进行处理
			TypeConverter typeConverter = getTypeConverter(descriptor
					.getPropertyType());
			if (typeConverter != null) {
				if (propertyValue != null) {
					try {
						BeanUtils.setProperty(object, descriptor.getName(),
								typeConverter.getObject(propertyValue));
						allPropertyNull = false;
					} catch (Exception e) {
						logger.errorMessage("为属性{0}赋值时出错", e,
								descriptor.getName());
					}
				}
				continue;
			}
			// 对象值为空，且转换器不存在
			if (!isSimpleType(descriptor.getPropertyType())) {
				// 如果是共它类型则递归调用
				try {
					String newPreName = getReallyPropertyName(preName, objName,
							propertyName);
					Class<?> type = clazz
							.getDeclaredField(descriptor.getName()).getType();
					if (type.isArray()) {// 如果是数组
						Object value = getObject(null, null,
								descriptor.getPropertyType(), context,
								newPreName);
						if (value != null) {
							BeanUtils.setProperty(object, descriptor.getName(),
									value);
							allPropertyNull = false;
						}
					} else if (implmentInterface(descriptor.getPropertyType(),
							Collection.class)) {// 如果是集合
						ParameterizedType pt = (ParameterizedType) clazz
								.getDeclaredField(descriptor.getName())
								.getGenericType();
						Type[] actualTypeArguments = pt
								.getActualTypeArguments();
						Collection<Object> collection = (Collection<Object>) getObjectInstance(type);
						buildCollection(null, collection,
								(Class) actualTypeArguments[0], context,
								newPreName);
						if (collection.size() != 0) {
							BeanUtils.setProperty(object, descriptor.getName(),
									collection);
							allPropertyNull = false;
						}
					} else {// 如果是其他类型
						Object value = getObject(null, null,
								descriptor.getPropertyType(), context,
								newPreName);
						if (value != null) {
							BeanUtils.setProperty(object, descriptor.getName(),
									value);
							allPropertyNull = false;
						}
					}
				} catch (Exception e) {
					logger.errorMessage("为属性{0}赋值时出错", e, descriptor.getName());
				}
			}

		}
		if (allPropertyNull) {
			return null;
		}
		return object;
	}

	private void buildCollection(String varName, Collection<Object> collection,
			Class<?> clazz, Context context, String preName) {
		if (clazz == null) {
			return;
		} else if (isSimpleType(clazz)) {
			buildCollectionSimple(varName, collection, clazz, context, preName);
		} else {
			buildCollectionObject(varName, collection, clazz, context, preName);
		}
	}

	private void buildCollectionSimple(String varName,
			Collection<Object> collection, Class<?> clazz, Context context,
			String preName) {
		if (clazz == null) {
			return;
		}
		String reallyVarName = varName;
		if (isNull(reallyVarName)) {
			reallyVarName = preName;
		}
		if (isNull(reallyVarName)) {
			throw new RuntimeException("简单类型数组或集合,变量名不可为空");
		}

		Object propertyValue = getPerpertyValue(reallyVarName, context);
		if (propertyValue != null) {
			if (propertyValue.getClass().isArray()) {
				// 如果是数组
				Object[] objArray = (Object[]) propertyValue;
				for (Object o : objArray) {
					collection.add(o);
				}
			} else {
				collection.add(propertyValue);
			}
		}
		// return collection;
	}

	private void buildCollectionObject(String varName,
			Collection<Object> collection, Class<?> clazz, Context context,
			String preName) {
		if (clazz == null) {
			return;
		}
		Object object = getObjectInstance(clazz);
		String objName = varName;
		if (isNull(objName)) {
			//20130806修改变量名为首字母小写
			objName = getObjName(object);
		}
		Map<String, Object> valueMap = new HashMap<String, Object>();
		Class<?> reallyType = object.getClass();
		int size = -1;
		// 先计算出collection的size
		for (PropertyDescriptor descriptor : PropertyUtils
				.getPropertyDescriptors(reallyType)) {
			if (descriptor.getPropertyType().equals(Class.class)) {
				continue;
			}
			if (size != -1) {
				break;
			}
			//201402025修改此处代码，修改propertyName获取逻辑
//			String propertyName = getPropertyName(reallyType, descriptor.getName());
			String propertyName = descriptor.getName();
			
			Object propertyValue = getPerpertyValue(preName, objName,
					propertyName, context);
			if (propertyValue != null) {
				valueMap.put(propertyName, propertyValue);
				if (propertyValue.getClass().isArray()) {
					// 如果是数组
					Object[] objArray = (Object[]) propertyValue;
					if (objArray.length > size) {
						size = objArray.length;
					}
				} else if (size == -1) {
					size = 1;
				}
			}
		}
		// 20130424
		// 说明集合完全没有相关的值
		// 返回空集合
		if (size == -1) {
			return;
		}
		// 初始化objecList的数据
		List<Object> objecList = new ArrayList<Object>();
		for (int i = 0; i < size; i++) {
			Object o = getObjectInstance(clazz);
			objecList.add(o);
		}

		for (PropertyDescriptor descriptor : PropertyUtils
				.getPropertyDescriptors(reallyType)) {
			if (descriptor.getPropertyType().equals(Class.class)) {
				continue;
			}
			//201402025修改此处代码，修改propertyName获取逻辑
//			String propertyName = getPropertyName(reallyType, descriptor.getName());
			String propertyName = descriptor.getName();
			Object propertyValue = getPerpertyValue(preName, objName,
					propertyName, context);
			if (propertyValue == null) {
				continue;
			}
			try {
				if (size == 1) {
					BeanUtils.setProperty(objecList.get(0),
							descriptor.getName(), propertyValue);
				} else {
					Object[] objArray = (Object[]) propertyValue;
					for (int i = 0; i < size; i++) {
						BeanUtils.setProperty(objecList.get(i),
								descriptor.getName(), objArray[i]);
					}
				}
				continue;
			} catch (Exception e) {
				logger.errorMessage("为属性{0}赋值时出错", e, descriptor.getName());
			}
		}
		// objecList的数据放入collection
		for (Object o : objecList) {
			collection.add(o);
		}
		// return collection;
	}

	private Object buildArrayObjectWithArray(String varName,
			Class<?> arrayClass, Context context, String preName) {
		return buildArrayObjectWithObject(varName,
				arrayClass.getComponentType(), context, preName);
	}

	private Object buildArrayObjectWithObject(String varName,
			Class<?> objectClass, Context context, String preName) {
		if (objectClass == null) {
			return null;
		}
		Collection<Object> collection = new ArrayList<Object>();
		buildCollection(varName, collection, objectClass, context, preName);
		if (collection.size() == 0) {
			return null;
		} else {
			Object array = Array.newInstance(objectClass, collection.size());
			Iterator<Object> iterator = collection.iterator();
			Object[] objectsArray = (Object[]) array;
			for (int i = 0; i < collection.size(); i++) {
				objectsArray[i] = iterator.next();
			}
			return array;
		}
	}

	private TypeConverter<?, ?> getTypeConverter(Class<?> destType,
			Class<? extends Object> sourceType) {
		for (TypeConverter<?, ?> typeConverter : typeConverterList) {
			if (typeConverter.getSourceType().equals(sourceType)
					&& typeConverter.getDestinationType().equals(destType)) {
				return typeConverter;
			}
		}
		return null;
	}

	private TypeConverter<?, ?> getTypeConverter(Class<?> destType) {
		for (TypeConverter<?, ?> typeConverter : typeConverterList) {
			if (typeConverter.getDestinationType().equals(destType)) {
				return typeConverter;
			}
		}
		return null;
	}

	boolean isSimpleType(Class<?> clazz) {
		if (clazz.equals(int.class) || clazz.equals(char.class)
				|| clazz.equals(byte.class) || clazz.equals(boolean.class)
				|| clazz.equals(short.class) || clazz.equals(long.class)
				|| clazz.equals(double.class) || clazz.equals(float.class)) {
			return true;
		}
		if (clazz.equals(Integer.class) || clazz.equals(Character.class)
				|| clazz.equals(Byte.class) || clazz.equals(Boolean.class)
				|| clazz.equals(Short.class) || clazz.equals(Long.class)
				|| clazz.equals(Double.class) || clazz.equals(Float.class)
				|| clazz.equals(String.class)) {
			return true;
		}
		return false;
	}
	//201402025注释此函数
//	private String getPropertyName(Class<?> clazz, String name) {
//		try {
//			Field[] fileds = clazz.getDeclaredFields();
//			
//			Annotation parameterNameAnnotation = clazz.getDeclaredField(name)
//					.getAnnotation(ParameterName.class);
//			if (parameterNameAnnotation != null) {
//				Object[] args = null;
//				Class<?>[] argsType = null;
//				return parameterNameAnnotation.annotationType()
//						.getDeclaredMethod("name", argsType)
//						.invoke(parameterNameAnnotation, args).toString();
//			}
//		} catch (Exception e) {
//			logger.errorMessage("获取属性名重名名时失败", e);
//		}
//		return name;
//	}

	/**
	 * 根据clazz获取对象 先从creator中获取，若找不到，则去springbean中获取
	 * 
	 * @param clazz
	 * @return
	 */
	private Object getObjectInstance(Class<?> clazz) {
		Object o = getIntanceByCreator(clazz);
		if (o != null) {
			return o;
		}
		return getInstanceBySpringBean(clazz);
	}

	private Object getInstanceBySpringBean(String bean) {
		if (bean == null || "".equals(bean)) {
			return null;
		}
		try {
			return SpringUtil.getBean(bean);
		} catch (Exception e) {
			logger.logMessage(LogLevel.WARN, e.getMessage());
			return null;
		}

	}

	private Object getInstanceBySpringBean(Class<?> clazz) {
		if (clazz == null) {
			return null;
		}
		try {
			return SpringUtil.getBean(clazz);
		} catch (Exception e) {
			logger.logMessage(LogLevel.WARN, e.getMessage());
			try {
				return clazz.newInstance();
			} catch (Exception e1) {
				throw new RuntimeException(e1);
			}
		}
	}

	/**
	 * 根据clazz从creators中获取其实例
	 * 
	 * @param clazz
	 * @return 若找到则返回对象实例，否则返回null
	 */
	private Object getIntanceByCreator(Class<?> clazz) {
		for (TypeCreator<?> creator : typeCreatorList) {
			if (clazz.equals(creator.getType())) { // clazz是否继承自getClass
				return creator.getInstance();
			}
		}
		return null;
	}

	/**
	 * 判断clazz是否实现了interfaceClazz
	 * 
	 * @param clazz
	 * @param interfaceClazz
	 * @return
	 */
	private boolean implmentInterface(Class<?> clazz, Class<?> interfaceClazz) {
		return interfaceClazz.isAssignableFrom(clazz);
	}

	public void addTypeConverter(TypeConverter<?, ?> typeConverter) {
		typeConverterList.add(typeConverter);
	}
	
	public void removeTypeConverter(TypeConverter<?, ?> typeConverter) {
		typeConverterList.remove(typeConverter);
	}

	public void addTypeCreator(TypeCreator<?> typeCreator) {
		typeCreatorList.add(typeCreator);

	}
	
	public void removeTypeCreator(TypeCreator<?> typeCreator) {
		typeCreatorList.remove(typeCreator);

	}

	/**
	 * @param preName
	 * @param objName
	 * @param propertyName
	 * @return
	 */
	public String getReallyPropertyName(String preName, String objName,
			String propertyName) {
		if (preName == null || "".equals(preName)) {
			return String.format("%s.%s", objName, propertyName);
		}
		return String.format("%s.%s", preName, propertyName);
	}

	private Object getPerpertyValue(String reallyName, Context context) {
		Object value = context.get(reallyName);
		if (value == null) {
			value = context.get(reallyName.replace(".", "_"));
		}
		if (value == null) {
			int index = reallyName.indexOf('.') + 1;
			if (index != 0) {
				value = getPerpertyValue(reallyName.substring(index), context);
			}
		}
		return value;
	}

	private boolean isNull(String str) {
		if (str == null || "".equals(str.trim())) {
			return true;
		}
		return false;
	}
	
	private String getObjName(Object object){
		String className =  object.getClass().getSimpleName();
		if(className.length()==1)
			return className.toLowerCase();
		return className.substring(0,1).toLowerCase() + className.substring(1);
	}

}
