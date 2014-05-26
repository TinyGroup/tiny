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
package org.tinygroup.imda.validate.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.context.Context;
import org.tinygroup.imda.validate.ValidateExecutor;
import org.tinygroup.imda.validate.ValidateManager;
import org.tinygroup.imda.validate.ValidateResults;
import org.tinygroup.imda.validate.ValidateRule;

public class ValidateManagerImpl implements ValidateManager {
	static Map<String, ValidateExecutor> validateExecutorMap = new HashMap<String, ValidateExecutor>();
	{
		addValidateExecutor(new AcceptValidator());
		addValidateExecutor(new DateISOValidator());
		addValidateExecutor(new DateValidator());
		addValidateExecutor(new EmailValidator());
		addValidateExecutor(new EqualToValidator());
		addValidateExecutor(new ExtensionValidator());
		addValidateExecutor(new MaxLengthValidator());
		addValidateExecutor(new MaxValidator());
		addValidateExecutor(new MinLengthValidator());
		addValidateExecutor(new MinValidator());
		addValidateExecutor(new NumberValidator());
		addValidateExecutor(new RangeLengthValidator());
		addValidateExecutor(new RangeValidator());
		addValidateExecutor(new RequiredValidator());
		addValidateExecutor(new UrlValidator());
		addValidateExecutor(new CellPhoneNumberValidator());
	}

	public void addValidateExecutor(ValidateExecutor validateExecutor) {
		validateExecutorMap.put(validateExecutor.getRuleName(),
				validateExecutor);
	}

	public boolean validateField(ValidateRule validateRule, String fieldName,
			Context context) {
		String value = context.get(fieldName);
		ValidateExecutor validateExecutor = validateExecutorMap
				.get(validateRule.getRuleName());
		if (validateExecutor != null) {// 如果找不到对应的校验器,则忽略之
			if (!validateExecutor.isValidate(validateRule, value, context)) {
				return false;
			}
		}
		return true;
	}

	public ValidateResults validate(Map<String, List<ValidateRule>> rules,
			Context context) {
		ValidateResults result = new ValidateResults();
		for (String fieldName : rules.keySet()) {
			for (ValidateRule rule : rules.get(fieldName)) {
				if (!validateField(rule, fieldName, context)) {
					result.addValidateResult(fieldName, rule);
				}
			}
		}
		return result;
	}

	public String getDefaultMessage(String ruleName) {
		ValidateExecutor executor = validateExecutorMap.get(ruleName);
		if (executor != null) {
			return executor.getDefaultMessage();
		}
		return null;
	}
}
