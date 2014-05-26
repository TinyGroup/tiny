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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.commons.i18n.LocaleUtil;
import org.tinygroup.fileresolver.FullContextFileRepository;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;

/**
 * 
 * 功能说明: 全路径静态资源文件搜索的实现类，保存搜索的静态资源文件信息
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-2-22 <br>
 * <br>
 */
public class FullContextFileRepositoryImpl implements FullContextFileRepository {
	private static final String SLASH = "\\";
	private static final String BACKSLASH = "/";
	private static final Logger logger = LoggerFactory
			.getLogger(FullContextFileRepositoryImpl.class);
	// key值为 类型:路径
	private Map<String, FileObject> fileMap = new HashMap<String, FileObject>();
	private Map<String, String> fileTypeMap;
	List<String> searchPathList = new ArrayList<String>();

	// String searchPath;

	public void addSearchPath(String searchPath) {

		searchPathList.add(searchPath);
		FileObject fileObject = VFS.resolveFile(searchPath);
		addFileObject(fileObject);

	}

	private void addFileObject(FileObject fileObject) {

		if (fileObject.isFolder()) {
			if (fileObject.getChildren() != null) {
				for (FileObject child : fileObject.getChildren()) {
					addFileObject(child);
				}
			}
		} else {
			addFileObject(fileObject.getPath(), fileObject);
		}
	}

	public void addFileObject(String path, FileObject fileObject) {
		logger.logMessage(LogLevel.DEBUG, "添加文件：{}-[{}]", path,
				fileObject.getAbsolutePath());
		fileMap.put(path, fileObject);
	}

	public FileObject getFileObject(String path) {
		FileObject fileObject = fileMap.get(path);
		if (fileObject == null && searchPathList.size() > 0) {
			for (String searchPath : searchPathList) {
				fileObject = VFS.resolveFile(String.format("%s%s", searchPath,
						path.replaceAll(BACKSLASH, SLASH + File.separator)));
				if (fileObject.isExist()) {
					addFileObject(path, fileObject);
					break;
				}
			}
		}
		return fileObject;
	}

	public void removeFileObject(String path) {
		fileMap.remove(path);
	}

	public void setFileTypeMap(Map<String, String> fileTypeMap) {
		this.fileTypeMap = fileTypeMap;

	}

	public String getFileContentType(String extName) {
		return fileTypeMap.get(extName);
	}

	public FileObject getRootFileObject(String path) {

		String fullPath = getFileObject(path).getAbsolutePath();
		return VFS.resolveFile(fullPath.substring(0,
				fullPath.length() - path.length() + 1));
	}

	public FileObject getFileObjectDetectLocale(String path) {
		StringBuffer sb = new StringBuffer();
		sb.append(path.substring(0, path.lastIndexOf('.')));
		sb.append(".");
		String locale = LocaleUtil.getContext().getLocale().toString();
		sb.append(locale);
		sb.append(path.substring(path.lastIndexOf('.')));
		FileObject fileObject = getFileObject(sb.toString());
		if (fileObject != null&&fileObject.isExist()) {
			logger.logMessage(LogLevel.DEBUG, "找到并使用[{}]的[{}]语言文件:{}",path,locale,fileObject.getPath());
			return fileObject;
		}
		return getFileObject(path);
	}

}
