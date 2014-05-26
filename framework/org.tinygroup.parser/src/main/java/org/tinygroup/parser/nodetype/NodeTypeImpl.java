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
package org.tinygroup.parser.nodetype;

import org.tinygroup.parser.NodeType;

public class NodeTypeImpl implements NodeType {
	private NodeSign head = null;
	private NodeSign tail = null;
	private boolean hasHeader = true;
	private boolean hasContent = false;
	private boolean hasBody = false;

	public NodeTypeImpl(NodeSign head, NodeSign tail, boolean hasHeader,
			boolean hasContent, boolean hasBody) {
		this.head = head;
		this.tail = tail;
		this.hasHeader = hasHeader;
		this.hasContent = hasContent;
		this.hasBody = hasBody;
	}

	/**
	 * 是否有头部
	 * 
	 * @return boolean
	 */
	public boolean isHasHeader() {
		return hasHeader;
	}

	/**
	 * 是否有子结点
	 * 
	 * @return boolean
	 */
	public boolean isHasBody() {
		return hasBody;
	}

	/**
	 * 是否有文本内容
	 * 
	 * @return boolean
	 */
	public boolean isHasContent() {
		return hasContent;
	}

	/**
	 * 获取头标签
	 * 
	 * @return NodeSign
	 * 
	 */
	public NodeSign getHead() {
		return head;
	}

	/**
	 * 获取结尾标签
	 * 
	 * @return NodeSign
	 * 
	 */
	public NodeSign getTail() {
		return tail;
	}

	/**
	 * 获取头标签 为输入参数添加头标签
	 * 
	 * @param str
	 * @return StringBuffer
	 */
	public void getHeader(StringBuffer sb, String str) {
		if (head != null) {
			if (head.getStart() != null) {
				sb.append(head.getStart());
			}
			sb.append(str);
			// if (head.getEnd() != null)
			sb.append(head.getEnd());
		}
	}

	/**
	 * 获取结尾标签 为输入参数添加结尾标签
	 * 
	 * @param str
	 * @return StringBuffer
	 * 
	 */
	public void getTail(StringBuffer sb, String str) {
		if (tail != null) {
			if (tail.getStart() != null) {
				sb.append(tail.getStart());
			}
			sb.append(str);
			if (tail.getEnd() != null) {
				sb.append(tail.getEnd());
			}
		}
	}

	public boolean isText() {
		return false;
	}
}
