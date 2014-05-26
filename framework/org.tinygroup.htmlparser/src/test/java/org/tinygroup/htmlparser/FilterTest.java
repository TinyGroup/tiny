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

import java.io.File;
import java.util.Hashtable;
import java.util.List;

import junit.framework.TestCase;

import org.tinygroup.commons.file.IOUtils;
import org.tinygroup.htmlparser.node.HtmlNode;
import org.tinygroup.htmlparser.parser.HtmlStringParser;
import org.tinygroup.parser.filter.NameFilter;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;

public class FilterTest extends TestCase {
	HtmlDocument doc = null;

	
	protected void setUp() throws Exception {
		File file1 = new File("src/test/resources/dtd.html");
		System.out.println(file1.getAbsolutePath());
		HtmlStringParser parser = new HtmlStringParser();
		FileObject file = VFS.resolveFile("file:" + file1.getAbsolutePath());
		doc = parser.parse(IOUtils.readFromInputStream(file.getInputStream(), "utf-8"));
		super.setUp();
	}

	public void testParser() {
		System.out.println(doc);
		assertNotNull(doc);
	}

	public void testFindNode() {
		NameFilter<HtmlNode> filter = new NameFilter<HtmlNode>(doc.getRoot());
		List<HtmlNode> nodes = filter.findNodeList("note");
		assertEquals(1, nodes.size());
	}

	public void testIncludeAttributeFindNode() {
		NameFilter<HtmlNode> filter = new NameFilter<HtmlNode>(doc.getRoot());
		List<HtmlNode> nodes = filter.findNodeList("to");
		assertEquals(2, nodes.size());
		Hashtable<String, String> includeAttribute = new Hashtable<String, String>();
		includeAttribute.put("a", "1");
		filter.setIncludeAttribute(includeAttribute);
		List<HtmlNode> nodes1 = filter.findNodeList("to");
		assertEquals(1, nodes1.size());
	}

	public void testExcludeAttributeFindNode() {
		NameFilter<HtmlNode> filter = new NameFilter<HtmlNode>(doc.getRoot());
		List<HtmlNode> nodes = filter.findNodeList("to");
		assertEquals(2, nodes.size());

		Hashtable<String, String> excludeAttribute = new Hashtable<String, String>();
		excludeAttribute.put("a", "1");
		filter.setExcludeAttribute(excludeAttribute);
		List<HtmlNode> nodes1 = filter.findNodeList("to");
		assertEquals(1, nodes1.size());
	}

	public void testIncludeAttributeV() {
		NameFilter<HtmlNode> filter = new NameFilter<HtmlNode>(doc.getRoot());
		List<HtmlNode> nodes = filter.findNodeList("from");
		assertEquals(4, nodes.size());

		String[] incV1 = { "a", "b" };
		filter.setIncludeAttributes(incV1);
		List<HtmlNode> nodes1 = filter.findNodeList("from");
		assertEquals(1, nodes1.size());

		String[] incV2 = { "a" };
		filter.setIncludeAttributes(incV2);
		List<HtmlNode> nodes2 = filter.findNodeList("from");
		assertEquals(2, nodes2.size());
	}

	public void testExcludeAttributeV() {
		NameFilter<HtmlNode> filter = new NameFilter<HtmlNode>(doc.getRoot());
		List<HtmlNode> nodes = filter.findNodeList("from");
		assertEquals(4, nodes.size());

		String[] excV1 = { "a", "b" };
		filter.setExcludeAttribute(excV1);
		List<HtmlNode> nodes1 = filter.findNodeList("from");
		assertEquals(2, nodes1.size());

		String[] excV2 = { "a" };
		filter.setExcludeAttribute(excV2);
		List<HtmlNode> nodes2 = filter.findNodeList("from");
		assertEquals(2, nodes2.size());
	}

	public void testIncludeText() {
		NameFilter<HtmlNode> filter = new NameFilter<HtmlNode>(doc.getRoot());
		List<HtmlNode> nodes = filter.findNodeList("from");
		assertEquals(4, nodes.size());

		filter.setIncludeText("John");
		List<HtmlNode> nodes1 = filter.findNodeList("from");
		assertEquals(2, nodes1.size());
	}

	public void testExcludeText() {
		NameFilter<HtmlNode> filter = new NameFilter<HtmlNode>(doc.getRoot());
		List<HtmlNode> nodes = filter.findNodeList("from");
		assertEquals(4, nodes.size());

		filter.setExcludeText("John");
		List<HtmlNode> nodes1 = filter.findNodeList("from");
		assertEquals(2, nodes1.size());
	}

	public void testIncludeNode() {
		NameFilter<HtmlNode> filter = new NameFilter<HtmlNode>(doc.getRoot());
		List<HtmlNode> nodes = filter.findNodeList("body");
		assertEquals(4, nodes.size());

		filter.setIncludeNode("a", "b");
		List<HtmlNode> nodes1 = filter.findNodeList("body");
		assertEquals(1, nodes1.size());

		filter.setIncludeNode("a");
		assertEquals(2, filter.findNodeList("body").size());
	}

	public void testExcludeNode() {
		NameFilter<HtmlNode> filter = new NameFilter<HtmlNode>(doc.getRoot());
		List<HtmlNode> nodes = filter.findNodeList("body");
		assertEquals(4, nodes.size());

		filter.setExcludeNode("a", "b");
		List<HtmlNode> nodes1 = filter.findNodeList("body");
		assertEquals(2, nodes1.size());

		filter.setExcludeNode("a");
		assertEquals(2, filter.findNodeList("body").size());
	}

	public void testXorNode() {
		NameFilter<HtmlNode> filter = new NameFilter<HtmlNode>(doc.getRoot());
		List<HtmlNode> nodes = filter.findNodeList("body");
		assertEquals(4, nodes.size());

		filter.setXorSubNode("a", "c");
		assertEquals(2, filter.findNodeList("body").size());
	}

	public void testXorPro() {
		NameFilter<HtmlNode> filter = new NameFilter<HtmlNode>(doc.getRoot());
		List<HtmlNode> nodes = filter.findNodeList("from");
		assertEquals(4, nodes.size());

		filter.setXorProperties("a", "b");
		assertEquals(2, filter.findNodeList("from").size());
	}
}
