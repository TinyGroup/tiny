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
import org.tinygroup.entity.AbstractRelationModelServiceProcessor;
import org.tinygroup.entity.PageInfo;
import org.tinygroup.entity.RelationProcessor;
import org.tinygroup.entity.common.View;
import org.tinygroup.entity.relationmodel.RelationModel;

public class RelationViewModelServiceProcessor extends AbstractRelationModelServiceProcessor<PageInfo>{

	
	protected PageInfo process(Context context, RelationModel relationModel,
			View view) {
		RelationProcessor processor=new RelationProcessor(relationModel, view, context);
		return processor.getBeansWithRelationModelView();
	}

}
