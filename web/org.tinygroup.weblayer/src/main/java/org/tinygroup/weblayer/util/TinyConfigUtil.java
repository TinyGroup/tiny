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
package org.tinygroup.weblayer.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.tinygroup.parser.filter.NameFilter;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * 配置解析工具类
 * @author renhui
 *
 */
public final class TinyConfigUtil {
	
	private static final String URL_PATTERN = "url-pattern";

	private static final String INIT_PARAM = "init-param";

	private static final String SERVLET_MAPPING = "servlet-mapping";
	
	private static final String FILTER_MAPPING = "filter-mapping";

	public static Map<String, String> getInitParam(XmlNode xmlNode){
		Map<String, String> initParamMap = new HashMap<String, String>();
		NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(xmlNode);
		List<XmlNode> initParamNodes = nameFilter.findNodeList(INIT_PARAM);
		for (XmlNode initParamNode : initParamNodes) {
			String name = initParamNode.getAttribute("name");
			String value = initParamNode.getAttribute("value");
			initParamMap.put(name, value);
		}
		return initParamMap;
	}
	
	public static Set<String> getServletMapping(XmlNode xmlNode){
		return getUrlMapping(xmlNode,SERVLET_MAPPING);
	}
	
	public static Set<String> getFiltterMapping(XmlNode xmlNode){
		return getUrlMapping(xmlNode,FILTER_MAPPING);
	}

	public static XmlNode createServletXmlNode(String nodeName,Map<String, String> initParams,Set<String> patterns){
		return createXmlNode(nodeName, SERVLET_MAPPING, initParams, patterns);
	}
	
	public static XmlNode createFilterXmlNode(String nodeName,Map<String, String> initParams,Set<String> patterns){
		return createXmlNode(nodeName, FILTER_MAPPING, initParams, patterns);
	}
	
	
	private static Set<String> getUrlMapping(XmlNode xmlNode,String nodeName) {
		Set<String> patternStrs = new HashSet<String>();
		NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(xmlNode);
		List<XmlNode> servletMappings = nameFilter
				.findNodeList(nodeName);
		for (XmlNode servletMapping : servletMappings) {
			String urlPattern = servletMapping.getAttribute(URL_PATTERN);
			patternStrs.add(urlPattern);
		}
		return patternStrs;
	}
	
	private static XmlNode createXmlNode(String nodeName,String urlPatternNodeName,Map<String, String> initParams,Set<String> patterns){
		XmlNode xmlNode=new XmlNode(nodeName);
		if(initParams!=null){
			for (String name : initParams.keySet()) {
				String value=initParams.get(name);
				XmlNode initNode=new XmlNode(INIT_PARAM);
				initNode.setAttribute("name", name);
				initNode.setAttribute("value", value);
				xmlNode.addNode(initNode);
			}
		}
		if(patterns!=null){
			for (String urlPattern : patterns) {
				xmlNode.addNode(createPatternNode(urlPatternNodeName,urlPattern));
			}
		}
		
		return xmlNode;
		
	}

	private static XmlNode createPatternNode(String nodeName,String urlPattern) {
		XmlNode urlPatternNode=new XmlNode(nodeName);
		urlPatternNode.setAttribute(URL_PATTERN, urlPattern);
		return urlPatternNode;
	}
	
	
}
