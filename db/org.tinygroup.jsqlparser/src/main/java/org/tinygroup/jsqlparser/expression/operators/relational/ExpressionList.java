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
package org.tinygroup.jsqlparser.expression.operators.relational;

import java.util.List;

import org.tinygroup.jsqlparser.expression.Expression;
import org.tinygroup.jsqlparser.statement.select.PlainSelect;

/**
 * A list of expressions, as in SELECT A FROM TAB WHERE B IN (expr1,expr2,expr3)
 */
public class ExpressionList implements ItemsList {

	private List<Expression> expressions;

	public ExpressionList() {
	}

	public ExpressionList(List<Expression> expressions) {
		this.expressions = expressions;
	}

	public List<Expression> getExpressions() {
		return expressions;
	}

	public void setExpressions(List<Expression> list) {
		expressions = list;
	}


	public void accept(ItemsListVisitor itemsListVisitor) {
		itemsListVisitor.visit(this);
	}


	public String toString() {
		return PlainSelect.getStringList(expressions, true, true);
	}
}
