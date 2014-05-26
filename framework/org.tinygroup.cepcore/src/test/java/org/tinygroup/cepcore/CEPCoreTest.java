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
package org.tinygroup.cepcore;


import junit.framework.TestCase;

import org.tinygroup.cepcore.test.aop.util.AopTestUtil;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceRequest;

public class CEPCoreTest extends TestCase {
	
	private EventProcessor channel1 = new Channel1();
	
	protected void setUp() throws Exception {
		super.setUp();
		//cepCore.setManager(new CEPCoreNodeImpl());
		AopTestUtil.registerEventProcessor(new EventProcessor1());
		AopTestUtil.registerEventProcessor(new EventProcessor2());
		AopTestUtil.registerEventProcessor(channel1);
	}

	public void testProcess() {
		Event event = new Event();
		event.setEventId("1");
		ServiceRequest serviceRequest = new ServiceRequest();
		serviceRequest.setServiceId("1111");
		event.setServiceRequest(serviceRequest);
		AopTestUtil.getCep().setNodeName("aaa");
		AopTestUtil.execute(event);
		
	}

	public void testProcess1() {
		Event event = new Event();
		event.setEventId("2");
		ServiceRequest serviceRequest = new ServiceRequest();
		serviceRequest.setServiceId("2222");
		event.setServiceRequest(serviceRequest);
		AopTestUtil.getCep().setNodeName("aaa");
		AopTestUtil.execute(event);
		
	}

	public void testProcess3() {
		Event event = new Event();
		event.setEventId("3");
		ServiceRequest serviceRequest = new ServiceRequest();
		serviceRequest.setServiceId("3333");
		event.setServiceRequest(serviceRequest);
		AopTestUtil.getCep().setNodeName("aaa");
		AopTestUtil.execute(event);
	}

	public void testProcess4() {
		Event event = new Event();
		event.setEventId("4");
		ServiceRequest serviceRequest = new ServiceRequest();
		serviceRequest.setServiceId("1111");
		event.setServiceRequest(serviceRequest);
		AopTestUtil.getCep().setNodeName("aaa");
		channel1.process(event);
	}

	
}
