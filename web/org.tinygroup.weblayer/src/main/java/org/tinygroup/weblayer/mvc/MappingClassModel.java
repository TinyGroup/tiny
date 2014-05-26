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

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.weblayer.mvc.annotation.RequestMapping;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

/**
 * 
 * 功能说明:保存请求路径映射信息的实体类
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-4-23 <br>
 * <br>
 */
public class MappingClassModel {
	
	private PathMatcher pathMatcher=new AntPathMatcher();
	/**
	 * 请求路径映射的类型
	 */
	private Class mapClass;

	/**
	 * 类上注解实例
	 */
	private RequestMapping classMapping;

	private List<MappingMethodModel> mappingMethodModels;

	private Map<String, MappingMethodModel> urlMapping;
	
	private Set<String> urlPatterns;

	public MappingClassModel(Class mapClass, RequestMapping classMapping) {
		super();
		this.mapClass = mapClass;
		this.classMapping = classMapping;
		if (mappingMethodModels == null) {
			mappingMethodModels = CollectionUtil.createArrayList();
		}
		if(urlPatterns==null){
			urlPatterns=CollectionUtil.createHashSet();
		}
		if(urlMapping==null){
			urlMapping=CollectionUtil.createHashMap();
		}
	}

	public Class getMapClass() {
		return mapClass;
	}

	public void setMapClass(Class mapClass) {
		this.mapClass = mapClass;
	}

	public RequestMapping getClassMapping() {
		return classMapping;
	}

	public void setClassMapping(RequestMapping classMapping) {
		this.classMapping = classMapping;
	}

	public void addMethodModel(MappingMethodModel methodModel) {
		mappingMethodModels.add(methodModel);
	}

	public List<MappingMethodModel> getMappingMethodModels() {
		return mappingMethodModels;
	}
	
	public void putUrlMapping(String urlPattern,MappingMethodModel methodModel){
		urlPatterns.add(urlPattern);
		urlMapping.put(urlPattern, methodModel);
	}
	
	public void putUrlMappings(Set<String> urlPatterns,MappingMethodModel methodModel){
	     for (String urlPattern : urlPatterns) {
	    	 putUrlMapping(urlPattern, methodModel);
		}
	}
	
	public Map<String, MappingMethodModel> getUrlMapping(){
		return urlMapping;
	}
	
	public MappingMethodModel removeMappingMethodWithUrlPattern(String urlPattern){
		urlPatterns.remove(urlPattern);
		return  urlMapping.remove(urlPattern);
	}
	
	public MappingMethodModel getMappingMethodWithUrlPattern(String urlPattern){
		return urlMapping.get(urlPattern);
	}
	
}
