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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * @author chenjiao 维护当前节点上所有的远程节点服务信息 position1为获取服务存在的远程节点
 *         position2为当某个已存在节点重新注册到本节点时，对该节点的服务信息进行刷新的操作
 *         position1之所以加锁，是为了避免当执行position2时，发生position1的读操作引起问题
 *         比如:若某节点A进行刷新操作，假设节点A之前存在服务S，刷新时该S已不存在，刷新时，还未删除S时，postion1读取到了S存在A上
 *         从而进行调用，结果调用失败，若失败同时，此处刷新完毕，当调用失败，CepNode就会将本地该节点的信息删除，从而导致A从本地被删除。
 */
public class NodeServiceContainer2 {
	// 正在删除的节点，节点删除前加入此列表，节点删除后，从此列表移除
	// 删除一个节点时,若结点已存在此列表中，则不再删除
	// 获取一个服务对应的节点时，检查若该节点存在于此列表，则表明该节点正在被删除不可用，需要重新计算
	// private List<String> currentRemoveNode = new Vector<String>();

	/**
	 * Map<节点name,Map<serviceId,ServiceInfo>>
	 */
	 volatile Map<String, Map<String, ServiceInfo>> nodeServiceIdMap = new HashMap<String, Map<String, ServiceInfo>>();

	/**
	 * Map<节点name,Map<serviceName,ServiceInfo>>
	 */
	 volatile Map<String, Map<String, ServiceInfo>> nodeServiceNameMap = new HashMap<String, Map<String, ServiceInfo>>();

	/**
	 * Map<节点名，节点>
	 */
	 volatile Map<String, Node> nodeMap = new HashMap<String, Node>();

	/**
	 * 存储节点上次添加时间
	 */
//	 ConcurrentMap<String, Long> nodeAddTime = new ConcurrentHashMap<String, Long>();
	/**
	 * 存储节点当前状态
	 */
	ConcurrentMap<String, String> nodeStatus = new ConcurrentHashMap<String, String>();
	String REMOVING = "remove";
	String REMOVED = "removed";
	String ADDING = "adding";
	String ADDED = "added";

	private static Logger logger = LoggerFactory
			.getLogger(NodeServiceContainer2.class);
	private String nodeStrategyBean = "";// 服务负载均衡bean

	public void setNodeStrategyBean(String nodeStrategyBean) {
		this.nodeStrategyBean = nodeStrategyBean;
		logger.logMessage(LogLevel.INFO, "NodeServiceContainer2负载均衡bean:{0}",
				nodeStrategyBean);
	}

	/**
	 * 根据服务id\name\event获取一个供调用的远端节点
	 * 
	 * @param serviceId
	 * @param serviceName
	 * @param event
	 * @return
	 */
	public Node getNode(String serviceId,  Event event,List<String> skipNodes) {
		Node result = null;
		if (!CEPCoreUtil.isNull(serviceId)) {// 根据serviceId获得node
			NodeStrategy stratrgy = SpringUtil.getBean(nodeStrategyBean);
			for (String nodeName : nodeServiceIdMap.keySet()) {
				if(skipNodes.contains(nodeName)){
					continue;
				}
				Map<String, ServiceInfo> serviceIdMap = nodeServiceIdMap
						.get(nodeName);
				if (serviceIdMap.containsKey(serviceId)) {
					stratrgy.addNode(nodeMap.get(nodeName));
				}
			}
			result = stratrgy.getNode();
		}

		if (result == null) {
			throw new CEPRunException("cepcore.requestFindNoRemoteNode",
					serviceId);
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
		for (String nodeName : nodeServiceIdMap.keySet()) {
			Map<String, ServiceInfo> serviceIdMap = nodeServiceIdMap
					.get(nodeName);
			if (serviceIdMap.containsKey(serviceId)) {
				return serviceIdMap.get(serviceId);
			}
		}
		throw new RequestNotFoundException(serviceId);
	}


	
	
	public boolean removeCepNode(Node node) {
		String nodeName = node.getNodeName();
		String status = nodeStatus.get(nodeName);
//		logger.logMessage(LogLevel.INFO,
//				"开始尝试删除节点信息 node:{0},status:{0},removeTime:{1}", nodeName,status, time);
		if(REMOVED.equals(status)||REMOVING.equals(status)||ADDING.equals(status)){
			logger.logMessage(LogLevel.INFO,
					"node:{0},status:{0},无需删除", nodeName,status);
			return false;
		}
//		Long addTime = null;
//		if (time != null) {
//			addTime =  nodeAddTime.get(nodeName);
//			if (addTime == null) {
//				logger.logMessage(LogLevel.INFO,
//						"node:{0},status:{0},状态为空，无需删除", nodeName,status);
//				return false;
//			}
//			
//			if (addTime - time >= 0) {
//				logger.logMessage(LogLevel.INFO,
//						"addTime:{0},removeTime:{1},compute:{2}", addTime, time,
//						addTime - time);
//				logger.logMessage(LogLevel.INFO,
//						"node:{0},status:{0},注销发起时间早于上次添加时间，无需删除", nodeName,status);
//				return false;
//			}
//		}
//		logger.logMessage(LogLevel.INFO,
//				"node:{0},status:{1},注销发起时间晚于上次添加时间，删除节点信息,removeTime:{2},addTime:{3},compute:{4}",nodeName,status, new Date(time),new Date(addTime),addTime-time);
//	
		nodeStatus.put(nodeName, REMOVING);
		nodeServiceIdMap.remove(nodeName);
		nodeServiceNameMap.remove(nodeName);
		nodeMap.remove(nodeName);
		nodeStatus.put(nodeName, REMOVED);
		logger.logMessage(LogLevel.INFO,
				"删除节点信息 node:{0}完毕", nodeName);
		return true;
	}

	public void addCepNode(Node node) {
		
		String nodeName = node.getNodeName();
		logger.logMessage(LogLevel.INFO,
				"开始添加节点信息 node:{0}", nodeName);
		nodeStatus.put(nodeName, ADDING);
		List<ServiceInfo> list = node.getServiceInfos();
		Map<String, ServiceInfo> serviceIdMap = new HashMap<String, ServiceInfo>();
		Map<String, ServiceInfo> serviceNameMap = new HashMap<String, ServiceInfo>();
		for (ServiceInfo info : list) {
			serviceIdMap.put(info.getServiceId(), info);
		}
		nodeServiceIdMap.put(nodeName, serviceIdMap);
		nodeServiceNameMap.put(nodeName, serviceNameMap);
		nodeMap.put(nodeName, node);
//		long addTime = System.currentTimeMillis();
//		nodeAddTime.put(nodeName, addTime);
//		logger.logMessage(LogLevel.INFO, "addNode:{0},time:{1}", nodeName,
//				addTime);
		nodeStatus.put(nodeName, ADDED);
		logger.logMessage(LogLevel.INFO,
				"添加节点信息 node:{0}完毕", nodeName);
	}

}
