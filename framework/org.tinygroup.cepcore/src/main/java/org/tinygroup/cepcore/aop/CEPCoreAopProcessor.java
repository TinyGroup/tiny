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
package org.tinygroup.cepcore.aop;

import org.tinygroup.event.Event;

/**
 * CEP中的AOP处理器
 * 
 * @author luog
 * 
 */
public interface CEPCoreAopProcessor {
	int BEFORE = 1;
	int AFTER = 2;
	int BEFORE_LOCAL = 4;
	int AFTER_LOCAL = 8;
	int BEFORE_REMOTE = 16;
	int AFTER_REMOTE = 32;

	/**
	 * 返回拦截位置，拦截位置可以上上面定义的常量的相加的组合
	 * 
	 * @return
	 */
	int getPosition();

	/**
	 * 是否匹配
	 * 
	 * @param event
	 * @return
	 */
	boolean isMatch(Event event);

	/**
	 * 匹配时处理事件
	 * 
	 * @param event
	 */
	void process(Event event);
}
