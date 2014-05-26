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
package org.tinygroup.application.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.tinygroup.application.Application;
import org.tinygroup.application.ApplicationProcessor;
import org.tinygroup.commons.io.StreamUtil;
import org.tinygroup.commons.order.OrderUtil;
import org.tinygroup.config.Configuration;
import org.tinygroup.config.ConfigurationManager;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * 默认的应用实现<br>
 * Tiny框架认为，应用都是一个Application，无论是控制台App还是Web App。<br>
 * 只不过这个应用的复杂程度不同而已。
 * 
 */
public class ApplicationDefault implements Application {
	private static Logger logger = LoggerFactory
			.getLogger(ApplicationDefault.class);
	private ConfigurationManager configurationManager = ConfigurationUtil
			.getConfigurationManager();
	private List<ApplicationProcessor> list = new ArrayList<ApplicationProcessor>();

	/**
	 * 使用输入流作为应用配置构造方法，流的关闭由提供者负责
	 * 
	 * @param inputStream
	 */
	public ApplicationDefault(InputStream inputStream, boolean closeStream) {
		String applicationConfig;
		if (inputStream != null) {
			try {
				applicationConfig = StreamUtil.readText(inputStream, "UTF-8",
						closeStream);
				if (applicationConfig != null) {
					configurationManager
							.loadApplicationConfig(applicationConfig);
				}
			} catch (Exception e) {
				logger.errorMessage("载入应用配置信息时出错，错误原因：{}！", e, e.getMessage());
			}
		}
	}

	/**
	 * 通过xml配置信息构造应用
	 * 
	 * @param applicationConfig
	 */
	public ApplicationDefault(String applicationConfig) {
		if (applicationConfig != null) {
			configurationManager.loadApplicationConfig(applicationConfig);
		}
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see org.tinygroup.application.Application#start()
	 */
	// public void start() {
	// logger.logMessage(LogLevel.INFO, "启动应用开始...");
	// XmlNode node = configurationManager.getApplicationConfig();
	// List<XmlNode> applicationProcessors =
	// node.getSubNodes("application-processor");
	// for(XmlNode processorConfig:applicationProcessors){
	// String processorBean = processorConfig.getAttribute("bean");
	// ApplicationProcessor processor = SpringUtil.getBean(processorBean);
	// }
	// loadSpringAndConfiguration();// 载入Spring及配置
	// configurationManager.setConfigurationList(SpringUtil
	// .getBeansOfType(Configuration.class));
	// configurationManager.distributeConfig();// 分发配置信息

	// FileResolver fileResolver = SpringUtil.getBean(FileResolver.class);
	// fileResolver.resolve();
	// 启动插件框架
	//
	// logger.logMessage(LogLevel.INFO, "启动应用结束。");
	// }

	// /**
	// * 读取自定义扫描路径列表
	// *
	// * @param fileResolver
	// */
	// private void loadFileResolverConfig(FileResolver fileResolver) {
	// XmlNode applicationConfig = configurationManager.getApplicationConfig();
	// if(applicationConfig==null)
	// return;
	// PathFilter<XmlNode> filter = new PathFilter<XmlNode>(applicationConfig);
	// XmlNode findNode = filter
	// .findNode("/application/file-resolver-configuration");
	// if (findNode != null) {
	// String resolve = findNode.getAttribute("resolve-classpath");
	// if (resolve == null || resolve.length() == 0) {
	// resolve = "true";
	// }
	// List<XmlNode> skipList = filter
	// .findNodeList("/application/file-resolver-configuration/skip-patterns/skip-pattern");
	// for (XmlNode node : skipList) {
	// fileResolver.addSkipPathPattern(node.getAttribute("pattern"));
	// }
	// List<XmlNode> classPathList = filter
	// .findNodeList("/application/file-resolver-configuration/class-paths/class-path");
	// for (XmlNode classPath : classPathList) {
	// fileResolver.addManualClassPath(classPath.getAttribute("path"));
	// }
	// }
	// }
	//
	// private void loadSpringAndConfiguration() {
	// logger.logMessage(LogLevel.INFO, "加载Spring Bean文件开始...");
	// FileResolver fileResolver = new FileResolverImpl();
	// fileResolver.addFileProcessor(new SpringBeansFileProcessor());
	// fileResolver.addFileProcessor(new ConfigurationFileProcessor());
	// loadFileResolverConfig(fileResolver);
	// fileResolver.resolve();
	// logger.logMessage(LogLevel.INFO, "加载Spring Bean文件结束。");
	// }
	//
	// /*
	// * （非 Javadoc）
	// *
	// * @see org.tinygroup.application.Application#stop()
	// */
	// public void stop() {
	// logger.logMessage(LogLevel.INFO, "停止应用开始...");
	// PluginManager pluginManager = SpringUtil.getBean(PluginManager.class);
	// pluginManager.stop();
	// logger.logMessage(LogLevel.INFO, "停止应用结束。");
	// }

	private static final String WEBROOT = "TINY_WEBROOT";
	private static final String FILE_PROCESSOR_ORDER = "application.order.xml";
	private boolean isOrder;
	// 保存应用程序的坏境变量
	private Context context = new ContextImpl();
	// 应用处理器列表
	private List<ApplicationProcessor> applicationProcessors = new ArrayList<ApplicationProcessor>();

	private XmlNode applicationConfig;

	public ApplicationDefault() {
	}

	public boolean isOrder() {
		return isOrder;
	}

	public void setOrder(boolean isOrder) {
		this.isOrder = isOrder;
	}

	/**
	 * 启动应用程序
	 */
	public void start() {
		// String webRoot = context.get(WEBROOT);
		// if (webRoot != null) {
		// fullContextFileRepository.addSearchPath(webRoot);
		// }
		logger.logMessage(LogLevel.INFO, "启动应用开始...");
		XmlNode node = configurationManager.getApplicationConfig();
		if (node != null) {
			List<XmlNode> processorConfigs = node
					.getSubNodesRecursively("application-processor");
			if (processorConfigs != null) {
				for (XmlNode processorConfig : processorConfigs) {
					String processorBean = processorConfig.getAttribute("bean");
					ApplicationProcessor processor = SpringUtil
							.getBean(processorBean);
					applicationProcessors.add(processor);
				}
			}
		}
		if (applicationProcessors != null) {
			if (isOrder) {
				// 对ApplicationProcessor列表进行排序
				OrderUtil.order(applicationProcessors);
			}
			for (ApplicationProcessor applicationProcessor : applicationProcessors) {
				applicationProcessor.setApplication(this);
				logger.logMessage(LogLevel.INFO, "应用处理器{}正在启动...",
						applicationProcessor.getClass());
				applicationProcessor.start();
				logger.logMessage(LogLevel.INFO, "应用处理器{}启动完毕。",
						applicationProcessor.getClass());
			}
		}
	}

	/**
	 * 关闭应用程序
	 */
	public void stop() {
		for (ApplicationProcessor processorLoader : applicationProcessors) {
			logger.logMessage(LogLevel.INFO, "应用处理器{}正在停止...",
					processorLoader.getClass());
			processorLoader.stop();
			logger.logMessage(LogLevel.INFO, "应用处理器{}停止完毕。",
					processorLoader.getClass());
		}
	}

	public Context getContext() {
		return context;
	}

	public String getNodeName() {
		return "application-properties";
	}

	public void setConfiguration(XmlNode xmlNode) {
		applicationConfig = xmlNode;
		if (applicationConfig != null) {
			List<XmlNode> subNodes = applicationConfig.getSubNodes("property");
			if (subNodes != null) {
				for (XmlNode property : subNodes) {
					String name = property.getAttribute("name");
					String value = property.getAttribute("value");
					if (name != null && value != null) {
						context.put(name, value);
						logger.logMessage(LogLevel.INFO,
								"从配置文件加载应用参数<{}>=<{}>。", name, value);
					} else {
						logger.logMessage(LogLevel.ERROR, "应用参数<{}>=<{}>设置失败！",
								name, value);
					}
				}
			}
		}
	}

}
