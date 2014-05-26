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

import org.tinygroup.context.Context;
import org.tinygroup.imda.validate.ValidateExecutor;
import org.tinygroup.imda.validate.ValidateRule;

public class MaxLengthValidator implements ValidateExecutor {

	public String getRuleName() {
		return "maxlength";
	}

	public boolean isValidate(ValidateRule validateRule, String value, Context context) {
		int length = Integer.parseInt(validateRule.getRuleValue());
		if (value == null || value.length() == 0) {
			return true;
		}
		if (value.length() <= length) {
			return true;
		}
		return false;
	}

	public String getDefaultMessage() {
		return "请输入一个长度最多是 {0} 的字符串";
	}

}
