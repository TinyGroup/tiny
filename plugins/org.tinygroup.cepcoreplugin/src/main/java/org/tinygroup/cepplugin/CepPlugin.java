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
package org.tinygroup.cepplugin;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.config.impl.AbstractConfiguration;
import org.tinygroup.plugin.Plugin;

/**
 * 
 * 功能说明:cep插件 

 * 开发人员: renhui <br>
 * 开发时间: 2013-11-18 <br>
 * <br>
 */
public class CepPlugin extends AbstractConfiguration implements Plugin {
	
	private static final String CEP_NODE_PATH="/application/cep-node-config";
	
	private CEPCore cepcore ;
	

	public CEPCore getCepcore() {
		return cepcore;
	}

	public void setCepcore(CEPCore cepcore) {
		this.cepcore = cepcore;
	}

	public String getApplicationNodePath() {
		return CEP_NODE_PATH;
	}

	public String getComponentConfigPath() {
		return null;
	}

	public void start() {
		try {
			cepcore.setConfig(applicationConfig);
//			cepcore.start();
		} catch (Exception e) {
			logger.errorMessage("CEP 启动出错", e);
		}
	}

	public void stop() {
		try {
			cepcore.stop();
		} catch (Exception e) {
			logger.errorMessage("CEP 停止出错", e);
		}
	}

}
