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
package org.tinygroup.plugin.applicationprocessor;

import org.tinygroup.application.Application;
import org.tinygroup.application.ApplicationProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.plugin.PluginManager;
import org.tinygroup.xmlparser.node.XmlNode;

public class PluginMgtProcessor implements ApplicationProcessor {
	private static final String PLUGIN_MANAGEMENT_NODE_NAME = "plugin-management";
	private static Logger logger = LoggerFactory
			.getLogger(PluginMgtProcessor.class);
	private XmlNode pluginMgtNode;
	private PluginManager pluginManager;

	public void setApplication(Application application) {

	}

	public void stop() {
		if (pluginManager != null) {
			pluginManager.stop();
		}
	}

	public void setPluginManager(PluginManager pluginManager) {
		this.pluginManager = pluginManager;
	}

	public void start() {
		logger.logMessage(LogLevel.INFO, "启动插件管理器");
		pluginManager.start();
		logger.logMessage(LogLevel.INFO, "启动插件管理器成功");
	}

	public void setConfiguration(XmlNode xmlNode) {
		this.pluginMgtNode = xmlNode;
	}

	public String getNodeName() {
		return PLUGIN_MANAGEMENT_NODE_NAME;
	}

	public XmlNode getConfiguration() {
		return pluginMgtNode;
	}

	public String getApplicationNodePath() {
		return null;
	}

	public String getComponentConfigPath() {
		return null;
	}

	public void config(XmlNode applicationConfig, XmlNode componentConfig) {
		
	}

	public XmlNode getComponentConfig() {
		return null;
	}

	public XmlNode getApplicationConfig() {
		return null;
	}

	public int getOrder() {
		return DEFAULT_PRECEDENCE;
	}

}
