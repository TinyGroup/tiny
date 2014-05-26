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
 * true校验器
 * 
 * @author renhui
 * 
 */
public class TrueValidator extends AbstractValidator {

	private static final String TRUE_VALIDATOR_MESSAGE_KEY = "true_validator_message_key";
	private static final String DEFAULT_MESSAGE = "{0}:{1}的值不为true。";

	public <T> void validate(String name, String title, T value,
			ValidateResult validateResult) {
		if ( value instanceof Boolean
				&& !Boolean.getBoolean(value.toString())) {
			validateResult.addError(name, i18nMessages.getMessage(
					TRUE_VALIDATOR_MESSAGE_KEY, DEFAULT_MESSAGE, name, title));
		}

	}
}
