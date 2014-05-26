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
package org.tinygroup.net.test.impl;

import org.jboss.netty.channel.ChannelHandler;
import org.tinygroup.net.Server;
import org.tinygroup.net.coder.hessian.HessianDecoder;
import org.tinygroup.net.coder.hessian.HessianEncoder;

public class TestServer extends Server{

	public TestServer(int port) {
		super(port);
	}

	public ChannelHandler getEncoder() {
		return new HessianEncoder();
	}

	public ChannelHandler getDecoder() {
		return new HessianDecoder();
	}

	public ChannelHandler getHandler() {
		return null;
	}

}
