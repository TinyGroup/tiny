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
package org.tinygroup.weblayer.tinyprocessor;

import org.tinygroup.config.Configuration;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * 
 * 功能说明:velocity上下文配置
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-11-21 <br>
 * <br>
 */
public class VelocityContextConfiguration implements Configuration {

	private XmlNode applicationConfig;
	private XmlNode componentConfig;
	private XmlNode combineNode;
	
	
	private static final String VELOCITY_CONFIG_PATH = "/application/velocity-context-config";
	

	public String getApplicationNodePath() {
		return VELOCITY_CONFIG_PATH;
	}

	public String getComponentConfigPath() {
		return "/velocitycontext.config.xml";
	}

	public void config(XmlNode applicationConfig, XmlNode componentConfig) {
		this.applicationConfig = applicationConfig;
		this.componentConfig = componentConfig;
		combineNode=new XmlNode("velocity-context-config");
		XmlNode staticClassNodes=new XmlNode("static-classes");
		staticClassNodes.addAll(ConfigurationUtil.combineFindNodeList("static-class", applicationConfig, componentConfig));
		combineNode.addNode(staticClassNodes);
		XmlNode springBeanNodes=new XmlNode("spring-beans");
		springBeanNodes.addAll(ConfigurationUtil.combineFindNodeList("bean", applicationConfig, componentConfig));
		combineNode.addNode(springBeanNodes);
		
	}

	public XmlNode getComponentConfig() {
		return componentConfig;
	}

	public XmlNode getApplicationConfig() {
		return applicationConfig;
	}
	
	public XmlNode getCombineNode(){
		return combineNode;
	}

}
