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

import java.io.File;
import java.util.Hashtable;
import java.util.List;

import junit.framework.TestCase;

import org.tinygroup.commons.file.IOUtils;
import org.tinygroup.parser.filter.NameFilter;
import org.tinygroup.parser.filter.QuickNameFilter;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlStringParser;

public class FilterTest extends TestCase {
	XmlDocument doc = null;

	
	protected void setUp() throws Exception {
		File file1 = new File("src/test/resources/dtd.xml");
		System.out.println(file1.getAbsolutePath());
		XmlStringParser parser = new XmlStringParser();
		FileObject file = VFS.resolveFile("file:" + file1.getAbsolutePath());
		doc = parser.parse(IOUtils.readFromInputStream(file.getInputStream(), "utf-8"));
		super.setUp();
	}

	public void testParser() {
		System.out.println(doc);
		assertNotNull(doc);
	}

	public void testFindNode() {
		NameFilter<XmlNode> filter = new NameFilter<XmlNode>(doc.getRoot());
		List<XmlNode> nodes = filter.findNodeList("note");
		assertEquals(1, nodes.size());
	}

	public void testIncludeAttributeFindNode() {
		NameFilter<XmlNode> filter = new NameFilter<XmlNode>(doc.getRoot());
		List<XmlNode> nodes = filter.findNodeList("to");
		assertEquals(2, nodes.size());
		Hashtable<String, String> includeAttribute = new Hashtable<String, String>();
		includeAttribute.put("a", "1");
		filter.setIncludeAttribute(includeAttribute);
		List<XmlNode> nodes1 = filter.findNodeList("to");
		assertEquals(1, nodes1.size());
	}

	public void testExcludeAttributeFindNode() {
		NameFilter<XmlNode> filter = new NameFilter<XmlNode>(doc.getRoot());
		List<XmlNode> nodes = filter.findNodeList("to");
		assertEquals(2, nodes.size());

		Hashtable<String, String> excludeAttribute = new Hashtable<String, String>();
		excludeAttribute.put("a", "1");
		filter.setExcludeAttribute(excludeAttribute);
		List<XmlNode> nodes1 = filter.findNodeList("to");
		assertEquals(1, nodes1.size());
	}

	public void testIncludeAttributeV() {
		NameFilter<XmlNode> filter = new NameFilter<XmlNode>(doc.getRoot());
		List<XmlNode> nodes = filter.findNodeList("from");
		assertEquals(7, nodes.size());

		String[] incV1 = { "a", "b" };
		filter.setIncludeAttributes(incV1);
		List<XmlNode> nodes1 = filter.findNodeList("from");
		assertEquals(1, nodes1.size());

		String[] incV2 = { "a" };
		filter.setIncludeAttributes(incV2);
		List<XmlNode> nodes2 = filter.findNodeList("from");
		assertEquals(2, nodes2.size());
	}

	public void testExcludeAttributeV() {
		NameFilter<XmlNode> filter = new NameFilter<XmlNode>(doc.getRoot());
		List<XmlNode> nodes = filter.findNodeList("from");
		assertEquals(7, nodes.size());

		String[] excV1 = { "a", "b" };
		filter.setExcludeAttribute(excV1);
		List<XmlNode> nodes1 = filter.findNodeList("from");
		assertEquals(4, nodes1.size());

		String[] excV2 = { "a" };
		filter.setExcludeAttribute(excV2);
		List<XmlNode> nodes2 = filter.findNodeList("from");
		assertEquals(5, nodes2.size());
	}

	public void testIncludeText() {
		NameFilter<XmlNode> filter = new NameFilter<XmlNode>(doc.getRoot());
		List<XmlNode> nodes = filter.findNodeList("from");
		assertEquals(7, nodes.size());

		filter.setIncludeText("John");
		List<XmlNode> nodes1 = filter.findNodeList("from");
		assertEquals(2, nodes1.size());
	}

	public void testExcludeText() {
		NameFilter<XmlNode> filter = new NameFilter<XmlNode>(doc.getRoot());
		List<XmlNode> nodes = filter.findNodeList("from");
		assertEquals(7, nodes.size());

		filter.setExcludeText("John");
		List<XmlNode> nodes1 = filter.findNodeList("from");
		assertEquals(5, nodes1.size());
	}

	public void testIncludeNode() {
		NameFilter<XmlNode> filter = new NameFilter<XmlNode>(doc.getRoot());
		List<XmlNode> nodes = filter.findNodeList("body");
		assertEquals(4, nodes.size());

		filter.setIncludeNode("a", "b");
		List<XmlNode> nodes1 = filter.findNodeList("body");
		assertEquals(1, nodes1.size());

		filter.setIncludeNode("a");
		assertEquals(2, filter.findNodeList("body").size());
	}

	public void testExcludeNode() {
		NameFilter<XmlNode> filter = new NameFilter<XmlNode>(doc.getRoot());
		List<XmlNode> nodes = filter.findNodeList("body");
		assertEquals(4, nodes.size());

		filter.setExcludeNode("a", "b");
		List<XmlNode> nodes1 = filter.findNodeList("body");
		assertEquals(2, nodes1.size());

		filter.setExcludeNode("a");
		assertEquals(2, filter.findNodeList("body").size());
	}

	public void testXorNode() {
		NameFilter<XmlNode> filter = new NameFilter<XmlNode>(doc.getRoot());
		List<XmlNode> nodes = filter.findNodeList("body");
		assertEquals(4, nodes.size());

		filter.setXorSubNode("a", "c");
		assertEquals(2, filter.findNodeList("body").size());
	}

	public void testXorPro() {
		NameFilter<XmlNode> filter = new NameFilter<XmlNode>(doc.getRoot());
		List<XmlNode> nodes = filter.findNodeList("from");
		assertEquals(7, nodes.size());

		filter.setXorProperties("a", "b");
		assertEquals(3, filter.findNodeList("from").size());
	}
}
