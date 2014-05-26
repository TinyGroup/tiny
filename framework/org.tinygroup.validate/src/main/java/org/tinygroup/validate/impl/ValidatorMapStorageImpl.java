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
package org.tinygroup.validate.impl;

import java.util.HashMap;
import java.util.Map;

import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.validate.Validator;
import org.tinygroup.validate.ValidatorMapStorage;
import org.tinygroup.validate.config.Validators;

/**
 * 校验映射信息存储接口的实现
 * @author renhui
 *
 */
public class ValidatorMapStorageImpl implements ValidatorMapStorage {
	
	private Map<String, Validator> validateMap = new HashMap<String, Validator>();
	

	public void addValidators(Validators validators) {

		for (org.tinygroup.validate.config.Validator validator : validators
				.getValidatorList()) {
			validateMap.put(validator.getAnnotaionClassName(),
					(Validator) SpringUtil.getBean(validator
							.getValidatorClassName()));
		}
	}
	
	public void removeValidators(Validators validators) {
		for (org.tinygroup.validate.config.Validator validator : validators
				.getValidatorList()) {
			validateMap.remove(validator.getAnnotaionClassName());
		}
	}

	public Validator getValidator(String mapName) {
		return validateMap.get(mapName);
	}

}
