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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.CEPCoreNodeManager;
import org.tinygroup.cepcore.CEPCoreNodeOperation;
import org.tinygroup.cepcore.exception.CEPConnectException;
import org.tinygroup.cepcore.exception.CEPRunException;
import org.tinygroup.cepcore.util.CEPCoreUtil;
import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.context.util.ContextFactory;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceInfo;
import org.tinygroup.event.central.Node;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

public class CEPCoreCentralImplBak extends AbstractCEPCoreOp implements
		CEPCoreNodeManager, CEPCoreNodeOperation {
	private static Logger logger = LoggerFactory
			.getLogger(CEPCoreCentralImplBak.class);
	private ConcurrentMap<String, Node> cepNodesMap = new ConcurrentHashMap<String, Node>();
	private ConcurrentMap<String, Node> centralNodesMap = new ConcurrentHashMap<String, Node>();

	public Event remoteProcess(Event event) {
		throw new CEPRunException("cepcore.cepCentralNotSupportRemoteRequest");
	}

	public void registeNode(List<Node> nodes, Node sc) {
		for (Node node : nodes) {
			registeCepNode(node);
		}
	}

	public void registeNode(Node node, Node sc) {

		removeConnect(node);

		if (Node.CEP_NODE.equals(node.getType())) {
			registeCepNode(node);
		} else if (Node.CENTRAL_NODE.equals(node.getType())) {
			registeCepCentral(node);
		}
	}

	private synchronized void registeCepNode(Node node) {

		logger.logMessage(LogLevel.INFO, "注册CEP节点{0}", node.toString());
		registeCepNodeToExist(node);
		registeExistToCepNode(node);
		logger.logMessage(LogLevel.INFO, "注册CEP节点{0}完成", node.toString());
		
		
	}
	
	private void registeCepNodeToExist(Node node){
		String key = CEPCoreUtil.getNodeKey(node);
		// 如果存在，则删除旧信息
		if (cepNodesMap.containsKey(key)){
			cepNodesMap.remove(key);
		}
		// 向所有已加入节点发送注册节点信息
		logger.logMessage(LogLevel.INFO, "推送该节点至服务中心已有节点列表");
		Context c = ContextFactory.getContext();
		c.put("node", node);
		c.put("sourceNode", getNode());
		distributeCepNode(node, REG_NODE_SERVICE, c);
		logger.logMessage(LogLevel.INFO, "推送该节点至服务中心已有节点列表完成");
	}
	private void registeExistToCepNode(Node node){
		
		// 将已存在的节点列表信息传递至注册节点
		logger.logMessage(LogLevel.INFO, "推送中心已有节点至节点{0}", node.toString());
		String key = CEPCoreUtil.getNodeKey(node);
		
		List<Node> nodes = getAllCepNodes();
		if(nodes.size()==0){
			logger.logMessage(LogLevel.INFO, "中心已有节点数为0，无需推送");
		}else{
			Context c2 = ContextFactory.getContext();
			c2.put("nodes", nodes);
			c2.put("sourceNode", getNode());
			distributeCepNodes(node, REG_NODES_SERVICE, c2);
		}
		logger.logMessage(LogLevel.INFO, "推送中心已有节点至节点{0}完成", node.toString());
		// 添加入队列
		cepNodesMap.put(key, node);

	}
	private List<Node> getAllCepNodes() {
		List<Node> list = new ArrayList<Node>();
		for (String key : cepNodesMap.keySet()) {
			list.add(cepNodesMap.get(key));
		}
		return list;
	}

	private void distributeCepNode(Node node, String serviceId, Context c) {
		Event event = getEvent(serviceId, c);
		String nodeKey = CEPCoreUtil.getNodeKey(node);
		for (String key : cepNodesMap.keySet()) {
			if (!key.equals(nodeKey)) {
				Node cep = cepNodesMap.get(key);
				try {
					remoteprocess(event, cep);
				} catch (Exception e) {
					logger.errorMessage("向CEP:{0}发送请求[id:{1}]时出错", e,
							cep.toString(), serviceId);
				}
			}
		}

	}

	private void distributeCepNodes(Node node, String serviceId, Context c) {
		Event event = getEvent(serviceId, c);
		try {
			remoteprocess(event, node);
		} catch (Exception e) {
			logger.errorMessage("向CEP:{0}发送请求[id:{1}]时出错", e, node.toString(),
					serviceId);
		}
	}

	private synchronized void registeCepCentral(Node node) {
		centralNodesMap.put(CEPCoreUtil.getNodeKey(node), node);
	}

	public void unregisteNode(List<Node> nodes,Node sourceNode) {
		for (Node node : nodes) {
			unregisteNode(node,sourceNode);
		}
	}

	public void unregisteNode(Node node,Node sourceNode) {
		if (Node.CEP_NODE.equals(node.getType())) {
			unregisteCepNode(node);
		} else if (Node.CENTRAL_NODE.equals(node.getType())) {
			unregisteCepCentral(node);
		}
		removeConnect(node);

	}

	private synchronized void unregisteCepCentral(Node node) {
		//centralNodesMap.remove(node.getNodeName());
	}

	private synchronized void unregisteCepNode(Node node) {
		logger.logMessage(LogLevel.INFO, "注销CEP节点{0}", node.toString());
		String nodeKey = CEPCoreUtil.getNodeKey(node);
		if (!cepNodesMap.containsKey(nodeKey)) {
			return;
		}

		Node cepNode = cepNodesMap.remove(nodeKey);
		Context c = ContextFactory.getContext();
		c.put("node", cepNode);
		distributeCepNode(cepNode, UNREG_NODE_SERVICE, c);
		logger.logMessage(LogLevel.INFO, "注销CEP节点{0}完成", node.toString());
	}

	public void addCentralNodes(List<Node> centralNodes) {
		for (Node centralNode : centralNodes) {
			addCentralNode(centralNode);
		}
	}

	public void addCentralNode(Node centralNode) {
		centralNodesMap.put(centralNode.getNodeName(), centralNode);
	}

	// public void registerprocess(List<ServiceInfo> services) {
	// // DO NOTHING
	// }
	//
	// public void unregisterprocess(List<ServiceInfo> services) {
	// // DO NOTHING
	// }

	protected String getType() {
		return Node.CENTRAL_NODE;
	}

	public void stopCEPCore(CEPCore cep) {
		super.stopCEPCore(cep);
		Node node = getNode();// 获取本地node
		node.getServiceInfos().addAll(cep.getServiceInfos());
		unregisteProcess(node);
	}

	private void unregisteProcess(Node node) {
		logger.logMessage(LogLevel.INFO, "开始注销节点:{0}", node.toString());
		for (Node cep : cepNodesMap.values()) {
			Context c = new ContextImpl();
			c.put("node", node);
			try {
				remoteprocess(getEvent(UNREG_NODE_SERVICE, c), cep);// 向所有的cep节点注销
			} catch (Exception e) {
				logger.logMessage(LogLevel.INFO, "向CEP节点:{0}注销时出错:{1}",
						cep.toString(),e.getMessage());
			}

		}
		for (Node central : centralNodesMap.values()) {
			Context c = new ContextImpl();
			c.put("node", node);
			try {
				remoteprocess(getEvent(UNREG_NODE_SERVICE, c), central);// 向所有的cep节点注销
			} catch (Exception e) {
				logger.logMessage(LogLevel.INFO, "向SC节点:{0}注销时出错:{1}",
						central.toString(),e.getMessage());
			}
		}
		logger.logMessage(LogLevel.INFO, "注销节点完成:{0}", node.toString());

	}

	public Event remoteprocess(Event event, Node remoteNode) {
		try {
			return super.remoteprocess(event, remoteNode);
		} catch (CEPConnectException e) {
			unregisteNode(remoteNode,getNode());
			throw e;
		}
	}

	public boolean check(Node node) {
		String nodeKey = CEPCoreUtil.getNodeKey(node);
		return cepNodesMap.containsKey(nodeKey);
	}
	public ServiceInfo getServiceInfo(String serviceId) {
		throw new CEPRunException("cepcore.cepCentralNotSupportRequestGet");
	}

}
