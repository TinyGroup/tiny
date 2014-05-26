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
package org.tinygroup.flowbasiccomponent.test.testcase;

import junit.framework.TestCase;

import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.flowbasiccomponent.test.BaseBean;
import org.tinygroup.flowbasiccomponent.test.util.FlowTestUtil;

public class PageFlowTest extends TestCase{
	public void testCallMethodPlus(){
		Context c = new ContextImpl();
		c.put("i", 1);
		c.put("j", 2);
		FlowTestUtil.execute("callComputePlus", c);
		assertEquals( 3,c.get("plusResult"));
	}
	public void testCallBeanMethod(){
		Context a = new ContextImpl();
		a.put("name", "test");
		FlowTestUtil.execute("callBeanMethod", a);
		assertEquals( "hello:test",a.get("helloResult"));
	}
	public void testCallEl(){
		Context c = new ContextImpl();
		c.put("age", 12);
		FlowTestUtil.execute("callEl", c);
		assertEquals( 13,c.get("elResult"));
	}
	public void testContextToObject(){
		Context c = new ContextImpl();
		c.put("className", "org.tinygroup.flowbasiccomponent.test.BaseBean");
		c.put("resultKey", "objResult");
		c.put("name", "name");
		c.put("age", 12);
		c.put("flag", true);
		FlowTestUtil.execute("flowContext2Object", c);
		BaseBean b = c.get("objResult");
		assertEquals( b.getAge(),12);
		assertEquals( b.getName(),"name");
		assertEquals( b.isFlag(),true);
	}
}
