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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * annotation风格的校验管理器
 * @author renhui
 *
 */
public interface AnnotationValidatorManager extends ValidatorManager {
	
	String VALIDATOR_MANAGER_BEAN_NAME = "annotationValidatorManager";
	/**
	 * 根据用户注解，添加用户需要进行校验的类及属性上的校验规则
	 * @param <T>
	 * @param clazz 用户需要校验的类
	 * @param field 用户需要校验的字段
	 * @param annotation 校验的注解
	 */
	<T> void addValidatorAnnotation(Class<T> clazz, Field field,
			Annotation annotation);

	/**
	 * 如果用户需要进行校验的类中的属性含有@Field注解，则执行此方法，进行注册
	 * @param <T>
	 * @param clazz 用户需要校验的类
	 * @param field 用户需要校验的字段
	 * @param annotation
	 */
	<T> void addFieldAnnotation(Class<T> clazz, Field field,
			Annotation annotation);
}
