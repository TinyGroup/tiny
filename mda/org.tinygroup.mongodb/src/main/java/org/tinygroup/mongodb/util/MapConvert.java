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
package org.tinygroup.mongodb.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.context.Context;
import org.tinygroup.mongodb.common.Field;
import org.tinygroup.mongodb.common.ObjectField;
import org.tinygroup.mongodb.common.Operation;
import org.tinygroup.mongodb.common.OperationField;
import org.tinygroup.mongodb.common.OperationGroup;
import org.tinygroup.mongodb.engine.MongoField;
import org.tinygroup.mongodb.model.MongoDBModel;

import com.mongodb.BasicDBObject;

public class MapConvert {

	private Context context;
	private MongoDBModel model;
	private Operation operation;
	Map<String, Boolean> objectStatus=new HashMap<String, Boolean>();

	public MapConvert(MongoDBModel model, Operation operation, Context context) {
		this.context = context;
		this.model = model;
		this.operation = operation;
	}

	public Map<String, Object> deal(Context newContext) {
		Map<String, Object> map = new HashMap<String, Object>();
		initOperationObject(operation.getOperationGroup(), map);
		return map;
	}

	public void insertOperationObject(OperationGroup operationGroup,
			Map<String, Object> map) {
		if (operationGroup != null) {
			List<OperationField> operationFields = operationGroup.getFields();
			for (OperationField operationField : operationFields) {
				MongoField mongoField = model.getMongoField(operationField
						.getFieldId());
				ObjectField of = mongoField.getObjectField();
				if (of == null) {
					dealField(mongoField, map, "", operationField.isHidden());
				} else {
					if (of.isArray()) {
						if (map.get(of.getName()) == null) {
							map.put(of.getName(),
									new ArrayList<Map<String, Object>>());
							objectStatus.put(of.getName(), true);
						}
					} else {
						if (map.get(of.getName()) == null) {
							map.put(of.getName(), new HashMap<String, Object>());
						}
					}
					dealInsertObjectField(of, mongoField, map, "",
							operationField.isHidden());
				}
			}
			if (!CollectionUtil.isEmpty(operationGroup.getOperationGroups())) {
				for (OperationGroup group : operationGroup.getOperationGroups()) {
					initOperationObject(group, map);
				}
			}

		}
	}

	public void addObjectArrayOperationObject(OperationGroup operationGroup,
			Map<String, Object> addToSetMap,Map<String, Object> commonMap) {
		if (operationGroup != null) {
			List<OperationField> operationFields = operationGroup.getFields();
			for (OperationField operationField : operationFields) {
				MongoField mongoField = model.getMongoField(operationField
						.getFieldId());
				ObjectField of = mongoField.getObjectField();
				if (of != null) {
					if (of.isArray()) {
						Map<String, Object> subMap=(Map<String, Object>) addToSetMap.get(of.getName());
						if (subMap == null) {
							subMap = new HashMap<String, Object>();
							subMap.put("$each",
									new ArrayList<Map<String, Object>>());
							addToSetMap.put(of.getName(), subMap);
							objectStatus.put(of.getName(), true);
						}
						dealAddObjectModel(of, mongoField, addToSetMap, "",
								operationField.isHidden());
					}else{//不是数组格式
						Map<String, Object> objectMap = (Map<String, Object>) commonMap.get(of
								.getName());
						if (objectMap == null) {
							objectMap= new HashMap<String, Object>();
							commonMap.put(of.getName(),objectMap);
						}
						String propertyName = mongoField.getFieldName();
						Object value = context.get(propertyName);
						if (value == null) {
							if (operationField.isHidden()) {
								value = mongoField.getFieldDefaultValue();
							}
						}
						Field field = mongoField.getField();
						objectMap.put(field.getName(), value);
					}
				}else{
					dealField(mongoField, commonMap, "", operationField.isHidden());
				}
			}
//			if (!CollectionUtil.isEmpty(operationGroup.getOperationGroups())) {
//				for (OperationGroup group : operationGroup.getOperationGroups()) {
//					initOperationObject(group, map);
//				}
//			}

		}
	}
	
	public void removeObjectOperationObject(OperationGroup operationGroup,
			Map<String, Object> map) {
		if (operationGroup != null) {
			List<OperationField> operationFields = operationGroup.getFields();
			for (OperationField operationField : operationFields) {
				MongoField mongoField = model.getMongoField(operationField
						.getFieldId());
				ObjectField of = mongoField.getObjectField();
				if (of != null) {
					if (of.isArray()) {
						if (map.get("$pull") == null) {
							Map<String, Object> removeMap=new HashMap<String, Object>();
							map.put("$pull", removeMap);
							Map<String, Object> subMap=new HashMap<String, Object>();
							removeMap.put(of.getName(), subMap);
						}//删除对象数组中元素
						dealRemoveObjectModel(of, mongoField, map, "",
								operationField.isHidden());
					}else{
						Map<String, Object> removeMap=new HashMap<String, Object>();
						map.put("$unset", removeMap);
						removeMap.put(of.getName(), 1);
						break;//移除对象字段
					}
				}
			}
			if (!CollectionUtil.isEmpty(operationGroup.getOperationGroups())) {
				for (OperationGroup group : operationGroup.getOperationGroups()) {
					initOperationObject(group, map);
				}
			}

		}
	}

	public void updateOperationObject(OperationGroup operationGroup,
			Map<String, Object> map) {
		if (operationGroup != null) {
			List<OperationField> operationFields = operationGroup.getFields();
			for (OperationField operationField : operationFields) {
				MongoField mongoField = model.getMongoField(operationField
						.getFieldId());
				ObjectField of = mongoField.getObjectField();
				if (of == null) {
					dealField(mongoField, map, "", operationField.isHidden());
				} else {
					dealUpdateObjectField(of, mongoField, map, "",
							operationField.isHidden());
				}
			}
			if (!CollectionUtil.isEmpty(operationGroup.getOperationGroups())) {
				for (OperationGroup group : operationGroup.getOperationGroups()) {
					initOperationObject(group, map);
				}
			}

		}
	}

	public void initOperationObject(OperationGroup operationGroup,
			Map<String, Object> map) {
		if (operationGroup != null) {
			List<OperationField> operationFields = operationGroup.getFields();
			for (OperationField operationField : operationFields) {
				MongoField mongoField = model.getMongoField(operationField
						.getFieldId());
				ObjectField of = mongoField.getObjectField();
				if (of == null) {
					dealField(mongoField, map, "", operationField.isHidden());
				} else {
					dealObjectField(of, map, "", operationField.isHidden());
				}
				// } else {
				// dealObjectField(of, map, "", operationField.isHidden());
				// }
			}
			if (!CollectionUtil.isEmpty(operationGroup.getOperationGroups())) {
				for (OperationGroup group : operationGroup.getOperationGroups()) {
					initOperationObject(group, map);
				}
			}

		}
	}

	private void dealAddObjectModel(ObjectField of, MongoField mongoField,
			Map<String, Object> map, String parentName, boolean hidden) {
		String propertyName = mongoField.getFieldName();
		Map<String, Object> addMap=(Map<String, Object>) map.get(of.getName());
		List<Map<String, Object>> objectList = (List<Map<String, Object>>) addMap.get("$each");
		Object value = context.get(propertyName);
		if (value!=null&&value.getClass().isArray()) {
			Object[] values=(Object[])value;
			if(objectStatus.get(of.getName())){
				for (int i = 0; i < values.length; i++) {
					Map<String, Object> subMap=new HashMap<String, Object>();
					objectList.add(subMap);
				}
				objectStatus.put(of.getName(), false);
			}
			Field field = mongoField.getField();
			for (int i = 0; i < values.length; i++) {
				Map<String, Object> subMap = objectList.get(i);
				if (values[i] == null) {
					if (hidden) {
						values[i] = mongoField.getFieldDefaultValue();
					}
				}
				subMap.put(field.getName(), values[i]);
			}
		}else{
			if(objectStatus.get(of.getName())){
				Map<String, Object> subMap=new HashMap<String, Object>();
				objectList.add(subMap);
				objectStatus.put(of.getName(), false);
			}
			Field field = mongoField.getField();
			Map<String, Object> subMap = objectList.get(0);
			if (value == null) {
				if (hidden) {
					value = mongoField.getFieldDefaultValue();
				}
			}
			subMap.put(field.getName(), value);
		}
	}

	private void dealInsertObjectField(ObjectField of, MongoField mongoField,
			Map<String, Object> map, String string, boolean hidden) {
		String propertyName = mongoField.getFieldName();
		if (of.isArray()) {// 暂时只支持两层嵌套
			List<Map<String, Object>> objectList = (List<Map<String, Object>>) map
					.get(of.getName());
			Object value = context.get(propertyName);
			if (value!=null) {
				if(value.getClass().isArray()){
					Object[] values=(Object[])value;
					if(objectStatus.get(of.getName())){
						for (int i = 0; i < values.length; i++) {
							Map<String, Object> subMap=new HashMap<String, Object>();
							objectList.add(subMap);
						}
						objectStatus.put(of.getName(), false);
					}
					Field field = mongoField.getField();
					for (int i = 0; i < values.length; i++) {
						Map<String, Object> subMap = objectList.get(i);
						if (values[i] == null) {
							if (hidden) {
								values[i] = mongoField.getFieldDefaultValue();
							}
						}
						subMap.put(field.getName(), values[i]);
					}
				}else{
					if(objectStatus.get(of.getName())){
						Map<String, Object> subMap=new HashMap<String, Object>();
						objectList.add(subMap);
						objectStatus.put(of.getName(), false);
					}
					Field field = mongoField.getField();
					Map<String, Object> subMap = objectList.get(0);
					if (value == null) {
						if (hidden) {
							value = mongoField.getFieldDefaultValue();
						}
					}
					subMap.put(field.getName(), value);
				}
				
			}

		} else {
			Map<String, Object> objectMap = (Map<String, Object>) map.get(of
					.getName());
			Object value = context.get(propertyName);
			if (value == null) {
				if (hidden) {
					value = mongoField.getFieldDefaultValue();
				}
			}
			Field field = mongoField.getField();
			objectMap.put(field.getName(), value);
		}

	}

	private void dealRemoveObjectModel(ObjectField of, MongoField mongoField,
			Map<String, Object> map, String string, boolean hidden) {
		String propertyName = mongoField.getFieldName();
		Map<String, Object> removeMap=(Map<String, Object>) map.get("$pull");
		Map<String, Object> objectMap = (Map<String, Object>) removeMap.get(of
				.getName());
		Object value = context.get(propertyName);
		if (value == null) {
			if (hidden) {
				value = mongoField.getFieldDefaultValue();
			}
		}
		Field field = mongoField.getField();
		if(value!=null){
			objectMap.put(field.getName(), value);
		}
	}
	
	
	
	private void dealUpdateObjectField(ObjectField of, MongoField mongoField,
			Map<String, Object> map, String string, boolean hidden) {
		String propertyName = mongoField.getFieldName();
		Object value = context.get(propertyName);
		if (value == null) {
			if (hidden) {
				value = mongoField.getFieldDefaultValue();
			}
		}
		if (of.isArray()) {
			Field field = mongoField.getField();
			map.put(of.getName() + ".$." + field.getName(), value);
		} else {
			map.put(propertyName, value);
		}

	}

	private void dealField(MongoField field, Map<String, Object> map,
			String parentName, boolean isHidden) {
		String propertyName = field.getFieldName();
		Object value = context.get(propertyName);
		if (value == null) {
			if (isHidden) {
				value = field.getFieldDefaultValue();
			}
		}
		if(value!=null){
			map.put(propertyName, value);
		}
	}

	/*
	 * 
	 * 
	 * name:"lily" age:11 pic: [ { obj:[
	 * {name:"1",age:11,address:[{provice:[{name
	 * :p1},{name:p11}]},{provice:[{name:p111}]}]},
	 * {name:"2",age:12,address:[{provice
	 * :[{name:p2},{name:p21}]},{provice:[{name:p221}]}]},
	 * {name:"3",age:13,address
	 * :[{provice:[{name:p3},{name:p31}]},{provice:[{name:p331}]}]}] ,
	 * name:"aa1" },
	 * 
	 * { obj:[
	 * {name:"4",age:14,address:[{provice:[{name:p4},{name:p41},{name:p441
	 * }]},{provice:[{name:p4441}]}]},
	 * {name:"5",age:15,address:[{provice:[{name:
	 * p5},{name:p51}]},{provice:[{name:p551}]}]},
	 * {name:"6",age:16,address:[{provice
	 * :[{name:p6}]},{provice:[{name:p661},{name:p6661},{name:p66661}]}]}] ,
	 * name:"aa2" } ],
	 * 
	 * location:[
	 * {name:"l1",where:[{place:"p1",controy:{name:"c1"}},{place:"p12"
	 * ,controy:{name:"c2"}]} {name:"l2",where:[{place:"p2"}]} ]
	 * 
	 * }
	 */

	/**
	 * 处理第一层的数据对象
	 * 
	 * @param of
	 * @param map
	 * @param parentName
	 * @param isHidden
	 */
	private void dealObjectField(ObjectField of, Map<String, Object> map,
			String parentName, boolean isHidden) {
		// String fieldPathName = get(parentName, of.getName());
		String fieldPathName = getName(of.getId());
		String fieldName = of.getName();
		if (of.isArray()) {
			String arrayLength = get(fieldPathName, "TINY_LENGTH");
			String lengthString = (String) context.get(arrayLength);
			if (lengthString == null || "".equals(lengthString)) {
				return;
			}
			int firstLength = Integer.parseInt(lengthString);// 2
			int[] lengthArray = new int[] { firstLength };

			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < lengthArray[0]; i++) {// length = 2
				mapList.add(new HashMap<String, Object>());
			}
			for (ObjectField child : of.getObjectFields()) {
				dealObjectFieldArray(child, fieldPathName, isHidden,
						lengthArray, mapList);
			}
			for (Field child : of.getFields()) {
				dealFieldArray(child, fieldPathName, mapList);
			}
			map.put(fieldName, mapList);
		} else {
			for (ObjectField child : of.getObjectFields()) {
				dealObjectField(child, map, fieldPathName, isHidden);
			}
			for (Field child : of.getFields()) {
				// dealField(child, map, fieldPathName, isHidden);
			}
		}

	}

	private void dealFieldArray(Field field, String parentName,
			List<Map<String, Object>> mapList) {
		String propertyName = get(parentName, field.getName());
		Object[] value = context.get(propertyName);
		for (int i = 0; i < mapList.size(); i++) {
			mapList.get(i).put(field.getName(), value[i]);
		}
	}

	@SuppressWarnings("unchecked")
	private void dealObjectFieldArray(ObjectField of, String parentName,
			boolean isHidden, int[] parentArray,// [2]
			List<Map<String, Object>> mapList) {
		String fieldPathName = getName(of.getId());
		String fieldName = of.getName();
		// get(parentName, of.getName());// pic.obj
		if (of.isArray()) {
			String arrayLength = get(fieldPathName, "TINY_LENGTH");// pic.obj.tinylength
			int[] lengthArray = context.get(arrayLength);// [3,3]
			List<Map<String, Object>> thisMapList = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < mapList.size(); i++) {
				List<Map<String, Object>> subMapList = new ArrayList<Map<String, Object>>();
				for (int j = 0; j < lengthArray[i]; j++) {
					Map<String, Object> map = new HashMap<String, Object>();
					subMapList.add(map);
					thisMapList.add(map);
				}
				// mapList.get(i).put(fieldPathName, subMapList);
				mapList.get(i).put(fieldName, subMapList);
			}

			for (Field child : of.getFields()) {
				String childName = child.getName();
				String propertyName = get(fieldPathName, childName);

				Object[] value = context.get(propertyName);
				int length = 0;
				for (int i = 0; i < mapList.size(); i++) {
					int oldLength = length;
					length = length + lengthArray[i];
					for (int j = oldLength, m = 0; j < length; j++, m++) {
						List<Map<String, Object>> mapl = (List<Map<String, Object>>) mapList
								.get(i).get(fieldName);
						mapl.get(m).put(childName, value[j]);
					}
				}
			}
			for (ObjectField child : of.getObjectFields()) {
				dealObjectFieldArray(child, fieldPathName, isHidden,
						lengthArray, thisMapList);
			}

		} else {

			List<Map<String, Object>> subMapList = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < mapList.size(); i++) {
				Map<String, Object> subMap = new HashMap<String, Object>();
				mapList.get(i).put(of.getName(), subMap);
				subMapList.add(subMap);
			}
			for (Field child : of.getFields()) {
				String childName = child.getName();
				String propertyName = get(fieldPathName, childName);
				Object[] value = context.get(propertyName);
				for (int i = 0; i < mapList.size(); i++) {
					subMapList.get(i).put(childName, value[i]);
				}
			}
			for (ObjectField child : of.getObjectFields()) {
				String propertyName = get(fieldPathName, child.getName());
				dealObjectFieldArray(child, propertyName, isHidden,
						parentArray, subMapList);
			}

		}
	}

	private String get(String parentName, String name) {
		if (parentName == null || "".equals(parentName))
			return name;
		return parentName + "." + name;
	}

	private String getName(String fieldId) {
		return model.getMongoField(fieldId).getFieldName();
	}

}
