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
package org.tinygroup.flow;

import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.flow.component.AbstractFlowComponent;

public class FlowExceptionTestInPageFlow extends AbstractFlowComponent {
	
	protected void setUp() throws Exception {
		super.setUp();
		
	}

	public void testFlowNodeException(){
		Context context = new ContextImpl();
		pageFlowExecutor.execute("flowExceptionTest", "begin", context);
		Throwable throwable=context.get("flowExceptionTest-hello");
//		assertTrue(throwable.getCause() instanceof NoSuchMethodException);
	}
	
	public void testNodeException(){
		Context context = new ContextImpl();
		pageFlowExecutor.execute("exceptionWithNode", "begin", context);
		Throwable throwable=context.get("flowExceptionTest-hello");
//		assertTrue(throwable.getCause() instanceof NoSuchMethodException);
	}
	
	public void testFlowException(){
		Context context = new ContextImpl();
		pageFlowExecutor.execute("exceptionWithFlow", "begin", context);
		Throwable throwable=context.get("flowExceptionTest-hello");
//		assertTrue(throwable.getCause() instanceof NoSuchMethodException);
	}
	
}
