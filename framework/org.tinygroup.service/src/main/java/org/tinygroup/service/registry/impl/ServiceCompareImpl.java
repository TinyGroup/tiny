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
package org.tinygroup.service.registry.impl;

import org.tinygroup.service.Service;
import org.tinygroup.service.exception.NotComparableServiceException;
import org.tinygroup.service.registry.ServiceCompare;
import org.tinygroup.service.registry.ServiceRegistryItem;

/**
 * 比较两个服务的大小
 *
 * @author luoguo
 *
 * @param <T>
 */
public class ServiceCompareImpl<T extends Service> implements
		ServiceCompare<Service> {

	public int compare(ServiceRegistryItem source,
			ServiceRegistryItem dest)
			throws NotComparableServiceException {
		boolean isSameServiceId = isSame(source.getServiceId(),
				dest.getServiceId());
		if (isSameServiceId) {
			return 0;
		}
		throw new NotComparableServiceException();
	}

	private boolean isSame(String source, String dest) {
		return source.equals(dest);
	}
}
