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
package org.tinygroup.flow.el;

import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.flow.component.AbstractFlowComponent;

public class TestNextNodeByElInPageFlow extends AbstractFlowComponent{
	protected void setUp() throws Exception {
		super.setUp();
		
	}
	
	//a+b = c 如果 c < 30
	//就执行 c = a + c 直到 c > 30
	//然后再执行 c = c + b
	public void testNextNodeByE1(){
		Context c = new ContextImpl();
		c.put("a", 5);
		c.put("b", 15);
		pageFlowExecutor.execute("NextNodeByEl", c);
		assertEquals(45, c.get("c"));
	}
	
	public void testNextNodeByE12(){
		Context c = new ContextImpl();
		c.put("a", 6);
		c.put("b", 15);
		pageFlowExecutor.execute("NextNodeByEl", c);
		assertEquals(48, c.get("c"));
	}
	
	public void testFlowPropertyWithEl(){
		Context c = new ContextImpl();
		c.put("parama", "a");
		c.put("paramb", "b");
		c.put("paramc", "c");
		c.put("a", 5);
		c.put("b", 15);
		pageFlowExecutor.execute("NextNodeByEl", c);
		assertEquals(45, c.get("c"));
	}
	
	public void testFlowPropertyWithEl2(){
		Context c = new ContextImpl();
		c.put("parama", "a");
		c.put("paramb", "b");
		c.put("paramc", "c");
		c.put("a", 6);
		c.put("b", 15);
		pageFlowExecutor.execute("NextNodeByEl", c);
		assertEquals(48, c.get("c"));
	}
	
	//a+b = c 
	//然后再执行 c = c + b
	public void testNextNodeByNullE1(){
		Context c = new ContextImpl();
		c.put("a", 5);
		c.put("b", 15);
		pageFlowExecutor.execute("NextNodeByNullEl", c);
		assertEquals(35, c.get("c"));
	}
	
	public void testNextNodeByNullE12(){
		Context c = new ContextImpl();
		c.put("a", 6);
		c.put("b", 15);
		pageFlowExecutor.execute("NextNodeByNullEl", c);
		assertEquals(36, c.get("c"));
	}
}
