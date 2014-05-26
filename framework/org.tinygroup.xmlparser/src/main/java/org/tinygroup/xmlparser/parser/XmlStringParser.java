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
package org.tinygroup.xmlparser.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.tinygroup.xmlparser.XmlDocument;
import org.tinygroup.xmlparser.XmlNodeType;
import org.tinygroup.xmlparser.document.XmlDocumentImpl;
import org.tinygroup.xmlparser.node.XmlNode;

public class XmlStringParser extends XmlParser<String> {
    private static final String TAIL_END_PATTERN = "TailEndPattern";
    public static final String HEAD_END_PATTERN = "HeadEndPattern";
    private int start = 0;
	private static Pattern endTagName = Pattern
			.compile("(\\w|[\u4e00-\u9fa5]|[.]|[:]|[-])+\\s*>");// 头标签 名正则表达式
	// ，以‘>’结束，不管之前是否为空格符
	private static Pattern startTagName = Pattern
			.compile("(\\w|[\u4e00-\u9fa5]|[-]|[:]|[.])+");// 头标签名正则表达式
	private static Pattern attribute = Pattern // 结点标签属性的正则表达式
			.compile("(\\b(\\w|[\u4e00-\u9fa5]|[/]|[:]|[.]|[-])+\\s*=\\s*\"[^\"]*\")|(\\b(\\w|[\u4e00-\u9fa5]|[/]|[.]|[:]|[-])+\\s*=\\s*'[^']*')|(\\b(\\w|[\u4e00-\u9fa5]|[/]|[.]|[-])+\\s*=\\s*(\\w|[\u4e00-\u9fa5]|[.]|[-])+)");
	private static Map<String, Pattern> patternTable = new HashMap<String, Pattern>();

	private String parseNode(String xmlSource, XmlNode pnode) {
		Pattern pattern = Pattern.compile(getHeadStartPattern());
		Matcher matcher = pattern.matcher(xmlSource);
		matcher.region(start, xmlSource.length());// 匹配域限制
		nexttag: while (matcher.find()) {
			if (start < matcher.start()) {
				String str = xmlSource.substring(start, matcher.start());
				if (str.trim().length() > 0) {
					XmlNode node = new XmlNode(XmlNodeType.TEXT);
					node.setContent(str);
					pnode.addNode(node);
				}
				start = matcher.start();
			}
			String headStart = xmlSource.substring(matcher.start(),
					matcher.end());// 开头匹配字符串
			if (headStart.equals("/>")) {
				// 结束，返回节点
				start = matcher.end();
				return null;
			} else if (headStart.equals("</")) {
				Matcher m = endTagName.matcher(xmlSource);
				m.region(start, xmlSource.length());
				if (m.find()) {
					start = m.end();
					String r = xmlSource.substring(m.start(), m.end() - 1)
							.trim();
					if (r.length() == 0) {
						return null;
					} else if (r.equals(pnode.getNodeName())) {
						return null;
					} else {
						return r;
					}
				}
			}
			for (XmlNodeType nodetype : XmlNodeType.values()) {// 查找已定义好的又相匹配的NoteType
				if (nodetype.getHead() != null
						&& nodetype.getHead().getStart() != null
						&& nodetype.getHead().getStart().equals(headStart)) {
					XmlNode node = new XmlNode(nodetype);
					pnode.addNode(node);
					// 读入标签名称
					if (nodetype == XmlNodeType.ELEMENT) {// 如果为元素标签，则读入元素标签名称
						Matcher m = startTagName.matcher(xmlSource);
						m.region(start, xmlSource.length());
						if (m.find()) {
							node.setNodeName(xmlSource.substring(m.start(),
									m.end()));
							start = m.end();
						}
					}
					// 如果结点有头部，为结点添加属性
					if (nodetype.isHasHeader()) {
						Matcher m = attribute.matcher(xmlSource);
						m.region(start,
								xmlSource.indexOf('>', matcher.end()));
						parseHeader(xmlSource, node, m);
					}
					// 读入头标签结束符
					if (nodetype.getHead().getEnd() != null) {
						Pattern p = patternTable.get(nodetype
								+ HEAD_END_PATTERN);
						if (p == null) {
							p = Pattern.compile(getHeadEndPattern(nodetype));
							patternTable.put(nodetype + HEAD_END_PATTERN, p);
						}
						Matcher m = p.matcher(xmlSource);
						m.region(start, xmlSource.length());
						if (m.find()) {
							char c = xmlSource.charAt(m.start() - 1);
							start = m.end();
							if (c != '/') {
								if (nodetype.isHasBody()) {// 如果结点有子结点，嵌套读取子结点
									String r = parseNode(xmlSource, node);
									if (r != null) {
										if (r.endsWith(pnode.getNodeName())) {
											return null;
										} else {
											return r;
										}
									} else {
										matcher.region(start,
												xmlSource.length());
										continue nexttag;
									}
								}
							} else {// 如果结点为文本内容，继续对其文本内容进行解析
								matcher.region(start, xmlSource.length());
								continue nexttag;
							}
						}
					}
					// 读入尾标签
					if (nodetype.getTail() != null
							&& nodetype.getTail().getEnd() != null) {
						Pattern p = patternTable.get(nodetype
								+ TAIL_END_PATTERN);
						if (p == null) {
							p = Pattern.compile(getTailEndPattern(nodetype));
							patternTable.put(nodetype + TAIL_END_PATTERN, p);
						}
						Matcher m = p.matcher(xmlSource);
						m.region(matcher.end(), xmlSource.length());
						if (m.find()) {
							node.setContent(xmlSource.substring(matcher.end(),
									m.start()));
							start = m.end();
							matcher.region(start, xmlSource.length());
							continue nexttag;
						}
					}
				}
			}
		}
		return null;
	}

	private void parseHeader(String xmlSource, XmlNode node, Matcher m) {
		while (m.find()) {
			String str = xmlSource.substring(m.start(), m.end());
			String k = str.substring(0, str.indexOf('=')).trim();
			String v = str.substring(str.indexOf('=') + 1).trim();
			if (v.startsWith("\"")) {
				v = v.substring(1, v.length() - 1);
			} else if (v.startsWith("'")) {
				v = v.substring(1, v.length() - 1);
			}
			node.setAttribute(k, v);
			start = m.end();
		}
	}

	/**
	 * 解析xml文档
	 * 
	 * @param xmlSource
	 * @return XmlDocument
	 */
	public XmlDocument parse(String xmlSource) {
		XmlDocument document = new XmlDocumentImpl();
		Pattern pattern = Pattern.compile(getHeadStartPattern());
		Matcher matcher = pattern.matcher(xmlSource);
		nexttag: while (matcher.find()) {
			// 前面的无效字符丢弃
			start = matcher.end();
			String headStart = matcher.group();
			for (XmlNodeType nt : XmlNodeType.values()) {
				if (nt.getHead() != null && nt.getHead().getStart() != null
						&& nt.getHead().getStart().equals(headStart)) {// 查找NodeType，在Document中添加相应NodeType结点
					XmlNode node = new XmlNode(nt);
					if (nt == XmlNodeType.ELEMENT) {
						document.setRoot(node);
					} else if (nt == XmlNodeType.COMMENT) {
						document.addComment(node);
					} else if (nt == XmlNodeType.DOCTYPE) {
						document.addDoctype(node);
					} else if (nt == XmlNodeType.XML_DECLARATION) {
						document.setXmlDeclaration(node);
					} else if (nt == XmlNodeType.PROCESSING_INSTRUCTION) {
						document.addProcessingInstruction(node);
					}
					// 读入标签名称
					if (nt == XmlNodeType.ELEMENT) {
						Matcher m = startTagName.matcher(xmlSource);
						m.region(start, xmlSource.length());
						if (m.find()) {
							node.setNodeName(xmlSource.substring(m.start(),
									m.end()));
							start = m.end();
						}
					}
					if (nt.isHasHeader()) {// 为结点添加属性信息
						Matcher m = attribute.matcher(xmlSource);
						m.region(start, xmlSource.indexOf('>', matcher.end()));
						parseHeader(xmlSource, node, m);
					}
					// 读入头标签结束符
					if (nt.getHead().getEnd() != null) {
						Pattern p = patternTable.get(nt + HEAD_END_PATTERN);
						if (p == null) {
							p = Pattern.compile(getHeadEndPattern(nt));
							patternTable.put(nt + HEAD_END_PATTERN, p);
						}
						Matcher m = p.matcher(xmlSource);
						m.region(matcher.end(), xmlSource.length());
						if (m.find()) {
							start = m.end();
							if (nt.isHasBody()) {
								parseNode(xmlSource, node);
								matcher.region(start, xmlSource.length());
								continue nexttag;
							}
						}
					}
					// 读入结尾标签
					if (nt.getTail() != null && nt.getTail().getEnd() != null) {
						Pattern p = patternTable.get(nt + TAIL_END_PATTERN);
						if (p == null) {
							p = Pattern.compile(getTailEndPattern(nt));
							patternTable.put(nt + TAIL_END_PATTERN, p);
						}
						Matcher m = p.matcher(xmlSource);
						m.region(start, xmlSource.length());
						if (m.find()) {
							node.setContent(xmlSource.substring(matcher.end(),
									m.start()));
							start = m.end();
							matcher.region(start, xmlSource.length());
							continue nexttag;
						}
					}
				}
			}
		}
		return document;
	}

}
