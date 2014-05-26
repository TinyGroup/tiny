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

import org.tinygroup.imda.validate.ValidateRule;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 模型的属性
 * 
 * @author luoguo
 * 
 */
@XStreamAlias("field")
public class Field extends BaseObject {
	
	private static final String DEFAULT_DATA_TYPE = "java.lang.String";

	@XStreamAsAttribute
	@XStreamAlias("is-object-id")
	private boolean isObjectId;

	/**
	 * 是否主键
	 */
	@XStreamAsAttribute
	private boolean primary;
	/**
	 * 是否唯一
	 */
	@XStreamAsAttribute
	private boolean unique;


	@XStreamAsAttribute
	@XStreamAlias("not-null")
	private boolean notNull;

	/**
	 * 输入方式，每种方式可以有自己的属性，目前是该输入模式处理器的beanid
	 */
	@XStreamAsAttribute
	@XStreamAlias("input-mode")
	InputMode inputMode;
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

	@XStreamAsAttribute
	@XStreamAlias("collection-field")
	boolean collectionField = true;// 是否是表字段，只有是表字段，才会创建数据表，非表字段用于生成辅助性输入，比如：修改密码时的校验密码框

	/**
	 * 默认值
	 */
	@XStreamAsAttribute
	@XStreamAlias("default-value")
	String defaultValue;

	public boolean isCollectionField() {
		return collectionField;
	}

	public void setCollectionField(boolean collectionField) {
		this.collectionField = collectionField;
	}

	/**
	 * 验证规则，这个和校验框架对接，每种方式可以自己的属性
	 */
	@XStreamAlias("validate-rules")
	private List<ValidateRule> validateRules;
	/**
	 * 实体名称，如果存在，就表示是引用自另外的模型引用
	 */
	@XStreamAsAttribute
	@XStreamAlias("model-id")
	private String modelId;
	/**
	 * 是否需要加密
	 */
	@XStreamAsAttribute
	private boolean encrypt;
	
	@XStreamAsAttribute
	@XStreamAlias("data-type")
	private String dataType;
	@XStreamAsAttribute
	@XStreamAlias("default-value-getter")
	private String defaultValueGetter;


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


	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}


	public boolean isPrimary() {
		return primary;
	}

	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

	public boolean isUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}


	public List<ValidateRule> getValidateRules() {
		if (validateRules == null)
			validateRules = new ArrayList<ValidateRule>();
		return validateRules;
	}

	public void setValidateRules(List<ValidateRule> validateRules) {
		this.validateRules = validateRules;
	}

	public boolean isNotNull() {
		return notNull;
	}

	public void setNotNull(boolean notNull) {
		this.notNull = notNull;
	}


	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public boolean isObjectId() {
		return isObjectId;
	}

	public void setObjectId(boolean isObjectId) {
		this.isObjectId = isObjectId;
	}

	public boolean isEncrypt() {
		return encrypt;
	}

	public void setEncrypt(boolean encrypt) {
		this.encrypt = encrypt;
	}

	public String getDataType() {
		if(dataType==null){
			dataType=DEFAULT_DATA_TYPE;
		}
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDefaultValueGetter() {
		return defaultValueGetter;
	}

	public void setDefaultValueGetter(String defaultValueGetter) {
		this.defaultValueGetter = defaultValueGetter;
	}	
	
	
	
}
