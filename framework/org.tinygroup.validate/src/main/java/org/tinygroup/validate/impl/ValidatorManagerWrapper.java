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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.validate.ValidateResult;
import org.tinygroup.validate.Validator;

/**
 * 校验管理器的包装类,主要是存储需要校验的类的名称
 * 
 * @author renhui
 * 
 */
public class ValidatorManagerWrapper {

	protected static Logger logger = LoggerFactory
			.getLogger(AnnotationValidatorManagerImpl.class);

	private Map<Class<?>, FieldValidatorMap> classFieldValidators = new HashMap<Class<?>, FieldValidatorMap>();
	private Map<String, List<Validator>> basicValidatorMap = new HashMap<String, List<Validator>>();

	void validator(String name, Object value, ValidateResult result) {
		if(name==null||"".equals(name)){
			logger.logMessage(LogLevel.WARN,"简单类型验证规则名不可为空" , name);
			result.addError(name, "简单类型验证规则名不可为空");
			return;
		}
		List<Validator> list = basicValidatorMap.get(name);
		if (list == null) {
			logger.logMessage(LogLevel.WARN, "不存在简单类型验证规则:{0}", name);
			result.addError(name, "不存在简单类型验证规则:"+name);
			return;
		}
		for (Validator v : list) {
			v.validate(name, "简单数据类型", value, result);
		}

	}

	void validatorObject(String className, String scene, Object value,
			ValidateResult result, List<Object> validatedObjects) {
		FieldValidatorMap validatorMap = classFieldValidators.get(value
				.getClass());
		if (validatorMap != null) {
			try {
				if (validatedObjects == null)
					validatedObjects = new ArrayList<Object>();
				validatorMap.validator(className, scene, value, result,
						validatedObjects);
			} catch (Exception e) {
				logger.errorMessage(e.getMessage(), e);
			}
		}

	}

	public void putClassFieldValidators(Class<?> clazz,
			FieldValidatorMap fieldValidatorMap) {
		classFieldValidators.put(clazz, fieldValidatorMap);
	}
	
	public void putBasicValidators(String s, Validator v) {
		if (basicValidatorMap.containsKey(s)) {
			basicValidatorMap.get(s).add(v);
		} else {
			List<Validator> list = new ArrayList<Validator>();
			list.add(v);
			basicValidatorMap.put(s, list);
		}
	}
	
	public void removeBasicValidators(String s, Validator v) {
		if (basicValidatorMap.containsKey(s)) {
			basicValidatorMap.get(s).remove(v);
		} 
	}

	public FieldValidatorMap getClassFieldValidators(Class<?> clazz) {

		return classFieldValidators.get(clazz);
	}
	
	public FieldValidatorMap removeClassFieldValidators(Class<?> clazz) {
		return classFieldValidators.remove(clazz);
	}

	String getWrapperKey(Class<?> clazz, Field field) {
		return clazz.getName() + "." + field.getName();
	}

}
