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
package org.tinygroup.htmlparser;

import org.tinygroup.htmlparser.node.HtmlNode;
import org.tinygroup.parser.Document;

import java.util.List;

public interface HtmlDocument extends Document<HtmlNode> {
    /**
     * 获取HTML声明
     *
     * @return HtmlNode
     */
    HtmlNode getHtmlDeclaration();

    /**
     * 获取CDATA部分
     *
     * @return List<HtmlNode>
     */
    List<HtmlNode> getDoctypeList();

    /**
     * 获取HTML处理指令
     *
     * @return List<HtmlNode>
     */
    List<HtmlNode> getProcessingInstructionList();

    /**
     * 获取Html注释
     *
     * @return List<HtmlNode>
     */
    List<HtmlNode> getCommentList();

    /**
     * 设置HTML声明
     *
     * @param node
     * @return void
     */
    void setHtmlDeclaration(HtmlNode node);

    /**
     * 添加CDATA文本
     *
     * @param node
     * @return void
     */
    void addDoctype(HtmlNode node);

    /**
     * 添加HTML处理指令
     *
     * @param node
     * @return void
     */
    void addProcessingInstruction(HtmlNode node);

    /**
     * 添加注释
     *
     * @param node
     * @return void
     */
    void addComment(HtmlNode node);

}
