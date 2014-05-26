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
package org.tinygroup.rpc;

import java.rmi.Naming;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.tinygroup.cepcore.exception.CEPConnectException;
import org.tinygroup.event.central.Node;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.rpc.util.RMIRemoteUtil;

public class RMIFactory extends BasePoolableObjectFactory {
	private static Logger logger = LoggerFactory.getLogger(RMIFactory.class);
	private Node node;

	public RMIFactory(Node node) {
		this.node = node;
	}

	
	public Object makeObject() throws Exception {
		String url = RMIRemoteUtil.getURL(node);
		CEPCoreRMI rmi = null;
		try {
			rmi = (CEPCoreRMI) Naming.lookup(url);
		} catch (Exception e) {
			logger.logMessage(LogLevel.ERROR, "获取连接失败,目标节点{0}:{1}:{2},{3}",
					node.getIp(), node.getPort(), node.getNodeName(),
					e.getMessage());
			throw new CEPConnectException(e, node);
		}
		return rmi;
	}

}
