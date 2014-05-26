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
package org.tinygroup.net.test.p;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.tinygroup.net.coder.hessian.HessianDecoder;
import org.tinygroup.net.coder.hessian.HessianEncoder;

/**
 * Modification of {@link EchoClient} which utilizes Java object serialization.
 */
public class ObjectEchoClient {

	private final String host;
	private final int port;
	private final int firstMessageSize;

	public ObjectEchoClient(String host, int port, int firstMessageSize) {
		this.host = host;
		this.port = port;
		this.firstMessageSize = firstMessageSize;
	}

	public void run() {
		// Configure the client.
		ClientBootstrap bootstrap = new ClientBootstrap(
				new NioClientSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		// Set up the pipeline factory.
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(
						new HessianEncoder(),
						new HessianDecoder(),
						new ObjectEchoClientHandler(firstMessageSize));
			}
		});

		// Start the connection attempt.
		bootstrap.connect(new InetSocketAddress(host, port));
	}

	public static void main(String[] args) throws Exception {
		// Parse options.
		int port = 8899;
		int firstMessageSize = 1;

		new ObjectEchoClient("localhost", port, firstMessageSize).run();
	}
}
