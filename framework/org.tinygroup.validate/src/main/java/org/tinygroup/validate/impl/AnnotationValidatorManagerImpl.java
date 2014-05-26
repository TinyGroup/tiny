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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.validate.AnnotationValidatorManager;
import org.tinygroup.validate.Validator;

/**
 * 注解管理接口实现类
 * 
 * @author renhui
 * 
 */
public class AnnotationValidatorManagerImpl extends AbstractValidatorManger
		implements AnnotationValidatorManager {

	private static final String FIELD_ANNOTATION_CLASS_NAME = "org.tinygroup.validate.annotation.Field";

	private List<Field> hasFiledAnnotationList = new ArrayList<Field>();

	public <T> void addValidatorAnnotation(Class<T> clazz, Field field,
			Annotation annotation) {

		// 如果是Field则直接返回
		if (isFieldAnnotation(annotation)) {
			return;
		}

		logger.logMessage(LogLevel.DEBUG, "类名:[{0}],字段名:[{1}],找到校验器注解:[{2}]",
				clazz.getName(), field.getName(), annotation.annotationType()
						.getName());
		if (hasFiledAnnotationList.contains(field)) {
			FieldValidatorMap map = getFieldValidatorMap(clazz);
			if (map == null) {
				map = new FieldValidatorMap();
			}
			try {
				String wrapperKey=getWrapperKey(clazz, field);
				FieldWapper fieldWapper = fieldWrapperMap.get(wrapperKey);
				if (fieldWapper == null) {
					org.tinygroup.validate.annotation.Field fieldAnnotation = field
							.getAnnotation(org.tinygroup.validate.annotation.Field.class);
					String name = fieldAnnotation.name();
					if (name == null || "".equals(name)) {
						name = field.getName();
					}
					fieldWapper = new FieldWapper(field, name,
							fieldAnnotation.title());
					fieldWrapperMap.put(wrapperKey, fieldWapper);
				}
				map.addValidator(fieldWapper, "",
						getValidatorFromAnnotation(field, annotation));
			} catch (Exception e) {
				logger.errorMessage(e.getMessage(), e);
			}
			putClassFieldValidators(clazz, map);

		}

	}

	private boolean isFieldAnnotation(Annotation annotation) {
		return FIELD_ANNOTATION_CLASS_NAME.equals(annotation.annotationType()
				.getName());
	}

	private Validator getValidatorFromAnnotation(Field field,
			Annotation annotation) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		Validator validator = getValidator(annotation.annotationType()
				.getName());
		// 设置validator对象的值
		Method[] methods = annotation.annotationType().getDeclaredMethods();
		for (Method method : methods) {
			BeanUtils.setProperty(validator, method.getName(),
					method.invoke(annotation));
		}
		return validator;
	}

	public <T> void addFieldAnnotation(Class<T> clazz, Field field,
			Annotation annotation) {
		logger.logMessage(LogLevel.DEBUG,
				"类名:[{0}],字段名:[{1}],找到标识校验字段的注解:[{2}]", clazz.getName(),
				field.getName(), annotation.annotationType().getName());
		hasFiledAnnotationList.add(field);
	}
}
