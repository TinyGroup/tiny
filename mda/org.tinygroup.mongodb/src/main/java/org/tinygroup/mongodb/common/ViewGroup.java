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
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 字段分组
 * 
 * @author luoguo
 * 
 */
@XStreamAlias("view-group")
public class ViewGroup extends BaseObject {
	@XStreamAsAttribute
	String url;//来自哪个URL，如果有这个值，则不能再包含操作字段列表,如果有，也会被忽略
	@XStreamImplicit
	List<DisplayField> fields;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}



	public List<DisplayField> getFields() {
		if (fields == null)
			fields = new ArrayList<DisplayField>();
		return fields;
	}

	public void setFields(List<DisplayField> fields) {
		this.fields = fields;
	}

}
