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
package org.tinygroup.weblayer.webcontext.parser.impl;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.tinygroup.weblayer.webcontext.parser.upload.FileUploadReName;

/**
 * 继承自commons-fileupload-1.2.1的同名类，改进了如下内容：
 * <ul>
 * <li>添加新的<code>keepFormFieldInMemory</code>参数。</li>
 * <li>创建新的DiskFileItem对象。</li>
 * </ul>
 *
 * @author Michael Zhou
 */
public class DiskFileItemFactory extends org.apache.commons.fileupload.disk.DiskFileItemFactory {
    private boolean keepFormFieldInMemory;
    
    private boolean saveInFile;
    
    private HttpServletRequest request;
    
    private FileUploadReName rename;
    

	public FileUploadReName getRename() {
		return rename;
	}

	public void setRename(FileUploadReName rename) {
		this.rename = rename;
	}

	public boolean isKeepFormFieldInMemory() {
        return keepFormFieldInMemory;
    }

    public void setKeepFormFieldInMemory(boolean keepFormFieldInMemory) {
        this.keepFormFieldInMemory = keepFormFieldInMemory;
    }
    
    
    public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	
    public FileItem createItem(String fieldName, String contentType, boolean isFormField, String fileName) {
        int sizeThreshold = getSizeThreshold();
        boolean saveInFile=isSaveInFile();
        if (isFormField && (sizeThreshold == 0 || keepFormFieldInMemory)) {
            return new InMemoryFormFieldItem(fieldName, contentType, isFormField,saveInFile ,fileName, sizeThreshold,
                                             keepFormFieldInMemory, getRepository(),request,rename);
        } else {
            return new DiskFileItem(fieldName, contentType, isFormField, saveInFile,fileName, sizeThreshold,
                                    keepFormFieldInMemory, getRepository(),request,rename);
        }
    }

	public void setSaveInFile(boolean saveInFile) {
		this.saveInFile=saveInFile;
	}

	public boolean isSaveInFile() {
		return saveInFile;
	}

	
	
}
