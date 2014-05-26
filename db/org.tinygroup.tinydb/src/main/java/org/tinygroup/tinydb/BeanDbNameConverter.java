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
package org.tinygroup.tinydb;

/**
 * Bean属性名与数据库属性名之间的转换函数<br>
 * 默认实现规则：数据库通过下划分分隔单词，Bean属性名采用驼峰规则(需要严格遵守Bean规范)
 * 
 * @author luoguo
 * 
 */
public interface BeanDbNameConverter {
	/**
	 * 数据库字段名转化为属性名
	 * 
	 * @param dbFieldName
	 * @return
	 */
	String dbFieldNameToPropertyName(String dbFieldName);

	/**
	 * 属性名转化为数据库字段名
	 * 
	 * @param propertyName
	 * @return
	 */
	String propertyNameToDbFieldName(String propertyName);

	/**
	 * 数据库表名转换为类型名
	 * 
	 * @param tableName
	 * @return
	 */
	String dbTableNameToTypeName(String tableName);

	/**
	 * 类型名转换为数据库表名
	 * 
	 * @param typeName
	 * @return
	 */
	String typeNameToDbTableName(String typeName);
}
