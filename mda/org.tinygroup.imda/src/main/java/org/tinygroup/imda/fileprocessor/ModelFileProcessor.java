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
package org.tinygroup.imda.fileprocessor;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.imda.ModelLoader;
import org.tinygroup.imda.ModelManager;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.vfs.FileObject;

/**
 * 模型文件搜索器
 * 
 * @author luoguo
 * 
 */
public class ModelFileProcessor extends AbstractFileProcessor {
	private String modelExtNames;// 用逗号隔开的扩展名列表
	private List<String> extNameList = new ArrayList<String>();
	private ModelManager manager;

	public ModelManager getManager() {
		return manager;
	}

	public void setManager(ModelManager manager) {
		this.manager = manager;
	}

	public String getModelExtNames() {
		return modelExtNames;
	}

	public void setModelExtNames(String modelExtNames) {
		this.modelExtNames = modelExtNames;
		if (!StringUtil.isBlank(modelExtNames)) {
			String[] extNameArray = modelExtNames.split(",");
			for (String extName : extNameArray) {
				extNameList.add(extName);
			}
		}
	}

	public boolean isMatch(FileObject fileObject) {
		for (String fileExtName : extNameList) {
			if (fileObject.getFileName().endsWith(fileExtName)) {
				return true;
			}
		}
		return false;
	}

	public void process() {
		for (FileObject fileObject : deleteList) {
			for (ModelLoader modelLoader : manager.getModelLoaders()) {
				if (fileObject.getFileName().endsWith(
						modelLoader.getExtFileName())) {
					logger.logMessage(LogLevel.INFO, "正在移除模型文件[{0}]",
							fileObject.getAbsolutePath());
					try {
						Object model = caches.get(fileObject.getAbsolutePath());
						if (model != null) {
							manager.removeModel(model);
							caches.remove(fileObject.getAbsolutePath());
						}
					} catch (Throwable e) {
						logger.errorMessage("在移除模型文件[{0}]时，发生异常：[{1}]！", e,
								fileObject.getAbsolutePath(), e.getMessage());
					}
					logger.logMessage(LogLevel.INFO, "移除模型文件[{0}]完毕",
							fileObject.getAbsolutePath());

				}
			}

		}
		for (FileObject fileObject : changeList) {
			for (ModelLoader modelLoader : manager.getModelLoaders()) {
				if (fileObject.getFileName().endsWith(
						modelLoader.getExtFileName())) {
					logger.logMessage(LogLevel.INFO, "正在加载模型文件[{0}]",
							fileObject.getAbsolutePath());
					try {
						Object oldModel = caches.get(fileObject
								.getAbsolutePath());
						if (oldModel != null) {
							manager.removeModel(oldModel);
						}
						Object model = modelLoader.loadModel(fileObject);
						caches.put(fileObject.getAbsolutePath(), model);
						if (model != null) {
							manager.addModel(model);
						}
					} catch (Throwable e) {
						logger.errorMessage("在加载模型文件[{0}]时，发生异常：[{1}]！", e,
								fileObject.getAbsolutePath(), e.getMessage());
					}
					logger.logMessage(LogLevel.INFO, "加载模型文件[{0}]完毕",
							fileObject.getAbsolutePath());
				}
			}

		}

	}

}
