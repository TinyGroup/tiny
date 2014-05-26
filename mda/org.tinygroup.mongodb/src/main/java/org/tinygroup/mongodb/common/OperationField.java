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

@XStreamAlias("operation-field")
public class OperationField {

	/**
	 * 是否可编辑
	 */
	@XStreamAsAttribute
	boolean editable;

	/**
	 * 是否隐藏
	 */
	@XStreamAsAttribute
	boolean hidden;

	/**
	 * 输入方式，每种方式可以有自己的属性，目前是该输入模式处理器的beanid
	 */
	@XStreamAsAttribute
	@XStreamAlias("input-mode")
	InputMode inputMode;

	/**
	 * 对应的模型字段id
	 */
	@XStreamAsAttribute
	@XStreamAlias("field-id")
	String fieldId;

	@XStreamAlias("front-event-defines")
	List<FrontEventDefine> frontEventDefines;

	@XStreamAlias("format-string")
	String formatString;// 格式化字符串，用于格式化显示比如：<b><a
						// href="showInfo?userId=$userId">$userName</b>，与具体的操作有关，要和操作约定匹配
	@XStreamAlias("expression-mode")
	@XStreamAsAttribute
	String expressionMode;

	public String getFormatString() {
		return formatString;
	}

	public void setFormatString(String formatString) {
		this.formatString = formatString;
	}

	public List<FrontEventDefine> getFrontEventDefines() {
		if (frontEventDefines == null) {
			return new ArrayList<FrontEventDefine>();
		}
		return frontEventDefines;
	}

	public void setFrontEventDefines(List<FrontEventDefine> frontEventDefines) {
		this.frontEventDefines = frontEventDefines;
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

	public InputMode getInputMode() {
		return inputMode;
	}

	public void setInputMode(InputMode inputMode) {
		this.inputMode = inputMode;
	}

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	public String getExpressionMode() {
		return expressionMode;
	}

	public void setExpressionMode(String expressionMode) {
		this.expressionMode = expressionMode;
	}
	

}
