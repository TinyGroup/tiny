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
package org.tinygroup.mongodb.engine.operation;

import org.tinygroup.context.Context;
import org.tinygroup.context.util.ContextFactory;
import org.tinygroup.imda.tinyprocessor.ModelRequestInfo;
import org.tinygroup.mongodb.common.Operation;
import org.tinygroup.mongodb.engine.AbstractMongoParamterBuilder;
import org.tinygroup.mongodb.engine.MongoModelInfoGetter;
import org.tinygroup.mongodb.model.MongoDBModel;
import org.tinygroup.springutil.SpringUtil;

/**
 * 
 * 功能说明: 对操作列表、条件列表进行参数组装，例如修改操作

 * 开发人员: renhui <br>
 * 开发时间: 2013-11-25 <br>
 * <br>
 */
public abstract class AbstractOperationAllParamterBuilder extends
		AbstractMongoParamterBuilder {

	
	protected Context buildParameter(MongoDBModel model,
			ModelRequestInfo modelRequestInfo, Context context) {
		MongoModelInfoGetter getter=SpringUtil.getBean("mongoModelInfoGetter");
		Operation operation=(Operation) getter.getOperation(model, modelRequestInfo.getProcessorId());
		MongoOperationContext builder=new MongoOperationContext(model, operation, context);
		return builder.buildAllParamter(ContextFactory.getContext());
	}

}
