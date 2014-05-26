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
package org.tinygroup.parser.filter;

import org.tinygroup.parser.Node;
import org.tinygroup.parser.NodeFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractFilterImpl<T extends Node<T>> implements NodeFilter<T> {
    private Map<String, String> includeAttributeWithValue = null; // 包含属性名及值
    private Map<String, String> excludeAttributeWithValue = null; // 包含属性名及值
    private String[] includeAttribute = null;// 仅包含属性名
    private String[] excludeAttribute = null;// 仅包含属性名
    private String[] includeText = null;// 包含文本
    private String[] excludeText = null;// 不包含文本
    private String[] includeNode = null;// 包含子节点
    private String[] excludeNode = null;// 不包含子节点
    private String[] includeByNode = null;// 祖先节点名称列表必须包含该数组中的元素
    private String[] excludeByNode = null;// 祖先节点名称列表不能包含该数组中的元素
    private String[] xorSubNode = null;// 至少包含一个指定名称的节点
    private String[] xorProperties = null;// 至少包含一个指定名称属性

    private T node = null;
    private String nodeName;

    public T getNode() {
        return node;
    }

    class NodeList implements Comparable<NodeList> {
        private boolean caseSensitive;

        public NodeList(boolean caseSensitive) {
            this.caseSensitive = caseSensitive;
        }

        private String nodeName;
        private List<T> nodeList = new ArrayList<T>();

        public List<T> getNodeList() {
            return nodeList;
        }

        NodeList(String nodeName) {
            this.nodeName = nodeName;
        }

        public int compareTo(NodeList o) {
            if (!caseSensitive) {
                return nodeName.toLowerCase().compareTo(o.nodeName.toLowerCase());
            }
            return nodeName.compareTo(o.nodeName);
        }
    }

    public void init(T node) {
        this.node = node;
    }

    public AbstractFilterImpl() {
    }

    public AbstractFilterImpl(T node) {
        init(node);
    }

    public NodeFilter setXorSubNode(String... xorSubNode) {
        this.xorSubNode = xorSubNode;
        return this;
    }

    public NodeFilter setXorProperties(String... xorProperties) {
        this.xorProperties = xorProperties;
        return this;
    }

    protected boolean isRightNode(T node) {
        if (xorSubNode != null) {
            if (!checkXorSubNode(node)) {
                return false;
            }
        }
        if (xorProperties != null) {
            if (!checkXorProperties(node)) {
                return false;
            }
        }
        if (includeText != null) {
            String content = node.getContent();
            if (content == null) {
                return false;
            }
            for (String str : includeText) {
                if (content.indexOf(str) < 0) {
                    return false;
                }
            }
        }
        if (excludeText != null) {
            String content = node.getContent();
            if (content != null) {
                for (String str : excludeText) {
                    if (content.indexOf(str) >= 0) {
                        return false;
                    }
                }
            }
        }
        if (includeAttributeWithValue != null) {// 所有的属性都要被包含
            for (String key : includeAttributeWithValue.keySet()) {
                String value = node.getAttribute(key);
                if (value == null || !value.equals(includeAttributeWithValue.get(key))) {
                    return false;
                }
            }
        }
        if (excludeAttributeWithValue != null) {// 包含了不应有的属性
            for (String key : excludeAttributeWithValue.keySet()) {
                String value = node.getAttribute(key);
                if (value != null && value.equals(excludeAttributeWithValue.get(key))) {
                    return false;
                }
            }
        }
        if (includeAttribute != null) {// 没有包含应有的属性名
            for (String key : includeAttribute) {
                if (node.getAttribute(key) == null) {
                    return false;
                }
            }
        }
        if (excludeAttribute != null) {// 包含了不应有的属性名
            for (String key : excludeAttribute) {
                if (node.getAttribute(key) != null) {
                    return false;
                }
            }
        }
        if (includeByNode != null) {
            List<String> ancNodeNames = new ArrayList<String>();
            ancestorNodeNames(node, ancNodeNames);// 将所有祖先节点名字放入list
            if (ancNodeNames.size() == 0) {
                return false;
            }
            for (String inByName : includeByNode) {
                if (!ancNodeNames.contains(inByName)) {
                    return false;
                }
            }
        }
        if (excludeByNode != null) {
            List<String> ancNodeNames = new ArrayList<String>();
            ancestorNodeNames(node, ancNodeNames);// 将所有祖先节点名字放入list
            if (ancNodeNames.size() != 0) {
                for (String inByName : excludeByNode) {
                    if (ancNodeNames.contains(inByName)) {
                        return false;
                    }
                }
            }
        }
        if (includeNode != null) {
            List<T> sn = node.getSubNodes();
            if (sn == null || sn.size() == 0) {
                return false;
            }
            for (String inName : includeNode) {
                if (!existNode(sn, inName)) {
                    return false;// 没有包含应有的结点
                }
            }
        }
        if (excludeNode != null) {
            List<T> sn = node.getSubNodes();
            if (sn != null && sn.size() > 0) {
                for (String inName : excludeNode) {
                    if (existNode(sn, inName)) {
                        return false;// 包含了禁止的结点
                    }
                }
            }
        }
        return true;
    }

    private boolean checkXorProperties(T node) {
        boolean r = false;
        for (String nodeName : xorProperties) {
            if (node.getNodeType().isHasHeader() && node.getAttribute(nodeName) != null) {
                r = true;
                break;
            }
        }
        return r;
    }

    private boolean checkXorSubNode(T node) {
        boolean r = false;
        for (String nodeName : xorSubNode) {
            if (nodeName.trim().length() == 0) {
                continue;
            }
            if (node.getSubNodes(nodeName) != null) {
                r = true;
                break;
            }
        }
        return r;
    }

    /**
     * 判断节点列表是否存在对应名称的节点
     *
     * @param nodes
     * @param nodeName :查询的节点名称
     * @return
     */
    private boolean existNode(List<T> nodes, String nodeName) {
        for (T node : nodes) {
            if (nodeName.equals(node.getNodeName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 节点的所有祖先节点名称
     *
     * @param node
     * @param list
     */
    private void ancestorNodeNames(T node, List<String> list) {
        T parent = node.getParent();
        if (parent != null) {
            if (parent.getNodeName() != null) {
                list.add(parent.getNodeName());
            }
            ancestorNodeNames(parent, list);
        }
    }

    /**
     * 过滤不符合条件的结点
     *
     * @param nodeWithPath
     * @return List<T>
     */
    protected List<T> filteNode(List<T> nodeWithPath) {
        List<T> result = new ArrayList<T>();
        for (T tNode : nodeWithPath) {
            if (isRightNode(tNode)) {
                result.add(tNode);
            }
        }
        return result;
    }

    public NodeFilter setIncludeAttribute(Map<String, String> includeAttribute) {
        if (this.includeAttributeWithValue == null) {
            this.includeAttributeWithValue = includeAttribute;
        } else {
            this.includeAttributeWithValue.putAll(includeAttribute);
        }
        return this;
    }

    public NodeFilter setIncludeAttribute(String key, String value) {
        if (includeAttributeWithValue == null) {
            includeAttributeWithValue = new HashMap<String, String>();
        }
        includeAttributeWithValue.put(key, value);
        return this;
    }

    public NodeFilter setExcludeAttribute(Map<String, String> excludeAttribute) {
        if (this.excludeAttributeWithValue == null) {
            this.excludeAttributeWithValue = excludeAttribute;
        } else {
            this.excludeAttributeWithValue.putAll(excludeAttribute);
        }
        return this;
    }

    public NodeFilter setIncludeText(String... includeText) {
        this.includeText = includeText;
        return this;
    }

    public NodeFilter setExcludeText(String... excludeText) {
        this.excludeText = excludeText;
        return this;
    }

    public NodeFilter setIncludeNode(String... includeNode) {
        this.includeNode = includeNode;
        return this;
    }

    public NodeFilter setExcludeNode(String... excludeNode) {
        this.excludeNode = excludeNode;
        return this;
    }

    public NodeFilter setIncludeByNode(String... includeByNode) {
        this.includeByNode = includeByNode;
        return this;
    }

    public NodeFilter setExcludeByNode(String... excludeByNode) {
        this.excludeByNode = excludeByNode;
        return this;
    }

    public NodeFilter clearCondition() {
        includeAttributeWithValue = null;
        excludeAttributeWithValue = null;
        includeText = null;
        excludeText = null;
        includeNode = null;
        excludeNode = null;
        includeAttribute = null;
        excludeAttribute = null;
        xorSubNode = null;
        xorProperties = null;
        return this;
    }

    public NodeFilter setIncludeAttributes(String... includeAttribute) {
        this.includeAttribute = includeAttribute;
        return this;
    }

    public NodeFilter setExcludeAttribute(String... excludeAttribute) {
        this.excludeAttribute = excludeAttribute;
        return this;
    }

    public T findNode(String nodeName) {
        List<T> result = findNodeList(nodeName);
        if (result.size() == 0) {
            return null;
        } else {
            return result.get(0);
        }
    }

    public NodeFilter setNodeName(String nodeName) {
        this.nodeName = nodeName;
        return this;
    }

    public T findNode() {
        return findNode(nodeName);
    }

    public List<T> findNodeList() {
        return findNodeList(nodeName);
    }

}
