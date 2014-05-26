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
package org.tinygroup.entity.relationmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.commons.tools.Assert;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.context.Context;
import org.tinygroup.entity.BaseModel;
import org.tinygroup.entity.EntityModelHelper;
import org.tinygroup.entity.EntityRelationsManager;
import org.tinygroup.entity.common.Field;
import org.tinygroup.entity.entitymodel.EntityModel;
import org.tinygroup.entity.relation.EntityRelation;
import org.tinygroup.entity.relation.ModelReference;
import org.tinygroup.entity.util.ModelUtil;
import org.tinygroup.exception.TinySysRuntimeException;
import org.tinygroup.imda.ModelManager;
import org.tinygroup.metadata.config.stdfield.StandardField;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.tinydb.BeanDbNameConverter;
import org.tinygroup.tinydb.BeanOperatorManager;
import org.tinygroup.tinydb.operator.DBOperator;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 关系模型
 * 
 * @author luoguo
 * 
 */
@XStreamAlias("relation-model")
public class RelationModel extends BaseModel {
	/**
	 * 主表的beantype.获取operate的beantype就是由这个属性设置的
	 */
	@XStreamAsAttribute
	@XStreamAlias("main-bean-type")
	String mainBeanType;
	/**
	 * 模型引用<br>
	 * 用于引用其它实体类型，可以引入多个实体
	 */
	@XStreamAlias("model-references")
	private List<ModelReference> modelReferences;
	@XStreamAlias("relation-groups")
	List<RelationGroup> groups;
	transient Map<String, Field> fieldMap;
	BeanDbNameConverter nameConverter;
	ModelManager manager;

	public List<ModelReference> getModelReferences() {
		if (modelReferences == null) {
			modelReferences = new ArrayList<ModelReference>();
		}
		return modelReferences;
	}

	public void setModelReferences(List<ModelReference> modelReferences) {
		this.modelReferences = modelReferences;
	}

	public List<RelationGroup> getGroups() {
		if (groups == null) {
			groups = new ArrayList<RelationGroup>();
		}
		return groups;
	}

	public void setGroups(List<RelationGroup> groups) {
		this.groups = groups;
	}

	public String getMainBeanType() {
		return mainBeanType;
	}

	public void setMainBeanType(String mainBeanType) {
		this.mainBeanType = mainBeanType;
	}

	@SuppressWarnings("rawtypes")
	public String getCamelName(String name) {
		if (nameConverter == null) {
			BeanOperatorManager manager = SpringUtil
					.getBean(BeanOperatorManager.OPERATOR_MANAGER_BEAN);
			DBOperator operator = manager.getDbOperator(this.getMainBeanType());
			Assert.assertNotNull(operator, "operator must not null");
			this.nameConverter = operator.getBeanDbNameConverter();
		}
		return nameConverter.dbFieldNameToPropertyName(name);
	}

	public Field getField(String fieldId) {
		if (fieldMap == null) {
			fieldMap = new HashMap<String, Field>();
		}
		if (manager == null) {
			manager = SpringUtil.getBean(ModelManager.MODELMANAGER_BEAN);
		}
		Field field = fieldMap.get(fieldId);
		if (field != null) {
			return field;
		}
		if (groups != null) {
			for (RelationGroup group : groups) {
				for (RelationField f : group.getFields()) {
					if (f.getFieldId().equals(fieldId)) {
						EntityModelHelper helper = getHelperWithFieldId(f
								.getFieldId());
						field = helper.getFieldById(fieldId);
						if (field == null) {
							field = helper.getFieldById(f.getRefFieldId());
						}
						fieldMap.put(fieldId, field);
						return field;
					}
				}
			}
		}
		throw new RuntimeException("实体模型" + title + "中找不到标识是" + fieldId + "的定义");
	}

	public StandardField getStandardField(String fieldId) {
		RelationField relationField = relationFields.get(fieldId);
		if (relationField != null) {
			StandardField stdField = ModelUtil.getStandardField(getField(
					fieldId).getStandardFieldId());
			if (stdField != null) {
				return createNewStandardField(relationField, stdField);
			}
			throw new RuntimeException("实体模型" + title + "中找不到标识是" + fieldId
					+ "对应的标准字段定义");

		} else {
			throw new TinySysRuntimeException(
					"ientity.fieldIdnotDefineInRelationField", fieldId);
		}

	}
    /**
     * 
     * 创建新的标准字段信息带有name和title属性
     * @param relationField
     * @param stdField
     * @return
     */
	public StandardField createNewStandardField(RelationField relationField,
			StandardField stdField) {
		String aliasName = relationField.getAliseName();
		String title= relationField.getTitle();
		String refFieldId=relationField.getRefFieldId();
		if(!StringUtil.isBlank(refFieldId)){
			if(StringUtil.isBlank(aliasName)){//如果 ref-field-id值不为空且没有设置别名，那么别名值取自field-id的值
				aliasName=relationField.getFieldId();
			}
		}
		StandardField newStdField = new StandardField();
		if (StringUtil.isBlank(aliasName)) {//如果没有设置aliasName属性则取自标准字段
			aliasName=stdField.getName();
		}
		if(StringUtil.isBlank(title)){//如果为设置title属性则取自标准字段
		    title=stdField.getTitle();
		}
		newStdField.setName(aliasName);
		newStdField.setTitle(title);
		return newStdField;
	}

	private transient Map<String, EntityModelHelper> modelHelps = new HashMap<String, EntityModelHelper>();

	/**
	 * key：field-id；value:model-id
	 */
	private transient Map<String, String> fieldId2ModelId = new HashMap<String, String>();
	/**
	 * key：field-id；value：RelationField
	 */
	private transient Map<String, RelationField> relationFields = new HashMap<String, RelationField>();
	/**
	 * 关系模型 的关联关系列表
	 */
	private transient List<EntityRelation> relations = new ArrayList<EntityRelation>();
	/**
	 * key：model-id；value:model-alias-id
	 */
	private transient Map<String, String> modelId2AliasId = new HashMap<String, String>();

	/**
	 * key：model-alias-id；value:modelId
	 */
	private transient Map<String, String> aliasId2ModelId = new HashMap<String, String>();
	/**
	 * 是否可以初始化的标识，只能进行一次初始化
	 */
//	private boolean hasInited = false;

	public void init(Context context) {
//		if (!hasInited) {
			initVariable();
			initModelReference(context);
			initRelationGroup();
//			hasInited = true;
//		}

	}

	public void initRelationGroup() {
		List<RelationGroup> groups = this.getGroups();
		for (RelationGroup group : groups) {
			List<RelationField> fields = group.getFields();
			for (RelationField field : fields) {
				String modelId = field.getModelId();
				EntityModelHelper helper = modelHelps.get(modelId);
				if (helper != null) {
					String fieldId = field.getFieldId();
					if (!fieldId2ModelId.containsKey(fieldId)) {
						fieldId2ModelId.put(fieldId, modelId);
					}
					if (!relationFields.containsKey(field.getFieldId())) {
						relationFields.put(field.getFieldId(), field);
					}

				} else {
					throw new TinySysRuntimeException("ientity.notDefineModel",
							modelId);
				}
			}
		}
	}

	public void initModelReference(Context context) {
		ModelManager modelManager = SpringUtil
				.getBean(ModelManager.MODELMANAGER_BEAN);
		EntityRelationsManager entityRelationsManager = SpringUtil
				.getBean(EntityRelationsManager.MANAGER_BEAN_NAME);
		List<ModelReference> ModelReferences = this.getModelReferences();
		for (ModelReference modelReference : ModelReferences) {
			String relationId = modelReference.getRelationId();
			EntityRelation entityRelation = entityRelationsManager
					.getEntityRelation(relationId);
			if (entityRelation != null) {
				String mainModelId = entityRelation.getMainEntityId();
				EntityModel mainModel = modelManager.getModel(mainModelId);
				if (mainModel != null) {
					EntityModelHelper helper = new EntityModelHelper(mainModel,
							context);
					modelHelps.put(mainModelId, helper);
					String aliasName = entityRelation.getMainAliasName();
					if (!StringUtil.isBlank(aliasName)) {
						modelHelps.put(aliasName, helper);
						modelId2AliasId.put(mainModelId, aliasName);
						modelId2AliasId.put(aliasName, aliasName);
						aliasId2ModelId.put(aliasName, mainModelId);
					}
				}
				String fromModelId = entityRelation.getFromEntityId();
				EntityModel fromModel = modelManager.getModel(fromModelId);
				if (fromModel != null) {
					EntityModelHelper helper = new EntityModelHelper(fromModel,
							context);
					modelHelps.put(fromModelId, helper);
					String aliasName = entityRelation.getFromAliasName();
					if (!StringUtil.isBlank(aliasName)) {
						modelHelps.put(aliasName, helper);
						modelId2AliasId.put(fromModelId, aliasName);
						modelId2AliasId.put(aliasName, aliasName);
						aliasId2ModelId.put(aliasName, fromModelId);
					}

				}
				relations.add(entityRelation);

			} else {
				throw new TinySysRuntimeException(
						"ientity.notFountEntityRelation", relationId);
			}

		}
	}

	public void initVariable() {
		modelHelps = new HashMap<String, EntityModelHelper>();
		modelId2AliasId = new HashMap<String, String>();
		aliasId2ModelId = new HashMap<String, String>();
		relations = new ArrayList<EntityRelation>();
		fieldId2ModelId = new HashMap<String, String>();
		relationFields = new HashMap<String, RelationField>();
	}

	public EntityModelHelper getHelperWithFieldId(String fieldId) {
		String modelId = fieldId2ModelId.get(fieldId);
		if (modelId != null) {
			return modelHelps.get(modelId);
		} else {
			throw new TinySysRuntimeException(
					"ientity.fieldIdnotDefineInRelationField", fieldId, modelId);
		}
	}

	public EntityModelHelper getHelperWithModelId(String modelId) {
		return modelHelps.get(modelId);
	}

	public String getTableAliasName(String fieldId) {
		String modelId = fieldId2ModelId.get(fieldId);
		return modelId2AliasId.get(modelId);
	}

	public List<EntityRelation> getRelations() {
		return relations;
	}

	public RelationField getRelationFieldWithFieldId(String fieldId) {
		return relationFields.get(fieldId);
	}

}
