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

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.validate.ValidateResult;
import org.tinygroup.validate.Validator;
import org.tinygroup.validate.ValidatorManager;
import org.tinygroup.validate.ValidatorMapStorage;
import org.tinygroup.validate.annotation.Validation;

/**
 * 抽象的校验管理器类
 * 
 * @author renhui
 * 
 */
public abstract class AbstractValidatorManger implements ValidatorManager {

	protected static Logger logger = LoggerFactory
			.getLogger(AnnotationValidatorManagerImpl.class);

	private ValidatorMapStorage validatorMapStorage;

	private ValidatorManagerWrapper validatorManagerWrapper;

	protected Map<String, FieldWapper> fieldWrapperMap = new HashMap<String, FieldWapper>();

	public void validate(String scene, Object object, ValidateResult result) {
		if (object == null) {
			result.addError("", "待校验数据为空");
			return;
		}
		if (ClassUtil.isBasicClass(object.getClass())) {
			validatorManagerWrapper.validator(scene, object, result);
		} else {
			Class<?> clazz = object.getClass();
			String className = obtainNameFromClass(clazz);
			validatorManagerWrapper.validatorObject(className, scene, object,
					result, null);
		}

	}

	public void validate(Object object, ValidateResult result) {
		validate("", object, result);
	}

	private String obtainNameFromClass(Class<?> clazz) {
		Validation validation = (Validation) clazz
				.getAnnotation(Validation.class);
		String className = null;
		if (validation != null) {
			className = validation.name();
		}
		if (className == null || "".equals(className)) {
			className = ClassUtil.humpString(clazz);
		}

		return className;
	}

	public ValidatorMapStorage getValidatorMapStorage() {
		return validatorMapStorage;
	}

	public void setValidatorMapStorage(ValidatorMapStorage validatorMapStorage) {
		this.validatorMapStorage = validatorMapStorage;
	}

	Validator getValidator(String mapName) {
		return validatorMapStorage.getValidator(mapName);
	}

	String getWrapperKey(Class<?> clazz, Field field) {
		return clazz.getName() + "." + field.getName();
	}

	public ValidatorManagerWrapper getValidatorManagerWrapper() {
		return validatorManagerWrapper;
	}

	public void setValidatorManagerWrapper(
			ValidatorManagerWrapper validatorManagerWrapper) {
		this.validatorManagerWrapper = validatorManagerWrapper;
	}

	protected void putClassFieldValidators(Class<?> clazz,
			FieldValidatorMap fieldValidatorMap) {
		validatorManagerWrapper.putClassFieldValidators(clazz,
				fieldValidatorMap);
	}

	protected FieldValidatorMap getFieldValidatorMap(Class<?> clazz) {
		return validatorManagerWrapper.getClassFieldValidators(clazz);
	}
	
	protected FieldValidatorMap removeFieldValidatorMap(Class<?> clazz) {
		return validatorManagerWrapper.removeClassFieldValidators(clazz);
	}

	protected void putBasicValidators(String s, Validator v) {
		validatorManagerWrapper.putBasicValidators(s, v);
	}
	protected void removeBasicValidators(String s, Validator v) {
		validatorManagerWrapper.putBasicValidators(s, v);
	}
}
