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

import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.service.test.base.ServiceUser;
import org.tinygroup.service.util.ServiceTestUtil;

import junit.framework.TestCase;

public class UserServiceTest extends TestCase {

	/**
	 * 测试正常用法 对象入参 对象出参
	 */
	public void testUserAdd() {
		Context context = new ContextImpl();
		ServiceUser user = new ServiceUser();
		user.setName("testName");
		user.setAge(10);
		user.setMale(true);
		context.put("user", user);
		ServiceTestUtil.execute("serviceAddServiceUser", context);

		Object user2 = context.get("user2");
		assertEquals(user, user2);
	}

	/**
	 * 测试对象参数配置为非必须时不传参数 
	 * 对象入参(非必须 不传) 对象出参
	 */
	public void testUserAddNull() {
		Context context = new ContextImpl();
		ServiceTestUtil.execute("serviceAddServiceUser", context);
		ServiceUser user2 = context.get("user2");
		assertEquals("user2", user2.getName());
	}

	/**
	 * 正常用法
	 */
	public void testServiceAddServiceUserByName() {
		Context context = new ContextImpl();
		String name = "testUser";
		context.put("name", name);
		ServiceTestUtil.execute("serviceAddServiceUserByName", context);

		ServiceUser user2 = context.get("user2");
		assertEquals(name, user2.getName());
	}

	/**
	 * 测试int简单类型的包装类型 以及简单类型的从String中转换
	 * 入参是String,Integer(在context中是string),出参int
	 */
	public void testServiceSetServiceUserAge() {
		Context context = new ContextImpl();
		context.put("name", "testuser");
		context.put("age", "1");
		ServiceTestUtil.execute("serviceSetServiceUserAge", context);
		assertEquals(1, context.get("i"));
	}

	/**
	 * 测试int简单类型 以及简单类型的从String中转换 入参是String,int(在context中是string),出参无
	 */
	public void testServiceSetServiceUserAgeInt() {
		Context context = new ContextImpl();
		context.put("name", "testuser");
		context.put("age", "1");
		ServiceTestUtil.execute("serviceSetServiceUserAgeInt", context);
	}

	/**
	 * 测试数组功能 入参是String[],int[],出参int
	 */
	public void testServiceSetServiceUserAgeArray() {
		Context context = new ContextImpl();

		context.put("names", new String[5]);
		context.put("ages", new int[5]);
		ServiceTestUtil.execute("serviceSetServiceUserAgeArray", context);
		assertEquals(5, context.get("length"));
	}

}
