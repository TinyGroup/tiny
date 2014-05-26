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
package org.tinygroup.annotation.config;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.tinygroup.annotation.impl.AnnotationClassMap;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 注解匹配器，通过注解匹配器定义了在什么情况下匹配注解，并定义了匹配成功时执行的处理器列表
 * 
 * @author luoguo
 * 
 */
@XStreamAlias("annotation-class-matcher")
public class AnnotationClassMatcher {
	@XStreamAlias("annotation-id")
	@XStreamAsAttribute
	private String annotationId;// 注解标识
	@XStreamAlias("class-name")
	@XStreamAsAttribute
	private String className;// 匹配的类名,正则表达式
	@XStreamAlias("annotation-type")
	@XStreamAsAttribute
	private String annotationType;// 注解类名,正则表式
	@XStreamAlias("processor-beans")
	@XStreamAsAttribute
	private List<ProcessorBean> processorBeans;// 如果匹配上执行的bean实例名列表，必须实现ProcessorBean接口
	@XStreamAlias("annotation-property-matchers")
	@XStreamAsAttribute
	private List<AnnotationPropertyMatcher> annotationPropertyMatchers;// 属性注解匹配器列表
	@XStreamAlias("annotation-method-matchers")
	@XStreamAsAttribute
	private List<AnnotationMethodMatcher> annotationMethodMatchers;// 方法注解匹配器列表

	private AnnotationTypeMatcher annotationTypeMatcher;// 注解类型匹配器

	public void initAnnotationTypeMatcher() {

		List<String> classNames = null;
		if (annotationId != null) {
			// 如果annotationId配置了就从注解配置信息中获取 类名正则表达式
			classNames = AnnotationClassMap.getClassNamesById(annotationId);
		}
		if (classNames == null) {
			classNames = new ArrayList<String>();
			classNames.add(className);
		}
		if (annotationTypeMatcher == null)
			annotationTypeMatcher = new AnnotationTypeMatcher(classNames,
					annotationType);
		if (annotationPropertyMatchers != null) {
			for (AnnotationPropertyMatcher annotationPropertyMatcher : annotationPropertyMatchers) {
				annotationPropertyMatcher.initAnnotationTypeMatcher();
			}
		}
		if (annotationMethodMatchers != null) {
			for (AnnotationMethodMatcher annotationMethodMatcher : annotationMethodMatchers) {
				annotationMethodMatcher.initAnnotationTypeMatcher();
			}
		}
	}

	public boolean isClassMatch(String classFullPath) {
		return annotationTypeMatcher.isMatch(classFullPath);
	}

	public boolean isAnnotationTypeMatch(Annotation annotation) {
		return annotationTypeMatcher.isAnnotationTypeMatch(annotation);
	}

	public String getAnnotationId() {
		return annotationId;
	}

	public void setAnnotationId(String annotationId) {
		this.annotationId = annotationId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getAnnotationType() {
		return annotationType;
	}

	public void setAnnotationType(String annotationType) {
		this.annotationType = annotationType;
	}

	public List<ProcessorBean> getProcessorBeans() {
		if (processorBeans == null) {
			processorBeans = new ArrayList<ProcessorBean>();
		}
		return processorBeans;
	}

	public void setProcessorBeans(List<ProcessorBean> processorBeans) {
		this.processorBeans = processorBeans;
	}

	public List<AnnotationPropertyMatcher> getAnnotationPropertyMatchers() {
		if (annotationPropertyMatchers == null) {
			annotationPropertyMatchers = new ArrayList<AnnotationPropertyMatcher>();
		}
		return annotationPropertyMatchers;
	}

	public void setAnnotationPropertyMatchers(
			List<AnnotationPropertyMatcher> annotationPropertyMatchers) {
		this.annotationPropertyMatchers = annotationPropertyMatchers;
	}

	public List<AnnotationMethodMatcher> getAnnotationMethodMatchers() {
		if (annotationMethodMatchers == null) {
			annotationMethodMatchers = new ArrayList<AnnotationMethodMatcher>();
		}
		return annotationMethodMatchers;
	}

	public void setAnnotationMethodMatchers(
			List<AnnotationMethodMatcher> annotationMethodMatchers) {
		this.annotationMethodMatchers = annotationMethodMatchers;
	}

}
