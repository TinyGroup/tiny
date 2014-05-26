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
package org.tinygroup.mongodb.engine.view.nestedarrayquery;

import org.tinygroup.context.Context;
import org.tinygroup.imda.tinyprocessor.ModelRequestInfo;
import org.tinygroup.mongodb.engine.view.AbstractViewParamterBuilder;
import org.tinygroup.mongodb.model.MongoDBModel;

/**
 * 
 * 功能说明: 查询对象嵌套数组的参数组装
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-11-28 <br>
 * <br>
 */
public class NestedArrayQueryParamterBuilder extends AbstractViewParamterBuilder {

	
	protected Context buildParameter(MongoDBModel model,
			ModelRequestInfo modelRequestInfo, Context context) {
		 Context newContext= super.buildParameter(model, modelRequestInfo, context);
		 newContext.put(PAGE_SIZE, context.get(PAGE_SIZE));
		 newContext.put(PAGE_NUMBER, context.get(PAGE_NUMBER));
		 newContext.put(ORDER_BY_FIELD, context.get(ORDER_BY_FIELD));
		 newContext.put(SORT_DIRECTION, context.get(SORT_DIRECTION));
		 newContext.put(GROUP_BY_FIELD, context.get(GROUP_BY_FIELD));
		 return newContext;
	}

}
