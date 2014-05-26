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

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

import org.tinygroup.validate.ValidateResult;
import org.tinygroup.validate.impl.AbstractValidator;

public class SizeValidator extends  AbstractValidator {
	
	private static final String MIN_SIZE_VALIDATOR_MESSAGE_KEY = "min_size_validator_message_key";
	private static final String MAX_SIZE_VALIDATOR_MESSAGE_KEY = "max_size_validator_message_key";
	private static final String MIN_DEFAULT_MESSAGE = "{0}:{1}的值为{2}其长度不能小于{3}。";
	private static final String MAX_DEFAULT_MESSAGE = "{0}:{1}的值为{2}其长度不能大于{3}。";
	private Integer min, max;

	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public <T> void validate(String name, String title, T value,
			ValidateResult validateResult) {
		if (value != null) {
			if (value instanceof String) {
				String stringValue = value.toString();
				if (min != null && stringValue.length() < min) {
					validateResult.addError(name, i18nMessages.getMessage(MIN_SIZE_VALIDATOR_MESSAGE_KEY,MIN_DEFAULT_MESSAGE,name, title,value,min));
				}
				if (max != null && stringValue.length() > max) {
					validateResult.addError(name, i18nMessages.getMessage(MAX_SIZE_VALIDATOR_MESSAGE_KEY,MAX_DEFAULT_MESSAGE,name, title,value,max));
				}
			}
			if (value instanceof Collection) {
				Collection<?> collection = (Collection<?>) value;
				if (min != null && collection.size() < min) {
					validateResult.addError(name, i18nMessages.getMessage(MIN_SIZE_VALIDATOR_MESSAGE_KEY,MIN_DEFAULT_MESSAGE,name, title,value,min));
				}
				if (max != null && collection.size() > max) {
					validateResult.addError(name, i18nMessages.getMessage(MAX_SIZE_VALIDATOR_MESSAGE_KEY,MAX_DEFAULT_MESSAGE,name, title,value,max));
				}
			}
			if (value instanceof Array) {
				Object[] array = (Object[]) value;
				if (min != null && array.length < min) {
					validateResult.addError(name, i18nMessages.getMessage(MIN_SIZE_VALIDATOR_MESSAGE_KEY,MIN_DEFAULT_MESSAGE,name, title,value,min));
				}
				if (max != null && array.length > max) {
					validateResult.addError(name, i18nMessages.getMessage(MAX_SIZE_VALIDATOR_MESSAGE_KEY,MAX_DEFAULT_MESSAGE,name, title,value,max));
				}
			}
			if (value instanceof Map) {
				Map<?, ?> map = (Map<?, ?>) value;
				if (min != null && map.size() < min) {
					validateResult.addError(name, i18nMessages.getMessage(MIN_SIZE_VALIDATOR_MESSAGE_KEY,MIN_DEFAULT_MESSAGE,name, title,value,min));
				}
				if (max != null && map.size() > max) {
					validateResult.addError(name, i18nMessages.getMessage(MAX_SIZE_VALIDATOR_MESSAGE_KEY,MAX_DEFAULT_MESSAGE,name, title,value,max));
				}
			}
		}
	}

}
