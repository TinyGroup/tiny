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
package org.tinygroup.jsqlparser.statement.create.table;

import java.util.List;

import org.tinygroup.jsqlparser.statement.select.PlainSelect;

/**
 * A column definition in a CREATE TABLE statement.<br>
 * Example: mycol VARCHAR(30) NOT NULL
 */
public class ColumnDefinition {

	private String columnName;
	private ColDataType colDataType;
	private List<String> columnSpecStrings;

	/**
	 * A list of strings of every word after the datatype of the column.<br>
	 * Example ("NOT", "NULL")
	 */
	public List<String> getColumnSpecStrings() {
		return columnSpecStrings;
	}

	public void setColumnSpecStrings(List<String> list) {
		columnSpecStrings = list;
	}

	/**
	 * The {@link ColDataType} of this column definition
	 */
	public ColDataType getColDataType() {
		return colDataType;
	}

	public void setColDataType(ColDataType type) {
		colDataType = type;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String string) {
		columnName = string;
	}


	public String toString() {
		return columnName + " " + colDataType + (columnSpecStrings != null ? " " + PlainSelect.getStringList(columnSpecStrings, false, false) : "");
	}
}
