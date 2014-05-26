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

import java.text.SimpleDateFormat;

public class DateISOValidator implements ValidateExecutor {
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSSZZ");

	public String getRuleName() {
		return "dateISO";
	}

	public boolean isValidate(ValidateRule validateRule, String value, Context context) {
		if (value == null || value.length() == 0) {
			return true;
		}
		try {
			simpleDateFormat.parse(value);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public String getDefaultMessage() {
		return "请输入合法的日期 (ISO)";
	}
}
