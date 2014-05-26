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
package org.tinygroup.cepcore.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.CEPCoreRemoteInterface;
import org.tinygroup.cepcore.CEPCoreNodeManager;
import org.tinygroup.cepcore.exception.CEPRunException;
import org.tinygroup.cepcore.util.CEPCoreUtil;
import org.tinygroup.context.Context;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceRequest;
import org.tinygroup.event.central.Node;
import org.tinygroup.parser.filter.NameFilter;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.xmlparser.node.XmlNode;

public abstract class AbstractCEPCoreOp implements CEPCoreNodeManager {

	private int weight = Node.DEFAULT_WEIGHT;
	private CEPCoreRemoteInterface remoteImpl;

	public void setWeight(int weight) {
		this.weight = weight;
	}

	private String nodeName;

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	private String port;

	public void setPort(String port) {
		this.port = port;
	}

	private String ip;

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setConfig(XmlNode xml) {
		if (xml == null) {
			return;
		}
		NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(xml);

		XmlNode remoteConfig = nameFilter.findNode("remote-bean");
		if (remoteConfig != null) {
			String beanId = remoteConfig.getAttribute("bean");
			if (!CEPCoreUtil.isNull(beanId)) {
				this.remoteImpl = SpringUtil.getBean(beanId);
			} else {
				throw new CEPRunException("cepcore.noRemoteBean");
			}
		} else {
			throw new CEPRunException("cepcore.noRemoteBean");
		}

		XmlNode nodeConfig = nameFilter.findNode("node-config");
		if (nodeConfig != null) {
			setNodeName(nodeConfig.getAttribute("name"));
			setPort(nodeConfig.getAttribute("port"));
			setIp(nodeConfig.getAttribute("ip"));
			String nodeWeight = nodeConfig.getAttribute("weight");
			if (!CEPCoreUtil.isNull(nodeWeight)) {
				setWeight(Integer.parseInt(nodeWeight));
			}
		}
		List<XmlNode> configNodes = nameFilter.findNodeList("sc");
		if (configNodes != null) {
			List<Node> centralNodes = new ArrayList<Node>();
			for (XmlNode configNode : configNodes) {
				String lIp = configNode.getAttribute("ip");
				String lPort = configNode.getAttribute("port");
				String lName = configNode.getAttribute("name");
				Node node = new Node(lIp, lPort, lName, 0);
				node.setType(Node.CENTRAL_NODE);
				centralNodes.add(node);
			}
			addCentralNodes(centralNodes);
		}

	}

	protected void removeConnect(Node remoteNode) {
		remoteImpl.removeConnect(remoteNode);
	}

	public Event remoteprocess(Event event, Node remoteNode) {
		return remoteImpl.remoteprocess(event, remoteNode);
	}

	public void startCEPCore(CEPCore cep) {
		Node node = getNode();// 获取本地node
		remoteImpl.startCEPCore(cep, node);

	}

	public void stopCEPCore(CEPCore cep) {
		Node node = getNode();// 获取本地node
		remoteImpl.stopCEPCore(cep, node);
	}

	protected Event getEvent(String serviceId, Context context) {
		Event e = new Event();
		e.setEventId(UUID.randomUUID().toString());
		ServiceRequest request = new ServiceRequest();
		request.setContext(context);
		request.setServiceId(serviceId);
		e.setServiceRequest(request);
		return e;
	}

	protected Node getNode() {
		Node node = new Node();
		String lIp = this.ip;
		if (lIp == null || "".equals(lIp)) {
			try {
				lIp = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				throw new CEPRunException(e, "cepcore.getIpError");
			}
		}
		node.setIp(lIp);
		if (port == null || "".equals(port)) {
			port = DEFAULT_PORT;
		}
		node.setPort(port);
		node.setNodeName(nodeName);
		node.setType(getType());
		node.setWeight(weight);
		return node;
	}

	protected abstract String getType();

}
