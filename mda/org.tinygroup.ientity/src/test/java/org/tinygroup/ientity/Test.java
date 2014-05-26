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
package org.tinygroup.ientity;

import java.io.File;

import org.tinygroup.entity.entitymodel.EntityModel;
import org.tinygroup.imda.ModelManager;
import org.tinygroup.springutil.SpringUtil;

import com.thoughtworks.xstream.XStream;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ModelTestUtil.init();
		ModelManager manager = SpringUtil
				.getBean(ModelManager.MODELMANAGER_BEAN);
		XStream stream=new XStream();
		stream.processAnnotations(EntityModel.class);
		EntityModel user=(EntityModel) stream.fromXML(new File("src/main/resources/models/user.entity.xml"));
		manager.addModel(user);
	}

}
