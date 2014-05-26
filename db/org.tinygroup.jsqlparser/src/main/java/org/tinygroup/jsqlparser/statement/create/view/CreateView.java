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
package org.tinygroup.jsqlparser.statement.create.view;

import java.util.List;
import org.tinygroup.jsqlparser.schema.Table;
import org.tinygroup.jsqlparser.statement.Statement;
import org.tinygroup.jsqlparser.statement.StatementVisitor;
import org.tinygroup.jsqlparser.statement.select.PlainSelect;
import org.tinygroup.jsqlparser.statement.select.SelectBody;

/**
 * A "CREATE VIEW" statement
 */
public class CreateView implements Statement {

	private Table view;
	private SelectBody selectBody;
	private boolean orReplace = false;
	private List<String> columnNames = null;
	private boolean materialized = false;


	public void accept(StatementVisitor statementVisitor) {
		statementVisitor.visit(this);
	}

	/**
	 * In the syntax tree, a view looks and acts just like a Table.
	 *
	 * @return The name of the view to be created.
	 */
	public Table getView() {
		return view;
	}

	public void setView(Table view) {
		this.view = view;
	}

	/**
	 * @return was "OR REPLACE" specified?
	 */
	public boolean isOrReplace() {
		return orReplace;
	}

	/**
	 * @param orReplace was "OR REPLACE" specified?
	 */
	public void setOrReplace(boolean orReplace) {
		this.orReplace = orReplace;
	}

	/**
	 * @return the SelectBody
	 */
	public SelectBody getSelectBody() {
		return selectBody;
	}

	public void setSelectBody(SelectBody selectBody) {
		this.selectBody = selectBody;
	}

	public List<String> getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}

	public boolean isMaterialized() {
		return materialized;
	}

	public void setMaterialized(boolean materialized) {
		this.materialized = materialized;
	}


	public String toString() {
		StringBuilder sql = new StringBuilder("CREATE ");
		if (isOrReplace()) {
			sql.append("OR REPLACE ");
		}
		if (isMaterialized()) {
			sql.append("MATERIALIZED ");
		}
		sql.append("VIEW ");
		sql.append(view);
		if (columnNames != null) {
			sql.append(PlainSelect.getStringList(columnNames, true, true));
		}
		sql.append(" AS ").append(selectBody);
		return sql.toString();
	}
}
