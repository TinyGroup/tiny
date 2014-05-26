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
package org.tinygroup.binarytree;

import java.util.Iterator;

/**
 * 平衡二驻树
 * 
 * @author luoguo
 * 
 * @param <T>
 */
public interface AVLTree<T extends Comparable<T>> {

	/**
	 * 返回迭代器
	 * 
	 * @return
	 */
	Iterator<T> iterator();

	/**
	 * 添加一个元素到平衡二叉树当中去
	 * 
	 * @param elem
	 * @return
	 */
	boolean add(T elem);

	boolean[] add(T[] elem);

	/**
	 * 从平衡二叉树中删除一个元素
	 * 
	 * @param elem
	 * @return
	 */
	boolean remove(T elem);

	/**
	 * 求出平衡二叉树中元素的个数
	 * 
	 * @return
	 */
	int size();

	/**
	 * 求出平衡二叉树的高度
	 * 
	 * @return
	 */
	int height();

	/**
	 * 在平衡二叉树中获取一个元素
	 * 
	 * @param e
	 *            通过compareTo比较为0即可的元素，不一定与二叉树中的元素完全相同
	 * @return 返回在平衡二叉树中的与e通过compareTo比较为0的元素，如果找不到，则返回null
	 */
	T contains(T e);

	/**
	 * 树的高度非递归求法
	 * 
	 * @return
	 */
	int heightIter();

}
