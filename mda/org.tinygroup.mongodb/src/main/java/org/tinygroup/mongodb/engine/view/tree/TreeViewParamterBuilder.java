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

import org.tinygroup.context.Context;
import org.tinygroup.imda.tinyprocessor.ModelRequestInfo;
import org.tinygroup.mongodb.engine.view.AbstractViewParamterBuilder;
import org.tinygroup.mongodb.model.MongoDBModel;

/**
 * 
 * 功能说明:树形结构参数组装 

 * 开发人员: renhui <br>
 * 开发时间: 2013-11-28 <br>
 * <br>
 */
public class TreeViewParamterBuilder extends AbstractViewParamterBuilder {
	public static final String QUERY_ALL_TREE="queryAllTree";

	
	protected Context buildParameter(MongoDBModel model,
			ModelRequestInfo modelRequestInfo, Context context) {
		 Context newContext= super.buildParameter(model, modelRequestInfo, context);
		 newContext.put(QUERY_ALL_TREE, context.get(QUERY_ALL_TREE));
		 return newContext;
	}
	
	
}
