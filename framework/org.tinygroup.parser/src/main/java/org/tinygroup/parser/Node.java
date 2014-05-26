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

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.tinygroup.commons.processor.ForEachProcessor;
import org.tinygroup.parser.node.Attribute;

/**
 * 节点接口
 *
 * @author luoguo
 */
public interface Node<T extends Node<T>> extends ForEachProcessor<T> {
    /**
     * 获取结点头标签相关内容
     *
     * @return StringBuffer
     */
    void getHeader(StringBuffer sb);

    /**
     * 返回子节点
     *
     * @param name
     * @return
     */
    List<T> getSubNodes(String name);

    /**
     * 返回子孙节点列表
     *
     * @param name
     * @return
     */
    List<T> getSubNodesRecursively(String name);

    /**
     * 返回子孙节点
     *
     * @param name
     * @return
     */
    T getSubNodeRecursively(String name);

    /**
     * 添加内容节点
     *
     * @param content
     */
    void addContent(String content);

    /**
     * 设置结点名称
     *
     * @param name
     */
    void setNodeName(String name);

    /**
     * 获取结尾标签
     *
     * @return StringBuffer
     */
    void getFooter(StringBuffer sb);

    /**
     * 获取根结点
     *
     * @return T
     */
    T getRoot();

    /**
     * 设置父亲节点
     *
     * @param parent
     */
    void setParent(T parent);

    /**
     * 返回节点名称
     *
     * @return
     */
    String getNodeName();

    /**
     * 返回父亲节点
     *
     * @return
     */
    T getParent();

    /**
     * 返回中间内容
     *
     * @return
     */
    StringBuffer getBody();

    /**
     * 写出数据
     *
     * @param stream
     * @throws IOException
     */
    void write(OutputStream stream) throws IOException;

    /**
     * 返回节点类型
     *
     * @return
     */
    NodeType getNodeType();

    /**
     * 返回属性
     *
     * @param attributeName
     * @return
     */
    String getAttribute(String attributeName);

    /**
     * 删除属性
     *
     * @param attributeName
     */
    void removeAttribute(String attributeName);

    /**
     * 设置属性值
     *
     * @param attributeName
     * @param value
     */
    void setAttribute(String attributeName, String value);

    /**
     * 匹量设置属性
     *
     * @param attributeMap
     */
    void setAttribute(Map<String, String> attributeMap);

    /**
     * 获取属性值，如果值不存在，则返回默认值
     *
     * @param attributeName
     * @param defaultValue
     */
    String getAttribute(String attributeName, String defaultValue);

    /**
     * 添加节点
     *
     * @param node 要增加的节点
     * @return 如果增加成功，则返回node节点，否则返回null
     */
    T addNode(T node);

    /**
     * 删除节点
     *
     * @param node
     * @return 删除的节点，如果当前节点中不包含node节点，则返回null
     */
    T removeNode(T node);

    /**
     * 删除指定节点
     *
     * @param nodeName
     * @return
     */
    List<T> removeNode(String nodeName);

    /**
     * 获取内容
     *
     * @return
     */
    String getContent();

    /**
     * 变成StreamBuffer
     *
     * @return
     */
    StringBuffer toStringBuffer();

    /**
     * 设置内容
     *
     * @param content
     */
    void setContent(String content);

    /**
     * 返回属属性
     *
     * @return
     */
    Map<String, String> getAttributes();

    List<Attribute> getAttributeList();

    /**
     * 返回子节点
     *
     * @return
     */
    List<T> getSubNodes();

    /**
     * 是否单节点
     *
     * @return
     */
    boolean isSingleNode();

    /**
     * 是否大小写敏感
     *
     * @return
     */
    boolean isCaseSensitive();

    /**
     * 根据大小写相关返回名字
     *
     * @param name
     * @return
     */
    String getCaseSensitiveName(String name);

    /**
     * 返回纯文本内容
     *
     * @return
     */
    String getPureText();
}
