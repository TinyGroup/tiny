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
 * 优先队列优先级提升策略
 * Created by IntelliJ IDEA. User: luoguo
 * Date: 11-3-31 Time: 上午8:53 To change
 * this template use File | Settings | File Templates.
 */

public interface PriorityIncreaseStrategy<E> {
    /**
     * 对优级队列中的优先级别进行提升
     *
     * @param queue
     */
    void increasePriority(PriorityQueue<E> queue);
}
