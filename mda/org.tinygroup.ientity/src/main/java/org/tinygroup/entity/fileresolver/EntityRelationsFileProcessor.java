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
package org.tinygroup.entity.fileresolver;

import org.tinygroup.entity.EntityRelationsManager;
import org.tinygroup.entity.relation.EntityRelations;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

import com.thoughtworks.xstream.XStream;

/**
 * 
 * 功能说明:模型引用关系文件处理器 

 * 开发人员: renhui <br>
 * 开发时间: 2013-10-25 <br>
 * <br>
 */
public class EntityRelationsFileProcessor extends AbstractFileProcessor {
	
	private static final String ENTITY_RELATIONS_FILE_EXT_NAME=".entityrelations.xml";

	
	public boolean isMatch(FileObject fileObject) {
		return fileObject.getFileName().endsWith(ENTITY_RELATIONS_FILE_EXT_NAME);
	}

	public void process() {
		
		XStream xStream = XStreamFactory.getXStream("entities");
		EntityRelationsManager manager=SpringUtil.getBean(EntityRelationsManager.MANAGER_BEAN_NAME);
		for (FileObject fileObject  : deleteList) {
			logger.logMessage(LogLevel.INFO, "正在移除EntityRelation描述文件：[{}]",
					fileObject.getAbsolutePath());
			EntityRelations entityRelations = (EntityRelations)caches.get(fileObject.getAbsolutePath());
			if(entityRelations!=null){
				manager.removeEntityRelations(entityRelations);
				caches.remove(fileObject.getAbsolutePath());
			}
			logger.logMessage(LogLevel.INFO, "EntityRelation描述文件：[{}]移除成功。",
					fileObject.getAbsolutePath());
		}
		for (FileObject fileObject : changeList) {
			logger.logMessage(LogLevel.INFO, "正在加载EntityRelation描述文件：[{}]",
					fileObject.getAbsolutePath());
			EntityRelations oldRelations=(EntityRelations)caches.get(fileObject.getAbsolutePath());
			if(oldRelations!=null){
				manager.removeEntityRelations(oldRelations);
			}
			EntityRelations entityRelations = (EntityRelations) xStream
					.fromXML(fileObject.getInputStream());
			manager.addEntityRelations(entityRelations);
			caches.put(fileObject.getAbsolutePath(), entityRelations);
			logger.logMessage(LogLevel.INFO, "EntityRelation描述文件：[{}]加载成功。",
						fileObject.getAbsolutePath());
			
		}

	}

}
