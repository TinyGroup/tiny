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
package org.tinygroup.serviceplugin;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.config.Configuration;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.plugin.Plugin;
import org.tinygroup.service.ServiceProviderInterface;
import org.tinygroup.serviceplugin.processor.ServiceProcessor;
import org.tinygroup.serviceplugin.processor.impl.ServiceProcessorImpl;
import org.tinygroup.xmlparser.node.XmlNode;

public class ServicePlugin implements Plugin, Configuration {
	private static Logger logger = LoggerFactory.getLogger(ServicePlugin.class);
	private XmlNode applicationConfig;
	private XmlNode componentConfig;
	private ServiceProcessor processor;
	private ServiceProviderInterface provider;
	private CEPCore cepcore;
	
	
	public ServiceProviderInterface getProvider() {
		return provider;
	}

	public void setProvider(ServiceProviderInterface provider) {
		this.provider = provider;
	}

	public CEPCore getCepcore() {
		return cepcore;
	}

	public void setCepcore(CEPCore cepcore) {
		this.cepcore = cepcore;
	}

	public String getApplicationNodePath() {
		return null;
	}

	public String getComponentConfigPath() {
		return null;
	}

	public void config(XmlNode applicationConfig, XmlNode componentConfig) {
		this.componentConfig = componentConfig;
		this.applicationConfig = applicationConfig;
	}

	private void initProcessors(XmlNode config) {
		logger.logMessage(LogLevel.DEBUG, "初始化ServiceProcessor");
		processor = new ServiceProcessorImpl();
		processor.setConfig(config);
		processor.addServiceProvider(provider);
		logger.logMessage(LogLevel.DEBUG, "初始化ServiceProcessor完成");
	}

	public XmlNode getComponentConfig() {
		return componentConfig;
	}

	public XmlNode getApplicationConfig() {
		return applicationConfig;
	}

	public void start() {
		initProcessors(applicationConfig);
		cepcore.registerEventProcessor(processor);
	}

	public void stop() {
		cepcore.unregisterEventProcessor(processor);
	}

}
