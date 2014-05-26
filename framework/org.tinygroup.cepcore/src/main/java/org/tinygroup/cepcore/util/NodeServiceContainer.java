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
package org.tinygroup.cepcore.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.cepcore.NodeStrategy;
import org.tinygroup.cepcore.exception.CEPRunException;
import org.tinygroup.cepcore.exception.RequestNotFoundException;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceInfo;
import org.tinygroup.event.central.Node;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.springutil.SpringUtil;

public class NodeServiceContainer {
	private Map<String, NodeStrategy> serviceIdNodeStrategy = new HashMap<String, NodeStrategy>();
	private Map<String, NodeStrategy> serviceNameNodeStrategy = new HashMap<String, NodeStrategy>();
	private Map<String, Map<String, Node>> serviceIdNodeMap = new HashMap<String, Map<String, Node>>();
	private Map<String, Map<String, Node>> serviceNameNodeMap = new HashMap<String, Map<String, Node>>();
	private Map<String, ServiceInfo> serviceIdInfoMap = new HashMap<String, ServiceInfo>();
	private static Logger logger = LoggerFactory
			.getLogger(NodeServiceContainer.class);
	private String nodeStrategyBean = "";// 服务负载均衡bean
	// key cepNode
	private Map<String, Node> cepNodesMap = new HashMap<String, Node>();

	public void setNodeStrategyBean(String nodeStrategyBean) {
		this.nodeStrategyBean = nodeStrategyBean;
	}


	private Node getNode(String service, Map<String, Node> nodeMap,
			Map<String, NodeStrategy> serviceNodeStrategy, Event event) {
		if (nodeMap == null) {
			return null;
		}
		String eventNodeName = event.getServiceRequest().getNodeName();
		if (CEPCoreUtil.isNull(eventNodeName)) {
			return serviceNodeStrategy.get(service).getNode();
		} else {
			for (String key : nodeMap.keySet()) {
				if (eventNodeName.equals(nodeMap.get(key).getNodeName())) {
					return nodeMap.get(key);
				}
			}
			return null;
		}
	}

	/**
	 * 根据服务id\name\event获取一个供调用的远端节点
	 * 
	 * @param serviceId
	 * @param serviceName
	 * @param event
	 * @return
	 */
	public Node getNode(String serviceId, String serviceName, Event event) {
		Map<String, Node> map = null;
		Node result = null;
		if (!CEPCoreUtil.isNull(serviceId)) {// 根据serviceId获得node
			map = serviceIdNodeMap.get(serviceId);
			result = getNode(serviceId, map, serviceIdNodeStrategy, event);
		}
		if (result == null && !CEPCoreUtil.isNull(serviceName)) {// 根据serviceName获得node
			map = serviceNameNodeMap.get(serviceName);
			result = getNode(serviceId, map, serviceNameNodeStrategy, event);
		}
		if (result == null) {
			throw new CEPRunException("cepcore.requestFindNoRemoteNode",serviceId,serviceName);
		}
		return result;
	}

	/**
	 * 根据serviceId/Name获取服务信息
	 * 
	 * @param serviceId
	 * @param serviceName
	 * @return
	 */
	public ServiceInfo getServiceInfo(String serviceId) {
		ServiceInfo info = null;
		if (!CEPCoreUtil.isNull(serviceId)) {
			info = serviceIdInfoMap.get(serviceId);
		}
		if (info == null) {
			throw new RequestNotFoundException(serviceId);
		}
		return info;
	}

	public void removeCepNode(Node node) {
		String nodeKey = CEPCoreUtil.getNodeKey(node);
		if (cepNodesMap.containsKey(nodeKey)) {
			cepNodesMap.remove(nodeKey);
			removeCepNodeInfo(node);
		}
	}

	/**
	 * 移除指定节点的所有信息
	 * 
	 * @param node
	 */
	private void removeCepNodeInfo(Node node) {
		String nodeKey = CEPCoreUtil.getNodeKey(node);
		removeServiceNodeMap(serviceIdNodeMap, serviceIdNodeStrategy, nodeKey);
		removeServiceNodeMap(serviceNameNodeMap, serviceNameNodeStrategy,
				nodeKey);
	}
	
	public void addCepNode(Node node){
		String nodeKey = CEPCoreUtil.getNodeKey(node);
		List<ServiceInfo> list = node.getServiceInfos();
		if (list != null) {
			cepNodesMap.put(nodeKey, node);
			addServiceInfos(list, node);
		} else {
			logger.logMessage(LogLevel.INFO, "CEP节点提供的服务为空,无服务需要添加");
		}
	}

	private void addServiceInfos(List<ServiceInfo> list, Node node) {
		for (ServiceInfo info : list) {
			String serviceId = info.getServiceId();
			logger.logMessage(LogLevel.INFO, "开始添加CEP节点提供的服务,id:{0}",
					serviceId);

			if (!CEPCoreUtil.isNull(serviceId)) {// 添加serviceId相关
				addServiceNodeMap(serviceId, serviceIdNodeMap, node,
						serviceIdNodeStrategy);
				serviceIdInfoMap.put(serviceId, info);
			}
			logger.logMessage(LogLevel.INFO, "添加CEP节点提供的服务完成,id:{0}",
					info.getServiceId());
		}
	}

	private void addServiceNodeMap(String service,
			Map<String, Map<String, Node>> serviceNodeMap, Node node,
			Map<String, NodeStrategy> serviceNodeStrategyMap) {
		if (CEPCoreUtil.isNull(service)) {
			return;
		}
		Map<String, Node> nodeMap = serviceNodeMap.get(service);
		if (nodeMap == null) {
			nodeMap = new HashMap<String, Node>();
			serviceNodeMap.put(service, nodeMap);// 添加service/nodeMap的映射
		}
		String nodeKey = CEPCoreUtil.getNodeKey(node);
		if (!nodeMap.containsKey(nodeKey)) {
			nodeMap.put(nodeKey, node);// 存入Node
		}

		if (!serviceNodeStrategyMap.containsKey(service)) {
			NodeStrategy stratrgy = SpringUtil.getBean(nodeStrategyBean);
			stratrgy.addNodes(serviceNodeMap.get(service));// 节点策略添加node
			serviceNodeStrategyMap.put(service, stratrgy);// 存入service/节点策略映射
		} else {
			serviceNodeStrategyMap.get(service).addNode(node);// 节点策略添加Node
		}

	}

	private void removeServiceNodeMap(
			Map<String, Map<String, Node>> serviceNodeMap,
			Map<String, NodeStrategy> nodeStrategys, String nodeKey) {
		List<String> serviceRemoveList = new ArrayList<String>();
		Map<String, Map<String, Node>> serviceRemoveNodeMap = new HashMap<String, Map<String, Node>>();
		for (String service : serviceNodeMap.keySet()) {
			Map<String, Node> nodeMap = serviceNodeMap.get(service);
			if (nodeMap.containsKey(nodeKey)) {
				if (nodeMap.size() == 1) {
					serviceRemoveList.add(service);
				} else {
					serviceRemoveNodeMap.put(service, nodeMap);
				}
			}
		}
		for (String service : serviceRemoveList) {
			logger.logMessage(LogLevel.INFO, "移除服务:{s}", service);
			serviceNodeMap.remove(service);// 删除服务
			nodeStrategys.remove(service);// 删除服务对应的节点策略
		}
		for (String service : serviceRemoveNodeMap.keySet()) {
			Map<String, Node> nodeMap = serviceRemoveNodeMap.get(service);
			Node node = nodeMap.remove(nodeKey);// 删除服务对应的某个节点
			nodeStrategys.get(service).removeNode(node);// 删除服务节点策略中的某个节点
		}
	}
}
