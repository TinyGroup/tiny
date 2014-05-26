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
package org.tinygroup.htmlparser.document;

import org.tinygroup.htmlparser.HtmlDocument;
import org.tinygroup.htmlparser.node.HtmlNode;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class HtmlDocumentImpl implements HtmlDocument {
	private HtmlNode root = null;
	private HtmlNode htmlDeclaration = null;
	private List<HtmlNode> commentList = null;
	private List<HtmlNode> doctypeList = null;
	private List<HtmlNode> processingInstructionList = null;

	/**
	 * 获取根结点 s
	 * 
	 * @return HtmlNode
	 */
	public HtmlNode getRoot() {
		return root;
	}

	/**
	 * 设置根结点
	 * 
	 * @param root
	 * @return void
	 */
	public void setRoot(HtmlNode root) {
		this.root = root;
	}

	/**
	 * 获取HTML声明
	 * 
	 * @return HtmlNode
	 */
	public HtmlNode getHtmlDeclaration() {
		return htmlDeclaration;
	}

	/**
	 * 设置HTML声明
	 * 
	 * @param htmlDeclaration
	 * @return void
	 */
	public void setHtmlDeclaration(HtmlNode htmlDeclaration) {
		this.htmlDeclaration = htmlDeclaration;
	}

	/**
	 * 获取Html注释
	 * 
	 * @return List<HtmlNode>
	 */
	public List<HtmlNode> getCommentList() {
		return commentList;
	}

	/**
	 * 获取CDATA部分
	 * 
	 * @return List<HtmlNode>
	 */
	public List<HtmlNode> getDoctypeList() {
		return doctypeList;
	}

	/**
	 * 获取HTML处理指令
	 * 
	 * @return List<HtmlNode>
	 */
	public List<HtmlNode> getProcessingInstructionList() {
		return processingInstructionList;
	}

	/**
	 * 添加CDATA文本
	 * 
	 * @param node
	 * @return void
	 */
	public void addDoctype(HtmlNode node) {
		if (doctypeList == null) {
			doctypeList = new ArrayList<HtmlNode>();
		}
		doctypeList.add(node);
	}

	/**
	 * 添加HTML处理指令
	 * 
	 * @param node
	 * @return void
	 */
	public void addProcessingInstruction(HtmlNode node) {
		if (processingInstructionList == null) {
			processingInstructionList = new ArrayList<HtmlNode>();
		}
		processingInstructionList.add(node);
	}

	/**
	 * 添加注释
	 * 
	 * @param node
	 * @return void
	 */
	public void addComment(HtmlNode node) {
		if (commentList == null) {
			commentList = new ArrayList<HtmlNode>();
		}
		commentList.add(node);

	}

	/**
	 * 获取完整HTML文档
	 * 
	 * @return String
	 */

	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (htmlDeclaration != null){
			sb.append(this.htmlDeclaration.toStringBuffer());
		}
		if (this.commentList != null) {
			for (HtmlNode n : commentList) {
				sb.append(n.toStringBuffer());
			}
		}
		if (this.doctypeList != null) {
			for (HtmlNode n : doctypeList) {
				sb.append(n.toStringBuffer());
			}
		}
		if (this.processingInstructionList != null) {
			for (HtmlNode n : processingInstructionList) {
				sb.append(n.toStringBuffer());
			}
		}
		sb.append(root.toStringBuffer());
		return sb.toString();
	}

	/**
	 * 将HTML文档写入指定的输出流中
	 * 
	 * @param out
	 * @return void
	 */
	public void write(OutputStream out) throws IOException {
		if (htmlDeclaration != null) {
			htmlDeclaration.write(out);
		}
		if (this.commentList != null) {
			for (HtmlNode n : commentList) {
				n.write(out);
			}
		}
		if (this.doctypeList != null) {
			for (HtmlNode n : doctypeList) {
				n.write(out);
			}
		}
		if (this.processingInstructionList != null) {
			for (HtmlNode n : processingInstructionList) {
				n.write(out);
			}
		}
		root.write(out);
	}
}
