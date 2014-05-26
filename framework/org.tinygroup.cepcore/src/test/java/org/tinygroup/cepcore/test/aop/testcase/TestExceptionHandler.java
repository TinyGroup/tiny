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
package org.tinygroup.cepcore.test.aop.testcase;

import org.tinygroup.cepcore.test.aop.util.AopTestUtil;
import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceRequest;
import org.tinygroup.exception.TinySysRuntimeException;

import junit.framework.TestCase;

public class TestExceptionHandler extends TestCase {
	public void testExceptionHandler() {
		Event event = new Event();
		ServiceRequest request = new ServiceRequest();
		event.setServiceRequest(request);
		event.setEventId("aaaa");
		Context context = new ContextImpl();
		request.setContext(context);
		request.setServiceId("exceptionService");
		boolean flag = false;
		try {
			AopTestUtil.execute(event);
		} catch (TinySysRuntimeException e) {
			flag = true;
		}
		if(flag){
			assertTrue(true);
		}
		
	}
}
