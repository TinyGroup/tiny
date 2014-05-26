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
package org.tinygroup.xmlparser.document;

import java.io.IOException;

import org.tinygroup.parser.exception.ParseException;
import org.tinygroup.xmlparser.XmlDocument;
import org.tinygroup.xmlparser.parser.XmlStringParser;

import junit.framework.TestCase;

public class DocumentTest extends TestCase {
	XmlDocument doc = null;
	XmlStringParser stringParser = new XmlStringParser();

	protected void setUp() throws Exception {
		super.setUp();
		doc = stringParser
				.parse("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><?name pidata?><!DOCTYPE rootElement><!DOCTYPE rootElement SYSTEM \"URIreference\"><!DOCTYPE rootElement PUBLIC \"PublicIdentifier\" \"URIreference\"><!--aa--><!--bb--><root a=c><!-- abc --><![CDATA[aasdf]]><b><c></c></b></root>");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetRoot() {
		assertNotNull(doc.getRoot());
		assertEquals("root", doc.getRoot().getNodeName());
	}

	public void testSetRoot() {
		doc.setRoot(stringParser.parse("<aa><b/></aa>").getRoot());
		assertEquals("aa", doc.getRoot().getNodeName());
	}

	public void testGetXmlDeclaration() {
		assertEquals("1.0", doc.getXmlDeclaration().getAttribute("version"));
	}

	public void testGetCommentList() {
		assertEquals(2, doc.getCommentList().size());
	}

	public void testGetDoctypeList() {
		assertEquals(3, doc.getDoctypeList().size());
	}

	public void testGetProcessingInstructionList() {
		assertEquals(1, doc.getProcessingInstructionList().size());
	}

	public void testToString() {
		System.out.println(doc.toString());
	}

	public void testTimes() {
		long l1 = System.currentTimeMillis();
		int ti = 10000;
		try {
			for (int i = 0; i < ti; i++) {
				doc = stringParser
						.parse("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><?name pidata?><!DOCTYPE rootElement><!DOCTYPE rootElement SYSTEM \"URIreference\"><!DOCTYPE rootElement PUBLIC \"PublicIdentifier\" \"URIreference\"><!--aa--><!--bb--><root a=c><!-- abc --><![CDATA[aasdf]]><b><c></c></b></root>");
			}
			long l2 = System.currentTimeMillis();
			System.out.println("times:" + (l2 - l1));
			System.out.println("String length:" + doc.toString().length() * ti
					/ (l2 - l1) * 1000);
		} catch (ParseException e) {
			fail(e.getMessage());
		}
	}

	public void testWrite() {
		try {
			doc.write(System.out);
		} catch (IOException e) {
			fail("不应该有异常" + e.getMessage());

		}
	}

}
