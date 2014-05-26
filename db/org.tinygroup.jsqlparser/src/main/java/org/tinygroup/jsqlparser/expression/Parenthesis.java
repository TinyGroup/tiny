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
package org.tinygroup.jsqlparser.expression;

/**
 * It represents an expression like "(" expression ")"
 */
public class Parenthesis implements Expression {

	private Expression expression;
	private boolean not = false;

	public Parenthesis() {
	}

	public Parenthesis(Expression expression) {
		setExpression(expression);
	}

	public Expression getExpression() {
		return expression;
	}

	public final void setExpression(Expression expression) {
		this.expression = expression;
	}


	public void accept(ExpressionVisitor expressionVisitor) {
		expressionVisitor.visit(this);
	}

	public void setNot() {
		not = true;
	}

	public boolean isNot() {
		return not;
	}


	public String toString() {
		return (not ? "NOT " : "") + "(" + expression + ")";
	}
}
