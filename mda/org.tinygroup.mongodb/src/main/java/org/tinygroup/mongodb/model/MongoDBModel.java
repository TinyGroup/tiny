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
package org.tinygroup.mongodb.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.mongodb.common.BaseObject;
import org.tinygroup.mongodb.common.Field;
import org.tinygroup.mongodb.common.Group;
import org.tinygroup.mongodb.common.ObjectField;
import org.tinygroup.mongodb.common.Operation;
import org.tinygroup.mongodb.common.OperationField;
import org.tinygroup.mongodb.common.OperationGroup;
import org.tinygroup.mongodb.common.View;
import org.tinygroup.mongodb.engine.MongoField;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
@XStreamAlias("mongo-model")
public class MongoDBModel extends BaseObject {
	@XStreamAsAttribute
	@XStreamAlias("abstract-model")
	Boolean abstractModel;// 是否抽象模型，如果是抽象模型，只能够用来被继承

	@XStreamAsAttribute
	private String version;// 版本

	@XStreamAsAttribute
	@XStreamAlias("parent-model-id")
	String parentModelId;// 如果是继承自另外的模型，这里可以输入，只能是单继承

	@XStreamAlias("operations")
	List<Operation> operations;// 操作

	@XStreamAlias("views")
	List<View> views;// 视图
	@XStreamAlias("groups")
	List<Group> groups;
	
	transient Map<String, MongoField> fieldMap=new HashMap<String, MongoField>();

	transient Map<String, ObjectField> objectFieldMap=new HashMap<String, ObjectField>();;
	
	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public Boolean getAbstractModel() {
		return abstractModel;
	}

	public void setAbstractModel(Boolean abstractModel) {
		this.abstractModel = abstractModel;
	}

	public String getParentModelId() {
		return parentModelId;
	}

	public void setParentModelId(String parentModelId) {
		this.parentModelId = parentModelId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<Operation> getOperations() {
		if (operations == null)
			operations = new ArrayList<Operation>();
		return operations;
	}

	public void setOperations(List<Operation> operations) {
		this.operations = operations;
	}

	public List<View> getViews() {
		if (views == null)
			views = new ArrayList<View>();
		return views;
	}

	public void setViews(List<View> views) {
		this.views = views;
	}
	
	/**
	 * 返回操作的字段对应的字段定义列表
	 * 
	 * @param operation
	 * @return
	 */
	public List<Field> getOperationFields(Operation operation) {
		List<Field> fields = new ArrayList<Field>();
		processOperationGroup(operation.getOperationGroup(), fields);
		return fields;
	}

	private void processOperationGroup(OperationGroup group, List<Field> fields) {
		for (OperationField operationField : group.getFields()) {
			Field field = getField(operationField.getFieldId());
			fields.add(field);
		}
		for (OperationGroup subGroup : group.getOperationGroups()) {
			processOperationGroup(subGroup, fields);
		}
	}
	
	public Field getField(String fieldId) {
		if (fieldMap == null) {
			fieldMap = new HashMap<String, MongoField>();
			groups();//初始化字段
		}
		MongoField mongoField = fieldMap.get(fieldId);
		if (mongoField != null) {
			return mongoField.getField();
		}
		throw new RuntimeException("实体模型" + title + "中找不到标识是" + fieldId + "的定义");
	}
	
	public ObjectField getObjectField(String fieldId) {
		if (fieldMap == null) {
			fieldMap = new HashMap<String, MongoField>();
		}
		MongoField mongoField = fieldMap.get(fieldId);
		if (mongoField != null) {
			return mongoField.getObjectField();
		}
		throw new RuntimeException("实体模型" + title + "中找不到标识是" + fieldId + "的定义");
	}
	
	public MongoField getMongoField(String fieldId) {
		if (fieldMap == null) {
			fieldMap = new HashMap<String, MongoField>();
		}
		MongoField mongoField = fieldMap.get(fieldId);
		if (mongoField != null) {
			return mongoField;
		}
		throw new RuntimeException("实体模型" + title + "中找不到标识是" + fieldId + "的定义");
	}
	
	public void groups(){
		fieldMap(groups);
	}
	
	public void fieldMap(List<Group> groups){
		if (fieldMap == null) {
			fieldMap = new HashMap<String, MongoField>();
		}
		if(objectFieldMap==null){
			objectFieldMap=new HashMap<String, ObjectField>();
		}
		for (Group group : groups) {	
		    for (Field field : group.getFields()) {
				fieldMap.put(field.getId(), new MongoField(field.getName(),field));
		    }
			List<ObjectField> objectFields=group.getObjectFields();
		    if(!CollectionUtil.isEmpty(objectFields)){
		    	for (ObjectField objectField : objectFields) {
		    		fieldMap.put(objectField.getId(), new MongoField(objectField.getName(),objectField));
                    objectFieldMap.put(objectField.getName(), objectField);
		    		cycleObjectField(objectField.getName(),objectField);
				}
		    }
		}    
	}
	
	private void cycleObjectField(String objectFieldName,ObjectField objectField){
		for (Field field : objectField.getFields()) {
			fieldMap.put(field.getId(), new MongoField(objectFieldName+"."+field.getName(),field, objectField));
		}
		List<ObjectField> objectFields=objectField.getObjectFields();
		for (ObjectField objectField2 : objectFields) {
			objectFieldName=objectFieldName+"."+objectField2.getName();
			fieldMap.put(objectField2.getId(), new MongoField(objectFieldName,objectField2));
			objectFieldMap.put(objectField2.getName(), objectField2);
			cycleObjectField(objectFieldName,objectField2);
		}
	}
	
	
	public String getMongoFieldName(String fieldId){
		MongoField field=getMongoField(fieldId);
		return field.getFieldName();
	}
	
	public String getFieldName(String fieldId){
		Field field=getField(fieldId);
		return field.getName();
	}
	
	public ObjectField getObjectFieldByName(String objectFieldName){
		return objectFieldMap.get(objectFieldName);
	}
	

}
