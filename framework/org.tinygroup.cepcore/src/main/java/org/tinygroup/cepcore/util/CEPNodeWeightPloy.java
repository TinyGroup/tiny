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
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.tinygroup.cepcore.NodeStrategy;
import org.tinygroup.event.central.Node;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

public class CEPNodeWeightPloy implements NodeStrategy {
	private static final Logger logger = LoggerFactory
			.getLogger(CEPNodeWeightPloy.class);
	static final int PRE = -1;
	static final int AFT = 1;
	static final int ME = 0;
//	private int totalWeight = 0;
	private List<WeightRange> weightRanges = new Vector<WeightRange>();
	// nodeName,node
	private ConcurrentMap<String, Node> nodes = new ConcurrentHashMap<String, Node>();
//	private ConcurrentMap<Node, WeightRange> nodeWeightRanges = new ConcurrentHashMap<Node, WeightRange>();

	public Node getNode() {
		if (nodes.size() == 0) {
			return null;
		}
		Map<Node,Integer> map = new HashMap<Node, Integer>();
		int totalWeight = 0;
		for(Node node : nodes.values()){
			int weight = node.getWeight();
			map.put(node, weight);
			totalWeight +=weight;
		}
		int random = (int) (Math.random() * totalWeight);
		Node defaultNode = null;
		for(Node node:map.keySet()){
			defaultNode = node;
			int weight = map.get(node);
			random -= weight;
			if(random<=0){
				return node;
			}
		}
		return defaultNode;
		
//		logger.logMessage(LogLevel.INFO, "权重随机数为:{0} ", random);
//		try {
//			return getWeightRange(weightRanges.size() / 2, random).getNode();
//		} catch (Exception e) {
//			return getNode();
//		}
		
	}

	public Node getNode(String name) {
		return nodes.get(name);
	}

	private WeightRange getWeightRange(int index, int position) {
		WeightRange w = weightRanges.get(index);
		int answer = w.check(position);
		if (answer == ME) {
			return w;
		} else if (answer == PRE) {
			return getWeightRange(index / 2, position);
		} else {
			int newIndex = (weightRanges.size() - index) / 2 + index;
			return getWeightRange(newIndex, position);
		}
	}

	public void addNodes(Map<String, Node> nodesMap) {
		for (Node node : nodesMap.values()) {
			addNode(node);
		}
	}

	public void addNode(Node node) {
		logger.logMessage(LogLevel.INFO, "策略开始添加节点:{0},当前总长:{0}",
				node.toString(), weightRanges.size());
		String nodeName = node.getNodeName();
		nodes.put(nodeName, node);
//		if (nodes.containsKey(nodeName)) {
//			logger.logMessage(LogLevel.INFO, "节点已存在");
//			Node oldNode = nodes.get(nodeName);
//			if (oldNode.getWeight() == node.getWeight()) {// 如果新旧节点的weight一样，则返回
//				logger.logMessage(LogLevel.INFO, "添加节点与已存在节点权重相同，退出添加");
//				return;
//			} else {
//				logger.logMessage(LogLevel.INFO, "添加节点与已存在节点权重不同，删除原节点");
//				removeNode(oldNode);
//			}
//
//		}
//		WeightRange w = new WeightRange(node, totalWeight);
//		logger.logMessage(LogLevel.INFO, "节点随机范围为min:{0},max:{1}", w.min, w.max);
//		totalWeight += node.getWeight();// 总权重添加
//		weightRanges.add(w);// 权重范围添加
//		nodes.put(nodeName, node);// 节点列表添加
//		nodeWeightRanges.put(node, w);// 节点权重范围添加
		logger.logMessage(LogLevel.INFO, "策略添加节点:{0}完成,当前总长:{0}",
				node.toString(), weightRanges.size());
	}

	public void removeNode(Node removeNode) {
		logger.logMessage(LogLevel.INFO, "策略开始删除节点:{0},当前总长:{0}",
				removeNode.toString(), weightRanges.size());
		String nodeName = removeNode.getNodeName();
		nodes.remove(nodeName);
//		if (!nodes.containsKey(nodeName)) {
//			logger.logMessage(LogLevel.INFO, "节点不存在,退出删除");
//			return;
//		}
//		Node node = nodes.remove(nodeName); // 节点列表移除
//		totalWeight -= node.getWeight();// 总weight移除
//		WeightRange w = nodeWeightRanges.remove(node);// 节点权重范围移除
//		int index = weightRanges.indexOf(w);
//		logger.logMessage(LogLevel.INFO, "节点Index为:{1}", index);
//		for (int i = index + 1; i < weightRanges.size(); i++) {
//			weightRanges.get(i).reduce(node.getWeight());
//		}
//		weightRanges.remove(w);// 权重范围移除
		logger.logMessage(LogLevel.INFO, "策略删除节点:{0}完成,当前总长:{0}",
				removeNode.toString(), weightRanges.size());
	}

	class WeightRange {
		private Node node;
		private int min;
		private int max;

		public void reduce(int weight) {
			min -= weight;
			max -= weight;
		}

		public WeightRange(Node node, int beginWeight) {
			this.node = node;
			min = beginWeight;
			max = beginWeight + node.getWeight();
		}

		public Node getNode() {
			return node;
		}

		public int check(int value) {
			logger.logMessage(LogLevel.INFO,
					"检查值:{0}所在,当前node:{1},min:{2},max:{3}", value,
					node.toString(), min, max);
			if (min <= value & value < max) {
				return 0;
			} else if (min > value) {
				return PRE;
			} else {
				return AFT;
			}
		}
	}

}
