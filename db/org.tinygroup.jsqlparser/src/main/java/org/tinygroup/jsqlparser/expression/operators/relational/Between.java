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

import org.tinygroup.jsqlparser.expression.Expression;
import org.tinygroup.jsqlparser.expression.ExpressionVisitor;

/**
 * A "BETWEEN" expr1 expr2 statement
 */
public class Between implements Expression {

	private Expression leftExpression;
	private boolean not = false;
	private Expression betweenExpressionStart;
	private Expression betweenExpressionEnd;

	public Expression getBetweenExpressionEnd() {
		return betweenExpressionEnd;
	}

	public Expression getBetweenExpressionStart() {
		return betweenExpressionStart;
	}

	public Expression getLeftExpression() {
		return leftExpression;
	}

	public boolean isNot() {
		return not;
	}

	public void setBetweenExpressionEnd(Expression expression) {
		betweenExpressionEnd = expression;
	}

	public void setBetweenExpressionStart(Expression expression) {
		betweenExpressionStart = expression;
	}

	public void setLeftExpression(Expression expression) {
		leftExpression = expression;
	}

	public void setNot(boolean b) {
		not = b;
	}


	public void accept(ExpressionVisitor expressionVisitor) {
		expressionVisitor.visit(this);
	}


	public String toString() {
		return leftExpression + " " + (not ? "NOT " : "") + "BETWEEN " + betweenExpressionStart + " AND "
				+ betweenExpressionEnd;
	}
}
