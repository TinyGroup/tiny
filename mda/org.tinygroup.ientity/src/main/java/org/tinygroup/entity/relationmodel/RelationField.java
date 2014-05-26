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
package org.tinygroup.entity.relationmodel;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 模型的属性
 * 
 * @author luoguo
 * 
 */
@XStreamAlias("relation-field")
public class RelationField {

	/**
	 * 是否显示字段
	 */
	@XStreamAsAttribute
	private boolean display;
	/**
	 * 引用的字段ID
	 */
	@XStreamAlias("field-id")
	@XStreamAsAttribute
	String fieldId;
	/**
	 * 实体名称，如果存在，就表示是引用自另外的模型引用
	 */
	@XStreamAsAttribute
	@XStreamAlias("model-id")
	private String modelId;

	@XStreamAsAttribute
	@XStreamAlias("alise-name")
	private String aliseName;// 别名
	/**
	 * 聚合函数,如果有，就生成having子句
	 */
	@XStreamAsAttribute
	@XStreamAlias("aggregate-function")
	String aggregateFunction;
	@XStreamAsAttribute
	@XStreamAlias("ref-field-id")//引用的数据库字段
	String refFieldId;
	
	@XStreamAsAttribute
	@XStreamAlias("title")//引用的数据库字段
	String title;


	public String getAggregateFunction() {
		return aggregateFunction;
	}

	public void setAggregateFunction(String aggregateFunction) {
		this.aggregateFunction = aggregateFunction;
	}

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getAliseName() {
		return aliseName;
	}

	public void setAliseName(String aliseName) {
		this.aliseName = aliseName;
	}

	public String getRefFieldId() {
		return refFieldId;
	}

	public void setRefFieldId(String refFieldId) {
		this.refFieldId = refFieldId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	

}
