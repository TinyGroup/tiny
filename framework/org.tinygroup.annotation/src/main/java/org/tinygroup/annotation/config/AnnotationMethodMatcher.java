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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 方法注解匹配器
 * 
 * @author luoguo
 * 
 */
@XStreamAlias("annotation-method-matcher")
public class AnnotationMethodMatcher {
	@XStreamAlias("method-name")
	@XStreamAsAttribute
	private String methodName; // 要匹配的方法名

	@XStreamAlias("annotation-type")
	@XStreamAsAttribute
	private String annotationType;// 要匹配的注解类型
	@XStreamAlias("processor-beans")
	@XStreamAsAttribute
	private List<ProcessorBean> processorBeans;// 如果匹配上执行的bean名称列表

	private AnnotationTypeMatcher annotationTypeMatcher;

	public void initAnnotationTypeMatcher() {
		if (annotationTypeMatcher == null) {
			List<String> methodNames = new ArrayList<String>();
			methodNames.add(methodName);
			annotationTypeMatcher = new AnnotationTypeMatcher(methodNames,
					annotationType);
		}
	}

	public boolean isMethodMatch(String classFullPath) {
		return annotationTypeMatcher.isMatch(classFullPath);
	}

	public boolean isAnnotationTypeMatch(Annotation annotation) {
		return annotationTypeMatcher.isAnnotationTypeMatch(annotation);
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
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

}
