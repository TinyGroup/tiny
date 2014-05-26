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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.config.Configuration;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.fileresolver.FileResolver;
import org.tinygroup.fileresolver.FullContextFileRepository;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * 全路径资源提供器实现类
 * 
 * @author luoguo
 * 
 */
public class FullContextFileFinder extends AbstractFileProcessor implements
		Configuration {
	private static final String FILE = "file";
	private static final String CONTENT_TYPE = "content-type";
	private static final String EXT_NAME = "ext-name";
	// application.xml full-context-file-finder节点名
	private static final String FULL_CONTEXT_FILE_FINDER_PATH = "/application/file-resolver-configuration/full-context-file-finder";

	// 保存文件类型列表
	private Map<String, String> extFileContentTypeMap = new HashMap<String, String>();
	FullContextFileRepository fullContextFileRepository;

	public FullContextFileRepository getFullContextFileRepository() {
		return fullContextFileRepository;
	}

	public void setFullContextFileRepository(
			FullContextFileRepository fullContextFileRepository) {
		this.fullContextFileRepository = fullContextFileRepository;
	}

	public boolean isMatch(FileObject fileObject) {
		if (extFileContentTypeMap.containsKey(fileObject.getExtName())) {
			return true;
		}
		return false;
	}

	private void process(FileObject fileObject) {
		fullContextFileRepository.addFileObject(fileObject.getPath(),
				fileObject);
	}

	public void process() {
		fullContextFileRepository.setFileTypeMap(extFileContentTypeMap);
		for (FileObject fileObject : deleteList) {
			fullContextFileRepository.removeFileObject(fileObject.getPath());
		}
		for (FileObject fileObject : changeList) {
			process(fileObject);
		}
	}

	
	public void config(XmlNode applicationConfig, XmlNode componentConfig) {
		super.config(applicationConfig, componentConfig);
		List<XmlNode> fileNodes = ConfigurationUtil.combineSubList(FILE,
				applicationConfig, componentConfig);
		for (XmlNode fileNode : fileNodes) {
			String extName = fileNode.getAttribute(EXT_NAME);
			String contentType = fileNode.getAttribute(CONTENT_TYPE);
			extFileContentTypeMap.put(extName, contentType);
		}
	}

	public void setFileResolver(FileResolver fileResolver) {
	}

	public String getApplicationNodePath() {
		return FULL_CONTEXT_FILE_FINDER_PATH;
	}

	
	public String getComponentConfigPath() {
		return "/filefind.config.xml";
	}
	
}
