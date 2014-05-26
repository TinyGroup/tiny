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
package org.tinygroup.service.registry;

import org.tinygroup.service.Service;
import org.tinygroup.service.exception.NotComparableServiceException;

/**
 * 服务比较接口，对仅版本不同的服务进行比较
 *
 * @author luoguo
 *
 */
public interface ServiceCompare<T extends Service> {
	/**
	 * 比较两个服务的版本
	 *
	 * @param source
	 * @param dest
	 * @return
	 * @exception NotComparableServiceException
	 *                当两个服务不可比较时，抛出此异常
	 */
	int compare(ServiceRegistryItem source, ServiceRegistryItem dest)
			throws NotComparableServiceException;

}
