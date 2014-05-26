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

import junit.framework.TestCase;

import org.tinygroup.channel.util.ChannelTestUtil;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceRequest;

public class ChannelSampleTest extends TestCase {
	ChannelSample channelSample;

	protected void setUp() throws Exception {
		super.setUp();
		channelSample = new ChannelSample();
		channelSample.setCepCore(ChannelTestUtil.getCep());
		EventFilter eventFilter = new EventFilter() {

			public Event filter(Event event) {
				Event e = event;
				event.getServiceRequest().setServiceId(
						event.getServiceRequest().getServiceId().toLowerCase());
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
		ChannelTestUtil.registerEventProcessor(new EventProcessor1());
		ChannelTestUtil.registerEventProcessor(new EventProcessor2());
	}

	public void testSendEvent() {
		Event event = getEvent();
		channelSample.sendEvent(event);
	}

	private Event getEvent() {
		Event event = new Event();
		event.setEventId("123");
		ServiceRequest serviceRequest = new ServiceRequest();
		event.setServiceRequest(serviceRequest);
		serviceRequest.setServiceId("aabbcc");
		serviceRequest.setContext(new ContextImpl());
		return event;
	}

	public void testReceiveEvent() {
		Event event = getEvent();
		channelSample.process(event);

	}
}
