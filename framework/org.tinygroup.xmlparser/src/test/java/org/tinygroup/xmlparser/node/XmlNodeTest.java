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
package org.tinygroup.xmlparser.node;

import java.io.File;
import java.util.List;

import org.tinygroup.commons.file.IOUtils;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;
import org.tinygroup.xmlparser.XmlDocument;
import org.tinygroup.xmlparser.parser.XmlStringParser;

import junit.framework.TestCase;

public class XmlNodeTest extends TestCase {

	XmlDocument doc = null;

	protected void setUp() throws Exception {
		File file1 = new File("src/test/resources/dtd.xml");
		System.out.println(file1.getAbsolutePath());
		XmlStringParser parser = new XmlStringParser();
		FileObject file = VFS.resolveFile("file:" + file1.getAbsolutePath());
		doc = parser.parse(IOUtils.readFromInputStream(file.getInputStream(), "utf-8"));
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetSubNodes() {
		XmlNode node = doc.getRoot();
		List<XmlNode> subNodes = node.getSubNodes("from");
		assertEquals(5, subNodes.size());
	}

	public void testGetProgenyNodes() {
		XmlNode node = doc.getRoot();
		List<XmlNode> progenyNodes = node.getSubNodesRecursively("from");
		assertEquals(7, progenyNodes.size());
	}

	public void testGetSubNode() {
		XmlNode node = doc.getRoot();
		XmlNode subNode = node.getSubNode("zhang1");
		assertNull(subNode);
	}

	public void testGetProgenyNode() {
		XmlNode node = doc.getRoot();
		XmlNode progenyNode = node.getSubNodeRecursively("zhang1");
		assertNotNull(progenyNode);
		assertEquals("abcd", progenyNode.getContent());
	}

}
