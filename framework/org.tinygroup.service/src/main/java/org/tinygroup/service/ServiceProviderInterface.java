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
package org.tinygroup.service;

import org.tinygroup.context.Context;
import org.tinygroup.service.exception.ServiceExecuteException;
import org.tinygroup.service.exception.ServiceNotExistException;
import org.tinygroup.service.registry.ServiceRegistry;
import org.tinygroup.service.registry.ServiceRegistryItem;

/**
 * 服务执行接口，每种服务容器都必须实现此接口
 * 
 * @author luoguo
 * 
 */
public interface ServiceProviderInterface {

	/**
	 * 设置服务注册表
	 * 
	 * @param serviceRegistry
	 */
	void setServiceRegistory(ServiceRegistry serviceRegistry);

	/**
	 * 返回服务注册表
	 */
	ServiceRegistry getServiceRegistory();

	/**
	 * 验证输入参数
	 * 
	 * @param service
	 * @return
	 * @throws ServiceNotExistException
	 */
	void validateInputParameter(Service service, Context context);

	/**
	 * 检查输出参数
	 * 
	 * @param service
	 */
	void validateOutputParameter(Service service, Context context);

	/**
	 * 执行服务
	 * 
	 * @param service
	 * @throws ServiceExecuteException
	 */
	void execute(Service service, Context context);

	/**
	 * 执行指定ID服务，并把服务结果放在环境当中
	 * 
	 * @param serviceId
	 * @param context
	 */
	void execute(String serviceId, Context context);


	Service getService(String serviceId);

	/**
	 * 注入参数
	 * 
	 * @param config
	 */
	<T> void setConfig(T config);

	ServiceRegistryItem getServiceRegistryItem(Service service);
}
