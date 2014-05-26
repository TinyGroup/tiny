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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.tinygroup.event.central.Node;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

public class NodeContainer {
	private static Logger logger = LoggerFactory.getLogger(NodeContainer.class);
	// key centralNode(sc)
	private ConcurrentMap<String, Node> centralNodesMap = new ConcurrentHashMap<String, Node>();
	// key sc节点存活状态
	private ConcurrentMap<String, SCStatus> centralNodesStatusMap = new ConcurrentHashMap<String, SCStatus>();
	// cepNodeKey,List<centralNodeKey>
	private ConcurrentMap<String, List<String>> cepCentralRelationMap = new ConcurrentHashMap<String, List<String>>();

	public void removeCepCentralRelation(Node node) {
		cepCentralRelationMap.remove(CEPCoreUtil.getNodeKey(node));
	}

	/**
	 * 添加一个SC节点
	 * @param node
	 */
	public void addCentralNode(Node node) {
		String nodeKey = CEPCoreUtil.getNodeKey(node);
		if (!centralNodesMap.containsKey(nodeKey)) {
			centralNodesMap.put(nodeKey, node);
			logger.logMessage(LogLevel.INFO, "服务中心节点不存在,加入服务中心节点列表");
		} else {
			logger.logMessage(LogLevel.INFO, "服务中心节点已存在,无需添加");
		}
	}

	/**
	 * 移除一个SC节点
	 * 假移除，将SC状态设置为DOWN
	 * @param node
	 */
	public void removeCentralNode(Node node) {
		String nodeKey = CEPCoreUtil.getNodeKey(node);
		if (centralNodesMap.containsKey(nodeKey)) {
			centralNodesStatusMap.put(nodeKey, SCStatus.DOWN);
		} else {
			logger.logMessage(LogLevel.INFO, "服务中心节点不存在");
		}
	}

	/**
	 * 获取所有的SC节点
	 * @return
	 */
	public Map<String, Node> getAllCentralNodes() {
		return centralNodesMap;
	}

	/**
	 * 获取所有状态不是DOWN的节点
	 * @return
	 */
	public Map<String, Node> getNotDownCentralNodes() {
		Map<String, Node> map = new HashMap<String, Node>();
		for (String nodeKey : centralNodesMap.keySet()) {
			Node node = centralNodesMap.get(nodeKey);
			if (!centralNodesStatusMap.containsKey(nodeKey)
					|| centralNodesStatusMap.get(nodeKey) != SCStatus.DOWN) {
				map.put(nodeKey, node);
			}

		}
		return map;
	}

	/**
	 * 获取状态是DOWN的节点
	 * @return
	 */
	public Map<String, Node> getDownCentralNodes() {
		Map<String, Node> map = new HashMap<String, Node>();
		for (String nodeKey : centralNodesMap.keySet()) {
			Node node = centralNodesMap.get(nodeKey);
			if (centralNodesStatusMap.get(nodeKey) == SCStatus.DOWN) {
				map.put(nodeKey, node);
			}

		}
		return map;
	}

	/**
	 * 获取状态是ALIVE的节点
	 * @return
	 */
	public Map<String, Node> getAliveCentralNodes() {
		Map<String, Node> map = new HashMap<String, Node>();
		for (String nodeKey : centralNodesMap.keySet()) {
			Node node = centralNodesMap.get(nodeKey);
			if (centralNodesStatusMap.get(nodeKey) == SCStatus.ALIVE) {
				map.put(nodeKey, node);
			}

		}
		return map;
	}

	/**
	 * 在Breath中判断当前节点是否需要重新注册
	 * @param centralNode
	 * @return
	 */
	public boolean isNeedRegToInBreath(Node centralNode) {
		String nodeKey = CEPCoreUtil.getNodeKey(centralNode);
		if (centralNodesStatusMap.containsKey(nodeKey)) {
			if (centralNodesStatusMap.get(nodeKey) == SCStatus.OUT) {
				return true; // 如果之前状态是out，则此次要重新组册
			}
		} else {
			centralNodesStatusMap.put(nodeKey, SCStatus.ALIVE);
		}
		return false;
	}

	/**
	 * 在CheckDown中判断当前节点是否需要重新注册
	 * @param centralNode
	 * @return
	 */
	public boolean isNeedRegToInCheckDown(Node centralNode) {
		String nodeKey = CEPCoreUtil.getNodeKey(centralNode);
		if (centralNodesStatusMap.containsKey(nodeKey)) {
			if (centralNodesStatusMap.get(nodeKey) == SCStatus.DOWN) {
				return true;
			}
		} else {
			centralNodesStatusMap.put(nodeKey, SCStatus.ALIVE);
		}

		return false;
	}

	/**
	 * 设置SC节点状态
	 * @param centralNode
	 * @param status
	 */
	public void setCentralNodeStatus(Node centralNode, SCStatus status) {
		String nodeKey = CEPCoreUtil.getNodeKey(centralNode);
		centralNodesStatusMap.put(nodeKey, status);
	}

	/**
	 * 获取与cepNode有关联的SC节点
	 * @param cepNode
	 * @return
	 */
	public List<Node> getNodeCentralNodes(Node cepNode) {
		String nodeKey = CEPCoreUtil.getNodeKey(cepNode);
		List<String> centralKeys = null;
		centralKeys = cepCentralRelationMap.get(nodeKey);
		if (centralKeys == null) {
			return null;
		}
		List<Node> centralNodes = new ArrayList<Node>();
		// 只对活着的中心点注销
		for (Node central : getAliveCentralNodes().values()) {
			String centralKey = CEPCoreUtil.getNodeKey(central);
			if (centralKeys.contains(centralKey)) {
				centralNodes.add(central);
			}
		}
		return centralNodes;
	}

	/**
	 * 添加普通节点和SC的关联
	 * @param cep
	 * @param sc
	 */
	public void addCepCentralRelation(Node cep, Node sc) {
		logger.logMessage(LogLevel.INFO, "添加Node:{0}与SC:{1}的关联",
				cep.toString(), sc.toString());
		String cepKey = CEPCoreUtil.getNodeKey(cep);
		String centralKey = CEPCoreUtil.getNodeKey(sc);
		List<String> centralKeys = null;
		if (cepCentralRelationMap.containsKey(cepKey)) {
			centralKeys = cepCentralRelationMap.get(cepKey);
		} else {
			centralKeys = new ArrayList<String>();
			List<String> existCentralKeys = cepCentralRelationMap.putIfAbsent(cepKey, centralKeys);
			if(existCentralKeys!=null){
				centralKeys  = existCentralKeys;
			}
		}
		if (!centralKeys.contains(centralKey)) {
			centralKeys.add(centralKey);
			logger.logMessage(LogLevel.INFO, "添加Node:{0}与SC:{1}的关联",
					cep.toString(), sc.toString());
		}

		logger.logMessage(LogLevel.INFO, "添加Node与SC的关联完成", cep.toString(),
				sc.toString());
	}

	/**
	 * 判断某节点的链接是否需要移除
	 * 如果节点的状态不存在或者是ALIVE，则需要修改节点状态为OUT,并返回true，认为节点链接需要移除
	 * @param remoteNode
	 * @return
	 */
	public boolean isNeedRemoveConnect(Node remoteNode) {
		// 如果SC状态不存在或者之前的状态为TRUE
		// 则调整状态，并移除已有的连接
		String nodeKey = CEPCoreUtil.getNodeKey(remoteNode);
		if (!centralNodesStatusMap.containsKey(nodeKey)
				|| centralNodesStatusMap.get(nodeKey) == SCStatus.ALIVE) {
			centralNodesStatusMap.put(nodeKey, SCStatus.OUT);
			// 移除SC对应的连接
			return true;
		}
		return false;
	}
}
