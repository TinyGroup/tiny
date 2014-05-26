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
package org.tinygroup.config.util;

import java.util.List;

import junit.framework.TestCase;

import org.tinygroup.xmlparser.node.XmlNode;

public class AppConfigUtilTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetPropertyName() {
		XmlNode appNode = new XmlNode("aa");
		appNode.setAttribute("a", "1");
		appNode.setAttribute("b", "1");
		XmlNode compNode = new XmlNode("aa");
		compNode.setAttribute("b", "2");
		compNode.setAttribute("c", "2");
		assertEquals("1", ConfigurationUtil.getPropertyName(appNode, compNode, "a"));
		assertEquals("1", ConfigurationUtil.getPropertyName(appNode, compNode, "b"));
		assertEquals("2", ConfigurationUtil.getPropertyName(appNode, compNode, "c"));
		assertEquals("3",
				ConfigurationUtil.getPropertyName(appNode, compNode, "d", "3"));
	}

	public void testCombineSubListXmlNodeXmlNodeStringString() {
		XmlNode appNode = new XmlNode("aa");
		XmlNode compNode = new XmlNode("aa");
		for (int i = 0; i < 3; i++) {
			appNode.addNode(new XmlNode("aa" + i));
			compNode.addNode(new XmlNode("bb" + i));
		}
		assertEquals(6, ConfigurationUtil.combineSubList(appNode, compNode).size());
	}

	public void testCombineSubListXmlNodeXmlNode() {
		XmlNode appNode = new XmlNode("aa");
		XmlNode compNode = new XmlNode("aa");
		for (int i = 0; i < 3; i++) {
			XmlNode node = new XmlNode("aa");
			node.setAttribute("a", "" + i);
			node.setAttribute("b" + (i * i), "" + (i * i + 1));
			appNode.addNode(node);
			XmlNode node2 = new XmlNode("aa");
			node2.setAttribute("a", "" + i);
			node2.setAttribute("b" + (i * 2), "" + (i * 2 + 2));
			compNode.addNode(node2);
		}
		List<XmlNode> combineSubList = ConfigurationUtil.combineSubList(appNode,
				compNode, "aa", "a");
		assertEquals(3, combineSubList.size());
	}

}
