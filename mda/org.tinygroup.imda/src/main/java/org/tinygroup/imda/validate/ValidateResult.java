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
package org.tinygroup.imda.validate;

import java.util.List;

/**
 * 校验结果
 * 
 * @author luoguo
 * 
 */
public class ValidateResult {
	String  fieldName;// 要校验的字段
	List<ValidateRule> failedRules;// 失败的规则

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public List<ValidateRule> getFailedRules() {
		return failedRules;
	}

	public void setFailedRules(List<ValidateRule> failedRules) {
		this.failedRules = failedRules;
	}

}
