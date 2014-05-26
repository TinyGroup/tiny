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
import org.tinygroup.weblayer.webcontext.DefaultWebContext;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * tinyfilter的抽象实现
 * @author renhui
 *
 */
public abstract class AbstractTinyFilter implements TinyFilter {
	
	protected static final String TINY_FILTER = "tiny-filter";
	
	protected static final String INIT_PARAM = "init-param";

	protected static final String FILTER_MAPPING = "filter-mapping";
	
	private XmlNode xmlNode;
	
	// 存放初始化参数的map
	protected Map<String, String> initParamMap = new HashMap<String, String>();
	// 存放映射正则表达式列表
	private List<Pattern> patterns = new ArrayList<Pattern>();
    //存放正则表达式的字符串格式
	private List<String> patternStrs = new ArrayList<String>();

	protected static Logger logger = LoggerFactory.getLogger(AbstractTinyFilter.class);

	public String getNodeName() {
		return TINY_FILTER;
	}

	public void setConfiguration(XmlNode xmlNode) {
		this.xmlNode=xmlNode;
		
	}
	
	protected void initPattern(XmlNode xmlNode) {
		NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(xmlNode);
		List<XmlNode> filterMappings = nameFilter
				.findNodeList(FILTER_MAPPING);
		for (XmlNode filterMapping : filterMappings) {
			String urlPattern = filterMapping.getAttribute("url-pattern");
			if (!patternStrs.contains(urlPattern)) {
				patterns.add(Pattern.compile(urlPattern));
				patternStrs.add(urlPattern);
			}
			logger.logMessage(LogLevel.DEBUG, "<{}>的url-pattern:'{}'", this
					.getClass().getName(), urlPattern);
		}
	}

	protected void initParam(XmlNode xmlNode) {
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

	public void preProcess(WebContext context) {
		

	}

	public void postProcess(WebContext context) {
		

	}

	public void initTinyFilter() {
		//获取
		if (xmlNode != null) {
			initParam(xmlNode);
			initPattern(xmlNode);
		}
	}

	public void destoryTinyFilter() {
		xmlNode = null;
		initParamMap = null;
		patterns = null;
		patternStrs = null;

	}

	public boolean isMatch(String url) {
		for (Pattern pattern : patterns) {
			Matcher matcher = pattern.matcher(url);
			if (matcher.matches()) {
				logger.logMessage(LogLevel.DEBUG, "请求路径：<{}>,匹配的tiny-filter:<{}>",url,this.getClass().getSimpleName());
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
	
	public WebContext wrapContext(WebContext wrappedContext) {
		WebContext context=getAlreadyWrappedContext(wrappedContext);
		initContext(context);
		return context;
	}
	
	protected void initContext(WebContext context) {
	
	}


	/**
      * 
      * 返回已经包装的上下文
      * @param wrappedContext
      * @return
      */
	protected WebContext getAlreadyWrappedContext(WebContext wrappedContext) {
		return new DefaultWebContext(wrappedContext);
	}
	
	protected  String get(String param) {
		return getInitParamMap().get(param);  
	}
	/**
	 * 
	 * 解析额外节点配置信息
	 * @param parserNode
	 */
	protected void parserExtraConfig(XmlNode parserNode) {

	}

	public int getOrder() {
		return DEFAULT_PRECEDENCE;
	}
	
	

}
