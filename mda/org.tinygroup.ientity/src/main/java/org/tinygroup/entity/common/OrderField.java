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
package org.tinygroup.entity.common;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 排序字段<br>
 * fieldId属性为排序字段对当前模型字段的引用 该字段必有值 modelId 该属性在实体模型中无效 在视图模型中
 * 为空，则fieldId为当前视图模型字段 不为空，则fieldId为modelId所指向的模型的字段的Id
 * 
 * @author chenjiao
 * 
 */
@XStreamAlias("order-field")
public class OrderField {
	// 引用的模型(或视图)字段id
	// 必不为空
	@XStreamAsAttribute
	@XStreamAlias("field-id")
	String fieldId;

	// modelId为relatemodel的Id
	// 可为空
	// 在实体模型中必为空
	// 在视图模型中，若为空，则fieldId为视图模型字段
	// 若不为空，则fieldId为此字段所指向模型的字段Id
	@XStreamAsAttribute
	@XStreamAlias("model-id")
	String modelId;

	@XStreamAsAttribute
	@XStreamAlias("order-mode")
	String orderMode;// 如果是固定值，则这里有值[desc/asc]，否则为传入值，如果没有传入值，则忽略之
	// 是否可编辑
	@XStreamAsAttribute
	boolean editable;
	// 是否隐藏
	@XStreamAsAttribute
	boolean hidden;

	// 展现方式

	public String getOrderMode() {
		return orderMode;
	}

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	public void setOrderMode(String orderMode) {
		this.orderMode = orderMode;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

}
