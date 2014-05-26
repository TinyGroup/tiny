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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.commons.cryptor.Cryptor;
import org.tinygroup.commons.cryptor.DefaultCryptor;
import org.tinygroup.commons.tools.Assert;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.context.Context;
import org.tinygroup.entity.common.ConditionField;
import org.tinygroup.entity.common.DisplayField;
import org.tinygroup.entity.common.Field;
import org.tinygroup.entity.common.Group;
import org.tinygroup.entity.common.GroupField;
import org.tinygroup.entity.common.HavingField;
import org.tinygroup.entity.common.OperationField;
import org.tinygroup.entity.common.OrderField;
import org.tinygroup.entity.entitymodel.EntityModel;
import org.tinygroup.entity.util.ModelUtil;
import org.tinygroup.exception.TinySysRuntimeException;
import org.tinygroup.expression.ExpressionManager;
import org.tinygroup.expression.SqlExpression;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.metadata.config.stdfield.StandardField;
import org.tinygroup.metadata.util.MetadataUtil;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.tinydb.BeanDbNameConverter;
import org.tinygroup.tinydb.BeanOperatorManager;
import org.tinygroup.tinydb.operator.DBOperator;

/**
 * 
 * 功能说明:实体模型帮助类
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-9-6 <br>
 * <br>
 */
public class EntityModelHelper {

	private static final String DEFAULT_ORDER_DIRECTION = "asc";

	public static final String COMPARE_MODE = "CompareMode";
	/**
	 * LengthEqualsCompareMode比较模式字段值长度参数名称的后缀
	 */
	public static final String LEVEL_LENGTH = "LevelLength";

	public static final String HAVING_COMPARE_MODE = "HavingCompareMode";

	public static final String HAVING_FIELD = "HavingField";

	public static final String SPLIT = ",";

	protected Logger logger = LoggerFactory
			.getLogger(ViewServiceProcessor.class);
	/**
	 * 要处理的实体模型
	 */
	protected EntityModel model;
	/**
	 * 上下文对象
	 */
	protected Context context;

	@SuppressWarnings("rawtypes")
	protected DBOperator operator;

	protected BeanDbNameConverter converter;

	protected String tableName;

	protected String primaryKeyName;

	protected String primaryPropertyName;

	/**
	 * key:配置fieldId value:bean属性名称
	 */
	protected Map<String, String> fieldId2PropertyName = new HashMap<String, String>();
	/**
	 * key:配置fieldId value:字段名称
	 */
	protected Map<String, String> fieldId2DbFieldName = new HashMap<String, String>();

	protected Map<String, Field> fieldId2Field = new HashMap<String, Field>();

	protected Map<String, String> fieldId2ModelId = new HashMap<String, String>();
    
	private CompareModeContain contain;
	
	protected Cryptor cryptor = new DefaultCryptor();

	public EntityModelHelper(EntityModel model, Context context) {
		super();
		this.model = model;
		this.context = context;
		BeanOperatorManager manager = SpringUtil
				.getBean(BeanOperatorManager.OPERATOR_MANAGER_BEAN);
		this.operator = manager.getDbOperator(model.getName());
		Assert.assertNotNull(operator, "operator must not null");
		this.converter = operator.getBeanDbNameConverter();
		this.tableName = manager.getTableConfiguration(model.getName())
				.getName();
		contain=SpringUtil.getBean(CompareModeContain.COMPARE_MODE_CONTAIN);
		List<Group> groups = model.getGroups();
		if (groups != null) {
			for (Group group : groups) {
				List<Field> fields = group.getFields();
				if (fields != null) {
					for (Field field : fields) {
						String dbFieldName = getDbFieldName(field
								.getStandardFieldId());
						if (field.isPrimary()) {
							this.primaryKeyName = dbFieldName;
							primaryPropertyName = nameConverter(primaryKeyName);
						}
						fieldId2DbFieldName.put(field.getId(), dbFieldName);
						fieldId2PropertyName.put(field.getId(),
								nameConverter(dbFieldName));
						fieldId2Field.put(field.getId(), field);
						String modelId = field.getModelId();
						if (StringUtil.isBlank(modelId)) {
							modelId = model.getId();
						}
						fieldId2ModelId.put(field.getId(), modelId);
					}
				}
			}
		}

	}

	public String nameConverter(String dbFieldName) {
		return converter.dbFieldNameToPropertyName(dbFieldName);
	}

	/**
	 * 
	 * 
	 * @param 找不到标准字段则返回参数
	 * @return
	 */
	public String getDbFieldName(String standardFieldId) {
		try {
			StandardField standardField=getStandardField(standardFieldId);
			return standardField.getName();
		} catch (Exception e) {
		}
		return standardFieldId;
	}

	public StandardField getStandardField(String standardFieldId) {
		return MetadataUtil.getStandardField(standardFieldId);
	}

	/**
	 * 
	 * 根据fieldId 获取对应的数据库字段名称
	 * 
	 * @param fieldId
	 * @return
	 */
	public String getDbFieldNameWithFieldId(String fieldId) {
		return fieldId2DbFieldName.get(fieldId);
	}

	/**
	 * 
	 * 根据fieldId 获取对应的bean 属性名称
	 * 
	 * @param fieldId
	 * @return
	 */
	public String getPropertyName(String fieldId) {
		return fieldId2PropertyName.get(fieldId);
	}

	public String aggregateFunctionSqlClause(String functionName,
			String fieldName, String aliasName) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(functionName).append("(").append(fieldName)
				.append(")");
		if (aliasName != null) {
			stringBuffer.append(" ").append(aliasName);
		}
		return stringBuffer.toString();
	}

	public void generateQuerySqlClause(String tableAliasName,
			List<ConditionField> conditionFields, List<Object> params,
			StringBuffer stringBuffer) {
		StringBuffer conditionBuffer = new StringBuffer();
		ConditionTemp temp = new ConditionTemp(false, null);
		if (conditionFields != null && conditionFields.size() > 0) {
			for (ConditionField conditionField : conditionFields) {
				processConditionField(tableAliasName, params, conditionBuffer,
						temp, conditionField);
			}
		}
		if (conditionBuffer.length() > 0) {
			stringBuffer.append(" where ").append(conditionBuffer.toString());
		}
	}

	public boolean processOrderField(int i, boolean hasOrder,
			String tableAliasName, StringBuffer orderBuffer,
			String[] directArray, OrderField orderField) {
		if (isTableField(orderField.getFieldId())) {
			String dbFieldName = fieldId2DbFieldName.get(orderField
					.getFieldId());
			String orderModer = orderField.getOrderMode();
			if (orderModer == null) {
				if (directArray != null && directArray.length > i) {
					orderModer = directArray[i];
				} else {
					orderModer = DEFAULT_ORDER_DIRECTION;
				}
			}
			if (orderModer != null) {// 配置有指定以配置为准，配置没有前台有传以前台为准，配置没有前台也没有则不排序
				orderBuffer.append(
						getDbFieldWithTableAliasName(dbFieldName,
								tableAliasName)).append(
						generateOrderMode(orderModer));
				hasOrder = true;
			}
		}
		return hasOrder;
	}

	private String generateOrderMode(String orderMode) {
		return " " + orderMode + SPLIT;
	}

	public boolean processGroupField(boolean hasGroup, String tableAliasName,
			StringBuffer groupBuffer, GroupField groupField) {
		if (groupField != null) {
			if (isTableField(groupField.getFieldId())) {
				String dbFieldName = fieldId2DbFieldName.get(groupField
						.getFieldId());
				groupBuffer.append(
						getDbFieldWithTableAliasName(dbFieldName,
								tableAliasName)).append(SPLIT);
				hasGroup = true;
			}
		}
		return hasGroup;
	}

	public boolean processHavingField(int size, int index,
			String tableAliasName, List<Object> params, boolean hasHaving,
			StringBuffer havingBuffer, HavingField havingField) {
		if (isTableField(havingField.getFieldId())) {
			String dbFieldName = fieldId2DbFieldName.get(havingField
					.getFieldId());
			String propertyName = fieldId2PropertyName.get(havingField
					.getFieldId());
			String value = context.get(getSpliceParamterName(propertyName,
					HAVING_FIELD));
			if (value != null) {
				String compareMode = checkHavingCompareMode(
						havingField.getCompareMode(), propertyName);
				String fieldInfo = getFieldInfo(
						havingField,
						getDbFieldWithTableAliasName(dbFieldName,
								tableAliasName));
				CompareMode compare = getCompareMode(compareMode);
				if (compare != null) {
					String compareSymbols = compare
							.generateCompareSymbols(fieldInfo);
					havingBuffer.append(compareSymbols).append(
							generateConnectMode(havingField.getConnectMode()));
					hasHaving = true;
					compare.assembleParamterValue(propertyName, context, params);
					if (index == size - 1) {// 删除最后一个条件连接符
						int lastIndex = havingBuffer
								.lastIndexOf(generateConnectMode(havingField
										.getConnectMode()));
						havingBuffer.delete(lastIndex, havingBuffer.length());
					}
				} else {
					throw new TinySysRuntimeException(
							"ientity.notFountCompareMode", compareMode);
				}

			}
		}
		return hasHaving;
	}

	private String getFieldInfo(HavingField havingField, String dbFieldName) {
		String fieldInfo = dbFieldName;
		if (havingField.getAggregateFunction() != null) {
			fieldInfo = aggregateFunctionSqlClause(
					havingField.getAggregateFunction(), dbFieldName, null);
		}
		return fieldInfo;
	}

	public void processConditionField(String tableAliasName,
			List<Object> params, StringBuffer conditionBuffer,
			ConditionTemp temp, ConditionField conditionField) {
		if (isTableField(conditionField.getFieldId())) {
			String propertyName = fieldId2PropertyName.get(conditionField
					.getFieldId());
			String dbFieldName = fieldId2DbFieldName.get(conditionField
					.getFieldId());
			String compareMode = checkCompareMode(
					conditionField.getCompareMode(), propertyName);
			CompareMode compare = getCompareMode(compareMode);
			if (compare != null) {
				if (context.get(propertyName) != null || !compare.needValue()) {
					appendLastConnectMode(temp.isHasCondition(),
							temp.getConnectMode(), conditionBuffer);
					appendCondition(dbFieldName, propertyName, tableAliasName,
							conditionBuffer, compare, params);
//					if(compare.needValue()){//需要有值才能放入list参数列表中
						compare.assembleParamterValue(propertyName, context, params);
//					}
					temp.setHasCondition(true);
					temp.setConnectMode(conditionField.getConnectMode());
				}
			} else {
				throw new TinySysRuntimeException(
						"ientity.notFountCompareMode", compareMode);
			}
		}
	}

	/**
	 * 
	 * 添加上个条件的连接符
	 * 
	 * @param i
	 * @param conditionBuffer
	 * @param conditionFields
	 */
	public void appendLastConnectMode(boolean hasCondition, String connectMode,
			StringBuffer conditionBuffer) {
		if (hasCondition) {
			conditionBuffer.append(generateConnectMode(connectMode));// 加入上次条件的操作符
		}
	}

	/**
	 * 
	 * 添加条件信息
	 * 
	 * @param dbFieldName
	 * @param propertyName
	 * @param conditionBuffer
	 * @param compare
	 * @param params
	 */
	public void appendCondition(String dbFieldName, String propertyName,
			String tableAliasName, StringBuffer conditionBuffer,
			CompareMode compare, List<Object> params) {
			String compareSymbols = compare
					.generateCompareSymbols(getDbFieldWithTableAliasName(
							dbFieldName, tableAliasName));
			conditionBuffer.append(compareSymbols);
	}

	public String checkCompareMode(String defaultCompareMode,
			String propertyName) {
		String compareMode = context.get(getSpliceParamterName(propertyName,
				COMPARE_MODE));
		if (compareMode == null) {
			compareMode = defaultCompareMode;
		}
		return compareMode;
	}

	public String checkHavingCompareMode(String defaultCompareMode,
			String propertyName) {
		String compareMode = context.get(getSpliceParamterName(propertyName,
				HAVING_COMPARE_MODE));
		if (compareMode == null) {
			compareMode = defaultCompareMode;
		}
		return compareMode;
	}

	public Object getValueFromContext(String propertyName) {
		return context.get(propertyName);
	}

	public CompareMode getCompareMode(String compareModeName) {
		return contain.getCompareMode(compareModeName);
	}

	public boolean isTableField(String fieldId) {
		Field field = fieldId2Field.get(fieldId);
		if (field != null) {
			return field.isTableField();
		}
		return false;
	}

	public String generateConnectMode(String connectMode) {
		return " " + connectMode + " ";
	}

	public String getSpliceParamterName(String propertyName, String suffix) {
		return ModelUtil.getSpliceParamterName(propertyName, suffix);
	}

	public EntityModel getModel() {
		return model;
	}

	public void setModel(EntityModel model) {
		this.model = model;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public DBOperator<?> getOperator() {
		return operator;
	}

	public void setOperator(DBOperator<?> operator) {
		this.operator = operator;
	}

	public BeanDbNameConverter getConverter() {
		return converter;
	}

	public void setConverter(BeanDbNameConverter converter) {
		this.converter = converter;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getPrimaryKeyName() {
		return primaryKeyName;
	}

	public void setPrimaryKeyName(String primaryKeyName) {
		this.primaryKeyName = primaryKeyName;
	}

	public String getPrimaryPropertyName() {
		return primaryPropertyName;
	}

	public void setPrimaryPropertyName(String primaryPropertyName) {
		this.primaryPropertyName = primaryPropertyName;
	}

	public Context buildConditionParameter(Context newContext,
			List<ConditionField> conditionFields) {
		if (conditionFields != null) {
			for (ConditionField conditionField : conditionFields) {
				processConditionParamter(newContext, conditionField);
			}
		}
		return newContext;

	}

	public void processConditionParamter(Context newContext,
			ConditionField conditionField) {
		String propertyName = fieldId2PropertyName.get(conditionField
				.getFieldId());
		Object value = context.get(propertyName);
		if (value == null) {
			if (conditionField.isHidden()) {
				value = getDefaultFieldValue(conditionField.getFieldId());
			}
		}
		if (value != null) {
			newContext.put(propertyName, value);
		}
		String compareParamterName = getSpliceParamterName(propertyName,
				COMPARE_MODE);
		Object compareModeValue = context.get(compareParamterName);
		if (compareModeValue != null) {
			newContext.put(compareParamterName, compareModeValue);
		}
		String lengthParamterName=getSpliceParamterName(propertyName, LEVEL_LENGTH);
		Object lengthValue=context.get(lengthParamterName);
		if(lengthValue!=null){
			newContext.put(lengthParamterName, lengthValue);
		}
	}

	public void processHavingParamter(Context newContext,
			HavingField havingField) {
		String propertyName = fieldId2PropertyName
				.get(havingField.getFieldId());
		String paramName = getSpliceParamterName(propertyName, HAVING_FIELD);
		Object value = context.get(paramName);
		if (value == null) {
			if (havingField.isHidden()) {
				value = getDefaultFieldValue(havingField.getFieldId());
			}
		}
		if (value != null) {
			newContext.put(paramName, value);
		}
		String compareParamterName = getSpliceParamterName(propertyName,
				HAVING_COMPARE_MODE);
		Object compareModeValue = context.get(compareParamterName);
		if (compareModeValue != null) {
			newContext.put(compareParamterName, compareModeValue);
		}
	}

	public String getDefaultFieldValue(String fieldID) {
		Field field = fieldId2Field.get(fieldID);
		if (field != null) {
			return field.getDefaultValue();
		}
		return null;
	}

	public String getTableAliasName(String tableAliasName) {
		if (!StringUtil.isBlank(tableAliasName)) {
			return tableAliasName;
		}
		String aliasName = model.getAliasName();
		if (aliasName == null) {
			aliasName = model.getName();
		}
		return aliasName;
	}

	public boolean processDisplayField(StringBuffer stringBuffer,
			String fieldId, String tableAliasName, String dbFieldAliasName,
			boolean hasField, DisplayField displayField) {
		if (fieldId == null) {
			fieldId = displayField.getFieldId();// 如果传入fieldId参数不为空，则用传入的
		}
		if (isTableField(fieldId)) {
			String dbFieldName = fieldId2DbFieldName.get(fieldId);
			if (displayField.getAggregateFunction() != null
					&& !displayField.isAggregateByView()) {
				stringBuffer.append(
						aggregateFunctionSqlClause(
								displayField.getAggregateFunction(),
								getDbFieldWithTableAliasName(dbFieldName,
										tableAliasName), dbFieldAliasName))
						.append(SPLIT);
			} else {
				stringBuffer
						.append(getDbFieldWithAliasName(dbFieldName,
								tableAliasName,
								getDbFieldAliasName(fieldId, dbFieldAliasName)))
						.append(SPLIT);
			}
			hasField = true;
		}else{
			appendExpressionClause(dbFieldAliasName,displayField, stringBuffer);
		}
		return hasField;
	}
	
	
	protected void appendExpressionClause(String fieldAliasName,OperationField field,StringBuffer stringBuffer){
		String expressionMode=field.getExpressionMode();
		if(!StringUtil.isBlank(expressionMode)){
			ExpressionManager manager=SpringUtil.getBean(ExpressionManager.MANAGER_BEAN_NAME);
			SqlExpression expression=manager.getExpression(expressionMode);
			if(expression!=null){
				stringBuffer.append(expression.interpret());
				String aliasName=getExpressionAliasName(field.getFieldId(), fieldAliasName);
				if (aliasName != null) {
					stringBuffer.append(" ").append(aliasName);
				}
				stringBuffer.append(SPLIT);
			}
		}
	}
    /**
     * 
     * 获取表达式片段的别名
     * @param fieldId
     * @param fieldAliasName
     * @return
     */
	private String getExpressionAliasName(String fieldId, String fieldAliasName) {
		if (!StringUtil.isBlank(fieldAliasName)) {
			return fieldAliasName;
		}
		String aliasName= fieldId2Field.get(fieldId).getAliseName();
		if(StringUtil.isBlank(aliasName)){
		    aliasName= fieldId2DbFieldName.get(fieldId);
		}
		return aliasName;
	}

	/**
	 * 
	 * 只带表别名的字段信息
	 * 
	 * @param fieldName
	 * @param tableAliasName
	 * @return
	 */
	private String getDbFieldWithTableAliasName(String fieldName,
			String tableAliasName) {
		StringBuffer buffer = new StringBuffer();
		if (tableAliasName != null) {
			buffer.append(tableAliasName).append(".");
		}
		buffer.append(fieldName);
		return buffer.toString();
	}

	/**
	 * 
	 * 带表别名和字段别名的字段信息
	 * 
	 * @param fieldName
	 * @param tableAliasName
	 * @param dbFieldAliasName
	 * @return
	 */
	private String getDbFieldWithAliasName(String fieldName,
			String tableAliasName, String dbFieldAliasName) {
		StringBuffer buffer = new StringBuffer();
		if (tableAliasName != null) {
			buffer.append(tableAliasName).append(".");
		}
		buffer.append(fieldName);
		if (dbFieldAliasName != null) {
			buffer.append(" ").append(dbFieldAliasName);
		}
		return buffer.toString();
	}

	/**
	 * 
	 * 获取数据库字段别名
	 * 
	 * @param fieldId
	 * @param aliasName
	 * @return
	 */
	protected String getDbFieldAliasName(String fieldId, String aliasName) {
		if (!StringUtil.isBlank(aliasName)) {
			return aliasName;
		}
		return fieldId2Field.get(fieldId).getAliseName();
	}

	public Field getFieldById(String fieldId) {
		return fieldId2Field.get(fieldId);
	}

}
