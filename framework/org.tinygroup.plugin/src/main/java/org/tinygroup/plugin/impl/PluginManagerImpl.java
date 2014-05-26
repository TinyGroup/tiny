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
package org.tinygroup.plugin.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.plugin.Plugin;
import org.tinygroup.plugin.PluginManager;
import org.tinygroup.plugin.config.PluginConfig;
import org.tinygroup.plugin.config.PluginConfigs;
import org.tinygroup.springutil.SpringUtil;

public class PluginManagerImpl implements PluginManager {
	Logger logger = LoggerFactory.getLogger(PluginManagerImpl.class);
	List<PluginConfig> pluginConfigList = new ArrayList<PluginConfig>();
	Map<String, PluginConfig> pluginConfigMap = new HashMap<String, PluginConfig>();

	public void addPlugin(PluginConfigs pluginConfigs) {
		if (pluginConfigs.getPluginConfigs() != null) {
			for (PluginConfig pluginConfig : pluginConfigs.getPluginConfigs()) {
				addPlugin(pluginConfig);
			}
		}
	}

	public void addPlugin(PluginConfig pluginConfig) {
		pluginConfigList.add(pluginConfig);
		pluginConfigMap.put(pluginConfig.getPluginName(), pluginConfig);
	}

	public void start() {
		refact();
		for (PluginConfig pluginConfig : pluginConfigList) {
			if (pluginConfig.isHealthy() && pluginConfig.getStatus() == STOPED) {// 启动健康的未启动插件
				startPlugin(pluginConfig);
			}
		}
	}

	public void stop() {
		for (PluginConfig pluginConfig : pluginConfigList) {
			if (pluginConfig.getStatus() == RUNING) {// 停止运行的插件
				stopPlugin(pluginConfig);
			}
		}
	}

	public void startPlugin(String pluginName) {
		PluginConfig pluginConfig = pluginConfigMap.get(pluginName);
		startPlugin(pluginConfig);
	}

	private void startPlugin(PluginConfig pluginConfig) {
		logger.logMessage(LogLevel.INFO, "开始启动插件：{}...",
				pluginConfig.getPluginName());
		if (pluginConfig.getStatus() == RUNING) {
			logger.logMessage(LogLevel.INFO, "{}插件已经启动！",
					pluginConfig.getPluginName());
		} else {
			if (!pluginConfig.isHealthy()) {// 如果当前插件不健康
				logger.logMessage(LogLevel.INFO, "{}插件不健康，无法启动，原因是依赖的以下插件不健康：",
						pluginConfig.getPluginName());
				for (PluginConfig config : pluginConfig.getDependList()) {
					if (!config.isHealthy()) {
						logger.logMessage(LogLevel.INFO, "{}插件不健康，无法启动！",
								config.getPluginName());
					}
				}
			} else {
				for (PluginConfig config : pluginConfig.getDependList()) {
					if (config.getStatus() == STOPED) {
						startPlugin(config);
					}
				}
			}
			Plugin plugin = SpringUtil.getBean(pluginConfig.getPluginBean());
			plugin.start();
			pluginConfig.setStatus(RUNING);
		}
		logger.logMessage(LogLevel.INFO, "插件：{}启动完毕。",
				pluginConfig.getPluginName());

	}

	public void stopPlugin(String pluginName) {
		PluginConfig pluginConfig = pluginConfigMap.get(pluginName);
		if (pluginConfig == null) {
			logger.logMessage(LogLevel.INFO, "不存在<{}>插件！", pluginName);
			return;
		}else{
			stopPlugin(pluginConfig);
		}
	}

	private void stopPlugin(PluginConfig pluginConfig) {
		if (pluginConfig.getStatus() == STOPED) {
			logger.logMessage(LogLevel.INFO, "<{}>插件已经停止！",
					pluginConfig.getPluginName());
		} else {
			Plugin plugin = SpringUtil.getBean(pluginConfig.getPluginBean());
			for (PluginConfig config : pluginConfig.getDependByList()) {// 停止依赖当前插件的插件
				stopPlugin(config);
			}
			plugin.stop();
			pluginConfig.setStatus(STOPED);
		}
	}

	public int getPluginStatus(String pluginName) {
		return pluginConfigMap.get(pluginName).getStatus();
	}

	public PluginConfig getPluginConfig(String pluginName) {
		return pluginConfigMap.get(pluginName);
	}

	public List<PluginConfig> getPluginConfigList() {
		List<PluginConfig> list = new ArrayList<PluginConfig>();
		list.addAll(pluginConfigList);
		return list;
	}

	public void stopLevel(int pluginLevel) {
		for (PluginConfig pluginConfig : pluginConfigList) {
			if (pluginConfig.getPluginLevel() >= pluginLevel
					&& pluginConfig.isHealthy()
					&& pluginConfig.getStatus() == RUNING) {
				stopPlugin(pluginConfig);
			}
		}
	}

	public void startLevel(int pluginLevel) {
		for (PluginConfig pluginConfig : pluginConfigList) {
			if (pluginConfig.getPluginLevel() <= pluginLevel
					&& pluginConfig.isHealthy()
					&& pluginConfig.getStatus() == STOPED) {
				startPlugin(pluginConfig);
			}
		}
	}

	private void refact() {
		// 首先重置状态
		for (PluginConfig pluginConfig : pluginConfigList) {
			pluginConfig.getDependByList().clear();
			pluginConfig.getDependList().clear();
			pluginConfig.setHealthy(false);
			pluginConfig.setRefacted(false);
		}// 进行反应计算
		for (PluginConfig pluginConfig : pluginConfigList) {
			refactPlugin(pluginConfig,new ArrayList<String>());
		}// 停止不健康的
		for (PluginConfig pluginConfig : pluginConfigList) {
			if (!pluginConfig.isHealthy()) {// 停止不健康的插件
				stopPlugin(pluginConfig);
			}
		}
	}

	private void refactPlugin(PluginConfig pluginConfig,List<String> pluginNames) {
		String pluginName = pluginConfig.getPluginName();
		if(pluginNames.contains(pluginName)){
			return ;
		}else{
			pluginNames.add(pluginName);
		}
		boolean healthy = true;
		healthy = healthy&&dealDepend(pluginConfig,pluginNames);
		healthy = healthy&&dealDependBy(pluginConfig,pluginNames);
		pluginConfig.setHealthy(healthy);// 设置健康情况
		pluginConfig.setRefacted(true);// 设置已经反应过
	}
	
	private boolean dealDependBy(PluginConfig pluginConfig,List<String> pluginNames){
		String dependByPlugins = pluginConfig.getDependByPlugins();
		if(dependByPlugins != null && dependByPlugins.length() > 0){
			String[] dependByPluginArray = dependByPlugins.split(",");
			boolean healthy = true;
			for (String name : dependByPluginArray) {
				PluginConfig dependByPluginConfig = pluginConfigMap.get(name);
				if (dependByPluginConfig == null) {
					healthy = false;
					logger.logMessage(LogLevel.ERROR, "依赖插件{}的插件{}不存在！",
							pluginConfig.getPluginName(), name);
				} else {
					if (!dependByPluginConfig.isRefacted()) {// 如果还没有发生过，则使反应
						refactPlugin(dependByPluginConfig,pluginNames);
					}
					if (!dependByPluginConfig.isHealthy()) {
						logger.logMessage(LogLevel.ERROR, "依赖插件{}的插件{}不健康！",
								pluginConfig.getPluginName(), name);
						healthy = false;
					}
					pluginConfig.getDependByList().add(dependByPluginConfig);
					dependByPluginConfig.getDependList().add(pluginConfig);
				}
			}
			return healthy;
		} 
		return true;
	}
	
	private boolean dealDepend(PluginConfig pluginConfig,List<String> pluginNames){
		String dependPlugins = pluginConfig.getDependPlugins();
		if (dependPlugins != null && dependPlugins.length() > 0) {
			String[] dependPluginArray = dependPlugins.split(",");
			boolean healthy = true;
			for (String name : dependPluginArray) {
				PluginConfig dependPluginConfig = pluginConfigMap.get(name);
				if (dependPluginConfig == null) {
					healthy = false;
					logger.logMessage(LogLevel.ERROR, "插件{}依赖的插件{}不存在！",
							pluginConfig.getPluginName(), name);
				} else {
					if (!dependPluginConfig.isRefacted()) {// 如果还没有发生过，则使反应
						refactPlugin(dependPluginConfig,pluginNames);
					}
					if (!dependPluginConfig.isHealthy()) {
						logger.logMessage(LogLevel.ERROR, "插件{}依赖的插件{}不健康！",
								pluginConfig.getPluginName(), name);
						healthy = false;
					}
					pluginConfig.getDependList().add(dependPluginConfig);
					dependPluginConfig.getDependByList().add(pluginConfig);
				}
			}
			return healthy;
		}
		return true;
	}

	public void removePlugin(PluginConfigs pluginConfigs) {
		if (pluginConfigs.getPluginConfigs() != null) {
			for (PluginConfig pluginConfig : pluginConfigs.getPluginConfigs()) {
				removePlugin(pluginConfig);
			}
		}

	}

	public void removePlugin(PluginConfig pluginConfig) {
		stopPlugin(pluginConfig);// 停止当前插件
		pluginConfigList.remove(pluginConfig);// 删除之
		pluginConfigMap.remove(pluginConfig.getPluginName());// 删除之
	}

}
