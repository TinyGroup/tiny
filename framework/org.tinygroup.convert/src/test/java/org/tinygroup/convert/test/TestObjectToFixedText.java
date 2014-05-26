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
package org.tinygroup.convert.test;

import junit.framework.TestCase;
import org.tinygroup.convert.ConvertException;
import org.tinygroup.convert.objecttxt.fixwidth.ObjectToText;
import org.tinygroup.convert.objecttxt.fixwidth.TextToObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class TestObjectToFixedText extends TestCase {
	private static String testText="name               address         length_name123456         address123456   1     _name1234567        address1234567  1     _name12345678       address12345678 1     _哈哈哈哈哈哈哈哈哈 address12345678 1     _";
	public void testToFixedText() throws ConvertException {
		List<String> properties = new ArrayList<String>();
		properties.add("name");
		properties.add("address");
		properties.add("length");
		ObjectToText<User> ottUser = new ObjectToText<User>(new HashMap<String, String>(), properties, "_");
		List<User> users = getUsers();
		String text = ottUser.convert(users);
		assertEquals(testText, text);
	}
	public void testToObject() throws ConvertException {
		TextToObject<User> ttoUser = new TextToObject<User>(User.class, new HashMap<String, String>(), "_");
		List<User> users = ttoUser.convert(testText);
		List<User> excepted = getUsers();
		assertEquals(excepted.get(0).getName(), users.get(0).getName());
		assertEquals(excepted.get(0).getAddress(), users.get(0).getAddress());
		assertEquals(excepted.get(0).getLength(), users.get(0).getLength());
		assertEquals(excepted.get(1).getName(), users.get(1).getName());
		assertEquals(excepted.get(2).getName(), users.get(2).getName());
	}
	
	private List<User> getUsers(){
		List<User> users = new ArrayList<User>();
		User u1 = new User();
		u1.setAddress("address123456");
		u1.setLength(1);
		u1.setName("name123456");
		users.add(u1);
		
		User u2 = new User();
		u2.setAddress("address1234567");
		u2.setLength(1);
		u2.setName("name1234567");
		users.add(u2);
		
		User u3 = new User();
		u3.setAddress("address12345678");
		u3.setLength(1);
		u3.setName("name12345678");
		users.add(u3);
		
		User u4 = new User();
		u4.setAddress("address12345678");
		u4.setLength(1);
		u4.setName("哈哈哈哈哈哈哈哈哈");
		users.add(u4);
		
		return users;
	}
}
