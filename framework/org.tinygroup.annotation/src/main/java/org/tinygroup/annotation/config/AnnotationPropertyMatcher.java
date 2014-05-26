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
 * 属性注解匹配器
 * 
 * @author luoguo
 * 
 */
@XStreamAlias("annotation-property-matcher")
public class AnnotationPropertyMatcher {
	@XStreamAlias("property-name")
	@XStreamAsAttribute
	private String propertyName;// 匹配的属性名
	@XStreamAlias("annotation-type")
	@XStreamAsAttribute
	private String annotationType;// 匹配的注解研
	@XStreamAlias("processor-beans")
	@XStreamAsAttribute
	private List<ProcessorBean> processorBeans;// 如果匹配上执行的ProcessorBean bean名列表

	private AnnotationTypeMatcher annotationTypeMatcher;

	public void initAnnotationTypeMatcher() {
		if (annotationTypeMatcher == null) {
			List<String> propertyNames = new ArrayList<String>();
			propertyNames.add(propertyName);
			annotationTypeMatcher = new AnnotationTypeMatcher(propertyNames,
					annotationType);
		}
	}

	public boolean isPropertyMatch(String classFullPath) {
		return annotationTypeMatcher.isMatch(classFullPath);
	}

	public boolean isAnnotationTypeMatch(Annotation annotation) {
		return annotationTypeMatcher.isAnnotationTypeMatch(annotation);
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
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
