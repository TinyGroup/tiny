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
import org.tinygroup.metadata.bizdatatype.BusinessTypeProcessor;
import org.tinygroup.metadata.config.bizdatatype.BusinessTypes;
import org.tinygroup.metadata.util.MetadataUtil;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

import com.thoughtworks.xstream.XStream;

public class BusinessTypeFileResolver extends AbstractFileProcessor {
	private static final String BIZDATATYPE_EXTFILENAME = ".bizdatatype.xml";

	public boolean isMatch(FileObject fileObject) {
		return fileObject.getFileName().endsWith(BIZDATATYPE_EXTFILENAME);
	}

	public void process() {
		BusinessTypeProcessor businessTypeProcessor = SpringUtil
				.getBean(MetadataUtil.BUSINESSTYPEPROCESSOR_BEAN);
		XStream stream = XStreamFactory
				.getXStream(MetadataUtil.METADATA_XSTREAM);
		for (FileObject fileObject : deleteList) {
			logger.logMessage(LogLevel.INFO, "正在移除bizdatatype文件[{0}]",
					fileObject.getAbsolutePath());
			BusinessTypes businessTypes = (BusinessTypes) caches.get(fileObject
					.getAbsolutePath());
			if (businessTypes != null) {
				businessTypeProcessor.removeBusinessTypes(businessTypes);
				caches.remove(fileObject.getAbsolutePath());
			}
			logger.logMessage(LogLevel.INFO, "移除bizdatatype文件[{0}]结束",
					fileObject.getAbsolutePath());
		}
		for (FileObject fileObject : changeList) {
			logger.logMessage(LogLevel.INFO, "正在加载bizdatatype文件[{0}]",
					fileObject.getAbsolutePath());
			BusinessTypes oldBusinessTypes=(BusinessTypes) caches.get(fileObject.getAbsolutePath());
			if(oldBusinessTypes!=null){
				businessTypeProcessor.removeBusinessTypes(oldBusinessTypes);
			}
			BusinessTypes businessTypes = (BusinessTypes) stream
					.fromXML(fileObject.getInputStream());
			businessTypeProcessor.addBusinessTypes(businessTypes);
			caches.put(fileObject.getAbsolutePath(), businessTypes);
			logger.logMessage(LogLevel.INFO, "加载bizdatatype文件[{0}]结束",
					fileObject.getAbsolutePath());
		}
	}

}
