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
import org.tinygroup.entity.common.DisplayField;
import org.tinygroup.entity.common.GroupField;
import org.tinygroup.entity.common.HavingField;
import org.tinygroup.entity.common.OrderField;
import org.tinygroup.entity.common.View;
import org.tinygroup.entity.common.ViewGroup;
import org.tinygroup.entity.relation.EntityRelation;
import org.tinygroup.entity.relationmodel.RelationField;
import org.tinygroup.entity.relationmodel.RelationModel;
import org.tinygroup.imda.processor.ParameterBuilder;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.BeanOperatorManager;
import org.tinygroup.tinydb.operator.DBOperator;

/**
 * 
 * 功能说明: 关联关系服务和参数组装处理类
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-10-23 <br>
 * <br>
 */
public class RelationProcessor {

	private Context context;

	private View view;
	
	private RelationModel relationModel;

	@SuppressWarnings("rawtypes")
	protected DBOperator operator;

	private List<ConditionField> conditionFields;
	private List<ViewGroup> viewGroups;
	private List<GroupField> groupFields;
	private List<OrderField> orderFields;
	private List<HavingField> havingFields;

	private Map<String, OrderField> orderExist = new HashMap<String, OrderField>();

	private Map<String, GroupField> groupExist = new HashMap<String, GroupField>();
	

	public RelationProcessor(RelationModel relationModel, View view,
			Context context) {
		this.context = context;
		this.view = view;
		this.relationModel=relationModel;
		BeanOperatorManager manager = SpringUtil
				.getBean(BeanOperatorManager.OPERATOR_MANAGER_BEAN);
		this.operator = manager.getDbOperator(relationModel.getMainBeanType());
		
		relationModel.init(context);//对关联模型进行初始化
		
		this.conditionFields = view.getConditionFields();
		this.viewGroups = view.getViewGroups();
		this.groupFields = view.getGroupFields();
		for (GroupField groupField : groupFields) {
			EntityModelHelper helper = getHelper(groupField.getFieldId());
			String propertyName = helper.getPropertyName(groupField
					.getFieldId());
			groupExist.put(propertyName, groupField);
		}
		this.orderFields = view.getOrderFields();
		for (OrderField orderField : orderFields) {
			EntityModelHelper helper = getHelper(orderField.getFieldId());
			String propertyName = helper.getPropertyName(orderField
					.getFieldId());
			orderExist.put(propertyName, orderField);
		}
		this.havingFields = view.getHavingFields();

	}

	@SuppressWarnings("unchecked")
	public PageInfo getBeansWithRelationModelView() {
		if (view != null) {
			List<Object> params = new ArrayList<Object>();
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("select ");
			generateTableViewSelectSqlClause(stringBuffer);
			genetateTableSqlClause(stringBuffer);
			generateQuerySqlClause(params, stringBuffer);
			generateGroupSqlClause(stringBuffer);
			generateHavingSqlClause(stringBuffer, params);
			generateOrderSqlClause(stringBuffer);
			int pageSize = checkPageSize();
			String pageNumStr = context.get(ParameterBuilder.PAGE_NUMBER);
			PageInfo pageInfo = new PageInfo();
			String sql = stringBuffer.toString();
			Bean[] beans = null;
			int totalSize = 0;
			if (pageSize > 0) {
				int pageNum = 0;
				if (pageNumStr != null) {
					pageNum = Integer.parseInt(pageNumStr);
				}
				String accountSql = "select count(*) from (" + sql + ") temp";
				totalSize = operator.account(accountSql, params);
				int totalPages = totalSize % pageSize == 0 ? totalSize
						/ pageSize : totalSize / pageSize + 1;
				pageNum = checkPageNumber(totalPages, pageNum);
				int start = pageNum * pageSize + 1;
				beans = operator.getPagedBeans(sql, start, pageSize, params.toArray());
				pageInfo.setPageSize(pageSize);
				pageInfo.setTotalPages(totalPages);
				pageInfo.setPageNumber(pageNum);

			} else {
				beans = operator.getBeans(sql, params.toArray());
				if (beans != null) {
					totalSize = beans.length;
				}
			}
			if (beans == null) {
				beans = new Bean[0];
			}
			pageInfo.setBeans(beans);
			pageInfo.setTotalSize(totalSize);
			return pageInfo;
		}
		return null;

	}

	/**
	 * 
	 * 获取分页大小信息
	 * 
	 * @return
	 */
	private int checkPageSize() {
		String pageSizeStr = context.get(ParameterBuilder.PAGE_SIZE);
		int pageSize = 0;
		if (view.isFrontPaging()) {// 如果是前台分页，则返回0
			return 0;
		}

		if (pageSizeStr == null) {
			pageSize = view.getPageSize();
		} else {
			pageSize = Integer.parseInt(pageSizeStr);
		}
		return pageSize;
	}

	/**
	 * 
	 * 根据总页数来确定当前页是否有效，不有效则返回最后一页记录
	 */
	private int checkPageNumber(int totalPages, int pageNum) {
		if (totalPages <= pageNum && totalPages > 0) {
			return totalPages - 1;
		}
		if (totalPages <= 0) {
			return 0;
		}
		return pageNum;
	}

	private void generateOrderSqlClause(StringBuffer stringBuffer) {// 以后再重构，排序逻辑提出来，现在只能对所有字段进行单一方向排序
		String orderFieldInfo = context.get(ParameterBuilder.ORDER_BY_FIELD);
		String orderDirects = context.get(ParameterBuilder.SORT_DIRECTION);
		boolean hasOrder = false;
		StringBuffer orderBuffer = new StringBuffer();
		if (orderFieldInfo != null) {
			String[] orderArray = orderFieldInfo.split(EntityModelHelper.SPLIT);
			String[] directArray = null;
			if (orderDirects != null) {
				directArray = orderDirects.split(EntityModelHelper.SPLIT);
			}
			for (int i = 0; i < orderArray.length; i++) {
				if (orderFields != null && orderFields.size() > 0) {
					OrderField orderField = orderExist.get(orderArray[i]);
					if (orderField != null) {
						String fieldId = orderField.getFieldId();
						EntityModelHelper helper = getHelper(fieldId);
						hasOrder = helper.processOrderField(i, hasOrder,
								getTableAliasName(fieldId), orderBuffer,
								directArray, orderField);
					}

				}
			}

			if (hasOrder) {
				stringBuffer.append(" order by ");
				orderBuffer.deleteCharAt(orderBuffer.length() - 1);
				stringBuffer.append(orderBuffer.toString());
			}

		}

	}

	private EntityModelHelper getHelper(String fieldId) {
		return relationModel.getHelperWithFieldId(fieldId);
	}

	private String getTableAliasName(String fieldId) {
		return relationModel.getTableAliasName(fieldId);
	}

	private void generateHavingSqlClause(StringBuffer stringBuffer,
			List<Object> params) {
		boolean hasHaving = false;
		StringBuffer havingBuffer = new StringBuffer();
		int size = havingFields.size();
		if (havingFields != null && size > 0) {
			for (int i = 0; i < size; i++) {
				HavingField havingField = havingFields.get(i);
				if (havingField != null) {
					String fieldId = havingField.getFieldId();
					EntityModelHelper helper = getHelper(fieldId);
					hasHaving = helper.processHavingField(size, i,
							getTableAliasName(fieldId), params, hasHaving,
							havingBuffer, havingField);
				}

			}
		}

		if (hasHaving) {
			if (havingBuffer.length() > 0) {
				stringBuffer.append(" having ").append(havingBuffer.toString());
			}
		}

	}

	private void generateGroupSqlClause(StringBuffer stringBuffer) {
		String groupByInfo = context.get(ParameterBuilder.GROUP_BY_FIELD);
		if (groupByInfo != null) {
			String[] groupArray = groupByInfo.split(EntityModelHelper.SPLIT);
			boolean hasGroup = false;
			StringBuffer groupBuffer = new StringBuffer();
			for (String group : groupArray) {
				if (groupFields != null && groupFields.size() > 0) {
					GroupField groupField = groupExist.get(group);
					if (groupField != null) {
						String fieldId = groupField.getFieldId();
						EntityModelHelper helper = getHelper(fieldId);
						hasGroup = helper.processGroupField(hasGroup,
								getTableAliasName(fieldId), groupBuffer,
								groupField);
					}
				}
			}
			if (hasGroup) {
				stringBuffer.append(" group by ");
				groupBuffer.deleteCharAt(groupBuffer.length() - 1);
				stringBuffer.append(groupBuffer.toString());
			}

		}

	}

	private void generateQuerySqlClause(List<Object> params,
			StringBuffer stringBuffer) {
		StringBuffer conditionBuffer = new StringBuffer();
		ConditionTemp temp = new ConditionTemp(false, null);
		if (conditionFields != null && conditionFields.size() > 0) {
			for (ConditionField conditionField : conditionFields) {
				String fieldId = conditionField.getFieldId();
				EntityModelHelper helper = getHelper(fieldId);
				helper.processConditionField(getTableAliasName(fieldId),
						params, conditionBuffer, temp, conditionField);
			}
		}
		if (conditionBuffer.length() > 0) {
			stringBuffer.append(" where ").append(conditionBuffer.toString());
		}

	}

	/**
	 * 
	 * 生成表连接查询的sql片段
	 * 
	 * @param stringBuffer
	 */
	private void genetateTableSqlClause(StringBuffer stringBuffer) {
		stringBuffer.append(" from ");
		Map<String, Boolean> status = new HashMap<String, Boolean>();
		List<EntityRelation> relations=relationModel.getRelations();
		for (int i = 0; i < relations.size(); i++) {
			EntityRelation relation = relations.get(i);
			String mainModelId = relation.getMainEntityId();
			String mainAliasName = relation.getMainAliasName();
			boolean hasAppend = false;
			boolean hasConnect=false;
			String mainKey = formmatKey(mainModelId, mainAliasName);
			if (!status.containsKey(mainKey)) {// 模型未被处理过
				if (i != 0) {
					stringBuffer.append(" ").append(relation.getConnectMode())
							.append(" ");// 第一个模型引用除外
					hasConnect=true;
				}
				tableNameSqlClause(stringBuffer, mainModelId, mainAliasName);
				hasAppend = true;
				status.put(mainKey, true);// 标记已经处理过
			}
			String fromModelId = relation.getFromEntityId();
			String fromAliasName = relation.getFromAliasName();
			String fromKey = formmatKey(fromModelId, fromAliasName);
			if (!status.containsKey(fromKey)) {// 引用的模型为被处理过
				if(!hasConnect){
				stringBuffer.append(" ").append(relation.getConnectMode())
						.append(" ");// 连接信息
				}
				tableNameSqlClause(stringBuffer, fromModelId, fromAliasName);
				hasAppend = true;
				status.put(fromKey, true);// 标记已经处理过
			}
			if (hasAppend) {// 增加过表名片段信息，则增加表连接条件信息
				stringBuffer.append(" on ");
				joinConditionSqlClase(stringBuffer, mainModelId, mainAliasName,
						relation.getMainStandardFieldId());
				stringBuffer.append("=");
				joinConditionSqlClase(stringBuffer, fromModelId, fromAliasName,
						relation.getFromStandardFieldId());
			}

		}

	}

	/**
	 * 
	 * 格式化
	 * 
	 * @param modelName
	 * @param modelAliasName
	 * @return
	 */
	private String formmatKey(String modelName, String modelAliasName) {
		StringBuffer buf = new StringBuffer();
		buf.append(modelName);
		if (modelAliasName != null) {
			buf.append("_" + modelAliasName);
		}
		return buf.toString();
	}

	/**
	 * 
	 * 生成表连接查询的sql片段
	 * 
	 * @param stringBuffer
	 * @param model
	 */
	private void joinConditionSqlClase(StringBuffer stringBuffer,
			String modelId, String modelAliasName, String standardFieldId) {
		EntityModelHelper modelHelper = relationModel.getHelperWithModelId(modelId);
		if (StringUtil.isBlank(modelAliasName)) {
			modelAliasName = modelHelper.getTableAliasName(modelAliasName);
		}
		if (!StringUtil.isBlank(modelAliasName)) {
			stringBuffer.append(modelAliasName).append(".");
		}
		stringBuffer.append(modelHelper.getDbFieldName(standardFieldId));
	}

	/**
	 * 
	 * 表连接查询的表名信息
	 * 
	 * @param stringBuffer
	 * @param reference
	 */
	private void tableNameSqlClause(StringBuffer stringBuffer, String modelId,
			String modelAliasName) {
		EntityModelHelper modelHelper = relationModel.getHelperWithModelId(modelId);
		stringBuffer.append(modelHelper.getTableName());
		if (StringUtil.isBlank(modelAliasName)) {
			modelAliasName = modelHelper.getTableAliasName(modelAliasName);
		}
		if (!StringUtil.isBlank(modelAliasName)) {
			stringBuffer.append(" ").append(modelAliasName);
		}
	}

	private void generateTableViewSelectSqlClause(StringBuffer stringBuffer) {
		boolean hasField = false;
		if (viewGroups != null && viewGroups.size() > 0) {
			for (ViewGroup viewGroup : viewGroups) {
				List<DisplayField> displayFields = viewGroup.getFields();
				for (DisplayField displayField : displayFields) {
					String fieldId = displayField.getFieldId();
					RelationField relationField = relationModel.getRelationFieldWithFieldId(fieldId);
					if (relationField != null) {
						String aliasName = relationField.getAliseName();
						String refFieldId=relationField.getRefFieldId();
						if(!StringUtil.isBlank(refFieldId)){
							if(StringUtil.isBlank(aliasName)){//如果 ref-field-id值不为空且没有设置别名，那么别名值取自field-id的值
								aliasName=relationField.getFieldId();
							}
						}else{
							refFieldId=relationField.getFieldId();
						}
						EntityModelHelper helper = getHelper(refFieldId);
						hasField = helper.processDisplayField(stringBuffer,refFieldId,
								getTableAliasName(fieldId), aliasName, hasField,
								displayField);
					}
					
				}
			}
		}
		if (hasField) {
			stringBuffer.deleteCharAt(stringBuffer.length() - 1);
		} else {
			stringBuffer.append("*");
		}

	}



	/**
	 * 
	 * 关联模型参数组装方法
	 * 
	 * @param newContext
	 * @param view
	 * @return
	 */
	public Context buildParameter(Context newContext, View view) {
		List<ConditionField> conditionFields = view.getConditionFields();
		for (ConditionField conditionField : conditionFields) {
			String fieldId = conditionField.getFieldId();
			EntityModelHelper helper = getHelper(fieldId);
			helper.processConditionParamter(newContext, conditionField);
		}
		List<HavingField> havingFields = view.getHavingFields();
		for (HavingField havingField : havingFields) {
			String fieldId = havingField.getFieldId();
			EntityModelHelper helper = getHelper(fieldId);
			helper.processHavingParamter(newContext, havingField);
		}

		return newContext;

	}

}
