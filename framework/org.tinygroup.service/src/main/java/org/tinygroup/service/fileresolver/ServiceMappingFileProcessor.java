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
package org.tinygroup.service.fileresolver;

import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.service.ServiceMappingManager;
import org.tinygroup.service.config.ServiceViewMappings;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

import com.thoughtworks.xstream.XStream;

public class ServiceMappingFileProcessor extends AbstractFileProcessor {

	private static final String SERVICEMAPPING_EXT_FILENAMES = ".servicemapping.xml";

	public boolean isMatch(FileObject fileObject) {
		return fileObject.getFileName().endsWith(SERVICEMAPPING_EXT_FILENAMES);
	}

	public void process() {
		ServiceMappingManager manager = SpringUtil
				.getBean(ServiceMappingManager.MANAGER_BEAN);
		XStream stream = XStreamFactory
				.getXStream(ServiceMappingManager.XSTREAM_PACKAGE_NAME);
		for (FileObject fileObject : deleteList) {
			logger.logMessage(LogLevel.INFO, "正在ServiceMappings文件[{0}]",
					fileObject.getAbsolutePath());
			ServiceViewMappings mappings = (ServiceViewMappings) caches
					.get(fileObject.getAbsolutePath());
			if (mappings != null) {
				manager.removeServiceMappings(mappings);
				caches.remove(fileObject.getAbsolutePath());
			}
			logger.logMessage(LogLevel.INFO, "读取ServiceMappings文件[{0}]结束",
					fileObject.getAbsolutePath());
		}
		for (FileObject fileObject : changeList) {
			logger.logMessage(LogLevel.INFO, "正在ServiceMappings文件[{0}]",
					fileObject.getAbsolutePath());
			ServiceViewMappings oldMappings = (ServiceViewMappings) caches
					.get(fileObject.getAbsolutePath());
			if (oldMappings != null) {
				manager.removeServiceMappings(oldMappings);
			}
			ServiceViewMappings mappings = (ServiceViewMappings) stream
					.fromXML(fileObject.getInputStream());
			manager.addServiceMappings(mappings);
			caches.put(fileObject.getAbsolutePath(), mappings);
			logger.logMessage(LogLevel.INFO, "读取ServiceMappings文件[{0}]结束",
					fileObject.getAbsolutePath());
		}
	}

}
