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
package org.tinygroup.database.config.table;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.metadata.config.BaseObject;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("index")
public class Index extends BaseObject {
	@XStreamAsAttribute
	private Boolean unique;// 是否唯一
	@XStreamAsAttribute
	private Boolean reverse;// 是否反向
	@XStreamImplicit
	private List<String> fields;// 字段列表，中间用逗号分隔

	public String getName() {
		return super.getName();
	}
	
	public Boolean getUnique() {
		return unique;
	}

	public void setUnique(Boolean unique) {
		this.unique = unique;
	}

	public Boolean getReverse() {
		return reverse;
	}

	public void setReverse(Boolean reverse) {
		this.reverse = reverse;
	}

	public List<String> getFields() {
		if (fields == null)
			fields = new ArrayList<String>();
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

}
