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
package org.tinygroup.queue.impl;

import org.tinygroup.commons.exceptions.NotExistException;
import org.tinygroup.queue.Queue;

/**
 * Created by IntelliJ IDEA. User: luoguo Date: 11-3-30 Time: 下午1:37 To change
 * this template use File | Settings | File Templates.
 */
public class QueueImpl<E> implements Queue<E> {

	private int maxSize;
	private E[] queueArray;
	private int offerPosition = 0;
	private int size = 0;
	private int pollPosition = 0;
	private String name;

	/**
	 * 构造函数
	 * 
	 */
	public QueueImpl() {
		this(Queue.DEFAULT_QUEUE_SIZE);
	}

	/**
	 * 构造函数
	 * 
	 * @param name
	 */
	public QueueImpl(String name) {
		this(name, Queue.DEFAULT_QUEUE_SIZE);
	}

	/**
	 * 构造函数
	 * 
	 * @param name
	 * @param size
	 */
	@SuppressWarnings("unchecked")
	public QueueImpl(String name, int size) {
		this.name = name;
		this.maxSize = size;
		queueArray = (E[]) (new Object[size]);
	}

	/**
	 * 构造函数
	 * 
	 * @param size
	 */
	public QueueImpl(int size) {
		this(null, size);
	}

	/**
	 * 添加元素到队列中
	 * 
	 * @param o
	 * @return boolean
	 */
	public void offer(E o) {
		synchronized (queueArray) {
			if (size >= maxSize) {
				throw new RuntimeException("优先队列已满！");
			} else {
				queueArray[offerPosition++] = o;
				if (offerPosition == maxSize) {
					offerPosition = 0;
				}
				size++;
			}
		}
	}

	/**
	 * 元素出队，并删除 返回所删除的元素
	 * 
	 * @return E
	 */
	public E poll() {
		synchronized (queueArray) {
			if (size == 0) {
				throw new RuntimeException("队列为空！");
			} else {
				E e = queueArray[pollPosition];
				queueArray[pollPosition] = null;
				pollPosition++;
				if (pollPosition == maxSize) {
					pollPosition = 0;
				}
				size--;
				return e;
			}
		}
	}

	/**
	 * 元素出队，并删除 返回所删除的元素
	 * 
	 * @return E
	 */
	public E remove() {
		synchronized (queueArray) {
			if (size == 0) {
				throw new NotExistException();
			} else {
				E e = queueArray[pollPosition];
				queueArray[pollPosition] = null;
				pollPosition++;
				if (pollPosition == maxSize) {
					pollPosition = 0;
				}
				size--;
				return e;
			}
		}
	}

	/**
	 * 获取队列头元素
	 * 
	 * @return E
	 */
	public E peek() {
		synchronized (queueArray) {
			if (maxSize == 0) {
				throw new RuntimeException("队列为空！");
			} else {
				return queueArray[pollPosition];
			}
		}
	}

	/**
	 * 获取队列头元素
	 * 
	 * @return E
	 */
	public E element() {
		synchronized (queueArray) {
			if (isEmpty()) {
				throw new NotExistException();
			} else {
				return queueArray[pollPosition];
			}
		}
	}

	/**
	 * 检查队列是否为空
	 */
	public boolean isEmpty() {
		synchronized (queueArray) {
			return size == 0;
		}
	}

	/**
	 * 检查队列是否为满
	 */
	public boolean isFull() {
		synchronized (queueArray) {
			return size == maxSize;
		}
	}

	/**
	 * 设置队列名称
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取队列名称
	 * 
	 * @return String
	 */
	public String getName() {
		if (name != null) {
			return name;
		} else {
			return this.getClass().getSimpleName();
		}
	}

	/**
	 * 获取队列长度
	 * 
	 * @return int
	 */
	public int getSize() {
		return maxSize;
	}

	/**
	 * 获取已用队列长度
	 * 
	 * @return int
	 */
	public int getUsingSize() {
		synchronized (queueArray) {
			return size;
		}
	}

	/**
	 * 获取空闲队列长度
	 * 
	 * @return int
	 */
	public int getIdleSize() {
		synchronized (queueArray) {
			return maxSize - size;
		}
	}

	public int size() {
		return size;
	}
}
