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
 * 优先队列. 
 * User: luoguo 
 * Date: 11-3-30 
 * Time: 下午9:22 
 * To change this template use
 * File | Settings | File Templates.
 */
public interface PriorityQueue<E> extends Queue<E> {
	/**
	 * 添加元素到队列中.
	 * 
	 * @param e
	 *            要增加的元素
	 * @param priority
	 *            要增加的元素的优先级
	 * @return 是否添加成功
	 */
	void offer(E e, int priority);

	void setPriorityIncreaseStrategy(PriorityIncreaseStrategy<E> strategy);
}
