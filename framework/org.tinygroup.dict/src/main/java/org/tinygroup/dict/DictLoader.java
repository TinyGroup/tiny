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

import org.tinygroup.context.Context;

/**
 * 字典加载器
 * 
 * @author luoguo
 * 
 */
public interface DictLoader {
	/**
	 * 返回属性的语种
	 * 
	 * @return
	 */
	String getLanguage();

	/**
	 * 载入字典
	 * 
	 * @param dictManager
	 */
	void load(DictManager dictManager);

	/**
	 * 清除的数据
	 * 
	 * @param dictManager
	 */
	void clear(DictManager dictManager);

	/**
	 * 返回字典
	 * 
	 * @param dictTypeName
	 * @param dictManager
	 * @param context
	 * @return
	 */
	Dict getDict(String dictTypeName, DictManager dictManager, Context context);

	/**
	 * 返回分组名称
	 * 
	 * @return
	 */
	String getGroupName();

	/**
	 * 设置分组名称
	 * 
	 * @param groupName
	 */
	void setGroupName(String groupName);
	
	/**
	 * 设置语言
	 * 
	 * @param language
	 */
	 void setLanguage(String language);

	/**
	 * 设置字典过滤器
	 * 
	 * @param dictFilter
	 */
	void setDictFilter(DictFilter dictFilter);
}
