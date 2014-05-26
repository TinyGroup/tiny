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
package org.tinygroup.commons.order;
public interface Ordered {

	/**
	 * Useful constant for the highest precedence value.
	 * @see java.lang.Integer#MIN_VALUE
	 */
	int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;

	/**
	 * Useful constant for the lowest precedence value.
	 * @see java.lang.Integer#MAX_VALUE
	 */
	int LOWEST_PRECEDENCE = Integer.MAX_VALUE;
	/**
	 * 默认值
	 */
	int DEFAULT_PRECEDENCE=0;


	/**
	 * Return the order value of this object, with a
	 * higher value meaning greater in terms of sorting.
	 * <p>Normally starting with 0 or 1, with {@link #LOWEST_PRECEDENCE}
	 * indicating greatest. Same order values will result in arbitrary
	 * positions for the affected objects.
	 * <p>Higher value can be interpreted as lower priority,
	 * consequently the first object has highest priority
	 * (somewhat analogous to Servlet "load-on-startup" values).
	 * <p>Note that order values below 0 are reserved for framework
	 * purposes. Application-specified values should always be 0 or
	 * greater, with only framework components (internal or third-party)
	 * supposed to use lower values.
	 * @return the order value
	 * @see #LOWEST_PRECEDENCE
	 */
	int getOrder();

}