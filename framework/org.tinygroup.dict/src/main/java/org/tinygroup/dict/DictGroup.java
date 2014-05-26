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
 * 字典分组
 * 
 * @author luoguo
 * 
 */
public final class DictGroup implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1208765878434753097L;
	private String name;// 分组名称
	private String text;// 分组说明
	private List<DictItem> itemList;// 组内字典项列表

	public DictGroup() {

	}

	public DictGroup(String name, String text) {
		this.name = name;
		this.text = text;
	}

	public void addDictItem(DictItem dictItem) {
		if (itemList == null) {
			itemList = new ArrayList<DictItem>();
		}
		itemList.add(dictItem);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<DictItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<DictItem> itemList) {
		this.itemList = itemList;
	}

}
