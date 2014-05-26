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
package org.tinygroup.entity.engine.relation;

import org.tinygroup.entity.engine.entity.EntityModelLoader;
import org.tinygroup.imda.ModelLoader;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

import com.thoughtworks.xstream.XStream;

public class RelationModelLoader implements ModelLoader{
	private static Logger logger = LoggerFactory.getLogger(EntityModelLoader.class);
	public static int LoaderValue = 5;
	public String getExtFileName() {
		return ".relationmodel.xml";
	}

	public Object loadModel(FileObject fileObject) {
		logger.logMessage(LogLevel.INFO, "正在加载关系模型文件<{}>",fileObject.getAbsolutePath());
		XStream xstream =XStreamFactory.getXStream("entities");
		Object object= xstream.fromXML(fileObject.getInputStream());
		logger.logMessage(LogLevel.INFO, "关系模型文件<{}>加载完毕。",fileObject.getAbsolutePath());
		return object;
	}

}
