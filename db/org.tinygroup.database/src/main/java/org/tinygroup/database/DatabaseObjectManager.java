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
package org.tinygroup.database;

import java.util.List;

/**
 * 数据库对象管理器
 * @author luoguo
 *
 */
public interface DatabaseObjectManager {
	/**
	 * 返回自定义SQL名字列表
	 * @param dialectName
	 * @param packageName
	 * @return
	 */
	List<String> getCustomSqlNames(String dialectName, String packageName);

	/**
	 * 返回表名列表
	 * @param dialectName
	 * @param packageName
	 * @param tableName
	 * @return
	 */
	List<String> getTableNames(String dialectName, String packageName,
			String tableName);

	/**
	 * 返回存储过程列表
	 * @param dialectName
	 * @param packageName
	 * @return
	 */
	List<String> getProcedureNames(String dialectName, String packageName);

	/**
	 * 返回视图名列表
	 * @param dialectName
	 * @param packageName
	 * @param tableName
	 * @return
	 */
	List<String> getViewNames(String dialectName, String packageName,
			String viewName);

	/**
	 * 返回索引列表
	 * @param dialectName
	 * @param packageName
	 * @param tableName
	 * @return
	 */
	List<String> getIndexNames(String dialectName, String packageName,
			String tableName);

	/**
	 * 返回有初始化数据的表名列表
	 * @param dialectName
	 * @param packageName
	 * @return
	 */
	List<String> getInitDataTableNames(String dialectName, String packageName);

}
