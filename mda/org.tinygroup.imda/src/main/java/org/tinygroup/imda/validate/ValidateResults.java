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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidateResults {
	Map<String, List<ValidateRule>> validateResultMap = new HashMap<String, List<ValidateRule>>();

	public Map<String, List<ValidateRule>> getValidateResultMap() {
		return validateResultMap;
	}

	public void addValidateResult(String fieldName, ValidateRule validateRule) {
		List<ValidateRule> ruleList = validateResultMap.get(fieldName);
		if (ruleList == null) {
			ruleList = new ArrayList<ValidateRule>();
			validateResultMap.put(fieldName, ruleList);
		}
		ruleList.add(validateRule);
	}
}
