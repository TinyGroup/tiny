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
package org.tinygroup.cache;

import java.util.Set;

/**
 * 
 * 功能说明: 缓存通用接口
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-5-22 <br>
 * <br>
 */
public interface Cache {
	/**
	 * 
	 * 缓存区域初始化
	 * 
	 * @param region
	 */
	void init(String region);

	/**
	 * @
	 * 
	 */
	Object get(String key);

	/**
	 * @param key
	 * @param Object
	 *            @
	 */
	void put(String key, Object object);

	/**
	 * 
	 * @param key
	 * @param Object
	 */
	void putSafe(String key, Object object);

	/**
	 * @param groupName
	 * @param key
	 * @param Object
	 *            @
	 */
	void put(String groupName, String key, Object object);

	/*
	 */
	Object get(String groupName, String key);

	/**
	 * @param group
	 * @return
	 */
	Set<String> getGroupKeys(String group);

	/**
	 * @param group
	 *            @
	 */
	void cleanGroup(String group);

	/**
	 * @
	 */
	void clear();

	/**
	 * @param key
	 *            @
	 */
	void remove(String key);

	/**
	 * @param key
	 *            @
	 */
	void remove(String group, String key);

	/**
	 * @return
	 */
	String getStats();

	/**
	 * @param numberToFree
	 * @return @
	 */
	int freeMemoryElements(int numberToFree);
	
	void destory();
}
