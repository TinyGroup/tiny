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

/**
 * 队列接口
 * Created by IntelliJ IDEA. User: luoguo
 */
public interface Queue<E> extends QueueMonitor {
    /**
     * 默认优先队列大小
     */
    int DEFAULT_QUEUE_SIZE = 500;

    /**
     * 添加元素到队列中.
     *
     * @param e 要增加的元素
     */
    void offer(E e);

    /**
     * 从队列头部取一个数据并且把它删除掉，如果队列为空，则返回NULL
     *
     * @return 元素
     */
    E poll();

    /**
     * 从队列头部取一个元素并且把它删除掉，如果队列为空，则抛出异常
     *
     * @return
     */
    E remove();

    /**
     * 从队列头部取一个元素，但是不删除。如果队列为空，则返回NULL
     *
     * @return
     */
    E peek();

    int size();

    /**
     * 从队列头部取一个元素，但是不删除。如果队列为空，则抛出异常
     *
     * @return
     */
    E element();

    /**
     * 返回队列是否为空
     *
     * @return
     */
    boolean isEmpty();

    /**
     * 返回是否满
     *
     * @return
     */
    boolean isFull();
}