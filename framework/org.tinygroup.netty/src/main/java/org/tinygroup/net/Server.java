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

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 服务抽象类
 */
public abstract class Server implements Netty {
	private static final Logger logger = LoggerFactory.getLogger(Server.class);
	ServerBootstrap bootstrap;
	private final int port;
	Channel channel;

	/**
	 * 构造方法
	 * 
	 * @param port
	 */
	public Server(int port) {
		this.port = port;
	}

	/**
	 * 停止方法
	 */
	public void stop() {
		logger.logMessage(LogLevel.INFO, "服务器正在停止中，端口:{} ...", port);
		channel.close();
		bootstrap.shutdown();
		logger.logMessage(LogLevel.INFO, "服务器停止完毕，端口:{}。", port);
	}

	/**
	 * 运行方法
	 */
	public void run() {
		logger.logMessage(LogLevel.INFO, "服务器正在启动中，端口:{} ...", port);
		// 配置服务器
		bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool()));

		// 设置pipeline工厂.
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(getEncoder(), getDecoder(),
						getHandler());
			}
		});

		// 绑定并开始接口外部连接
		channel = bootstrap.bind(new InetSocketAddress(port));
		logger.logMessage(LogLevel.INFO, "服务器启动完毕，端口:{}。", port);

	}

}
