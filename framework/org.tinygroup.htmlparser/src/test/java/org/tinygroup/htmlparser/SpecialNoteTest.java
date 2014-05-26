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

import org.tinygroup.htmlparser.node.HtmlNode;
import org.tinygroup.htmlparser.parser.HtmlStringParser;

import junit.framework.TestCase;

public class SpecialNoteTest extends TestCase {
	HtmlNode node = null;

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetPropertyName() {
		node = new HtmlStringParser().parse("<a:b abc:dd=\"aaa\" />").getRoot();
		System.out.println(node.toString());
		assertNotNull(node.getAttribute("abc:dd"));
	}
}
