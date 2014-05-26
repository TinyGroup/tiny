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
package org.tinygroup.parser;

import org.tinygroup.parser.nodetype.NodeSign;


public interface NodeType {
    /**
     * 是否有头部
     *
     * @return boolean
     */
    boolean isHasHeader();

    /**
     * 是否有子结点
     *
     * @return boolean
     */
    boolean isHasBody();

    /**
     * 是否有文本内容
     *
     * @return boolean
     */
    boolean isHasContent();

    boolean isText();

    /**
     * 获取头标签
     *
     * @return NodeSign
     */
    NodeSign getHead();

    /**
     * 获取结尾标签
     *
     * @return NodeSign
     */
    NodeSign getTail();

    /**
     * 获取头标签 为输入参数添加头标签
     *
     * @param str
     * @return StringBuffer
     */
    void getHeader(StringBuffer sb, String str);

    /**
     * 获取结尾标签 为输入参数添加结尾标签
     *
     * @param str
     * @return StringBuffer
     */
    void getTail(StringBuffer sb, String str);

}