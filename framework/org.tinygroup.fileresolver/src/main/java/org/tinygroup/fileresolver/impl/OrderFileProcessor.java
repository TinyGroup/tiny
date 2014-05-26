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
package org.tinygroup.fileresolver.impl;

import org.tinygroup.logger.LogLevel;
import org.tinygroup.order.processor.OrderProcessor;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.vfs.FileObject;

/**
 * 
 * 功能说明:对象顺序文件的扫描器
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-5-17 <br>
 * <br>
 */
public class OrderFileProcessor extends AbstractFileProcessor {

	private static final String ORDER_FILE_NAME = ".order.xml";

	public boolean isMatch(FileObject fileObject) {
		return fileObject.getFileName().endsWith(ORDER_FILE_NAME);
	}

	public void process() {

		logger.logMessage(LogLevel.INFO, "处理对象顺序文件开始");
		OrderProcessor<?> orderProcessor = SpringUtil
				.getBean(OrderProcessor.ORDER_NAME);
		for (FileObject fileObject : changeList) {
			logger.logMessage(LogLevel.INFO, "加载对象顺序文件：[{}]",
					fileObject.getAbsolutePath());
			orderProcessor.loadOrderFile(fileObject);
			logger.logMessage(LogLevel.INFO, "加载对象顺序文件：[{}]完毕",
					fileObject.getAbsolutePath());
		}
		logger.logMessage(LogLevel.INFO, "处理对象顺序文件结束");
	}

}
