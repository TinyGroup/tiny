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
package org.tinygroup.pageflow.fileresolver;

import org.tinygroup.fileresolver.FileResolver;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.flow.FlowExecutor;
import org.tinygroup.flow.config.ComponentDefines;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

import com.thoughtworks.xstream.XStream;

/**
 * 扫描组件的文件处理器
 * 
 * @author renhui
 * 
 */
public class PageFlowComponentProcessor extends AbstractFileProcessor {

	/**
	 * 扫描的文件后缀
	 */
	private static final String PAGE_FLOW_COMPONENT_EXT_FILENAME = ".pagefc.xml";

	public boolean isMatch(FileObject fileObject) {
		return fileObject.getFileName().endsWith(
				PAGE_FLOW_COMPONENT_EXT_FILENAME);
	}

	public void process() {
		FlowExecutor flowExecutor = SpringUtil
				.getBean(FlowExecutor.PAGE_FLOW_BEAN);
		XStream stream = XStreamFactory
				.getXStream(FlowExecutor.FLOW_XSTREAM_PACKAGENAME);
		for (FileObject fileObject : deleteList) {
			logger.logMessage(LogLevel.INFO, "正在删除页面组件fc文件[{0}]",
					fileObject.getAbsolutePath());
			ComponentDefines components = (ComponentDefines) caches
					.get(fileObject.getAbsolutePath());
			if (components != null) {
				flowExecutor.removeComponents(components);
				caches.remove(fileObject.getAbsolutePath());
			}
			logger.logMessage(LogLevel.INFO, "删除页面组件fc文件[{0}]结束",
					fileObject.getAbsolutePath());
		}
		for (FileObject fileObject : changeList) {
			logger.logMessage(LogLevel.INFO, "正在读取页面组件pagefc文件[{0}]",
					fileObject.getAbsolutePath());
			ComponentDefines oldComponents = (ComponentDefines) caches
					.get(fileObject.getAbsolutePath());
			if (oldComponents != null) {
				flowExecutor.removeComponents(oldComponents);
			}
			ComponentDefines components = (ComponentDefines) stream
					.fromXML(fileObject.getInputStream());
			flowExecutor.addComponents(components);
			caches.put(fileObject.getAbsolutePath(), components);
			logger.logMessage(LogLevel.INFO, "读取页面组件pagefc文件[{0}]结束",
					fileObject.getAbsolutePath());
		}

	}

	public void setFileResolver(FileResolver fileResolver) {

	}

}
