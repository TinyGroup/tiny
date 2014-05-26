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
package org.tinygroup.validate.validator;

import org.tinygroup.validate.ValidateResult;
import org.tinygroup.validate.impl.AbstractValidator;

/**
 * 包含校验器
 * @author luoguo
 *
 */
public class EnumValidator extends AbstractValidator {
	// 用逗号分隔多个值
	private String values;
	
	private static final String ENUM_VALIDATOR_MESSAGE_KEY="enum_validator_message_key";
	
	private static final String DEFAULT_MESSAGE="{0}:{1}的值不在可选范围内。";
	

	public String getValues() {
		return values;
	}

	public void setValues(String values) {
		this.values = values;
	}

	public <T> void validate(String name, String title, T value,
			ValidateResult validateResult) {
		if (value != null && values != null) {
			String[] valueArray = values.split(",");
			String stringValue = value.toString();
			for (String str : valueArray) {
				if (str.equals(stringValue)) {
					return;
				}
			}
			validateResult.addError(name, i18nMessages.getMessage(ENUM_VALIDATOR_MESSAGE_KEY,DEFAULT_MESSAGE, name,title));
		}
	}
	
}
