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

import org.tinygroup.database.config.procedure.Procedures;
import org.tinygroup.database.procedure.ProcedureProcessor;
import org.tinygroup.database.util.DataBaseUtil;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

import com.thoughtworks.xstream.XStream;

public class ProcedureFileResolver extends AbstractFileProcessor {
	private static final String PROCEDURE_EXTFILENAME = ".procedure.xml";

	public boolean isMatch(FileObject fileObject) {
		return fileObject.getFileName().endsWith(PROCEDURE_EXTFILENAME);
	}

	public void process() {
		ProcedureProcessor procedureProcessor = SpringUtil
				.getBean(DataBaseUtil.PROCEDURE_BEAN);
		XStream stream = XStreamFactory
				.getXStream(DataBaseUtil.DATABASE_XSTREAM);
		for (FileObject fileObject : deleteList) {
			logger.logMessage(LogLevel.INFO, "正在移除procedure文件[{0}]",
					fileObject.getAbsolutePath());
			Procedures procedures = (Procedures)caches.get(fileObject.getAbsolutePath());
			if(procedures!=null){
				procedureProcessor.removeProcedures(procedures);
				caches.remove(fileObject.getAbsolutePath());
			}
			logger.logMessage(LogLevel.INFO, "移除procedure文件[{0}]结束",
					fileObject.getAbsolutePath());
		}
		for (FileObject fileObject : changeList) {
			logger.logMessage(LogLevel.INFO, "正在加载procedure文件[{0}]",
					fileObject.getAbsolutePath());
			Procedures oldProcedures = (Procedures)caches.get(fileObject.getAbsolutePath());
			if(oldProcedures!=null){
				procedureProcessor.removeProcedures(oldProcedures);
			}	
			Procedures procedures = (Procedures) stream.fromXML(fileObject
					.getInputStream());
			procedureProcessor.addProcedures(procedures);
			caches.put(fileObject.getAbsolutePath(), procedures);
			logger.logMessage(LogLevel.INFO, "加载procedure文件[{0}]结束",
					fileObject.getAbsolutePath());
		}
	}

}
