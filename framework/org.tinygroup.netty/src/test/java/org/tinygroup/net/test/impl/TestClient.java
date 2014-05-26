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
package org.tinygroup.net.test.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.tinygroup.event.Event;
import org.tinygroup.net.Client;
import org.tinygroup.net.coder.hessian.HessianDecoder;
import org.tinygroup.net.coder.hessian.HessianEncoder;
import org.tinygroup.net.exception.InterruptedRuntimeException;

public class TestClient extends Client{
	Map<String, Thread> threadMap = new ConcurrentHashMap<String, Thread>();
	Map<String, Event> eventMap = new ConcurrentHashMap<String, Event>();
	Map<String, ChannelHandlerContext> channelHandlerContextMap = new ConcurrentHashMap<String, ChannelHandlerContext>();// 存放连接上下文

	public void updateEvent(String eventId, Event event) {
		eventMap.put(eventId, event);
	}

	public TestClient(String host, int port) {
		super(host, port);
	}

	public ChannelHandler getEncoder() {
		return new HessianEncoder();
	}

	public ChannelHandler getDecoder() {
		return new HessianDecoder();
	}

	public ChannelHandler getHandler() {
		return null;
	}

	@SuppressWarnings("unchecked")
	
	public <T> T sendObjectLocal(Object data) {
		Event event = (Event) data;
		String eventId = event.getEventId();
		// 存放数据
		try {
			newRequest(event, eventId);
			context.getChannel().write(data);// 写出数据
			synchronized (eventId) {
				int priority = event.getPriority();
				if (priority < 0) {
					priority = 0;
				}
				if (priority > 9) {
					priority = 9;
				}
				eventId.wait((priority + 1) * timeout);
			}
			// 已经醒来
			Event e = getEvent(eventId);
			if (e == null) {
				throw new RuntimeException("请求" + eventId + "因为网络中断而无法访问.");
			}
			if (e.getType() == Event.EVENT_TYPE_REQUEST) {// 如果超时，则返回请求超时异常
				throw new RuntimeException("请求" + eventId + "调用时，超时.");
			}
			Throwable throwable = e.getThrowable();
			if (throwable != null) {// 如果有异常发生，则抛出异常
				if (throwable instanceof RuntimeException) {
					throw (RuntimeException) throwable;
				} else if (throwable instanceof Error) {
					throw (Error) throwable;
				} else {
					throw new RuntimeException(throwable);
				}
			}
			return (T) e;
		} catch (InterruptedException e) {
			for (String key : channelHandlerContextMap.keySet()) {
				if (channelHandlerContextMap.get(key) == context) {
					clearRequest(key);
				}
			}
			throw new InterruptedRuntimeException(e);
		} finally {
			clearRequest(eventId);
		}
	}

	private void newRequest(Event event, String eventId) {
		threadMap.put(eventId, Thread.currentThread());
		eventMap.put(eventId, event);
		channelHandlerContextMap.put(eventId, context);
	}

	Event getEvent(String eventId) {
		return eventMap.get(eventId);
	}
	void clearRequest(String eventId) {
		threadMap.remove(eventId);
		eventMap.remove(eventId);
		channelHandlerContextMap.remove(eventId);
	}
}
