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
package org.tinygroup.weblayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.parser.filter.NameFilter;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * tiny servlet 处理器的抽象实现
 * 
 * @author renhui
 * 
 */
public abstract class AbstractTinyProcessor implements TinyProcessor {

	private static final String TINY_PROCESSOR = "tiny-processor";

	private static final String INIT_PARAM = "init-param";

	private static final String SERVLET_MAPPING = "servlet-mapping";
	private XmlNode xmlNode;
	// 存放初始化参数的map
	private Map<String, String> initParamMap = new HashMap<String, String>();
	// 存放映射正则表达式列表
	private List<Pattern> patterns = new ArrayList<Pattern>();
    //存放正则表达式的字符串格式
	private List<String> patternStrs = new ArrayList<String>();

	private static Logger logger = LoggerFactory.getLogger(AbstractTinyProcessor.class);

	public String getNodeName() {
		return TINY_PROCESSOR;
	}

	public void setConfiguration(XmlNode xmlNode) {
		this.xmlNode = xmlNode;
		
	}

	/**
	 * 加载节点配置信息，存入对象中
	 * 
	 * @param xmlNode
	 */
	public void init() {
		if (xmlNode != null) {
			initParam(xmlNode);
			initPattern(xmlNode);
		}
	}

	public void destory() {
		xmlNode = null;
		initParamMap = null;
		patterns = null;
		patternStrs = null;

	}

	private void initPattern(XmlNode xmlNode) {
		NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(xmlNode);
		List<XmlNode> servletMappings = nameFilter
				.findNodeList(SERVLET_MAPPING);
		for (XmlNode servletMapping : servletMappings) {
			String urlPattern = servletMapping.getAttribute("url-pattern");
			if (!patternStrs.contains(urlPattern)) {
				patterns.add(Pattern.compile(urlPattern));
				patternStrs.add(urlPattern);
			}
			logger.logMessage(LogLevel.DEBUG, "<{}>的url-pattern:'{}'", this
					.getClass().getName(), urlPattern);
		}
	}

	private void initParam(XmlNode xmlNode) {
		NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(xmlNode);
		List<XmlNode> initParamNodes = nameFilter.findNodeList(INIT_PARAM);
		for (XmlNode initParamNode : initParamNodes) {
			String name = initParamNode.getAttribute("name");
			String value = initParamNode.getAttribute("value");
			initParamMap.put(name, value);
			logger.logMessage(LogLevel.DEBUG, "<{}>的初始化参数name='{}',value='{}'",
					this.getClass().getName(), name, value);

		}
	}

	public XmlNode getConfiguration() {
		return xmlNode;
	}

	public boolean isMatch(String urlString) {
		for (Pattern pattern : patterns) {
			Matcher matcher = pattern.matcher(urlString);
			if (matcher.matches()) {
				logger.logMessage(LogLevel.DEBUG, "请求路径：<{}>,匹配的tiny-processor:<{}>",urlString,this.getClass().getSimpleName());
				return true;
			}
		}
		return false;
	}

	public Map<String, String> getInitParamMap() {
		return initParamMap;
	}

	public List<Pattern> getPatterns() {
		return patterns;
	}

	public void process(String urlString, WebContext context) {
		reallyProcess(urlString, context);
	}

	/**
	 * tinyprocessor的逻辑处理方法，由子类来完成
	 * 
	 * @param urlString
	 * @param context
	 */
	public abstract void reallyProcess(String urlString, WebContext context);
}
