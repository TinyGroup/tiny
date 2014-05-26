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

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelState;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 客户端响应抽象类
 */
public abstract class ClientHandler extends SimpleChannelUpstreamHandler {

	private static final Logger logger = LoggerFactory
			.getLogger(ClientHandler.class.getName());

	protected Client client;

	/**
	 * Creates a client-side handler.
	 */
	public ClientHandler() {
	}

	
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)
			throws Exception {
		if (e instanceof ChannelStateEvent
				&& ((ChannelStateEvent) e).getState() != ChannelState.INTEREST_OPS) {
			logger.logMessage(LogLevel.ERROR, e.toString());
		}
		super.handleUpstream(ctx, e);
	}

	
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		// Send the first message if this handler is a client-side handler.
		client.setReady(true);
		client.setChannelHandlerContext(ctx);
	}

	
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		processObject(e.getMessage(), ctx);
	}

	/**
	 * 定义子类对报文进行处理的方法
	 * 
	 * @param message 报文对象
	 * @param ctx 上下文
	 */
	protected abstract void processObject(Object message,
			ChannelHandlerContext ctx);

	
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		client.setReady(false);
		logger.logMessage(LogLevel.ERROR, "发生错误：{}，通道关闭", e.getCause()
				.getMessage());
		e.getChannel().close();
	}

	public void setClient(Client client) {
		this.client = client;
	}

}
