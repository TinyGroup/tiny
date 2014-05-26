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
package org.tinygroup.ini.impl;

import junit.framework.TestCase;
import org.tinygroup.ini.IniOperator;
import org.tinygroup.ini.Sections;
import org.tinygroup.ini.ValuePair;

import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luoguo on 14-3-29.
 */
public class IniOperatorDefaultTest extends TestCase {
	IniOperator operator;

	public void setUp() throws Exception {
		super.setUp();
		operator = new IniOperatorDefault();
		Sections sections = new Sections();
		operator.setSections(sections);
	}

	public void testGetSections() throws Exception {
		assertNotNull(operator.getSections());
	}

	public void testPut() throws Exception {
		operator.put("aa", "abc", 123);
		operator.put("aa", "abc", 456);

		assertEquals(1, operator.getSection("aa").getValuePairList().size());
		Integer value = operator.get(Integer.class, "aa", "abc");
		assertEquals(value.intValue(), 456);
	}

	public void testAddValuePair() throws Exception {
		operator.add("aa", new ValuePair("bb", "cc", "dd"));
		operator.add("aa", new ValuePair("bb", "cc", "dd"));
		operator.add("bb", new ValuePair("bb", "cc", "dd"));

		assertEquals(2, operator.getSection("aa").getValuePairList().size());
		assertEquals(1, operator.getSection("bb").getValuePairList().size());
	}

	public void testAddValuePairList() throws Exception {
		List<ValuePair> valuePairs = new ArrayList<ValuePair>();
		valuePairs.add(new ValuePair("abc", "1"));
		valuePairs.add(new ValuePair("abc", "2"));
		operator.add("aa", valuePairs);
		operator.add("bb", new ArrayList<ValuePair>());
		assertEquals(2, operator.getSection("aa").getValuePairList().size());
		assertEquals(0, operator.getSection("bb").getValuePairList().size());
	}

	public void testAdd() throws Exception {
		operator.add("aa", "abc", 123);
		operator.add("aa", "abc", 123);
		operator.add("bb", "abc", 123);
		assertEquals(2, operator.getSections().getSection("aa")
				.getValuePairList().size());
		assertEquals(1, operator.getSections().getSection("bb")
				.getValuePairList().size());
	}

	public void testGetValuePairList() throws Exception {
		operator.add("aa", new ValuePair("aa", "cc", "dd"));
		operator.add("aa", new ValuePair("bb", "cc", "dd"));
		operator.add("aa", new ValuePair("bb", "cc", "dd"));
		List<ValuePair> list = operator.getValuePairList("aa", "aa");
		assertEquals(1, list.size());
		list = operator.getValuePairList("aa", "bb");
		assertEquals(2, list.size());
	}

	public void testGetValuePair() throws Exception {
		operator.add("aa", new ValuePair("abc", "123"));
		operator.add("aa", new ValuePair("bbb", "bbb"));
		assertEquals("123", operator.getValuePair("aa", "abc").getValue());
		assertEquals("bbb", operator.getValuePair("aa", "bbb").getValue());
	}

	public void testGetValue() throws Exception {
		operator.put("aa", "abc", 123);
		operator.put("aa", "abc", 456);

		assertEquals("456", operator.get("aa", "abc"));
	}

	public void testGetValue1() throws Exception {
		operator.put("aa", "abc", 123);
		operator.put("aa", "bbb", 123);

		assertEquals("ccc", operator.get("aa", "ccc", "ccc"));
		assertEquals("dd", operator.get("dd", "dd", "dd"));
	}

	public void testGetValue2() throws Exception {
		operator.put("aa", "abc", 123);
		operator.put("bb", "abc", 123.11F);
		operator.put("cc", "abc", 123L);
		operator.put("dd", "abc", true);

		assertEquals(123, operator.get(int.class, "aa", "abc").intValue());
		assertEquals(123.11F, operator.get(float.class, "bb", "abc")
				.floatValue());
		assertEquals(123L, operator.get(long.class, "cc", "abc").longValue());
		assertTrue(operator.get(boolean.class, "dd", "abc").booleanValue());
		assertEquals("123.11", operator.get(String.class, "bb", "abc"));
	}

	public void testGetValue3() throws Exception {
		// assertEquals(111, operator.get(int.class, "aa", "ddd",
		// 111).intValue());
	}

	public void testGetValueList() throws Exception {
		operator.add("aa", "abc", 123);
		operator.add("aa", "abc", 345);
		operator.add("aa", "bbb", 345);

		List<Integer> list = operator.getList(int.class, "aa", "abc");
		assertEquals(2, list.size());
	}

	public void testSet() throws Exception {
	}

	public void testRead() throws Exception {
		String string = ";abc\naa=bb;ccc\r\n[ccc];ddd\naa=bb;ccdd\r\nccc=ddd;aa;bb;cc";
		operator.read(new StringBufferInputStream(string), "UTF-8");
		operator.write(System.out, "UTF-8");
	}

	public void testWrite() throws Exception {
		operator.put("aa", "abc", 123);
		operator.put("aa", "abc", 123);
		operator.put("aa", "abc1", 123);
		operator.put("aa", "abc2", 123);
		operator.write(System.out, "UTF-8");
	}
}
