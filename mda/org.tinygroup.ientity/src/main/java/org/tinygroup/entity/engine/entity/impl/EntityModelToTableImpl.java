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
package org.tinygroup.entity.engine.entity.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.tinygroup.database.config.table.Table;
import org.tinygroup.database.config.table.TableField;
import org.tinygroup.entity.common.Field;
import org.tinygroup.entity.common.Group;
import org.tinygroup.entity.common.Index;
import org.tinygroup.entity.engine.entity.EntityModelToTable;
import org.tinygroup.entity.entitymodel.EntityModel;
import org.tinygroup.exception.TinySysRuntimeException;
import org.tinygroup.tinydb.BeanDbNameConverter;
import org.tinygroup.tinydb.impl.DefaultNameConverter;

/**
 * 
 * 功能说明: 实体模型转table

 * 开发人员: renhui <br>
 * 开发时间: 2013-9-23 <br>
 * <br>
 */
public class EntityModelToTableImpl implements EntityModelToTable {

	private BeanDbNameConverter converter=new DefaultNameConverter();
	
	public Table model2Table(EntityModel model) {
		Table table=new Table();
		baseTableSet(model, table);
		tableFieldSet(model, table);
		tableIndexSet(model, table);
		return table;
	}

	private void tableIndexSet(EntityModel model, Table table) {
		List<Index> indexs=model.getIndexs();
		List<org.tinygroup.database.config.table.Index> tableIndexs=new ArrayList<org.tinygroup.database.config.table.Index>();
		for (Index index : indexs) {
			org.tinygroup.database.config.table.Index tableIndex=new org.tinygroup.database.config.table.Index();
			try {
				BeanUtils.copyProperties(tableIndex, index);
			} catch (Exception e) {
			    throw new TinySysRuntimeException("ientity.objectConversionError",tableIndex,index);
			} 
			tableIndexs.add(tableIndex);
		}
		table.setIndexList(tableIndexs);
	}

	private void tableFieldSet(EntityModel model, Table table) {
		List<Group> groups= model.getGroups();
		List<TableField> tableFields=new ArrayList<TableField>();
		for (Group group : groups) {
			for (Field field : group.getFields()) {
				TableField tableField=new TableField();
				if(field.isTableField()){
					tableField.setId(field.getId());
					tableField.setNotNull(field.isNotNull());
					tableField.setPrimary(field.isPrimary());
					tableField.setStandardFieldId(field.getStandardFieldId());
					tableField.setUnique(field.isUnique());
					tableField.setAutoIncrease(field.isAutoIncrease());
					tableFields.add(tableField);
				}
				
			}
			
		}
		table.setFieldList(tableFields);
	}

	private void baseTableSet(EntityModel model, Table table) {
		table.setId(model.getId());
		table.setDescription(model.getDescription());
		table.setName(converter.typeNameToDbTableName(model.getName()));
		table.setTitle(model.getTitle());
	}


}
