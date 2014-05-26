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
import org.tinygroup.event.ServiceInfo;
import org.tinygroup.event.central.Node;

public interface CEPCoreNodeOperation{
	/**
	 * 向远程节点发起请求调用
	 * @param event
	 * @param remoteNode
	 * @return 请求处理完成返回的event
	 */
	Event remoteprocess(Event event,Node remoteNode);
	
	/**
	 * 启动cepcore
	 * @param cep
	 */
	void startCEPCore(CEPCore cep);
	/**
	 * 关闭cepcore
	 * @param cep
	 */
	void stopCEPCore(CEPCore cep);
	/**
	 * 根据服务id、name查询服务信息
	 * 在cepcore中,该方法能查询到本地及远程的服务信息,若查询不到,则抛异常
	 * 在sc的manager实现中,该方法将抛出不支持的异常
	 * 在as/ar的manager实现中,该方法将从远程服务信息中查询，若查询不到，则抛异常
	 * @param serviceId
	 * @param serviceName
	 * @return
	 */
	ServiceInfo getServiceInfo(String serviceId);
	
}
