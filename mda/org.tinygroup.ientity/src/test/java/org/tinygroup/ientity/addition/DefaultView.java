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
import org.tinygroup.entity.common.DisplayField;
import org.tinygroup.entity.common.Field;
import org.tinygroup.entity.common.Group;
import org.tinygroup.entity.common.OperationReference;
import org.tinygroup.entity.common.View;
import org.tinygroup.entity.common.ViewGroup;
import org.tinygroup.entity.entitymodel.EntityModel;

public final class DefaultView {
	/**
	 * 默认视图名
	 */
	private static String dVIEWNAME = "默认视图";
	/**
	 * 默认视图id
	 */
	private static String dVIEWID = "defaultmodleview";

	/**
	 * 默认查询操作的uuid
	 */
	public static final String DEFAULT_TABLE_OP_UUID = "modeloptableuuid";

	private static final String TABLE_TITLE = "表格查询";

	/**
	 * 表格默认单页数据条数
	 */
	private static int dPAGESIZE = 10;

	private DefaultView() {

	}

	/**
	 * 为模型生成基本视图，包含所有的模型字段及生成的默认的增删改查操作
	 * 
	 * @param model
	 */
	public static void createDefaultView(EntityModel model) {

		View view = new View();
		view.setTitle(TABLE_TITLE);
		view.setName("table" + model.getName());
		view.setId(model.getId()+DEFAULT_TABLE_OP_UUID);
		view.setType("table");
		view.setModal(false);
		view.setEnableDelete(true);
		view.setEnableModify(true);
		view.setPageSize(dPAGESIZE);
		createDefaultViewOp(model.getId(),view);
		if (model.getGroups().size() > 0) {
			view.getViewGroups().addAll(createOpFiledGroups(model.getGroups()));
			view.setConditionFields(createConditionFields(model.getGroups()
					.get(0)));
		}
		// 将model原有的view全部清除
		model.getViews().clear();
		// 将默认view添加至模型
		model.getViews().add(view);
	}

	private static List<ConditionField> createConditionFields(Group group) {
		List<ConditionField> conditionFields = new ArrayList<ConditionField>();
		for (Field field : group.getFields()) {
			if (!field.isPrimary()) {
				ConditionField conditionField = new ConditionField();
				conditionField.setFieldId(field.getId());
				if (field.isPrimary()) {
					conditionField.setEditable(false);
					conditionField.setHidden(true);
				} else {
					conditionField.setEditable(true);
					conditionField.setHidden(false);
				}
				conditionFields.add(conditionField);
			}
		}

		return conditionFields;
	}

	/**
	 * 为视图添加默认的增删改查操作引用
	 * 
	 * @param view
	 */
	private static void createDefaultViewOp(String modelId,View view) {
		view.getReferences().add(
				new OperationReference(modelId+DefaultOperation.DEFAULT_ADD_OP_UUID));
		view.getReferences().add(
				new OperationReference(modelId+
						DefaultOperation.DEFAULT_ADD_COPY_OP_UUID));
		view.getReferences()
				.add(new OperationReference(modelId+
						DefaultOperation.DEFAULT_DELETE_OP_UUID));
		view.getReferences()
				.add(new OperationReference(modelId+
						DefaultOperation.DEFAULT_UPDATE_OP_UUID));
		view.getReferences().add(
				new OperationReference(modelId+DefaultOperation.DEFAULT_VIEW_OP_UUID));

	}

	/**
	 * 根据模型字段分组列表生成视图字段分组列表
	 * 
	 * @param groups
	 * @return
	 */
	private static List<ViewGroup> createOpFiledGroups(List<Group> groups) {
		List<ViewGroup> vGroups = new ArrayList<ViewGroup>();
		for (Group group : groups) {
			vGroups.add(createOpFiledGroups(group));
		}
		return vGroups;
	}

	/**
	 * 根据模型字段分组生成视图字段分组
	 * 
	 * @param group
	 * @return
	 */
	private static ViewGroup createOpFiledGroups(Group group) {
		ViewGroup vGroup = new ViewGroup();
		vGroup.getFields().addAll(createDisplayFields(group.getFields()));
		vGroup.setName(group.getName());
		vGroup.setTitle(group.getTitle());
		vGroup.setId(getViewGroupUUID(group));
		vGroup.setEnableDelete(group.getEnableDelete());
		vGroup.setEnableModify(group.getEnableModify());
		return vGroup;
	}

	/**
	 * 根据模型字段列表获取视图字段列表
	 * 
	 * @param entityFields
	 * @return
	 */
	private static List<DisplayField> createDisplayFields(
			List<Field> entityFields) {
		List<DisplayField> operationFields = new ArrayList<DisplayField>();
		for (Field entityField : entityFields) {
			DisplayField f = new DisplayField();
			f.setFieldId(entityField.getId());
			operationFields.add(f);
			if (entityField.isPrimary()) {
				f.setHidden(true);
			}
		}
		return operationFields;
	}

	/**
	 * 获取视图字段列表uuid
	 * 
	 * @param group
	 * @return
	 */
	private static String getViewGroupUUID(Group group) {
		return String.format("view%s", group.getId());
	}
}
