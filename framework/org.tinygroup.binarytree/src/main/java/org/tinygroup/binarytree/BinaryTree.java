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

import org.tinygroup.binarytree.impl.BinaryTreeImpl;
import org.tinygroup.commons.processor.Processor;

/**
 * Created by IntelliJ IDEA.<BR>
 * User: luoguo <BR>
 * Date: 11-3-31 Time: 上午11:10<BR>
 * To change this template use File | Settings | File Templates.
 */
public interface BinaryTree<T extends Comparable<T>> {
	int DLR_ORDER = -1;// 前序
	int LDR_ORDER = 0;// 中序
	int LRD_ORDER = 1;// 后序

	BinaryTree<T> search(T find);

	/**
	 * 增加一个节点
	 * 
	 * @param t
	 */
	BinaryTree<T> add(T t);

	/**
	 * 返回当前节点数据
	 * 
	 * @return
	 */
	T getData();

	/**
	 * 删除指定结点子树
	 * 
	 * @param t
	 */
	void remove(T t);

	/**
	 * 对每个节点执行操作
	 * 
	 * @param process
	 */
	void foreach(Processor<T> process);

	/**
	 * 用指定的遍历方式对每一个节点执行操作
	 * 
	 * @param process
	 * @param mode
	 *            前序、后序、中序
	 */
	void foreach(Processor<T> process, int mode);

	BinaryTreeImpl<T> getParent();

	/**
	 * 求树的节点数
	 * 
	 * @return
	 */
	int size();

	/**
	 * 获取树的高度
	 * 
	 * @return
	 */
	int height();

	/**
	 * 转换为平衡二叉树
	 * 
	 * @return
	 */
	AVLTree<T> toAVLTree();
}
