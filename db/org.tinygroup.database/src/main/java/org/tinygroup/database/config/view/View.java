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

import org.tinygroup.metadata.config.BaseObject;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * @author chenjiao
 *
 */
@XStreamAlias("view")
public class View extends BaseObject {
	@XStreamAsAttribute
	private String schema;
	@XStreamAlias("view-fields")
	List<ViewField> fieldList;
	@XStreamAlias("view-conditions")
	List<ViewCondition> conditionList;// 表间有主外键关系的，不用加条件
	@XStreamAlias("order-by-fields")
	List<OrderByField> orderByFieldList;
	@XStreamAlias("view-tables")
	List<ViewTable> tableList;
	@XStreamAlias("view-havings")
	List<ViewHaving> havingList;// 表间有主外键关系的，不用加条件
	
	public String getName() {
		if (getSchema() == null || "".equals(getSchema()))
			return super.getName();
		return String.format("%s.%s", getSchema(), super.getName());
	}
	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public List<ViewHaving> getHavingList() {
		if (havingList == null)
			havingList = new ArrayList<ViewHaving>();
		return havingList;
	}

	public void setHavingList(List<ViewHaving> havingList) {
		this.havingList = havingList;
	}

	public List<ViewTable> getTableList() {
		if (tableList == null)
			tableList = new ArrayList<ViewTable>();
		return tableList;
	}

	public void setTableList(List<ViewTable> tableList) {
		this.tableList = tableList;
	}

	public List<ViewField> getFieldList() {
		if (fieldList == null)
			fieldList = new ArrayList<ViewField>();
		return fieldList;
	}

	public void setFieldList(List<ViewField> fieldList) {
		this.fieldList = fieldList;
	}

	public List<ViewCondition> getConditionList() {
		if (conditionList == null)
			conditionList = new ArrayList<ViewCondition>();
		return conditionList;
	}

	public void setConditionList(List<ViewCondition> conditionList) {
		this.conditionList = conditionList;
	}

	public List<OrderByField> getOrderByFieldList() {
		if (orderByFieldList == null)
			orderByFieldList = new ArrayList<OrderByField>();
		return orderByFieldList;
	}

	public void setOrderByFieldList(List<OrderByField> orderByFieldList) {
		this.orderByFieldList = orderByFieldList;
	}

	// @XStreamAlias("group-by-fields")
	// List<GroupByField> groupByFieldList;
	// public List<GroupByField> getGroupByFieldList() {
	// if(groupByFieldList==null)
	// groupByFieldList = new ArrayList<GroupByField>();
	// return groupByFieldList;
	// }
	// public void setGroupByFieldList(List<GroupByField> groupByFieldList) {
	// this.groupByFieldList = groupByFieldList;
	// }
}
