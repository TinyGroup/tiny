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
 * Every number with a point or a exponential format is a DoubleValue
 */
public class DoubleValue implements Expression {

	private double value;
	private String stringValue;

	public DoubleValue(final String value) {
		String val = value;
		if (val.charAt(0) == '+') {
			val = val.substring(1);
		}
		this.value = Double.parseDouble(val);
		this.stringValue = val;
	}


	public void accept(ExpressionVisitor expressionVisitor) {
		expressionVisitor.visit(this);
	}

	public double getValue() {
		return value;
	}

	public void setValue(double d) {
		value = d;
	}


	public String toString() {
		return stringValue;
	}
}
