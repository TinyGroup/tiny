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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.fileresolver.FileProcessor;
import org.tinygroup.fileresolver.FileResolver;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * 文件处理器的抽象实现
 * 
 * @author renhui
 * 
 */
public abstract class AbstractFileProcessor implements FileProcessor {

	protected List<FileObject> fileObjects = new ArrayList<FileObject>();
	
	protected List<FileObject> changeList=new ArrayList<FileObject>();
	
	protected List<FileObject> deleteList=new ArrayList<FileObject>();

	protected static Logger logger = LoggerFactory
			.getLogger(AbstractFileProcessor.class);

	protected FileResolver fileResolver;

	protected XmlNode applicationConfig;

	protected XmlNode componentConfig;

	protected Map<String, Object> caches=new HashMap<String, Object>();
	
	public FileResolver getFileResolver() {
		return fileResolver;
	}

	public void setFileResolver(FileResolver fileResolver) {
		this.fileResolver = fileResolver;
	}

	public void add(FileObject fileObject) {
		fileObjects.add(fileObject);
		changeList.add(fileObject);
	}

	public void noChange(FileObject fileObject) {

	}

	public void modify(FileObject fileObject) {
		fileObjects.add(fileObject);
		changeList.add(fileObject);
	}

	public void delete(FileObject fileObject) {
		fileObjects.remove(fileObject);
		deleteList.add(fileObject);
	}

	public void clean() {
		fileObjects.clear();
		changeList.clear();
		deleteList.clear();
	}

	public boolean supportRefresh() {
		return true;
	}

	public String getApplicationNodePath() {
		return null;
	}

	public String getComponentConfigPath() {
		return null;
	}

	public void config(XmlNode applicationConfig, XmlNode componentConfig) {
		this.applicationConfig = applicationConfig;
		this.componentConfig = componentConfig;
	}

	public XmlNode getComponentConfig() {
		return componentConfig;
	}

	public XmlNode getApplicationConfig() {
		return applicationConfig;
	}

	public int getOrder() {
		return DEFAULT_PRECEDENCE;
	}
}
