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
package org.tinygroup.htmlparser.parser;

import org.tinygroup.htmlparser.HtmlDocument;
import org.tinygroup.htmlparser.HtmlNodeType;
import org.tinygroup.htmlparser.document.HtmlDocumentImpl;
import org.tinygroup.htmlparser.node.HtmlNode;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlStringParser extends HtmlParser<String> {
    private static final String TAIL_END_PATTERN = "TailEndPattern";
    public static final String HEAD_END_PATTERN = "HeadEndPattern";
    private int start = 0;
    private static Pattern endTagName = Pattern.compile("(\\w|[\u4e00-\u9fa5]|[.]|[:]|[-])+\\s*>");// 头标签 名正则表达式
    // ，以‘>’结束，不管之前是否为空格符
    private static Pattern startTagName = Pattern.compile("(\\w|[\u4e00-\u9fa5]|[-]|[:]|[.])+");// 头标签名正则表达式
    private static Pattern attribute = Pattern // 结点标签属性的正则表达式
            .compile("(\\b(\\w|[\u4e00-\u9fa5]|[/]|[:]|[.]|[-])+\\s*=\\s*\"[^\"]*\")|(\\b(\\w|[\u4e00-\u9fa5]|[/]|[.]|[:]|[-])+\\s*=\\s*'[^']*')|(\\b(\\w|[\u4e00-\u9fa5]|[/]|[.]|[-])+\\s*=\\s*(\\w|[\u4e00-\u9fa5]|[.]|[-])+)|(\\w|[\u4e00-\u9fa5]|[.]|[:]|[-])+");
    private static Map<String, Pattern> patternTable = new HashMap<String, Pattern>();

    private String parseNode(String htmlSource, HtmlNode pnode) {
        Pattern pattern = Pattern.compile(getHeadStartPattern());
        Matcher matcher = pattern.matcher(htmlSource);
        matcher.region(start, htmlSource.length());// 匹配域限制
        nexttag:
        while (matcher.find()) {
            if (start < matcher.start()) {
                String str = htmlSource.substring(start, matcher.start());
                if (str.trim().length() > 0) {
                    HtmlNode node = new HtmlNode(HtmlNodeType.TEXT);
                    node.setContent(str);
                    pnode.addNode(node);
                }
                start = matcher.start();
            }
            String headStart = htmlSource.substring(matcher.start(), matcher.end());// 开头匹配字符串
            if (headStart.equals("/>")) {
                // 结束，返回节点
                start = matcher.end();
                return null;
            } else if (headStart.equals("</")) {
                Matcher m = endTagName.matcher(htmlSource);
                m.region(start, htmlSource.length());
                if (m.find()) {
                    start = m.end();
                    String r = htmlSource.substring(m.start(), m.end() - 1).trim();
                    if (r.length() == 0) {
                        return null;
                    } else if (r.equals(pnode.getNodeName())) {
                        return null;
                    } else {
                        return r;
                    }
                }
            }
            for (HtmlNodeType nodetype : HtmlNodeType.values()) {// 查找已定义好的又相匹配的NoteType
                if (nodetype.getHead() != null && nodetype.getHead().getStart() != null
                        && nodetype.getHead().getStart().equals(headStart)) {
                    HtmlNode node = new HtmlNode(nodetype);
                    pnode.addNode(node);
                    // 读入标签名称
                    if (nodetype == HtmlNodeType.ELEMENT) {// 如果为元素标签，则读入元素标签名称
                        Matcher m = startTagName.matcher(htmlSource);
                        m.region(start, htmlSource.length());
                        if (m.find()) {
                            node.setNodeName(htmlSource.substring(m.start(), m.end()));
                            start = m.end();
                        }
                    }
                    // 如果结点有头部，为结点添加属性
                    if (nodetype.isHasHeader()) {
                        Matcher m = attribute.matcher(htmlSource);
                        m.region(start, htmlSource.indexOf('>', matcher.end()));
                        parseHeader(htmlSource, node, m);
                    }
                    // 读入头标签结束符
                    if (nodetype.getHead().getEnd() != null) {
                        Pattern p = patternTable.get(nodetype + HEAD_END_PATTERN);
                        if (p == null) {
                            p = Pattern.compile(getHeadEndPattern(nodetype));
                            patternTable.put(nodetype + HEAD_END_PATTERN, p);
                        }
                        Matcher m = p.matcher(htmlSource);
                        m.region(start, htmlSource.length());
                        if (m.find()) {
                            char c = htmlSource.charAt(m.start() - 1);
                            start = m.end();
                            if (c != '/') {
                                if (nodetype.isHasBody()) {// 如果结点有子结点，嵌套读取子结点
                                    String r = parseNode(htmlSource, node);
                                    if (r != null) {
                                        if (r.endsWith(pnode.getNodeName())) {
                                            return null;
                                        } else {
                                            return r;
                                        }
                                    } else {
                                        matcher.region(start, htmlSource.length());
                                        continue nexttag;
                                    }
                                }
                            } else {// 如果结点为文本内容，继续对其文本内容进行解析
                                matcher.region(start, htmlSource.length());
                                continue nexttag;
                            }
                        }
                    }
                    // 读入尾标签
                    if (nodetype.getTail() != null && nodetype.getTail().getEnd() != null) {
                        Pattern p = patternTable.get(nodetype + TAIL_END_PATTERN);
                        if (p == null) {
                            p = Pattern.compile(getTailEndPattern(nodetype));
                            patternTable.put(nodetype + TAIL_END_PATTERN, p);
                        }
                        Matcher m = p.matcher(htmlSource);
                        m.region(matcher.end(), htmlSource.length());
                        if (m.find()) {
                            node.setContent(htmlSource.substring(matcher.end(), m.start()));
                            start = m.end();
                            matcher.region(start, htmlSource.length());
                            continue nexttag;
                        }
                    }
                }
            }
        }
        return null;
    }

    private void parseHeader(String htmlSource, HtmlNode node, Matcher m) {
        while (m.find()) {
            String str = htmlSource.substring(m.start(), m.end());
            if (str.indexOf('=') > 0) {
                String k = str.substring(0, str.indexOf('=')).trim();
                String v = str.substring(str.indexOf('=') + 1).trim();
                if (v.startsWith("\"")) {
                    v = v.substring(1, v.length() - 1);
                } else if (v.startsWith("'")) {
                    v = v.substring(1, v.length() - 1);
                }
                node.setAttribute(k, v);
            } else {
                node.setAttribute(str, str);
            }
            start = m.end();

        }
    }

    /**
     * 解析html文档
     *
     * @param htmlSource
     * @return HtmlDocument
     */
    public HtmlDocument parse(String htmlSource) {
        HtmlDocument document = new HtmlDocumentImpl();
        Pattern pattern = Pattern.compile(getHeadStartPattern());
        Matcher matcher = pattern.matcher(htmlSource);
        HtmlNode rootNode = new HtmlNode(HtmlNodeType.ELEMENT);
        document.setRoot(rootNode);
        nextTag:
        while (matcher.find()) {
            // 前面的无效字符丢弃
            start = matcher.end();
            String headStart = matcher.group();
            for (HtmlNodeType nt : HtmlNodeType.values()) {
                if (nt.getHead() != null && nt.getHead().getStart() != null
                        && nt.getHead().getStart().equals(headStart)) {// 查找NodeType，在Document中添加相应NodeType结点
                    HtmlNode node = new HtmlNode(nt);
                    if (nt == HtmlNodeType.ELEMENT) {
                        rootNode.addNode(node);
                    } else if (nt == HtmlNodeType.COMMENT) {
                        document.addComment(node);
                    } else if (nt == HtmlNodeType.DOCTYPE) {
                        document.addDoctype(node);
                    } else if (nt == HtmlNodeType.PROCESSING_INSTRUCTION) {
                        document.addProcessingInstruction(node);
                    }
                    // 读入标签名称
                    if (nt == HtmlNodeType.ELEMENT) {
                        Matcher m = startTagName.matcher(htmlSource);
                        m.region(start, htmlSource.length());
                        if (m.find()) {
                            node.setNodeName(htmlSource.substring(m.start(), m.end()));
                            start = m.end();
                        }
                    }
                    if (nt.isHasHeader()) {// 为结点添加属性信息
                        Matcher m = attribute.matcher(htmlSource);
                        m.region(start, htmlSource.indexOf('>', matcher.end()));
                        parseHeader(htmlSource, node, m);
                    }
                    // 读入头标签结束符
                    if (nt.getHead().getEnd() != null) {
                        Pattern p = patternTable.get(nt + HEAD_END_PATTERN);
                        if (p == null) {
                            p = Pattern.compile(getHeadEndPattern(nt));
                            patternTable.put(nt + HEAD_END_PATTERN, p);
                        }
                        Matcher m = p.matcher(htmlSource);
                        m.region(start, htmlSource.length());
                        if (m.find()) {
                            start = m.end();
                            if (nt.isHasBody()) {
                                parseNode(htmlSource, node);
                                matcher.region(start, htmlSource.length());
                                continue nextTag;
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
                        Matcher m = p.matcher(htmlSource);
                        m.region(start, htmlSource.length());
                        if (m.find()) {
                            node.setContent(htmlSource.substring(matcher.end(), m.start()));
                            start = m.end();
                            matcher.region(start, htmlSource.length());
                            continue nextTag;
                        }
                    }
                }
            }
        }
        HtmlNode root = document.getRoot();
        if (root.getNodeName() == null && root.getNodeType() == HtmlNodeType.ELEMENT) {
            if (root.getSubNodes().size() == 1) {
                document.setRoot(root.getSubNodes().get(0));
            }
        }

        return document;
    }

}
