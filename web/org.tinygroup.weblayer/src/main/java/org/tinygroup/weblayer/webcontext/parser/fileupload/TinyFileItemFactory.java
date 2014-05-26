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
package org.tinygroup.weblayer.webcontext.parser.fileupload;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;

/**
 * 
 * 功能说明: tiny框架内部的文件上传工厂

 * 开发人员: renhui <br>
 * 开发时间: 2014-1-3 <br>
 * <br>
 */
public class TinyFileItemFactory implements FileItemFactory {
	
	private FileItemStorage storage;
	
	public FileItem createItem(String fieldName, String contentType,
			boolean isFormField, String fileName) {
		return new TinyFileItem(fieldName, contentType, isFormField, fileName,storage);
	}


	public FileItemStorage getStorage() {
		return storage;
	}

	public void setStorage(FileItemStorage storage) {
		this.storage = storage;
	}
	
}
