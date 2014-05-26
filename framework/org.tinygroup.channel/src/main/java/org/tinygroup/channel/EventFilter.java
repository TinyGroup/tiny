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

import org.tinygroup.event.Event;

/**
 * 事件过滤器
 * @author luoguo
 *
 */
public interface EventFilter {
	/**
	 * 事件过滤后，会返回事件，返回的事件可以与传入的事件不同
	 * @param event
	 * @return
	 */
	Event filter(Event event);
}
