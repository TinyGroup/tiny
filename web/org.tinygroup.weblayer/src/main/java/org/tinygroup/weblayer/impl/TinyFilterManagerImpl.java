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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.commons.order.OrderUtil;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.parser.filter.NameFilter;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.weblayer.FilterWrapper;
import org.tinygroup.weblayer.TinyFilter;
import org.tinygroup.weblayer.TinyFilterManager;
import org.tinygroup.weblayer.configmanager.TinyFiterConfigManager;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * tiny-filter的管理类
 * 
 * @author renhui
 * 
 */
public class TinyFilterManagerImpl implements TinyFilterManager {

	private TinyFiterConfigManager configManager;

	private Map<TinyFilter, List<XmlNode>> processorXmlNodes = new HashMap<TinyFilter, List<XmlNode>>();

	private static Logger logger = LoggerFactory
			.getLogger(TinyFilterManagerImpl.class);

	private List<TinyFilter> tinyFilters = new ArrayList<TinyFilter>();

	private TinyFilterWrapper wrapper;

	public List<TinyFilter> getTinyFiltersWithUrl(String url) {
		List<TinyFilter> filters = new ArrayList<TinyFilter>();
		for (TinyFilter tinyFilter : tinyFilters) {
			if (tinyFilter.isMatch(url)) {
				filters.add(tinyFilter);
			}
		}
		return filters;
	}

	public void initTinyResources() {
		tinyFilters.clear();//先清空
		processorXmlNodes.clear();
		if (configManager != null) {
			List<XmlNode> configs = configManager.getConfigs();
			XmlNode component=configManager.getComponentConfig();
			XmlNode application = configManager.getApplicationConfig();
			configs.addAll(ConfigurationUtil.combineSubList(application, component, TinyFilter.TINY_FILTER, "id"));
			configs.addAll(ConfigurationUtil.combineSubList(application, component, TinyFilter.TINY_WRAPPER_FILTER, "id"));//包装节点
			for (XmlNode config : configs) {
				nodeProcessor(config);
			}
			initProcessor();
			// 对tiny-filter列表进行排序
			OrderUtil.order(tinyFilters);
		}
	}

	private void nodeProcessor(XmlNode xmlNode) {
		processXmlNode(xmlNode);
	}

	/**
	 * 解析xmlnode节点
	 * 
	 * @param xmlNode
	 */
	private void processXmlNode(XmlNode xmlNode) {
		NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(xmlNode);
		List<XmlNode> nodes = nameFilter.findNodeList(TinyFilter.TINY_FILTER);
		for (XmlNode node : nodes) {
			processSubNode(node);
		}
		NameFilter<XmlNode> wrapperFilter = new NameFilter<XmlNode>(xmlNode);
		List<XmlNode> wrapperNode = wrapperFilter
				.findNodeList(TinyFilter.TINY_WRAPPER_FILTER);
		for (XmlNode node : wrapperNode) {
			wrapper = (TinyFilterWrapper) processSubNode(node);
		}
	}

	private TinyFilter createTinyFilter(XmlNode node) {
		String filterId = node.getAttribute("id");
		String filterClassName =getFilterBeanName(node); 
		logger.logMessage(LogLevel.INFO, "tiny-filter:{}开始被加载", filterId);
		TinyFilter filter = null;
		try {
			filter = instanceFilter(filterClassName);
		} catch (Exception e) {
			logger.errorMessage("创建tiny-filter处理器：{}出错，由于其类名称：{}不能进行实例化", e,
					filterId, filterClassName);
			throw new RuntimeException(e);
		}
		logger.logMessage(LogLevel.INFO, "tiny-filter:{}加载结束", filterId);
		return filter;
	}

	private String getFilterBeanName(XmlNode node) {
		String beanName= node.getAttribute("bean-name");
		if(StringUtil.isBlank(beanName)){
			beanName=node.getAttribute("class");
		}
		return beanName;
	}

	private TinyFilter processSubNode(XmlNode node) {
		TinyFilter filter = createTinyFilter(node);
		List<XmlNode> processorNodes = null;
		if (!processorXmlNodes.containsKey(filter)) {
			processorNodes = new ArrayList<XmlNode>();
		} else {
			processorNodes = processorXmlNodes.get(filter);
		}
		if (processorNodes != null) {
			processorNodes.add(node);
			processorXmlNodes.put(filter, processorNodes);
		}
		tinyFilters.add(filter);
		return filter;
	}

	/**
	 * 初始化各个processor
	 */
	private void initProcessor() {
		for (TinyFilter tinyFilter : processorXmlNodes.keySet()) {
			XmlNode xmlNode = createXmlNode(processorXmlNodes.get(tinyFilter));
			tinyFilter.setConfiguration(xmlNode);
			tinyFilter.initTinyFilter();
		}

	}

	private XmlNode createXmlNode(List<XmlNode> xmlNodes) {
		XmlNode newXmlNode = new XmlNode(TinyFilter.TINY_FILTER);
		for (XmlNode xmlNode : xmlNodes) {
			List<XmlNode> subNodes = xmlNode.getSubNodes();
			for (XmlNode node : subNodes) {
				newXmlNode.addNode(node);
			}
		}
		return newXmlNode;
	}

	private TinyFilter instanceFilter(String className)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		TinyFilter filter = SpringUtil.getBean(className);
		if (filter == null) {
			filter = (TinyFilter) Class.forName(className).newInstance();
		}
		return filter;
	}

	public void destoryTinyResources() {
		for (TinyFilter tinyFilter : tinyFilters) {
			tinyFilter.destoryTinyFilter();
		}
		configManager = null;
		processorXmlNodes = null;
		tinyFilters = null;

	}

	public void setConfigManager(TinyFiterConfigManager configManager) {
		this.configManager = configManager;
	}

	public boolean existFilterWrapper() {
		return wrapper != null;
	}

	public FilterWrapper getFilterWrapper() {
		return wrapper;
	}

}
