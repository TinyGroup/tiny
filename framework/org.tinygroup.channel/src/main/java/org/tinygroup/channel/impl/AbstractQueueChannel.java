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
package org.tinygroup.channel.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.tinygroup.event.Event;
import org.tinygroup.queue.PriorityQueue;
import org.tinygroup.queue.impl.PriorityQueueImpl;

/**
 * 抽象通道，实现了事件相关，日志 队列
 * 
 * @author luoguo
 * 
 */
public abstract class AbstractQueueChannel extends AbstractChannel {

	private PriorityQueue<Event> sendQueue = null;
	private PriorityQueue<Event> receiveQueue = null;
	ExecutorService executorService = Executors.newFixedThreadPool(Runtime
			.getRuntime().availableProcessors() * 2);
	private volatile Boolean stopSign = true;
	private EventProcessTask eventProcessTask;

	public void setQueueInfo(int sendQueueSize, int receiveQueueSize) {
		if (sendQueueSize > 0) {
			sendQueue = new PriorityQueueImpl<Event>(sendQueueSize);
		}
		if (receiveQueueSize > 0) {
			receiveQueue = new PriorityQueueImpl<Event>(receiveQueueSize);
		}
	}

	/**
	 * 真正的发送处理
	 * 
	 * @param event
	 */
	protected abstract void sendReal(Event event);

	/**
	 * 真正的接收处理
	 * 
	 * @param event
	 */
	protected abstract void receiveReal(Event event);

	protected void send(Event event) {
		sendQueue.offer(event);
		eventProcessTask.notify();
	}

	protected void receive(Event event) {
		receiveQueue.offer(event);
		eventProcessTask.notify();
	}

	public void start() {
		stopSign = false;
		eventProcessTask = new EventProcessTask();
		executorService.execute(eventProcessTask);
	}

	public void stop() {
		stopSign = true;
		executorService.shutdown();
	}

	class EventProcessTask extends Thread {
		public void run() {
			while (!stopSign) {
				processSend();
				processReceive();
				if (sendQueue.isEmpty() && receiveQueue.isEmpty()) {
					try {
						wait();
					} catch (InterruptedException e) {
					}
				}
			}
		}

		private void processReceive() {
			synchronized (sendQueue) {
				executorService.execute(new SendEventTask(sendQueue.peek()));
			}

		}

		private void processSend() {
			synchronized (receiveQueue) {
				executorService.execute(new ReceiveProcessTask(receiveQueue
						.peek()));
			}
		}
	}

	class ReceiveProcessTask extends Thread {
		private Event event;

		public ReceiveProcessTask(Event event) {
			this.event = event;
		}

		public void run() {
			receiveReal(event);
		}
	}

	class SendEventTask extends Thread {
		private Event event;

		public SendEventTask(Event event) {
			this.event = event;
		}

		public void run() {
			sendReal(event);
		}
	}
}
