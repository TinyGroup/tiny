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
package org.tinygroup.flow.impl;

import org.tinygroup.flow.FlowExecutor;
import org.tinygroup.flow.config.Flow;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;
import org.tinygroup.xstream.XStreamFactory;

import com.thoughtworks.xstream.XStream;

public class FlowLoader {

	private static Logger logger = LoggerFactory.getLogger(FlowLoader.class);

	/**
	 * flow文件的文件扩展名
	 */
	private static final String FLOW_FILE_EXTENSION = ".flow.xml";

	/**
	 * 为指定的FlowExecutor加载目录path下所有的flow文件
	 * 
	 * @param path
	 * @param executor
	 */
	public void load(String path, FlowExecutor executor) {
		logger.logMessage(LogLevel.DEBUG, "扫描路径{0}查找Flow文件", path);
		FileObject file = VFS.resolveFile(path);
		load(file, executor);
		logger.logMessage(LogLevel.DEBUG, "路径{0}扫描结束", path);
	}

	/**
	 * 读取文件(目录下)为flow对象
	 * 
	 * @param file
	 * @param executor
	 */
	private void load(FileObject file, FlowExecutor executor) {
		if (file.isFolder()) {
			loadDir(file, executor);
		} else {
			loadFile(file, executor);
		}
	}

	/**
	 * 读取文件为flow对象
	 * 
	 * @param file
	 * @param executor
	 */
	private void loadFile(FileObject file, FlowExecutor executor) {
		logger.logMessage(LogLevel.DEBUG, "开始扫描文件{0}", file.getAbsolutePath());
		if (file.getFileName().endsWith(FLOW_FILE_EXTENSION)) {
			XStream xStream = XStreamFactory
					.getXStream(FlowExecutor.FLOW_XSTREAM_PACKAGENAME);
			Flow flow = null;
			flow = (Flow) xStream.fromXML(file.getInputStream());
			executor.addFlow(flow);
			logger.logMessage(LogLevel.DEBUG, "添加Flow[id:{0}]", flow.getId());
		}
		logger.logMessage(LogLevel.DEBUG, "扫描文件{0}结束", file.getAbsolutePath());
	}

	/**
	 * 读取目录下的文件为flow对象
	 * 
	 * @param file
	 * @param executor
	 */
	private void loadDir(FileObject file, FlowExecutor executor) {
		logger.logMessage(LogLevel.DEBUG, "开始扫描目录{0}", file.getAbsolutePath());
		for (FileObject o : file.getChildren()) {
			load(o, executor);
		}
		logger.logMessage(LogLevel.DEBUG, "扫描目录{0}结束", file.getAbsolutePath());
	}

}
