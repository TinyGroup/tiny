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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.tinygroup.i18n.I18nMessageFactory;
import org.tinygroup.i18n.I18nMessages;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.validate.ValidateResult;
import org.tinygroup.validate.Validator;
import org.tinygroup.validate.ValidatorManager;

/**
 * 字段与对应的校验器映射类
 * 
 * @author renhui
 * 
 */
public class FieldValidatorMap {

	private ValidatorManagerWrapper validatorManagerWrapper;

	private I18nMessages i18nMessages = I18nMessageFactory.getI18nMessages();

	public static final String TINY_DEFAULT_SCENE = "tiny_default";

	public FieldValidatorMap() {
		validatorManagerWrapper = SpringUtil
				.getBean(ValidatorManagerWrapper.class);
	}

	private Map<FieldWapper, Map<String, List<Validator>>> fieldSceneValidatorsMap = new HashMap<FieldWapper, Map<String, List<Validator>>>();

	void addValidator(FieldWapper field, String scene, Validator validator) {
		if (scene == null || "".equals(scene)) {
			scene = TINY_DEFAULT_SCENE;
		}
		Map<String, List<Validator>> sceneMap = fieldSceneValidatorsMap
				.get(field);
		if (sceneMap == null) {
			sceneMap = new HashMap<String, List<Validator>>();
			fieldSceneValidatorsMap.put(field, sceneMap);
		}
		List<Validator> validators = sceneMap.get(scene);
		if (validators == null) {
			validators = new ArrayList<Validator>();
			sceneMap.put(scene, validators);
		}
		if (validator != null) {// 20130605
								// 为了支持对象嵌套而允许validator为空，使得可以添加对象的FieldWapper来占位，用于后面的嵌套调用
			validators.add(validator);
		}
	}

	void validator(String className, String scene, Object value,
			ValidateResult result, List<Object> validatedObjects)
			throws IllegalArgumentException, IllegalAccessException {

		for (Entry<FieldWapper, Map<String, List<Validator>>> entry : fieldSceneValidatorsMap
				.entrySet()) {
			FieldWapper wapper = entry.getKey();
			Field field = wapper.getField();
			field.setAccessible(true);
			String displayName = className + "." + wapper.getFieldName();
			String title = i18nMessages.getMessage(
					ValidatorManager.FIELD_TITLE_KEY, wapper.getTitle());
			Object object = field.get(value);
			if (!ClassUtil.isBasicClass((Class<?>) field.getType())) {
				if (validatedObjects.contains(object)) {
					continue;
				} else {
					validatedObjects.add(object);

				}
			}
			Map<String, List<Validator>> sceneValidatorsMap = entry.getValue();
			List<Validator> validators = sceneValidatorsMap.get(scene);// 获取scene的validator
			if (validators == null) {
				validators = sceneValidatorsMap.get(TINY_DEFAULT_SCENE);
			}
			if (validators != null) {
				for (Validator validator : validators) {
					if (validator != null) {
						validator.validate(displayName, title, object, result);
					}
				}
			}
			// 如果这个字段是非基本类型，那么从列表中获取该类的验证校验器进行校验
			if (validatorManagerWrapper != null && object != null
					&& !ClassUtil.isBasicClass((Class<?>) field.getType())) {
				// 如果是数组类型且数组元素是对象类型
				if (ClassUtil.arrayElementIsObjectType(object.getClass())) {
					validateArray(result, displayName, scene, object,
							validatedObjects);
				} else if (ClassUtil.isCollectTypes(object)) {// 如果是集合类型且集合类型中保存的类型是对象类型
					validateCollection(result, displayName, scene, object,
							validatedObjects);
				} else if (ClassUtil.isMapTypes(object)) {// 如果是映射类型且其元素值是对象类型
					validateMap(result, displayName, scene, object,
							validatedObjects);

				} else {
					validatorManagerWrapper.validatorObject(displayName, scene,
							object, result, validatedObjects);
				}

			}
		}
	}

	private void validateMap(ValidateResult result, String displayName,
			String scene, Object object, List<Object> validatedObjects) {
		Map<?, ?> map = (Map<?, ?>) object;
		for (Object key : map.keySet()) {
			Object obj = map.get(key);
			if (validatedObjects.contains(obj)) {
				continue;
			}
			String keyName = key.toString();
			if (obj != null && !ClassUtil.isBasicClass(obj.getClass())) {
				displayName = getDisplayNameWithString(keyName, displayName,
						obj.getClass());
				validatorManagerWrapper.validatorObject(displayName, scene,
						obj, result, validatedObjects);
			}
		}
	}

	private void validateCollection(ValidateResult result, String displayName,
			String scene, Object object, List<Object> validatedObjects) {
		Collection<Object> collection = (Collection<Object>) object;
		Object[] objs = collection.toArray();
		for (int i = 0; i < objs.length; i++) {
			Object obj = objs[i];
			if (validatedObjects.contains(obj)) {
				continue;
			}
			if (obj != null && !ClassUtil.isBasicClass(obj.getClass())) {
				displayName = getDisplayNameWithInteger(i, displayName,
						obj.getClass());
				validatorManagerWrapper.validatorObject(displayName, scene,
						obj, result, validatedObjects);
			}
		}
	}

	private void validateArray(ValidateResult result, String displayName,
			String scene, Object object, List<Object> validatedObjects) {
		Object[] objects = (Object[]) object;
		for (int i = 0; i < objects.length; i++) {
			Object obj = objects[i];
			if (validatedObjects.contains(obj)) {
				continue;
			}
			if (obj != null && !ClassUtil.isBasicClass(obj.getClass())) {
				displayName = getDisplayNameWithInteger(i, displayName,
						obj.getClass());
				validatorManagerWrapper.validatorObject(displayName, scene,
						obj, result, validatedObjects);
			}
		}
	}

	private <T> String getDisplayNameWithString(String keyName,
			String displayName, Class<T> clazz) {
		String name = displayName + "[" + keyName + "]";
		String simpleName = ClassUtil.humpString(clazz);
		return name + "." + simpleName;
	}

	private <T> String getDisplayNameWithInteger(int i, String displayName,
			Class<T> clazz) {
		String name = displayName + "[" + i + "]";
		String simpleName = ClassUtil.humpString(clazz);
		return name + "." + simpleName;
	}

}
