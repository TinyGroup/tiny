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
package org.tinygroup.xmlparser.node;

import org.tinygroup.parser.node.NodeImpl;
import org.tinygroup.xmlparser.XmlNodeType;

/**
 *
 * @author luoguo
 * 
 */
public class XmlNode extends NodeImpl<XmlNode, XmlNodeType> {

	/**
	 * 构造方法
	 * 
	 * @param nodeType
	 */
	public XmlNode(XmlNodeType nodeType) {
		super(nodeType);
	}

	/**
	 * 构造方法
	 * 
	 * @param nodeName
	 */
	public XmlNode(String nodeName) {
		super(nodeName, XmlNodeType.ELEMENT);
	}

	/**
	 * 构造方法
	 * 
	 * @param nodeType
	 * @param nodeName
	 */
	public XmlNode(XmlNodeType nodeType, String nodeName) {
		super(nodeType, nodeName);
	}

	/**
	 * 构造方法
	 * 
	 * @param nodeName
	 * @param nodeType
	 */
	public XmlNode(String nodeName, XmlNodeType nodeType) {
		super(nodeType, nodeName);
	}

	public void addContent(String content) {
		XmlNode node = new XmlNode(XmlNodeType.TEXT);
		node.setContent(content);
		addNode(node);
	}

	
	protected String encode(String string) {
		String str = string;
		str = str.replaceAll("&", "&amp;");
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll("\"", "&quot;");
		str = str.replaceAll("'", "&apos;");
		return str;
	}

	
	protected String decode(String string) {
		String str = string;
		str = str.replaceAll("&amp;", "&");
		str = str.replaceAll("&lt;", "<");
		str = str.replaceAll("&gt;", ">");
		str = str.replaceAll("&quot;", "\"");
		str = str.replaceAll("&apos;", "'");
		return str;
	}

	public String getPureText() {
		StringBuffer sb = new StringBuffer();
		getPureText(this, sb);
		return sb.toString();
	}

	void getPureText(XmlNode node, StringBuffer sb) {
		if (node.getNodeType() == XmlNodeType.CDATA
				|| node.getNodeType() == XmlNodeType.TEXT) {
			String content = node.getContent();
			if (content != null) {
				sb.append(content).append(" ");
			}
		} else {
			if (node.getNodeType().isHasHeader() && node.getSubNodes() != null) {
				for (XmlNode n : node.getSubNodes()) {
					getPureText(n, sb);
				}
			}
		}
	}

	public boolean isCaseSensitive() {
		return true;
	}

}
