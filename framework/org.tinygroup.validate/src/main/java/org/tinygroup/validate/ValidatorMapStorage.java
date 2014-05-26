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
package org.tinygroup.validate;

import org.tinygroup.validate.config.Validators;

/**
 * 
 * @author luoguo
 *
 */
public interface ValidatorMapStorage {

	String VALIDATOR_MAP_BEAN_NAME = "validatorMapStorage";

	/**
	 * 添加验证器
	 * @param validators
	 */
	void addValidators(Validators validators);
	
	/**
	 * 移除验证器
	 * @param validators
	 */
	void removeValidators(Validators validators);

	/**
	 * 根据映射名称找到该名称对应的校验器
	 * @param mapName 映射名称
	 * @return
	 */
	Validator getValidator(String mapName);

}
