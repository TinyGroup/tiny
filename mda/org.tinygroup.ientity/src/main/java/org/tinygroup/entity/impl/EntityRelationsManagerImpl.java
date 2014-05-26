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
package org.tinygroup.entity.impl;

import java.util.HashMap;
import java.util.Map;

import org.tinygroup.entity.EntityRelationsManager;
import org.tinygroup.entity.relation.EntityRelation;
import org.tinygroup.entity.relation.EntityRelations;

/**
 * 
 * 功能说明: 实体关联关系管理类的实现

 * 开发人员: renhui <br>
 * 开发时间: 2013-10-25 <br>
 * <br>
 */
public class EntityRelationsManagerImpl implements EntityRelationsManager {
	
	private Map<String, EntityRelation> relationMap=new HashMap<String, EntityRelation>();

	public void addEntityRelations(EntityRelations relations) {

		for (EntityRelation relation : relations.getEntityRelations()) {
			relationMap.put(relation.getRelationId(), relation);
		}
	}
	
	public void removeEntityRelations(EntityRelations relations) {

		for (EntityRelation relation : relations.getEntityRelations()) {
			relationMap.remove(relation.getRelationId());
		}
	}


	public EntityRelation getEntityRelation(String relationId) {
		return relationMap.get(relationId);
	}

}
