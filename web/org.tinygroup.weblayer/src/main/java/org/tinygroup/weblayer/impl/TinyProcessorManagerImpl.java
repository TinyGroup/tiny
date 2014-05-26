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

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.parser.filter.NameFilter;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.weblayer.TinyProcessor;
import org.tinygroup.weblayer.TinyProcessorManager;
import org.tinygroup.weblayer.WebContext;
import org.tinygroup.weblayer.configmanager.TinyProcessorConfigManager;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * tiny servlet处理器管理接口的默认实现
 * 
 * @author renhui
 * 
 */
public class TinyProcessorManagerImpl implements TinyProcessorManager {

	private List<TinyProcessor> tinyProcessorList = new ArrayList<TinyProcessor>();

	private TinyProcessorConfigManager configManager;

	private Map<TinyProcessor, List<XmlNode>> processorXmlNodes = new HashMap<TinyProcessor, List<XmlNode>>();

	private static Logger logger = LoggerFactory
			.getLogger(TinyProcessorManagerImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.tinygroup.weblayer.impl.TinyProcessorManager#execute(java
	 * .lang.String, org.tinygroup.weblayer.WebContext)
	 */
	public boolean execute(String url, WebContext context) {
		boolean canExecute = false;
		for (TinyProcessor tinyProcessor : tinyProcessorList) {
			if (tinyProcessor.isMatch(url)) {
				canExecute = true;
				tinyProcessor.process(url, context);
				break;
			}
		}
		return canExecute;
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
		List<XmlNode> nodes = nameFilter
				.findNodeList(TinyProcessor.TINY_PROCESSOR);
		for (XmlNode node : nodes) {
			String processorId = node.getAttribute("id");
			String processorClassName =getProcessorBeanName(node); 
			logger.logMessage(LogLevel.INFO, "tiny-processor:{}开始被加载",
					processorId);
			TinyProcessor processor = null;
			try {
				processor = instanceProcessor(processorClassName);
				List<XmlNode> processorNodes = null;
				if (!processorXmlNodes.containsKey(processor)) {
					processorNodes = new ArrayList<XmlNode>();
				} else {
					processorNodes = processorXmlNodes.get(processor);
				}
				if (processorNodes != null) {
					processorNodes.add(node);
					processorXmlNodes.put(processor, processorNodes);
				}

			} catch (Exception e) {
				logger.errorMessage(
						"创建tiny-processor处理器：{}出错，由于其类名称：{}不能进行实例化", e,
						processorId, processorClassName);
				throw new RuntimeException(e);
			}
			logger.logMessage(LogLevel.INFO, "tiny-processor:{}加载结束",
					processorId);

		}
	}

	private String getProcessorBeanName(XmlNode xmlNode) {
		String beanName= xmlNode.getAttribute("bean-name");
		if(StringUtil.isBlank(beanName)){
			beanName=xmlNode.getAttribute("class");
		}
		return beanName;
	}

	/**
	 * 初始化各个processor
	 */
	private void initProcessor() {
		for (TinyProcessor tinyProcessor : processorXmlNodes.keySet()) {

			XmlNode xmlNode = createXmlNode(processorXmlNodes
					.get(tinyProcessor));
			tinyProcessor.setConfiguration(xmlNode);
			tinyProcessor.init();
			tinyProcessorList.add(tinyProcessor);
		}

	}

	private XmlNode createXmlNode(List<XmlNode> xmlNodes) {
		XmlNode newXmlNode = new XmlNode(TinyProcessor.TINY_PROCESSOR);
		for (XmlNode xmlNode : xmlNodes) {
			List<XmlNode> subNodes = xmlNode.getSubNodes();
			for (XmlNode node : subNodes) {
				newXmlNode.addNode(node);
			}
		}
		return newXmlNode;
	}

	private TinyProcessor instanceProcessor(String servletClassName)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		TinyProcessor processor = SpringUtil.getBean(
				servletClassName);
		if (processor == null) {
			processor = (TinyProcessor) Class.forName(servletClassName)
					.newInstance();
		}
		return processor;
	}

	public void initTinyResources() {
		tinyProcessorList.clear();//先清除
		processorXmlNodes.clear();
		if (configManager != null) {
			List<XmlNode> configs = configManager.getConfigs();
			XmlNode component = configManager.getComponentConfig();
			XmlNode application = configManager.getApplicationConfig();
			configs.addAll(ConfigurationUtil.combineSubList(application, component, TinyProcessor.TINY_PROCESSOR, "id"));
			for (XmlNode config : configs) {
				nodeProcessor(config);
			}
			initProcessor();
		}

	}

	public void setConfigManager(TinyProcessorConfigManager configManager) {
		this.configManager = configManager;
	}

	public void destoryTinyResources() {
		for (TinyProcessor tinyProcessor : tinyProcessorList) {
			tinyProcessor.destory();
		}
		configManager = null;
		processorXmlNodes = null;

	}

}
