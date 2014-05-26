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
package org.tinygroup.xmlparser;

import java.util.List;

import org.tinygroup.parser.Document;
import org.tinygroup.xmlparser.node.XmlNode;

public interface XmlDocument extends Document<XmlNode> {
	/**
	 * 获取XML声明
	 * 
	 * @return XmlNode
	 */
	XmlNode getXmlDeclaration();

	/**
	 * 获取CDATA部分
	 * 
	 * @return List<XmlNode>
	 */
	List<XmlNode> getDoctypeList();

	/**
	 * 获取XML处理指令
	 * 
	 * @return List<XmlNode>
	 */
	List<XmlNode> getProcessingInstructionList();

	/**
	 * 获取Xml注释
	 * 
	 * @return List<XmlNode>
	 * 
	 */
	List<XmlNode> getCommentList();

	/**
	 * 设置XML声明
	 * 
	 * @param xmlDeclaration
	 * @return void
	 */
	void setXmlDeclaration(XmlNode node);

	/**
	 * 添加CDATA文本
	 * 
	 * @param CDATA文本
	 * @return void
	 */
	void addDoctype(XmlNode node);

	/**
	 * 添加XML处理指令
	 * 
	 * @param processingInstruction
	 * @return void
	 */
	void addProcessingInstruction(XmlNode node);

	/**
	 * 添加注释
	 * 
	 * @param comment
	 * @return void
	 */
	void addComment(XmlNode node);

}
