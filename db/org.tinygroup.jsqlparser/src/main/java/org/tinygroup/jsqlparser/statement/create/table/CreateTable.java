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

import org.tinygroup.jsqlparser.schema.Table;
import org.tinygroup.jsqlparser.statement.Statement;
import org.tinygroup.jsqlparser.statement.StatementVisitor;
import org.tinygroup.jsqlparser.statement.select.PlainSelect;

/**
 * A "CREATE TABLE" statement
 */
public class CreateTable implements Statement {

	private Table table;
	private List<String> tableOptionsStrings;
	private List<ColumnDefinition> columnDefinitions;
	private List<Index> indexes;


	public void accept(StatementVisitor statementVisitor) {
		statementVisitor.visit(this);
	}

	/**
	 * The name of the table to be created
	 */
	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	/**
	 * A list of {@link ColumnDefinition}s of this table.
	 */
	public List<ColumnDefinition> getColumnDefinitions() {
		return columnDefinitions;
	}

	public void setColumnDefinitions(List<ColumnDefinition> list) {
		columnDefinitions = list;
	}

	/**
	 * A list of options (as simple strings) of this table definition, as
	 * ("TYPE", "=", "MYISAM")
	 */
	public List<?> getTableOptionsStrings() {
		return tableOptionsStrings;
	}

	public void setTableOptionsStrings(List<String> list) {
		tableOptionsStrings = list;
	}

	/**
	 * A list of {@link Index}es (for example "PRIMARY KEY") of this table.<br>
	 * Indexes created with column definitions (as in mycol INT PRIMARY KEY) are
	 * not inserted into this list.
	 */
	public List<Index> getIndexes() {
		return indexes;
	}

	public void setIndexes(List<Index> list) {
		indexes = list;
	}


	public String toString() {
		String sql = "";

		sql = "CREATE TABLE " + table + " (";

		sql += PlainSelect.getStringList(columnDefinitions, true, false);
		if (indexes != null && indexes.size() != 0) {
			sql += ", ";
			sql += PlainSelect.getStringList(indexes);
		}
		sql += ")";
		String options = PlainSelect.getStringList(tableOptionsStrings, false, false);
		if (options != null && options.length() > 0) {
			sql += " " + options;
		}

		return sql;
	}
}
