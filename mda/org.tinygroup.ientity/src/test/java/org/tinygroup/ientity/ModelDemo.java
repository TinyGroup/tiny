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
package org.tinygroup.ientity;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.tinygroup.entity.common.ConditionField;
import org.tinygroup.entity.common.DisplayField;
import org.tinygroup.entity.common.Field;
import org.tinygroup.entity.common.Group;
import org.tinygroup.entity.common.GroupField;
import org.tinygroup.entity.common.Index;
import org.tinygroup.entity.common.Operation;
import org.tinygroup.entity.common.OperationField;
import org.tinygroup.entity.common.OperationGroup;
import org.tinygroup.entity.common.OrderField;
import org.tinygroup.entity.common.View;
import org.tinygroup.entity.common.ViewGroup;
import org.tinygroup.entity.entitymodel.EntityModel;

import com.thoughtworks.xstream.XStream;

public class ModelDemo {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		XStream xStream = new XStream();
		xStream.processAnnotations(EntityModel.class);
		System.out.println(xStream.toXML(getEntityModel()));
	}

	static EntityModel getEntityModel() {
		EntityModel user = new EntityModel();
		setUserBasicInfo(user);
		setGroup(user);
		setView(user);
		setIndex(user);
		setOperation(user);
		return user;
	}

	private static void setOperation(EntityModel user) {
		List<Operation> operations=new ArrayList<Operation>();
		user.setOperations(operations);
		Operation operation=new Operation();
		operations.add(operation);
		operation.setId("query");
		operation.setName("query user");
		operation.setDescription("查询用户操作");
		operation.setPermissionCheck(true);
		List<ConditionField> conditionFields=new ArrayList<ConditionField>();
		ConditionField conditionField=new ConditionField();
		conditionField.setCompareMode("=");
		conditionField.setFieldId("user_id");
		conditionField.setGroups("user_id,name,age");
		conditionFields.add(conditionField);
		operation.setConditionFields(conditionFields);
		OperationGroup operationGroup=new OperationGroup();
		operationGroup.setId("query_fields");
		operationGroup.setDescription("查询字段列表");
		List<OperationField> operationFields=new ArrayList<OperationField>();
		operationGroup.setFields(operationFields);
		OperationField operationField=new OperationField();
		operationFields.add(operationField);
		operationField.setFieldId("user_id");
		OperationField operationField1=new OperationField();
		operationFields.add(operationField1);
		operationField1.setFieldId("name");
		OperationField operationField2=new OperationField();
		operationFields.add(operationField2);
		operationField2.setFieldId("age");
		operation.setOperationGroup(operationGroup);
		
       
	}

	private static void setIndex(EntityModel user) {
		List<Index> indexs=new ArrayList<Index>();
		Index index=new Index();
		indexs.add(index);
		index.setDescription("索引");
		index.setId("user_id_index");
		index.setUnique(true);
		List<String> fields=new ArrayList<String>();
		fields.add("user_id");
		index.setFields(fields);
		user.setIndexs(indexs);
	}

	private static void setView(EntityModel user) {
		List<View> views=new ArrayList<View>();
		user.setViews(views);
		View view=new View();
		views.add(view);
		setViewGroup(view);
		setViewCondition(view);
		setViewOrder(view);
		setViewGroupBy(view);
	}

	private static void setViewGroupBy(View view) {
		List<GroupField> groupFields=new ArrayList<GroupField>();
		view.setGroupFields(groupFields);
		GroupField groupField=new GroupField();
		groupField.setFieldId("user_id");
		groupFields.add(groupField);
	}

	private static void setViewOrder(View view) {
		List<OrderField> orderFields=new ArrayList<OrderField>();
		view.setOrderFields(orderFields);
		OrderField orderField=new OrderField();
		orderField.setFieldId("user_id");
		orderField.setOrderMode("asc");
		orderFields.add(orderField);
	}

	private static void setViewCondition(View view) {
		List<ConditionField> conditionFields=new ArrayList<ConditionField>();
		view.setConditionFields(conditionFields);
		ConditionField conditionField=new ConditionField();
		conditionField.setEditable(true);
		conditionField.setHidden(false);
		conditionField.setFieldId("user_id");
		conditionField.setCompareMode("=");
		conditionFields.add(conditionField);
	}

	private static void setViewGroup(View view) {
		List<ViewGroup> viewGroups=new ArrayList<ViewGroup>();
		view.setViewGroups(viewGroups);
		ViewGroup viewGroup=new ViewGroup();
		viewGroups.add(viewGroup);
		List<DisplayField> dispFields=new ArrayList<DisplayField>();
		viewGroup.setFields(dispFields);
		DisplayField field=new DisplayField();
		dispFields.add(field);
		field.setFieldId("name");
	}

	private static void setGroup(EntityModel user) {
		List<Group> groups = new ArrayList<Group>();
		user.setGroups(groups);
		Group group = new Group();
		group.setId("Basic");
		group.setName("Basic");
		group.setTitle("基本信息");
		groups.add(group);
		List<Field> fields = new ArrayList<Field>();
		group.setFields(fields);
		Field id = new Field();
		fields.add(id);
		id.setId("id");
		id.setPrimary(true);
		id.setStandardFieldId("user_id");
		Field name = new Field();
		fields.add(name);
		name.setId("name");
		name.setStandardFieldId("name");
		Field age = new Field();
		fields.add(age);
		age.setId("age");
		age.setStandardFieldId("age");
	}

	private static void setUserBasicInfo(EntityModel user) {
		user.setCacheEnabled(true);
		user.setAbstractModel(false);
		user.setDescription("用户模型");
		user.setEnableDelete(true);
		user.setEnableModify(true);
		user.setExtendInformation("扩展信息");
		user.setId("user");
		user.setName("user");
		user.setTitle("用户模型");
		user.setVersion("1.0");
	}
}
