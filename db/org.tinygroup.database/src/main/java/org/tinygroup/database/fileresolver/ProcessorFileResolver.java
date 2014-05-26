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
package org.tinygroup.database.fileresolver;

import org.tinygroup.database.ProcessorManager;
import org.tinygroup.database.config.processor.Processors;
import org.tinygroup.database.util.DataBaseUtil;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

import com.thoughtworks.xstream.XStream;

public class ProcessorFileResolver extends AbstractFileProcessor {
	private static final String PROCESSOR_EXTFILENAME = ".database.processor.xml";

	public boolean isMatch(FileObject fileObject) {
		return fileObject.getFileName().endsWith(PROCESSOR_EXTFILENAME);
	}

	public void process() {
		logger.logMessage(LogLevel.INFO, "开始读取database.processor文件");
		ProcessorManager processorManager = SpringUtil
				.getBean(DataBaseUtil.PROCESSORMANAGER_BEAN);
		XStream stream = XStreamFactory
				.getXStream(DataBaseUtil.PROCESSOR_XSTREAM);
		for (FileObject fileObject : deleteList) {
			logger.logMessage(LogLevel.INFO, "开始移除database.processor文件{0}",
					fileObject.getAbsolutePath());
			Processors processors = (Processors)caches.get(fileObject.getAbsolutePath());
			if(processors!=null){
				processorManager.removePocessors(processors);
				caches.remove(fileObject.getAbsolutePath());
			}
			logger.logMessage(LogLevel.INFO, "移除database.processor文件{0}完毕",
					fileObject.getAbsolutePath());
		}
		for (FileObject fileObject : changeList) {
			logger.logMessage(LogLevel.INFO, "开始读取database.processor文件{0}",
					fileObject.getAbsolutePath());
			Processors oldProcessors = (Processors)caches.get(fileObject.getAbsolutePath());
			if(oldProcessors!=null){
				processorManager.removePocessors(oldProcessors);
			}	
			Processors processors = (Processors) stream.fromXML(fileObject
					.getInputStream());
			processorManager.addPocessors(processors);
			caches.put(fileObject.getAbsolutePath(), processors);
			logger.logMessage(LogLevel.INFO, "读取database.processor文件{0}完毕",
					fileObject.getAbsolutePath());
		}
		logger.logMessage(LogLevel.INFO, "database.processor文件读取完毕");

	}

}
