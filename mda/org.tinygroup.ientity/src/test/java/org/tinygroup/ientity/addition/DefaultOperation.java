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
package org.tinygroup.ientity.addition;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.entity.common.ConditionField;
import org.tinygroup.entity.common.Field;
import org.tinygroup.entity.common.Group;
import org.tinygroup.entity.common.Operation;
import org.tinygroup.entity.common.OperationField;
import org.tinygroup.entity.common.OperationGroup;
import org.tinygroup.entity.entitymodel.EntityModel;

public final class DefaultOperation {
	/**
	 * 默认新增操作的uuid
	 */
	public static final String DEFAULT_ADD_OP_UUID = "modelopadduuid";
	
	/**
	 * 默认新增操作的uuid
	 */
	public static final String DEFAULT_ADD_COPY_OP_UUID = "modelopaddcopyuuid";
	
	/**
	 * 默认修改操作的uuid
	 */
	public static final String DEFAULT_UPDATE_OP_UUID = "modelopupdateuuid";
	/**
	 * 默认删除操作的uuid
	 */
	public static final String DEFAULT_DELETE_OP_UUID = "modelopdeleteuuid";


	/**
	 * 默认查询查看的uuid
	 */
	public static final String DEFAULT_VIEW_OP_UUID = "modelopqueryuuid";
	
	private static final String VIEW_TITLE="查看";
	private static final String ADD_TITLE="新增";
	private static final String UPDATE_TITLE="修改";
	private static final String DELETE_TITLE="删除";
	private static final String ADD_COPY_TITLE="拷贝添加";


	private DefaultOperation() {

	}

	/**
	 * 为模型生成默认的增删改查操作
	 * 
	 * @param model
	 */
	public static void createDefaultOp(EntityModel model) {
		// 将原有的模型操作全部清除
		model.getOperations().clear();
		model.getOperations().add(
				createOp(model, DEFAULT_ADD_OP_UUID, "add"+upperFirst(model.getName()),"add",ADD_TITLE));
		model.getOperations().add(
				createOp(model, DEFAULT_UPDATE_OP_UUID, "modify"+upperFirst(model.getName()),"modify",UPDATE_TITLE));
		model.getOperations().add(
				createOp(model, DEFAULT_DELETE_OP_UUID, "delete"+upperFirst(model.getName()),"delete",DELETE_TITLE));
		model.getOperations().add(
				createOp(model, DEFAULT_VIEW_OP_UUID, "view"+upperFirst(model.getName()),"view",VIEW_TITLE));
		model.getOperations().add(
				createOp(model, DEFAULT_ADD_COPY_OP_UUID, "copyAdd"+upperFirst(model.getName()),"copyAdd",ADD_COPY_TITLE));
//		model.getOperations().add(
//				createOp(model, DEFAULT_TABLE_OP_UUID, "table"+upperFirst(model.getName()),"table",TABLE_TITLE));
	}

	/**
	 * 为模型生成默认的指定类型的操作
	 * 
	 * @param model
	 *            模型
	 * @param type
	 *            操作类型
	 * @param title
	 *            操作中文名
	 * @param uuid
	 *            操作uuid
	 * @return
	 */
	private static Operation createOp(EntityModel model, String uuid,String name,String type,
			String title) {
		Operation op = new Operation();
		op.setType(type);
		op.setEnableDelete(true);
		op.setEnableModify(true);
		op.setName(name);
		op.setModal(true);
		op.setTitle(title);
		op.setId(model.getId()+uuid);
        if(model.getGroups().size()>0){
        	if(!name.equals("delete")){
        	 	op.setOperationGroup(createOpFiledGroups(model.getGroups().get(0), type));
        	}
        	if(!name.equals("add")){
        		op.setConditionFields(createConditionFields(model.getGroups().get(0)));
        	}
        
        }
      
		return op;
	}


	private static List<ConditionField> createConditionFields(Group group) {
		List<ConditionField> conditionFields=new ArrayList<ConditionField>();
		for (Field field : group.getFields()) {
			ConditionField conditionField=new ConditionField();
			conditionField.setFieldId(field.getId());
			if (field.isPrimary()) {
				conditionField.setEditable(false);
				conditionField.setHidden(false);
				conditionFields.add(conditionField);
			} 
		}
		
		return conditionFields;
	}

	/**
	 * 根据模型字段分组生成操作字段分组
	 * 
	 * @param group
	 * @param type
	 * @return
	 */
	private static OperationGroup createOpFiledGroups(Group group, String type) {
		OperationGroup opGroup = new OperationGroup();
		opGroup.setName("BaseGroup");
		opGroup.setTitle("基本分组");
		opGroup.setId("BaseGroup");
		opGroup.getFields().addAll(createOperationFields(group.getFields(),type));
		opGroup.setName(group.getName());
		opGroup.setTitle(group.getTitle());
		opGroup.setId(getOpGroupUUID(group, type));
		opGroup.setEnableDelete(group.getEnableDelete());
		opGroup.setEnableModify(group.getEnableModify());
		return opGroup;
	}

	/**
	 * 根据模型字段列表生成操作字段列表
	 * 
	 * @param entityFields
	 * @return
	 */
	private static List<OperationField> createOperationFields(
			List<Field> entityFields,String operationType) {
		List<OperationField> operationFields = new ArrayList<OperationField>();
		for (Field entityField : entityFields) {
			if(entityField.isPrimary()&&operationType.equals("copyAdd")){
				continue;
			}
			operationFields.add(createOperationField(entityField));
		}
		return operationFields;
	}

	/**
	 * 根据模型字段生成操作字段
	 * 
	 * @param entityField
	 * @return
	 */
	private static OperationField createOperationField(Field entityField) {
		OperationField opField = new OperationField();
		opField.setFieldId(entityField.getId());
		if (entityField.isPrimary()) {
			opField.setEditable(false);
			opField.setHidden(true);
		} else {
			opField.setEditable(true);
			opField.setHidden(false);
		}
		return opField;
	}

	/**
	 * 获取操作分组的uuid
	 * 
	 * @param group
	 * @param type
	 * @return
	 */
	private static String getOpGroupUUID(Group group, String type) {
		return String.format("%sop%s", type, group.getId());
	}
	public static String upperFirst(String property){
		return property.substring(0, 1).toUpperCase() + property.substring(1);
	}
}

