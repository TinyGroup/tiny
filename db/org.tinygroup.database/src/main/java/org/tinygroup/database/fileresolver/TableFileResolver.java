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

import org.tinygroup.database.config.table.Tables;
import org.tinygroup.database.table.TableProcessor;
import org.tinygroup.database.util.DataBaseUtil;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

import com.thoughtworks.xstream.XStream;

public class TableFileResolver extends AbstractFileProcessor {

	private static final String TABLE_EXTFILENAME = ".table.xml";

	public boolean isMatch(org.tinygroup.vfs.FileObject fileObject) {
		return fileObject.getFileName().endsWith(TABLE_EXTFILENAME);
	}

	public void process() {
		TableProcessor tableProcessor = SpringUtil
				.getBean(DataBaseUtil.TABLEPROCESSOR_BEAN);
		XStream stream = XStreamFactory
				.getXStream(DataBaseUtil.DATABASE_XSTREAM);
		for (FileObject fileObject : deleteList) {
			logger.logMessage(LogLevel.INFO, "正在移除table文件[{0}]",
					fileObject.getAbsolutePath());
			Tables tables = (Tables)caches.get(fileObject.getAbsolutePath());
			if(tables!=null){
				tableProcessor.removeTables(tables);
				caches.remove(fileObject.getAbsolutePath());
			}
			logger.logMessage(LogLevel.INFO, "移除table文件[{0}]结束",
					fileObject.getAbsolutePath());
		}
		for (FileObject fileObject : changeList) {
			logger.logMessage(LogLevel.INFO, "正在加载table文件[{0}]",
					fileObject.getAbsolutePath());
			Tables oldTables = (Tables)caches.get(fileObject.getAbsolutePath());
			if(oldTables!=null){
				tableProcessor.removeTables(oldTables);
			}	
			Tables tables = (Tables) stream
					.fromXML(fileObject.getInputStream());
			tableProcessor.addTables(tables);
			caches.put(fileObject.getAbsolutePath(), tables);
			logger.logMessage(LogLevel.INFO, "加载table文件[{0}]结束",
					fileObject.getAbsolutePath());
		}
	}

}
