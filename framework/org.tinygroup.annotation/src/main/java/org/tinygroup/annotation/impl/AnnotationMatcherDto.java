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
package org.tinygroup.annotation.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.tinygroup.annotation.config.AnnotationClassMatcher;
import org.tinygroup.annotation.config.AnnotationMethodMatcher;
import org.tinygroup.annotation.config.AnnotationPropertyMatcher;



/**
 * 保存注解配置信息的中间类
 * @author renhui
 *
 */
public class AnnotationMatcherDto {
	
	private AnnotationClassMatcher annotationClassMatcher;
	
	private AnnotationMethodMatcher annotationMethodMatcher;
	
	private AnnotationPropertyMatcher annotationPropertyMatcher;
	
	private Method method;
	
	private Field field;
	
	 AnnotationMatcherDto(AnnotationClassMatcher annotationClassMatcher){
		this.annotationClassMatcher=annotationClassMatcher;
	}

	 AnnotationMatcherDto(
			AnnotationMethodMatcher annotationMethodMatcher, Method method) {
		super();
		this.annotationMethodMatcher = annotationMethodMatcher;
		this.method = method;
	}

	 AnnotationMatcherDto(
			AnnotationPropertyMatcher annotationPropertyMatcher, Field field) {
		super();
		this.annotationPropertyMatcher = annotationPropertyMatcher;
		this.field = field;
	}

	 AnnotationClassMatcher getAnnotationClassMatcher() {
		return annotationClassMatcher;
	}

	 AnnotationMethodMatcher getAnnotationMethodMatcher() {
		return annotationMethodMatcher;
	}

	 AnnotationPropertyMatcher getAnnotationPropertyMatcher() {
		return annotationPropertyMatcher;
	}

	 Method getMethod() {
		return method;
	}

	 Field getField() {
		return field;
	}
	
	
	
	

}
