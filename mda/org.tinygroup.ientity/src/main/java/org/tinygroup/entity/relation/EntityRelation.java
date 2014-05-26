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

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.entity.base.BaseObject;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 实体关系<br>
 * 比如表结构如下：<br>
 * ----------------------------------<br>
 * org<br>
 * org_id|org_name|org_desc<br>
 * user<br>
 * user_id|user_name|org_id|user_desc<br>
 * ----------------------------------<br>
 * 则关联关系为： <entity-relation main-entity-id="user" from-entity-id="org"
 * main-key-field-id="org_id" from-key-field-id="org_id" /><br>
 * 
 */
@XStreamAlias("entity-relation")
public class EntityRelation extends BaseObject {
	@XStreamAsAttribute
	@XStreamAlias("relation-id")
	String relationId;//关联关系标识
	@XStreamAsAttribute
	@XStreamAlias("main-entity-id")
	String mainEntityId;// 源实体模型标识
	@XStreamAsAttribute
	@XStreamAlias("main-alias-name")
	String mainAliasName;
	@XStreamAsAttribute
	@XStreamAlias("from-entity-id")
	String fromEntityId;// 目标实体模型标识
	@XStreamAsAttribute
	@XStreamAlias("from-alias-name")
	String fromAliasName;
	@XStreamAsAttribute
	@XStreamAlias("main-standard-field-id")
	String mainStandardFieldId;// 源表关联标准字段标识
	@XStreamAsAttribute
	@XStreamAlias("from-standard-field-id")
	String fromStandardFieldId;// 表关联标准字段标识
	
	/**
	 * 连接方式，可以是：left(左连接),right(右连接),full(全连接),inner(内连接),outer(外连接),cross(交叉连接)
	 * ,self(自连接)
	 * 
	 */
	@XStreamAsAttribute
	@XStreamAlias("connect-mode")
	String connectMode=DEFAULT_CONNECT_MODE;
	
	private static final String DEFAULT_CONNECT_MODE=" inner join";


	public String getConnectMode() {
		if(StringUtil.isBlank(connectMode)){
			connectMode=DEFAULT_CONNECT_MODE;
		}
		return connectMode;
	}

	public void setConnectMode(String connectMode) {
		this.connectMode = connectMode;
	}


	
	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public String getMainEntityId() {
		return mainEntityId;
	}

	public void setMainEntityId(String mainEntityId) {
		this.mainEntityId = mainEntityId;
	}

	public String getMainAliasName() {
		return mainAliasName;
	}

	public void setMainAliasName(String mainAliasName) {
		this.mainAliasName = mainAliasName;
	}

	public String getFromEntityId() {
		return fromEntityId;
	}

	public void setFromEntityId(String fromEntityId) {
		this.fromEntityId = fromEntityId;
	}

	public String getFromAliasName() {
		return fromAliasName;
	}

	public void setFromAliasName(String fromAliasName) {
		this.fromAliasName = fromAliasName;
	}

	public String getMainStandardFieldId() {
		return mainStandardFieldId;
	}

	public void setMainStandardFieldId(String mainStandardFieldId) {
		this.mainStandardFieldId = mainStandardFieldId;
	}

	public String getFromStandardFieldId() {
		return fromStandardFieldId;
	}

	public void setFromStandardFieldId(String fromStandardFieldId) {
		this.fromStandardFieldId = fromStandardFieldId;
	}

	
	
}
