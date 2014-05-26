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

import org.tinygroup.jsqlparser.expression.operators.relational.ExpressionList;

/**
 * A function as MAX,COUNT...
 */
public class Function implements Expression {

	private String name;
	private ExpressionList parameters;
	private boolean allColumns = false;
	private boolean distinct = false;
	private boolean isEscaped = false;


	public void accept(ExpressionVisitor expressionVisitor) {
		expressionVisitor.visit(this);
	}

	/**
	 * The name of he function, i.e. "MAX"
	 *
	 * @return the name of he function
	 */
	public String getName() {
		return name;
	}

	public void setName(String string) {
		name = string;
	}

	/**
	 * true if the parameter to the function is "*"
	 *
	 * @return true if the parameter to the function is "*"
	 */
	public boolean isAllColumns() {
		return allColumns;
	}

	public void setAllColumns(boolean b) {
		allColumns = b;
	}

	/**
	 * true if the function is "distinct"
	 *
	 * @return true if the function is "distinct"
	 */
	public boolean isDistinct() {
		return distinct;
	}

	public void setDistinct(boolean b) {
		distinct = b;
	}

	/**
	 * The list of parameters of the function (if any, else null) If the
	 * parameter is "*", allColumns is set to true
	 *
	 * @return the list of parameters of the function (if any, else null)
	 */
	public ExpressionList getParameters() {
		return parameters;
	}

	public void setParameters(ExpressionList list) {
		parameters = list;
	}

	/**
	 * Return true if it's in the form "{fn function_body() }"
	 *
	 * @return true if it's java-escaped
	 */
	public boolean isEscaped() {
		return isEscaped;
	}

	public void setEscaped(boolean isEscaped) {
		this.isEscaped = isEscaped;
	}


	public String toString() {
		String params;

		if (parameters != null) {
			params = parameters.toString();
			if (isDistinct()) {
				params = params.replaceFirst("\\(", "(DISTINCT ");
			} else if (isAllColumns()) {
                params = params.replaceFirst("\\(", "(ALL ");
            }
		} else if (isAllColumns()) {
			params = "(*)";
		} else {
			params = "()";
		}

		String ans = name + "" + params + "";

		if (isEscaped) {
			ans = "{fn " + ans + "}";
		}

		return ans;
	}
}
