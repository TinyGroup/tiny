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

import java.util.List;

import org.tinygroup.jsqlparser.statement.select.PlainSelect;

/**
 * CASE/WHEN expression.
 *
 * Syntax:
 * <code><pre>
 * CASE
 * WHEN condition THEN expression
 * [WHEN condition THEN expression]...
 * [ELSE expression]
 * END
 * </pre></code>
 *
 * <br/>
 * or <br/>
 * <br/>
 *
 * <code><pre>
 * CASE expression
 * WHEN condition THEN expression
 * [WHEN condition THEN expression]...
 * [ELSE expression]
 * END
 * </pre></code>
 *
 * See also: https://aurora.vcu.edu/db2help/db2s0/frame3.htm#casexp
 * http://sybooks.sybase.com/onlinebooks/group-as/asg1251e /commands/
 *
 * @ebt-link;pt=5954?target=%25N%15_52628_START_RESTART_N%25
 *
 *
 * @author Havard Rast Blok
 */
public class CaseExpression implements Expression {

	private Expression switchExpression;
	private List<Expression> whenClauses;
	private Expression elseExpression;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.tinygroup.jsqlparser.expression.Expression#accept(org.tinygroup.jsqlparser.expression.ExpressionVisitor)
	 */

	public void accept(ExpressionVisitor expressionVisitor) {
		expressionVisitor.visit(this);
	}

	/**
	 * @return Returns the switchExpression.
	 */
	public Expression getSwitchExpression() {
		return switchExpression;
	}

	/**
	 * @param switchExpression The switchExpression to set.
	 */
	public void setSwitchExpression(Expression switchExpression) {
		this.switchExpression = switchExpression;
	}

	/**
	 * @return Returns the elseExpression.
	 */
	public Expression getElseExpression() {
		return elseExpression;
	}

	/**
	 * @param elseExpression The elseExpression to set.
	 */
	public void setElseExpression(Expression elseExpression) {
		this.elseExpression = elseExpression;
	}

	/**
	 * @return Returns the whenClauses.
	 */
	public List<Expression> getWhenClauses() {
		return whenClauses;
	}

	/**
	 * @param whenClauses The whenClauses to set.
	 */
	public void setWhenClauses(List<Expression> whenClauses) {
		this.whenClauses = whenClauses;
	}


	public String toString() {
		return "CASE " + ((switchExpression != null) ? switchExpression + " " : "")
				+ PlainSelect.getStringList(whenClauses, false, false) + " "
				+ ((elseExpression != null) ? "ELSE " + elseExpression + " " : "") + "END";
	}
}
