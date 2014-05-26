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
package org.tinygroup.metadata.config.stddatatype;

import java.util.List;

import org.tinygroup.metadata.config.PlaceholderValue;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 方言类型
 * @author luoguo
 *
 */
@XStreamAlias("dialect-type")
public class DialectType {
	@XStreamAsAttribute
	private String language;// 语言
	@XStreamAsAttribute
	private String type;// 类型 带长度或者精度的，例如varchar20
	@XStreamAsAttribute
	@XStreamAlias("type-name")
	private String typeName;//不带长度与精度的，例如varchar，用于指定数据源依赖的类型名称
	@XStreamAlias("data-type")
	@XStreamAsAttribute
	private int dataType;//用于指定java.sql.Types 的 SQL 类型 
	@XStreamAsAttribute
	@XStreamAlias("default-value")
	private String defaultValue;// 默认值
	@XStreamImplicit
	private List<PlaceholderValue> placeholderValueList;// 占位符列表

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public List<PlaceholderValue> getPlaceholderValueList() {
		return placeholderValueList;
	}

	public void setPlaceholderValueList(
			List<PlaceholderValue> placeholderValueList) {
		this.placeholderValueList = placeholderValueList;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	
	

}
