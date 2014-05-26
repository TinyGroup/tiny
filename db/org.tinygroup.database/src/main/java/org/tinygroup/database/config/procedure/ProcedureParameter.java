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
package org.tinygroup.database.config.procedure;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 参数
 * @author luoguo
 *
 */
@XStreamAlias("procedure-parameter")
public class ProcedureParameter {
	@XStreamAlias("standard-field-id")
	@XStreamAsAttribute
	private String standardFieldId;// 标准字段名称
	@XStreamAlias("parameter-type")
	@XStreamAsAttribute
	private ParameterType parameterType;// 输入输出
	@XStreamAlias("default-value")
	@XStreamAsAttribute
	private String defaultValue;// 默认值

	

	public String getStandardFieldId() {
		return standardFieldId;
	}

	public void setStandardFieldId(String standardFieldId) {
		this.standardFieldId = standardFieldId;
	}

	public ParameterType getParameterType() {
		return parameterType;
	}

	public void setParameterType(ParameterType parameterType) {
		this.parameterType = parameterType;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

}
