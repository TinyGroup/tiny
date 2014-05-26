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
package org.tinygroup.expression.impl;

import org.tinygroup.expression.Expression;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * 功能说明:乘法表达式 

 * 开发人员: renhui <br>
 * 开发时间: 2013-11-14 <br>
 * <br>
 */
@XStreamAlias("multiply-expression")
public class MultiplyExpression extends BinaryExpression{
	

	public MultiplyExpression() {
		super();
	}

	public MultiplyExpression(Expression left, Expression right) {
		super(left, right);
	}

	public String interpret() {
		return getLeft().interpret()+"*"+getRight().interpret();
	}

}
