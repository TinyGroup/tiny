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
package org.tinygroup.cepcore;

import java.rmi.RemoteException;
import java.util.List;

import org.tinygroup.event.Event;
import org.tinygroup.event.central.Node;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * 此接口用于管理远端节点信息
 * 
 * @author chenjiao
 * 
 */
public interface CEPCoreNodeManager extends CEPCoreNodeOperation {
	String REG_NODE_SERVICE = "cepNodeRegisteNode";
	String BREATH_CHECK_SERVICE = "cepNodeBreathCheck";
	String REG_NODES_SERVICE = "cepNodeRegisteNodes";
	String UNREG_NODE_SERVICE = "cepNodeUnRegisteNode";
	String DEFAULT_NODE_BEAN = "cepCoreManagerNode";
	String DEFAULT_CENTRAL_BEAN = "cepCoreManagerCentral";
	String DEFAULT_NODE_STRATEGY_BEAN = "nodeWeightPloyStrategy";
	String DEFAULT_PORT = "8808";

	/**
	 * 对事件进行远端处理 若找到远端节点能处理该event，则处理，并返回true 若未找到，返回false
	 * 
	 * @param event
	 * @return 是否有能处理event的远端节点
	 */
	Event remoteProcess(Event event);

	/**
	 * 注册其他节点的服务到本节点
	 * 
	 * @param node
	 */
	void registeNode(Node node,Node sourceNode);

	/**
	 * 注册其他节点的服务到本节点
	 * 
	 * @param node
	 */
	void registeNode(List<Node> nodes,Node sourceNode);

	/**
	 * 在本节点注销其他节点的服务
	 * 
	 * @param node
	 * @throws RemoteException
	 */
	void unregisteNode(Node node,Node sourceNode);

	/**
	 * 心跳方法
	 * 若返回为true，则sc中存在该node
	 * 若为flase,则不存在，需要重新注册
	 * @param node
	 */
	boolean check(Node node);

	/**
	 * 在本节点注销其他节点的服务
	 * 
	 * @param node
	 */
	void unregisteNode(List<Node> nodes,Node sourceNode);

	void addCentralNodes(List<Node> centralNodes);

	void addCentralNode(Node centralNode);

	void setNodeName(String nodeName);

	void setConfig(XmlNode xml);
}
