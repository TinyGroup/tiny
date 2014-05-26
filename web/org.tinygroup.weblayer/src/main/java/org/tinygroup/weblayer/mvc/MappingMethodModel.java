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
package org.tinygroup.weblayer.mvc;

import java.lang.reflect.Method;
import java.util.Set;

import org.tinygroup.weblayer.mvc.annotation.RequestMapping;
import org.tinygroup.weblayer.mvc.annotation.View;

public class MappingMethodModel {
	
	private Set<String> urlPatterns;
	
	private Method mapMethod;
	
	private RequestMapping methodMapping;
	
	private View view;

	public MappingMethodModel(Method mapMethod,
			RequestMapping methodMapping) {
		super();
		this.mapMethod = mapMethod;
		this.methodMapping = methodMapping;
	}


	public Method getMapMethod() {
		return mapMethod;
	}

	public void setMapMethod(Method mapMethod) {
		this.mapMethod = mapMethod;
	}

	public RequestMapping getMethodMapping() {
		return methodMapping;
	}

	public void setMethodMapping(RequestMapping methodMapping) {
		this.methodMapping = methodMapping;
	}


	public Set<String> getUrlPatterns() {
		return urlPatterns;
	}


	public void setUrlPatterns(Set<String> urlPatterns) {
		this.urlPatterns = urlPatterns;
	}


	public View getView() {
		return view;
	}


	public void setView(View view) {
		this.view = view;
	}
	

	
	

}
