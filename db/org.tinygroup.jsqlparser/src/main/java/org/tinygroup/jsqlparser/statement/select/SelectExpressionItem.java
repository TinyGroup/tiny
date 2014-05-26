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

import org.tinygroup.jsqlparser.expression.Alias;
import org.tinygroup.jsqlparser.expression.Expression;

/**
 * An expression as in "SELECT expr1 AS EXPR"
 */
public class SelectExpressionItem implements SelectItem {

	private Expression expression;
	private Alias alias;

	public SelectExpressionItem() {
	}

	public SelectExpressionItem(Expression expression) {
		this.expression = expression;
	}
	
	public Alias getAlias() {
		return alias;
	}

	public Expression getExpression() {
		return expression;
	}

	public void setAlias(Alias alias) {
		this.alias = alias;
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}


	public void accept(SelectItemVisitor selectItemVisitor) {
		selectItemVisitor.visit(this);
	}


	public String toString() {
		return expression + ((alias != null) ? alias.toString() : "");
	}
}
