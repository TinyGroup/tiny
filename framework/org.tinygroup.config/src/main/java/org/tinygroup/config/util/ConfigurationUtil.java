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
package org.tinygroup.config.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.config.ConfigurationManager;
import org.tinygroup.config.impl.ConfigurationManagerImpl;
import org.tinygroup.parser.filter.NameFilter;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * 应用配置工具类，用于把父对象中的配置参数应用到子对象中。
 *
 * @author luoguo
 */
public final class ConfigurationUtil {
    private static ConfigurationManager configurationManager = new ConfigurationManagerImpl();

    private ConfigurationUtil() {
    }

    public static ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }

    /**
     * 获取属性值，应用配置的优先级更高
     *
     * @param applicationNode
     * @param componentNode
     * @param attributeName
     * @return
     */
    public static String getPropertyName(XmlNode applicationNode, XmlNode componentNode,
                                         String attributeName) {
        String value = null;
        checkNodeName(applicationNode, componentNode);
        if (applicationNode != null) {
            value = applicationNode.getAttribute(attributeName);
        }
        if (value == null && componentNode != null) {
            value = componentNode.getAttribute(attributeName);
        }
        return value;
    }

    /**
     * 获取属性值，应用配置的优先级更高。<br>
     * 如果读取的结果为Null或为""，则返回默认值
     *
     * @param applicationNode
     * @param componentNode
     * @param attributeName
     * @param defaultValue
     * @return
     */
    public static String getPropertyName(XmlNode applicationNode, XmlNode componentNode,
                                         String attributeName, String defaultValue) {
        String value = getPropertyName(applicationNode, componentNode, attributeName);
        if (value == null || value.trim().length() == 0) {
            value = defaultValue;
        }
        return value;
    }

    private static void checkNodeName(XmlNode applicationNode, XmlNode componentNode) {
        if (applicationNode == null || componentNode == null) {// 如果有一个为空，则返回
            return;
        }
        String applicationNodeName = applicationNode.getNodeName();
        String componentNodeName = componentNode.getNodeName();
        if (applicationNodeName != null && componentNodeName != null
                && !applicationNodeName.equals(componentNodeName)) {
            throw new RuntimeException(
                    applicationNodeName + "与" + componentNodeName + "两个节点名称不一致！");
        }
    }

    /**
     * 根据关键属性进行子节点合并
     *
     * @param applicationNode
     * @param componentNode
     * @param keyPropertyName
     * @return
     */
    public static List<XmlNode> combineSubList(XmlNode applicationNode, XmlNode componentNode,
                                               String nodeName, String keyPropertyName) {
        checkNodeName(applicationNode, componentNode);
        List<XmlNode> result = new ArrayList<XmlNode>();
        if (applicationNode == null && componentNode == null) {
            return result;
        }
        List<XmlNode> applicationNodeList = null;
        if (applicationNode != null) {
            applicationNodeList = applicationNode.getSubNodes(nodeName);
        }
        List<XmlNode> componentNodeList = null;
        if (componentNode != null) {
            componentNodeList=componentNode.getSubNodes(nodeName);
        }
        if (componentNodeList == null && applicationNodeList == null) {
            return result;
        }
        if (componentNodeList == null || componentNodeList.size() == 0) {// 如果组件配置为空
            result.addAll(applicationNode.getSubNodes(nodeName));
            return result;
        }
        if (applicationNodeList == null || applicationNodeList.size() == 0) {// 如果应用配置为空
            result.addAll(componentNode.getSubNodes(nodeName));
            return result;
        }
        Map<String, XmlNode> appConfigMap = nodeListToMap(applicationNodeList, keyPropertyName);
        Map<String, XmlNode> compConfigMap = nodeListToMap(componentNodeList, keyPropertyName);
        for (String key : appConfigMap.keySet()) {
            XmlNode compNode = compConfigMap.get(key);
            XmlNode appNode = appConfigMap.get(key);
            if (compNode == null) {
                result.add(appNode);
            } else {// 如果两个都有，则合并之
                result.add(combine(appNode, compNode));
            }
        }
        for (String key : compConfigMap.keySet()) {
            XmlNode appNode = compConfigMap.get(key);
            if (appNode == null) {
                result.add(appNode);
            }
        }
        return result;
    }

    /**
     * 合并单个节点
     *
     * @param applicationNode
     * @param componentNode
     * @return
     */
    public static XmlNode combineXmlNode(XmlNode applicationNode, XmlNode componentNode) {
        checkNodeName(applicationNode, componentNode);
        if (applicationNode == null && componentNode == null) {
            return null;
        }
        XmlNode result = null;
        if (applicationNode != null && componentNode == null) {
            result = applicationNode;
        } else if (applicationNode == null && componentNode != null) {
            result = componentNode;
        } else {
            result = combine(applicationNode, componentNode);
        }
        return result;
    }

    private static XmlNode combine(XmlNode appNode, XmlNode compNode) {
        XmlNode result = new XmlNode(appNode.getNodeName());
        result.setAttribute(compNode.getAttributes());
        result.setAttribute(appNode.getAttributes());
        if (!CollectionUtil.isEmpty(compNode.getSubNodes())) {
            result.addAll(compNode.getSubNodes());
        }
        if (!CollectionUtil.isEmpty(appNode.getSubNodes())) {
            result.addAll(appNode.getSubNodes());
        }
        return result;
    }

    private static Map<String, XmlNode> nodeListToMap(List<XmlNode> subNodes,
                                                      String keyPropertyName) {
        Map<String, XmlNode> nodeMap = new HashMap<String, XmlNode>();
        for (XmlNode node : subNodes) {
            String value = node.getAttribute(keyPropertyName);
            nodeMap.put(value, node);
        }
        return nodeMap;
    }

    /**
     * 简单合并
     *
     * @param applicationNode
     * @param componentNode
     * @return
     */
    public static List<XmlNode> combineSubList(XmlNode applicationNode, XmlNode componentNode) {

        checkNodeName(applicationNode, componentNode);
        List<XmlNode> result = new ArrayList<XmlNode>();
        if (applicationNode == null && componentNode == null) {
            return result;
        }
        if (componentNode != null && componentNode.getSubNodes() != null) {
            result.addAll(componentNode.getSubNodes());
        }
        if (applicationNode != null && applicationNode.getSubNodes() != null) {
            result.addAll(applicationNode.getSubNodes());
        }
        return result;
    }


    /**
     * 简单合并
     *
     * @param nodeName
     * @param applicationNode
     * @param componentNode
     * @return
     */
    public static List<XmlNode> combineSubList(String nodeName, XmlNode applicationNode,
                                               XmlNode componentNode) {
        checkNodeName(applicationNode, componentNode);
        List<XmlNode> result = new ArrayList<XmlNode>();
        if (applicationNode == null && componentNode == null) {
            return result;
        }
        if (componentNode != null && componentNode.getSubNodes(nodeName) != null) {
            result.addAll(componentNode.getSubNodes(nodeName));
        }
        if (applicationNode != null && applicationNode.getSubNodes(nodeName) != null) {
            result.addAll(applicationNode.getSubNodes(nodeName));
        }
        return result;
    }

    /**
     * 简单合并
     *
     * @param nodeName
     * @param applicationNode
     * @param componentNode
     * @return
     */
    public static List<XmlNode> combineFindNodeList(String nodeName, XmlNode applicationNode,
                                                    XmlNode componentNode) {
        checkNodeName(applicationNode, componentNode);
        List<XmlNode> result = new ArrayList<XmlNode>();
        if (applicationNode == null && componentNode == null) {
            return result;
        }
        if (componentNode != null) {
            NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(componentNode);
            List<XmlNode> nodes = nameFilter.findNodeList(nodeName);
            if (nodes != null) {
                result.addAll(nodes);
            }
        }
        if (applicationNode != null) {
            NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(applicationNode);
            List<XmlNode> nodes = nameFilter.findNodeList(nodeName);
            if (nodes != null) {
                result.addAll(nodes);
            }
        }
        return result;
    }
}
