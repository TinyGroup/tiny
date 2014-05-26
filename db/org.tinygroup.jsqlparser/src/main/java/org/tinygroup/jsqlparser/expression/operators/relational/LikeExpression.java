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

import org.tinygroup.jsqlparser.expression.BinaryExpression;
import org.tinygroup.jsqlparser.expression.ExpressionVisitor;

public class LikeExpression extends BinaryExpression {

	private boolean not = false;
	private String escape = null;


	public boolean isNot() {
		return not;
	}

	public void setNot(boolean b) {
		not = b;
	}


	public void accept(ExpressionVisitor expressionVisitor) {
		expressionVisitor.visit(this);
	}


	public String getStringExpression() {
		return ((not) ? "NOT " : "") + "LIKE";
	}


	public String toString() {
		String retval = super.toString();
		if (escape != null) {
			retval += " ESCAPE " + "'" + escape + "'";
		}

		return retval;
	}

	public String getEscape() {
		return escape;
	}

	public void setEscape(String escape) {
		this.escape = escape;
	}
}
