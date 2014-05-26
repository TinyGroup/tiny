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
package org.tinygroup.mongodb.engine.view;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.BSONObject;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.context.Context;
import org.tinygroup.imda.processor.ParameterBuilder;
import org.tinygroup.mongodb.common.ConditionField;
import org.tinygroup.mongodb.common.DisplayField;
import org.tinygroup.mongodb.common.Field;
import org.tinygroup.mongodb.common.GroupField;
import org.tinygroup.mongodb.common.HavingField;
import org.tinygroup.mongodb.common.MongoRelation;
import org.tinygroup.mongodb.common.ObjectField;
import org.tinygroup.mongodb.common.OrderField;
import org.tinygroup.mongodb.common.RelationCondition;
import org.tinygroup.mongodb.common.View;
import org.tinygroup.mongodb.common.ViewGroup;
import org.tinygroup.mongodb.engine.MongoDbContext;
import org.tinygroup.mongodb.engine.MongoField;
import org.tinygroup.mongodb.engine.PageInfo;
import org.tinygroup.mongodb.engine.view.extendinfo.FieldMaps;
import org.tinygroup.mongodb.engine.view.tree.TreeViewParamterBuilder;
import org.tinygroup.mongodb.model.MongoDBModel;
import org.tinygroup.mongodb.util.ModelUtil;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 
 * 功能说明: 视图的上下文
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-11-27 <br>
 * <br>
 */
public class MongoViewContext extends MongoDbContext {

	private View view;

	private List<DisplayField> statistics = new ArrayList<DisplayField>();

	private Map<String, MongoRelationData> mongoRelations = new HashMap<String, MongoRelationData>();

	private DBObject displayObject;

	private DBObject groupObject;

	private String nestedObject;// 保存嵌套对象的属性

	public MongoViewContext(MongoDBModel model, View view, Context context) {
		super(model, context);
		this.view = view;
		displayObject = generateDisplayObject();
		groupObject = generateGroupObject();
		List<MongoRelation> relations = view.getRelations();
		for (MongoRelation mongoRelation : relations) {
			List<RelationCondition> conditions = mongoRelation.getConditions();
			for (RelationCondition condition : conditions) {
				String sourceRelationField = condition.getSourceRelationField();
				MongoField field = getMongoField(sourceRelationField);
				String objectFieldName = null;
				ObjectField of = field.getObjectField();
				if (of != null) {
					objectFieldName = of.getName();
				}
				String sourcePropertyName = field.getField().getName();
				String targetRelationField = condition.getTargetRelationField();
				mongoRelations.put(sourcePropertyName, new MongoRelationData(
						objectFieldName, sourcePropertyName,
						targetRelationField, mongoRelation));
			}
		}

	}

	public PageInfo queryMongoModel() {

		if (view != null) {
			DBObject conditionObject = generateConditionObject();
			DBObject havingObject = generateHavingConditionObject();
			DBObject orderObject = generateOrderObject();
			int pageSize = checkPageSize();
			String pageNumStr = context.get(ParameterBuilder.PAGE_NUMBER);
			int pageNum = 1;
			if (pageNumStr != null) {
				pageNum = Integer.parseInt(pageNumStr);
			}
			PageInfo pageInfo = persistence.find(displayObject,
					conditionObject, groupObject, havingObject, orderObject,
					pageNum, pageSize);

			// 查询数组时返回记录不会过滤不符合条件的数组记录，需要进行代码过滤掉
			BSONObject[] records = pageInfo.getObjects();
			if (records != null) {
				for (BSONObject record : records) {
					filterArrayRecords(record);
				}
			}
			return pageInfo;
		}

		return null;
	}

	/**
	 * 
	 * 根据嵌套数组的条件,过滤生成符合的记录
	 * 
	 * @param record
	 */
	private void filterArrayRecords(BSONObject record) {
		List<ConditionField> conditionFields = view.getConditionFields();
		for (ConditionField conditionField : conditionFields) {
			MongoField mongoField = getMongoField(conditionField.getFieldId());
			ObjectField of = mongoField.getObjectField();
			String fieldName = mongoField.getField().getName();
			if (of != null) {
				if (of.isArray()) {
					String propertyName = mongoField.getFieldName();
					Object value = context.get(propertyName);
					if (value != null) {// 参数值为null不在从数组字段过滤结果集
						Object itemArray = record.get(of.getName());// 数组字段
						if (itemArray != null) {
							BasicDBList newList = new BasicDBList();
							if (itemArray instanceof BasicDBList) {
								BasicDBList itemList = (BasicDBList) itemArray;
								for (Object item : itemList) {
									BSONObject itemRecord = (BSONObject) item;
									if (itemRecord.get(fieldName).equals(value)) {
										newList.add(itemRecord);
									}
								}
								record.put(of.getName(), newList);
							}
						}
					}

				}
			}
		}
	}

	/**
	 * 
	 * 文档中嵌套数组对象的分页
	 * 
	 * @return
	 */
	public PageInfo nestedArrayQuery() {
		PageInfo pageInfo = new PageInfo();
		int pageSize = checkPageSize();
		String pageNumStr = context.get(ParameterBuilder.PAGE_NUMBER);
		int pageNum = 1;
		if (pageNumStr != null) {
			pageNum = Integer.parseInt(pageNumStr);
		}
		int size = 0;

		if (view != null) {
			DBObject conditionObject = generateConditionObject();
			DBObject havingObject = generateHavingConditionObject();
			DBObject orderObject = generateOrderObject();
			DBObject displayObject = generateNestedDisplayObject();
			BSONObject[] records = persistence.find(displayObject,
					conditionObject, groupObject, havingObject, orderObject);
			if (records != null && records.length == 1) {
				BSONObject record = records[0];
				filterArrayRecords(record);// 根据嵌套字段条件进行过滤
				Object object = record.get(nestedObject);
				if (object != null) {
					if (object instanceof BasicDBList) {
						BasicDBList nestedObjects = (BasicDBList) object;
						size = nestedObjects.size();
						pageInfo.pageAttributeSet(pageSize, pageNum, size);
						int start = pageInfo.getStart();
						int length = start + pageInfo.getPageSize();
						int pageLength = pageInfo.getPageSize();
						;
						if (size < length) {
							pageLength = size - start;
						}
						BSONObject[] pageObjects = new BSONObject[pageLength];
						for (int i = start, j = 0; j < pageLength; i++, j++) {
							BSONObject bsonObject = (BSONObject) nestedObjects
									.get(i);
							pageObjects[j] = bsonObject;
						}
						pageInfo.setObjects(pageObjects);
						return pageInfo;
					}
				}

			} else {
				pageInfo.pageAttributeSet(pageSize, pageNum, size);
				pageInfo.setObjects(new BSONObject[0]);
			}
		}

		return pageInfo;

	}

	/**
	 * 
	 * 关联查询
	 * 
	 * @return
	 */
	public PageInfo relationQueryMongoModel() {

		if (view != null) {
			BSONObject[] records = getRecords();
			if (records != null) {
				List<BSONObject> bsonObjects = new ArrayList<BSONObject>();
				for (BSONObject record : records) {
					List<BSONObject> objects = new ArrayList<BSONObject>();
					boolean first = true;
					for (String propertyName : mongoRelations.keySet()) {
						if (first) {
							BSONObject object = new BasicDBObject();
							object.putAll(record);
							objects.add(object);
							first = false;
						}
						MongoRelationData data = mongoRelations
								.get(propertyName);
						objects = data.generateObjects(objects);
					}
					bsonObjects.addAll(objects);
				}
				int pageSize = checkPageSize();
				String pageNumStr = context.get(ParameterBuilder.PAGE_NUMBER);
				int pageNum = 1;
				if (pageNumStr != null) {
					pageNum = Integer.parseInt(pageNumStr);
				}
				int size = bsonObjects.size();
				PageInfo pageInfo = new PageInfo();
				pageInfo.pageAttributeSet(pageSize, pageNum, size);
				int start = pageInfo.getStart();
				int length = start + pageInfo.getPageSize();
				int pageLength = pageInfo.getPageSize();
				if (size < length) {
					pageLength = size - start;
				}
				BSONObject[] pageObjects = new BSONObject[pageLength];
				for (int i = start, j = 0; j < pageLength; i++, j++) {
					BSONObject bsonObject = bsonObjects.get(i);
					pageObjects[j] = bsonObject;
				}
				pageInfo.setObjects(pageObjects);
				return pageInfo;
			}

		}

		return null;
	}

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
	 * 树查询
	 * 
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T[] queryMongoModelOfTree(Class<T> clazz) {
		if (view != null) {
			BSONObject[] records = getRecords();
			if (records != null) {
				String queryAll = context
						.get(TreeViewParamterBuilder.QUERY_ALL_TREE);
				FieldMaps maps = new FieldMaps(this, context);
				maps.parserExtendInfos(view.getExtendInformation());
				T[] treeNodes = (T[]) Array.newInstance(clazz, records.length);
				for (int i = 0; i < records.length; i++) {
					if (!StringUtil.isBlank(queryAll)
							&& queryAll.equals("true")) {
						BSONObject[] subRecords = getSubRecords(records[i]);
						treeNodes[i] = maps.object2TreeNodeWithRecursive(
								records[i], subRecords, clazz);
					} else {
						treeNodes[i] = maps.object2TreeNode(records[i], clazz);
					}
				}
				return treeNodes;
			}
		}
		return null;

	}

	private BSONObject[] getRecords() {
		DBObject conditionObject = generateConditionObject();
		DBObject havingObject = generateHavingConditionObject();
		DBObject orderObject = generateOrderObject();
		BSONObject[] records = persistence.find(displayObject, conditionObject,
				groupObject, havingObject, orderObject);
		return records;
	}

	public BSONObject[] getSubRecords(BSONObject bsonObject) {
		contextReplace(bsonObject);
		return getRecords();
	}

	/**
	 * 
	 * 改变参数值
	 * 
	 * @param bsonObject
	 */
	private void contextReplace(BSONObject bsonObject) {
		List<ConditionField> conditionFields = view.getConditionFields();
		for (ConditionField conditionField : conditionFields) {
			String propertyName = getFieldName(conditionField.getFieldId());
			if (bsonObject.containsField(propertyName)) {
				Object value = bsonObject.get(propertyName);
				context.put(propertyName, value);
			}
		}
	}

	/**
	 * 
	 * view的参数组装
	 * 
	 * @param newContext
	 * @param conditionFields
	 * @return
	 */
	public Context buildConditionParameter(Context newContext,
			List<ConditionField> conditionFields) {
		super.buildConditionFieldParamter(conditionFields, newContext);
		if (view.getHavingFields() != null) {
			for (HavingField havingField : view.getHavingFields()) {
				processHavingParamter(havingField, newContext);
			}
		}
		return newContext;
	}

	public DBObject generateDisplayObject() {
		List<ViewGroup> viewGroups = view.getViewGroups();
		if (viewGroups != null && viewGroups.size() > 0) {
			DBObject display = new BasicDBObject();
			for (ViewGroup viewGroup : viewGroups) {
				List<DisplayField> displayFields = viewGroup.getFields();
				for (DisplayField displayField : displayFields) {
					MongoField field = getMongoField(displayField.getFieldId());
					String propertyName = field.getFieldName();
					String aggregateFunction = displayField
							.getAggregateFunction();
					if (aggregateFunction != null) {
						statistics.add(displayField);
					}
					display.put(propertyName, 1);
				}
			}
			return display;
		}
		return null;
	}

	public DBObject generateNestedDisplayObject() {
		List<ViewGroup> viewGroups = view.getViewGroups();
		if (viewGroups != null && viewGroups.size() > 0) {
			DBObject display = new BasicDBObject();
			for (ViewGroup viewGroup : viewGroups) {
				List<DisplayField> displayFields = viewGroup.getFields();
				for (DisplayField displayField : displayFields) {
					MongoField field = getMongoField(displayField.getFieldId());
					ObjectField of = field.getObjectField();
					if (of != null) {
						if (nestedObject == null) {
							nestedObject = of.getName();
						}
						String propertyName = field.getFieldName();
						display.put(propertyName, 1);
					}

				}
			}
			return display;
		}
		return null;
	}

	/**
	 * 
	 * 生成view的条件BSONObject
	 * 
	 * @return
	 */
	public DBObject generateConditionObject() {
		return generateConditionObject(view.getConditionFields());
	}

	/**
	 * 
	 * 生成view的having条件BSONObject
	 * 
	 * @return
	 */
	public DBObject generateHavingConditionObject() {
		List<ConditionField> conditionFields = new ArrayList<ConditionField>();
		conditionFields.addAll(view.getHavingFields());
		return generateConditionObject(conditionFields);
	}

	/**
	 * 
	 * 生成view的分组BSONObject
	 * 
	 * @return
	 */
	public DBObject generateGroupObject() {
		List<GroupField> groupFields = view.getGroupFields();
		if (!CollectionUtil.isEmpty(groupFields)) {
			BSONObject _group = new BasicDBObject();
			for (GroupField groupField : groupFields) {
				MongoField field = getMongoField(groupField.getFieldId());
				String propertyName = field.getFieldName();
				_group.put(propertyName, "$" + propertyName);
			}
			DBObject objectFields = new BasicDBObject("_id", _group);
			for (DisplayField displayField : statistics) {
				MongoField field = getMongoField(displayField.getFieldId());
				String propertyName = field.getFieldName();
				String aggregateFunction = displayField.getAggregateFunction();
				if (aggregateFunction != null) {
					objectFields.put(propertyName + aggregateFunction,
							new BasicDBObject("$" + aggregateFunction, "$"
									+ propertyName));
				}
			}
			return objectFields;
		}
		return null;
	}

	public DBObject generateOrderObject() {
		List<OrderField> orderFields = view.getOrderFields();
		if (!CollectionUtil.isEmpty(orderFields)) {
			DBObject order = new BasicDBObject();
			for (OrderField orderField : orderFields) {
				MongoField field = getMongoField(orderField.getFieldId());
				String propertyName = field.getFieldName();
				if (orderField.getOrderMode() != null
						&& orderField.getOrderMode().equals(DESC)) {
					order.put(propertyName, -1);
				} else {
					order.put(propertyName, 1);
				}
			}
			return order;
		}
		return null;
	}

	private void processHavingParamter(HavingField havingField,
			Context newContext) {
		Field field = getField(havingField.getFieldId());
		String propertyName = field.getName();
		String paramName = ModelUtil.getSpliceParamterName(propertyName,
				HAVING_FIELD);
		Object value = context.get(paramName);
		if (value == null) {
			if (havingField.isHidden()) {
				value = field.getDefaultValue();
			}
		}
		if (value != null) {
			newContext.put(paramName, value);
		}
		String compareParamterName = ModelUtil.getSpliceParamterName(
				propertyName, HAVING_COMPARE_MODE);
		Object compareModeValue = context.get(compareParamterName);
		if (compareModeValue != null) {
			newContext.put(compareParamterName, compareModeValue);
		}
	}

	public boolean hasGroupByField() {
		return !CollectionUtil.isEmpty(view.getGroupFields());
	}

	public boolean hasHavingField() {
		return !CollectionUtil.isEmpty(view.getHavingFields());
	}

	public boolean hasOrderField() {
		return !CollectionUtil.isEmpty(view.getOrderFields());
	}

}
