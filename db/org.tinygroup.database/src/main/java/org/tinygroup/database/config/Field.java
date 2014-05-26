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
package org.tinygroup.database.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("field")
public class Field{
	@XStreamAsAttribute
	@XStreamAlias("standard-field-id")
	private String standardFieldId;// 标准字段Id
	@XStreamAsAttribute
	private boolean primary;
	@XStreamAsAttribute
	private boolean unique;
	@XStreamAsAttribute
	@XStreamAlias("not-null")
	private boolean notNull;
	@XStreamAsAttribute
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStandardFieldId() {
		return standardFieldId;
	}

	public void setStandardFieldId(String standardFieldId) {
		this.standardFieldId = standardFieldId;
	}

	public void setNotNull(boolean notNull) {
		this.notNull = notNull;
	}

	public boolean getPrimary() {
		return primary;
	}

	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

	public boolean getUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public boolean getNotNull() {
		return notNull;
	}

}
