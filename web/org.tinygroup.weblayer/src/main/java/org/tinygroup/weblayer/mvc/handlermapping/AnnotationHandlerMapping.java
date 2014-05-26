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
package org.tinygroup.weblayer.mvc.handlermapping;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.tinygroup.commons.tools.AnnotationUtils;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.weblayer.mvc.HandlerExecutionChain;
import org.tinygroup.weblayer.mvc.MappingClassModel;
import org.tinygroup.weblayer.mvc.MappingMethodModel;
import org.tinygroup.weblayer.mvc.annotation.RequestMapping;
import org.tinygroup.weblayer.mvc.annotation.View;
import org.tinygroup.weblayer.util.TinyPathMatcher;

/**
 * 
 * 功能说明: 注解方式处理类
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-4-22 <br>
 * <br>
 */
public class AnnotationHandlerMapping extends AbstractHandlerMapping {
	private TinyPathMatcher pathMatcher=new TinyPathMatcher();
	private static final Logger logger = LoggerFactory
			.getLogger(AnnotationHandlerMapping.class);
	
	public void init() {
		super.init();
		Set<Class> mappingClassModels = getManager().getMappingClasses();
		logger.logMessage(LogLevel.INFO, "tiny-mvc 开始初始化操作");
		for (Class clazz : mappingClassModels) {
			MappingClassModel model = getManager().getMappingModelWithClass(
					clazz);
			RequestMapping mapping = model.getClassMapping();
			Set<String> urls = new LinkedHashSet<String>();
			if (mapping != null) {
				String[] typeLevelPatterns = mapping.value();
				if (typeLevelPatterns.length > 0) {
					// @RequestMapping specifies paths at type level
					String[] methodLevelPatterns = determineUrlsForHandlerMethods(
							model, true);
					for (String typeLevelPattern : typeLevelPatterns) {
						if (!typeLevelPattern.startsWith("/")) {
							typeLevelPattern = "/" + typeLevelPattern;
						}
						for (String methodLevelPattern : methodLevelPatterns) {
							MappingMethodModel methodModel = model
									.removeMappingMethodWithUrlPattern(methodLevelPattern);
							String combinedPattern = pathMatcher.combine(typeLevelPattern,
											methodLevelPattern);
							model.putUrlMapping(combinedPattern, methodModel);
							getManager().putUrlMapping(combinedPattern, model);
						}
					}
				} else {
					// actual paths specified by @RequestMapping at method level
					determineUrlsForHandlerMethods(model, false);
				}
			} else {
				determineUrlsForHandlerMethods(model, false);
			}
		}
		logger.logMessage(LogLevel.INFO, "tiny-mvc 初始化操作结束");
	}

	public HandlerExecutionChain getHandler(String requestUrl)
			throws Exception {
             return   getManager().getMappingModelWithUrl(requestUrl);
	}

	private String[] determineUrlsForHandlerMethods(MappingClassModel model,
			final boolean hasTypeLevelMapping) {
		final Set<String> urls = new LinkedHashSet<String>();
		List<MappingMethodModel> methodModels = model.getMappingMethodModels();
		for (MappingMethodModel methodModel : methodModels) {
			final Set<String> urlPatterns = new LinkedHashSet<String>();
			RequestMapping mapping = methodModel.getMethodMapping();
			if (mapping != null) {
				String[] mappedPatterns = mapping.value();
				if (mappedPatterns.length > 0) {
					for (String mappedPattern : mappedPatterns) {
						if (!hasTypeLevelMapping
								&& !mappedPattern.startsWith("/")) {
							mappedPattern = "/" + mappedPattern;
						}
						addUrlsForPath(urlPatterns, mappedPattern);
						addUrlsForPath(urls, mappedPattern);
					}
				} else if (hasTypeLevelMapping) {
					// empty method-level RequestMapping
					urls.add(null);
					urlPatterns.add(null);
				}
			}
			model.putUrlMappings(urlPatterns, methodModel);
			getManager().putUrlMappings(urlPatterns, model);
			Method method=methodModel.getMapMethod();
			View view= AnnotationUtils.findAnnotation(method, View.class);
			if(view!=null){
				methodModel.setView(view);
			}

		}
		return StringUtil.toStringArray(urls);

	}

	/**
	 * Add URLs and/or URL patterns for the given path.
	 * 
	 * @param urls
	 *            the Set of URLs for the current bean
	 * @param path
	 *            the currently introspected path
	 */
	private void addUrlsForPath(Set<String> urls, String path) {
		urls.add(path);
//		if (path.indexOf('.') == -1 && !path.endsWith("/")) {
//			urls.add(path + ".*");
//			urls.add(path + "/");
//		}
	}

}
