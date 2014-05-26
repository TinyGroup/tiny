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
package org.tinygroup.flowplugin;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.flow.FlowExecutor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.plugin.Plugin;

public class FlowPlugin implements Plugin {
	/**
	 * 通过依赖关系获取的cepcore实例
	 */
	private CEPCore cepcore;
	FlowExecutor executor;
	private FlowPluginProcessor flowProcessor;
	
	private static Logger logger = LoggerFactory
			.getLogger(FlowPlugin.class);

	public CEPCore getCepcore() {
		return cepcore;
	}

	public void setCepcore(CEPCore cepcore) {
		this.cepcore = cepcore;
	}

	public FlowExecutor getExecutor() {
		return executor;
	}

	public void setExecutor(FlowExecutor executor) {
		this.executor = executor;
	}

	public void init() {
		initProcessors();
	}

	public void initProcessors() {
		flowProcessor = new FlowPluginProcessor();
		flowProcessor.addExecutor(executor);
	}

	public void start() {
		init();
		logger.logMessage(LogLevel.DEBUG, "启动FlowPlugin");
		cepcore.registerEventProcessor(flowProcessor);
		logger.logMessage(LogLevel.DEBUG, "启动FlowPlugin完成");
	}

	public void stop() {
		logger.logMessage(LogLevel.DEBUG, "停止FlowPlugin");
		cepcore.unregisterEventProcessor(flowProcessor);
		logger.logMessage(LogLevel.DEBUG, "停止FlowPlugin完成");
	}

}
