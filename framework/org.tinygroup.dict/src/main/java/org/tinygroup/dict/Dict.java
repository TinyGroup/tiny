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
package org.tinygroup.dict;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 字典类型
 * 
 * @author luoguo
 * 
 */
public final class Dict implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2990470143017973104L;
	private String name;// 类型名称
	private String description;// 描述
	private List<DictGroup> dictGroupList;// 字典分组列表，一个字典可以包含多个字典分组

	public Dict() {
	}

	public Dict(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public void addDictGroup(DictGroup dictGroup) {
		if (dictGroupList == null) {
			dictGroupList = new ArrayList<DictGroup>();
		}
		dictGroupList.add(dictGroup);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<DictGroup> getDictGroupList() {
		return dictGroupList;
	}

	public void setDictGroupList(List<DictGroup> dictGroupList) {
		this.dictGroupList = dictGroupList;
	}

}
