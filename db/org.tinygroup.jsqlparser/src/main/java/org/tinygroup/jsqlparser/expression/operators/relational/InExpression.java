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

public class InExpression implements Expression, SupportsOldOracleJoinSyntax {

	private Expression leftExpression;
	private ItemsList leftItemsList;
	private ItemsList rightItemsList;
	private boolean not = false;

    private int oldOracleJoinSyntax = NO_ORACLE_JOIN;

	public InExpression() {
	}

	public InExpression(Expression leftExpression, ItemsList itemsList) {
		setLeftExpression(leftExpression);
		setRightItemsList(itemsList);
	}


    public void setOldOracleJoinSyntax(int oldOracleJoinSyntax) {
        this.oldOracleJoinSyntax = oldOracleJoinSyntax;
        if (oldOracleJoinSyntax < 0 || oldOracleJoinSyntax > 1) {
            throw new IllegalArgumentException("unexpected join type for oracle found with IN (type=" + oldOracleJoinSyntax + ")");
        }
    }


    public int getOldOracleJoinSyntax() {
        return oldOracleJoinSyntax;
    }

	public ItemsList getRightItemsList() {
		return rightItemsList;
	}

	public Expression getLeftExpression() {
		return leftExpression;
	}

	public final void setRightItemsList(ItemsList list) {
		rightItemsList = list;
	}

	public final void setLeftExpression(Expression expression) {
		leftExpression = expression;
	}

	public boolean isNot() {
		return not;
	}

	public void setNot(boolean b) {
		not = b;
	}

	public ItemsList getLeftItemsList() {
		return leftItemsList;
	}

	public void setLeftItemsList(ItemsList leftItemsList) {
		this.leftItemsList = leftItemsList;
	}


	public void accept(ExpressionVisitor expressionVisitor) {
		expressionVisitor.visit(this);
	}

    private String getLeftExpressionString() {
        return leftExpression + (oldOracleJoinSyntax == ORACLE_JOIN_RIGHT ? "(+)" : "");
    }


	public String toString() {
		return (leftExpression == null ? leftItemsList : getLeftExpressionString()) + " " + ((not) ? "NOT " : "") + "IN " + rightItemsList + "";
	}


	public int getOraclePriorPosition() {
		return SupportsOldOracleJoinSyntax.NO_ORACLE_PRIOR;
	}


	public void setOraclePriorPosition(int priorPosition) {
        if (priorPosition != SupportsOldOracleJoinSyntax.NO_ORACLE_PRIOR) {
            throw new IllegalArgumentException("unexpected prior for oracle found");
        }
	}
}
