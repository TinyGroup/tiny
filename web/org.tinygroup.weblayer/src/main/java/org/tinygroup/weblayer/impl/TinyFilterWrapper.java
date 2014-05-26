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
package org.tinygroup.weblayer.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.parser.filter.NameFilter;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.weblayer.AbstractTinyFilter;
import org.tinygroup.weblayer.FilterWrapper;
import org.tinygroup.weblayer.TinyFilterHandler;
import org.tinygroup.weblayer.WebContext;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * tiny-filter的包装类
 * 
 * @author renhui
 * 
 */
public class TinyFilterWrapper extends AbstractTinyFilter implements
		FilterWrapper {

	private static final String SPLIT_CHAR = ",";

	private List<Filter> filters = new ArrayList<Filter>();

	private static final Logger logger = LoggerFactory
			.getLogger(TinyFilterWrapper.class);

	private List<String> filterBeanNames = new ArrayList<String>();;

	
	protected void initParam(XmlNode xmlNode) {
		NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(xmlNode);
		List<XmlNode> initParamNodes = nameFilter.findNodeList(INIT_PARAM);
		for (XmlNode initParamNode : initParamNodes) {
			String name = initParamNode.getAttribute("name");
			String value = initParamNode.getAttribute("value");
			if (TinyFilterConfig.FILTER_BEAN_NAMES.equals(name)
					&& !StringUtil.isBlank(value)) {
				String[] beanNames = value.split(SPLIT_CHAR);
				StringBuffer buffer = new StringBuffer();
				for (String beanName : beanNames) {
					if (!filterBeanNames.contains(beanName)) {
						buffer.append(beanName).append(SPLIT_CHAR);
						filterBeanNames.add(beanName);
					}
				}
				if(buffer.length()>0){
					initParamMap.put(name, buffer.deleteCharAt(buffer.length() - 1)
							.toString());
				}
				

			} else {
				initParamMap.put(name, value);
			}
			logger.logMessage(LogLevel.DEBUG, "<{}>的初始化参数name='{}',value='{}'",
					this.getClass().getName(), name, value);

		}
	}

	
	public void initTinyFilter() {
		super.initTinyFilter();
		logger.logMessage(LogLevel.INFO, "filter包装类开始实例化filter");
		for (String beanName : filterBeanNames) {
			Filter filter = SpringUtil.getBean(beanName);
			if (filter != null) {
				logger.logMessage(LogLevel.INFO, "实例化filter：<{}>", beanName);
				try {
					filter.init(new TinyFilterConfig(getInitParamMap()));
				} catch (ServletException e) {
					logger.errorMessage("初始化filter:{}出错", e, beanName);
					throw new RuntimeException("初始化filter出错", e);
				}
				filters.add(filter);
			}
		}
		logger.logMessage(LogLevel.INFO, "filter包装类实例化filter结束");

	}

	
	public void destoryTinyFilter() {
		super.destoryTinyFilter();
		for (Filter filter : filters) {
			filter.destroy();
		}
	}

	public void filterWrapper(WebContext context, TinyFilterHandler hander) {
		HttpServletRequest request = context.getRequest();
		HttpServletResponse response = context.getResponse();
		String servletPath = hander.getServletPath();
		if (isMatch(servletPath)) {
			TinyFilterChain filterChain = new TinyFilterChain(filters, hander);
			try {
				filterChain.doFilter(request, response);
			} catch (Exception e) {
				logger.errorMessage(e.getMessage(), e);
				throw new RuntimeException("过滤器链执行出错", e);
			}
		}
	}

}
