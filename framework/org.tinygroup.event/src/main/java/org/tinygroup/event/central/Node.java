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
package org.tinygroup.event.central;

import org.tinygroup.event.ServiceInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务节点
 *
 * @author luoguo
 */
public final class Node implements Comparable<Node>, Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -830350685731524644L;
    // IP地址
    private String ip;
    // 服务端口
    private String port;
    /**
     * 节点名
     */
    private String nodeName;
    /**
     * 分配权重，当一个服务被发现在若干个节点下都提供时，具体调用时，根据权重进行随机分配
     */
    private int weight;

    private String type;

    public static final String CEP_NODE = "cepnode";
    public static final String CENTRAL_NODE = "centralnode";
    public static final int DEFAULT_WEIGHT = 10;

    private List<ServiceInfo> serviceInfos;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ServiceInfo> getServiceInfos() {
        if (serviceInfos == null) {
            serviceInfos = new ArrayList<ServiceInfo>();
        }
        return serviceInfos;
    }

    public void setServiceInfos(List<ServiceInfo> serviceInfos) {
        this.serviceInfos = serviceInfos;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public Node() {
    }

    public Node(String ip, String port, String nodeName, int weight) {
        this.port = port;
        this.ip = ip;
        this.nodeName = nodeName;
        this.weight = weight;
    }

    public Node(String nodeName, int weight) {
        this.nodeName = nodeName;
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    /**
     * 比较节点名称
     */
    public int compareTo(Node node) {
        return nodeName.compareTo(node.nodeName);
    }

    /**
     * 比较节点名称
     */
    public boolean equals(Object object) {
        if (object instanceof Node) {
            Node n = (Node) object;
            return nodeName.equals(n.nodeName);
        }
        return false;
    }

    public int hashCode() {
        return nodeName.hashCode();
    }

    public String toString() {
        return String.format("%s:%s:%s", ip, port, nodeName);
    }

}
