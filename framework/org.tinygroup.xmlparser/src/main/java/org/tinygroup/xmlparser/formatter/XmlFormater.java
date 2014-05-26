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
package org.tinygroup.xmlparser.formatter;

import java.io.IOException;
import java.io.OutputStream;

import org.tinygroup.parser.formater.NodeFormaterImpl;
import org.tinygroup.xmlparser.XmlDocument;
import org.tinygroup.xmlparser.node.XmlNode;

public class XmlFormater extends NodeFormaterImpl<XmlDocument, XmlNode> {

	/**
	 * 格式化xml文档 并以StringBuffer格式返回
	 * 
	 * @param doc
	 * @return StringBuffer
	 */
	
	protected StringBuffer formatDocumentSelf(XmlDocument doc) {
		StringBuffer sb = new StringBuffer();
		if (doc.getXmlDeclaration() != null) {
			formatNode(sb, doc.getXmlDeclaration(), 0);
		}
		if (doc.getDoctypeList() != null) {
			for (XmlNode n : doc.getDoctypeList()) {
				formatNode(sb, n, 0);
			}
		}
		if (doc.getCommentList() != null) {
			for (XmlNode n : doc.getCommentList()) {
				formatNode(sb, n, 0);
			}
		}
		return sb;
	}

	/**
	 * 格式化xml文档 并在指定的输出流中输出
	 * 
	 * @param doc
	 * @param out
	 * @return void
	 */
	
	protected void formatDocumentSelf(XmlDocument doc, OutputStream out)
			throws IOException {
		if (doc.getXmlDeclaration() != null) {
			formatNode(doc.getXmlDeclaration(), out, 0);
		}
		if (doc.getDoctypeList() != null) {
			for (XmlNode n : doc.getDoctypeList()) {
				formatNode(n, out, 0);
			}
		}
		if (doc.getCommentList() != null) {
			for (XmlNode n : doc.getCommentList()) {
				formatNode(n, out, 0);
			}
		}
	}
}
