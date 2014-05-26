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
package org.tinygroup.entity.engine.relation.view;

import org.tinygroup.context.Context;
import org.tinygroup.context.util.ContextFactory;
import org.tinygroup.entity.AbstractRelationModelParameterBuilder;
import org.tinygroup.entity.RelationProcessor;
import org.tinygroup.entity.common.View;
import org.tinygroup.entity.relationmodel.RelationModel;
import org.tinygroup.entity.util.ModelUtil;
import org.tinygroup.imda.tinyprocessor.ModelRequestInfo;

public class RelationViewParameterBuilder extends
		AbstractRelationModelParameterBuilder {

	
	protected Context buildParameter(RelationModel model,
			ModelRequestInfo modelRequestInfo, Context context) {

		View view = ModelUtil.getView(model, modelRequestInfo.getProcessorId());
		RelationProcessor processor = new RelationProcessor(
				model, view, context);
		Context newContext = ContextFactory.getContext();
		processor.buildParameter(newContext, view);
		newContext.put(PAGE_SIZE, context.get(PAGE_SIZE));
		newContext.put(PAGE_NUMBER, context.get(PAGE_NUMBER));
		newContext.put(ORDER_BY_FIELD, context.get(ORDER_BY_FIELD));
		newContext.put(SORT_DIRECTION, context.get(SORT_DIRECTION));
		newContext.put(GROUP_BY_FIELD, context.get(GROUP_BY_FIELD));
		return newContext;

	}
}
