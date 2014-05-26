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
package org.tinygroup.cepcore;

import org.tinygroup.event.Event;

/**
 * 服务路由策略
 * 
 * @author luog
 * 
 */
public interface ServiceRouteStrategy {
	/**
	 * 根据路由策略，返回响应服务的事件处理器
	 * 
	 * @param serviceRepository
	 * @return
	 */
	EventProcessor getServiceRepository(Event event,
			ServiceRepository serviceRepository);
}
