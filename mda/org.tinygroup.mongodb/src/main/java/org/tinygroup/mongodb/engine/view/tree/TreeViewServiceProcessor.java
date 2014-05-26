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
package org.tinygroup.mongodb.engine.view.tree;

import org.tinygroup.mongodb.engine.WijtreeNode;
import org.tinygroup.mongodb.engine.view.AbstractViewServiceProcessor;
import org.tinygroup.mongodb.engine.view.MongoViewContext;

/**
 * 
 * 功能说明:树查询服务处理 

 * 开发人员: renhui <br>
 * 开发时间: 2013-11-28 <br>
 * <br>
 */
public class TreeViewServiceProcessor extends AbstractViewServiceProcessor<WijtreeNode[]> {

	
	protected WijtreeNode[] serviceProcessor(MongoViewContext serviceProcessor) {
		return serviceProcessor.queryMongoModelOfTree(WijtreeNode.class);
	}

}
