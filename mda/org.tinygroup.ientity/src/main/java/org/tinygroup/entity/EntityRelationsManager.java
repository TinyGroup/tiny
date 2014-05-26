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
package org.tinygroup.entity;

import org.tinygroup.entity.relation.EntityRelation;
import org.tinygroup.entity.relation.EntityRelations;

/**
 * 
 * 功能说明: 实体模型引用关联接口

 * 开发人员: renhui <br>
 * 开发时间: 2013-10-25 <br>
 * <br>
 */
public interface EntityRelationsManager {

	String MANAGER_BEAN_NAME="entityRelationsManager";
	
	/**
	 * 
	 * 增加实体关联配置信息
	 * @param relations
	 */
	void addEntityRelations(EntityRelations relations);
	
	/**
	 * 
	 * 移除实体关联配置信息
	 * @param relations
	 */
	void removeEntityRelations(EntityRelations relations);
	
    /**
     * 
     * 根据实体关联id获取关联实体对象
     * @param relationId
     * @return
     */
	EntityRelation getEntityRelation(String relationId);
	
	
}
