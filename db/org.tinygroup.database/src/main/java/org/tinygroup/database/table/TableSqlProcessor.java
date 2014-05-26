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
package org.tinygroup.database.table;

import java.sql.DatabaseMetaData;
import java.util.List;

import org.tinygroup.database.config.table.Table;


public interface TableSqlProcessor {
	String NULLABLE="NULLABLE";
	String TYPE_NAME="TYPE_NAME";
	String COLUMN_SIZE="COLUMN_SIZE";
	String DECIMAL_DIGITS="DECIMAL_DIGITS";
	String COLUMN_NAME="COLUMN_NAME";
	/**
	 * 获得创建表格的语句(包含index)
	 * @param table
	 * @param packageName
	 * @return
	 */
	List<String> getCreateSql(Table table, String packageName);
	/**
	 * 获得创建表格的语句(不包含index)
	 * @param table
	 * @param packageName
	 * @return
	 */
	List<String> getTableCreateSql(Table table, String packageName);
	/**
	 * 获得创建索引的语句
	 * @param table
	 * @param packageName
	 * @return
	 */
	List<String> getIndexCreateSql(Table table, String packageName);
	/**
	 * 获得(正向)Update表格的语句
	 * @param table
	 * @param packageName
	 * @param metadata
	 * @return
	 */
	List<String> getUpdateSql(Table table, String packageName,
			DatabaseMetaData metadata,String catalog);
	
	
	String getDropSql(Table table, String packageName);
	
	boolean checkTableExist(Table table,String catalog,
			DatabaseMetaData metadata);
}
