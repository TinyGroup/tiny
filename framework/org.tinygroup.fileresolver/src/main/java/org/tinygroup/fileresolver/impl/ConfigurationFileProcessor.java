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
package org.tinygroup.fileresolver.impl;

import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.config.Configuration;
import org.tinygroup.config.ConfigurationManager;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.vfs.FileObject;

public class ConfigurationFileProcessor extends AbstractFileProcessor {

	private static final String CONFIG_EXT_FILENAME = ".config.xml";

	public boolean isMatch(FileObject fileObject) {
		return fileObject.getFileName().toLowerCase()
				.endsWith(CONFIG_EXT_FILENAME);
	}

	public void process() {
		ConfigurationManager configurationManager = ConfigurationUtil
				.getConfigurationManager();
		if (!CollectionUtil.isEmpty(deleteList)
				|| !CollectionUtil.isEmpty(changeList)) {
			for (FileObject fileObject : deleteList) {
				logger.logMessage(LogLevel.INFO, "正在移除组件配置文件[{0}]",
						fileObject.getAbsolutePath());
				configurationManager
						.unloadComponentConfig(fileObject.getPath());
				logger.logMessage(LogLevel.INFO, "移除组件配置文件[{0}]结束",
						fileObject.getAbsolutePath());
			}
			for (FileObject fileObject : changeList) {
				logger.logMessage(LogLevel.INFO, "开始读取组件配置文件:{0}",
						fileObject.getFileName());
				configurationManager.loadComponentConfig(fileObject);
				logger.logMessage(LogLevel.INFO, "读取组件配置文件:{0}完成",
						fileObject.getFileName());
			}
			configurationManager.setConfigurationList(SpringUtil
					.getBeansOfType(Configuration.class));
			configurationManager.distributeConfig();// 重新分发配置信息
		}

	}

	public int getOrder() {
		return HIGHEST_PRECEDENCE;
	}

}
