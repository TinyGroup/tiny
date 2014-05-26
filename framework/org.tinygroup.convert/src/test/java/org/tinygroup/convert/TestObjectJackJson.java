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

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.tinygroup.convert.objectjson.jackson.JsonToObject;
import org.tinygroup.convert.objectjson.jackson.ObjectToJson;

import java.util.HashMap;
import java.util.Map;


public class TestObjectJackJson extends AbstractConvertTestCase {


    protected void setUp() throws Exception {
        super.setUp();

    }


    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testJson2Object() throws ConvertException {
        String json = "{\"id\":1,\"name\":\"haha\",\"email\":\"email\",\"address\":\"address\",\"birthday\":{\"birthday\":\"2010-11-22\"}}";
        JsonToObject<Student> jsonToObject = new JsonToObject<Student>(Student.class);
        Student student = jsonToObject.convert(json);
        assertEquals("email", student.getEmail());
        assertEquals(1, student.getId());
        assertEquals("haha", student.getName());
        assertEquals("2010-11-22", student.getBirthday().getBirthday());
    }

    public void testObject2Json() throws ConvertException {
        Student student = createStudent();
        ObjectToJson<Student> objectToJson = new ObjectToJson<Student>();
        System.out.println(objectToJson.convert(student));

    }

    public void testMap2Json() throws ConvertException {
        Map<String, Object> maps = new HashMap<String, Object>();
        Student student = createStudent();
        maps.put("student", student);
        maps.put("id", 1234);
        ObjectToJson objectToXml = new ObjectToJson(JsonSerialize.Inclusion.NON_NULL);
        System.out.println(objectToXml.convert(maps));

    }

    public void testJson2Map() throws ConvertException {

        String json = "{\"student\":{\"id\":1,\"name\":\"haha\",\"email\":\"email\",\"address\":\"address\",\"birthday\":{\"birthday\":\"2010-11-22\"}},\"id\":1234}";
        JsonToObject jsonToObject = new JsonToObject(HashMap.class);
        Map<String, Object> maps = (Map<String, Object>) jsonToObject.convert(json);
        assertFalse(maps.get("student") instanceof Student);
    }

}
