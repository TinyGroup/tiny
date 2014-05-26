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
package org.tinygroup.jsqlparser.statement.drop;

import java.util.List;

import org.tinygroup.jsqlparser.statement.Statement;
import org.tinygroup.jsqlparser.statement.StatementVisitor;
import org.tinygroup.jsqlparser.statement.select.PlainSelect;

public class Drop implements Statement {

	private String type;
	private String name;
	private List<String> parameters;


	public void accept(StatementVisitor statementVisitor) {
		statementVisitor.visit(this);
	}

	public String getName() {
		return name;
	}

	public List<String> getParameters() {
		return parameters;
	}

	public String getType() {
		return type;
	}

	public void setName(String string) {
		name = string;
	}

	public void setParameters(List<String> list) {
		parameters = list;
	}

	public void setType(String string) {
		type = string;
	}


	public String toString() {
		String sql = "DROP " + type + " " + name;

		if (parameters != null && parameters.size() > 0) {
			sql += " " + PlainSelect.getStringList(parameters);
		}

		return sql;
	}
}
