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
package org.tinygroup.velocity.fileresolver;

import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.velocity.VelocityHelper;
import org.tinygroup.vfs.FileObject;

public class VelocityMacroFileProcessor extends AbstractFileProcessor {

	public boolean isMatch(FileObject fileObject) {
		if (fileObject.getFileName().endsWith(".component")) {
			return true;
		}
		return false;
	}

	public void process() {
		VelocityHelper velocityHelper = SpringUtil.getBean("velocityHelper");
		for (FileObject fileObject : deleteList) {
			logger.logMessage(LogLevel.INFO, "正在移除ui宏模板组件文件[{0}]",
					fileObject.getAbsolutePath());
			velocityHelper.removeMacroFile(fileObject);
			logger.logMessage(LogLevel.INFO, "正在移除ui宏模板组件文件[{0}]",
					fileObject.getAbsolutePath());
		}
		
		for (FileObject fileObject : changeList) {
			logger.logMessage(LogLevel.INFO, "正在加载ui宏模板配置文件[{0}]",
					fileObject.getAbsolutePath());
			FileObject oldFileObject=(FileObject) caches.get(fileObject.getAbsolutePath());
			if(oldFileObject!=null){
				velocityHelper.removeMacroFile(oldFileObject);
			}
			velocityHelper.addMacroFile(fileObject);
			caches.put(fileObject.getAbsolutePath(), fileObject);
			logger.logMessage(LogLevel.INFO, "正在加载ui宏模板配置文件[{0}]",
					fileObject.getAbsolutePath());
		}

	}


}
