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

import java.sql.Timestamp;

/**
 * A Timestamp in the form {ts 'yyyy-mm-dd hh:mm:ss.f . . .'}
 */
public class TimestampValue implements Expression {

	private Timestamp value;

	public TimestampValue(String value) {
		this.value = Timestamp.valueOf(value.substring(1, value.length() - 1));
	}


	public void accept(ExpressionVisitor expressionVisitor) {
		expressionVisitor.visit(this);
	}

	public Timestamp getValue() {
		return value;
	}

	public void setValue(Timestamp d) {
		value = d;
	}


	public String toString() {
		return "{ts '" + value + "'}";
	}
}
