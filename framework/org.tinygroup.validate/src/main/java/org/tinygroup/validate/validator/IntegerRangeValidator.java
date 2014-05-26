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

public class IntegerRangeValidator extends AbstractValidator {
	private static final String MIN_RANGE_VALIDATOR_MESSAGE_KEY = "min_range_validator_message_key";
	private static final String MAX_RANGE_VALIDATOR_MESSAGE_KEY = "max_range_validator_message_key";
	private static final String MIN_DEFAULT_MESSAGE = "{0}:{1}的大小为{2}不能小于{3}。";
	private static final String MAX_DEFAULT_MESSAGE = "{0}:{1}的大小为{2}不能大于{3}。";
	
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

	public <R> void validate(String name, String title, R value,
			ValidateResult validateResult) {
		if (value != null) {
			
			if (min != null && (Integer)value < min) {
				validateResult.addError(name, i18nMessages.getMessage(MIN_RANGE_VALIDATOR_MESSAGE_KEY,MIN_DEFAULT_MESSAGE,name, title,value,min));
			}
			if (max != null && (Integer)value > max) {
				validateResult.addError(name, i18nMessages.getMessage(MAX_RANGE_VALIDATOR_MESSAGE_KEY,MAX_DEFAULT_MESSAGE,name, title,value,max));
			}
		}
	}
}
