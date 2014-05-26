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

import org.tinygroup.database.config.table.Index;
import org.tinygroup.database.config.table.Table;
import org.tinygroup.database.config.table.TableField;
import org.tinygroup.database.table.TableSqlProcessor;
import org.tinygroup.database.util.DataBaseNameUtil;
import org.tinygroup.database.util.DataBaseUtil;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.metadata.config.stdfield.StandardField;
import org.tinygroup.metadata.util.MetadataUtil;

public abstract class SqlProcessorImpl implements TableSqlProcessor {
	private static Logger logger = LoggerFactory
			.getLogger(SqlProcessorImpl.class);

	protected abstract String getDatabaseType();

	public List<String> getCreateSql(Table table, String packageName) {
		List<String> list = new ArrayList<String>();
		// 生成表格创建语句
		list.addAll(getTableCreateSql(table, packageName));
		// 生成index
		list.addAll(getIndexCreateSql(table, packageName));
		return list;
	}

	public List<String> getTableCreateSql(Table table, String packageName) {
		StringBuffer ddlBuffer = new StringBuffer();
		List<String> list = new ArrayList<String>();
		// 生成表格主体
		appendHeader(ddlBuffer, table);
		appendBody(ddlBuffer, packageName, table, list);
		appendFooter(ddlBuffer);
		list.add(0, ddlBuffer.toString());
		return list;
	}

	public List<String> getIndexCreateSql(Table table, String packageName) {
		return appendIndexs(table);
	}

	public List<String> getUpdateSql(Table table, String packageName,
			DatabaseMetaData metadata, String catalog) {
		List<String> list = new ArrayList<String>();
		getTableColumnUpdate(table, packageName, metadata, catalog, list);
		getOtherUpdate(table, metadata, catalog, list);
		return list;
	}

	protected void getOtherUpdate(Table table, DatabaseMetaData metadata,
			String catalog, List<String> list) {

	}

	protected void getTableColumnUpdate(Table table, String packageName,
			DatabaseMetaData metadata, String catalog, List<String> list) {

		Map<String, Map<String, String>> dbColumns = getColumns(metadata,
				catalog, table);
		// 存放table中有但数据库表格中不存在的字段， 初始化时存放所有的字段信息
		Map<String, TableField> tableFieldDbNames = getFiledDbNames(table
				.getFieldList());
		// 存放table中有且数据库表格中存在的字段
		Map<String, TableField> existInTable = new HashMap<String, TableField>();
		// 存放table中无，但数据库中有的字段
		List<String> dropFields = checkTableColumn(dbColumns,
				tableFieldDbNames, existInTable);
		try {
			// 处理完所有表格列，若filedDbNames中依然有数据，则表格该数据是新增字段
			for (TableField field : tableFieldDbNames.values()) {
				// 如果新增的字段包含表格的主键,则直接drop整张表格，重新创建表格
				if (field.getPrimary()) {
					list.add(getDropSql(table, packageName));
					list.addAll(getCreateSql(table, packageName));
					return;
				}
				// StringBuffer ddlBuffer = new StringBuffer();
				// ddlBuffer.append(String.format("ALTER TABLE %s ADD ",
				// table.getName()));
				// appendField(ddlBuffer, packageName, field);
				// addlist.add(ddlBuffer.toString());
			}
			// 对于table\数据库中均有的字段的处理，这里是对比是否允许为空而进行相关修改
			List<String> existUpdateList = dealExistFields(existInTable,
					dbColumns, table);
			// 生成drop字段的sql
			List<String> droplist = dealDropFields(dropFields, table);
			// 生成add字段的sql
			List<String> addlist = dealAddFields(tableFieldDbNames,
					packageName, table);
			list.addAll(existUpdateList);
			list.addAll(droplist);
			list.addAll(addlist);
		} catch (Exception e) {
			logger.errorMessage("生成表格{0}更新sql失败", e, table.getName());
		}

	}

	protected List<String> checkTableColumn(
			Map<String, Map<String, String>> columns,
			Map<String, TableField> fieldDbNames,
			Map<String, TableField> existInTable) {
		List<String> dropFields = new ArrayList<String>();
		for (String colum : columns.keySet()) {
			// 遍历当前表格所有列
			// 若存在于map，则不处理，切从map中删除该key
			// 若不存在于map，则从表格中删除该列
			if (fieldDbNames.containsKey(colum)) {
				existInTable.put(colum, fieldDbNames.get(colum));
				fieldDbNames.remove(colum);
				continue;
			}
			dropFields.add(colum);
		}
		return dropFields;
	}

	protected List<String> dealDropFields(List<String> dropFields, Table table) {
		List<String> droplist = new ArrayList<String>();
		for (String colum : dropFields) {
			StringBuffer ddlBuffer = new StringBuffer();
			ddlBuffer.append(String.format("ALTER TABLE %s DROP COLUMN %s",
					table.getName(), colum));
			droplist.add(ddlBuffer.toString());

		}
		return droplist;
	}

	protected List<String> dealAddFields(Map<String, TableField> fieldDbNames,
			String packageName, Table table) {
		List<String> addList = new ArrayList<String>();
		for (TableField field : fieldDbNames.values()) {
			StringBuffer ddlBuffer = new StringBuffer();
			ddlBuffer.append(String.format("ALTER TABLE %s ADD ",
					table.getName()));
			appendField(ddlBuffer, field);
			addList.add(ddlBuffer.toString());
		}
		return addList;
	}

	protected abstract List<String> dealExistFields(
			Map<String, TableField> existInTable,
			Map<String, Map<String, String>> dbColumns, Table table) ;

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
			// 外键FOREIGN KEY REFERENCES Persons(Id_P)
			// 目前的设定好象是不进行设置？
		}
	}

	protected void appendField(StringBuffer ddlBuffer, TableField field) {
		StandardField standardField = MetadataUtil.getStandardField(field
				.getStandardFieldId());
		ddlBuffer.append(String.format(" %s ",
				DataBaseUtil.getDataBaseName(standardField.getName())));
		ddlBuffer.append(" ");
		ddlBuffer.append(MetadataUtil.getStandardFieldType(
				standardField.getId(), getDatabaseType()));
		Boolean notNull = field.getNotNull();
		if (notNull != null && notNull.booleanValue()) {
			ddlBuffer.append(" NOT NULL");
		} else {
			notNull = standardField.getNotNull();
			if (notNull != null && notNull.booleanValue()) {
				ddlBuffer.append(" NOT NULL");
			}
		}

		Boolean primary = field.getPrimary();
		if (primary != null && primary.booleanValue()) {
			ddlBuffer.append(" PRIMARY KEY");
		} else {
			Boolean unique = field.getUnique();
			if (unique != null && unique.booleanValue()) {
				ddlBuffer.append(" UNIQUE");
			}
		}
		// 处理自增
		if (field.isAutoIncrease() && field.getPrimary()) {// 如果是自增而且是主键
			ddlBuffer.append(appendIncrease());
		}
	}

	abstract String appendIncrease();

	private List<String> appendIndexs(Table table) {
		List<String> indexSqlList = new ArrayList<String>();
		List<Index> list = table.getIndexList();
		if (list != null) {
			for (Index index : list) {
				indexSqlList.add(appendIndex(index, table));
			}
		}
		return indexSqlList;
	}

	private String appendIndex(Index index, Table table) {
		// DROP INDEX index_name ON table_name
		// DROP INDEX table_name.index_name
		// DROP INDEX index_name
		StringBuffer ddlBuffer = new StringBuffer();
		Boolean unique = index.getUnique();
		if (unique != null && unique.booleanValue()) {
			ddlBuffer.append("CREATE UNIQUE INDEX ");
		} else {
			ddlBuffer.append("CREATE INDEX ");
		}
		if (table.getSchema() == null || "".equals(table.getSchema())) {
			ddlBuffer.append(index.getName());
		} else {
			ddlBuffer.append(String.format("%s.%s", table.getSchema(),
					index.getName()));
		}

		ddlBuffer.append(" ON ");
		ddlBuffer.append(table.getName());
		ddlBuffer.append(" ( ");
		List<String> fields = index.getFields();
		String fieldsStr = "";
		if (fields != null) {
			for (String fieldId : fields) {
				fieldsStr = fieldsStr + ","
						+ getFieldStdFieldName(fieldId, table);
			}
			if (fieldsStr.startsWith(",")) {
				fieldsStr = fieldsStr.substring(1);
			}
		}
		ddlBuffer.append(fieldsStr);
		ddlBuffer.append(" ) ");
		return ddlBuffer.toString();
	}

	private String getFieldStdFieldName(String fieldId, Table table) {
		for (TableField field : table.getFieldList()) {
			if (field.getId().equals(fieldId)) {
				StandardField standardField = MetadataUtil
						.getStandardField(field.getStandardFieldId());
				return DataBaseUtil.getDataBaseName(standardField.getName());
			}
		}
		throw new RuntimeException(String.format(
				"未找到ID：%s的表格字段(或该表格字段对应的标准字段)", fieldId));
	}

	private void appendFooter(StringBuffer ddlBuffer) {
		ddlBuffer.append(")");
		// ddlBuffer.append("\n");
	}

	private void appendHeader(StringBuffer ddlBuffer, Table table) {
		ddlBuffer.append(String.format("CREATE TABLE %s (", table.getName()));
	}

	private Map<String, TableField> getFiledDbNames(List<TableField> fields) {
		Map<String, TableField> filedDbNames = new HashMap<String, TableField>();
		for (TableField field : fields) {
			StandardField standardField = MetadataUtil.getStandardField(field
					.getStandardFieldId());
			String filedDbName = DataBaseUtil.getDataBaseName(standardField
					.getName());
			filedDbNames.put(DataBaseNameUtil.getColumnNameFormat(filedDbName),
					field);
		}
		return filedDbNames;
	}

	public String getDropSql(Table table, String packageName) {
		return String.format("DROP TABLE %s", table.getName());
	}

	protected Map<String, Map<String, String>> getColumns(
			DatabaseMetaData metadata, String catalog, Table table) {
		Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
		try {
			String schema = DataBaseUtil.getSchema(table, metadata);
			String tableName=table.getNameWithOutSchema().toUpperCase();
			map= getColumns(metadata, catalog, schema, tableName);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return map;
	
	}
	
	protected Map<String, Map<String, String>> getColumns(
			DatabaseMetaData metadata, String catalog, String  schema,String tableName) {
		ResultSet colRet = null;
		Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
		try {
			colRet = metadata.getColumns(catalog, schema,tableName, "%");
			while (colRet.next()) {
				Map<String, String> attributes = new HashMap<String, String>();
				String columnName = colRet.getString(COLUMN_NAME);
				attributes.put(NULLABLE, colRet.getString(NULLABLE));
				attributes.put(TYPE_NAME, colRet.getString(TYPE_NAME));
				attributes.put(COLUMN_SIZE, colRet.getString(COLUMN_SIZE));
				attributes.put(DECIMAL_DIGITS, colRet.getString(DECIMAL_DIGITS));
				map.put(columnName.toUpperCase(), attributes);
			}
		} catch (SQLException e1) {
			throw new RuntimeException(e1);
		} finally {
			DataBaseUtil.closeResultSet(colRet);
		}
		return map;
	}
	

	public boolean checkTableExist(Table table, String catalog,
			DatabaseMetaData metadata) {
		ResultSet r = null;
		try {
			String schema = DataBaseUtil.getSchema(table, metadata);
			r = metadata.getTables(catalog, schema, table.getNameWithOutSchema().toUpperCase(),
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

	protected String getDbColumnType(Map<String,String> attributes){
		String lengthInfo = attributes.get(COLUMN_SIZE);
		if(attributes.get(DECIMAL_DIGITS)!=null){
			lengthInfo = lengthInfo+","+attributes.get(DECIMAL_DIGITS);
		}
		return String.format("%s(%s)", attributes.get(TYPE_NAME),lengthInfo);
	}
	// public List<String> getDeupdateSql(Table table, String packageName,
	// DatabaseMetaData metadata,String catalog ) {
	// List<String> list = new ArrayList<String>();
	// Map<String, String> columns = getColumns(metadata, catalog,table);
	// try {
	// for (TableField field : table.getFieldList()) {
	// if (findExist(field.getStandardFieldId(), columns)) {
	// continue;
	// }
	// StandardField standardField = MetadataUtil
	// .getStandardField(field.getStandardFieldId());
	// StringBuffer ddlBuffer = new StringBuffer();
	// ddlBuffer.append(String.format("ALTER TABLE %s DROP %s",
	// table.getName(),
	// DataBaseUtil.getDataBaseName(standardField.getName())));
	// // 20130216注释错误代码
	// // appendField(ddlBuffer, packageName, field);
	//
	// list.add(ddlBuffer.toString());
	// }
	// } catch (Exception e) {
	// logger.errorMessage("生成表格{0}反向更新sql失败", e, table.getName());
	// }
	//
	// return list;
	// }
	// private boolean findExist(String tableFiledId, Map<String, String> map) {
	// for (String colName : map.keySet()) {
	// StandardField standardField = MetadataUtil
	// .getStandardField(tableFiledId);
	// String fieldName = DataBaseUtil.getDataBaseName(standardField
	// .getName());
	// if (colName.equals(DataBaseNameUtil.getColumnNameFormat(fieldName)))
	// return true;
	// }
	// return false;
	// }
}
