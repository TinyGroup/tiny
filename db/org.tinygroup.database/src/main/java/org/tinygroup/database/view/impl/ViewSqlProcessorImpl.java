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
package org.tinygroup.database.view.impl;

import java.sql.DatabaseMetaData;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.database.config.table.Table;
import org.tinygroup.database.config.table.TableField;
import org.tinygroup.database.config.view.Having;
import org.tinygroup.database.config.view.OrderByField;
import org.tinygroup.database.config.view.View;
import org.tinygroup.database.config.view.ViewCondition;
import org.tinygroup.database.config.view.ViewField;
import org.tinygroup.database.config.view.ViewFieldRef;
import org.tinygroup.database.config.view.ViewHaving;
import org.tinygroup.database.config.view.ViewTable;
import org.tinygroup.database.util.DataBaseUtil;
import org.tinygroup.database.view.ViewSqlProcessor;
import org.tinygroup.metadata.config.stdfield.StandardField;
import org.tinygroup.metadata.util.MetadataUtil;

public class ViewSqlProcessorImpl implements ViewSqlProcessor {

	public String getCreateSql(View view) {
		// 这里生成视图的逻辑过于复杂了
		// 是否可以考虑直接让用户写代码块
		StringBuffer sb = new StringBuffer();
		appendHead(view.getName(), sb);
		
		Map<String,String> tableNames = new HashMap<String,String>();//table的 id与tableName映射
		appendTables(view, sb,tableNames);
		Map<String,String> fieldNames= new HashMap<String,String>();//viewfield的 id与fieldName映射
		appendFields(view,sb,tableNames,fieldNames);
		//处理条件
		appendCondition(view,sb,tableNames,fieldNames);
		//appendGroupBy(view, sb);
		//处理Having子句
		appendHaving(view,sb,tableNames,fieldNames);
		appendOrderBy(view,sb,tableNames,fieldNames);
		sb.append(";");
		return sb.toString();
	}

	// CREATE VIEW [视图名] AS
	// SELECT 字段1,字段2
	// FROM 表1,表2
	// WHERE 字段=值
	private void appendHead(String viewName, StringBuffer sb) {
		sb.append("CREATE OR REPLACE VIEW ");
		sb.append(viewName).append(" ");
		sb.append("AS ");
	}

	private void appendFields(View view, StringBuffer sb,Map<String,String> tableNames,Map<String,String> fieldNames) {
		sb.append(" SELECT ");
		String fieldStr = "";
		for (ViewField field : view.getFieldList()) {
			String fieldName = getFieldName(view,field,tableNames,fieldNames);
			
			fieldStr = fieldStr + fieldName + ",";
		}
		if (fieldStr.endsWith(",")) {
			fieldStr = fieldStr.substring(0, fieldStr.length() - 1);
		}
		sb.append(fieldStr);
	}

	private void appendTables(View view, StringBuffer sb,Map<String,String> tableNames) {
		sb.append(" FROM ");
		String tableStr = "";
		for (ViewTable viewTable : view.getTableList()) {
			String tableName = getTableName(viewTable, tableNames);
			tableStr = tableStr + tableName + ",";
		}
		if (tableStr.endsWith(",")) {
			tableStr = tableStr.substring(0, tableStr.length() - 1);
		}
		sb.append(tableStr);
	}

	// 这里暂时只有 key = value and key = value;
	private void appendCondition(View view, StringBuffer sb,Map<String,String> tableNames,Map<String,String> fieldNames) {
		if (view.getConditionList() == null
				|| view.getConditionList().size() == 0)
			return;
		sb.append(" WHERE ");
		String conditionStr = dealCondtionList(view,view.getConditionList(),tableNames,fieldNames);
		sb.append(conditionStr);
	}

	private void appendHaving(View view, StringBuffer sb,Map<String,String> tableNames,Map<String,String> fieldNames) {
		if (isNull(view.getHavingList())) {
			return;
		}
		sb.append(" HAVING ");
		String havingStr = dealHavingList(view,view.getHavingList(),tableNames, fieldNames);
		sb.append(havingStr);
	}

	private boolean isNull(List<?> list) {
		if (list == null || list.size() == 0) {
			return true;
		}
		return false;
	}

	private String dealHavingList(View view,List<ViewHaving> list,Map<String,String> tableNames,Map<String,String> fieldNames) {
		if (isNull(list))
			return "";
		String subHavingStr = "";
		if (list.size() == 1) {
			subHavingStr = getHaving(view,list.get(0),tableNames, fieldNames);
		} else {
			for (ViewHaving subHaving : list) {
				subHavingStr += String.format("( %s ) AND ",
						getHaving(view,subHaving,tableNames, fieldNames));
			}
			int index = subHavingStr.lastIndexOf("AND");
			subHavingStr = subHavingStr.substring(0, index);
		}
		return subHavingStr;
	}

	private String getHaving(View view,ViewHaving viewHaving,Map<String,String> tableNames,Map<String,String> fieldNames) {
		String havingValue = "";
		if (viewHaving.getValueHaving() == null) {
			havingValue = viewHaving.getValue();
		} else {
			Having valueHaving = viewHaving.getValueHaving();
			havingValue = parseHaving(view,valueHaving,tableNames, fieldNames);
		}
		String havingKey = parseHaving(view,viewHaving.getKeyHaving(),tableNames, fieldNames);
		return String.format(" %s %s %s ", havingKey, viewHaving.getOperator(),
				havingValue);
	}

	private String parseHaving(View view, Having having,Map<String,String> tableNames,Map<String,String> fieldNames){
		ViewFieldRef field = having.getField();
		String fieldStr = getViewFieldRefName(view, field, tableNames, fieldNames);
		String function = having.getAggregateFunction();
		String havingStr = "";
		if(function==null||"".equals(function)){
			havingStr = fieldStr;
		}else{
			havingStr = String.format(" %s( %s ) ", function,fieldStr);
		}
		return havingStr;
	}

	private String getCondition(View view,ViewCondition condition,Map<String,String> tableNames,Map<String,String> fieldNames) {
		
		String conditionValue = "";
		if (condition.getValueField() == null) { //如果条件字段为空，则读取条件配置的固定值
			conditionValue = condition.getValue();
		} else {
			ViewFieldRef valueField = condition.getValueField();
			if( valueField.getViewFieldId() == null){
				conditionValue  = getViewFieldRefName(view, valueField, tableNames, fieldNames);
			}else{
				conditionValue = fieldNames.get(valueField.getViewFieldId());
			}
		}
		String fieldName = "";
		ViewFieldRef keyField = condition.getKeyField();
		if( keyField.getViewFieldId() == null){
			fieldName  = getViewFieldRefName(view, keyField, tableNames, fieldNames);
		}else{
			fieldName = fieldNames.get(keyField.getViewFieldId());
		}
		String conditionStr = fieldName + condition.getOperator()
				+ conditionValue;
		String subCondtionStr = dealCondtionList(view,condition.getConditionList(), tableNames, fieldNames);
		if ("".equals(subCondtionStr))
			return conditionStr;
		if (condition.getConditionList().size() > 1) {
			return String
					.format(" %s OR ( %s ) ", conditionStr, subCondtionStr);
		} else {
			return String.format(" %s OR %s ", conditionStr, subCondtionStr);
		}

	}

	private String dealCondtionList(View view,List<ViewCondition> list,Map<String,String> tableNames,Map<String,String> fieldNames) {
		if (list == null || list.size() == 0) {
			return "";
		}
		String subCondtionStr = "";
		if (list.size() == 1) {
			subCondtionStr = getCondition(view,list.get(0),tableNames,fieldNames);
		} else {
			for (ViewCondition subCondition : list) {
				subCondtionStr += String.format("( %s ) AND ",
						getCondition(view,subCondition,tableNames,fieldNames));
			}
			int index = subCondtionStr.lastIndexOf("AND");
			subCondtionStr = subCondtionStr.substring(0, index);
		}
		return subCondtionStr;
	}

	private void appendOrderBy(View view, StringBuffer sb,Map<String,String> tableNames,Map<String,String> fieldNames) {
		if (view.getOrderByFieldList() == null
				|| view.getOrderByFieldList().size() == 0) {
			return;
		}

		String orderByFieldStr = "";
		for (OrderByField field : view.getOrderByFieldList()) {
			if (field.getField() == null) {
				continue;
			}
			ViewFieldRef viewFieldRef = field.getField();
			String fieldName = getViewFieldRefName(view, viewFieldRef, tableNames, fieldNames);
			orderByFieldStr += fieldName + " " + field.getDirection() + ",";
		}
		if ("".equals(orderByFieldStr)) {
			return;
		}
		orderByFieldStr = orderByFieldStr.substring(0,
				orderByFieldStr.length() - 1);
		sb.append(" ORDER BY ");
		sb.append(orderByFieldStr);
	}

//	private void appendGroupBy(View view, StringBuffer sb,Map<String,String> tableNames,Map<String,String> fieldNames) {
//		if (view.getGroupByFieldList() == null
//				|| view.getGroupByFieldList().size() == 0) {
//			return;
//		}
//		String groupByFieldStr = null;
//		for (GroupByField field : view.getGroupByFieldList()) {
//			if (field.getField() == null) {
//				continue;
//			}
//			ViewField viewField = field.getField();
//			String fieldName = viewField.getTable() + "."
//						+ viewField.getStandardFieldName(); //group by不可使用别名
//
//			if (groupByFieldStr == null) {
//				groupByFieldStr = " GROUP BY " + fieldName;
//			} else {
//				groupByFieldStr += " , " + fieldName;
//			}
//		}
//		sb.append(groupByFieldStr);
//	}

	private String getFieldName(View view,ViewField field,Map<String,String> tableNames,Map<String,String> fieldNames) {
		String fieldAlias = field.getAlias();
		String viewTableId = field.getViewTable();//
		String tableId = getViewTable(viewTableId,view).getTableId();
		
		Table table = DataBaseUtil.getTableById(tableId);//获取字段所在的表
		TableField tableField = getTableField(field.getTableFieldId(), table);
		StandardField tableFieldStd = MetadataUtil.getStandardField(tableField.getStandardFieldId());
		String tableFieldName = DataBaseUtil.getDataBaseName( tableFieldStd.getName());
		String fieldName = "";
		
		if (fieldAlias == null || "".equals(fieldAlias)) {
			fieldName = tableNames.get(viewTableId) + "." + tableFieldName;
			fieldNames.put(field.getId(), fieldName);
		} else {
			fieldName = tableNames.get(viewTableId) + "." + tableFieldName + " AS "
					+ fieldAlias;
			fieldNames.put(field.getId(), fieldAlias);
		}
		return fieldName;
	}

	private String getTableName(ViewTable viewTable,Map<String,String> tableNames) {
		String tableAlias = viewTable.getTableAlias();
		String tableId = viewTable.getTableId();
		String tableName = DataBaseUtil.getTableById(tableId).getName();
		if (tableAlias == null || "".equals(tableAlias)) {
			tableNames.put(viewTable.getId(), tableName);
		} else {
			tableName = tableName + " " + tableAlias;
			tableNames.put(viewTable.getId(), tableAlias);
		}
		return tableName;
	}

	public String getDropSql(View view) {
		return String.format("DROP VIEW %s", view.getName());
	}
	
	private TableField getTableField(String fieldId,Table table){
		for(TableField field:table.getFieldList()){
			if(field.getId().equals(fieldId))
				return field;
		}
		return null;
	}
	
	private Table getTable(String viewTableId,View view){
		for(ViewTable viewTable:view.getTableList()){
			if(viewTable.getId().equals(viewTableId)){
				String tableId  = viewTable.getTableId();
				return DataBaseUtil.getTableById(tableId);
				
			}
		}
		return null;
	}

	private ViewTable getViewTable(String viewTableId,View view){
		for(ViewTable vt : view.getTableList()){
			if(vt.getId().equals(viewTableId))
				return vt;
		}
		return null;
	}
	
	private String getViewFieldRefName(View view,ViewFieldRef viewFieldRef,Map<String,String> tableNames,Map<String,String> fieldNames){
		if(viewFieldRef.getViewFieldId()!=null){
			return fieldNames.get(viewFieldRef.getViewFieldId());
		}else{
			return getViewFieldRefName(view,viewFieldRef,tableNames);
		}
	}
	
	private String getViewFieldRefName(View view,ViewFieldRef viewFieldRef,Map<String,String> tableNames){
		Table table = getTable(viewFieldRef.getViewTableId(),view); //根据存放的表格id获取表格
		TableField tableField = getTableField(viewFieldRef.getTableFieldId(), table); //根据存放的表格字段id获取表格字段
		StandardField stdField = MetadataUtil.getStandardField(tableField.getStandardFieldId());//根据表格字段获取对应的标准字段
		String tableName = tableNames.get(viewFieldRef.getViewTableId()); //获取表格对应的表明
		String fieldName = DataBaseUtil.getDataBaseName(stdField.getName() );
		return tableName+"."+fieldName;
	}

	public List<String> getUpdateSql(View view, DatabaseMetaData metadata) {
		return null;
	}

	public List<String> getDeupdateSql(View view, DatabaseMetaData metadata) {
		return null;
	}

	

	public boolean checkViewExist(View view, String catalog,
			DatabaseMetaData metadata) {
		
		return false;
	}
}
