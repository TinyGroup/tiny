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
package org.tinygroup.container.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.tinygroup.container.BaseObject;
import org.tinygroup.container.Category;

public class CategoryImpl<K extends Comparable<K>, T extends BaseObject<K>>
		extends BaseObjectImpl<K> implements Category<K, T> {

	private T parent;
	private List<Category<K, T>> subList;
	
	private K parentId;

	public String getType() {
		return this.getClass().getSimpleName();
	}

	public T getParent() {
		return parent;
	}

	public void setParent(T parent) {
		if(parent!=null){
			this.parentId=parent.getId();
		}
		this.parent = parent;
	}

	public List<Category<K, T>> getSubList() {
		return this.subList;
	}

	public void setSubList(List<Category<K, T>> subList) {
		this.subList = subList;
	}

	public List<Category<K, T>> getSubList(Comparator<BaseObject<K>> comparator) {
		List<Category<K, T>> newList = new ArrayList<Category<K, T>>(subList);
		Collections.sort(newList, comparator);
		return newList;

	}

	/**
	 * 递归查找，要么自己包含，要么子孙包含
	 */
	public boolean containsSub(Category<K, T> category) {
		if (containsDirectly(category)) {
			return true;
		}
		if (subList != null) {
			for (Category<K, T> cat : subList) {
				if (cat.containsSub(category)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 要么自己包含，要要父亲的长辈包含
	 */
	public boolean belong(Category<K, T> category) {
		return category.containsSub(this);
	}

	public boolean containsDirectly(Category<K, T> category) {
		if (subList == null) {
			return false;
		}
		return subList.contains(category);
	}

	public boolean belongDirectly(Category<K, T> category) {
		return category.containsDirectly(this);
	}

	public K getParentId() {
		return parentId;
	}

	public void setParentId(K parentId) {
		this.parentId=parentId;
	}

}
