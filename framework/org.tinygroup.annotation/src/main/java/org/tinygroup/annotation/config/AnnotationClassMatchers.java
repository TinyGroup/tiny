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

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 注解匹配匹配，用于定义一组注解匹配模式及匹配成功后的处理
 * 
 * @author luoguo
 * 
 */
@XStreamAlias("annotation-class-matchers")
public class AnnotationClassMatchers {
	@XStreamImplicit
	private List<AnnotationClassMatcher> annotationClassMatcherList;

	public List<AnnotationClassMatcher> getAnnotationClassMatcherList() {
		if (annotationClassMatcherList == null) {
			annotationClassMatcherList = new ArrayList<AnnotationClassMatcher>();
		}
		return annotationClassMatcherList;
	}

	public void setAnnotationClassMatcherList(
			List<AnnotationClassMatcher> annotationClassMatcherList) {
		this.annotationClassMatcherList = annotationClassMatcherList;
	}

	public void initAnnotationClassMatchers() {
		if (annotationClassMatcherList != null) {
			for (AnnotationClassMatcher annotationClassMatcher : annotationClassMatcherList) {
				annotationClassMatcher.initAnnotationTypeMatcher();
			}
		}
	}

}
