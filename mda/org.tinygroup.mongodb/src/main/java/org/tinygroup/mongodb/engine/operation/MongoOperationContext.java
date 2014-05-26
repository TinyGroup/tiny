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
package org.tinygroup.mongodb.engine.operation;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.BSONObject;
import org.bson.types.BasicBSONList;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.context.Context;
import org.tinygroup.imda.util.MdaUtil;
import org.tinygroup.mongodb.common.*;
import org.tinygroup.mongodb.engine.MongoDbContext;
import org.tinygroup.mongodb.engine.MongoField;
import org.tinygroup.mongodb.model.MongoDBModel;
import org.tinygroup.mongodb.util.MapConvert;
import org.tinygroup.weblayer.webcontext.parser.fileupload.TinyFileItem;
import org.tinygroup.weblayer.webcontext.parser.impl.ItemFileObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 
 * 功能说明: 操作的上下文，包括参数组装和服务处理
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-11-27 <br>
 * <br>
 */
public class MongoOperationContext extends MongoDbContext {

	private Operation operation;

	public MongoOperationContext(MongoDBModel model, Operation operation,
			Context context) {
		super(model, context);
		this.operation = operation;
	}

	/**
	 * 
	 * 插入操作服务
	 */
	public BSONObject insertMongoModel() {
		if (operation == null) {
			return null;
		}
		DBObject insert = new BasicDBObject();
		insertOperationObject(operation.getOperationGroup(), insert);
		return persistence.insert(insert);
	}

	/**
	 * 
	 * 嵌套数组对象插入操作服务
	 */
	public BSONObject addObjectArrayModel() {
		if (operation != null) {
			DBObject objectArray = new BasicDBObject();
			DBObject common = new BasicDBObject();
			addObjectArrayOperationObject(operation.getOperationGroup(), objectArray,common);
			DBObject condition = generateConditionObject();
			if(common.keySet().size()>0){
				persistence.update(condition, common);//先增加普通属性
			}
			return persistence.insertObjectArrayModel(condition, objectArray);//再更新数组对象字段
		}
		return null;
	}

	/**
	 * 
	 * 嵌套对象删除操作服务
	 */
	public BSONObject removeObjectModel() {
		if (operation != null) {
			DBObject update = new BasicDBObject();
			removeObjectOperationObject(operation.getOperationGroup(), update);
			DBObject condition = generateConditionObject();
			return persistence.removeObjectModel(condition, update);
		}
		return null;
	}

	/**
	 * 
	 * 更新操作服务
	 * 
	 * @return
	 */
	public Long updateMongoModel() {
		if (operation != null) {
			DBObject update = new BasicDBObject();
			updateOperationObject(operation.getOperationGroup(), update);
			DBObject condition = generateConditionObject();
			return persistence.update(condition, update);
		}
		return null;
	}

	public Long deleteMongoModel() {
		if (operation != null) {
			DBObject condition = generateConditionObject();
			// 删除操作
			return persistence.delete(condition);
		}

		return null;
	}

	public BSONObject[] viewMongoModel() {
		if (operation != null) {
			DBObject selectObject = generateOperationSelectObject();
			DBObject conditionObject = generateConditionObject();
			// 查询操作
			return persistence.find(selectObject, conditionObject);
		}
		return null;
	}

	/**
	 * 
	 * 删除确认服务的参数组装成bsonobject对象
	 * 
	 * @return
	 */
	public BSONObject createBSONObject() {
		return generateConditionObject();
	}

	/**
	 * 
	 * 只组装操作字段参数
	 * 
	 * @return
	 */
	public Context buildOperationFieldParamter(OperationGroup operationGroup,
			Context newContext) {
		if (operationGroup != null) {
			List<OperationField> operationFields = operationGroup.getFields();
			for (OperationField operationField : operationFields) {
				MongoField mongoField = getMongoField(operationField
						.getFieldId());
				Field field=mongoField.getField();
				String propertyName = mongoField.getFieldName();
				Object value = context.get(propertyName);
				if(value==null){//mongoField.getFieldName()可能是对象嵌套属性例如：aaa.name,不存在参数值则获取name属性的参数值
					value=context.get(field.getName());
				}
				if (value != null) {
					if (value.getClass().isArray()) {//如果是文件表单字段，那么需要对参数值进行特殊处理。
						Object[] values=(Object[])value;
						for (int i = 0; i < values.length; i++) {
							if(values[i] instanceof ItemFileObject){
								ItemFileObject fileObject=(ItemFileObject)values[i];
								TinyFileItem fileItem= (TinyFileItem)fileObject.getFileItem();
								values[i]=fileItem.getUnique();
							}
						}
					} else {
						if(value instanceof ItemFileObject){//如果是文件类型表单字段
							ItemFileObject fileObject=(ItemFileObject)value;
							TinyFileItem fileItem= (TinyFileItem)fileObject.getFileItem();
							value=fileItem.getUnique();
						}
					}
					if(field.isEncrypt()){//加密字段特殊处理
						try {
							value=cryptor.encrypt(String.valueOf(value));
						} catch (Exception e) {
							logger.errorMessage("encrypt error", e);
						}
					}
				} else {//参数值为null，是否需要进行特殊处理
					if (mongoField.isObjectIdField()) {
						value = UUID.randomUUID().toString()
								.replaceAll("-", "");
					}
				}
				//对参数值进行类型转换处理
				newContext.put(propertyName, MdaUtil.getObject(value, field.getDefaultValue(), field.getDataType(), field.getDefaultValueGetter()));
			}
			if (operationGroup.getOperationGroups() != null) {
				for (OperationGroup subGroup : operationGroup
						.getOperationGroups()) {
					buildOperationFieldParamter(subGroup, newContext);
				}
			}

		}
		return newContext;
	}

	/**
	 * 
	 * 条件字段和参数字段都要组装
	 * 
	 * @return
	 */
	public Context buildAllParamter(Context newContext) {
		buildConditionFieldParamter(operation.getConditionFields(), newContext);
		buildOperationFieldParamter(operation.getOperationGroup(), newContext);
		return newContext;
	}

	/**
	 * 
	 * 生成操作的操作列表BSONObject
	 * 
	 * @return
	 */
	public BSONObject generateOperationObject() {
		BSONObject operationObject = new BasicDBObject();
		initOperationObject(operation.getOperationGroup(), operationObject);
		return operationObject;
	}

	/**
	 * 
	 * 生成操作的显示列表BSONObject
	 * 
	 * @return
	 */
	public DBObject generateOperationSelectObject() {
		DBObject operationObject = new BasicDBObject();
		initOperationSelectObject(operation.getOperationGroup(),
				operationObject);
		return operationObject;
	}

	private void initOperationSelectObject(OperationGroup group,
			BSONObject bsonObject) {
		List<OperationField> operationFields = group.getFields();
		for (OperationField operationField : operationFields) {
			if (isCollectionField(operationField.getFieldId())) {
				MongoField mongoField = getMongoField(operationField
						.getFieldId());
				String propertyName = mongoField.getFieldName();
				bsonObject.put(propertyName, 1);// 暂时不做嵌套功能
				// bsonObject = propertyCycleSet(bsonObject, field.getName());
			}

		}
		if (!CollectionUtil.isEmpty(group.getOperationGroups())) {
			for (OperationGroup operationGroup : group.getOperationGroups()) {
				initOperationSelectObject(operationGroup, bsonObject);
			}
		}

	}

	/**
	 * 
	 * 生成操作条件BSONObject
	 * 
	 * @return
	 */
	public DBObject generateConditionObject() {
		return generateConditionObject(operation.getConditionFields());
	}

	private void initOperationObject(OperationGroup group, BSONObject bsonObject) {
		MapConvert convert = new MapConvert(model, operation, context);
		Map<String, Object> map = new HashMap<String, Object>();
		convert.initOperationObject(operation.getOperationGroup(), map);
		bsonObject.putAll(map);
	}

	private void addObjectArrayOperationObject(OperationGroup group,
			BSONObject objectArrayObject,BSONObject commonObject) {
		MapConvert convert = new MapConvert(model, operation, context);
		Map<String, Object> commonMap = new HashMap<String, Object>();
		Map<String, Object> addToSetMap= new HashMap<String, Object>();
		convert.addObjectArrayOperationObject(operation.getOperationGroup(),
				addToSetMap,commonMap);
		objectArrayObject.putAll(addToSetMap);
		commonObject.putAll(commonMap);
	}

	private void removeObjectOperationObject(OperationGroup group,
			BSONObject bsonObject) {
		MapConvert convert = new MapConvert(model, operation, context);
		Map<String, Object> map = new HashMap<String, Object>();
		convert.removeObjectOperationObject(operation.getOperationGroup(), map);
		bsonObject.putAll(map);
	}

	private void insertOperationObject(OperationGroup group,
			BSONObject bsonObject) {
		MapConvert convert = new MapConvert(model, operation, context);
		Map<String, Object> map = new HashMap<String, Object>();
		convert.insertOperationObject(operation.getOperationGroup(), map);
		bsonObject.putAll(map);
	}

	private void updateOperationObject(OperationGroup group,
			BSONObject bsonObject) {
		MapConvert convert = new MapConvert(model, operation, context);
		Map<String, Object> map = new HashMap<String, Object>();
		convert.updateOperationObject(operation.getOperationGroup(), map);
		bsonObject.putAll(map);
	}

	// private BSONObject propertyCycleSet(
	// BSONObject bsonObject, MongoField mongoField) {
	// String propertyName=mongoField.getFieldName();
	// String tempName = propertyName;
	// int index = tempName.indexOf(".");
	// if (index != -1) {
	// do {
	// String key = tempName.substring(0, index);
	// String nextPropertyName = tempName.substring(index + 1,
	// tempName.length());
	// BSONObject subObject = createSubObject(key, bsonObject);
	// index = nextPropertyName.indexOf(".");
	// if (index == -1) {
	// mongoField.fieldValueSet(bsonObject, context);
	// }
	// tempName = nextPropertyName;
	// bsonObject = subObject;
	// } while (index != -1);
	//
	// } else {
	// bsonObject.put(propertyName, context.get(propertyName));
	// }
	// return bsonObject;
	// }
	//
	// private BSONObject createSubObject(String key,
	// BSONObject bsonObject) {
	// BSONObject subObject = (BSONObject) bsonObject.get(key);
	// ObjectField objectField = model.getObjectField(key);
	// if (subObject == null) {
	// if(objectField!=null){
	// int size = objectField.getArraySize();
	// if (size > 1) {
	// subObject = new BasicBSONList();
	// BasicBSONList list=(BasicBSONList)subObject;
	// for (int i = 0; i < size; i++) {
	// BSONObject object = new BasicDBObject();
	// List<Field> fields=objectField.getFields();
	// for (Field field : fields) {
	// MongoField mongoField=model.getMongoField(field.getId());
	// mongoField.fieldValueSet(i,object, context);
	// }
	// list.add(i, object);
	// }
	// subObject=list;
	// } else {
	// subObject=new BasicDBObject();
	// }
	// bsonObject.put(key, subObject);
	// }
	// }
	// return subObject;
	// }

	public void propertyValueSet(ObjectField objectField, BSONObject bsonObject) {
		if (objectField != null) {
			int size = objectField.getArraySize();
			if (size > 1) {
				BasicBSONList list = (BasicBSONList) bsonObject.get(objectField
						.getName());
				if (list == null) {
					list = new BasicBSONList();
					for (int i = 0; i < size; i++) {
						BSONObject object = objectFieldSet(objectField, i);
						list.add(i, object);
					}
					bsonObject.put(objectField.getName(), list);
				}

			} else {
				BSONObject object = (BSONObject) bsonObject.get(objectField
						.getName());
				if (object == null) {
					object = objectFieldSet(objectField, 0);
					bsonObject.put(objectField.getName(), object);
				}
			}
		}

	}

	private BSONObject objectFieldSet(ObjectField objectField, int j) {
		BSONObject object = new BasicDBObject();
		List<Field> fields = objectField.getFields();
		for (Field field : fields) {
			MongoField mongoField = model.getMongoField(field.getId());
			if (!mongoField.isHasSetValue()) {
				mongoField.fieldValueSet(j, object, context);
				mongoField.setHasSetValue(true);
			}
		}
		List<ObjectField> objectFields = objectField.getObjectFields();
		for (ObjectField objectField2 : objectFields) {
			propertyValueSet(objectField2, object);
		}
		return object;
	}

}
