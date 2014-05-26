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
package org.tinygroup.channel;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.impl.CEPcoreImpl;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceRequest;

import junit.framework.TestCase;

public class ChannelWithCEPCoreTest extends TestCase {
	ChannelSample channelSample;
	CEPCore cepCore = new CEPcoreImpl();

	protected void setUp() throws Exception {
		super.setUp();
		channelSample = new ChannelSample();
		EventFilter eventFilter = new EventFilter() {

			public Event filter(Event event) {
				Event e = event;
				return e;
			}
		};
		channelSample.addSendEventFilter(eventFilter);
		channelSample.addReceiveEventFilter(eventFilter);
		EventListener eventListener = new EventListener() {

			public void process(Event event) {
				System.out.println(String.format("Log:%s", event
						.getServiceRequest().getServiceId()));
			}
		};
		channelSample.addSendEventListener(eventListener);
		channelSample.addReceiveEventListener(eventListener);
		cepCore.registerEventProcessor(channelSample);
		cepCore.registerEventProcessor(new EventProcessor1());
		cepCore.registerEventProcessor(new EventProcessor2());
	}

	public void testSendEvent() {
		Event event = getEvent("aabbcc","","","");
		channelSample.sendEvent(event);
		assertEquals("aa", event.getServiceRequest().getContext().get("result"));
		event = getEvent("111111","aabbcc1","a","a");
		channelSample.sendEvent(event);
		assertEquals("bb", event.getServiceRequest().getContext().get("result"));
	}
	
	public void testSendEvent1() {
		Event event = getEvent("111111","111111","a","a");
		channelSample.sendEvent(event);
		assertEquals("bb", event.getServiceRequest().getContext().get("result"));
	}

	private Event getEvent(String id,String name,String artifactId,String groupId) {
		Event event = new Event();
		event.setEventId("123");
		ServiceRequest serviceRequest = new ServiceRequest();
		event.setServiceRequest(serviceRequest);
		serviceRequest.setServiceId(id);
		serviceRequest.setContext(new ContextImpl());
		return event;
	}
	

}
