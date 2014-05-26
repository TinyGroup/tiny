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
import java.util.List;
import java.util.Map;

import org.tinygroup.database.config.table.Table;
import org.tinygroup.database.config.table.TableField;
import org.tinygroup.database.util.DataBaseUtil;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.metadata.config.stdfield.StandardField;
import org.tinygroup.metadata.util.MetadataUtil;

public class OracleSqlProcessorImpl extends SqlProcessorImpl {
	private static Logger logger = LoggerFactory
			.getLogger(OracleSqlProcessorImpl.class);

	protected String getDatabaseType() {
		return "oracle";
	}

	String appendIncrease() {
		return "";
	}

	// 覆盖父类方法
	protected void appendBody(StringBuffer ddlBuffer, String packageName,
			Table table, List<String> list) {
		boolean isFirst = true;
		for (TableField field : table.getFieldList()) {
			if (!isFirst) {
				ddlBuffer.append(",");
			} else {
				isFirst = false;
			}
			appendField(ddlBuffer, field);
			if (field.isAutoIncrease() && field.getPrimary()) {// 如果是自增而且是主键
				appendSeq(list, table, field);
				appendTrigger(list, table, field);
			}
			// 外键FOREIGN KEY REFERENCES Persons(Id_P)
			// 目前的设定好象是不进行设置？
		}
	}

	private void appendSeq(List<String> list, Table table, TableField field) {
		String seq = "CREATE SEQUENCE "
				+ getSequenceName(table, field)
				+ " INCREMENT BY 1 START WITH 1 MAXVALUE 1.0E28 MINVALUE 1 NOCYCLE  NOCACHE NOORDER";
		list.add(seq);
	}

	private void appendTrigger(List<String> list, Table table, TableField field) {
		StandardField standardField = MetadataUtil.getStandardField(field
				.getStandardFieldId());
		String fieldName = DataBaseUtil
				.getDataBaseName(standardField.getName());
		StringBuffer trigger = new StringBuffer();
		trigger.append("CREATE OR REPLACE TRIGGER ").append(
				getTriggerName(table, field));
		trigger.append(" BEFORE INSERT ");
		trigger.append(" ON ").append(table.getName()).append(" FOR EACH ROW ");
		trigger.append(" BEGIN SELECT ");
		trigger.append(getSequenceName(table, field)).append(".NEXTVAL INTO:");
		trigger.append("NEW.").append(fieldName);
		trigger.append(" FROM DUAL;");
		trigger.append(" END;");
		list.add(trigger.toString());
	}

	private String getTriggerName(Table table, TableField field) {
		// return String.format("%s_%s_trigger", table.getName(),
		// field.getId());
		return String.format("%s_trigger", table.getName()).toUpperCase();
	}

	private String getSequenceName(Table table, TableField field) {
		// return String.format("%s_%s_seq", table.getName(), field.getId());
		return String.format("%s_seq", table.getName()).toUpperCase();
	}

	protected void getOtherUpdate(Table table, DatabaseMetaData metadata,
			String catalog, List<String> list) {
		TableField field = DataBaseUtil.getPrimaryField(table);
		if (field.isAutoIncrease()) {
			getSeqUpdate(table, field, catalog, metadata, list);
			getTriggerUpdate(table, field, catalog, metadata, list);
		}
	}

	private void getTriggerUpdate(Table table, TableField pField,
			String catalog, DatabaseMetaData metadata, List<String> list) {
		String trigger = getTriggerName(table, pField);
		ResultSet r = null;
		try {
			r = metadata.getTables(catalog,
					DataBaseUtil.getSchema(table, metadata), trigger,
					new String[] { "TRIGGER" });
			while (r.next()) {
				// 如果存在该trigger，则直接跳过
				return;
			}
			appendTrigger(list, table, pField);
		} catch (SQLException e) {
			logger.errorMessage("查找TRIGGER:{trigger}时出错", e, trigger);
		} finally {
			DataBaseUtil.closeResultSet(r);
		}

	}

	private void getSeqUpdate(Table table, TableField pField, String catalog,
			DatabaseMetaData metadata, List<String> list) {
		String seq = getSequenceName(table, pField);
		ResultSet r = null;
		try {
			r = metadata.getTables(catalog,
					DataBaseUtil.getSchema(table, metadata), seq,
					new String[] { "SEQUENCE" });
			while (r.next()) {
				// 如果存在该sequence，则直接跳过
				return;
			}
			appendSeq(list, table, pField);
		} catch (SQLException e) {
			logger.errorMessage("查找TRIGGER:{seq}时出错", e, seq);
		}finally {
			DataBaseUtil.closeResultSet(r);
		}
	}
	
	protected List<String> dealExistFields(Map<String, TableField> existInTable,Map<String, Map<String, String>> dbColumns, Table table){
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
				existUpdateList.add( String.format("ALTER TABLE %s MODIFY %s %s ", table.getName(),fieldName,tableDataType));
			}
//			logger.logMessage(LogLevel.INFO, tableDataType+"+"+dbColumnType);
//			StandardField standardField = MetadataUtil.getStandardField(field
//					.getStandardFieldId());
//			//如果数据库中字段允许为空，但table中不允许为空
			if(field.getNotNull()&&
					Integer.parseInt( attribute.get(NULLABLE))==DatabaseMetaData.columnNullable ){
				existUpdateList.add(String.format("ALTER TABLE %s MODIFY %s NOT NULL",
						table.getName(), fieldName));
			}else if(!field.getNotNull()&&
					Integer.parseInt(attribute.get(NULLABLE))==DatabaseMetaData.columnNoNulls ){
				existUpdateList.add(String.format("ALTER TABLE %s MODIFY %s NULL",
						table.getName(), fieldName));
			}
			
		}
		return existUpdateList;
	}
}
