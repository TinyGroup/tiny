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
package org.tinygroup.parser.filter;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.parser.Node;

/**
 * 根据名字进行过滤
 * 
 * @author luoguo
 * 
 * @param <T>
 */
public class NameFilter<T extends Node<T>> extends AbstractFilterImpl<T> {

	public NameFilter() {
	}

	public NameFilter(T node) {
		super(node);
	}

	/**
	 * 查找满足条件的节点列
	 * 
	 * @param nodeName
	 * @return List<T>
	 */
	public List<T> findNodeList(String nodeName) {
		List<T> result = new ArrayList<T>();
		findNodeListWithName(result, getNode(), nodeName);
		return result;
	}

	private void findNodeListWithName(List<T> result, T node, String nodeName) {
		String caseSensitiveName = node.getCaseSensitiveName(nodeName);
		if (caseSensitiveName.equals(node.getNodeName())) {
			if (isRightNode(node)) {
				isRightNode(node);
				result.add(node);
			}
		}
		if (node.getSubNodes() != null) {
			for (T n : node.getSubNodes()) {
				findNodeListWithName(result, n, nodeName);
			}
		}
	}

	// private void findNodeListWithName(List<T> result, T node, String
	// nodeName) {
	// if (node.getNodeName() != null
	// && node.getNodeName().equals(
	// node.getCaseSensitiveName(nodeName))) {
	// if (isRightNode(node)) {
	// result.add(node);
	// }
	// } else {
	// if (node.getSubNodes() != null) {
	// for (T n : node.getSubNodes()) {
	// findNodeListWithName(result, n, nodeName);
	// }
	// }
	// }
	// }

}
