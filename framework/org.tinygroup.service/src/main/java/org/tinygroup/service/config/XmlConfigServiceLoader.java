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
package org.tinygroup.service.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.tinygroup.config.Configuration;
import org.tinygroup.event.Parameter;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.service.ServiceProxy;
import org.tinygroup.service.exception.ServiceLoadException;
import org.tinygroup.service.loader.ServiceLoader;
import org.tinygroup.service.registry.ServiceRegistry;
import org.tinygroup.service.registry.ServiceRegistryItem;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xmlparser.node.XmlNode;

public abstract class XmlConfigServiceLoader extends AbstractFileProcessor implements ServiceLoader,
		Configuration {
	private Logger logger = LoggerFactory
			.getLogger(XmlConfigServiceLoader.class);
	private XmlNode applicationConfig;
	private XmlNode componentConfig;

	/**
	 * 载入服务
	 */
	public void loadService(ServiceRegistry serviceRegistry)
			throws ServiceLoadException {
		List<ServiceComponents> list = getServiceComponents();// 这个由子类提供
		for (ServiceComponents serviceComponents : list) {
			loadService(serviceRegistry, serviceComponents);
		}
	}

	public void removeService(ServiceRegistry serviceRegistry) {
		List<ServiceComponents> list = getServiceComponents();
		for (ServiceComponents serviceComponents : list) {
			removeServiceComponents(serviceRegistry, serviceComponents);
		}
	}

	private void removeServiceComponents(ServiceRegistry serviceRegistry,
			ServiceComponents serviceComponents) {
		for (ServiceComponent component : serviceComponents
				.getServiceComponents()) {
			for (ServiceMethod method : component.getServiceMethods()) {
				serviceRegistry.removeService(method.getServiceId());
			}

		}
	}

	private void loadService(ServiceRegistry serviceRegistry,
			ServiceComponents serviceComponents) throws ServiceLoadException {
		for (ServiceComponent serviceComponent : serviceComponents
				.getServiceComponents()) {
			try {

				Object object = getServiceInstance(serviceComponent);
				registerServices(object, serviceComponent, serviceRegistry);

			} catch (Exception e) {
				logger.errorMessage("实例化ServiceComponent时出错,类名:", e,
						serviceComponent.getType());
			}
		}
	}

	/**
	 * 注册所有服务服务
	 * 
	 * @param object
	 * @param serviceComponent
	 * @param serviceRegistry
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws Exception
	 */
	private void registerServices(Object object,
			ServiceComponent serviceComponent, ServiceRegistry serviceRegistry)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, InstantiationException {
		ServiceRegistryItem item = new ServiceRegistryItem();
		registerServices(object, serviceComponent, item, serviceRegistry);

	}

	/**
	 * 注册服务
	 * 
	 * @param object
	 * @param serviceComponent
	 * @param serviceRegistry
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws Exception
	 */
	private void registerServices(Object object,
			ServiceComponent serviceComponent, ServiceRegistryItem superItem,
			ServiceRegistry serviceRegistry) throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,
			InstantiationException {
		for (ServiceMethod serviceMethod : serviceComponent.getServiceMethods()) {
			ServiceRegistryItem item = new ServiceRegistryItem();
			item.setServiceId(serviceMethod.getServiceId());
			item.setLocalName(serviceMethod.getLocalName());
			item.setDescription(serviceMethod.getDescription());
			item.setCacheable(serviceMethod.isCacheable());
			registerService(object, serviceMethod, item, serviceRegistry);
		}
	}

	/**
	 * 注册服务
	 * 
	 * @param object
	 * @param serviceMethod
	 * @param item
	 * @param serviceRegistry
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws Exception
	 */
	private void registerService(Object object, ServiceMethod serviceMethod,
			ServiceRegistryItem item, ServiceRegistry serviceRegistry)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, InstantiationException {
		ServiceProxy serviceProxy = new ServiceProxy();
		serviceProxy.setMethodName(serviceMethod.getMethodName());
		serviceProxy.setObjectInstance(object);
		getInputParameterNames(item, serviceMethod, serviceProxy);
		getOutputParameterNames(item, serviceMethod, serviceProxy);
		item.setService(serviceProxy);
		serviceRegistry.registeService(item);

	}

	/**
	 * 把参数名称注册过来
	 * 
	 * @param item
     * @param serviceMethod
     * @param serviceProxy
	 * @return
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	private void getInputParameterNames(ServiceRegistryItem item,
			ServiceMethod serviceMethod, ServiceProxy serviceProxy)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		List<Parameter> inputParameterList = new ArrayList<Parameter>();
		List<Parameter> inputParameterDescriptors = new ArrayList<Parameter>();
		// ==================入参处理 begin========================
		for (ServiceParameter serviceParameter : serviceMethod
				.getServiceParameters()) {
			Parameter descriptor = new Parameter();
			inputParameterList.add(descriptor);
			descriptor.setType(serviceParameter.getType());
			descriptor.setArray(serviceParameter.isArray());
			descriptor.setName(serviceParameter.getName());
			descriptor.setRequired(serviceParameter.isRequired());
			descriptor.setValidatorSence(serviceParameter.getValidatorScene());
			descriptor.setCollectionType(serviceParameter.getCollectionType());
			inputParameterDescriptors.add(descriptor);
		}
		// ==================入参处理 end========================
		item.setParameters(inputParameterDescriptors);
		serviceProxy.setInputParameters(inputParameterList);
	}

	private void getOutputParameterNames(ServiceRegistryItem item,
			ServiceMethod serviceMethod, ServiceProxy serviceProxy)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		// ==================出参处理 begin========================
		if (serviceMethod.getServiceResult() != null) {
			ServiceResult serviceResult = serviceMethod.getServiceResult();
			Parameter descriptor = new Parameter();
			descriptor.setType(serviceResult.getType());
			descriptor.setArray(serviceResult.isArray());
			descriptor.setRequired(serviceResult.isRequired());
			descriptor.setName(serviceResult.getName());
			serviceProxy.setOutputParameter(descriptor);
			List<Parameter> outputParameterDescriptors = new ArrayList<Parameter>();
			outputParameterDescriptors.add(descriptor);
			item.setResults(outputParameterDescriptors);
		}
		// ==================出参处理 end========================
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

	protected abstract List<ServiceComponents> getServiceComponents();

	protected abstract Object getServiceInstance(
			ServiceComponent serviceComponent) throws Exception;

	public String getApplicationNodePath() {
		return null;
	}

	public String getComponentConfigPath() {
		return null;
	}

	public void config(XmlNode applicationConfig, XmlNode componentConfig) {
		this.applicationConfig = applicationConfig;
		this.componentConfig = componentConfig;
	}

	public XmlNode getComponentConfig() {
		return componentConfig;
	}

	public XmlNode getApplicationConfig() {
		return applicationConfig;
	}
	
	public boolean isMatch(FileObject fileObject) {
		return false;
	}

	public void process() {
		
	}
}
