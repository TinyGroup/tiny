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
package org.tinygroup.queue;

import java.util.Random;

import org.tinygroup.commons.exceptions.NotExistException;
import org.tinygroup.queue.impl.PriorityQueueImpl;

import junit.framework.TestCase;

public class PriorityQueueTest extends TestCase {
	static volatile boolean stopIt = false;

	PriorityQueueImpl<Integer> queue = null;

	protected void setUp() throws Exception {
		super.setUp();
		queue = new PriorityQueueImpl<Integer>(PriorityQueue.DEFAULT_QUEUE_SIZE);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testOffer1() {
		queue.setTimeslice(500);
		queue.offer(1, 5);
		queue.offer(2, 5);
		try {
			Thread.sleep(1000);
		} catch (Exception e) {

		}
		queue.offer(3, 5);
		queue.offer(4, 5);
		queue.offer(5, 5);
		queue.offer(6, 5);
		queue.offer(7, 5);
		queue.offer(8, 5);
		assertEquals(8, queue.getUsingSize());
	}

	public void testPriorityStrategy() {
		PriorityQueue<String> strQ = new PriorityQueueImpl<String>(2);
		strQ.offer("aa");
		strQ.offer("bb");
		assertEquals("aa", strQ.peek());
		try {
			strQ.offer("cc");
			fail();
		} catch (Exception e) {

		}
	}

	public void testOfferWithPriority() {
		queue.offer(1);
		queue.offer(3);
		queue.offer(2, 2);
		assertEquals(2, queue.poll().intValue());
		assertEquals(1, queue.poll().intValue());
		assertEquals(3, queue.poll().intValue());
	}

	public void testOffer() {
		assertEquals(queue.getIdleSize(), queue.getSize());
		queue.offer(1);
		assertEquals(1, queue.peek().intValue());
		assertEquals(queue.getIdleSize() + 1, queue.getSize());
		assertEquals(1, queue.getUsingSize());
		assertEquals(1, queue.poll().intValue());
		assertEquals(0, queue.getUsingSize());
		assertEquals(queue.getIdleSize(), queue.getSize());
		Integer i2 = new Integer(2);
		queue.offer(i2);
		try {
			queue.remove();
			queue.remove();
			fail("Error!!");
		} catch (NotExistException e) {

		}
	}

	public void testElement() {
		queue.offer(1);
		try {
			assertEquals(1, queue.element().intValue());
		} catch (NotExistException e) {
			fail("Error!!");
		}
		queue.poll();
		try {
			assertNull(queue.element());
		} catch (NotExistException e) {
		}
	}

	public void testIsEmpty() {
		assertEquals(true, queue.isEmpty());
	}

	public void testIsFull() {
		for (int i = 0; i < queue.getSize(); i++) {
			queue.offer(i);
		}
		assertEquals(true, queue.isFull());
		try {
			queue.offer(3);
			fail();
		} catch (Exception e) {

		}
	}

	public void testGetName() {
		assertEquals("PriorityQueueImpl", queue.getName());
	}

	public void testGetSize() {
		assertEquals(Queue.DEFAULT_QUEUE_SIZE, queue.getSize());
	}

	public void testGetUsingLength() {
		for (int i = 1; i <= 10; i++) {
			queue.offer(i);
			assertEquals(i, queue.getUsingSize());
			assertEquals(Queue.DEFAULT_QUEUE_SIZE - i, queue.getIdleSize());
		}
		for (int i = 10; i >= 1; i--) {
			assertEquals(Queue.DEFAULT_QUEUE_SIZE - i, queue.getIdleSize());
			assertEquals(i, queue.getUsingSize());
			queue.poll();
		}
	}

//	public void testThreadVisit() {
//		for (int i = 0; i < 100; i++) {
//			ThreadTest threadTest = new ThreadTest();
//			threadTest.start();
//		}
//		try {
//			Thread.sleep(1000);
//			stopIt = true;
//			Thread.sleep(100);
//		} catch (InterruptedException e) {
//		}
//		assertEquals(queue.getSize(),
//				queue.getIdleSize() + queue.getUsingSize());
//	}

//	public void testThreadVisits() {
//		for (int i = 0; i < 10; i++) {
//			testThreadVisit();
//		}
//	}

	class ThreadTest extends Thread {
		Random r = new Random(System.currentTimeMillis());

		public void run() {
			while (!stopIt) {
				if (r.nextInt() % 10 <= 5) {
					if (!queue.isFull())
						queue.offer(1);
				} else {
					if (!queue.isEmpty())
						queue.poll();
				}
				if (queue.getUsingSize() == queue.getIdleSize()) {
					stopIt = true;
				}
				try {
					sleep(1);
				} catch (InterruptedException e) {
					break;
				}
			}
		}
	}
}
