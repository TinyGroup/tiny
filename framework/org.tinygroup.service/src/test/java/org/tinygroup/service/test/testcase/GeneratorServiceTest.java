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
package org.tinygroup.service.test.testcase;

import java.util.List;

import junit.framework.TestCase;

import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.service.test.base.ServiceUser;
import org.tinygroup.service.util.ServiceTestUtil;

public class GeneratorServiceTest extends TestCase {
	
	
	public void testUserObject() {
		Context context = new ContextImpl();
		context.put("user.name", "username");
		context.put("user.age", 11);
		context.put("user.male", true);
		ServiceTestUtil.execute("serviceUserObject", context);
		ServiceUser user2 = context.get("user2");
		assertEquals(11, user2.getAge());
		assertEquals("username",user2.getName());
		assertEquals(true, user2.isMale());
		
	}
	
	
	public void testUserList() {
		Context context = new ContextImpl();
		context.put("user.name", "username");
		context.put("user.age", 11);
		context.put("user.male", true);
		
		context.put("users.name", new String[]{"a","b"});
		context.put("users.age", new String[]{"1","2"});
		context.put("users.male", new String[]{"true","false"});
		
		ServiceTestUtil.execute("serviceUserList", context);
		List<ServiceUser> users = context.get("userList");
		
		assertEquals(3, users.size());
		
		assertEquals(1, users.get(0).getAge());
		assertEquals("a",users.get(0).getName());
		assertEquals(true, users.get(0).isMale());
		
		assertEquals(2, users.get(1).getAge());
		assertEquals("b",users.get(1).getName());
		assertEquals(false, users.get(1).isMale());
		
		assertEquals(11, users.get(2).getAge());
		assertEquals("username",users.get(2).getName());
		assertEquals(true, users.get(2).isMale());
		
	}
	
	
	public void testUserArray() {
		Context context = new ContextImpl();
		
		context.put("users.name", new String[]{"a","b"});
		context.put("users.age", new String[]{"1","2"});
		context.put("users.male", new String[]{"true","false"});
		
		ServiceTestUtil.execute("serviceUserArray", context);
		ServiceUser[] users = context.get("userArray");
		
		assertEquals(2, users.length);
		
		assertEquals(1, users[0].getAge());
		assertEquals("a",users[0].getName());
		assertEquals(true, users[0].isMale());
		
		assertEquals(2, users[1].getAge());
		assertEquals("b",users[1].getName());
		assertEquals(false, users[1].isMale());
		
		
	}
}
