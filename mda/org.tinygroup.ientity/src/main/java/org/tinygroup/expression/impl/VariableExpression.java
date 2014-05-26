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

import org.tinygroup.commons.tools.StringUtil;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 
 * 
 * 功能说明: 变量表达式

 * 开发人员: renhui <br>
 * 开发时间: 2013-11-14 <br>
 * <br>
 */
@XStreamAlias("variable-expression")
public class VariableExpression extends AbstractSqlExpression {
    @XStreamAlias("variable-name")
    @XStreamAsAttribute
	private String variableName;
	
	public VariableExpression() {
		super();
	}

	public VariableExpression(String variableName) {
		this.variableName = variableName;
	}

	public String interpret() {
		if(!StringUtil.isBlank(getTableName())){
			return getTableName()+"."+variableName;
		}
		return variableName;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	
}
