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

import org.tinygroup.queue.PriorityIncreaseStrategy;
import org.tinygroup.queue.PriorityQueue;

/**
 * 默认的优先级提升策略
 */
public class DefaultPriorityIncreaseStrategy<E> implements PriorityIncreaseStrategy<E> {
    public void increasePriority(PriorityQueue<E> queue) {
        PriorityQueueImpl<E> priorityQueue = (PriorityQueueImpl<E>) queue;
        // 默认设定调用次数是队列大小的时候触发优先提升
        if (priorityQueue.getCallTimes() == priorityQueue.getSize()) {
            synchronized (priorityQueue.dateQueueListArray) {
                for (int i = priorityQueue.getReverseLevel(); i < priorityQueue.dateQueueListArray.length - 1; i++) {
                    priorityQueue.dateQueueListArray[i].addAll(priorityQueue.dateQueueListArray[i + 1]);
                }
            }
        }
    }

}
