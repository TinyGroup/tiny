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
package org.tinygroup.metadata.config.stdfield;

import java.util.List;

import org.tinygroup.metadata.config.BaseObject;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("standard-field")
public class StandardField extends BaseObject {
	@XStreamAsAttribute
	@XStreamAlias("business-type-id")
	private String  typeId;// 标准数据类型
	@XStreamAsAttribute
	@XStreamAlias("not-null")
	private boolean notNull;// 是否为空
	@XStreamImplicit
	private List<NickName> nickNames;// 别名列表

	
	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public boolean getNotNull() {
		return notNull;
	}

	public void setNotNull(boolean notNull) {
		this.notNull = notNull;
	}

	

	public List<NickName> getNickNames() {
		return nickNames;
	}

	public void setNickNames(List<NickName> nickNames) {
		this.nickNames = nickNames;
	}

}
