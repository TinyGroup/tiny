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
package org.tinygroup.weblayer.fileresolver;

import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.weblayer.TinyProcessorManager;
import org.tinygroup.weblayer.configmanager.TinyProcessorConfigManager;

/**
 * 搜索tinyprocessor的文件处理器
 * 
 * @author renhui
 * 
 */
public class TinyProcessorFileProcessor extends AbstractFileProcessor {

	private static final String SERVLETS_EXT_FILENAMES = ".tinyprocessor.xml";

	public boolean isMatch(FileObject fileObject) {
		return fileObject.getFileName().endsWith(SERVLETS_EXT_FILENAMES);
	}

	public void process() {
		TinyProcessorConfigManager configManager = SpringUtil
				.getBean(TinyProcessorConfigManager.TINY_PROCESSOR_CONFIGMANAGER);
		if(!CollectionUtil.isEmpty(deleteList)||!CollectionUtil.isEmpty(changeList)){
			for (FileObject fileObject : deleteList) {
				logger.log(LogLevel.INFO, "正在移除tiny-processor描述文件：<{}>",
						fileObject.getAbsolutePath());
				FileObject oldFileObject=(FileObject) caches.get(fileObject.getAbsolutePath());
				if(oldFileObject!=null){
					configManager.removeConfig(oldFileObject);
					caches.remove(fileObject.getAbsolutePath());
				}
				logger.log(LogLevel.INFO, "移除tiny-processor描述文件：<{}>结束",
						fileObject.getAbsolutePath());
			}
			
			for (FileObject fileObject : changeList) {
				FileObject oldFileObject=(FileObject) caches.get(fileObject.getAbsolutePath());
				if(oldFileObject!=null){
					configManager.removeConfig(oldFileObject);
				}
				logger.log(LogLevel.INFO, "找到tiny-processor描述文件：<{}>",
						fileObject.getAbsolutePath());
				configManager.addConfig(fileObject);
				caches.put(fileObject.getAbsolutePath(), fileObject);
			}
			TinyProcessorManager tinyProcessorManager = SpringUtil
					.getBean(TinyProcessorManager.TINY_PROCESSOR_MANAGER);
			tinyProcessorManager.initTinyResources();
		}
	}

}
