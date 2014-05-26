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
import java.util.Map;

public class ValidateMapToJqueryValidateDefine {
	public String convert(Map<String, List<ValidateRule>> ruleMap) {
		StringBuffer rulesBuffer = new StringBuffer();
		StringBuffer messagesBuffer = new StringBuffer();
		for (String fieldName : ruleMap.keySet()) {
			if (rulesBuffer.length() > 0) {
				rulesBuffer.append(",");
			}
			if (messagesBuffer.length() > 0) {
				messagesBuffer.append(",");
			}
			rulesBuffer.append(fieldName).append(":");
			messagesBuffer.append(fieldName).append(":");
			List<ValidateRule> ruleList = ruleMap.get(fieldName);
			rulesBuffer.append("{");
			messagesBuffer.append("{");

			for (ValidateRule validateRule : ruleList) {
				if (validateRule != ruleList.get(0)) {
					rulesBuffer.append(",");
					messagesBuffer.append(",");
				}
				rulesBuffer.append(validateRule.ruleName).append(":");
				messagesBuffer.append(validateRule.ruleName).append(":");
				if (validateRule.getRuleValue() == null
						|| validateRule.getRuleValue().length() == 0) {
					rulesBuffer.append("true");
				} else {
					String value = validateRule.ruleValue;
					if (validateRule.ruleName.equals("equalTo")) {
						value = "#" + value;
					}
					if (validateRule.ruleName.equals("range")
							|| validateRule.ruleName.equals("rangelength")) {
						value = "[" + value + "]";
					}
					rulesBuffer.append('"').append(value).append('"');
				}
				messagesBuffer.append('"').append(validateRule.getMessege())
						.append('"');
			}
			rulesBuffer.append("}");
			messagesBuffer.append("}");
		}
		StringBuffer sb = new StringBuffer();
		sb.append("rules:").append("{").append(rulesBuffer).append("}")
				.append(",");
		sb.append("messages:").append("{").append(messagesBuffer).append("}");

		return sb.toString();
	}
}
