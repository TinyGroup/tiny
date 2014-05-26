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
import org.tinygroup.database.config.table.Tables;

/**
 * 与数据表相关的处理
 * 
 * @author luoguo
 * 
 */
public interface TableProcessor {
	String BEAN_NAME="tableProcessor";

	void addTables(Tables tables);
	
	void removeTables(Tables tables);
	
	void addTable(Table table);
	
	void removeTable(Table table);

	Table getTable(String packageName, String name);

	Table getTable(String name);

	Table getTableById(String id);

	List<Table> getTables();

	List<String> getCreateSql(String name, String packageName, String language);

	List<String> getCreateSql(String name, String language);

	List<String> getCreateSql(Table table, String packageName, String language);

	List<String> getCreateSql(Table table, String language);

	List<String> getUpdateSql(String name, String packageName,
			DatabaseMetaData metadata,String catalog, String language);

	List<String> getUpdateSql(Table table, String packageName,
			DatabaseMetaData metadata,String catalog, String language);

	String getDropSql(String name, String packageName, String language);

	String getDropSql(Table table, String packageName, String language);

	boolean checkTableExist(Table table, String catalog,
			DatabaseMetaData metadata, String language);

}
