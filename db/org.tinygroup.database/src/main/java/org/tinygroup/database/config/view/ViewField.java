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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
@XStreamAlias("view-field")
public class ViewField{
	@XStreamAlias("view-table-id")
	@XStreamAsAttribute
	String viewTable; //viewTable的id
	@XStreamAsAttribute
	String alias;//别名 as
	@XStreamAsAttribute
	String tableFieldId; //表格字段id
	@XStreamAsAttribute
	String id; //视图字段本身的id
	
	public String getTableFieldId() {
		return tableFieldId;
	}
	public void setTableFieldId(String tableFieldId) {
		this.tableFieldId = tableFieldId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getViewTable() {
		return viewTable;
	}
	public void setViewTable(String viewTable) {
		this.viewTable = viewTable;
	}
	
	
	
	
}
