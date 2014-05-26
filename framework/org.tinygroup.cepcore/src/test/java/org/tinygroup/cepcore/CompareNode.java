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

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.tinygroup.event.central.Node;

public class CompareNode extends TestCase {
	public void testContain(){
		List<Node> nodes = new ArrayList<Node>();
		nodes.add(getNode("a"));
		nodes.add(getNode("b"));
		assertEquals(false, nodes.contains(getNode("a")));
		
	}
	
	private Node getNode(String name){
		Node node = new Node();
		node.setNodeName(name);
		return node;
	}
	
}
