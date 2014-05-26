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
package org.tinygroup.service.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.tinygroup.commons.beanutil.BeanUtil;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.event.Parameter;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.service.ServiceMappingManager;
import org.tinygroup.service.ServiceProxy;
import org.tinygroup.service.exception.ServiceLoadException;
import org.tinygroup.service.loader.AnnotationServiceLoader;
import org.tinygroup.service.registry.ServiceRegistry;
import org.tinygroup.service.registry.ServiceRegistryItem;
import org.tinygroup.springutil.SpringUtil;

public abstract class AbstractAnnotationServiceLoader implements
		AnnotationServiceLoader {
	private Logger logger = LoggerFactory
			.getLogger(AbstractAnnotationServiceLoader.class);

	private ServiceMappingManager serviceMappingManager;

	public ServiceMappingManager getServiceMappingManager() {
		return serviceMappingManager;
	}

	public void setServiceMappingManager(
			ServiceMappingManager serviceMappingManager) {
		this.serviceMappingManager = serviceMappingManager;
	}

	/**
	 * 载入服务
	 */
	public void loadService(ServiceRegistry serviceRegistry)
			throws ServiceLoadException {
		List<String> classNames = getClassNames();// 这个由子类提供
		for (String className : classNames) {
			try {
				logger.logMessage(LogLevel.INFO,
						"从{className}中查找ServiceAnnotation", className);
				Class<?> clazz = Class.forName(className);
				Annotation annotation = clazz
						.getAnnotation(ServiceComponent.class);
				if (annotation != null) {
					registerServices(clazz, annotation, serviceRegistry);
				} else {
					logger.logMessage(LogLevel.INFO,
							"{className}中无ServiceAnnotation", className);
				}
				logger.logMessage(LogLevel.INFO,
						"从{className}中查找ServiceAnnotation完成", className);

			} catch (Exception e) {
				logger.error("service.loadServiceException", e, className);
			}
		}
	}

	/**
	 * 
	 * 从AnnotationClassAction接入，新增注解服务
	 * 
	 * @param clazz
	 * @param annotation
	 * @param serviceRegistry
	 */
	public void loadService(Class<?> clazz, Annotation annotation,
			ServiceRegistry serviceRegistry) {
		String className = clazz.getName();
		logger.logMessage(LogLevel.INFO, "从{}中查找ServiceAnnotation", className);
		try {
			registerServices(clazz, annotation, serviceRegistry);
		} catch (Exception e) {
			logger.error("service.loadServiceException", e, className);
		}
	}

	public void removeService(ServiceRegistry serviceRegistry) {
		List<String> classNames = getClassNames();// 这个由子类提供
		for (String className : classNames) {
			try {
				Class<?> clazz = Class.forName(className);
				Annotation annotation = clazz
						.getAnnotation(ServiceComponent.class);
				if (annotation != null) {
					removeServices(clazz, serviceRegistry);
				}

			} catch (Exception e) {
				logger.log(LogLevel.ERROR, "service.loadServiceException",
						className);
			}
		}
	}

	private void removeServices(Class<?> clazz, ServiceRegistry serviceRegistry)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, InstantiationException {
		for (Method method : clazz.getMethods()) {
			Annotation annotation = method.getAnnotation(ServiceMethod.class);
			if (annotation != null) {
				String serviceId = getAnnotationStringValue(annotation,
						ServiceMethod.class, "serviceId");
				serviceRegistry.removeService(serviceId);
			}
		}

	}

	/**
	 * 注册所有服务服务
	 * 
	 * @param clazz
	 * @param annotation
	 * @param serviceRegistry
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws Exception
	 */
	private void registerServices(Class<?> clazz, Annotation annotation,
			ServiceRegistry serviceRegistry) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,
			InstantiationException {
		ServiceRegistryItem item = new ServiceRegistryItem();
		logger.logMessage(LogLevel.INFO, "读取ServiceComponent: {}",
				clazz.getName());
		registerServices(clazz, item, serviceRegistry);

	}

	/**
	 * 注册服务
	 * 
	 * @param clazz
	 * @param superItem
	 * @param serviceRegistry
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws Exception
	 */
	private void registerServices(Class<?> clazz,
			ServiceRegistryItem superItem, ServiceRegistry serviceRegistry)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, InstantiationException {
		for (Method method : clazz.getMethods()) {
			Annotation annotation = method.getAnnotation(ServiceMethod.class);
			if (annotation != null) {
				logger.logMessage(LogLevel.INFO, "开始加载方法{0}为服务",
						method.getName());
				ServiceRegistryItem item = new ServiceRegistryItem();
				// serviceId
				String serviceId = getAnnotationStringValue(annotation,
						ServiceMethod.class, "serviceId");
				if (StringUtil.isBlank(serviceId)) {
					serviceId = StringUtil.toCamelCase(clazz.getSimpleName()) + "." + StringUtil.toCamelCase(method.getName());
				}
				item.setServiceId(serviceId);
				// localName
				String localName = getAnnotationStringValue(annotation,
						ServiceMethod.class, "localName");
				item.setLocalName(localName);
				// description
				String description = getAnnotationStringValue(annotation,
						ServiceMethod.class, "description");
				item.setDescription(description);
				// cacheable
				boolean cacheable = Boolean
						.parseBoolean(getAnnotationStringValue(annotation,
								ServiceMethod.class, "cacheable"));
				item.setCacheable(cacheable);
				logger.logMessage(LogLevel.INFO, "方法对应服务serviceId:{serviceId}",
						serviceId);
				registerService(clazz, method, item, serviceRegistry);
				logger.logMessage(LogLevel.INFO, "加载方法{0}为服务完毕",
						method.getName());
				// 跳转信息servicemapping
				ServiceViewMapping serviceViewMapping = method
						.getAnnotation(ServiceViewMapping.class);
				if (serviceViewMapping != null) {
					org.tinygroup.service.config.ServiceViewMapping mapping = new org.tinygroup.service.config.ServiceViewMapping();
					mapping.setServiceId(serviceId);
					mapping.setPath(serviceViewMapping.value());
					serviceViewMapping.type();
					mapping.setType(StringUtil.defaultIfBlank(serviceViewMapping.type(), "forward"));
					serviceMappingManager.addServiceMapping(mapping);
				}
			}
		}
	}

	/**
	 * 注册服务
	 * 
	 * @param clazz
	 * @param method
	 * @param item
	 * @param serviceRegistry
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws Exception
	 */
	private void registerService(Class<?> clazz, Method method,
			ServiceRegistryItem item, ServiceRegistry serviceRegistry)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, InstantiationException {
		ServiceProxy serviceProxy = new ServiceProxy();
		serviceProxy.setObjectInstance(getServiceInstance(clazz));
		serviceProxy.setMethod(method);
		getInputParameterNames(item, method, serviceProxy);
		getOutputParameterNames(item, clazz, method, serviceProxy);

		item.setService(serviceProxy);
		serviceRegistry.registeService(item);

	}

	protected Object getServiceInstance(Class<?> clazz) {
		ServiceComponent serviceComponent = clazz.getAnnotation(ServiceComponent.class);
		if (StringUtil.isBlank(serviceComponent.bean())) {
			return SpringUtil.getBean(clazz);
		}
		return SpringUtil.getBean(serviceComponent.bean());
	}

	private void getOutputParameterNames(ServiceRegistryItem item,
			Class<?> clazz, Method method, ServiceProxy serviceProxy)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		logger.logMessage(LogLevel.INFO, "开始加载方法对应的服务出参,方法{0},服务:{1}",
				method.getName(), item.getServiceId());
		Class<?> parameterType = method.getReturnType();
		if(!Void.TYPE.equals(parameterType)){
			List<Parameter> outputParameterDescriptors = new ArrayList<Parameter>();
			Annotation annotation = method.getAnnotation(ServiceResult.class);
			Parameter descriptor = new Parameter();
			descriptor.setType(parameterType.getName());
			logger.logMessage(LogLevel.INFO, "服务出参type:{name}",
					descriptor.getType());
			descriptor.setArray(parameterType.isArray());
			if (annotation != null) {
				Boolean required = Boolean.valueOf(getAnnotationStringValue(
						annotation, ServiceResult.class, "required"));
				descriptor.setRequired(required);
				String name = getAnnotationStringValue(annotation,
						ServiceResult.class, "name");
				if (StringUtil.isBlank(name)) {
					name = StringUtil.toCamelCase(clazz.getSimpleName()) + "_"
							+ StringUtil.toCamelCase(method.getName()) + "_"
							+ "result";
				}
				descriptor.setName(name);
				logger.logMessage(LogLevel.INFO, "服务出参name:{name}", name);
			} else {
				logger.logMessage(LogLevel.INFO, "服务出参未配置");
			}
			serviceProxy.setOutputParameter(descriptor);
			outputParameterDescriptors.add(descriptor);
			item.setResults(outputParameterDescriptors);
		}
		logger.logMessage(LogLevel.INFO, "加载方法对应的服务出参完毕,方法{0},服务:{1}",
				method.getName(), item.getServiceId());

	}

	/**
	 * 把参数名称注册过来
	 * 
	 * @param method
	 * @return
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	private void getInputParameterNames(ServiceRegistryItem item,
			Method method, ServiceProxy serviceProxy)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		logger.logMessage(LogLevel.INFO, "开始加载方法对应的服务入参,方法{0},服务:{1}",
				method.getName(), item.getServiceId());
		String[] parameterNames = BeanUtil.getMethodParameterName(
				method.getDeclaringClass(), method);
		Annotation[][] annotations = method.getParameterAnnotations();
		Class<?>[] parameterTypes = method.getParameterTypes();
		List<Parameter> inputParameterDescriptors = new ArrayList<Parameter>();
		for (int i = 0; i < parameterTypes.length; i++) {
			Annotation annotation = getParameterAnnotation(annotations, i);
			Parameter descriptor = new Parameter();
			if (implmentInterface(parameterTypes[i], Collection.class)) {
				ParameterizedType pt = (ParameterizedType) (method
						.getGenericParameterTypes()[i]);
				Type[] actualTypeArguments = pt.getActualTypeArguments();
				Class<?> clazz = (Class<?>) actualTypeArguments[0];
				descriptor.setType(clazz.getName());
				descriptor.setCollectionType(parameterTypes[i].getName());
			} else {
				descriptor.setType(parameterTypes[i].getName());
			}
			descriptor.setArray(parameterTypes[i].isArray());
			if (annotation != null) {
				String name = getAnnotationStringValue(annotation,
						ServiceParameter.class, "name");
				if (name.length() == 0) {
					name = parameterNames[i];
				}
				descriptor.setName(name);
				boolean required = Boolean.valueOf(getAnnotationStringValue(
						annotation, ServiceParameter.class, "required"));
				descriptor.setRequired(required);
				String validatorSence = getAnnotationStringValue(annotation,
						ServiceParameter.class, "validatorSence");
				descriptor.setValidatorSence(validatorSence);
			} else {
				descriptor.setName(parameterNames[i]);
			}
			inputParameterDescriptors.add(descriptor);
		}
		item.setParameters(inputParameterDescriptors);
		serviceProxy.setInputParameters(inputParameterDescriptors);
		logger.logMessage(LogLevel.INFO, "加载方法对应的服务入参完毕,方法{0},服务:{1}",
				method.getName(), item.getServiceId());

	}

	private boolean implmentInterface(Class<?> clazz, Class<?> interfaceClazz) {
		return interfaceClazz.isAssignableFrom(clazz);
	}

	Annotation getParameterAnnotation(Annotation[][] annotations, int index) {
		for (int i = 0; i < annotations[index].length; i++) {
			if (annotations[index][i].annotationType().equals(
					ServiceParameter.class)) {
				return annotations[index][i];
			}
		}
		return null;
	}

	private String getAnnotationStringValue(Annotation annotation,
			Class<?> annotationClazz, String name)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		Object[] args = null;
		Class<?>[] argsType = null;
		return annotationClazz.getMethod(name, argsType)
				.invoke(annotation, args).toString();
	}

	protected abstract List<String> getClassNames();

}
