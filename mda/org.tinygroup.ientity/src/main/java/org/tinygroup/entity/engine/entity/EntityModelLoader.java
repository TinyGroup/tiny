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
package org.tinygroup.entity.engine.entity;

import org.tinygroup.database.config.table.Table;
import org.tinygroup.database.table.TableProcessor;
import org.tinygroup.entity.entitymodel.EntityModel;
import org.tinygroup.imda.ModelLoader;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

import com.thoughtworks.xstream.XStream;

public class EntityModelLoader implements ModelLoader {
	private static Logger logger = LoggerFactory
			.getLogger(EntityModelLoader.class);
	public static int LoaderValue = 5;
	private EntityModelToTable entityModelToTable;

	private TableProcessor tableProcessor;

	public String getExtFileName() {
		return ".entity.xml";
	}

	public Object loadModel(FileObject fileObject) {
		logger.logMessage(LogLevel.INFO, "正在加载实体模型文件<{}>",
				fileObject.getAbsolutePath());
		XStream xstream = XStreamFactory.getXStream("entities");
		EntityModel entityModel = (EntityModel) xstream.fromXML(fileObject
				.getInputStream());
		addTableWithModel(entityModel);
		logger.logMessage(LogLevel.INFO, "实体模型文件<{}>加载完毕。",
				fileObject.getAbsolutePath());
		return entityModel;
	}

	public void addTableWithModel(EntityModel model) {
		if (tableProcessor == null) {
			tableProcessor = SpringUtil
					.getBean(TableProcessor.BEAN_NAME);
		}
		if (entityModelToTable == null) {
			entityModelToTable = SpringUtil
					.getBean(EntityModelToTable.MODEL_TO_TABLE_BEAN_NAME);
		}
		Table table = entityModelToTable.model2Table(model);
		if (table != null) {
			tableProcessor.addTable(table);
		}

	}
}
