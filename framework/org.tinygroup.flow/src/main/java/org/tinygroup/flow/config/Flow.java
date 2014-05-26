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
package org.tinygroup.flow.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.event.Parameter;
import org.tinygroup.flow.FlowExecutor;
import org.tinygroup.flow.exception.FlowRuntimeException;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 流程，如果节点的名称叫exception，则表示是整个流程的异常处理节点，里面只能添加异常类的nextNode
 * 在执行时，如果节点没有处理，则异常会由本部分进行处理，本部分没有处理，则由异常管理器进行处理。
 *
 * @author luoguo
 */

@XStreamAlias("flow")
public class Flow {
    /**
     *
     */
    private static final long serialVersionUID = -7228372373320970405L;
    @XStreamAsAttribute
    @XStreamAlias("extend-flow-id")
    private String extendFlowId;// 继承的flowID
    @XStreamAsAttribute
    private String id;// flow的唯一ID
    @XStreamAsAttribute
    private String name;// flow的名字
    @XStreamAsAttribute
    private String title;// 流程名称
    private String description;// 流程说明
    private List<Node> nodes;// 流程节点
    private transient Map<String, Node> nodeMap;
    private transient FlowExecutor flowExecutor;
    private transient boolean assembled = false;// 是否已经组装完毕
    @XStreamAsAttribute
    @XStreamAlias("private-context")
    private boolean privateContext = false;
    @XStreamAsAttribute
    private boolean enable;// 是否可用

    @XStreamAlias("parameters")
    private List<Parameter> parameters;// 流程的参数


    public List<Parameter> getInputParameters() {
        if (parameters == null) {
            return null;
        }
        List<Parameter> result = new ArrayList<Parameter>();
        for (Parameter parameter : parameters) {
            if (parameter.getScope() == null
                    || parameter.getScope().equalsIgnoreCase(Parameter.BOTH)
                    || parameter.getScope().equalsIgnoreCase(Parameter.INPUT)) {
                result.add(parameter);
            }
        }
        return result;
    }

    public List<Parameter> getOutputParameters() {
        if (parameters == null) {
            return null;
        }
        List<Parameter> result = new ArrayList<Parameter>();
        for (Parameter parameter : parameters) {
            if (parameter.getScope() == null
                    || parameter.getScope().equalsIgnoreCase(Parameter.BOTH)
                    || parameter.getScope().equalsIgnoreCase(Parameter.OUTPUT)) {
                result.add(parameter);
            }
        }
        return result;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }


    public boolean isPrivateContext() {
        return privateContext;
    }

    public void setPrivateContext(boolean privateContext) {
        this.privateContext = privateContext;
    }

    public void assemble() {
        if (assembled) {
            return;
        }
        if (extendFlowId != null) {
            Flow parentFlow = flowExecutor.getFlowIdMap().get(extendFlowId);
            if (parentFlow == null) {
                throw new FlowRuntimeException("flow.flowNotExist", parentFlow);
            } else {
                parentFlow.assemble();
                copyFlow(parentFlow);
            }
        }
        assembled = true;
    }

    private void copyFlow(Flow parentFlow) {
        List<Node> tmpNodes = nodes;
        nodes = new ArrayList<Node>();
        nodes.addAll(parentFlow.getNodes());// 首先把父亲所有节点复制过来
        for (Node node : tmpNodes) {
            Node parentNode = parentFlow.getNodeMap().get(node.getId());
            if (parentNode == null) {
                // 如果父节点在子节点中不存在，则直接拿过来
                getNodeMap().put(node.getId(), node);
                nodes.add(node);
            } else {
                // 否则进行合并
                int index = nodes.indexOf(parentNode);
                nodes.remove(index);
                node.combine(parentNode);
                nodes.add(index, node);
            }
        }
    }

    public FlowExecutor getFlowExecutor() {
        return flowExecutor;
    }

    public void setFlowExecutor(FlowExecutor flowExecutor) {
        this.flowExecutor = flowExecutor;
    }

    public String getExtendFlowId() {
        return extendFlowId;
    }

    public void setExtendFlowId(String extendFlowId) {
        this.extendFlowId = extendFlowId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Node> getNodeMap() {
        if (nodeMap == null) {
            nodeMap = new HashMap<String, Node>();
            if (nodes != null) {
                for (Node node : nodes) {
                    nodeMap.put(node.getId(), node);
                }
            }
        }
        return nodeMap;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getServiceName() {
        return id;
    }


    public List<Parameter> getParameters() {
        List<Parameter> p = getInputParameters();
        if (p != null) {
            return p;
        }
        return new ArrayList<Parameter>();
    }

}
