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
package org.tinygroup.entity;

import org.tinygroup.context.Context;
import org.tinygroup.entity.common.Operation;
import org.tinygroup.entity.common.View;
import org.tinygroup.entity.entitymodel.EntityModel;
import org.tinygroup.entity.util.ModelUtil;
import org.tinygroup.imda.ModelManager;
import org.tinygroup.imda.processor.ModelServiceProcessor;
import org.tinygroup.imda.tinyprocessor.ModelRequestInfo;
import org.tinygroup.springutil.SpringUtil;

/**
 * 
 * 功能说明:模型实体服务处理的抽象类 

 * 开发人员: renhui <br>
 * 开发时间: 2013-9-6 <br>
 * <br>
 */
public abstract  class AbstractEntityModelServiceProcessor<R> implements
		ModelServiceProcessor<EntityModel,R> {


	public R process(ModelRequestInfo modelRequestInfo, Context context) {
		ModelManager modelManager=SpringUtil.getBean(ModelManager.MODELMANAGER_BEAN);
		EntityModel model=modelManager.getModel(modelRequestInfo.getModelId());
		Operation operation=ModelUtil.getOperation(model, modelRequestInfo.getProcessorId());
		if(operation!=null){
			return process(context, model, operation);
		}
		View view=ModelUtil.getView(model, modelRequestInfo.getProcessorId());
		if(view!=null){
			return process(context, model, view);
		}
		return null;
	}

	/**
	 * 
	 * 具体的操作服务处理逻辑
	 * @param context
	 * @param entityModel
	 * @param operation
	 * @return
	 */
	protected abstract R process(Context context, EntityModel entityModel,
			Operation operation);
	
	/**
	 * 
	 * 具体的视图服务处理逻辑
	 * @param context
	 * @param entityModel
	 * @param operation
	 * @return
	 */
	protected abstract R process(Context context, EntityModel entityModel,
			View view);

}
