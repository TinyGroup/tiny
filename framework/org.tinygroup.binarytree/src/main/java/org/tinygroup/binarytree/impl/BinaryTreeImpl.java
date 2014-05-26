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
package org.tinygroup.binarytree.impl;

import org.tinygroup.binarytree.AVLTree;
import org.tinygroup.binarytree.BinaryTree;
import org.tinygroup.commons.exceptions.ComparableException;
import org.tinygroup.commons.processor.Processor;

/**
 * 二叉树实现
 * 
 * @author luoguo
 * 
 * @param <T>
 */

public class BinaryTreeImpl<T extends Comparable<T>> implements BinaryTree<T> {
	private BinaryTreeImpl<T> parent = null;
	private BinaryTreeImpl<T> left = null;
	private BinaryTreeImpl<T> right = null;
	private T data;

	/**
	 * 构造函数
	 * 
	 * @param data
	 * @throws CompareException
	 */
	public BinaryTreeImpl(T data) {
		if (data instanceof Comparable) {
			this.data = data;
		} else {
			throw new ComparableException(data.getClass().getSimpleName()
					+ " must implements from Comparable.");
		}
	}

	/**
	 * 查找指定结点
	 * 
	 * @param find
	 * @return IBinaryTree<T>
	 */
	public BinaryTree<T> search(T find) {
		int c = compare(left.data, find);
		if (c == 0) {
			return this;
		} else if (c < 0) {
			return left.search(find);
		} else {
			return right.search(find);
		}
	}

	/**
	 * 添加新结点 返回所添加的结点
	 * 
	 * @param object
	 * @return IBinaryTree<T>
	 */
	public BinaryTree<T> add(T object) {
		if (compare(data, object) > 0) {
			if (left == null) {
				BinaryTreeImpl<T> tree = new BinaryTreeImpl<T>(object);
				tree.parent = this;
				left = tree;
			} else {
				left.add(object);
			}
		} else if (compare(data, object) < 0) {

			if (right == null) {
				BinaryTreeImpl<T> tree = new BinaryTreeImpl<T>(object);
				tree.parent = this;
				right = tree;
			} else {
				right.add(object);
			}
		}
		return this;
	}

	/**
	 * 获取结点数据
	 * 
	 * @return T
	 */
	public T getData() {
		return data;
	}

	private static class AddToAvlTree<T extends Comparable<T>> implements
			Processor<T> {

		private AVLTree<T> tree;

		public AddToAvlTree(AVLTree<T> avlTree) {
			this.tree = avlTree;
		}

		public void process(T data) {
			tree.add(data);
		}
	}

	public int compare(T a, T b) {
		return a.compareTo(b);
	}

	/**
	 * 左右子树对调
	 * 
	 * @return void
	 */
	public void swap() {
		BinaryTreeImpl<T> tree = this.left;
		this.left = this.right;
		this.right = tree;
		if (left != null) {
			left.swap();
		}
		if (right != null) {
			right.swap();
		}
	}

	/**
	 * 删除指定结点子树
	 * 
	 * @param o
	 * @return void
	 */
	public void remove(T o) {
		if (compare(left.data, o) == 0) {
			left = null;
		} else if (compare(right.data, o) == 0) {
			right = null;
		} else {
			left.remove(o);
			right.remove(o);
		}
	}

	/**
	 * 默认采用中序
	 */
	public void foreach(Processor<T> process) {
		foreach(process, BinaryTree.LDR_ORDER);
	}

	/**
	 * 用指定的遍历方式对每个结点执行操作 mode: 0:中序遍历 -1:前序遍历 1：后序遍历
	 * 
	 * @param process
	 * @param mode
	 * @return void
	 */
	public void foreach(Processor<T> process, int mode) {
		if (mode == BinaryTree.LDR_ORDER) {
			if (left != null) {
				left.foreach(process, mode);
			}
			process.process(this.data);
			if (right != null) {
				right.foreach(process, mode);
			}
		} else if (mode == BinaryTree.DLR_ORDER) {
			process.process(this.data);
			if (left != null) {
				left.foreach(process, mode);
			}
			if (right != null) {
				right.foreach(process, mode);
			}
		} else if (mode == BinaryTree.LRD_ORDER) {
			if (left != null) {
				left.foreach(process, mode);
			}
			if (right != null) {
				right.foreach(process, mode);
			}
			process.process(this.data);
		}
	}

	/**
	 * 获取树的结点个数
	 * 
	 * @return int
	 */
	public int size() {
		int s = 1;
		if (left != null) {
			s += left.size();
		}
		if (right != null) {
			s += right.size();
		}
		return s;
	}

	/**
	 * 获取树的高度
	 * 
	 * @return int
	 */
	public int height() {
		int lh = 0;
		int rh = 0;
		if (left != null) {
			lh = left.height();
		}
		if (right != null) {
			rh += right.height();
		}
		if (lh > rh) {
			return 1 + lh;
		} else {
			return 1 + rh;
		}
	}

	/**
	 * 转换成平衡二叉树
	 * 
	 * @return IAVLTree<T>
	 */
	public AVLTree<T> toAVLTree() {
		AVLTree<T> avlTree = new AVLTreeImpl<T>();
		AddToAvlTree<T> ps = new AddToAvlTree<T>(avlTree);
		foreach(ps, BinaryTree.LDR_ORDER);
		return avlTree;
	}

	public BinaryTreeImpl<T> getParent() {
		return parent;
	}

}
