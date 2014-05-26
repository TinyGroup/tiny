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
package org.tinygroup.parser.node;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import org.tinygroup.commons.processor.Processor;
import org.tinygroup.parser.Node;
import org.tinygroup.parser.NodeType;

/**
 *
 * @author luoguo
 */
public abstract class NodeImpl<T extends Node<T>, NT extends NodeType> implements Node<T> {
    private Map<String, String> attributes;
    private List<Attribute> attributeList;
    private String content = null;
    private List<T> subNodes = new ArrayList<T>();
    private T parent = null;
    private NT nodeType = null;
    private String nodeName = null;
    private String singleAttribute;

    public String getAttribute(String attributeName, String defaultValue) {
        String value = this.getAttribute(attributeName);
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }

    public void write(OutputStream stream) throws IOException {
        this.write(stream, "UTF-8");
    }

    /**
     * 获取非对称标签属性
     *
     * @return String
     */
    public String getSingleAttribute() {
        return singleAttribute;
    }

    /**
     * 设置非对称标签属性
     *
     * @param singleAttribute
     * @return void
     */
    public void setSingleAttribute(String singleAttribute) {
        this.singleAttribute = singleAttribute;
    }

    protected abstract String encode(String str);

    protected abstract String decode(String str);

    /**
     * 设置结点名称
     *
     * @param nodeName
     * @return void
     */
    public void setNodeName(String nodeName) {
        this.nodeName = getCaseSensitiveName(nodeName);
    }

    /**
     * 构造函数
     *
     * @param nodeType
     */
    protected NodeImpl(NT nodeType) {
        this.nodeType = nodeType;
    }

    /**
     * 构造函数
     *
     * @param nodeName
     */
    protected NodeImpl(String nodeName) {
        this.nodeName = getCaseSensitiveName(nodeName);
    }

    /**
     * 构造函数
     *
     * @param nodeType
     * @param nodeName
     */
    protected NodeImpl(NT nodeType, String nodeName) {
        this(nodeType);
        if (nodeType.isHasHeader()) {
            this.nodeName = getCaseSensitiveName(nodeName);
        }
    }

    /**
     * 构造函数
     *
     * @param nodeName
     * @param nodeType
     */
    protected NodeImpl(String nodeName, NT nodeType) {
        this(nodeType, nodeName);
    }

    /**
     * 设置父结点
     *
     * @param parent
     */
    public void setParent(T parent) {
        this.parent = parent;
    }

    /**
     * 获取结点类型
     *
     * @return NT
     */
    public NT getNodeType() {
        return nodeType;
    }

    /**
     * 获取结点文本内容
     *
     * @return String
     */
    public String getContent() {
        if (nodeType.isHasContent()) {
            return content;
        } else {
            StringBuffer sb = new StringBuffer();
            if (subNodes != null) {
                for (T n : subNodes) {
                    if (n.getNodeType().isHasContent()) {
                        sb.append(n.getContent());
                    }
                }
                return sb.toString();
            } else {
                return null;
            }
        }
    }

    /**
     * 设置结点文本内容
     *
     * @param content
     */
    public void setContent(String content) {
        String contentString = content.trim();
        if (nodeType.isHasContent()) {
            if (nodeType.isText()) {
                this.content = decode(contentString);
            } else {
                this.content = contentString;
            }
        } else {
            for(Node node:subNodes){
                if(node.getNodeType().isHasContent()){
                    node.setContent(content);
                    return;
                }
            }
            addContent(contentString);
        }
    }

    private String getEncodeContent() {
        if (nodeType.isText()) {
            return encode(content);
        } else {
            return content;
        }

    }

    /**
     * 获取结点属性
     *
     * @return Map<String,String>
     */
    public Map<String, String> getAttributes() {
        return attributes;
    }

    public List<Attribute> getAttributeList() {
        return attributeList;
    }

    public void setAttribute(Map<String, String> attributeMap) {
        if (attributeMap != null) {
            for (String name : attributeMap.keySet()) {
                String value = attributeMap.get(name);
                name = getCaseSensitiveName(name);
                setAttribute(name, value);
            }
        }
    }

    /**
     * 获取子结点
     *
     * @return List<T>
     */
    public List<T> getSubNodes() {
        return subNodes;
    }

    /**
     * 获取子结点
     */
    public List<T> getSubNodes(String nodeName) {
        if (subNodes == null) {
            return null;
        }
        nodeName = getCaseSensitiveName(nodeName);
        List<T> result = new ArrayList<T>();
        for (T t : subNodes) {
            if (t.getNodeName() != null && t.getNodeName().equals(nodeName)) {
                result.add(t);
            }
        }
        if (result.size() == 0) {
            return null;
        }
        return result;
    }

    /**
     * 获取子孙结点列表
     */
    public List<T> getSubNodesRecursively(String nodeName) {
        if (subNodes == null) {
            return null;
        }
        nodeName = getCaseSensitiveName(nodeName);
        List<T> result = new ArrayList<T>();
        for (T t : subNodes) {
            if (t.getNodeName() != null && t.getNodeName().equals(nodeName)) {
                result.add(t);
            }
            List<T> progenyNodes = t.getSubNodesRecursively(nodeName);
            if (progenyNodes != null && progenyNodes.size() > 0) {
                result.addAll(progenyNodes);
            }
        }
        if (result.size() == 0) {
            return null;
        }
        return result;
    }

    /**
     * 查找子孙节点中，指定节点名称的第一个节点
     *
     * @param nodeName
     * @return
     */
    public T getSubNodeRecursively(String nodeName) {
        if (subNodes == null) {
            return null;
        }
        nodeName = getCaseSensitiveName(nodeName);
        for (T t : subNodes) {
            if (t.getNodeName() != null && t.getNodeName().equals(nodeName)) {
                return t;
            }
            T p = t.getSubNodeRecursively(nodeName);
            if (p != null) {
                return p;
            }
        }
        return null;
    }

    /**
     * 返回名字，如果大小写敏感，则没有变化，如果大小写不敏感，则统一变成小写
     *
     * @param name
     * @return
     */
    public String getCaseSensitiveName(String name) {
        if (!isCaseSensitive()) {
            name = name.toLowerCase();
        }
        return name;
    }

    /**
     * 查找子节点中，指定节点名称的第一个节点
     *
     * @param nodeName
     * @return
     */
    public T getSubNode(String nodeName) {
        if (subNodes == null) {
            return null;
        }
        nodeName = getCaseSensitiveName(nodeName);
        for (T t : subNodes) {
            if (t.getNodeName() != null && t.getNodeName().equals(nodeName)) {
                return t;
            }
        }
        return null;
    }

    /**
     * @return
     */
    private String getFooterBuffer() {
        StringBuffer sb = new StringBuffer();
        if (nodeName != null && nodeType.isHasHeader()) {
            sb.append(nodeName);
        }
        return sb.toString();
    }

    /**
     * 获取头标签内相关内容
     *
     * @return
     */
    private String getHeaderBuffer() {
        StringBuffer sb = new StringBuffer();
        if (nodeName != null) {
            sb.append(nodeName);
        }
        if (attributeList != null) {
            for (Attribute attribute : attributeList) {
                sb.append(" ");
                sb.append(attribute.getName()).append("=").append("\"").append(encode(attribute.getValue())).append("\"");
            }
        }
        if (singleAttribute != null) {
            sb.append(" ").append(singleAttribute);
        }
        return sb.toString();
    }

    /**
     * 获取完整的头标签，包括标签标识符、名字、属性
     */
    public void getHeader(StringBuffer sb) {
        nodeType.getHeader(sb, getHeaderBuffer());
    }

    /**
     * 获取完整的结尾标签 若为非对称标签则返回空
     */
    public void getFooter(StringBuffer sb) {
        if (!isSingleNode()) {
            nodeType.getTail(sb, getFooterBuffer());
        }
    }

    /**
     * 获取根结点
     */
    @SuppressWarnings("unchecked")
    public T getRoot() {
        T n = (T) this;
        while (n.getParent() != null) {
            n = n.getParent();
        }
        return n;
    }

    /**
     * 获取父结点
     */
    public T getParent() {
        return parent;
    }

    /**
     * 获取结点内容 若为文本内容，返回文本 若为子结点，返回子结点
     */

    public StringBuffer getBody() {
        StringBuffer sb = new StringBuffer();
        if (nodeType.isHasContent() && content != null) {
            sb.append(content);
        }
        if (nodeType.isHasBody() && subNodes != null) {
            for (T n : subNodes) {
                sb.append(n.toStringBuffer());
            }
        }
        return sb;
    }

    /**
     * 返回当前结点完整的信息
     */
    public StringBuffer toStringBuffer() {
        StringBuffer sb = new StringBuffer();
        getHeader(sb);
        if (content != null) {
            sb.append(getEncodeContent());
        }
        if (subNodes != null) {
            for (T n : subNodes) {
                sb.append(n.toStringBuffer());
            }
        }
        getFooter(sb);
        return sb;
    }

    /**
     * 将结点数据写入指定的输出流中
     */
    public void write(OutputStream stream, String encode) throws IOException {
        StringBuffer sb = new StringBuffer();
        getHeader(sb);
        stream.write(sb.toString().getBytes(encode));
        if (content != null) {
            stream.write(getEncodeContent().getBytes(encode));
        }
        if (subNodes != null) {
            for (T n : subNodes) {
                n.write(stream);
            }
        }
        sb = new StringBuffer();
        getFooter(sb);
        stream.write(sb.toString().getBytes(encode));
    }

    /**
     * 根据属性名获取属性值
     */
    public String getAttribute(String attributeName) {
        if (attributes == null) {
            return null;
        }
        attributeName = getCaseSensitiveName(attributeName);
        return attributes.get(attributeName);
    }

    /**
     * 根据属性名删除指定的属性
     */
    public void removeAttribute(String attributeName) {
        String name = getCaseSensitiveName(attributeName);
        attributes.remove(name);
        if (attributeList != null) {
            Iterator<Attribute> iterable = attributeList.iterator();
            while (iterable.hasNext()) {
                Attribute attribute = iterable.next();
                if (attribute.getName().equals(name)) {
                    iterable.remove();
                    break;
                }
            }
        }
    }

    /**
     * 设置属性 若输入参数中的属性名不为空，属性值为空，则删除指定的属性 若输入参数中的属性名不为空，属性值不为空，则添加相应的属性
     */
    public void setAttribute(String attributeName, String value) {
        if (nodeType.isHasHeader()) {
            if (attributes == null) {
                attributes = new HashMap<String, String>();
                attributeList = new ArrayList<Attribute>();
            }
            String name = getCaseSensitiveName(attributeName);
            if (attributes.containsKey(name)) {
                for (Attribute attribute : attributeList) {
                    if (name.equals(attribute.getName())) {
                        attribute.setValue(value);
                        break;
                    }
                }
            } else {
                //如果不存在，则新增
                attributeList.add(new Attribute(name, value));
            }
            attributes.put(name, value);
        }
    }

    /**
     * 添加子结点
     */
    @SuppressWarnings("unchecked")
    public T addNode(T node) {
        if (!nodeType.isHasBody() || node == null) {
            return null;
        }
        if (subNodes == null) {
            subNodes = new ArrayList<T>();
        } else {
            if (subNodes.contains(node)) {
                return node;
            }
        }

        node.setParent((T) this);
        subNodes.add(node);
        return node;
    }

    /**
     * 添加所有节点
     *
     * @param nodes
     */
    public List<T> addAll(List<T> nodes) {
        if (!subNodes.equals(nodes) && nodeType.isHasBody() && nodes != null) {
            for (T t : nodes) {
                addNode(t);
            }
        }
        return nodes;
    }

    /**
     * 删除指定的子结点
     */
    public T removeNode(T node) {
        if (subNodes != null && subNodes.remove(node)) {
            node.setParent(null);
            return node;
        }
        return null;
    }

    /**
     * 删除所有子节点
     *
     * @return
     */
    public List<T> removeSubNotes() {
        List<T> r = subNodes;
        this.subNodes = null;
        return r;
    }


    public String toString() {
        return toStringBuffer().toString();
    }

    @SuppressWarnings("unchecked")
    public void foreach(Processor<T> processor) {
        processor.process((T) this);
        if (subNodes != null) {
            for (T n : subNodes) {
                n.foreach(processor);
            }
        }
    }

    /**
     * 获取结点名称
     */
    public String getNodeName() {
        return nodeName;
    }

    /**
     * 检测是否为非对称结点 若为对称结点返回false 若为非对称结点返回true
     */
    public boolean isSingleNode() {
        return false;
    }

    public List<T> removeNode(String nodeName) {
        nodeName = getCaseSensitiveName(nodeName);
        List<T> removedNodeList = new ArrayList<T>();
        if (subNodes != null) {
            for (int i = subNodes.size() - 1; i >= 0; i--) {
                T t = subNodes.get(i);
                if (t.getNodeName() != null && t.getNodeName().equals(nodeName)) {
                    removedNodeList.add(t);
                    subNodes.remove(i);
                }
            }
        }
        return removedNodeList;
    }
}
