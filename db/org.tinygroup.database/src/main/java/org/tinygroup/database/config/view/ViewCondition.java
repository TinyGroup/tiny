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
package org.tinygroup.database.config.view;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("view-condition")
public class ViewCondition {
//	@XStreamAlias("standard-field-name")
//	@XStreamAsAttribute
//	private String standardFieldName;// 源字段
//	@XStreamAsAttribute
//	private String table;
//	@XStreamAsAttribute
//	private String alias;// 源字段别名
	
	@XStreamAlias("key-field")
	@XStreamAsAttribute
	private ViewFieldRef keyField;
	
	@XStreamAsAttribute
	private String operator;
	@XStreamAsAttribute
	private String value;
	@XStreamAlias("view-conditions")
	List<ViewCondition> conditionList;// 表间有主外键关系的，不用加条件
	@XStreamAlias("value-field")
	@XStreamAsAttribute
	private ViewFieldRef valueField;

	public ViewFieldRef getKeyField() {
		return keyField;
	}

	public void setKeyField(ViewFieldRef keyField) {
		this.keyField = keyField;
	}

	//	public String getAlias() {
//		return alias;
//	}
//
//	public void setAlias(String alias) {
//		this.alias = alias;
//	}
	// public String getTable() {
	// return table;
	// }
	//
	// public void setTable(String table) {
	// this.table = table;
	// }
	// public String getStandardFieldName() {
	// return st public String getTable() {
	// return table;
	// }
	//
	// public void setTable(String table) {
	// this.table = table;
	// }
	// public String getStandardFieldName() {
	// return standardFieldName;
	// }
	//
	// public void setStandardFieldName(String standardFieldName) {
	// this.standardFieldName = standardFieldName;
	// }andardFieldName;
	// }
	//
	// public void setStandardFieldName(String standardFieldName) {
	// this.standardFieldName = standardFieldName;
	// }
	public ViewFieldRef getValueField() {
		return valueField;
	}

	public void setValueField(ViewFieldRef valueField) {
		this.valueField = valueField;
	}

	public List<ViewCondition> getConditionList() {
		if (conditionList == null)
			conditionList = new ArrayList<ViewCondition>();
		return conditionList;
	}

	public void setConditionList(List<ViewCondition> conditionList) {
		this.conditionList = conditionList;
	}

	

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
	

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
