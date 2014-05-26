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

import org.tinygroup.xmlparser.node.XmlNode;

/**
 * 所有需要进行应用配置统一管理的类，都推荐实现此接口。
 * 通过此接口，可以由框架自动注入配置信息，且在配置进行刷新的时候，自动推送参数到应用，以便及时做出更新。
 * 
 * 
 * @author luoguo
 * 
 */
public interface Configuration {
	/**
	 * 获取对应的节点名，指在父节点下的子节点
	 * @return
	 */
	String getApplicationNodePath();

	/**
	 * 返回模块配置路径不用带扩展名
	 * 
	 * @return
	 */
	String getComponentConfigPath();

	/**
	 * 设置配置信息
	 * 
	 * @param applicationConfig
     * @param componentConfig
	 */
	void config(XmlNode applicationConfig, XmlNode componentConfig);

	/**
	 * 获取组件配置信息
	 * 
	 * @return
	 */
	XmlNode getComponentConfig();

	/**
	 * 获取应用配置信息
	 * 
	 * @return
	 */
	XmlNode getApplicationConfig();
}
