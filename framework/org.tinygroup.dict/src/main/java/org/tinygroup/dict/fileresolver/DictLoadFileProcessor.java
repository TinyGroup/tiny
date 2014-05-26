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
package org.tinygroup.dict.fileresolver;

import org.tinygroup.dict.DictLoader;
import org.tinygroup.dict.DictManager;
import org.tinygroup.dict.config.DictLoaderConfig;
import org.tinygroup.dict.config.DictLoaderConfigs;
import org.tinygroup.fileresolver.FileResolver;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

import com.thoughtworks.xstream.XStream;

/**
 * 字典加载器配置的文件搜索处理器
 * 
 * @author renhui
 * 
 */
public class DictLoadFileProcessor extends AbstractFileProcessor {

	private static final String DICT_LOAND_EXT_NAME = ".dictloader.xml";

	public boolean isMatch(FileObject fileObject) {
		return fileObject.getFileName().endsWith(DICT_LOAND_EXT_NAME);
	}

	public void process() {
		DictManager manager = SpringUtil
				.getBean(DictManager.DICT_MANAGER_BEAN_NAME);
		XStream stream = XStreamFactory
				.getXStream(DictManager.XSTEAM_PACKAGE_NAME);
		logger.logMessage(LogLevel.INFO, "字典加载器配置文件处理开始");
		for (FileObject fileObject : fileObjects) {
			logger.logMessage(LogLevel.INFO, "找到字典加载配置文件:[{}]",
					fileObject.getAbsolutePath());
			DictLoaderConfigs configs = (DictLoaderConfigs) stream
					.fromXML(fileObject.getInputStream());
			for (DictLoaderConfig config : configs.getConfigs()) {
				DictLoader dictLoader = SpringUtil
						.getBean(config.getBeanName());
				dictLoader.setGroupName(config.getGroupName());
				dictLoader.setLanguage(config.getLanguage());
				manager.addDictLoader(dictLoader);
			}
		}
//		manager.load();
		logger.logMessage(LogLevel.INFO, "字典加载器配置文件处理结束");
	}

	public void setFileResolver(FileResolver fileResolver) {

	}

}
