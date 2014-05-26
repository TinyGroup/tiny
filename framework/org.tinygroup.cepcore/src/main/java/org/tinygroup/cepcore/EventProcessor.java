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

import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceInfo;

/**
 * 事件处理器 ，每个事件处理器，都需要提供其ID，路由表及处理器
 * @author luoguo
 *
 */
public interface EventProcessor {
	int TYPE_CHANNEL = 1;
	int TYPE_LOGICAL = 2;

	/**
	 * 处理事件
	 * @param event
	 */
	void process(Event event);

	/**
	 * 设置CEPCore
	 * @param cepCore
	 */
	void setCepCore(CEPCore cepCore);

	List<ServiceInfo> getServiceInfos();
	/**
	 * 返回处理器ID，ID必须唯一
	 * @return
	 */
	String getId();

	/**
	 * 返回处理器类型
	 * @return
	 */
	int getType();
}
