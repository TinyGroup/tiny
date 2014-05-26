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
package org.tinygroup.convert;

import junit.framework.TestCase;
import org.tinygroup.convert.textxml.fixwidth.TextToXml;
import org.tinygroup.convert.textxml.fixwidth.XmlToText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestTextXml extends TestCase {
	private static final String TEXT = "标识 姓名                 地址    Email,11   haha                 address email,12   哈哈哈哈哈哈哈哈哈哈 address email,13   haha1111111111       address email,";
	private static final String XML = "<students><student><id>11</id><name>haha</name><address>address</address><Email>email</Email></student><student><id>12</id><name>哈哈哈哈哈哈哈哈哈哈</name><address>address</address><Email>email</Email></student><student><id>13</id><name>haha1111111111</name><address>address</address><Email>email</Email></student></students>";

	public void testText2Xml() throws ConvertException {
		Map<String, String> titleMap = new HashMap<String, String>();
		titleMap.put("标识", "id");
		titleMap.put("姓名", "name");
		titleMap.put("地址", "address");
		titleMap.put("邮箱", "email");
		TextToXml textToXml = new TextToXml(titleMap, "students", "student",
				",");
		System.out.println(textToXml.convert(TEXT));
		assertEquals(XML, textToXml.convert(TEXT));
	}

	public void testXml2Text() throws ConvertException {
		Map<String, String> titleMap = new HashMap<String, String>();
		List<String> fieldList = new ArrayList<String>();
		titleMap.put("id", "标识");
		titleMap.put("name", "姓名");
		titleMap.put("address", "地址");
		titleMap.put("email", "Email");
		fieldList.add("id");
		fieldList.add("name");
		fieldList.add("address");
		fieldList.add("Email");
		XmlToText xmlToText = new XmlToText(titleMap, fieldList, "students",
				"student", ",");
		System.out.println(xmlToText.convert(XML));
		assertEquals(TEXT, xmlToText.convert(XML));
	}

}
