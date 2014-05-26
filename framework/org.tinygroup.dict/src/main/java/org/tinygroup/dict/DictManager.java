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
package org.tinygroup.dict;

import org.tinygroup.cache.Cache;
import org.tinygroup.context.Context;

/**
 * 字符管理器
 * 
 * @author luoguo
 * 
 */
public interface DictManager {
	String DICT_MANAGER_BEAN_NAME = "dictManager";
	String XSTEAM_PACKAGE_NAME = "dict";


	/**
	 * 设置缓冲
	 * 
	 * @param cache
	 */
	void setCache(Cache cache);

	/**
	 * 获得缓冲对象
	 * 
	 * @return
	 */
	Cache getCache();

	/**
	 * 添加字典加载器
	 * 
	 * @param dictLoader
	 */
	void addDictLoader(DictLoader dictLoader);

	/**
	 * 利用指定加载器载入字典
	 * 
	 */
	void load(DictLoader dictLoader);

	/**
	 * 清空所有字典项
	 */
	void clear();

	/**
	 * 清空某一加载器的字符项
	 * 
	 * @param dictLoader
	 */
	void clear(DictLoader dictLoader);

	/**
	 * 载入字典项
	 * 
	 */
	void load();

	/**
	 * 获取字典
	 * 
	 * @param context
	 * @return
	 */
	Dict getDict(String dictTypeName, Context context);

}
