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
package org.tinygroup.entity.engine.entity.operation;

import org.tinygroup.context.Context;
import org.tinygroup.context.util.ContextFactory;
import org.tinygroup.entity.AbstractEntityModelParameterBuilder;
import org.tinygroup.entity.OperationParamterBuilder;
import org.tinygroup.entity.common.Operation;
import org.tinygroup.entity.entitymodel.EntityModel;
import org.tinygroup.entity.util.ModelUtil;
import org.tinygroup.imda.tinyprocessor.ModelRequestInfo;

/**
 * 
 * 功能说明: 操作字段和条件字段参数组装

 * 开发人员: renhui <br>
 * 开发时间: 2013-9-4 <br>
 * <br>
 */
public abstract class AbstractParameterBuilder  extends AbstractEntityModelParameterBuilder{


	
	protected Context buildParameter(EntityModel model,
			ModelRequestInfo modelRequestInfo, Context context) {
		Operation operation=ModelUtil.getOperation(model, modelRequestInfo.getProcessorId());
		OperationParamterBuilder builder=new OperationParamterBuilder(context, model, operation);
		return builder.buildParameter(ContextFactory.getContext(),operation.getConditionFields());
	}
	
	

}