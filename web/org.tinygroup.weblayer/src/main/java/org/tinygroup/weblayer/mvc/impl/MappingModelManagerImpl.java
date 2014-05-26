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
package org.tinygroup.weblayer.mvc.impl;

import java.util.Map;
import java.util.Set;

import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.weblayer.mvc.HandlerExecutionChain;
import org.tinygroup.weblayer.mvc.MappingClassModel;
import org.tinygroup.weblayer.mvc.MappingMethodModel;
import org.tinygroup.weblayer.mvc.MappingModelManager;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

public class MappingModelManagerImpl implements MappingModelManager {
	 
	private PathMatcher pathMatcher=new AntPathMatcher();
	
	private Map<Class, MappingClassModel> classModels=CollectionUtil.createHashMap();
	
	private Map<String, MappingClassModel> urlModels=CollectionUtil.createHashMap();

	public void putMappingModel(Class clazz, MappingClassModel model) {
		classModels.put(clazz, model);
	}

	public MappingClassModel getMappingModelWithClass(Class clazz) {
		return classModels.get(clazz);
	}

	public Set<Class> getMappingClasses() {
		return classModels.keySet();
	}

	public HandlerExecutionChain getMappingModelWithUrl(String url) {
		Set<String> urlPatterns=urlModels.keySet();
		for (String urlPattern : urlPatterns) {
			if(pathMatcher.match(urlPattern, url)){
				MappingClassModel model= urlModels.get(urlPattern);
				MappingMethodModel methodModel=model.getMappingMethodWithUrlPattern(urlPattern);
				return new HandlerExecutionChain(model, methodModel);
			}
		}
		return null;
	}

	public void putUrlMappings(Set<String> urlPatterns,
			MappingClassModel model) {
		for (String urlPattern : urlPatterns) {
			putUrlMapping(urlPattern, model);
		}
		
	}

	public void putUrlMapping(String urlPattern, MappingClassModel model) {
		urlModels.put(urlPattern, model);
	}

}
