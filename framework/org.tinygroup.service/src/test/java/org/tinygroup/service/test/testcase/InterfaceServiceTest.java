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

import junit.framework.TestCase;

import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.service.util.ServiceTestUtil;

public class InterfaceServiceTest extends TestCase {
	/**
	 * InterfaceDef由于有两个实现类bean
	 * 所以需要在xml上配置bean指定bean id
	 * 在此处指定的bean是实现InterfaceDefImpl
	 * 所以返回是112
	 */
	public void testInterface() {
		Context context = new ContextImpl();
		ServiceTestUtil.execute("serviceTestInterface", context);
		assertEquals("112", (String)context.get("result"));
	}
	/**
	 * InterfaceDef2只有一个实现类bean
	 * 所以不需要指定bean id
	 */
	public void testInterface2() {
		Context context = new ContextImpl();
		ServiceTestUtil.execute("serviceTestInterface2", context);
		assertEquals("12", (String)context.get("result"));
	}
}
