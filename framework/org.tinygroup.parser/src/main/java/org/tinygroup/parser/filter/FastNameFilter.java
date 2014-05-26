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
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.tinygroup.commons.processor.Processor;
import org.tinygroup.parser.Node;

public class FastNameFilter<T extends Node<T>> extends AbstractFilterImpl<T> {
	private Map<String, List<T>> nodeTable = null;

	class NodeAdd implements Processor<T> {
		public void process(T node) {
			if (node.getNodeName() != null) {
				List<T> vector = nodeTable.get(node.getNodeName());
				if (vector == null) {
					vector = new ArrayList<T>();
					nodeTable.put(node.getNodeName(), vector);
				}
				vector.add(node);
			}
		}
	}

	public void init(T node) {
		super.init(node);
		nodeTable = new HashMap<String, List<T>>();
		node.foreach(new NodeAdd());

	}

	public FastNameFilter() {

	}

	public FastNameFilter(T node) {
		init(node);
	}

	public List<T> findNodeList(String nodeName) {
		List<T> result = nodeTable.get(nodeName);
		if (result != null) {
			result = filteNode(result);
		} else {
			result = new ArrayList<T>();
		}
		return result;
	}

}
