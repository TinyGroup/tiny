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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.database.config.table.Table;
import org.tinygroup.database.config.table.TableField;
import org.tinygroup.database.util.DataBaseUtil;
import org.tinygroup.metadata.config.stdfield.StandardField;
import org.tinygroup.metadata.util.MetadataUtil;

public class DerbySqlProcessorImpl extends SqlProcessorImpl {

	protected String getDatabaseType() {
		return "derby";
	}

	String appendIncrease() {
		return " GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) ";
	}
	
	public boolean checkTableExist(Table table, String catalog,
			DatabaseMetaData metadata) {

		ResultSet r = null;
		try {
			String schema = DataBaseUtil.getSchema(table, metadata);
			r = metadata.getTables(catalog, schema.toUpperCase(), table.getNameWithOutSchema().toUpperCase(),
					new String[] { "TABLE" });

			if (r.next()) {
				return true;
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			DataBaseUtil.closeResultSet(r);
		}

		return false;
	}
	
	
	protected List<String> dealExistFields(
			Map<String, TableField> existInTable,
			Map<String, Map<String, String>> dbColumns, Table table) {
		List<String>  existUpdateList = new ArrayList<String>();
		for(String fieldName:existInTable.keySet()){
			TableField field = existInTable.get(fieldName);
			if(field.getPrimary()){
				continue;
			}
			StandardField standardField = MetadataUtil.getStandardField(field
					.getStandardFieldId());
			Map<String, String> attribute = dbColumns.get(fieldName);
			String tableDataType = MetadataUtil.getStandardFieldType(
					standardField.getId(), getDatabaseType());
			String dbColumnType = getDbColumnType(attribute).replaceAll(" ", "").toLowerCase();
			if(dbColumnType.indexOf(tableDataType.replaceAll(" ", "").toLowerCase())==-1){
				existUpdateList.add( String.format("ALTER TABLE %s ALTER %s SET DATA TYPE %s", table.getName(),fieldName,tableDataType));
			}
			
//			StandardField standardField = MetadataUtil.getStandardField(field
//					.getStandardFieldId());
//			//如果数据库中字段允许为空，但table中不允许为空
			if(field.getNotNull()&&
					Integer.parseInt( attribute.get(NULLABLE))==DatabaseMetaData.columnNullable ){
				existUpdateList.add(String.format("ALTER TABLE %s ALTER  %s NOT NULL",
						table.getName(), fieldName));
			}else if(!field.getNotNull()&&
					Integer.parseInt(attribute.get(NULLABLE))==DatabaseMetaData.columnNoNulls){
				existUpdateList.add(String.format("ALTER TABLE %s ALTER  %s NULL",
						table.getName(), fieldName));
			}
			
		}
		return existUpdateList;
	}
	
	protected Map<String, Map<String, String>> getColumns(
			DatabaseMetaData metadata, String catalog, Table table) {
		Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
		try {
			String schema = DataBaseUtil.getSchema(table, metadata);
			if(schema!=null){
				schema=schema.toUpperCase();
			}
			String tableName=table.getNameWithOutSchema().toUpperCase();
			map= getColumns(metadata, catalog, schema, tableName);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return map;
	
	}

}
