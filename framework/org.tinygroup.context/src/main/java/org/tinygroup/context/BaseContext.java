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
package org.tinygroup.context;

import java.util.Map;

/**
 * 普通的环境，不支持多层嵌套
 * 
 * @author luoguo
 * 
 */
public interface BaseContext {
	/**
	 * 添加到环境
	 * 
	 * @param name
	 * @param object
	 */
	<T> T put(String name, T object);

	/**
	 * 把环境中某键值的名字换成另外的名字
	 * @param key
	 * @param newKey
	 * @return
	 */
	boolean renameKey(String key, String newKey);

	/**
	 * 遍历所有删除及子环境并删除找到的第一个
	 * 
	 * @param name
	 * @return
	 */
	<T> T remove(String name);

	/**
	 * 获取对象，如果当前环境中没有，则到子环境当中寻找
	 * 
	 * @param name
	 * @return
	 */
	<T> T get(String name);

	/**
	 * 
	 * @param name
	 * @param defaultValue
	 *            默认值
	 * @return
	 * @throws NotExistException
	 */
	<T> T get(String name, T defaultValue);

	int size();

	/**
	 * 检测变量是否存在
	 * 
	 * @param name
	 * @return 返回是否存在
	 */
	boolean exist(String name);

	/**
	 * 删除环境中的所有内容
	 */
	void clear();

	/**
	 * 返回环境中所有内容
	 * 
	 * @return
	 */
	Map<String, Object> getItemMap();

}