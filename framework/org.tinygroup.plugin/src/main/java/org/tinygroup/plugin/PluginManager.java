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
package org.tinygroup.plugin;

import java.util.List;

import org.tinygroup.plugin.config.PluginConfig;
import org.tinygroup.plugin.config.PluginConfigs;

/**
 * 插件管理器
 * 
 * @author luoguo
 * 
 */
public interface PluginManager {
	String BEAN_NAME = "tinyPluginMananger";
	/**
	 * 插件状态，只有两种，运行中和停止
	 */
	int RUNING = 1;// 正在运行
	int STOPED = 0;// 停止

	/**
	 * 添加一组插件配置
	 * 
	 * @param plugin
	 */
	void addPlugin(PluginConfigs pluginConfigs);

	/**
	 * 删除一组插件
	 * 
	 * @param pluginConfigs
	 */
	void removePlugin(PluginConfigs pluginConfigs);

	/**
	 * 添加插件配置
	 * 
	 * @param plugin
	 */
	void addPlugin(PluginConfig pluginConfig);

	/**
	 * 删除插件
	 * 
	 * @param pluginConfig
	 */
	void removePlugin(PluginConfig pluginConfig);

	/**
	 * 启动插件管理器
	 */
	void start();

	/**
	 * 返回指定插件的配置
	 * 
	 * @param pluginName
	 * @return
	 */
	PluginConfig getPluginConfig(String pluginName);

	/**
	 * 返回所有插件配置
	 * 
	 * @return
	 */
	List<PluginConfig> getPluginConfigList();

	/**
	 * 停止插件管理器
	 */
	void stop();

	/**
	 * 启动某个插件，注意：启动<=级别的插件
	 * 
	 * @param pluginName
	 */
	void startPlugin(String pluginName);

	/**
	 * 停止某个级别的插件，注意：停止>=级别的插件
	 * 
	 * @param pluginLevel
	 */
	void stopLevel(int pluginLevel);

	/**
	 * 启动某个级别的插件
	 * 
	 * @param pluginLevel
	 */
	void startLevel(int pluginLevel);

	/**
	 * 停止某个插件
	 * 
	 * @param pluginName
	 */
	void stopPlugin(String pluginName);

	/**
	 * 返回插件的运行状态
	 * 
	 * @param pluginName
	 * @return
	 */
	int getPluginStatus(String pluginName);
}
