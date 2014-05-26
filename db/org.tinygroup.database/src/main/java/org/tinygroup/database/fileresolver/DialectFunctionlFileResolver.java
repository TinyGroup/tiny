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

import org.tinygroup.database.config.dialectfunction.DialectFunctions;
import org.tinygroup.database.dialectfunction.DialectFunctionProcessor;
import org.tinygroup.database.util.DataBaseUtil;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

import com.thoughtworks.xstream.XStream;

public class DialectFunctionlFileResolver extends AbstractFileProcessor {

	private static final String FUNCTION_EXTFILENAME = ".dialectfunction.xml";

	public boolean isMatch(FileObject fileObject) {
		return fileObject.getFileName().endsWith(FUNCTION_EXTFILENAME);
	}

	public void process() {
		DialectFunctionProcessor functionProcessor = SpringUtil
				.getBean(DataBaseUtil.FUNCTION_BEAN);
		XStream stream = XStreamFactory
				.getXStream(DataBaseUtil.DATABASE_XSTREAM);
		for (FileObject fileObject : deleteList) {
			logger.logMessage(LogLevel.INFO, "正在移除function文件[{0}]",
					fileObject.getAbsolutePath());
			DialectFunctions functions = (DialectFunctions)caches.get(fileObject.getAbsolutePath());
            if(functions!=null){
            	functionProcessor.removeDialectFunctions(functions);
            	caches.remove(fileObject.getAbsolutePath());
            }
			logger.logMessage(LogLevel.INFO, "移除function文件[{0}]结束",
					fileObject.getAbsolutePath());
		}
		for (FileObject fileObject : changeList) {
			logger.logMessage(LogLevel.INFO, "正在加载function文件[{0}]",
					fileObject.getAbsolutePath());
			DialectFunctions oldFunctions=(DialectFunctions)caches.get(fileObject.getAbsolutePath());
			if(oldFunctions!=null){
				functionProcessor.removeDialectFunctions(oldFunctions);
			}
			DialectFunctions functions = (DialectFunctions) stream.fromXML(fileObject
					.getInputStream());
			functionProcessor.addDialectFunctions(functions);
			caches.put(fileObject.getAbsolutePath(), functions);
			logger.logMessage(LogLevel.INFO, "加载function文件[{0}]结束",
					fileObject.getAbsolutePath());
		}
	}

}
