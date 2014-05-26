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

import org.tinygroup.convert.objectxml.simple.ObjectToXml;
import org.tinygroup.convert.objectxml.simple.XmlToObject;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlStringParser;

import java.util.ArrayList;
import java.util.List;

public class TestObjectXmlWithSimple extends AbstractConvertTestCase {

	public void testObject2Xml() throws ConvertException {
		Student student1=createStudent();
		Student student2=createStudent1();
		List<Student> students=new ArrayList<Student>();
		students.add(student1);
		students.add(student2);
		ObjectToXml<Student> objectToXml=new ObjectToXml<Student>(true, "students", "student");
	  	String xml = objectToXml.convert(students);
	  	XmlNode node= new XmlStringParser().parse(xml).getRoot();
	  	List<XmlNode> subNodes = node.getSubNodes("student");
	  	assertEquals(2, subNodes.size());
	  	XmlNode studentNode = subNodes.get(0);
	  	assertEquals(studentNode.getAttribute("id"), "1");
		assertEquals(studentNode.getAttribute("name"), "haha");
		assertEquals(studentNode.getAttribute("email"), "email");
	}
	
	
	public void testXml2Object() throws ConvertException {
		
		String xml="<students>" +
				        "<student  id=\"1\" name=\"haha\" email=\"email\" " +
				        "      address=\"address\"/>" +
				        "<student  id=\"2\" name=\"haha2\" email=\"email2\" " +
				        "       address=\"address2\"/>" +
				     "</students>";
		XmlToObject<Student> xmlToObject=new XmlToObject<Student>(Student.class.getName(), "students", "student");
		List<Student> students=xmlToObject.convert(xml);
		Student student=students.get(0);
    	assertEquals(2, students.size());
		assertEquals("haha", student.getName());
		assertEquals("email", student.getEmail());
		assertEquals("address", student.getAddress());
	}
}
