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

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.exception.CEPConnectException;
import org.tinygroup.cepcore.util.CEPCoreUtil;
import org.tinygroup.cepcore.util.NodeContainer;
import org.tinygroup.cepcore.util.NodeServiceContainer2;
import org.tinygroup.cepcore.util.SCStatus;
import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceInfo;
import org.tinygroup.event.ServiceRequest;
import org.tinygroup.event.central.Node;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.parser.filter.NameFilter;
import org.tinygroup.xmlparser.node.XmlNode;

public class CEPCoreNode3Impl extends AbstractCEPCoreOp {

	private static Logger logger = LoggerFactory
			.getLogger(CEPCoreNode3Impl.class);
	// key centralNode
	private NodeServiceContainer2 serviceContainer = new NodeServiceContainer2();
	private NodeContainer nodeContainer = new NodeContainer();
	private BreathThread breathThread = null;
	private CheckDownThread checkThread = null;
	private static final int DEFAULT_INTERVAL = 20;
	private int breath_interval = DEFAULT_INTERVAL;
	private int sc_check_interval = DEFAULT_INTERVAL;
	private String nodeStrategy = DEFAULT_NODE_STRATEGY_BEAN;
//	private  Map<String, Long> lastFailTimeMap = new HashMap<String, Long>();

	/* (non-Javadoc)
	 * @see org.tinygroup.cepcore.CEPCoreNodeManager#remoteProcess(org.tinygroup.event.Event)
	 */
	public Event remoteProcess(Event event) {
		ServiceRequest request = event.getServiceRequest();
		String serviceId = request.getServiceId();
		ServiceInfo info = null;
		// synchronized (serviceContainer) {
		// logger.logMessage(LogLevel.ERROR,
		// "=======begin findService====="+System.currentTimeMillis());
		info = serviceContainer.getServiceInfo(serviceId);
		// logger.logMessage(LogLevel.ERROR,
		// "=======after findService====="+System.currentTimeMillis());
		// }
		Context newContext = CEPCoreUtil.getContext(event, info);
		event.getServiceRequest().setContext(newContext);
		return remoteProcessWithPreparedEvent(event,new ArrayList<String>());
	}

	/**
	 * 处理事件event,计算目标处理请求的节点时需要跳过skipNodes所描述的节点
	 * @param event 需要处理的事件
	 * @param skipNodes 需要跳过节点名列表
	 * @return
	 */
	private Event remoteProcessWithPreparedEvent(Event event,List<String> skipNodes) {
		ServiceRequest request = event.getServiceRequest();
		String serviceId = request.getServiceId();
		Node remoteNode = null;
		// synchronized (serviceContainer) {
		// logger.logMessage(LogLevel.ERROR,
		// "=======begin findNode====="+System.currentTimeMillis());
		remoteNode = serviceContainer.getNode(serviceId, event,skipNodes);
		// logger.logMessage(LogLevel.ERROR,
		// "=======begin findNode====="+System.currentTimeMillis());
		// }
		return remoteprocess(event, remoteNode,skipNodes);
	}

	public void setConfig(XmlNode xml) {
		super.setConfig(xml);
		NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(xml);
		XmlNode intervalConfigXml = nameFilter.findNode("node-sc-breath");
		if (intervalConfigXml != null) {
			String intervalConfig = intervalConfigXml.getAttribute("interval");
			if (!CEPCoreUtil.isNull(intervalConfig)) {
				breath_interval = Integer.parseInt(intervalConfig);
			}

		}

		XmlNode scCheckIntervalConfigXml = nameFilter.findNode("node-sc-check");
		if (scCheckIntervalConfigXml != null) {
			String scIntervalConfig = scCheckIntervalConfigXml
					.getAttribute("interval");
			if (!CEPCoreUtil.isNull(scIntervalConfig)) {
				sc_check_interval = Integer.parseInt(scIntervalConfig);
			}

		}

		XmlNode nodeStrategyConfigXml = nameFilter.findNode("node-strategy");
		if (nodeStrategyConfigXml != null) {
			nodeStrategy = nodeStrategyConfigXml.getAttribute("bean");
		}
		if (CEPCoreUtil.isNull(nodeStrategy)) {
			nodeStrategy = DEFAULT_NODE_STRATEGY_BEAN;
		}
		// 启动时调用，不需要同步
		serviceContainer.setNodeStrategyBean(nodeStrategy);

	}

	/* (non-Javadoc)
	 * @see org.tinygroup.cepcore.CEPCoreNodeManager#unregisteNode(org.tinygroup.event.central.Node, org.tinygroup.event.central.Node)
	 */
	public void unregisteNode(Node node, Node sourceNode) {
		logger.logMessage(LogLevel.INFO, "开始注销节点{0}", node.toString());
		if (Node.CENTRAL_NODE.equals(node.getType())) {
			unregisteCentralNode(node);
		} else {
			unregisteCepNode(node);
		}
		removeConnect(node);
		logger.logMessage(LogLevel.INFO, "注销节点{0}完成", node.toString());
	}

	public void registeNode(Node node, Node sc) {

		logger.logMessage(LogLevel.INFO, "开始注册节点{0}", node.toString());
		removeConnect(node);
		nodeContainer.addCepCentralRelation(node, sc);
		logger.logMessage(LogLevel.INFO, "节点{0}类型{1}", node.toString(),
				node.getType());
		if (Node.CENTRAL_NODE.equals(node.getType())) {
			registeCentralNode(node);
		} else {
			registeCepNode(node);
		}
		logger.logMessage(LogLevel.INFO, "注册节点{0}完成", node.toString());

	}

	/**
	 * 移除节点node的信息
	 * @param node 需要移除的节点
	 * @param time 向该节点发送请求的失败时间或者接收到sc注销请求时的时间
	 * @return
	 */
	private boolean removeNodeService(Node node) {
		// synchronized(serviceContainer){
		return serviceContainer.removeCepNode(node);
		// }
	}

	/**
	 * 注销本地信息中的node
	 * @param node
	 */
	private void unregisteCepNode(Node node) {
		logger.logMessage(LogLevel.INFO, "开始注销CEP节点");
		removeNodeService(node);
		logger.logMessage(LogLevel.INFO, "移除Node:{0}与SC的关联", node.toString());
		nodeContainer.removeCepCentralRelation(node);
		logger.logMessage(LogLevel.INFO, "移除Node:{0}与SC的关联完成", node.toString());
		logger.logMessage(LogLevel.INFO, "注销CEP节点完成");
	}

	public void registeNode(List<Node> nodes, Node sc) {
		logger.logMessage(LogLevel.INFO, "开始注册远程节点列表");
		for (Node node : nodes) {
			registeNode(node, sc);
		}
		logger.logMessage(LogLevel.INFO, "注册远程节点列表完成");
	}

	public void unregisteNode(List<Node> nodes, Node sourceNode) {
		logger.logMessage(LogLevel.INFO, "开始注销远程节点列表");
		for (Node node : nodes) {
			unregisteNode(node, sourceNode);
		}
		logger.logMessage(LogLevel.INFO, "注销远程节点列表完成");
	}

	private void registeCentralNode(Node node) {
		addCentralNode(node);
	}

	private void unregisteCentralNode(Node node) {
		logger.logMessage(LogLevel.INFO, "开始注销服务中心节点");
		nodeContainer.removeCentralNode(node);
		logger.logMessage(LogLevel.INFO, "注销服务中心节点完成");
	}

	private void registeCepNode(Node node) {
		// synchronized (serviceContainer) {
		serviceContainer.removeCepNode(node);
		serviceContainer.addCepNode(node);
		// }
	}

	public void addCentralNodes(List<Node> centralNodes) {
		for (Node node : centralNodes) {
			addCentralNode(node);
		}
	}

	public void addCentralNode(Node node) {
		nodeContainer.addCentralNode(node);
	}

	public void startCEPCore(CEPCore cep) {
		super.startCEPCore(cep);
		Node node = getNode();// 获取本地node
		node.getServiceInfos().addAll(cep.getServiceInfos());
		logger.logMessage(LogLevel.INFO, "开始注册当前节点至服务中心");
		registeProcess(node);
		logger.logMessage(LogLevel.INFO, "注册当前节点至服务中心完成");
		startBreathThread(cep);
		startCheckThread(cep);
	}

	public void startBreathThread(CEPCore cep) {
		logger.logMessage(LogLevel.INFO, "开始启动SC定时轮询,间隔{interval}秒",
				breath_interval);
		breathThread = new BreathThread(cep, breath_interval);
		breathThread.start();
		logger.logMessage(LogLevel.INFO, "启动SC定时轮询完毕,间隔{interval}秒",
				breath_interval);
	}

	public void startCheckThread(CEPCore cep) {
		logger.logMessage(LogLevel.INFO, "开始启动已关闭SC定时轮询,间隔{interval}秒",
				sc_check_interval);
		checkThread = new CheckDownThread(cep, sc_check_interval);
		checkThread.start();
		logger.logMessage(LogLevel.INFO, "启动已关闭SC定时轮询完毕,间隔{interval}秒",
				sc_check_interval);
	}

	private void breath(CEPCore cep) {
		Node node = getNode();
		node.getServiceInfos().addAll(cep.getServiceInfos());
		logger.logMessage(LogLevel.INFO, "开始向SC发起心跳检测");
		for (Node centralNode : nodeContainer.getNotDownCentralNodes().values()) {
			logger.logMessage(LogLevel.INFO, "尝试连接SC[{0}]",
					centralNode.toString());
			Context c = new ContextImpl();
			c.put("node", node);
			try {

				Event result = remoteprocess(getEvent(BREATH_CHECK_SERVICE, c),
						centralNode);
				logger.logMessage(LogLevel.INFO, "连接SC[{0}]成功",
						centralNode.toString());
				// 返回true，则表示sc上有此节点，否则sc上无此节点，需要重新注册
				Boolean checkResult = result.getServiceRequest().getContext()
						.get("success");
				logger.logMessage(LogLevel.INFO, "心跳处理结果:{checkResult}",
						checkResult);
				boolean needRegiste = true;
				if (checkResult != null) {
					needRegiste = !checkResult;
				}
				if (needRegiste == false) {
					needRegiste = nodeContainer
							.isNeedRegToInBreath(centralNode);
				}

				if (needRegiste) {
					logger.logMessage(LogLevel.INFO, "向SC[{0}]重新注册",
							centralNode.toString());
					registeProcess(node, centralNode);
					//20130918 注销此句
					//因为就算注册失败，也会执行这句话，而且这个状态修改在registeProcess已完成了
//					nodeContainer.setCentralNodeStatus(centralNode,
//							SCStatus.ALIVE);
					logger.logMessage(LogLevel.INFO, "向SC[{0}]重新注册完毕",
							centralNode.toString());
				}

			} catch (Exception e) {
				logger.logMessage(LogLevel.ERROR, "连接SC[{0}]时发生异常:{1}",
						centralNode.toString(), e.getMessage());
			}

		}
		logger.logMessage(LogLevel.INFO, "向SC发起心跳检测完成");
	}

	private void checkDown(CEPCore cep) {
		logger.logMessage(LogLevel.INFO, "开始尝试连接已关闭的SC");
		Node node = getNode();
		node.getServiceInfos().addAll(cep.getServiceInfos());
		for (Node centralNode : nodeContainer.getDownCentralNodes().values()) {
			logger.logMessage(LogLevel.INFO, "尝试连接已关闭的SC[{0}]",
					centralNode.toString());
			Context c = new ContextImpl();
			c.put("node", node);
			try {

				remoteprocess(getEvent(BREATH_CHECK_SERVICE, c), centralNode);
				logger.logMessage(LogLevel.INFO, "连接SC[{0}]成功",
						centralNode.toString());

				boolean needReg = nodeContainer
						.isNeedRegToInCheckDown(centralNode);
				if (needReg) {
					logger.logMessage(LogLevel.INFO, "向SC[{0}]重新注册",
							centralNode.toString());
					registeProcess(node, centralNode);
					//20130918 注销此句
					//因为就算注册失败，也会执行这句话，而且这个状态修改在registeProcess已完成了
//					nodeContainer.setCentralNodeStatus(centralNode,
//							SCStatus.ALIVE);
					logger.logMessage(LogLevel.INFO, "向SC[{0}]重新注册完毕",
							centralNode.toString());
				}
			} catch (Exception e) {
				logger.logMessage(LogLevel.ERROR, "连接已关闭的SC[{0}]时发生异常:{1}",
						centralNode.toString(), e.getMessage());
			}

		}
		logger.logMessage(LogLevel.INFO, "尝试连接已关闭的SC完成");
	}

	public void stopCEPCore(CEPCore cep) {
		super.stopCEPCore(cep);
		Node node = getNode();// 获取本地node
		node.getServiceInfos().addAll(cep.getServiceInfos());
		stopBreathThread();
		stopCheckThread();
		unregisteProcess(node);
	}

	public void stopBreathThread() {
		logger.logMessage(LogLevel.INFO, "开始停止SC定时轮询");
		if (breathThread != null && breathThread.isAlive()) {
			breathThread.stop = true;
			Thread thread = breathThread;
			breathThread = null;
			thread.interrupt();
			logger.logMessage(LogLevel.INFO, "设置停止标识符为true");
		}
		logger.logMessage(LogLevel.INFO, "停止SC定时轮询完成");
	}

	public void stopCheckThread() {
		logger.logMessage(LogLevel.INFO, "开始停止checkThread SC定时轮询");
		if (checkThread != null && checkThread.isAlive()) {
			checkThread.stop = true;
			Thread thread = checkThread;
			checkThread = null;
			thread.interrupt();
		}
		logger.logMessage(LogLevel.INFO, "停止checkThread SC定时轮询完成");
	}

	private void registeProcess(Node node) {
		logger.logMessage(LogLevel.INFO, "向服务中心注册节点{0}", node.toString());
		for (Node central : nodeContainer.getAllCentralNodes().values()) {
			registeProcess(node, central);
		}
		logger.logMessage(LogLevel.INFO, "向服务中心注册节点{0}完成", node.toString());
	}

	private void registeProcess(Node node, Node central) {
		Context c = new ContextImpl();
		c.put("node", node);
		c.put("sourceNode", getNode());
		logger.logMessage(LogLevel.INFO, "开始向{0}注册{1}", central.toString(),
				node.toString());
		nodeContainer.addCepCentralRelation(node, central);
		try {
			remoteprocess(getEvent(REG_NODE_SERVICE, c), central);// 向所有的服务中心节点注册
			nodeContainer.setCentralNodeStatus(central, SCStatus.ALIVE);
		} catch (Exception e) {
			logger.logMessage(LogLevel.ERROR, "向{0}注册{1}时失败:{2}",
					central.toString(), node.toString(), e.getMessage());
			nodeContainer.setCentralNodeStatus(central, SCStatus.OUT);
		}
		// }

		logger.logMessage(LogLevel.INFO, "向{0}注册{1}完成", central.toString(),
				node.toString());
	}

	// public void registerprocess(List<ServiceInfo> services) {
	// Node node = getNode();
	// node.getServiceInfos().addAll(services);
	// registeProcess(node);
	// }
	//
	// public void unregisterprocess(List<ServiceInfo> services) {
	// Node node = getNode();
	// node.getServiceInfos().addAll(services);
	// unregisteProcess(node);
	// }

	private void unregisteProcess(Node node) {
		List<Node> centralNodes = nodeContainer.getNodeCentralNodes(node);
		if (centralNodes == null) {
			logger.logMessage(LogLevel.INFO, "node:{0}无对应的SC，该节点或已被注销，退出注销",
					node.toString());
			return;
		}
		logger.logMessage(LogLevel.INFO, "node:{0}对应的SC列表长度:{2}",
				node.toString(), centralNodes.size());
		doUnregisteProcess(node, centralNodes);
	}

	private void doUnregisteProcess(Node node, List<Node> scs) {
		for (Node central : scs) {
			Context c = new ContextImpl();
			c.put("node", node);
			c.put("sourceNode", getNode());
			logger.logMessage(LogLevel.INFO, "开始向{0}注销{1}", central.toString(),
					node.toString());
			try {
				remoteprocess(getEvent(UNREG_NODE_SERVICE, c), central);// 向所有的服务中心节点注销
			} catch (Exception e) {
				logger.logMessage(LogLevel.ERROR, "向{0}注销{1}时失败:{1}",
						central.toString(), node.toString(), e.getMessage());
			}
			logger.logMessage(LogLevel.INFO, "向{0}注销{1}完成", central.toString(),
					node.toString());
		}
	}

	protected String getType() {
		return Node.CEP_NODE;
	}

	public Event remoteprocess(Event event, Node remoteNode){
		return remoteprocess(event, remoteNode, new ArrayList<String>());
	}
	
	private Event remoteprocess(Event event, Node remoteNode,List<String> skipNodes) {
//		long requestTime = System.currentTimeMillis();
		try {
			Event e =  super.remoteprocess(event, remoteNode);
//			lastFailTimeMap.remove(remoteNode.toString());
			return e;
		} catch (CEPConnectException e) {
			skipNodes.add(remoteNode.getNodeName());
//			Long lastFailTime =  null;
//			synchronized (lastFailTimeMap) {
//				lastFailTime =lastFailTimeMap.get(remoteNode.toString());
//
//				if (lastFailTime == null) { // 如果上次失败时间是空，则设置上次失败时间为此刻
//					lastFailTimeMap.put(remoteNode.toString(), requestTime);
//					lastFailTime = requestTime;
//				}
//			}
			
			//如果上次失败时间到此次失败 有5秒钟，则认为该节点已挂掉
//			if(requestTime - lastFailTime >= 30000) {
//				logger.logMessage(LogLevel.INFO, "上次失败时间:{0},此次请求时间:{1}，相距大于等于5秒",lastFailTime,requestTime);
				if (remoteNode.getType().equals(Node.CEP_NODE)) {
					// 请求至CEP节点连接失败，注销该CEP节点
					// 先在本地将该节点删除，否则若CEP是离线状态，该注销不会正常返回，将不会再本地删除该CEP节点信息
					if (removeNodeService(remoteNode)) {
						// 若是该CEP节点离线，则注销正常完成
						// 若是本节点离线，则注销失败，会发生连接SC节点失败，进入下面else if的逻辑
						unregisteProcess(remoteNode);
					}
					// 重新查找可处理的CEP节点，进行处理
					return remoteProcessWithPreparedEvent(event,skipNodes);

				} else if (remoteNode.getType().equals(Node.CENTRAL_NODE)) {
					logger.logMessage(LogLevel.INFO, "连接SC节点{0}时出错:{1}",
							remoteNode.toString(), e.getMessage());
					if (nodeContainer.isNeedRemoveConnect(remoteNode)) {
						removeConnect(remoteNode);// 移除SC对应的连接
					}
				}
				throw e;
//			}else{
//				logger.logMessage(LogLevel.INFO, "上次失败时间:{0},此次请求时间:{1}，相距不足5秒",lastFailTime,requestTime);
//				//时间不足5秒
//				return remoteProcessWithPreparedEvent(event,skipNodes);
//			}

			
		}
	}

	class CheckDownThread extends Thread {
		private static final int MILLISECOND_PER_SECOND = 1000;
		private CEPCore cep;
		private boolean stop = false;
		private int checkInterval;

		public CheckDownThread(CEPCore cep, int checkInterval) {
			this.cep = cep;
			this.checkInterval = checkInterval;
		}

		public void run() {
			while (!stop) {
				try {
					sleep(checkInterval * MILLISECOND_PER_SECOND);
					checkDown(cep);
				} catch (InterruptedException e) {
					logger.logMessage(LogLevel.INFO, "对已关闭SC定时轮询停止");
					stop = true;
				}
			}
		}
	}

	class BreathThread extends Thread {
		private static final int MILLISECOND_PER_SECOND = 1000;
		private CEPCore cep;
		private boolean stop = false;
		private int breathInterval;

		public BreathThread(CEPCore cep, int breathInterval) {
			this.cep = cep;
			this.breathInterval = breathInterval;
		}

		public void run() {
			while (!stop) {
				try {
					sleep(breathInterval * MILLISECOND_PER_SECOND);
					breath(cep);
				} catch (InterruptedException e) {
					// logger.errorMessage(e.getMessage(), e);
					logger.logMessage(LogLevel.INFO, "对SC心跳停止");
					stop = true;
				}
			}
		}
	}

	public boolean check(Node node) {
		return true;
	}

	public ServiceInfo getServiceInfo(String serviceId) {
		return serviceContainer.getServiceInfo(serviceId);
	}
}
