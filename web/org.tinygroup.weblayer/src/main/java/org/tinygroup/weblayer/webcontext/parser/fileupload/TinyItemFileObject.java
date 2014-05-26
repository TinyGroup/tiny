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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.SchemaProvider;
import org.tinygroup.vfs.impl.AbstractFileObject;
import org.tinygroup.weblayer.webcontext.parser.impl.ItemFileObject;

/**
 * 
 * 功能说明:tinyfileitem格式的文件对象 

 * 开发人员: renhui <br>
 * 开发时间: 2014-1-3 <br>
 * <br>
 */
public class TinyItemFileObject extends AbstractFileObject implements ItemFileObject {
	
	private TinyFileItem fileItem;

	public TinyItemFileObject(TinyFileItem fileItem) {
		super(null);
		this.fileItem = fileItem;
	}

	public String getFileName() {
		return fileItem.getFieldName();
	}

	public String getPath() {
		return "/";
	}

	public String getAbsolutePath() {
		return null;
	}

	public String getExtName() {
		return null;
	}

	public boolean isExist() {
		return fileItem.getSize()>0;
	}

	public long getSize() {
		return fileItem.getSize();
	}

	public InputStream getInputStream() {
		try {
			return fileItem.getInputStream();
		} catch (IOException e) {
			throw new RuntimeException("获取上传文件输入流出错",e);
		}
	}

	public boolean isFolder() {
		return false;
	}


	public List<FileObject> getChildren() {
		return null;
	}

	public FileObject getChild(String fileName) {
		return null;
	}

	public long getLastModifiedTime() {
		return 0;
	}


	public boolean isInPackage() {
		return false;
	}

	public URL getURL() {
		return null;
	}

	public OutputStream getOutputStream() {
		try {
			return fileItem.getOutputStream();
		} catch (IOException e) {
			throw new RuntimeException("获取上传文件输出流出错" ,e);
		}
	}

	public FileItem getFileItem() {
		return fileItem;
	}

}
