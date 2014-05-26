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
package org.tinygroup.cepcore.aop.impl;

import org.tinygroup.cepcore.aop.CEPCoreAopManager;
import org.tinygroup.config.impl.AbstractConfiguration;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * 
 * 功能说明:cep aop配置处理 

 * 开发人员: renhui <br>
 * 开发时间: 2013-11-22 <br>
 * <br>
 */
public class CepCoreAopConfig extends AbstractConfiguration {

	private static final String CEPCORE_AOP_CONFIG_PATH="/application/cepcore-aop-config";
	
	private CEPCoreAopManager manager;
	
	
	public CEPCoreAopManager getManager() {
		return manager;
	}

	public void setManager(CEPCoreAopManager manager) {
		this.manager = manager;
	}

	public String getApplicationNodePath() {
		return CEPCORE_AOP_CONFIG_PATH;
	}

	public String getComponentConfigPath() {
		return "/cepcoreaop.config.xml";
	}


	public void config(XmlNode applicationConfig, XmlNode componentConfig) {
		super.config(applicationConfig, componentConfig);
		XmlNode combineNode= ConfigurationUtil.combineXmlNode(applicationConfig, componentConfig);
		manager.init(combineNode);
	}
	
}
