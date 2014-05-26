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
package org.tinygroup.ientity.addition;

import java.io.File;
import java.io.IOException;

import org.tinygroup.commons.file.FileDealUtil;
import org.tinygroup.entity.entitymodel.EntityModel;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;
import org.tinygroup.xstream.XStreamFactory;

import com.thoughtworks.xstream.XStream;

public final class FastModelDealer {
	private static Logger logger = LoggerFactory
			.getLogger(FastModelDealer.class);
	private String modelName;
	private boolean findFlag = true;

	public static FastModelDealer getInstance() {
		return new FastModelDealer();
	}

	private FastModelDealer() {
	}

	/**
	 * 为指定路径的模型文件或指定目录下的模型文件进行处理 为其中需要快速生成默认操作和视图的模型进行处理
	 * 
	 * @param path
	 */
	public void find(String findPath, String findModelName) {
		this.modelName = findModelName;
		FileObject file = VFS.resolveFile(findPath);
		resolve(file);
	}

	/**
	 * 对文件进行处理 判断是否是目录 并分别进行处理
	 * 
	 * @param file
	 */
	private void resolve(FileObject file) {
		if (file.isFolder()) {
			resolveFolder(file);
		} else {
			resolveFile(file);
		}
	}

	/**
	 * 对单个文件进行处理，判断是否是实体模型文件
	 * 
	 * @param file
	 */
	private void resolveFile(FileObject file) {
		String extendName = file.getFileName();
		if (extendName.endsWith(".entity.xml")) {
			try {
				dealModel(file);
			} catch (Exception e) {
				logger.errorMessage("为模型生成默认操作和视图时发生异常,文件:{0}", e,
						file.getAbsolutePath());
			}

		}
	}

	/**
	 * 对目录处理，遍历其子文件
	 * 
	 * @param folder
	 */
	private void resolveFolder(FileObject folder) {
		for (FileObject file : folder.getChildren()) {
			resolve(file);
			if (!findFlag) {
				return;
			}
		}
	}

	/**
	 * 对于模型文件进行处理，读取文件，并为模型生成默认操作和视图
	 * 
	 * @param file
	 */
	private void dealModel(FileObject file) {
		XStream stream = XStreamFactory.getXStream("entities");
		EntityModel model = null;
		model = (EntityModel) stream.fromXML(file.getInputStream());
		// 为模型生成默认操作和视图
		if (modelName == null || "".equals(modelName)) {
			dealModel(stream, file, model);
		} else if (modelName.equals(model.getName())) {
			dealModel(stream, file, model);
			findFlag = false;
		}
	}

	/**
	 * 为模型生成默认操作和视图，并写入模型文件
	 * 
	 * @param stream
	 * @param file
	 * @param model
	 */
	private void dealModel(XStream stream, FileObject file, EntityModel model) {
		ModelAddition.createDefaultInfo(model);
		try {
			if (!file.isInPackage()) {
				logger.logMessage(LogLevel.INFO, "写模型{0},文件{1}",
						model.getName(), file.getAbsolutePath());
				FileDealUtil.write(
						new File(file.getAbsolutePath().replace("file:", "")),
						stream.toXML(model));
			}
		} catch (IOException e) {
			logger.errorMessage("写文件时发生错误，文件名:{0}", e, file.getAbsolutePath());
		}
	}
}
