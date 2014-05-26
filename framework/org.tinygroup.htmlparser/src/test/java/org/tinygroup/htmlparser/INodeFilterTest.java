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

import java.util.Hashtable;

import org.tinygroup.htmlparser.node.HtmlNode;
import org.tinygroup.parser.NodeFilter;
import org.tinygroup.parser.filter.NameFilter;

import junit.framework.TestCase;

public class INodeFilterTest extends TestCase {
	HtmlNode node = null;
	NodeFilter filter;

	protected void setUp() throws Exception {
		super.setUp();
		node = new HtmlNode("root");
		HtmlNode n1 = node.addNode(new HtmlNode("aa"));
		n1.setAttribute("a", "av");
		n1.setAttribute("b", "bv");
		n1.addNode(new HtmlNode("a"));
		n1 = node.addNode(new HtmlNode("aa"));
		n1.setAttribute("a", "av1");
		n1.setAttribute("b", "bv1");
		n1.setAttribute("c", "cv1");
		n1.addNode(new HtmlNode("b"));
		filter = new NameFilter(node);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testClearCondition() {
		filter.clearCondition();
		assertEquals(2, filter.findNodeList("aa").size());
	}

	public void testFindNodeList() {
		// 测试不包含属性名
		filter.clearCondition();
		assertEquals(1, filter.findNodeList("root").size());
		filter.setExcludeAttribute("c");
		assertEquals(1, filter.findNodeList("aa").size());
		// 测试包含属性名
		filter.clearCondition();
		assertEquals(1, filter.findNodeList("root").size());
		filter.setIncludeAttributes("c");
		assertEquals(1, filter.findNodeList("aa").size());
		// 测试包含指定属性值
		filter.clearCondition();
		Hashtable<String, String> pht = new Hashtable<String, String>();
		pht.put("a", "av1");
		filter.setIncludeAttribute(pht);
		assertEquals(1, filter.findNodeList("aa").size());
		filter.setExcludeAttribute("c");
		assertEquals(0, filter.findNodeList("aa").size());
		// 测试包含指定节点
		filter.clearCondition();
		filter.setIncludeNode("a");
		assertEquals(1, filter.findNodeList("aa").size());
		filter.setIncludeAttributes("c");
		assertEquals(0, filter.findNodeList("aa").size());
		// 测试包含指定节点
		filter.clearCondition();
		filter.setExcludeNode("c");
		assertEquals(2, filter.findNodeList("aa").size());
	}

}
