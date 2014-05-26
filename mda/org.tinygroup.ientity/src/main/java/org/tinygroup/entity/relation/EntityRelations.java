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
package org.tinygroup.entity.relation;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 实体关系<br>
 * 通过实体关系列表把所有的实体关系建立了联系
 * 
 * @author luoguo
 * 
 */
@XStreamAlias("entity-relations")
public class EntityRelations {
	@XStreamImplicit
	List<EntityRelation> entityRelations;// 实体关系列表

	public List<EntityRelation> getEntityRelations() {
		return entityRelations;
	}

	public void setEntityRelations(List<EntityRelation> entityRelations) {
		this.entityRelations = entityRelations;
	}

}
