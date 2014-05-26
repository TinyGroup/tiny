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
package org.tinygroup.net;

import org.jboss.netty.channel.ChannelHandler;

/**
 * Netty通信框架扩展接口
 * 
 * @author luoguo
 * 
 */
public interface Netty {
	/**
	 * 编码器<br>
	 * 在发送之前，对数据进行编码
	 * 
	 * @return
	 */
	ChannelHandler getEncoder();

	/**
	 * 解码器<br>
	 * 用于对对方传过来的数据进行解码
	 * 
	 * @return
	 */
	ChannelHandler getDecoder();

	/**
	 * 对发送过来的数据报文进行处理
	 * 
	 * @return
	 */
	ChannelHandler getHandler();

}
