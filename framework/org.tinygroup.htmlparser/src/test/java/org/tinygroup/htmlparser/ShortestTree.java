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
package org.tinygroup.htmlparser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ShortestTree {
	static LeafNodeComparator comparator = new LeafNodeComparator();
	static int totalNodes = 0;

	public static void main(String[] args) {
		List<Node> nodes = new ArrayList<Node>();
		for (int i = 0; i < 5; i++) {
			nodes.add(new Node("a" + i));
		}
		nodes.get(0).link(nodes.get(1), 3);
		nodes.get(1).link(nodes.get(2), 2);
		nodes.get(1).link(nodes.get(3), 4);
		nodes.get(2).link(nodes.get(4), 2);
		totalNodes = nodes.size();
		processNode(nodes);
	}

	static void processNode(List<Node> nodes) {
		while (totalNodes > 2) {
			for (Node node : nodes) {
				if (!node.deleted) {
					System.out.println("#");
					if (node.linkList.size() >= 2) {// 如果是叶子节点
						int size = nodes.size();
						cutNode(nodes, node);
						if (nodes.size() != size) {
							break;
						}
					}
				}
			}
			for (int i = nodes.size() - 1; i >= 0; i--) {
				if (nodes.get(i).deleted) {
					nodes.remove(i);
				}
			}
		}
		System.out.println(nodes.get(0).linkList.get(0).length);
	}

	private static void cutNode(List<Node> nodes, Node node) {
		if (node.linkList.size() == 2) {// 如果自己只有两个连接的节点
			combineNode(nodes, node);
			return;
		}
		List<Node> leafNodes = new ArrayList<Node>();
		for (Link link : node.linkList) {
			if (link.targetNode.linkList.size() == 1) {
				leafNodes.add(link.targetNode);
			}
		}
		Collections.sort(leafNodes, comparator);
		if (leafNodes.size() > 2) {
			for (int i = leafNodes.size() - 1; i >= 2; i--) {
				Node leafNode = leafNodes.get(i);
				removeNode(nodes, node, leafNode);
			}
		}
	}

	private static void combineNode(List<Node> nodes, Node node) {
		Node node1 = node.linkList.get(0).targetNode;
		Node node2 = node.linkList.get(1).targetNode;
		node1.link(node2, node.linkList.get(0).length
				+ node.linkList.get(1).length);
		removeNode(nodes, node1, node);
		removeNode(nodes, node2, node);
	}

	private static void removeNode(List<Node> nodes, Node node, Node leafNode) {
		leafNode.deleted = true;
		totalNodes--;
		for (Link link : node.linkList) {
			if (link.targetNode == leafNode) {
				node.linkList.remove(link);
				break;
			}
		}
	}
}

class LeafNodeComparator implements Comparator<Node> {

	public int compare(Node o1, Node o2) {
		if (o1.linkList.get(0).length > o2.linkList.get(0).length)
			return -1;
		if (o1.linkList.get(0).length < o2.linkList.get(0).length)
			return 1;
		return 0;
	}

}

class Node {
	boolean deleted;
	String name;
	List<Link> linkList = new ArrayList<Link>();

	public Node(String name) {
		this.name = name;
	}

	public void link(Node node, int length) {
		link(length, this, node);
		link(length, node, this);
	}

	static void link(int length, Node source, Node target) {
		Link link = new Link(length, target);
		source.linkList.add(link);
	}
}

class Link {
	public Link(int length, Node targetNode) {
		this.length = length;
		this.targetNode = targetNode;
	}

	int length;
	Node targetNode;;
}