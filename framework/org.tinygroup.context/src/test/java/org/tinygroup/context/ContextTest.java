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
package org.tinygroup.context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import junit.framework.TestCase;

import org.tinygroup.context.impl.ContextImpl;

public class ContextTest extends TestCase {
	Context Context = new ContextImpl();

	protected void setUp() throws Exception {
		super.setUp();
		Context.clear();
	}

	public void testPut() {
		Context.put("aa", 3);
		assertEquals(3, Context.get("aa"));
	}

	public void testRemove() {
		Context.put("aa", 3);
		assertEquals(3, Context.remove("aa"));
		if (Context.exist("aa")) {
			fail("应该不存在");
		}
	}

	public void testGetString() {
		Context.put("aa", 3);
		assertEquals(3, Context.get("aa"));
		Context testContext = new ContextImpl();
		Context.putSubContext("test", testContext);
		assertEquals(3, testContext.get("aa"));
	}

	public void testGetStringT() {
		Context.put("a1a", 3);
		int result = Context.get("aa", 4);
		assertEquals(4, result);
	}

	public void testExist() {
		Context.put("aa", 3);
		assertEquals(true, Context.exist("aa"));
		assertEquals(false, Context.exist("a1a"));
		Context context=new ContextImpl();
		context.put("abc", null);
		assertEquals(true, context.exist("abc"));
		Context subContext=new ContextImpl();
		subContext.put("name1", null);
		context.putSubContext("sub", subContext);
		assertEquals(true, subContext.exist("abc"));
        Context parentContext=new ContextImpl();
        parentContext.putSubContext("aaa3",context);
        assertEquals(true, parentContext.exist("name1"));
        assertEquals(true, parentContext.exist("abc"));

    }

	public void testClear() {
		Context.put("aaa", 3);
		Context.clear();
		assertEquals(false, Context.exist("aaa"));
	}

	public void testCreateSubContext() {
		assertNotNull(Context.createSubContext("aa"));
		assertNotNull(Context.getSubContext("aa"));

	}

	public void testRemoveStringString() {
		Context.createSubContext("aa");
		Context.put("aa", "a", 1);
		assertEquals(1, Context.get("aa", "a"));
	}

	public void testGetStringString() {
		Context.createSubContext("aa");
		Context.put("aa", "a", 1);
		assertEquals(1, Context.get("aa", "a"));
	}

	public void testPutStringStringT() {
		Context.createSubContext("aa");
		Context.put("aa", "a", 1);
		assertEquals(1, Context.get("aa", "a"));
	}

	public void testAddSubContext() {
		Context testContext = new ContextImpl();
		Context.putSubContext("test", testContext);
	}

	public void testRemoveSubContext() {
		Context testContext = new ContextImpl();
		Context.putSubContext("test", testContext);
		Context.removeSubContext("test");
		assertEquals(0, Context.getSubContextMap().size());
	}

	public void testGetSubContext() {
		Context testContext = new ContextImpl();
		Context.putSubContext("test", testContext);
		Context context = Context.getSubContext("test");
		assertNotNull(context);
	}

	public void testClearSubContext() {
		Context testContext = new ContextImpl();
		Context.putSubContext("test", testContext);
		Context.clearSubContext();
		assertEquals(0, Context.getSubContextMap().size());
	}

	public void testGetSubContextMap() {
		Context testContext = new ContextImpl();
		Context.putSubContext("test", testContext);
		Context.clearSubContext();
		assertEquals(0, Context.getSubContextMap().size());
	}

	public void testNotInContext(){
		Context context=new ContextImpl();
		context.put("name1", "value1");
		context.put("name3", "value3");
		Context subContext=new ContextImpl();
		subContext.put("name1", "subValue1");
		subContext.put("name2", "subValue2");
		context.putSubContext("sub", subContext);
		context.put("sub","name1","subValue");
		context.get("name1");
		
		String value=context.get("sub", "name1");
		assertEquals("subValue", value);
		String value1=context.get("sub", "name2");
		assertEquals("subValue2", value1);
		String value2=context.get("sub", "name4");
		assertEquals(null, value2);
		String value3=context.get("name");
		assertEquals(null, value3);
		String value4=subContext.get("name3");
		assertEquals("value3", value4);		
	}
	public void testMutiThreadContext(){
		
		ExecutorService pool= Executors.newFixedThreadPool(10);
		final Context context=new ContextImpl();
		context.put("name1", "value1");
		context.put("name3", "value3");
		Context subContext=new ContextImpl();
		subContext.put("name1", "subValue1");
		subContext.put("name2", "subValue2");
		context.putSubContext("sub", subContext);
		for (int i = 0; i < 10; i++) {
			pool.execute(new Runnable() {
				public void run() {
					myTask(context);
				}
			});
		}	
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		    fail(e.getMessage());
		}
		
	}
	
	private void myTask(Context context){
		String value2=context.get("name2");
		assertEquals("subValue2", value2);
		//System.out.println("线程名---"+Thread.currentThread().getName()+"--值："+value2);
		
	}
	/**
	 * 
	 */
	public void testMutiThreadContext2(){
		
		final Context context = createContext();
		for (int i = 0; i < 1; i++) {
			Thread thread=new Thread(new Task(context));
			thread.start();
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		    fail(e.getMessage());
		}
		
		
	}

	
	private Context createContext() {
		final Context context=new ContextImpl();
		context.put("name1", "value1");
		context.put("name3", "value3");
		Context subContext=new ContextImpl();
		subContext.put("name1", "subValue1");
		subContext.put("name2", "subValue2");
		context.putSubContext("sub", subContext);
		Context parentContext=new ContextImpl();
		parentContext.put("name2", "parentValue");
		parentContext.putSubContext("context", context);
		subContext.putSubContext("parent", parentContext);
		return context;
	}
	
	class Task implements Runnable{
		
		private Context context;
		
		Task(Context context){
			this.context=context;
		}

		public void run() {
			myTask(context);
		}
		
	}
	
	public void testRenameKey(){
		final Context context=createContext();
		boolean rename= context.renameKey("name2", "name5");
		assertEquals(true, rename);
		assertEquals("subValue2", context.get("name5"));
		
	}
	
}
