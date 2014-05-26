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
package org.tinygroup.xmlparser.document;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.tinygroup.xmlparser.XmlDocument;
import org.tinygroup.xmlparser.node.XmlNode;

public class XmlDocumentImpl implements XmlDocument {
	private XmlNode root = null;
	private XmlNode xmlDeclaration = null;
	private List<XmlNode> commentList = null;
	private List<XmlNode> doctypeList = null;
	private List<XmlNode> processingInstructionList = null;

	/**
	 * 获取根结点 s
	 * 
	 * @return XmlNode
	 */
	public XmlNode getRoot() {
		return root;
	}

	/**
	 * 设置根结点
	 * 
	 * @param root
	 * @return void
	 */
	public void setRoot(XmlNode root) {
		this.root = root;
	}

	/**
	 * 获取XML声明
	 * 
	 * @return XmlNode
	 */
	public XmlNode getXmlDeclaration() {
		return xmlDeclaration;
	}

	/**
	 * 设置XML声明
	 * 
	 * @param xmlDeclaration
	 * @return void
	 */
	public void setXmlDeclaration(XmlNode xmlDeclaration) {
		this.xmlDeclaration = xmlDeclaration;
	}

	/**
	 * 获取Xml注释
	 * 
	 * @return List<XmlNode>
	 */
	public List<XmlNode> getCommentList() {
		return commentList;
	}

	/**
	 * 获取CDATA部分
	 * 
	 * @return List<XmlNode>
	 */
	public List<XmlNode> getDoctypeList() {
		return doctypeList;
	}

	/**
	 * 获取XML处理指令
	 * 
	 * @return List<XmlNode>
	 */
	public List<XmlNode> getProcessingInstructionList() {
		return processingInstructionList;
	}

	/**
	 * 添加CDATA文本
	 * 
	 * @param CDATA文本
	 * @return void
	 */
	public void addDoctype(XmlNode node) {
		if (doctypeList == null) {
			doctypeList = new ArrayList<XmlNode>();
		}
		doctypeList.add(node);
	}

	/**
	 * 添加XML处理指令
	 * 
	 * @param processingInstruction
	 * @return void
	 */
	public void addProcessingInstruction(XmlNode node) {
		if (processingInstructionList == null) {
			processingInstructionList = new ArrayList<XmlNode>();
		}
		processingInstructionList.add(node);
	}

	/**
	 * 添加注释
	 * 
	 * @param comment
	 * @return void
	 */
	public void addComment(XmlNode node) {
		if (commentList == null) {
			commentList = new ArrayList<XmlNode>();
		}
		commentList.add(node);

	}

	/**
	 * 获取完整XML文档
	 * 
	 * @return String
	 */

	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (xmlDeclaration != null){
			sb.append(this.xmlDeclaration.toStringBuffer());
		}
		if (this.commentList != null) {
			for (XmlNode n : commentList) {
				sb.append(n.toStringBuffer());
			}
		}
		if (this.doctypeList != null) {
			for (XmlNode n : doctypeList) {
				sb.append(n.toStringBuffer());
			}
		}
		if (this.processingInstructionList != null) {
			for (XmlNode n : processingInstructionList) {
				sb.append(n.toStringBuffer());
			}
		}
		sb.append(root.toStringBuffer());
		return sb.toString();
	}

	/**
	 * 将XML文档写入指定的输出流中
	 * 
	 * @param out
	 * @return void
	 */
	public void write(OutputStream out) throws IOException {
		if (xmlDeclaration != null) {
			xmlDeclaration.write(out);
		}
		if (this.commentList != null) {
			for (XmlNode n : commentList) {
				n.write(out);
			}
		}
		if (this.doctypeList != null) {
			for (XmlNode n : doctypeList) {
				n.write(out);
			}
		}
		if (this.processingInstructionList != null) {
			for (XmlNode n : processingInstructionList) {
				n.write(out);
			}
		}
		root.write(out);
	}
}
