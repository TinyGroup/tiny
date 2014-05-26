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

import java.lang.reflect.Array;
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
import org.tinygroup.entity.entitymodel.EntityModel;
import org.tinygroup.entity.extendinfo.FieldMaps;
import org.tinygroup.entity.impl.IsNullCompareMode;
import org.tinygroup.exception.TinySysRuntimeException;
import org.tinygroup.imda.processor.ParameterBuilder;
import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.relation.Relation;

/**
 * 
 * 功能说明:视图服务处理逻辑类
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-9-6 <br>
 * <br>
 */
public class ViewServiceProcessor extends EntityModelHelper {

	/**
	 * 操作信息对象
	 */
	private View view;

	private List<ConditionField> conditionFields;
	private List<ViewGroup> viewGroups;
	private List<GroupField> groupFields;
	private List<OrderField> orderFields;
	private List<HavingField> havingFields;

	private Map<String, OrderField> orderExist = new HashMap<String, OrderField>();

	private Map<String, GroupField> groupExist = new HashMap<String, GroupField>();
	// 暂时未用
	private Map<String, ConditionField> conditionExist = new HashMap<String, ConditionField>();

	private Map<String, HavingField> havingExist = new HashMap<String, HavingField>();

	private Map<String, List<ConditionField>> queryModeMap = new HashMap<String, List<ConditionField>>();

	public ViewServiceProcessor(EntityModel model, Context context, View view) {
		super(model, context);
		this.view = view;
		this.conditionFields = view.getConditionFields();
		for (ConditionField conditionField : conditionFields) {
			String propertyName = fieldId2PropertyName.get(conditionField
					.getFieldId());
			conditionExist.put(propertyName, conditionField);
			createQueryGroupModes(conditionField);
		}
		this.viewGroups = view.getViewGroups();
		this.groupFields = view.getGroupFields();
		for (GroupField groupField : groupFields) {
			String propertyName = fieldId2PropertyName.get(groupField
					.getFieldId());
			groupExist.put(propertyName, groupField);
		}
		this.orderFields = view.getOrderFields();
		for (OrderField orderField : orderFields) {
			String propertyName = fieldId2PropertyName.get(orderField
					.getFieldId());
			orderExist.put(propertyName, orderField);
		}
		this.havingFields = view.getHavingFields();
		if (havingFields != null) {
			for (HavingField havingField : havingFields) {
				String propertyName = fieldId2PropertyName.get(havingField
						.getFieldId());
				havingExist.put(propertyName, havingField);
			}
		}

	}

	private void createQueryGroupModes(ConditionField conditionField) {
		String groups = conditionField.getGroups();
		if (groups != null) {
			String[] groupArray = groups.split(SPLIT);
			for (String group : groupArray) {
				List<ConditionField> fields = queryModeMap.get(group);
				if (fields == null) {
					fields = new ArrayList<ConditionField>();
				}
				fields.add(conditionField);
				queryModeMap.put(group, fields);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public PageInfo getBeansWithTableView() {
		if (view != null) {
			List<Object> params = new ArrayList<Object>();
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("select ");
			generateTableViewSelectSqlClause(stringBuffer);
			stringBuffer.append(" from ").append(tableName);
			String aliasName = getTableAliasName(null);
			if (!StringUtil.isBlank(aliasName)) {
				stringBuffer.append(" ").append(aliasName);
			}
			generateQuerySqlClause(aliasName, conditionFields, params,
					stringBuffer);
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

	/**
	 * 
	 * having子句
	 * 
	 * @param stringBuffer
	 */
	private void generateHavingSqlClause(StringBuffer stringBuffer,
			List<Object> params) {

		boolean hasHaving = false;
		StringBuffer havingBuffer = new StringBuffer();
		if (havingFields != null && havingFields.size() > 0) {
			int size = havingFields.size();
			for (int i = 0; i < size; i++) {
				HavingField havingField = havingFields.get(i);
				if (havingField != null) {
					hasHaving = processHavingField(size, i,
							getTableAliasName(null), params, hasHaving,
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

	/**
	 * table-view的查询信息sql片段
	 * 
	 * @param stringBuffer
	 */
	private void generateTableViewSelectSqlClause(StringBuffer stringBuffer) {
		boolean hasField = false;
		if (viewGroups != null && viewGroups.size() > 0) {
			for (ViewGroup viewGroup : viewGroups) {
				List<DisplayField> displayFields = viewGroup.getFields();
				for (DisplayField displayField : displayFields) {
					hasField = processDisplayField(stringBuffer,
							displayField.getFieldId(), getTableAliasName(null),
							null, hasField, displayField);

				}
			}
		}
		if (hasField) {
			stringBuffer.deleteCharAt(stringBuffer.length() - 1);
		} else {
			stringBuffer.append("*");
		}
	}

	private void generateGroupSqlClause(StringBuffer stringBuffer) {
		String groupByInfo = context.get(ParameterBuilder.GROUP_BY_FIELD);
		if (groupByInfo != null) {
			String[] groupArray = groupByInfo.split(SPLIT);
			boolean hasGroup = false;
			StringBuffer groupBuffer = new StringBuffer();
			for (String group : groupArray) {
				if (groupFields != null && groupFields.size() > 0) {
					GroupField groupField = groupExist.get(group);
					hasGroup = processGroupField(hasGroup,
							getTableAliasName(null), groupBuffer, groupField);
				}

			}
			if (hasGroup) {
				stringBuffer.append(" group by ");
				groupBuffer.deleteCharAt(groupBuffer.length() - 1);
				stringBuffer.append(groupBuffer.toString());
			}

		}

	}

	/**
	 * 以下几种情况不进行排序 1、前台没有传排序字段 2、传入排序字段参数，但是与配置不符合
	 * 3、传了排序字段参数也与配置符合，但是配置中没有指定方向，且前台也没有传递排序方向参数
	 * 
	 * @param stringBuffer
	 */
	private void generateOrderSqlClause(StringBuffer stringBuffer) {
		String orderFieldInfo = context.get(ParameterBuilder.ORDER_BY_FIELD);
		String orderDirects = context.get(ParameterBuilder.SORT_DIRECTION);
		boolean hasOrder = false;
		StringBuffer orderBuffer = new StringBuffer();
		if (orderFieldInfo != null) {
			String[] orderArray = orderFieldInfo.split(SPLIT);
			String[] directArray = null;
			if (orderDirects != null) {
				directArray = orderDirects.split(SPLIT);
			}
			for (int i = 0; i < orderArray.length; i++) {
				if (orderFields != null && orderFields.size() > 0) {
					OrderField orderField = orderExist.get(orderArray[i]);
					if (orderField != null) {
						hasOrder = processOrderField(i, hasOrder,
								getTableAliasName(null), orderBuffer,
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

	/**
	 * 
	 * 创建树形结构数据
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T[] getTreeNodes(Class<T> clazz) {
		if (view != null) {
			List<Object> params = new ArrayList<Object>();
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("select ");
			generateTableViewSelectSqlClause(stringBuffer);
			stringBuffer.append(" from ").append(tableName);
			String aliasName = getTableAliasName(null);
			if (!StringUtil.isBlank(aliasName)) {
				stringBuffer.append(" ").append(aliasName);
			}
			generateTreeQuerySqlClause(params, stringBuffer);
			generateOrderSqlClause(stringBuffer);
			FieldMaps fieldMaps = new FieldMaps(this, context);
			fieldMaps.parserExtendInfos(view.getExtendInformation());
			Bean[] beans = operator.getBeans(stringBuffer.toString(), params);
			if (beans != null) {
				String keyName = getKeyName();
				T[] treeNodes = (T[]) Array.newInstance(clazz, beans.length);
				for (int i = 0; i < beans.length; i++) {
					treeNodes[i] = fieldMaps.bean2TreeNode(beans[i], keyName,
							clazz);
				}
				return treeNodes;
			}
		}
		return (T[]) Array.newInstance(clazz, 0);
	}

	private String getKeyName() {
		Relation relation = operator.getRelation();
		if (relation == null) {
			throw new TinySysRuntimeException("ientity.treeRelationIsNotExist");
		}
		return relation.getKeyName() + relation.getCollectionMode();
	}

	/**
	 * 
	 * 创建树形结构数据
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T[] getTreeNodesWithCode(Class<T> clazz) {
		if (view != null) {
			List<Object> params = new ArrayList<Object>();
			String sql = generateSql(params);
			FieldMaps fieldMaps = new FieldMaps(this, context);
			fieldMaps.parserExtendInfos(view.getExtendInformation());
			Bean[] beans = operator.getBeans(sql, params);
			if (beans != null) {
				T[] treeNodes = (T[]) Array.newInstance(clazz, beans.length);
				for (int i = 0; i < beans.length; i++) {
					Bean[] subBeans = getBeans(beans[i]);
					treeNodes[i] = fieldMaps.bean2TreeNodeWithRecursive(
							beans[i], clazz, subBeans);
				}
				return treeNodes;
			}
		}
		return (T[]) Array.newInstance(clazz, 0);
	}

	private void contextReplace(Bean bean) {
		for (ConditionField conditionField : conditionFields) {
			String propertyName = getPropertyName(conditionField.getFieldId());
			if (bean.containsKey(propertyName)) {
				Object value = bean.get(propertyName);
				context.put(propertyName, value);
			}
		}

	}

	private String generateSql(List<Object> params) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("select ");
		generateTableViewSelectSqlClause(stringBuffer);
		stringBuffer.append(" from ").append(tableName);
		String aliasName = getTableAliasName(null);
		if (!StringUtil.isBlank(aliasName)) {
			stringBuffer.append(" ").append(aliasName);
		}
		generateQuerySqlClause(aliasName, conditionFields, params, stringBuffer);
		generateOrderSqlClause(stringBuffer);
		return stringBuffer.toString();
	}

	@SuppressWarnings("unchecked")
	public Bean[] getBeans(Bean bean) {
		contextReplace(bean);
		List<Object> subParams = new ArrayList<Object>();
		String sql = generateSql(subParams);
		return operator.getBeans(sql, subParams);
	}

	/**
	 * 
	 * 树形式的sql条件片段
	 * 
	 * @param params
	 * @param stringBuffer
	 */
	private void generateTreeQuerySqlClause(List<Object> params,
			StringBuffer stringBuffer) {
		StringBuffer conditionBuffer = new StringBuffer();
		boolean hasCondition = false;
		String connectMode = null;
		if (conditionFields != null && conditionFields.size() > 0) {
			for (int i = 0; i < conditionFields.size(); i++) {
				ConditionField conditionField = conditionFields.get(i);
				String propertyName = fieldId2PropertyName.get(conditionField
						.getFieldId());
				String dbFieldName = fieldId2DbFieldName.get(conditionField
						.getFieldId());
				if (context.get(propertyName) != null) {
					String compareMode = checkCompareMode(
							conditionField.getCompareMode(), propertyName);
					CompareMode compare = getCompareMode(compareMode);
					if (compare != null) {
						appendLastConnectMode(hasCondition, connectMode,
								conditionBuffer);
						appendCondition(dbFieldName, propertyName,
								getTableAliasName(null), conditionBuffer,
								compare, params);
						compare.assembleParamterValue(propertyName, context,
								params);
						hasCondition = true;
						connectMode = conditionField.getConnectMode();
					} else {
						throw new TinySysRuntimeException(
								"ientity.notFountCompareMode", compareMode);
					}
				} else {
					CompareMode compare = new IsNullCompareMode();
					appendLastConnectMode(hasCondition, connectMode,
							conditionBuffer);
					appendCondition(dbFieldName, propertyName,
							getTableAliasName(null), conditionBuffer, compare,
							params);
					hasCondition = true;
					connectMode = conditionField.getConnectMode();
				}
			}
		}
		if (conditionBuffer.length() > 0) {
			stringBuffer.append(" where ").append(conditionBuffer.toString());
		}

	}

}
