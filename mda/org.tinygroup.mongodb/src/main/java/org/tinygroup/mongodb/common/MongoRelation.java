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
package org.tinygroup.mongodb.common;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 
 * 功能说明: 关联关系配置

 * 开发人员: renhui <br>
 * 开发时间: 2013-12-5 <br>
 * <br>
 */
@XStreamAlias("mongo-relation")
public class MongoRelation {
	@XStreamAsAttribute
	@XStreamAlias("relation-model-id")
	private String relationModelId;
	@XStreamAsAttribute
	@XStreamAlias("relation-collection-name")
    private String relationCollectionName;
	@XStreamAlias("relation-conditions")
	private List<RelationCondition> conditions;
	@XStreamAlias("relation-fields")
	private List<RelationField> fields;
	public List<RelationCondition> getConditions() {
		return conditions;
	}
	public void setConditions(List<RelationCondition> conditions) {
		this.conditions = conditions;
	}
	public List<RelationField> getFields() {
		return fields;
	}
	public void setFields(List<RelationField> fields) {
		this.fields = fields;
	}
	public String getRelationCollectionName() {
		return relationCollectionName;
	}
	public void setRelationCollectionName(String relationCollectionName) {
		this.relationCollectionName = relationCollectionName;
	}
	public String getRelationModelId() {
		return relationModelId;
	}
	public void setRelationModelId(String relationModelId) {
		this.relationModelId = relationModelId;
	}
	
}
