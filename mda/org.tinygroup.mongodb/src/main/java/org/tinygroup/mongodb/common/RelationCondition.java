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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;


/**
 * 
 * 功能说明:关联关系条件配置 

 * 开发人员: renhui <br>
 * 开发时间: 2013-12-5 <br>
 * <br>
 */
@XStreamAlias("relation-condition")
public class RelationCondition {
    @XStreamAsAttribute
    @XStreamAlias("source-relation-field")
	private String sourceRelationField;
    @XStreamAsAttribute
    @XStreamAlias("target-relation-field")
    private String targetRelationField;
	public String getSourceRelationField() {
		return sourceRelationField;
	}
	public void setSourceRelationField(String sourceRelationField) {
		this.sourceRelationField = sourceRelationField;
	}
	public String getTargetRelationField() {
		return targetRelationField;
	}
	public void setTargetRelationField(String targetRelationField) {
		this.targetRelationField = targetRelationField;
	}
    
    
}
