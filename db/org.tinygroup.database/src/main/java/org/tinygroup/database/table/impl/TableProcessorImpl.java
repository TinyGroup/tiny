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
package org.tinygroup.database.table.impl;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.database.ProcessorManager;
import org.tinygroup.database.config.table.Table;
import org.tinygroup.database.config.table.Tables;
import org.tinygroup.database.table.TableProcessor;
import org.tinygroup.database.table.TableSqlProcessor;
import org.tinygroup.database.util.DataBaseUtil;
import org.tinygroup.metadata.util.MetadataUtil;
import org.tinygroup.springutil.SpringUtil;

public class TableProcessorImpl implements TableProcessor {
	private  Map<String, Map<String, Table>> tableMap = new HashMap<String, Map<String, Table>>();

	public void addTables(Tables tables) {
		String packageName = MetadataUtil.passNull(tables.getPackageName());
		if(!tableMap.containsKey(packageName)){
			tableMap.put(packageName, new HashMap<String, Table>());
		}
		Map<String, Table> nameMap = tableMap.get(packageName);
		for (Table table : tables.getTableList()) {
			nameMap.put(table.getName(), table);
		}
	}
	
	public void removeTables(Tables tables) {
		String packageName = MetadataUtil.passNull(tables.getPackageName());
		Map<String, Table> nameMap = tableMap.get(packageName);
		if(!CollectionUtil.isEmpty(nameMap)){
			for (Table table : tables.getTableList()) {
				nameMap.remove(table.getName());
			}
		}
	}
	
	public void addTable(Table table) {
		String packageName = MetadataUtil.passNull(table.getPackageName());
		if(!tableMap.containsKey(packageName)){
			tableMap.put(packageName, new HashMap<String, Table>());
		}
		Map<String, Table> nameMap = tableMap.get(packageName);
	    nameMap.put(table.getName(), table);
	}
	
	public void removeTable(Table table) {
		String packageName = MetadataUtil.passNull(table.getPackageName());
		Map<String, Table> nameMap = tableMap.get(packageName);
		if(!CollectionUtil.isEmpty(nameMap)){
			nameMap.remove(table.getName());
		}
	}

	public Table getTable(String packageName, String name) {
		if (packageName != null) {
			Map<String, Table> packageMap = tableMap.get(packageName);
			if (packageMap != null) {
				Table table = packageMap.get(name);
				if (table != null) {
					return table;
				}
			} else {
				//如果当前包中没有找到，则从所有的包中去找
//				throw new RuntimeException("未找到package:" + packageName
//						+ ",name:" + name + "的表格");
			}
		}
		for (String pkgName : tableMap.keySet()) {
			Map<String, Table> packageMap = tableMap.get(pkgName);
			if (packageMap != null) {
				Table table = packageMap.get(name);
				if (table != null) {
					return table;
				}
			}
		}
		throw new RuntimeException("未找到package:" + packageName + ",name:"
				+ name + "的表格");
	}

	public Table getTable(String name) {
		return getTable(null, name);
	}

	public List<String> getCreateSql(String name, String language) {
		return getCreateSql(name, null, language);
	}

	public List<String> getCreateSql(String name, String packageName,
			String language) {
		Table table = getTable(packageName, name);
		return getCreateSql(table, packageName, language);
	}

	public List<String> getCreateSql(Table table, String packageName,
			String language) {
		TableSqlProcessor sqlProcessor = getSqlProcessor(language);
		return sqlProcessor.getCreateSql(table, packageName);
	}

	public List<String> getCreateSql(Table table, String language) {
		return getCreateSql(table, null, language);
	}

	public Table getTableById(String id) {
		for (Map<String, Table> tables : tableMap.values()) {
			for (Table table : tables.values()) {
				if (table.getId().equals(id)) {
					return table;
				}
			}
		}
		throw new RuntimeException("未找到ID:" + id + "的表格");
	}

	public List<Table> getTables() {
		List<Table> tableList = new ArrayList<Table>();
		for (Map<String, Table> tables : tableMap.values()) {
			tableList.addAll(tables.values());
		}
		return tableList;
	}

	public List<String> getUpdateSql(String name, String packageName,
			DatabaseMetaData metadata,String catalog, String language) {
		Table table = getTable(packageName, name);
		return getUpdateSql(table, packageName, metadata,catalog, language);
	}

	public List<String> getUpdateSql(Table table, String packageName,
			DatabaseMetaData metadata,String catalog, String language) {
		TableSqlProcessor sqlProcessor = getSqlProcessor(language);
		return sqlProcessor.getUpdateSql(table, packageName, metadata, catalog);
	}


	public String getDropSql(String name, String packageName, String language) {
		Table table = getTable(packageName, name);
		return getDropSql(table, packageName, language);
	}

	public String getDropSql(Table table, String packageName, String language) {
		TableSqlProcessor sqlProcessor = getSqlProcessor(language);
		return sqlProcessor.getDropSql(table, packageName);
	}

	public boolean checkTableExist(Table table, String catalog,
			DatabaseMetaData metadata, String language) {
		TableSqlProcessor sqlProcessor = getSqlProcessor(language);
		return sqlProcessor.checkTableExist(table, catalog, metadata);
	}

	private TableSqlProcessor getSqlProcessor(String language) {
		ProcessorManager processorManager = SpringUtil
				.getBean(DataBaseUtil.PROCESSORMANAGER_BEAN);
		TableSqlProcessor sqlProcessor = (TableSqlProcessor) processorManager
				.getProcessor(language, "table");
		return sqlProcessor;
	}

	

}
