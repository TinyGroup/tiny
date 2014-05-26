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
package org.tinygroup.dao.query;

import java.util.ArrayList;
import java.util.List;

public class QueryCondition {
	private List<QueryCondition> conditions;
	private String key;
	private Object value;
	
	private QueryOperator operator;

	public List<QueryCondition> getConditions() {
		if(conditions == null)
			conditions = new ArrayList<QueryCondition>();
		return conditions;
	}

	public void setConditions(List<QueryCondition> conditions) {
		this.conditions = conditions;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public QueryOperator getOperator() {
		return operator;
	}

	public void setOperator(QueryOperator operator) {
		this.operator = operator;
	}

}
