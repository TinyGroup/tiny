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

import java.io.IOException;

import org.tinygroup.htmlparser.node.HtmlNode;

import junit.framework.TestCase;

public class INodeTest extends TestCase {
	HtmlNode node = null;

	protected void setUp() throws Exception {
		node = new HtmlNode("aa");
		super.setUp();
	}

	public void testEnDeCode() {
		node = new HtmlNode(HtmlNodeType.TEXT);
		node.setContent("&amp;&lt;&gt;&quot;&nbsp;&copy;&reg;");
		System.out.println(node.getContent());
		try {
			node.write(System.out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void testEnDeCode1() {
		System.out.println();
		node = new HtmlNode("aa");
		node.setAttribute("a", "&amp;&lt;&gt;&quot;&nbsp;&copy;&reg;");
		System.out.println(node.getAttribute("a"));
		try {
			node.write(System.out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetHeader() {
		node.setAttribute("a", "b");
		StringBuffer sb = new StringBuffer();
		node.getHeader(sb);
		assertEquals("<aa a=\"b\">", sb.toString());
	}

	public void testGetFooter() {
		StringBuffer sb = new StringBuffer();
		node.getFooter(sb);
		assertEquals("</aa>", sb.toString());

	}

	public void testGetRoot() {
		HtmlNode n = new HtmlNode("a");
		HtmlNode n1 = new HtmlNode("b");
		n.addNode(n1);
		HtmlNode n2 = new HtmlNode("b");
		n1.addNode(n2);
		assertEquals(n, n2.getRoot());
	}

	public void testGetParent() {
		HtmlNode n = new HtmlNode("a");
		HtmlNode n1 = new HtmlNode("b");
		n.addNode(n1);
		HtmlNode n2 = new HtmlNode("b");
		n1.addNode(n2);
		assertEquals(n, n1.getParent());
		assertEquals(n1, n2.getParent());
	}

	public void testGetBody() {
		HtmlNode n = new HtmlNode(HtmlNodeType.COMMENT);
		n.setContent("abc");
		assertEquals("abc", n.getContent());

		n = new HtmlNode(HtmlNodeType.TEXT);
		n.setContent("abc");
		assertEquals("abc", n.getContent());

		n = new HtmlNode(HtmlNodeType.CDATA);
		n.setContent("abc");
		assertEquals("abc", n.getContent());
	}

	public void testWrite() {
		node.setContent("abc");
		node.addNode(new HtmlNode("b"));
		try {
			node.write(System.out);
		} catch (IOException e) {
			fail("不应该有异常" + e.getMessage());
		}
	}

	public void testGetHtmlNodeType() {
		assertEquals(HtmlNodeType.ELEMENT, node.getNodeType());
	}

	public void testGetAttribute() {
		node.setAttribute("a", "b");
		assertEquals("b", node.getAttribute("a"));
	}

	public void testAddNode() {
		HtmlNode n = new HtmlNode("c");
		node.addNode(n);
		assertEquals("<aa><c></c></aa>", node.toString());
	}

	public void testRemoveNode() {
		HtmlNode n = new HtmlNode("c");
		node.addNode(n);
		assertEquals("<aa><c></c></aa>", node.toString());
		node.removeNode(n);
		assertEquals("<aa></aa>", node.toString());
		assertNull(n.getParent());
	}

	public void testGetContent() {
		HtmlNode node = new HtmlNode(HtmlNodeType.COMMENT);
		node.setContent("aaa");
		assertEquals("aaa", node.getContent().toString());
	}

	public void testToStringBuffer() {
		node.setAttribute("a", "b");
		assertEquals("<aa a=\"b\"></aa>", node.toStringBuffer().toString());
	}

	public void testGetAttributes() {
		assertNull(node.getAttribute("a"));
		node.setAttribute("a", "b");
		assertEquals("b", node.getAttribute("a"));
		node.setAttribute("a", null);
		assertNull(node.getAttribute("a"));
	}

	public void testGetSubNodes() {
		for (int i = 0; i < 100; i++) {
			node.addNode(new HtmlNode("node" + i));
		}
		assertEquals(100, node.getSubNodes().size());
	}

	public void testToStringEveryType() {
		HtmlNode n = new HtmlNode(HtmlNodeType.CDATA);
		n.setContent("aa");
		assertEquals("<![CDATA[aa]]>", n.toString());

		n = new HtmlNode(HtmlNodeType.COMMENT);
		n.setContent("aa");
		assertEquals("<!--aa-->", n.toString());

		n = new HtmlNode(HtmlNodeType.DOCTYPE);
		n.setContent("aa");
		assertEquals("<!DOCTYPE aa>", n.toString());

		n = new HtmlNode(HtmlNodeType.PROCESSING_INSTRUCTION);
		n.setContent("aa bb  cc");
		assertEquals("<?aa bb  cc?>", n.toString());
		n = new HtmlNode(HtmlNodeType.TEXT);
		n.setContent("aa");
		assertEquals("aa", n.toString());
	}
}
