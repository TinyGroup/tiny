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
package org.tinygroup.cache;
import org.tinygroup.config.impl.AbstractConfiguration;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * 
 * 功能说明:获取cache初始化范围的配置 

 * 开发人员: renhui <br>
 * 开发时间: 2013-11-22 <br>
 * <br>
 */
public class CacheInitConfig extends AbstractConfiguration {
	
	private static final String DEFAULT_REGION = "testCache1";

	private static final String CACHE_INIT_CONFIG_PATH="/application/cache-init-config";
	
	private String region;
	

	public String getApplicationNodePath() {
		return CACHE_INIT_CONFIG_PATH;
	}

	public String getComponentConfigPath() {
		return "/cacheinit.config.xml";
	}

	public void config(XmlNode applicationConfig, XmlNode componentConfig) {
        super.config(applicationConfig, componentConfig);
        region=ConfigurationUtil.getPropertyName(applicationConfig, componentConfig, "region",DEFAULT_REGION);
	}

	public String getRegion() {
		return region;
	}
	
}
