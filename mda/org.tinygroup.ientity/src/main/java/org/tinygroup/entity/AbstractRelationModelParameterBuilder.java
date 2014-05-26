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
import org.tinygroup.entity.relationmodel.RelationModel;
import org.tinygroup.imda.ModelManager;
import org.tinygroup.imda.processor.ParameterBuilder;
import org.tinygroup.imda.tinyprocessor.ModelRequestInfo;
import org.tinygroup.springutil.SpringUtil;

/**
 * 
 * 功能说明:参数组装基类 

 * 开发人员: renhui <br>
 * 开发时间: 2013-9-6 <br>
 * <br>
 */
public abstract class AbstractRelationModelParameterBuilder implements ParameterBuilder<RelationModel> {

	public Context buildParameter(ModelRequestInfo modelRequestInfo,
			Context context) {
		ModelManager modelManager=SpringUtil.getBean(ModelManager.MODELMANAGER_BEAN);
		RelationModel model=modelManager.getModel(modelRequestInfo.getModelId());
		return buildParameter(model,modelRequestInfo,context);
	}

	protected abstract Context buildParameter(RelationModel model,ModelRequestInfo modelRequestInfo, Context context);

}
