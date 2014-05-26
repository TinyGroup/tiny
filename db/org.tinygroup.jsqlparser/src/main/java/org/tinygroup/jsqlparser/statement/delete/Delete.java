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
package org.tinygroup.jsqlparser.statement.delete;

import org.tinygroup.jsqlparser.expression.Expression;
import org.tinygroup.jsqlparser.schema.Table;
import org.tinygroup.jsqlparser.statement.Statement;
import org.tinygroup.jsqlparser.statement.StatementVisitor;

public class Delete implements Statement {

	private Table table;
	private Expression where;


	public void accept(StatementVisitor statementVisitor) {
		statementVisitor.visit(this);
	}

	public Table getTable() {
		return table;
	}

	public Expression getWhere() {
		return where;
	}

	public void setTable(Table name) {
		table = name;
	}

	public void setWhere(Expression expression) {
		where = expression;
	}


	public String toString() {
		return "DELETE FROM " + table + ((where != null) ? " WHERE " + where : "");
	}
}
