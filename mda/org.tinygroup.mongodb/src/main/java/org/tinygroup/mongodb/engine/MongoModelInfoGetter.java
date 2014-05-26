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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.imda.ModelInformationGetter;
import org.tinygroup.imda.config.CustomizeStageConfig;
import org.tinygroup.imda.validate.ValidateRule;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.mongodb.common.BaseObject;
import org.tinygroup.mongodb.common.ConditionField;
import org.tinygroup.mongodb.common.Field;
import org.tinygroup.mongodb.common.Operation;
import org.tinygroup.mongodb.common.View;
import org.tinygroup.mongodb.model.MongoDBModel;

public class MongoModelInfoGetter implements ModelInformationGetter<MongoDBModel> {
	static final Logger logger = LoggerFactory.getLogger(MongoModelInfoGetter.class);

	public String getId(MongoDBModel model) {
		return model.getId();
	}

	public String getName(MongoDBModel model) {
		return model.getName();
	}

	public String getCategory(MongoDBModel model) {
		return null;
	}

	public String getTitle(MongoDBModel model) {
		return model.getTitle();
	}

	public String getDescription(MongoDBModel model) {
		return model.getDescription();
	}

	public Object getOperation(MongoDBModel model, String operationId) {
		for (Operation operation : model.getOperations()) {
			if (operation.getId().equals(operationId)) {
				return operation;
			}
		}
		for (View view : model.getViews()) {
			if (view.getId().equals(operationId)) {
				return view;
			}
		}
		throw new RuntimeException("在模型" + model.getTitle() + "中找不到ID为"
				+ operationId + "操作或视图");
	}

	public String getOperationType(MongoDBModel model, String operationId) {
		for (Operation operation : model.getOperations()) {
			if (operation.getId().equals(operationId)) {
				return operation.getType();
			}
		}
		for (View view : model.getViews()) {
			if (view.getId().equals(operationId)) {
				return view.getType();
			}
		}
		throw new RuntimeException("在模型" + model.getTitle() + "中找不到ID为"
				+ operationId + "操作或视图");
	}

	public CustomizeStageConfig getCustomizeStageConfig(MongoDBModel model,
			String operationId, String stageName) {
		for (Operation operation : model.getOperations()) {
			if (operation.getId().equals(operationId)) {
				for (CustomizeStageConfig customizeConfig : operation
						.getCustomizeStageConfigs()) {
					if (customizeConfig.getStageName().equals(stageName)) {
						return customizeConfig;
					}
				}
			}
		}
		for (View view : model.getViews()) {
			if (view.getId().equals(operationId)) {
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

	public String getOperationId(Object operation) {
		BaseObject baseObject = (BaseObject) operation;
		return baseObject.getId();
	}

	public List<String> getParamterList(MongoDBModel model, Object operationObject) {
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
				String camelName = model.getName();
				result.add(camelName);
			}
		}
		return result;
	}

	public Map<String, List<ValidateRule>> getOperationValidateMap(
			MongoDBModel model, Object operation) {
		Map<String, List<ValidateRule>> map = new HashMap<String, List<ValidateRule>>();
		if (operation instanceof Operation) {
			Operation operationInstance = (Operation) operation;
			List<Field> operationFields = model
					.getOperationFields(operationInstance);
			for (Field field : operationFields) {
				if (field.getValidateRules() != null
						&& field.getValidateRules().size() > 0) {
					map.put(model.getName(), field
							.getValidateRules());
				}
			}
		}
		return map;
	}

	

}
