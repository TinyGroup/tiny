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
package org.tinygroup.mongodb.engine.view;

import org.tinygroup.context.Context;
import org.tinygroup.context.util.ContextFactory;
import org.tinygroup.imda.tinyprocessor.ModelRequestInfo;
import org.tinygroup.mongodb.common.View;
import org.tinygroup.mongodb.engine.AbstractMongoParamterBuilder;
import org.tinygroup.mongodb.engine.MongoModelInfoGetter;
import org.tinygroup.mongodb.model.MongoDBModel;
import org.tinygroup.springutil.SpringUtil;

/**
 * 
 * 功能说明:view参数组装 

 * 开发人员: renhui <br>
 * 开发时间: 2013-11-28 <br>
 * <br>
 */
public abstract class AbstractViewParamterBuilder extends AbstractMongoParamterBuilder {

	
	protected Context buildParameter(MongoDBModel model,
			ModelRequestInfo modelRequestInfo, Context context) {
		MongoModelInfoGetter getter=SpringUtil.getBean("mongoModelInfoGetter");
		View view=(View) getter.getOperation(model, modelRequestInfo.getProcessorId());
		MongoViewContext viewContext=new MongoViewContext(model, view, context);
		return viewContext.buildConditionParameter(ContextFactory.getContext(), view.getConditionFields());
	}

}
