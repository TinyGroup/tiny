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
package org.tinygroup.imda.usermodel;

import org.tinygroup.context.Context;
import org.tinygroup.imda.ModelManager;
import org.tinygroup.imda.processor.ModelServiceProcessor;
import org.tinygroup.imda.tinyprocessor.ModelRequestInfo;
import org.tinygroup.springutil.SpringUtil;

public class CaseModelServiceProcessor<T> implements
		ModelServiceProcessor<CaseModel,T> {
	public T process(ModelRequestInfo modelRequestInfo, Context context) {
		ModelManager modelManager=SpringUtil.getBean("modelManager");
		CaseModel model=modelManager.getModel(modelRequestInfo.getModelId());
		context.put("model:", model.getId() + "page is:"+context.get(CaseModelParamBuilder.KEY));
		context.put(CaseModelParamBuilder.KEY,1+(Integer)context.get(CaseModelParamBuilder.KEY));
		return null;
	}
}
