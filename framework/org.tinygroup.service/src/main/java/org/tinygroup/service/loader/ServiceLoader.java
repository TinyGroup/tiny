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
package org.tinygroup.service.loader;

import org.tinygroup.service.exception.ServiceLoadException;
import org.tinygroup.service.registry.ServiceRegistry;

/**
 * 服务加载接口，用于加载某种类型的服务
 * 
 * @author luoguo
 * 
 */
public interface ServiceLoader {

	/**
	 * 输入服务到注册表中
	 * 
	 * @param serviceRegistry
	 */
	void loadService(ServiceRegistry serviceRegistry)
			throws ServiceLoadException;

	void setConfigPath(String path);

	void removeService(ServiceRegistry serviceRegistry);
}
