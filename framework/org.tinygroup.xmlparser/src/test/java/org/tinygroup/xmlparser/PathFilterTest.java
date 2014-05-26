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
package org.tinygroup.xmlparser;

import org.tinygroup.parser.NodeFilter;
import org.tinygroup.parser.filter.PathFilter;
import org.tinygroup.xmlparser.node.XmlNode;

import junit.framework.TestCase;

public class PathFilterTest extends TestCase {
	NodeFilter filter;

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testFindNodeList() {
		XmlNode node = new XmlNode("aa");
		filter = new PathFilter(node);
		assertEquals(1, filter.findNodeList("/aa").size());
	}

	public void testFindNodeList1() {
		XmlNode node = new XmlNode("aa");
		node.addNode(new XmlNode("b"));
		node.addNode(new XmlNode("b"));
		filter = new PathFilter(node);
		assertEquals(2, filter.findNodeList("/aa/b").size());
	}

	public void testFindNodeList2() {
		XmlNode node = new XmlNode("aa");
		// node.addNode(new XmlNode("b"));
		XmlNode n = node.addNode(new XmlNode("b"));
		n.addNode(new XmlNode("c"));
		XmlNode c = n.addNode(new XmlNode("c"));
		c.setAttribute("a", "a");
		filter = new PathFilter(c);
		assertEquals(2, filter.findNodeList("../../aa/b/c").size());

		filter = new PathFilter(c);
		filter.setIncludeAttributes("a");
		assertEquals(1, filter.findNodeList("../../aa/b/c").size());
	}
}
