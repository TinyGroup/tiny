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

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 字段分组
 * 
 * @author luoguo
 * 
 */
@XStreamAlias("operation-group")
public class OperationGroup extends BaseObject {
	/**
	 * 包含子组，如果包含子组，且包含fields，则也会被当成一个新组，组名：基本信息
	 */
	@XStreamImplicit
	List<OperationGroup> operationGroups;
	@XStreamAsAttribute
	@XStreamAlias("display-mode")
	String displayMode;// 分组显示模式，可以是Tab,FieldSet,Wizard,Expander,Accordion,Carousel等
	@XStreamAsAttribute
	Reference reference;// 如果有引用，则忽略url
	@XStreamAsAttribute	
	String url;// 来自哪个URL，如果有这个值，则不能再包含操作字段列表,如果有，也会被忽略
	@XStreamAlias("operation-fields")
	List<OperationField> fields;

	public List<OperationGroup> getOperationGroups() {
		if (operationGroups == null) {
			operationGroups = new ArrayList<OperationGroup>();
		}
		return operationGroups;
	}

	public void setOperationGroups(List<OperationGroup> operationGroups) {
		this.operationGroups = operationGroups;
	}

	public Reference getReference() {
		return reference;
	}

	public void setReference(Reference reference) {
		this.reference = reference;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDisplayMode() {
		return displayMode;
	}

	public void setDisplayMode(String displayMode) {
		this.displayMode = displayMode;
	}

	public List<OperationField> getFields() {
		if (fields == null)
			fields = new ArrayList<OperationField>();
		return fields;
	}

	public void setFields(List<OperationField> fields) {
		this.fields = fields;
	}

}
