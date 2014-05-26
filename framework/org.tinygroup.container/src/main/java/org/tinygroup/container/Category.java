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
package org.tinygroup.container;

import java.util.Comparator;
import java.util.List;

/**
 * 分类接口<br>
 * 分类用于组织树状结构数据
 * 
 * @author luoguo
 * 
 */
public interface Category<K extends Comparable<K>, T extends BaseObject<K>>
		extends BaseObject<K> {
	T getParent();// 返回上级分类

	void setParent(T parent);// 设置上级分类

	List<Category<K, T>> getSubList();// 返回下级分类

	void setSubList(List<Category<K, T>> subList);// 设置下级分类

	List<Category<K, T>> getSubList(Comparator<BaseObject<K>> comparator);// 返回经过排序的下级分类

	boolean containsSub(Category<K, T> category);// 返回是否包含某节点，自己包含或子孙包含

	boolean belong(Category<K, T> category);// 返回是否属于某节点，属于或父辈属于

	boolean containsDirectly(Category<K, T> category);// 返回是否直接包含某节点

	boolean belongDirectly(Category<K, T> category);// 返回是否直接属于某节点
	/**
	 * 
	 * 父节点标识
	 * @return
	 */
	K getParentId();
	
	void setParentId(K parentId);
	
}
