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

import org.tinygroup.convert.objectjson.xstream.JsonToObject;
import org.tinygroup.convert.objectjson.xstream.ObjectToJson;

public class TestObjectJson extends AbstractConvertTestCase {

	
	protected void setUp() throws Exception {
		super.setUp();
		
	}

	
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testJson2Object(){
		String json = "{\"student\": {" +
        "\"id\": 1," +
        "\"name\": \"haha\"," +
        "\"email\": \"email\"," +
        "\"address\": \"address\"," +
        "\"birthday\": {" +
            "\"birthday\": \"2010-11-22\"" +
        "}" +
       "}}";
		JsonToObject<Student>  jsonToObject=new JsonToObject<Student>(Student.class);
		Student student= jsonToObject.convert(json);
		assertEquals("haha", student.getName());
		assertEquals("email", student.getEmail());
		assertEquals("address", student.getAddress());
		assertEquals("2010-11-22", student.getBirthday().getBirthday());

	}
	
	public void testObject2Json(){
		Student student = createStudent();
	    ObjectToJson<Student> objectToJson=new ObjectToJson<Student>(Student.class);
		System.out.println(objectToJson.convert(student));
		
	}

	
	
	
	public void testObject2JsonWithList(){
		Classes classes = createClasses();
		ObjectToJson<Classes> objectToJson=new ObjectToJson<Classes>(Classes.class);
		System.out.println(objectToJson.convert(classes));
		
	}

	
	
    public void testJson2ObjectWithList(){
		
    	String json = "{\"classes\": {\"students\":[{" +
        "\"id\": 1," +
        "\"name\": \"haha\"," +
        "\"email\": \"email\"," +
        "\"address\": \"address\"," +
        "\"birthday\": {" +
          "\"birthday\": \"2010-11-22\"" +
        "}" +
       "},{" +
        "\"id\": 2," +
        "\"name\": \"tom\"," +
        "\"email\": \"tom@125.com\"," +
        "\"address\": \"china\"," +
        "\"birthday\": {" +
          "\"birthday\": \"2010-11-22\"" +
        "}" +
      "}]}}";
    	JsonToObject<Classes>  jsonToObject=new JsonToObject<Classes>(Classes.class);
    	Classes classes= jsonToObject.convert(json);
    	Student student=classes.getStudents().get(0);
    	assertEquals(2, classes.getStudents().size());
		assertEquals("haha", student.getName());
		assertEquals("email", student.getEmail());
		assertEquals("address", student.getAddress());
		assertEquals("2010-11-22", student.getBirthday().getBirthday());
    	
	}
	
	
}
