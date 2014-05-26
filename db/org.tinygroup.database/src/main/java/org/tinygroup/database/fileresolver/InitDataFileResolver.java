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

import org.tinygroup.database.config.initdata.InitDatas;
import org.tinygroup.database.initdata.InitDataProcessor;
import org.tinygroup.database.util.DataBaseUtil;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

import com.thoughtworks.xstream.XStream;

public class InitDataFileResolver extends AbstractFileProcessor {
	private static final String INITDATA_EXTFILENAME = ".init.xml";

	public boolean isMatch(FileObject fileObject) {
		return fileObject.getFileName().endsWith(INITDATA_EXTFILENAME);
	}

	public void process() {
		logger.logMessage(LogLevel.INFO, "开始处理表格初始化数据init文件");
		InitDataProcessor initDataProcessor = SpringUtil
				.getBean(DataBaseUtil.INITDATA_BEAN);
		XStream stream = XStreamFactory
				.getXStream(DataBaseUtil.INITDATA_XSTREAM);
		for (FileObject fileObject : deleteList) {
			logger.logMessage(LogLevel.INFO, "开始读取表格初始化数据init文件{0}",
					fileObject.getAbsolutePath().toString());
			InitDatas initDatas = (InitDatas)caches.get(fileObject.getAbsolutePath());
			if(initDatas!=null){
				initDataProcessor.removeInitDatas(initDatas);
				caches.remove(fileObject.getAbsolutePath());
			}
			logger.logMessage(LogLevel.INFO, "读取表格初始化数据init文件{0}完毕",
					fileObject.getAbsolutePath().toString());
		}
		for (FileObject fileObject : changeList) {
			logger.logMessage(LogLevel.INFO, "开始读取表格初始化数据init文件{0}",
					fileObject.getAbsolutePath().toString());
			InitDatas oldInitDatas=(InitDatas)caches.get(fileObject.getAbsolutePath());
			if(oldInitDatas!=null){
				initDataProcessor.removeInitDatas(oldInitDatas);
			}
			InitDatas initDatas = (InitDatas) stream.fromXML(fileObject
					.getInputStream());
			initDataProcessor.addInitDatas(initDatas);
			caches.put(fileObject.getAbsolutePath(), initDatas);
			logger.logMessage(LogLevel.INFO, "读取表格初始化数据init文件{0}完毕",
					fileObject.getAbsolutePath().toString());
		}
		logger.logMessage(LogLevel.INFO, "处理表格初始化数据init文件读取完毕");

	}

}
