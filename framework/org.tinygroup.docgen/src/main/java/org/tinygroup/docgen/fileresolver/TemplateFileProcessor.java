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
package org.tinygroup.docgen.fileresolver;

import org.tinygroup.docgen.DocumentGenerater;
import org.tinygroup.docgen.DocumentGeneraterManager;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.vfs.FileObject;

/**
 * 模板文件扫描器
 * 
 * @author luoguo
 * 
 */
public class TemplateFileProcessor extends AbstractFileProcessor {
	/**
	 * 扫描文件的后缀名，由bean注入
	 */
	private String fileExtName;
	/**
	 * 文档类型，由bean注入
	 */
	private String documentType;
	
	private DocumentGenerater generate;

	public String getFileExtName() {
		return fileExtName;
	}

	public void setFileExtName(String fileExtName) {
		this.fileExtName = fileExtName;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String docType) {
		this.documentType = docType;
	}
	
	public DocumentGenerater getGenerate() {
		return generate;
	}

	public void setGenerate(DocumentGenerater generate) {
		this.generate = generate;
	}

	public boolean isMatch(FileObject fileObject) {
		return fileObject.getFileName().endsWith(fileExtName);
	}

	public void process() {
		for (FileObject fileObject : deleteList) {
			logger.logMessage(LogLevel.INFO, "正在移除文档模板宏配置文件[{0}]",
					fileObject.getAbsolutePath());
			generate.removeMacroFile(fileObject);
			caches.remove(fileObject.getAbsolutePath());
			logger.logMessage(LogLevel.INFO, "移除文档模板宏配置文件[{0}]结束",
					fileObject.getAbsolutePath());
		}
		for (FileObject fileObject : changeList) {
			logger.logMessage(LogLevel.INFO, "正在加载文档模板宏配置文件[{0}]",
					fileObject.getAbsolutePath());
			FileObject oldFileObject=(FileObject) caches.get(fileObject.getAbsolutePath());
			if(oldFileObject!=null){
				generate.removeMacroFile(oldFileObject);
			}
			generate.addMacroFile(fileObject);
			caches.put(fileObject.getAbsolutePath(), fileObject);
			logger.logMessage(LogLevel.INFO, "加载文档模板宏配置文件[{0}]结束",
					fileObject.getAbsolutePath());
		}
		DocumentGeneraterManager manager = SpringUtil
				.getBean(DocumentGeneraterManager.MANAGER_BEAN_NAME);
		manager.putDocumentGenerater(documentType, generate);
	}

}
