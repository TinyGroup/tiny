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
package org.tinygroup.config;

import java.util.Collection;
import java.util.Map;

import org.tinygroup.vfs.FileObject;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * 应用配置管理器
 * 
 * @author luoguo
 * 
 */
public interface ConfigurationManager {
	String APPLICATION_CONFIGURATION_FILE = "/application.xml";
	String APPLICATION_ROOTNODE_NAME = "application";

	/**
	 * 载入配置
	 * 
	 * @param content
	 *            配置内容
	 */
	void loadApplicationConfig(String config);

	Map<String, String> getApplicationPropertiesMap();

	void setApplicationProperty(String key, String value);

	String getApplicationProperty(String key);

	String getApplicationProperty(String key, String defaultValue);

	/**
	 * 分发应用配置<br>
	 * 应用配置会促使配置管理器把配置信息推送到配置订阅者
	 */
	void distributeConfig();

	/**
	 * 添加组件配置
	 * 
	 */
	void loadComponentConfig(FileObject fileObject);
	
	/**
	 * 卸载组件配置
	 * @param path 组件配置文件路径
	 */
	void unloadComponentConfig(String path);

	/**
	 * 获取应用配置
	 * 
	 * @return
	 */
	XmlNode getApplicationConfig();

	void setConfigurationList(Collection<Configuration> configurationList);

	/**
	 * 获取组件配置MAP
	 * 
	 * @return
	 */
	Map<String, XmlNode> getComponentConfigMap();

}
