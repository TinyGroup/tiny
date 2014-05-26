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

import java.util.List;
import java.util.Set;

import org.tinygroup.event.ServiceInfo;

/**
 * 服务仓库
 * 
 * @author luog
 * 
 */
public interface ServiceRepository {
	/**
	 * 添加事件处理器
	 * 
	 * @param eventProcessor
	 */
	void addEventProcessor(EventProcessor eventProcessor);

	/**
	 * 返回指定名称的事件处理器
	 * 
	 * @param nodeName
	 * @return
	 */
	EventProcessor getEventProcessor(String nodeName);

	/**
	 * 删除指定事件处理器
	 * 
	 * @param nodeName
	 */
	void removeEventProcessor(String nodeName);

	/**
	 * 删除事件处理器
	 * 
	 * @param eventProcessor
	 */
	void removeEventProcessor(EventProcessor eventProcessor);

	/**
	 * 返回指定服务所在的事件处理器列表
	 * 
	 * @param serviceId
	 * @return
	 */
	List<EventProcessor> getEventProcessorList(String serviceId);

	/**
	 * 返回所有事件处理器列表
	 * 
	 * @return
	 */
	List<EventProcessor> getEventProcessorList();

	/**
	 * 返回指定服务的本地事件处理器列表
	 * 
	 * @param serviceId
	 * @return
	 */
	List<EventProcessor> getLocalEventProcessorList(String serviceId);

	/**
	 * 返回所有本地事件处理器列表
	 * 
	 * @return
	 */
	List<EventProcessor> getLocalEventProcessorList();

	/**
	 * 返回指定服务的远程事件处理器列表
	 * 
	 * @param serviceId
	 * @return
	 */
	List<EventProcessor> getRemoteEventProcessorList(String serviceId);

	/**
	 * 返回所有远程事件处理器列表
	 * 
	 * @return
	 */
	List<EventProcessor> getRemoteEventProcessorList();

	/**
	 * 返回事件处理器列表中的可以提供的服务集
	 * 
	 * @param eventProcessors
	 * @return
	 */
	Set<ServiceInfo> getServiceInfoList(List<EventProcessor> eventProcessors);

}
