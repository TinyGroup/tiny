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

import java.util.Set;

/**
 * 
 * 功能说明: MappingModel的管理接口

 * 开发人员: renhui <br>
 * 开发时间: 2013-4-23 <br>
 * <br>
 */
public interface MappingModelManager {

	/**
	 * 
	 * 保存控制层类Class的请求映射信息
	 * @param clazz 具有@Contoller注解的类
	 * @param model 请求映射信息
	 */
	 void putMappingModel(Class clazz,MappingClassModel model);
	
	/**
	 * 
	 * 根据Class获取请求映射信息
	 * @param clazz
	 * @return
	 */
	MappingClassModel getMappingModelWithClass(Class clazz);
	
     Set<Class> getMappingClasses();
	/**
	 * 
	 * 根据请求url获取相应的请求映射信息对象
	 * @param url
	 * @return
	 */
	 HandlerExecutionChain getMappingModelWithUrl(String url);
	 
	void putUrlMappings(Set<String> urlPatterns,MappingClassModel model);
	void putUrlMapping(String combinedPattern, MappingClassModel model); 
	
}
