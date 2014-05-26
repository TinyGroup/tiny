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
package org.tinygroup.plugin.config;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.plugin.PluginManager;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
@XStreamAlias("plugin-config")
public class PluginConfig {
	private static final int DEFAULT_LEVEL=10;

	/**
	 * 插件名称
	 */
	@XStreamAlias("plugin-name")
	@XStreamAsAttribute
	String pluginName;
	@XStreamAlias("plugin-title")
	@XStreamAsAttribute
	String pluginTitle;
	String description;

	public String getPluginTitle() {
		return pluginTitle;
	}

	public void setPluginTitle(String pluginTitle) {
		this.pluginTitle = pluginTitle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 插件对应的Bean
	 */
	@XStreamAlias("plugin-bean")
	@XStreamAsAttribute
	String pluginBean;
	/**
	 * 依赖的插件列表，如果有多个，用“,”分隔
	 */
	@XStreamAlias("depend-plugins")
	@XStreamAsAttribute
	String dependPlugins;
	/**
	 * 依赖本插件的插件列表，如果有多个，用“,”分隔
	 */
	@XStreamAlias("depend-by-plugins")
	@XStreamAsAttribute
	String dependByPlugins;
	
	@XStreamAlias("plugin-level")
	@XStreamAsAttribute
	Integer pluginLevel;
	/**
	 * 启动状态
	 */
	transient int status = PluginManager.STOPED ;
	/**
	 * 状态状态
	 */
	transient boolean healthy;
	/**
	 * 是否已经完成依赖反应
	 */
	transient boolean refacted;
	/**
	 * 依赖列表
	 */
	transient List<PluginConfig> dependList = new ArrayList<PluginConfig>();
	/**
	 * 被依赖列表
	 */
	transient List<PluginConfig> dependByList = new ArrayList<PluginConfig>();

	public boolean isRefacted() {
		return refacted;
	}

	public void setRefacted(boolean refacted) {
		this.refacted = refacted;
	}

	public List<PluginConfig> getDependList() {
		if(dependList==null){
			dependList=new ArrayList<PluginConfig>();
		}
		return dependList;
	}

	public List<PluginConfig> getDependByList() {
		if(dependByList==null){
			dependByList=new ArrayList<PluginConfig>();
		}
		return dependByList;
	}

	public String getDependByPlugins() {
		return dependByPlugins;
	}

	public void setDependByPlugins(String dependByPlugins) {
		this.dependByPlugins = dependByPlugins;
	}

	public boolean isHealthy() {
		return healthy;
	}

	public void setHealthy(boolean healthy) {
		this.healthy = healthy;
	}

	public Integer getPluginLevel() {
		if(pluginLevel==null){
			pluginLevel=DEFAULT_LEVEL;
		}
		return pluginLevel;
	}

	public void setPluginLevel(Integer pluginLevel) {
		this.pluginLevel = pluginLevel;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getPluginName() {
		return pluginName;
	}

	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}

	public String getPluginBean() {
		return pluginBean;
	}

	public void setPluginBean(String pluginBean) {
		this.pluginBean = pluginBean;
	}

	public String getDependPlugins() {
		return dependPlugins;
	}

	public void setDependPlugins(String dependPlugins) {
		this.dependPlugins = dependPlugins;
	}

}
