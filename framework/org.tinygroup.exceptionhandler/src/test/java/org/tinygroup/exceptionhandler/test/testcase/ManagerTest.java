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
package org.tinygroup.exceptionhandler.test.testcase;

import junit.framework.TestCase;

import org.tinygroup.exceptionhandler.ExceptionHandler;
import org.tinygroup.exceptionhandler.ExceptionHandlerManager;
import org.tinygroup.exceptionhandler.impl.ExceptionHandlerManagerImpl;
import org.tinygroup.exceptionhandler.test.exception.Exception1;
import org.tinygroup.exceptionhandler.test.exception.Exception2;
import org.tinygroup.exceptionhandler.test.exception.Exception3;
import org.tinygroup.exceptionhandler.test.handler.Handler1;
import org.tinygroup.exceptionhandler.test.handler.Handler2;
import org.tinygroup.exceptionhandler.test.handler.Handler3;
import org.tinygroup.exceptionhandler.test.util.ResultUtil;

public class ManagerTest extends TestCase {
	ExceptionHandlerManager manager;

	public void setUp() {
		manager = new ExceptionHandlerManagerImpl();
		addHandler(
				"org.tinygroup.exceptionhandler.test.exception.Exception1",
				new Handler1());
		addHandler(
				"org.tinygroup.exceptionhandler.test.exception.Exception2",
				new Handler2());
		addHandler(
				"org.tinygroup.exceptionhandler.test.exception.Exception3",
				new Handler3());
	}
	
	private void addHandler(String excption,ExceptionHandler<?> handler){
		try {
			manager.addHandler(
					excption,
					handler);
		} catch (ClassNotFoundException e) {
			System.out.println(String.format("添加Handler时出错,Exception:%s未找到",excption));
		}
	}
	
	public void testException1(){
		ResultUtil.clear();
		manager.handle(new Exception1("e1"),null);
		assertEquals(1, ResultUtil.getResult());
	}
	public void testException2(){
		ResultUtil.clear();
		manager.handle(new Exception2("e2"),null);
		assertEquals(2, ResultUtil.getResult());
	}
	public void testException3(){
		ResultUtil.clear();
		manager.handle(new Exception3("e3"),null);
		assertEquals(3, ResultUtil.getResult());
	}
	public void testExceptionAll1(){
		ResultUtil.clear();
		manager.handleWithAllHandler(new Exception1("e1"),null);
		assertEquals(1, ResultUtil.getResult());
	}
	public void testExceptionAll2(){
		ResultUtil.clear();
		manager.handleWithAllHandler(new Exception2("e2"),null);
		assertEquals(3, ResultUtil.getResult());
	}
	public void testExceptionAll3(){
		ResultUtil.clear();
		manager.handleWithAllHandler(new Exception3("e3"),null);
		assertEquals(6, ResultUtil.getResult());
	}

}
