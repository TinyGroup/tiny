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

import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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

/**
 * @author chenjiao
 * 维护当前节点上所有的远程节点服务信息
 * position1为获取服务存在的远程节点
 * position2为当某个已存在节点重新注册到本节点时，对该节点的服务信息进行刷新的操作
 * position1之所以加锁，是为了避免当执行position2时，发生position1的读操作引起问题
 * 比如:若某节点A进行刷新操作，假设节点A之前存在服务S，刷新时该S已不存在，刷新时，还未删除S时，postion1读取到了S存在A上
 * 从而进行调用，结果调用失败，若失败同时，此处刷新完毕，当调用失败，CepNode就会将本地该节点的信息删除，从而导致A从本地被删除。
 */
public class NodeServiceContainer2Bak {
	private ConcurrentMap<String, NodeStrategy> serviceIdNodeStrategy = new ConcurrentHashMap<String, NodeStrategy>();
	private ConcurrentMap<String, NodeStrategy> serviceNameNodeStrategy = new ConcurrentHashMap<String, NodeStrategy>();
	private ConcurrentMap<String, ServiceInfo> serviceIdInfoMap = new ConcurrentHashMap<String, ServiceInfo>();
	//正在删除的节点，节点删除前加入此列表，节点删除后，从此列表移除
	//删除一个节点时,若结点已存在此列表中，则不再删除
	//获取一个服务对应的节点时，检查若该节点存在于此列表，则表明该节点正在被删除不可用，需要重新计算
	private List<String> currentRemoveNode = new Vector<String>();
	

	private static Logger logger = LoggerFactory
			.getLogger(NodeServiceContainer2Bak.class);
	private String nodeStrategyBean = "";// 服务负载均衡bean

	public void setNodeStrategyBean(String nodeStrategyBean) {
		this.nodeStrategyBean = nodeStrategyBean;
		logger.logMessage(LogLevel.INFO, "NodeServiceContainer2负载均衡bean:",
				nodeStrategyBean);
	}

	private Node getNode(String service,
			Map<String, NodeStrategy> serviceNodeStrategy, Event event) {
		//synchronized (lock) {//position1 //TODO:貌似不能删除同步约束 20130910 16:23
			String eventNodeName = event.getServiceRequest().getNodeName();
			if (!serviceNodeStrategy.containsKey(service))// 请求不存在负责均衡器，即请求不存在
				return null;
			if (CEPCoreUtil.isNull(eventNodeName)) { // 若未指定节点，则根据负载均衡计算
				return serviceNodeStrategy.get(service).getNode();
			} else {
				return serviceNodeStrategy.get(service).getNode(eventNodeName);
			}
		//}
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
		Node result = null;
		if (!CEPCoreUtil.isNull(serviceId)) {// 根据serviceId获得node
			result = getNode(serviceId, serviceIdNodeStrategy, event);
		}
		if (result == null && !CEPCoreUtil.isNull(serviceName)) {// 根据serviceName获得node
			result = getNode(serviceId, serviceNameNodeStrategy, event);
		}
		if (result == null) {
			throw new CEPRunException("cepcore.requestFindNoRemoteNode",
					serviceId, serviceName);
		}
		if(currentRemoveNode.contains(result.toString())){
			logger.logMessage(LogLevel.INFO, "节点{0}正在被删除，重新计算节点",result.toString());
			result = getNode(serviceId, serviceName, event);
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
		String nodeKey = node.toString();
		if(currentRemoveNode.contains(nodeKey)){
			logger.logMessage(LogLevel.INFO, "节点{0}正在被删除,无需重复删除",nodeKey);
			return;
		}
		currentRemoveNode.add(nodeKey);
		removeCepNodeInfo(node);
		currentRemoveNode.remove(nodeKey);
	}

	/**
	 * 移除指定节点的所有信息
	 * 
	 * @param node
	 */
	private void removeCepNodeInfo(Node node) {
		removeServiceNodeMap(serviceIdNodeStrategy, node);
		removeServiceNodeMap(serviceNameNodeStrategy, node);
	}

	public void addCepNode(Node node) {
		List<ServiceInfo> list = node.getServiceInfos();
		if (list != null) {
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
				addServiceNodeMap(serviceId, node, serviceIdNodeStrategy);
				serviceIdInfoMap.put(serviceId, info);
			}
			logger.logMessage(LogLevel.INFO, "添加CEP节点提供的服务完成,id:{0}",
					info.getServiceId());
		}
	}

	private void addServiceNodeMap(String service, Node node,
			Map<String, NodeStrategy> serviceNodeStrategyMap) {
		if (!serviceNodeStrategyMap.containsKey(service)) {
			NodeStrategy stratrgy = SpringUtil.getBean(nodeStrategyBean);
			serviceNodeStrategyMap.put(service, stratrgy);// 存入service/节点策略映射
		}
		serviceNodeStrategyMap.get(service).addNode(node);// 节点策略添加Node
	}

	private void removeServiceNodeMap(Map<String, NodeStrategy> nodeStrategys,
			Node node) {
		for (NodeStrategy strategy : nodeStrategys.values()) {
			Node ifHasNode = strategy.getNode(node.getNodeName());
			if (ifHasNode != null) {
				strategy.removeNode(ifHasNode);
			}
		}
	}

//	public void refresh(Node node) {
////		synchronized (lock) {//position2
//			removeCepNode(node);
//			addCepNode(node);
//		}
//	}
}
