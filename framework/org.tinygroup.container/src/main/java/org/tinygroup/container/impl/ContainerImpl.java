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
import org.tinygroup.container.Container;

public class ContainerImpl<K extends Comparable<K>, T extends BaseObject<K>>
		extends BaseObjectImpl<K> implements Container<K, T> {

	private List<T> list;

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;

	}

	public List<T> getList(Comparator<T> comparator) {
		List<T> newList = new ArrayList<T>(list);
		Collections.sort(newList, comparator);
		return newList;
	}

	public boolean contains(T object) {
		return list.contains(object);
	}

}
