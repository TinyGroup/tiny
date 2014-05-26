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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.tinygroup.validate.ValidateResult;
import org.tinygroup.validate.impl.AbstractValidator;

public class PatternValidator extends AbstractValidator {
	private static final String PATTERN_VALIDATOR_MESSAGE_KEY = "pattern_validator_message_key";
	private static final String DEFAULT_MESSAGE = "{0}:{1}的值[{2}]与要求的格式[{3}]不匹配。";

	private String pattern;
	private static Map<String, Pattern> patternMap = new HashMap<String, Pattern>();

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public <T> void validate(String name, String title, T value,
			ValidateResult validateResult) {
		if (value != null) {
			Pattern thePattern = patternMap.get(pattern);
			if (thePattern == null) {
				thePattern = Pattern.compile(pattern);
				patternMap.put(pattern, thePattern);
			}
			String stringValue = value.toString();
			Matcher matcher = thePattern.matcher(stringValue);
			if (!matcher.find()
					|| matcher.group().length() != stringValue.length()) {
				validateResult.addError(name, i18nMessages.getMessage(
						PATTERN_VALIDATOR_MESSAGE_KEY, DEFAULT_MESSAGE, name,
						title, value, pattern));
			}
		}
	}

}
