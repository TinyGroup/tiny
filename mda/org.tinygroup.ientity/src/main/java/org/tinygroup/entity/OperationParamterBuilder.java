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
import java.util.UUID;

import org.tinygroup.context.Context;
import org.tinygroup.entity.common.ConditionField;
import org.tinygroup.entity.common.Field;
import org.tinygroup.entity.common.Operation;
import org.tinygroup.entity.common.OperationField;
import org.tinygroup.entity.common.OperationGroup;
import org.tinygroup.entity.entitymodel.EntityModel;
import org.tinygroup.imda.util.MdaUtil;
import org.tinygroup.weblayer.webcontext.parser.fileupload.TinyFileItem;
import org.tinygroup.weblayer.webcontext.parser.impl.ItemFileObject;

/**
 * 
 * 功能说明: 操作类参数组装
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-9-6 <br>
 * <br>
 */
public class OperationParamterBuilder extends EntityModelHelper {

	private Operation operation;

	public OperationParamterBuilder(Context context, EntityModel model,
			Operation operation) {
		super(model, context);
		this.operation = operation;
	}

	public Context buildOperationParameter(OperationGroup operationGroup,
			Context newContext) {
		if (operationGroup != null) {
			List<OperationField> operationFields = operationGroup.getFields();
			for (OperationField operationField : operationFields) {
				String propertyName = fieldId2PropertyName.get(operationField
						.getFieldId());
				Field field = fieldId2Field.get(operationField.getFieldId());
				Object value = context.get(propertyName);
				if (value != null) {
					if (value.getClass().isArray()) {// 如果是文件表单字段，那么需要对参数值进行特殊处理。
						Object[] values = (Object[]) value;
						for (int i = 0; i < values.length; i++) {
							if (values[i] instanceof ItemFileObject) {
								ItemFileObject fileObject = (ItemFileObject) values[i];
								TinyFileItem fileItem = (TinyFileItem) fileObject
										.getFileItem();
								values[i] = fileItem.getUnique();
							}
						}
					} else {
						if (value instanceof ItemFileObject) {// 如果是文件类型表单字段
							ItemFileObject fileObject = (ItemFileObject) value;
							TinyFileItem fileItem = (TinyFileItem) fileObject
									.getFileItem();
							value = fileItem.getUnique();
						}
					}
					if (field.isEncrypt()) {// 加密字段特殊处理
						try {
							value = cryptor.encrypt(String.valueOf(value));
						} catch (Exception e) {
							logger.errorMessage("encrypt error", e);
						}
					}
				} else {
					if (field.isObjectId()) {
						value = UUID.randomUUID().toString()
								.replaceAll("-", "");
					}
				}
				newContext.put(
						propertyName,
						MdaUtil.getObject(value, field.getDefaultValue(),
								field.getDataType(),
								field.getDefaultValueGetter()));
			}
			if (operationGroup.getOperationGroups() != null) {
				for (OperationGroup subGroup : operationGroup
						.getOperationGroups()) {
					buildOperationParameter(subGroup, newContext);
				}
			}

		}
		return newContext;
	}

	public Context buildParameter(Context newContext,
			List<ConditionField> conditionFields) {
		buildConditionParameter(newContext, conditionFields);
		buildOperationParameter(operation.getOperationGroup(), newContext);
		return newContext;
	}

}
