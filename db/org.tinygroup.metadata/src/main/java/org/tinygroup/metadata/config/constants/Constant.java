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
package org.tinygroup.metadata.config.constants;

import org.tinygroup.metadata.config.BaseObject;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("constant")
public class Constant extends BaseObject {
	@XStreamAlias("standard-type-name")
	private String standardTypeName;// 标准字段类型名
	@XStreamAsAttribute
	private String value;// 常量的值

	public String getStandardTypeName() {
		return standardTypeName;
	}

	public void setStandardTypeName(String standardTypeName) {
		this.standardTypeName = standardTypeName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
