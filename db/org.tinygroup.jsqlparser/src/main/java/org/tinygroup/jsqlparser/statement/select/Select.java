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
package org.tinygroup.jsqlparser.statement.select;

import java.util.Iterator;
import java.util.List;

import org.tinygroup.jsqlparser.statement.Statement;
import org.tinygroup.jsqlparser.statement.StatementVisitor;

public class Select implements Statement {

	private SelectBody selectBody;
	private List<WithItem> withItemsList;


	public void accept(StatementVisitor statementVisitor) {
		statementVisitor.visit(this);
	}

	public SelectBody getSelectBody() {
		return selectBody;
	}

	public void setSelectBody(SelectBody body) {
		selectBody = body;
	}


	public String toString() {
		StringBuilder retval = new StringBuilder();
		if (withItemsList != null && !withItemsList.isEmpty()) {
			retval.append("WITH ");
			for (Iterator<WithItem> iter = withItemsList.iterator(); iter.hasNext();) {
				WithItem withItem = (WithItem) iter.next();
				retval.append(withItem);
				if (iter.hasNext()) {
					retval.append(",");
				}
				retval.append(" ");
			}
		}
		retval.append(selectBody);
		return retval.toString();
	}

	public List<WithItem> getWithItemsList() {
		return withItemsList;
	}

	public void setWithItemsList(List<WithItem> withItemsList) {
		this.withItemsList = withItemsList;
	}
}
