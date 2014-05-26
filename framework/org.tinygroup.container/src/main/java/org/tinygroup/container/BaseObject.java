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
package org.tinygroup.container;

/**
 * 基本对象接口<br>
 * 
 * @author luoguo
 * 
 */
public interface BaseObject<T extends Comparable<T>> {
	/**
	 * 返回标识
	 * 
	 * @return
	 */
	T getId();

	/**
	 * 设置标识
	 * 
	 * @param id
	 */
	void setId(T id);

	/**
	 * 返回排序
	 * 
	 * @return
	 */
	int getOrder();// 返回排序情况

	/**
	 * 设置排序
	 * 
	 * @param order
	 */
	void setOrder(int order);

	/**
	 * 返回名字
	 * 
	 * @return
	 */
	String getName();// 返回名字

	/**
	 * 设置名字
	 * 
	 * @param name
	 */
	void setName(String name);

	/**
	 * 返回标题
	 * 
	 * @return
	 */
	String getTitle();// 返回中文名称

	/**
	 * 设置标题
	 * 
	 * @param title
	 */
	void setTitle(String title);

	/**
	 * 返回描述
	 * 
	 * @return
	 */
	String getDescription();// 返回描述

	/**
	 * 设置描述
	 * 
	 * @param description
	 */
	void setDescription(String description);
}
