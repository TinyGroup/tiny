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
package org.tinygroup.entity.entitymodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.commons.tools.Assert;
import org.tinygroup.entity.BaseModel;
import org.tinygroup.entity.common.Field;
import org.tinygroup.entity.common.Group;
import org.tinygroup.entity.common.Index;
import org.tinygroup.entity.common.Operation;
import org.tinygroup.entity.common.OperationField;
import org.tinygroup.entity.common.OperationGroup;
import org.tinygroup.entity.util.ModelUtil;
import org.tinygroup.metadata.config.stdfield.StandardField;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.tinydb.BeanDbNameConverter;
import org.tinygroup.tinydb.BeanOperatorManager;
import org.tinygroup.tinydb.operator.DBOperator;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 模型
 * 
 * @author luoguo
 * 
 */
@XStreamAlias("entity-model")
public class EntityModel extends BaseModel {
	@XStreamAsAttribute
	@XStreamAlias("cache-enabled")
	private Boolean cacheEnabled;// 是否启用缓冲
	@XStreamAlias("indexs")
	List<Index> indexs;// 索引
	@XStreamAlias("groups")
	List<Group> groups;
	transient Map<String, Field> fieldMap;
	BeanDbNameConverter nameConverter;

	@SuppressWarnings("rawtypes")
	public String getCamelName(String name) {
		if (nameConverter == null) {
			BeanOperatorManager manager = SpringUtil
					.getBean(BeanOperatorManager.OPERATOR_MANAGER_BEAN);
			DBOperator operator = manager.getDbOperator(this.getName());
			Assert.assertNotNull(operator, "operator must not null");
			this.nameConverter = operator.getBeanDbNameConverter();
		}
		return nameConverter.dbFieldNameToPropertyName(name);
	}

	public Field getField(String fieldId) {
		if (fieldMap == null) {
			fieldMap = new HashMap<String, Field>();
		}
		Field field = fieldMap.get(fieldId);
		if (field != null) {
			return field;
		}
		if (groups != null) {
			for (Group group : groups) {
				for (Field f : group.getFields()) {
					if (f.getId().equals(fieldId)) {
						fieldMap.put(fieldId, f);
						return f;
					}
				}
			}
		}
		throw new RuntimeException("实体模型" + title + "中找不到标识是" + fieldId + "的定义");
	}

	public StandardField getStandardField(String fieldId) {
		StandardField stdField = ModelUtil.getStandardField(getField(fieldId)
				.getStandardFieldId());
		if (stdField != null) {
			return stdField;
		}
		throw new RuntimeException("实体模型" + title + "中找不到标识是" + fieldId
				+ "对应的标准字段定义");
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


	public List<Group> getGroups() {
		if (groups == null) {
			groups = new ArrayList<Group>();
		}
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public Boolean getCacheEnabled() {
		return cacheEnabled;
	}

	public void setCacheEnabled(Boolean cacheEnabled) {
		this.cacheEnabled = cacheEnabled;
	}

	public List<Index> getIndexs() {
		if (indexs == null) {
			indexs = new ArrayList<Index>();
		}
		return indexs;
	}

	public void setIndexs(List<Index> indexs) {
		this.indexs = indexs;
	}

}
