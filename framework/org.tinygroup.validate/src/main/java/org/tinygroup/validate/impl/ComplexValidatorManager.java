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

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.validate.ValidateResult;
import org.tinygroup.validate.ValidatorManager;
import org.tinygroup.validate.ValidatorMapStorage;

/**
 * 复合的校验器管理对象
 * @author renhui
 *
 */
public class ComplexValidatorManager implements ValidatorManager {

	List<ValidatorManager> validatorManagers = new ArrayList<ValidatorManager>();

	public void validate(String scene,Object object, ValidateResult result) {
		for (ValidatorManager validatorManager : validatorManagers) {
			validatorManager.validate(scene,object, result);
		}

	}

	public List<ValidatorManager> getValidatorManagers() {
		return validatorManagers;
	}

	public void setValidatorManagers(List<ValidatorManager> validatorManagers) {
		this.validatorManagers = validatorManagers;
	}

	public void setValidatorMapStorage(ValidatorMapStorage storage) {

	}

	public void validate(Object object, ValidateResult result) {
		validate("", object, result);
		
	}

}
