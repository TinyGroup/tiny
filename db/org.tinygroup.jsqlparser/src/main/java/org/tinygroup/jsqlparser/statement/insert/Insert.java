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
package org.tinygroup.jsqlparser.statement.insert;

import java.util.List;

import org.tinygroup.jsqlparser.expression.operators.relational.ItemsList;
import org.tinygroup.jsqlparser.schema.Column;
import org.tinygroup.jsqlparser.schema.Table;
import org.tinygroup.jsqlparser.statement.Statement;
import org.tinygroup.jsqlparser.statement.StatementVisitor;
import org.tinygroup.jsqlparser.statement.select.PlainSelect;

/**
 * The insert statement. Every column name in
 * <code>columnNames</code> matches an item in
 * <code>itemsList</code>
 */
public class Insert implements Statement {

	private Table table;
	private List<Column> columns;
	private ItemsList itemsList;
	private boolean useValues = true;


	public void accept(StatementVisitor statementVisitor) {
		statementVisitor.visit(this);
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table name) {
		table = name;
	}

	/**
	 * Get the columns (found in "INSERT INTO (col1,col2..) [...]" )
	 *
	 * @return a list of {@link org.tinygroup.jsqlparser.schema.Column}
	 */
	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> list) {
		columns = list;
	}

	/**
	 * Get the values (as VALUES (...) or SELECT)
	 *
	 * @return the values of the insert
	 */
	public ItemsList getItemsList() {
		return itemsList;
	}

	public void setItemsList(ItemsList list) {
		itemsList = list;
	}

	public boolean isUseValues() {
		return useValues;
	}

	public void setUseValues(boolean useValues) {
		this.useValues = useValues;
	}


	public String toString() {
		String sql = "";

		sql = "INSERT INTO ";
		sql += table + " ";
		sql += ((columns != null) ? PlainSelect.getStringList(columns, true, true) + " " : "");

		if (useValues) {
			sql += "VALUES " + itemsList + "";
		} else {
			sql += "" + itemsList + "";
		}

		return sql;
	}
}
