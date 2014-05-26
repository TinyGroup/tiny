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

import org.tinygroup.jsqlparser.statement.create.table.ColDataType;

/**
 * 
 * @author tw
 */
public class CastExpression implements Expression {

	private Expression leftExpression;
	private ColDataType type;
	private boolean useCastKeyword = true;

	public ColDataType getType() {
		return type;
	}

	public void setType(ColDataType type) {
		this.type = type;
	}

	public Expression getLeftExpression() {
		return leftExpression;
	}

	public void setLeftExpression(Expression expression) {
		leftExpression = expression;
	}


	public void accept(ExpressionVisitor expressionVisitor) {
		expressionVisitor.visit(this);
	}

	public boolean isUseCastKeyword() {
		return useCastKeyword;
	}

	public void setUseCastKeyword(boolean useCastKeyword) {
		this.useCastKeyword = useCastKeyword;
	}


	public String toString() {
		if (useCastKeyword) {
			return "CAST(" + leftExpression + " AS " + type.toString() + ")";
		} else {
			return leftExpression + "::" + type.toString();
		}
	}
}
