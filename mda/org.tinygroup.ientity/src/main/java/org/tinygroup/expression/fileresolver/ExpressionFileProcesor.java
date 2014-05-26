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
package org.tinygroup.expression.fileresolver;

import org.tinygroup.expression.ExpressionConfigs;
import org.tinygroup.expression.ExpressionManager;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

import com.thoughtworks.xstream.XStream;

/**
 * 
 * 功能说明: 表达式文件处理器

 * 开发人员: renhui <br>
 * 开发时间: 2013-11-14 <br>
 * <br>
 */
public class ExpressionFileProcesor extends AbstractFileProcessor {
	
	private static final String EXPRESSION_EXT_FILENAME = ".expression.xml";

	public boolean isMatch(FileObject fileObject) {
		return fileObject.getFileName().endsWith(EXPRESSION_EXT_FILENAME);
	}

	public void process() {
	
		XStream stream = XStreamFactory.getXStream("entities");
		ExpressionManager manager=SpringUtil.getBean(ExpressionManager.MANAGER_BEAN_NAME);
		for (FileObject fileObject : deleteList) {
			logger.logMessage(LogLevel.DEBUG, "正在移除表达式配置文件[{0}]",
					fileObject.getAbsolutePath());
			ExpressionConfigs expressions=(ExpressionConfigs)caches.get(fileObject.getAbsolutePath());
			if(expressions!=null){
				manager.removeExpressions(expressions);
				caches.remove(fileObject.getAbsolutePath());
			}
			logger.logMessage(LogLevel.DEBUG, "移除表达式配置文件[{0}]结束",
					fileObject.getAbsolutePath());
		}
		for (FileObject fileObject : changeList) {
			logger.logMessage(LogLevel.DEBUG, "正在加载表达式配置文件[{0}]",
					fileObject.getAbsolutePath());
			ExpressionConfigs oldExpressionConfigs=(ExpressionConfigs)caches.get(fileObject.getAbsolutePath());
			if(oldExpressionConfigs!=null){
				manager.removeExpressions(oldExpressionConfigs);
			}
			ExpressionConfigs expressions=(ExpressionConfigs) stream.fromXML(fileObject.getInputStream());
			manager.addExpressions(expressions);
			caches.put(fileObject.getAbsolutePath(), expressions);
			logger.logMessage(LogLevel.DEBUG, "加载表达式配置文件[{0}]结束",
					fileObject.getAbsolutePath());
		}

	}

}
