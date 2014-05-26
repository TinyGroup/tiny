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
package org.tinygroup.channel;

import org.tinygroup.cepcore.EventProcessor;
import org.tinygroup.event.Event;

/**
 * 通道接口
 * @author luoguo
 *
 */
public interface ChannelInterface extends EventProcessor {
	/**
	 * 发送事件
	 * @param event
	 */
	void sendEvent(Event event);

	/**
	 * 添加发送事件监听器
	 * @param eventListener
	 */
	void addSendEventListener(EventListener eventListener);

	/**
	 * 添加发送事件过滤器
	 * @param eventFilter
	 */
	void addSendEventFilter(EventFilter eventFilter);

	/**
	 * 添加接收事件过滤器
	 * @param eventFilter
	 */
	void addReceiveEventFilter(EventFilter eventFilter);

	/**
	 * 添加接收事件监听器
	 * @param eventListener
	 */
	void addReceiveEventListener(EventListener eventListener);

	String getId();

	/**
	 * 开始通道
	 */
	void start();

	/**
	 * 通道停止
	 */
	void stop();
}
