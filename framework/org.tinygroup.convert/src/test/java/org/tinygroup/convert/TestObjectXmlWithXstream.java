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

import org.tinygroup.convert.objectxml.xstream.ObjectToXml;
import org.tinygroup.convert.objectxml.xstream.XmlToObject;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlStringParser;

import java.util.List;

public class TestObjectXmlWithXstream extends AbstractConvertTestCase {

	String studentXml = "<student><id>1</id><name>haha</name><email>email</email><address>address</address><birthday><birthday>2010-11-22</birthday></birthday></student>";
	
	public void testObject2Xml() {
		Student student1 = createStudent();
//		Student student2 = createStudent1();
//		List<Student> students = new ArrayList<Student>();
//		students.add(student1);
//		students.add(student2);
		ObjectToXml<Student> objectToXml = new ObjectToXml<Student>(
				Student.class);
		String xml = objectToXml.convert(student1);
		XmlNode node= new XmlStringParser().parse(xml).getRoot();
		assertEquals(node.getSubNode("id").getContent(), "1");
		assertEquals(node.getSubNode("name").getContent(), "haha");
		assertEquals(node.getSubNode("email").getContent(), "email");
	}

	public void testXml2Object() throws ConvertException {

		String xml = "<students>" +
				"<student>" +
				    "<id>1</id><name>haha</name><email>email</email>" +
				    "<address>address</address>" +
				    "<birthday><birthday>2010-11-22</birthday></birthday>" +
				 "</student>" +
				 "<student>" +
				     "<id>2</id><name>haha2</name><email>email2</email>" +
				     "<address>address2</address>" +
				     "<birthday><birthday>2010-11-23</birthday></birthday>" +
				  "</student>" +
				"</students>";
		XmlToObject<Student> xmlToObject = new XmlToObject<Student>(
				Student.class, "students");
		List<Student> students = (List<Student>) xmlToObject.convert(xml);
		Student student = students.get(0);
		assertEquals(2, students.size());
		assertEquals("haha", student.getName());
		assertEquals("email", student.getEmail());
		assertEquals("address", student.getAddress());
		assertEquals("2010-11-22", student.getBirthday().getBirthday());
	}
}
