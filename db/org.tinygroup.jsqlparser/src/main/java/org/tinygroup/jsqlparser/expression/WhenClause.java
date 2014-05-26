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
 * A clause of following syntax: WHEN condition THEN expression. Which is part
 * of a CaseExpression.
 *
 * @author Havard Rast Blok
 */
public class WhenClause implements Expression {

	private Expression whenExpression;
	private Expression thenExpression;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.tinygroup.jsqlparser.expression.Expression#accept(org.tinygroup.jsqlparser.expression.ExpressionVisitor)
	 */

	public void accept(ExpressionVisitor expressionVisitor) {
		expressionVisitor.visit(this);
	}

	/**
	 * @return Returns the thenExpression.
	 */
	public Expression getThenExpression() {
		return thenExpression;
	}

	/**
	 * @param thenExpression The thenExpression to set.
	 */
	public void setThenExpression(Expression thenExpression) {
		this.thenExpression = thenExpression;
	}

	/**
	 * @return Returns the whenExpression.
	 */
	public Expression getWhenExpression() {
		return whenExpression;
	}

	/**
	 * @param whenExpression The whenExpression to set.
	 */
	public void setWhenExpression(Expression whenExpression) {
		this.whenExpression = whenExpression;
	}


	public String toString() {
		return "WHEN " + whenExpression + " THEN " + thenExpression;
	}
}
