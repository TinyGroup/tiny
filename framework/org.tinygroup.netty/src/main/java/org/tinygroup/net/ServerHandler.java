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

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 服务器端响应抽象类
 */
public abstract class ServerHandler extends SimpleChannelUpstreamHandler {

	private static final Logger logger = LoggerFactory
			.getLogger(ServerHandler.class);

	protected Server server;

	
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		processObject(e.getMessage(), ctx);
	}

	/**
	 * 处理报文
	 * 
	 * @param message
	 *            报文
	 * @param ctx
	 *            上下文
	 */
	protected abstract void processObject(Object message,
			ChannelHandlerContext ctx);

	
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		logger.errorMessage("发生错误", e.getCause());
		e.getChannel().close();
		e.getChannel();
	}

	public void setServer(Server server) {
		this.server = server;
	}

}
