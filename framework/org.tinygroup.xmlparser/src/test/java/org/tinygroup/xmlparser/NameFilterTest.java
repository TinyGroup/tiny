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

import org.tinygroup.parser.filter.FastNameFilter;
import org.tinygroup.parser.filter.NameFilter;
import org.tinygroup.parser.filter.QuickNameFilter;
import org.tinygroup.xmlparser.node.XmlNode;

import junit.framework.TestCase;

//TODO 增加个数时，会内存堆栈不够
public class NameFilterTest extends TestCase {
	XmlNode node = null;

	public NameFilterTest() {
		node = new XmlNode("root");
		for (int i = 0; i < 60; i++) {
			XmlNode a = node.addNode(new XmlNode("a" + i));
			for (int j = 0; j < 60; j++) {
				XmlNode b = a.addNode(new XmlNode("b" + j));
				for (int k = 0; k < 60; k++) {
					b.addNode(new XmlNode("c" + k));
				}
			}
		}
	}

	protected void setUp() throws Exception {
		super.setUp();

	}

	protected void tearDown() throws Exception {
		super.tearDown();
		node = null;
	}

	public void testSpeed() {
		long t21 = System.currentTimeMillis();
		QuickNameFilter<XmlNode> quick = new QuickNameFilter(node);
		long t22 = System.currentTimeMillis();
		System.out.println("quick用时" + (t22 - t21));
		long t1 = System.currentTimeMillis();
		for (int x = 0; x < 10000; x++) {
			XmlNode node = quick.findNode("b6");
		}
		// System.out.println(nodeName);
		long t2 = System.currentTimeMillis();
		System.out.println("QuickNameFilter用时" + (t2 - t1));
	}

	public void testSpeed1() {
long t21 = System.currentTimeMillis();
FastNameFilter<XmlNode> fast = new FastNameFilter(node);
long t22 = System.currentTimeMillis();
System.out.println("fast用时" + (t22 - t21));
long t1 = System.currentTimeMillis();
for (int x = 0; x < 10000; x++) {
    XmlNode node = fast.findNode("b6");
}
// System.out.println(nodeName);
long t2 = System.currentTimeMillis();
System.out.println("FastNameFilter用时" + (t2 - t1));
	}

	public void testSpeed2() {
long t11 = System.currentTimeMillis();
NameFilter filter = new NameFilter(node);
long t12 = System.currentTimeMillis();
System.out.println("Name用时" + (t12 - t11));
long t1 = System.currentTimeMillis();
String nodeName = null;
for (int x = 0; x < 10; x++) {
    nodeName = filter.findNode("b6").toString();
}
long t2 = System.currentTimeMillis();
// System.out.println(nodeName);
System.out.println("NameFilter用时" + (t2 - t1));
	}

}
