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
 * 多层环境
 * 
 * @author luoguo
 * 
 */
public interface Context extends BaseContext {
	/**
	 * 创建子上下文
	 * 
	 * @param contextName
	 * @return
	 */
	Context createSubContext(String contextName);


	/**
	 * 从子环境删除
	 * @param contextName 子环境
	 * @param name 上下文变量名称
	 */
	<T> T remove(String contextName, String name);

	/**
	 * 从指定子环境获取变量
	 * 
	 * @param contextName 子环境
	 * @param name 上下文变量名称
	 * @return @
	 */
	<T> T get(String contextName, String name);

	/**
	 * 添加到子环境
	 * 
	 * @param contextName 子环境
	 * @param name 上下文变量名称
	 * @param object 要放入的数据
	 *            
	 */
	<T> T put(String contextName, String name, T object);

	/**
	 * 返回父亲
	 * 
	 * @return
	 */
	Context getParent();

	/**
	 * 设置父亲
	 * 
	 * @param parent
	 */
	void setParent(Context parent);

	/**
	 * 添加子环境
	 * 
	 * @param contextName 子环境
	 * @param context
	 * @return
	 */
	Context putSubContext(String contextName, Context context);

	/**
	 * 删除子上下文
	 * 
	 * @param contextName 子环境
	 */
	Context removeSubContext(String contextName);

	/**
	 * 返回子环境
	 * 
	 * @param contextName 子环境
	 * @return 子环境
	 */
	Context getSubContext(String contextName);

	/**
	 * 删除所有子上下文
	 */
	void clearSubContext();

	/**
	 * 返回子环境MAP
	 * 
	 * @return 子环境MAP
	 */
	Map<String, Context> getSubContextMap();

}