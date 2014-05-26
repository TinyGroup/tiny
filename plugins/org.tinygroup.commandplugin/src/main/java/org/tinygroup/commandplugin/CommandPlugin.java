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
package org.tinygroup.commandplugin;

import org.tinygroup.command.CommandSystem;
import org.tinygroup.command.ConsoleCommander;
import org.tinygroup.config.impl.AbstractConfiguration;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.plugin.Plugin;

/**
 * 
 * 功能说明:命令插件 

 * 开发人员: renhui <br>
 * 开发时间: 2013-11-18 <br>
 * <br>
 */
public class CommandPlugin extends AbstractConfiguration implements Plugin {
	
	private static final String PLUGIN_COMMAND_NODE_PATH = "/application/plugin-command";
	private static final String PLUGIN_INSTANCE_NAME = "plugin";// plugin.command.xml中的package-name
	
	private CommandSystem command;
	private ConsoleCommander consoleCommander;


	public String getApplicationNodePath() {
		return PLUGIN_COMMAND_NODE_PATH;
	}

	public String getComponentConfigPath() {
		return null;
	}

	public void start() {
		logger.logMessage(LogLevel.DEBUG, "启动插件命令行管理器");
		init();
		logger.logMessage(LogLevel.DEBUG, "启动插件命令行管理器成功");
	}
	
	private void init() {
		logger.logMessage(LogLevel.DEBUG, "初始化插件命令行管理器");
		command = CommandSystem.getInstance(PLUGIN_INSTANCE_NAME);
		consoleCommander = new ConsoleCommander(command);
		consoleCommander.start();
		logger.logMessage(LogLevel.DEBUG, "初始化插件命令行管理器成功");
	}

	public void stop() {
		if(command!=null){
			command.execute("destroy");
		}
		if(consoleCommander!=null&&consoleCommander.isAlive()){
			consoleCommander.stopRead();
		}
	}

}
