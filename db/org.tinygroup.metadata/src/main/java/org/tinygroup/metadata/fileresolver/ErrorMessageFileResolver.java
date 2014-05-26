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
package org.tinygroup.metadata.fileresolver;

import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.metadata.config.errormessage.ErrorMessages;
import org.tinygroup.metadata.errormessage.ErrorMessageProcessor;
import org.tinygroup.metadata.util.MetadataUtil;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

import com.thoughtworks.xstream.XStream;

public class ErrorMessageFileResolver extends AbstractFileProcessor {

	private static final String ERROR_EXTFILENAME = ".error.xml";

	public boolean isMatch(FileObject fileObject) {
		return fileObject.getFileName().endsWith(ERROR_EXTFILENAME);
	}

	public void process() {
		ErrorMessageProcessor errorMessageProcessor = SpringUtil
				.getBean(MetadataUtil.ERRORMESSAGEPROCESSOR_BEAN);
		XStream stream = XStreamFactory
				.getXStream(MetadataUtil.METADATA_XSTREAM);
		for (FileObject fileObject : deleteList) {
			logger.logMessage(LogLevel.INFO, "正在移除error文件[{0}]",
					fileObject.getAbsolutePath());
			ErrorMessages errorMessages = (ErrorMessages) caches.get(fileObject
					.getAbsolutePath());
			if (errorMessages != null) {
				errorMessageProcessor.removeErrorMessages(errorMessages);
				caches.remove(fileObject.getAbsolutePath());
			}
			logger.logMessage(LogLevel.INFO, "移除error文件[{0}]结束",
					fileObject.getAbsolutePath());
		}
		for (FileObject fileObject : changeList) {
			logger.logMessage(LogLevel.INFO, "正在加载error文件[{0}]",
					fileObject.getAbsolutePath());
			ErrorMessages oldErrorMessages=(ErrorMessages)caches.get(fileObject.getAbsolutePath());
			if(oldErrorMessages!=null){
				errorMessageProcessor.removeErrorMessages(oldErrorMessages);
			}
			ErrorMessages errorMessages = (ErrorMessages) stream
					.fromXML(fileObject.getInputStream());
			errorMessageProcessor.addErrorMessages(errorMessages);
			caches.put(fileObject.getAbsolutePath(), errorMessages);
			logger.logMessage(LogLevel.INFO, "加载error文件[{0}]结束",
					fileObject.getAbsolutePath());
		}
	}

}
