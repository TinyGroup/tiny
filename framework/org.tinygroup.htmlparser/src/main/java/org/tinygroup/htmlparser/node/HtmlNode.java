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
package org.tinygroup.htmlparser.node;

import org.tinygroup.htmlparser.HtmlNodeType;
import org.tinygroup.parser.node.NodeImpl;

import java.util.HashSet;

/**
 *
 * @author luoguo
 */
public class HtmlNode extends NodeImpl<HtmlNode, HtmlNodeType> {
    public static HashSet<String> singleNodeNames = new HashSet<String>();

    static {
        singleNodeNames.add("br");
        singleNodeNames.add("hr");
        singleNodeNames.add("img");
        singleNodeNames.add("input");
        singleNodeNames.add("param");
        singleNodeNames.add("meta");
        singleNodeNames.add("link");
    }

    /**
     * 构造方法
     *
     * @param nodeType
     */
    public HtmlNode(HtmlNodeType nodeType) {
        super(nodeType);
    }

    public HtmlNode getRoot() {
        HtmlNode root = super.getRoot();
        if (root.getNodeName() == null && root.getSubNodes().size() == 1) {
            return root.getSubNodes().get(0);
        }
        return root;
    }

    /**
     * 构造方法
     *
     * @param nodeName
     */
    public HtmlNode(String nodeName) {
        super(nodeName, HtmlNodeType.ELEMENT);
    }

    public boolean isSingleNode() {
        if (getNodeName() != null) {

        }
        return false;
    }

    /**
     * 构造方法
     *
     * @param nodeType
     * @param nodeName
     */
    public HtmlNode(HtmlNodeType nodeType, String nodeName) {
        super(nodeType, nodeName);
    }

    /**
     * 构造方法
     *
     * @param nodeName
     * @param nodeType
     */
    public HtmlNode(String nodeName, HtmlNodeType nodeType) {
        super(nodeType, nodeName);
    }

    public void addContent(String content) {
        HtmlNode node = new HtmlNode(HtmlNodeType.TEXT);
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

    void getPureText(HtmlNode node, StringBuffer sb) {
        if (node.getNodeType() == HtmlNodeType.CDATA || node.getNodeType() == HtmlNodeType.TEXT) {
            String content = node.getContent();
            if (content != null) {
                sb.append(content).append(" ");
            }
        } else {
            if (node.getNodeType().isHasHeader() && node.getSubNodes() != null) {
                for (HtmlNode n : node.getSubNodes()) {
                    getPureText(n, sb);
                }
            }
        }
    }

    public boolean isCaseSensitive() {
        return false;
    }

}
