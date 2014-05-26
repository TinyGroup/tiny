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
package org.tinygroup.entity.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.entity.BaseModel;
import org.tinygroup.entity.base.BaseObject;
import org.tinygroup.entity.common.ConditionField;
import org.tinygroup.entity.common.Field;
import org.tinygroup.entity.common.Operation;
import org.tinygroup.entity.common.View;
import org.tinygroup.entity.entitymodel.EntityModel;
import org.tinygroup.imda.ModelInformationGetter;
import org.tinygroup.imda.config.CustomizeStageConfig;
import org.tinygroup.imda.validate.ValidateRule;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

public class ModelInfoGetter implements ModelInformationGetter<BaseModel> {
	static final Logger logger = LoggerFactory.getLogger(ModelInfoGetter.class);

	public String getId(BaseModel model) {
		return model.getId();
	}

	public String getName(BaseModel model) {
		return model.getName();
	}

	public String getCategory(BaseModel model) {
		return null;
	}

	public String getTitle(BaseModel model) {
		return model.getTitle();
	}

	public String getDescription(BaseModel model) {
		return model.getDescription();
	}

	public Object getOperation(BaseModel model, String processId) {
		for (Operation operation : model.getOperations()) {
			if (operation.getId().equals(processId)) {
				return operation;
			}
		}
		for (View view : model.getViews()) {
			if (view.getId().equals(processId)) {
				return view;
			}
		}
		throw new RuntimeException("在模型" + model.getTitle() + "中找不到ID为"
				+ processId + "操作或视图");
	}

	public String getOperationType(BaseModel model, String processId) {
		for (Operation operation : model.getOperations()) {
			if (operation.getId().equals(processId)) {
				return operation.getType();
			}
		}
		for (View view : model.getViews()) {
			if (view.getId().equals(processId)) {
				return view.getType();
			}
		}
		throw new RuntimeException("在模型" + model.getTitle() + "中找不到ID为"
				+ processId + "操作或视图");
	}

	public String getOperationId(Object operation) {
		BaseObject baseObject = (BaseObject) operation;
		return baseObject.getId();
	}

	public List<String> getParamterList(BaseModel model, Object operationObject) {
		EntityModel entityModel = (EntityModel) model;
		List<ConditionField> conditionFields = null;
		if (operationObject instanceof Operation) {
			Operation operation = (Operation) operationObject;
			conditionFields = operation.getConditionFields();
		} else {// View
			View view = (View) operationObject;
			conditionFields = view.getConditionFields();
		}
		List<String> result = new ArrayList<String>();
		for (ConditionField conditionField : conditionFields) {
			if (!conditionField.isHidden()) {
				String camelName = entityModel.getCamelName(entityModel
						.getStandardField(conditionField.getFieldId())
						.getName());
				result.add(camelName);
			}
		}
		return result;
	}

	public CustomizeStageConfig getCustomizeStageConfig(BaseModel model,
			String processId, String stageName) {
		for (Operation operation : model.getOperations()) {
			if (operation.getId().equals(processId)) {
				for (CustomizeStageConfig customizeConfig : operation
						.getCustomizeStageConfigs()) {
					if (customizeConfig.getStageName().equals(stageName)) {
						return customizeConfig;
					}
				}
			}
		}
		for (View view : model.getViews()) {
			if (view.getId().equals(processId)) {
				for (CustomizeStageConfig customizeConfig : view
						.getCustomizeStageConfigs()) {
					if (customizeConfig.getStageName().equals(stageName)) {
						return customizeConfig;
					}
				}
			}
		}
		return null;
	}

	public Map<String, List<ValidateRule>> getOperationValidateMap(
			BaseModel model, Object operation) {
		Map<String, List<ValidateRule>> map = new HashMap<String, List<ValidateRule>>();
		if (operation instanceof Operation && model instanceof EntityModel) {
			Operation operationInstance = (Operation) operation;
			EntityModel entityModel = (EntityModel) model;
			List<Field> operationFields = entityModel
					.getOperationFields(operationInstance);
			for (Field field : operationFields) {
				if (field.getValidateRules() != null
						&& field.getValidateRules().size() > 0) {
					map.put(entityModel.getCamelName(entityModel
							.getStandardField(field.getId()).getName()), field
							.getValidateRules());
				}
			}
		}
		return map;
	}

}
