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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.expression.ExpressionConfigs;
import org.tinygroup.expression.ExpressionManager;
import org.tinygroup.expression.SqlExpression;

/**
 * 
 * 功能说明:表达式管理器接口实现 

 * 开发人员: renhui <br>
 * 开发时间: 2013-11-14 <br>
 * <br>
 */
public class ExpressionManagerImpl implements ExpressionManager {
	
	private Map<String, SqlExpression> expressionMap=new HashMap<String, SqlExpression>();

	public void addExpressions(ExpressionConfigs expressions) {

		List<SqlExpression> expressionList=expressions.getExpressions();
		for (SqlExpression expression : expressionList) {
			expressionMap.put(expression.getExpressionId(), expression);
		}
		
		
	}
	
	public void removeExpressions(ExpressionConfigs expressions) {

		List<SqlExpression> expressionList=expressions.getExpressions();
		for (SqlExpression expression : expressionList) {
			expressionMap.remove(expression.getExpressionId());
		}
		
		
	}

	public SqlExpression getExpression(String expressionId) {
		return expressionMap.get(expressionId);
	}

}
