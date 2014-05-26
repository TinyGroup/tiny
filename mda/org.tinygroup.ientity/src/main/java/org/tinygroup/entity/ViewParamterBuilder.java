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

import java.util.List;

import org.tinygroup.context.Context;
import org.tinygroup.entity.common.ConditionField;
import org.tinygroup.entity.common.HavingField;
import org.tinygroup.entity.common.View;
import org.tinygroup.entity.entitymodel.EntityModel;

/**
 * 
 * 功能说明: 操作类参数组装
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-9-6 <br>
 * <br>
 */
public class ViewParamterBuilder extends EntityModelHelper {

	private View view;

	public ViewParamterBuilder(Context context, EntityModel model, View view) {
		super(model, context);
		this.view = view;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public Context buildConditionParameter(Context newContext,
			List<ConditionField> conditionFields) {
		super.buildConditionParameter(newContext, conditionFields);
		if (view.getHavingFields() != null) {
			for (HavingField havingField : view.getHavingFields()) {
				processHavingParamter(newContext, havingField);
			}
		}
		return newContext;
	}


}
