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
package org.tinygroup.mongodb.engine;

import org.tinygroup.context.Context;
import org.tinygroup.imda.ModelManager;
import org.tinygroup.imda.processor.ModelServiceProcessor;
import org.tinygroup.imda.tinyprocessor.ModelRequestInfo;
import org.tinygroup.mongodb.common.Operation;
import org.tinygroup.mongodb.common.View;
import org.tinygroup.mongodb.model.MongoDBModel;
import org.tinygroup.springutil.SpringUtil;

/**
 * 
 * 功能说明: 操作和视图抽象服务类

 * 开发人员: renhui <br>
 * 开发时间: 2013-11-28 <br>
 * <br>
 */
public abstract class AbstractMongoServiceProcessor<R> implements
		ModelServiceProcessor<MongoDBModel, R> {

	public R process(ModelRequestInfo modelRequestInfo, Context context) {
		ModelManager modelManager=SpringUtil.getBean(ModelManager.MODELMANAGER_BEAN);
		MongoDBModel model=modelManager.getModel(modelRequestInfo.getModelId());
		MongoModelInfoGetter getter=SpringUtil.getBean("mongoModelInfoGetter");
		Object operate=getter.getOperation(model, modelRequestInfo.getProcessorId());
		if(operate!=null){
			if(operate instanceof Operation){
				Operation operation=(Operation)operate;
				return process(context, model, operation);
			}
			if(operate instanceof View){
				return process(context, model, (View)operate);
			}
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
	protected abstract R process(Context context, MongoDBModel mongoDBModel,
			Operation operation);
	
	/**
	 * 
	 * 具体的视图服务处理逻辑
	 * @param context
	 * @param entityModel
	 * @param operation
	 * @return
	 */
	protected abstract R process(Context context, MongoDBModel mongoDBModel,
			View view);

}
