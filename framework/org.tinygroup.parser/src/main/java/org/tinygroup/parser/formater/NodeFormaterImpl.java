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
package org.tinygroup.parser.formater;

import org.tinygroup.parser.Document;
import org.tinygroup.parser.Node;
import org.tinygroup.parser.NodeFormater;

import java.io.IOException;
import java.io.OutputStream;

public abstract class NodeFormaterImpl<T extends Document<N>, N extends Node<N>> implements NodeFormater<N, T> {
    private String indentString = "  ";
    static final String NEWLINE = "\r\n";
    private String encode = "UTF-8";

    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    /**
     * 获取缩进字符
     *
     * @return String
     */
    public String getIndentString() {
        return indentString;
    }

    /**
     * 获取指定数量的缩进字符
     *
     * @param tab
     * @return String
     */
    public String getIndentString(int tab) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < tab; i++) {
            sb.append(indentString);
        }
        return sb.toString();
    }

    /**
     * 设置缩进字符
     *
     * @param indentString
     * @return void
     */
    public void setIndentString(String indentString) {
        if (indentString != null) {
            this.indentString = indentString;
        }
    }

    protected abstract StringBuffer formatDocumentSelf(T doc);

    protected abstract void formatDocumentSelf(T doc, OutputStream out) throws IOException;

    protected void formatNode(StringBuffer sb, N node, int tab) {
        String is = getIndentString(tab);
        sb.append(is);
        node.getHeader(sb);
        if (node.getNodeType().isHasContent() && node.getContent() != null) {
            sb.append(node.getContent());
        } else {
            if (node.getSubNodes() != null) {
                if (node.getSubNodes().size() == 1 && node.getSubNodes().get(0).getNodeType().isHasContent()) {
                    sb.append(node.getSubNodes().get(0).getContent().trim());
                } else {
                    if (node.getNodeName() != null) {
                        sb.append(NEWLINE);
                    }
                    for (N n : node.getSubNodes()) {
                        formatNode(sb, n, node.getNodeName() != null ? tab + 1 : tab);
                    }
                    sb.append(is);
                }
            }
        }
        node.getFooter(sb);
        if (node.getNodeName() != null || node.getNodeType().getHead().getStart() != null) {
            sb.append(NEWLINE);
        }
    }

    protected void formatNode(N node, OutputStream out, int tab) throws IOException {
        String is = getIndentString(tab);

        out.write(is.getBytes(encode));
        StringBuffer sb = new StringBuffer();
        node.getHeader(sb);
        out.write(sb.toString().getBytes(encode));
        if (node.getNodeType().isHasContent() && node.getContent() != null) {
            out.write(node.getContent().getBytes(encode));
        } else {
            if (node.getSubNodes() != null) {
                if (node.getSubNodes().size() == 1 && node.getSubNodes().get(0).getNodeType().isHasContent()) {
                    out.write(node.getSubNodes().get(0).getContent().trim().getBytes(encode));
                } else {
                    out.write(NEWLINE.getBytes(encode));
                    for (N n : node.getSubNodes()) {
                        formatNode(n, out, tab + 1);
                    }
                    out.write(is.getBytes(encode));
                }
            }
        }
        sb = new StringBuffer();
        node.getFooter(sb);
        out.write(sb.toString().getBytes(encode));
        out.write(NEWLINE.getBytes(encode));
    }

    /**
     * 格式化结点
     *
     * @param node
     * @return String
     */
    public String format(N node) {
        StringBuffer sb = new StringBuffer();
        formatNode(sb, node, 0);
        return sb.toString();

    }

    /**
     * 格式化文档
     *
     * @param doc
     * @return String
     */
    public String format(T doc) {
        StringBuffer sb = new StringBuffer();
        formatDocumentSelf(doc);
        formatNode(sb, doc.getRoot(), 0);
        return sb.toString();
    }

    /**
     * 格式化结点 并在指定的输出流中输出
     *
     * @param node
     * @param out
     * @return void
     * @throws
     */
    public void format(N node, OutputStream out) throws IOException {
        formatNode(node, out, 0);
    }

    /**
     * 格式化文档 并在指定的输出流中输出
     *
     * @param doc
     * @param out
     * @return void
     * @throws
     */
    public void format(T doc, OutputStream out) throws IOException {
        formatDocumentSelf(doc, out);
        formatNode(doc.getRoot(), out, 0);
    }

}
