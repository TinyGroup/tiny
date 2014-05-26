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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.context.Context;
import org.tinygroup.entity.common.ConditionField;
import org.tinygroup.entity.common.Operation;
import org.tinygroup.entity.common.OperationField;
import org.tinygroup.entity.common.OperationGroup;
import org.tinygroup.entity.entitymodel.EntityModel;
import org.tinygroup.exception.TinySysRuntimeException;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.util.TinyDBUtil;

/**
 * 
 * 功能说明:操作服务处理逻辑类
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-9-6 <br>
 * <br>
 */
public class OperationServiceProcessor extends EntityModelHelper {

	/**
	 * 操作信息对象
	 */
	private Operation operation;

	private OperationGroup operationGroup;

	private List<ConditionField> conditionFields;

	public OperationServiceProcessor(EntityModel model, Context context,
			Operation operation) {
		super(model, context);
		this.operation = operation;
		this.operationGroup = operation.getOperationGroup();
		this.conditionFields = operation.getConditionFields();
	}

	public Bean[] getBeansWithOperation() {
		if (operation != null) {
			List<Object> params = new ArrayList<Object>();
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("select ");
			generateSelectSqlClause(operation.getOperationGroup(),stringBuffer);
			stringBuffer.append(" from ").append(tableName);
			String aliasName = getTableAliasName(null);
			if (!StringUtil.isBlank(aliasName)) {
				stringBuffer.append(" ").append(aliasName);
			}
			generateQuerySqlClause(aliasName,conditionFields, params, stringBuffer);
			logger.logMessage(LogLevel.DEBUG, stringBuffer.toString());
			return operator.getBeans(stringBuffer.toString(), params.toArray());
		}
		return new Bean[0];

	}

	@SuppressWarnings("unchecked")
	public Bean getBeanWithPrimaryKey() {
		return operator.getBean(context.get(primaryPropertyName));
	}

	private void generateSelectSqlClause(OperationGroup operationGroup,
			StringBuffer stringBuffer) {
		int oldlength=stringBuffer.length();
		getSubSqlClause(operationGroup, stringBuffer);
		if (stringBuffer.length()>oldlength) {
			stringBuffer.deleteCharAt(stringBuffer.length() - 1);
		} else {
			stringBuffer.append("*");
		}

	}

	private void getSubSqlClause(OperationGroup operationGroup,
			StringBuffer stringBuffer) {
		if (operationGroup != null) {
			List<OperationField> operationFields = operationGroup.getFields();
			if (operationFields != null && operationFields.size() > 0) {
				for (int j = 0; j < operationFields.size(); j++) {
					OperationField operationField = operationFields.get(j);
					if(isTableField(operationField.getFieldId())){
						stringBuffer
						.append(fieldId2DbFieldName.get(operationField
								.getFieldId())).append(",");
					}else{
						appendExpressionClause(null,operationField, stringBuffer);
					}
				
				}
			}
			if (operationGroup.getOperationGroups() != null) {
				for (OperationGroup subGroup : operationGroup
						.getOperationGroups()) {
					getSubSqlClause(subGroup, stringBuffer);
				}
			}

		}
	}

	public Bean insertBeanWithOperation() {
		if (operation == null) {
			return null;
		}
		Bean bean = new Bean(model.getName());
		insertBeanWithOperation(operationGroup, bean);
		bean = operator.insert(bean);
		return bean;
	}

	private void insertBeanWithOperation(OperationGroup operationGroup,
			Bean bean) {
		List<OperationField> operationFields = operationGroup.getFields();
		for (OperationField operationField : operationFields) {
			if(isTableField(operationField.getFieldId())){
				String propertyName = fieldId2PropertyName.get(operationField
						.getFieldId());
				bean.setProperty(propertyName, context.get(propertyName));
			}
		}
		if (operationGroup.getOperationGroups() != null) {
			for (OperationGroup subGroup : operationGroup.getOperationGroups()) {
				insertBeanWithOperation(subGroup, bean);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public Integer[] deleteBeanWithOperation() {
		if (conditionFields != null && conditionFields.size() > 0) {
			Map<String, String[]> valueMap=new HashMap<String, String[]>();
			int length=0;
			for (ConditionField conditionField : conditionFields) {
				if(isTableField(conditionField.getFieldId())){
					String propertyName = fieldId2PropertyName.get(conditionField
							.getFieldId());
					String values=context.get(propertyName);
					if(values!=null){
						String[] array=values.split(SPLIT);
						if(length==0){
							length=array.length;
						}
						valueMap.put(propertyName, array);
					}
					
				}
			}
			if(valueMap.size()>0){
				List<Bean> beans=new ArrayList<Bean>();
				for (int i = 0; i < length; i++) {
					Bean bean=new Bean(model.getName());
					for (String key : valueMap.keySet()) {
						bean.setProperty(key, valueMap.get(key)[i]);
					}
					beans.add(bean);
				}
				int[] affects= operator.batchDelete(beans);
				Integer[] records=new Integer[affects.length];
				for (int i = 0; i < affects.length; i++) {
					records[i]=affects[i];
				}
				return records;
			}
			
		}
		return new Integer[0];
	}	

	@SuppressWarnings("unchecked")
	public Integer updateBeanWithOperation() {
		if (operation != null) {
			Bean bean = new Bean(model.getName());
			getUpdateBean(operation.getOperationGroup(), bean);
			List<ConditionField> conditionFields = operation
					.getConditionFields();
			List<String> conditionColumns=new ArrayList<String>();
			for (ConditionField conditionField : conditionFields) {
				String propertyName = fieldId2PropertyName.get(conditionField
						.getFieldId());
				String dbFieldName = fieldId2DbFieldName.get(conditionField
						.getFieldId());
				bean.setProperty(propertyName, context.get(propertyName));
				conditionColumns.add(dbFieldName.toUpperCase());
			}
			return operator.update(bean,conditionColumns);

		}
		return 0;
	}

	private void getUpdateBean(OperationGroup operationGroup, Bean bean) {
		List<OperationField> operationFields = operationGroup.getFields();
		for (OperationField operationField : operationFields) {
		    if(isTableField(operationField.getFieldId())){
		    	String propertyName = fieldId2PropertyName.get(operationField
						.getFieldId());
				bean.setProperty(propertyName, context.get(propertyName));
		    }
		}
		if (operationGroup.getOperationGroups() != null) {
			for (OperationGroup subGroup : operationGroup.getOperationGroups()) {
				getUpdateBean(subGroup, bean);
			}
		}
	}

	public Bean createBeanParamter() {

		if (conditionFields != null && conditionFields.size() > 0) {
			List<String> properties = new ArrayList<String>();
			for (ConditionField conditionField : conditionFields) {
				properties.add(fieldId2PropertyName.get(conditionField
						.getFieldId()));
			}
			return TinyDBUtil.context2Bean(context, model.getName(),
					properties, operator.getSchema());// 最后一个参数目前没有
		}
		throw new TinySysRuntimeException("ientity.deleteParamterIsEmpty");
	}
	
	/**
	 * 
	 * 根据fieldId获取操作字段信息
	 * @param fieldId
	 * @return
	 */
	public OperationField getOperationField(String fieldId){
		if(operationGroup!=null){
			List<OperationField> fields= operationGroup.getFields();
			for (OperationField operationField : fields) {
				if (operationField.getFieldId().equals(fieldId)) {
					return operationField;
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * 根据fieldId获取条件字段信息
	 * @param fieldId
	 * @return
	 */
	public ConditionField getConditionField(String fieldId){
		
		for (ConditionField conditionField : conditionFields) {
			if (conditionField.getFieldId().equals(fieldId)) {
				return conditionField;
			}
		}
		return null;
	}
}
