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
import org.tinygroup.queue.PriorityIncreaseStrategy;
import org.tinygroup.queue.PriorityQueue;
import org.tinygroup.queue.Queue;
import org.tinygroup.queue.QueueMonitor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 优先队列 支持优先级提升 <br>
 * User: luoguo <br>
 * Date: 11-3-30 <br>
 * Time: 下午9:24 <br>
 */
public class PriorityQueueImpl<T> implements PriorityQueue<T>, QueueMonitor {
    private static final int ZERO = 0;
    private static final int DEFAULT_REVERSE_LEVEL = 3;
    private static final int DEFAULT_PRIORITY_LEVEL = 10;
    private static final int DEFAULT_TIMESLICE = 1000;
    private int timeslice = DEFAULT_TIMESLICE;// 1秒
    private int maxSize = DEFAULT_QUEUE_SIZE;
    /**
     * 优先级别的范围，默认最低级别是10
     */
    private int priorityLevel = DEFAULT_PRIORITY_LEVEL;// 级别从1到10
    /**
     * 队列存放元素个数
     */
    private int size = ZERO;
    /**
     * 保留级别，不允许把普通优先级的数据提到保留级别中，拥有绝对的优先性
     */
    private int reverseLevel = DEFAULT_REVERSE_LEVEL;
    /**
     * 存放不同级别的时间片段队列
     */
    List<DateQueue<T>>[] dateQueueListArray;
    /**
     * 统计队列调次数
     */
    private int callTimes = ZERO;
    /**
     * 优先队列提升级别，如果设置为空，将不进行级别提升
     */
    private PriorityIncreaseStrategy<T> strategy = new DefaultPriorityIncreaseStrategy<T>();
    /**
     * 优先队列的名称
     */
    private String name;

    public int getPriorityLevel() {
        return priorityLevel;
    }

    public int getCount() {
        return size;
    }

    public List<DateQueue<T>>[] getDateQueueListArray() {
        return dateQueueListArray;
    }

    public int getCallTimes() {
        return callTimes;
    }

    public PriorityIncreaseStrategy<T> getStrategy() {
        return strategy;
    }

    public PriorityIncreaseStrategy<T> getPriorityIncreaseStrategy() {
        return strategy;
    }

    public void setPriorityIncreaseStrategy(PriorityIncreaseStrategy<T> strategy) {
        this.strategy = strategy;
    }

    public int getReverseLevel() {
        return reverseLevel;
    }

    public void setReverseLevel(int reverseLevel) {
        this.reverseLevel = reverseLevel;
    }

    public int getTimeslice() {
        return timeslice;
    }

    public void setTimeslice(int timeslice) {
        this.timeslice = timeslice;
    }

    public PriorityQueueImpl() {
        this(DEFAULT_QUEUE_SIZE, DEFAULT_PRIORITY_LEVEL);
    }

    /**
     * @param size
     */
    public PriorityQueueImpl(int size) {
        this(size, DEFAULT_PRIORITY_LEVEL);

    }

    /**
     * 构造函数
     *
     * @param size          如果是0，表示不限制大小
     * @param priorityLevel
     */
    @SuppressWarnings("unchecked")
    public PriorityQueueImpl(int size, int priorityLevel) {
        this.maxSize = size;
        this.priorityLevel = priorityLevel;
        dateQueueListArray = new List[priorityLevel];
        for (int i = 0; i < priorityLevel; i++) {
            dateQueueListArray[i] = new ArrayList<DateQueue<T>>();
        }
    }

    /**
     * @param o
     * @param pPriority 从1开始
     */
    public void offer(T o, int pPriority) {
        int priority = pPriority;
        // 如果level不对，则放在最低优先级
        synchronized (dateQueueListArray) {
            if (priority > this.priorityLevel || priority < 0) {
                priority = priorityLevel;
            }
            if (strategy != null && this.maxSize > 0) {
                strategy.increasePriority(this);
            }
            if (size != maxSize || maxSize == 0) {
                List<DateQueue<T>> dateQueueList = dateQueueListArray[priority - 1];
                if (dateQueueList.size() > 0) {
                    DateQueue<T> dateQueue = dateQueueList.get(dateQueueList.size() - 1);
                    if (!dateQueue.queue.isFull() && new Date().getTime() - dateQueue.date.getTime() < timeslice) {
                        dateQueue.queue.offer(o);
                        size++;
                        return;
                    }

                }
                // 一个分片最多只能放size / priorityLevel个任务
                int s = maxSize;
                if (maxSize > priorityLevel) {
                    s = maxSize / priorityLevel;
                }
                DateQueue<T> dateQueue = new DateQueue<T>(new QueueImpl<T>(s));
                dateQueue.queue.offer(o);
                dateQueueList.add(dateQueue);
                size++;
            } else {
                throw new RuntimeException("优先队列已满！");
            }
        }
    }

    public void offer(T o) {// 没有指定level，则放在最低优先级
        offer(o, this.priorityLevel);
    }

    public T poll() {
        synchronized (dateQueueListArray) {
            if (size > 0) {
                // 优先级从高到低进行检查
                for (List<DateQueue<T>> dateQueueList : dateQueueListArray) {
                    // 如果有时间片队列
                    if (dateQueueList.size() > 0) {
                        // 取出第一个时间片队列
                        DateQueue<T> dateQueue = dateQueueList.get(0);
                        T t = dateQueue.queue.poll();
                        // 如果已经为空，则删除之
                        if (dateQueue.queue.isEmpty()) {
                            dateQueueList.remove(0);
                        }
                        size--;
                        callTimes++;
                        return t;
                    }
                }
            }
        }
        throw new RuntimeException("优先队列为空！");
    }

    public T remove() {
        synchronized (dateQueueListArray) {
            if (size > 0) {
                // 优先级从高到低进行检查
                for (List<DateQueue<T>> dateQueueList : dateQueueListArray) {
                    // 如果有时间片队列
                    if (dateQueueList.size() > 0) {
                        // 取出第一个时间片队列
                        DateQueue<T> dateQueue = dateQueueList.get(0);
                        T t = dateQueue.queue.poll();
                        // 如果已经为空，则删除之
                        if (dateQueue.queue.isEmpty()) {
                            dateQueueList.remove(0);
                        }
                        size--;
                        callTimes++;
                        return t;
                    }
                }
            }
            throw new NotExistException();
        }
    }

    public T peek() {
        synchronized (dateQueueListArray) {
            if (size > 0) {
                // 优先级从高到低进行检查
                for (List<DateQueue<T>> dateQueueList : dateQueueListArray) {
                    // 如果有时间片队列
                    if (dateQueueList.size() > 0) {
                        // 取出第一个时间片队列
                        DateQueue<T> dateQueue = dateQueueList.get(0);
                        return dateQueue.queue.peek();
                    }
                }
            }
        }
        throw new RuntimeException("优先队列为空！");
    }

    public T element() {
        synchronized (dateQueueListArray) {
            if (size > 0) {
                // 优先级从高到低进行检查
                for (List<DateQueue<T>> dateQueueList : dateQueueListArray) {
                    // 如果有时间片队列
                    if (dateQueueList.size() > 0) {
                        // 取出第一个时间片队列
                        DateQueue<T> dateQueue = dateQueueList.get(0);
                        return dateQueue.queue.peek();
                    }
                }
            }
            throw new NotExistException();
        }
    }

    public boolean isEmpty() {
        synchronized (dateQueueListArray) {
            return size == 0;
        }
    }

    public boolean isFull() {
        synchronized (dateQueueListArray) {
            if (maxSize == 0) {
                return false;
            } else {
                return size == maxSize;
            }
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        if (name != null) {
            return name;
        } else {
            return this.getClass().getSimpleName();
        }
    }

    public int getSize() {
        return maxSize;
    }

    public int getUsingSize() {
        synchronized (dateQueueListArray) {
            return size;
        }
    }

    public int getIdleSize() {
        synchronized (dateQueueListArray) {
            if (maxSize == 0) {
                return Short.MAX_VALUE;
            } else {
                return maxSize - size;
            }
        }
    }

    private class DateQueue<DT> {
        private Date date;
        private Queue<DT> queue;

        DateQueue(Queue<DT> queue) {
            this(new Date(), queue);
        }

        DateQueue(Date date, Queue<DT> queue) {
            this.date = date;
            this.queue = queue;
        }
    }

    public int size() {
        return size;
    }

}
