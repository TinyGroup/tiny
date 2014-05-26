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
package org.tinygroup.flow.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.tinygroup.commons.match.SimpleTypeMatcher;
import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.context2object.util.Context2ObjectUtil;
import org.tinygroup.event.Parameter;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flow.FlowExecutor;
import org.tinygroup.flow.config.Component;
import org.tinygroup.flow.config.ComponentDefine;
import org.tinygroup.flow.config.ComponentDefines;
import org.tinygroup.flow.config.Flow;
import org.tinygroup.flow.config.FlowProperty;
import org.tinygroup.flow.config.Node;
import org.tinygroup.flow.containers.ComponentContainers;
import org.tinygroup.flow.exception.FlowRuntimeException;
import org.tinygroup.flow.util.FlowElUtil;
import org.tinygroup.format.Formater;
import org.tinygroup.format.impl.ContextFormater;
import org.tinygroup.format.impl.FormaterImpl;
import org.tinygroup.i18n.I18nMessageFactory;
import org.tinygroup.i18n.I18nMessages;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 默认流程执行器
 *
 * @author luoguo
 */
public class FlowExecutorImpl implements FlowExecutor {
    private static final String ENGINE_VERSION = "1.0";
    private static Logger logger = LoggerFactory
            .getLogger(FlowExecutorImpl.class);
    private static Map<String, Class<?>> exceptionMap = new HashMap<String, Class<?>>();
    private static transient Formater formater = new FormaterImpl();
    private Map<String, Flow> flowIdMap = new HashMap<String, Flow>();// 包含了name和id两个，所以通过名字和id都可以访问
    private Map<String, Flow> flowIdVersionMap = new HashMap<String, Flow>();
    private I18nMessages i18nMessages = I18nMessageFactory.getI18nMessages();
    // 组件容器
    private ComponentContainers containers = new ComponentContainers();

    static {
        formater.addFormatProvider("", new ContextFormater());
        formater.addFormatProvider(Parameter.INPUT, new ContextFormater());
        formater.addFormatProvider(Parameter.OUTPUT, new ContextFormater());
        formater.addFormatProvider(Parameter.BOTH, new ContextFormater());
    }

    public Map<String, Flow> getFlowIdMap() {
        return flowIdMap;
    }

    public void execute(String flowId, Context context) {
        execute(flowId, null, context);
    }

    public void execute(String flowId, String nodeId, Context context) {
        String executorNodeId = nodeId;
        if (executorNodeId == null) {
            logger.logMessage(LogLevel.DEBUG, "开始执行流程[flowId: {0}]", flowId,
                    executorNodeId);
        } else {
            logger.logMessage(LogLevel.DEBUG,
                    "开始执行流程[flowId: {0}], nodeId: {1}", flowId, executorNodeId);

        }
        logContext(context);
        Flow flow = getFlowIdMap().get(flowId);
        if (flow == null) {
            logger.log(LogLevel.ERROR, "flow.flowNotExist", flowId);
            throw new FlowRuntimeException("flow.flowNotExist", flowId);
        }

        if (executorNodeId == null) {
            executorNodeId = "begin";
        }
        Node node = flow.getNodeMap().get(executorNodeId);
        if (node == null && flow.getNodes().size() > 0) {
            node = flow.getNodes().get(0);
        }
        if (node == null) {
            logger.log(LogLevel.ERROR, "flow.flowNodeNotExist", flowId,
                    executorNodeId);
            throw new FlowRuntimeException(i18nMessages.getMessage(
                    "flow.flowNodeNotExist", flowId, executorNodeId));
        }

        execute(flow, node, context);
        logContext(context);
        logger.logMessage(LogLevel.DEBUG, "流程[flowId: {0}], nodeId: {1}执行完毕。",
                flowId, executorNodeId);
    }

    private void logContext(Context context) {
        if (logger.isEnabled(LogLevel.DEBUG)) {
            logger.logMessage(LogLevel.DEBUG, "环境内容开始：");
            logItemMap(context.getItemMap());
            logSubContext(context.getSubContextMap());
            logger.logMessage(LogLevel.DEBUG, "环境内容结束");
        }
    }

    private void logSubContext(Map<String, Context> subContextMap) {
        logger.logMessage(LogLevel.DEBUG, "子环境[{0}]的内容开始：");
        if (subContextMap != null) {
            for (String key : subContextMap.keySet()) {
                logContext(subContextMap.get(key));
            }
        }
        logger.logMessage(LogLevel.DEBUG, "子环境[{0}]的内容结束：");
    }

    private void logItemMap(Map<String, Object> itemMap) {
        // for (String key : itemMap.keySet()) {
        // logger.logMessage(LogLevel.DEBUG, "key: {0}, value: {1}",
        // XStreamFactory.getXStream().toXML(itemMap.get(key)));
        // }
    }

    private static Class<?> getExceptionType(String name) {
        Class<?> exceptionType = exceptionMap.get(name);
        if (exceptionType == null) {
            try {
                exceptionType = (Class<?>) Class.forName(name);
                exceptionMap.put(name, exceptionType);
            } catch (ClassNotFoundException e) {
                throw new FlowRuntimeException(e);
            }
        }
        return exceptionType;
    }

    /**
     * 获取一个新环境
     *
     * @param flow
     * @param context
     * @return
     */
    private Context getNewContext(Flow flow, Context context) {
        Context flowContext = null;
        if (context == null) {
            return null;
        }
        flowContext = context.getSubContextMap().get(flow.getId());
        if (flowContext == null) {
            return getNewContext(flow, context.getParent());
        }
        return flowContext;
    }

    public void execute(Flow flow, Node node, Context context) {
        Context flowContext = context;
        try {
            checkInputParameter(flow, context);
            Component component = node.getComponent();
            String nodeId = node.getId(); // 当前节点id
            if (flow != null) {
                if (flow.isPrivateContext()) { // 是否context私有
                    flowContext = getNewContext(flow, context);
                    if (flowContext == null) {
                        flowContext = new ContextImpl();
                        context.putSubContext(flow.getId(), flowContext);
                    }
                }
            }
            if (component != null) {
                ComponentInterface componentInstance = getComponentInstance(component
                        .getName());
                setProperties(node, componentInstance, flowContext);
                if (!nodeId.equals("end")) { // 如果当前节点不是最终节点
                    componentInstance.execute(flowContext);
                }
            }
            if (nodeId != null && !nodeId.equals("end")) {
                // 先直接取，如果取到就执行，如果取不到，则用正则去匹配，效率方面的考虑
                String nextNodeId = node.getNextNodeId(context);
                if (nextNodeId == null) {
                    nextNodeId = node.getDefaultNodeId();
                }
                if (nextNodeId == null) {
                    int index = flow.getNodes().indexOf(node);
                    if (index != flow.getNodes().size() - 1) {
                        nextNodeId = flow.getNodes().get(index + 1).getId();
                    } else {
                        nextNodeId = "end";
                    }
                }
                if (nextNodeId != null) {
                    executeNextNode(flow, flowContext, nextNodeId);
                } else {
                    logger.log(LogLevel.ERROR, "flow.flowNodeNotExist",
                            flow.getId(), node.getId(), nodeId);
                    throw new FlowRuntimeException("flow.flowNodeNotExist",
                            flow.getId(), node.getId(), nodeId);
                }

            }
            checkOutputParameter(flow, flowContext);
        } catch (Exception exception) {
            logger.errorMessage("流程执行异常", exception);
            /**
             * 遍历所有异常节点
             */
            if (exceptionNodeProcess(flow, node, context, flowContext,
                    exception)) {
                return;
            }
            // 如果节点中没有处理掉，则由流程的异常处理节点进行处理
            Node exceptionNode = flow.getNodeMap().get("exception");
            if (exceptionNode != null
                    && exceptionNodeProcess(flow, exceptionNode, context,
                    flowContext, exception)) {
                return;
                // executeNextNode(flow, newContext, exceptionNode.getId());
            }

            // 如果还是没有被处理掉，则交由异常处理流程进行管理
            Flow exceptionFlow = this.getFlow("ExceptionFlow");
            if (exceptionFlow != null) {
                exceptionNode = exceptionFlow.getNodeMap().get("exception");
                if (exceptionNode != null
                        && exceptionNodeProcess(exceptionFlow, exceptionNode,
                        context, flowContext, exception)) {
                    return;
                }
            }
            throw new FlowRuntimeException(exception);
        }
    }

    private void checkInputParameter(Flow flow, Context context) {
        StringBuffer buf = new StringBuffer();
        if (flow.getInputParameters() != null) {
            for (Parameter parameter : flow.getInputParameters()) {
                if (parameter.isRequired()) {// 如果是必须
                    // =============20130619修改begin================
                    // Object value = context.get(parameter.getName());
                    // if (value == null) {//
                    // 如果从上下文直接拿没有拿到，则通过ClassNameObjectGenerator去解析
                    // value = getObjectByGenerator(parameter, context);
                    // if (value != null) {// 如果解析出来不为空，则放入上下文
                    // context.put(parameter.getName(), value);
                    // continue;
                    // }
                    // }
                    Object value = Context2ObjectUtil.getObject(parameter,
                            context);
                    if (value != null) {// 如果解析出来不为空，则放入上下文
                        context.put(parameter.getName(), value);
                        continue;
                    }
                    // =============20130619修改end================
                    if (value == null) {
                        buf.append("参数<");
                        buf.append(parameter.getName());
                        buf.append(">在环境中不存在；");
                    }
                }
            }
            if (buf.length() > 0) {
                // buf.insert(0, "流程<" + flow.getId() + ">需要的参数不足：");
                // throw new FlowRuntimeException(buf.toString());
                throw new FlowRuntimeException("flow.inParamNotExist",
                        flow.getId(), buf.toString());
            }

        }
    }

    // private Object getObjectByGenerator(Parameter parameter, Context context)
    // {
    // String collectionType = parameter.getCollectionType();
    // if (generator == null) {
    // generator = SpringUtil.getBean(
    // GeneratorFileProcessor.CLASSNAME_OBJECT_GENERATOR_BEAN);
    // }
    // if (collectionType != null && !"".equals(collectionType)) {
    // return generator.getObjectCollection(parameter.getName(),
    // collectionType, parameter.getType(), context);
    // } else if (parameter.isArray()) {
    // return generator.getObjectArray(parameter.getName(),
    // parameter.getType(), context);
    // }
    //
    // return generator.getObject(parameter.getName(),parameter.getName(),
    // parameter.getType(),
    // context);
    // }

    private void checkOutputParameter(Flow flow, Context context) {
        StringBuffer buf = new StringBuffer();
        if (flow.getOutputParameters() != null) {
            for (Parameter parameter : flow.getOutputParameters()) {
                if (parameter.isRequired()) {// 如果是必须
                    Object value = context.get(parameter.getName());
                    if (value == null) {
                        buf.append("参数<");
                        buf.append(parameter.getName());
                        buf.append(">在环境中不存在；");
                    }
                }
            }
            if (buf.length() > 0) {
                // buf.insert(0, "流程<" + flow.getId() + ">需要输出的参数不足：");
                // throw new FlowRuntimeException(buf.toString());
                throw new FlowRuntimeException("flow.outParamNotExist",
                        flow.getId(), buf.toString());
            }
        }
    }

    private boolean exceptionNodeProcess(Flow flow, Node node, Context context,
                                         Context newContext, Exception exception) {
        List<String> nextExceptionList = node.getNextExceptionList();
        // 20130524调整为顺序取异常进行匹配
        for (int i = 0; i < nextExceptionList.size(); i++) {
            String exceptionName = nextExceptionList.get(i);
            if (dealException(exception, context, newContext, node, flow,
                    exceptionName)) {
                return true;
            }
            Throwable t = exception.getCause();
            while (t != null) {
                if (dealException(t, context, newContext, node, flow,
                        exceptionName)) {
                    return true;
                }
                t = t.getCause();
            }

        }
        return false;
    }

    private boolean dealException(Throwable exception, Context context,
                                  Context newContext, Node node, Flow flow, String exceptionName) {
        if (getExceptionType(exceptionName).isInstance(exception)) {// 如果异常匹配
            String nextNode = node.getNextExceptionNodeMap().get(exceptionName);
            context.put("exceptionFlow", flow);
            context.put("exceptionNode", node);
            context.put("throwableObject", exception);
            executeNextNode(flow, newContext, nextNode);
            return true;
        }
        return false;
    }

    private void executeNextNode(Flow flow, Context context, String nextNodeId) {
        if (nextNodeId != null && !nextNodeId.equals("end")) {
            int index = nextNodeId.indexOf(':');
            Flow nextFlow = flow;
            Node nextNode = null;
            if (index > 0) {
                String[] str = nextNodeId.split(":");
                nextFlow = flowIdMap.get(str[0]);
                if (str.length == 1) {
                    nextNode = nextFlow.getNodeMap().get(
                            "start");
                } else {
                    nextNode = nextFlow.getNodeMap().get(str[1]);
                }
            } else {
                nextNode = flow.getNodeMap().get(nextNodeId);
            }
            if (nextNode != null) {
                execute(nextFlow, nextNode, context);
            }
        }
    }

    /**
     * 把配置的参数注入进去
     *
     * @param node
     * @param componentInstance
     */
    private void setProperties(Node node, ComponentInterface componentInstance,
                               Context context) {
        Map<String, FlowProperty> properties = node.getComponent()
                .getPropertyMap();
        if (properties != null) {
            for (String name : properties.keySet()) {
                FlowProperty property = properties.get(name);
                String value = property.getValue();
                Object object = null;
                // 如果是el表达式，则通过el表达式处理
                if (FlowProperty.EL_TYPE.equals(property.getType())) {
                    object = FlowElUtil.execute(value, context);
                } else {// 否则采用原有处理方式
                    object = getObject(value, context);
                }

                try {
                    PropertyUtils.setProperty(componentInstance, name, object);
                } catch (Exception e) {
                    throw new FlowRuntimeException(e);
                }
            }
        }
    }

    private Object getObject(String strArgs, Context context) {
        String str = strArgs;
        if (str instanceof String) {
            str = formater.format(context, str);
        }

        // 所有的都不是，说明是对象或表达式,此时返回null
        Object o = null;
        if (str != null) {
            str = str.trim();
            o = SimpleTypeMatcher.matchType(str);
        }
        return o;
    }

    protected Object getObjectByName(String name, Context context) {
        Object object = getObject(name, context);
        if (object == null) {
            int index = name.indexOf('.');
            if (index == -1) {
                object = context.get(name);
            } else {
                String k = name.substring(0, index);
                String p = name.substring(index + 1);
                object = context.get(k);
                if (object != null) {
                    try {
                        object = PropertyUtils.getProperty(object, p);
                    } catch (Exception e) {
                        throw new FlowRuntimeException(e);
                    }
                }
            }
        }
        return object;
    }

    public void assemble() {
        for (Flow flow : flowIdMap.values()) {
            flow.assemble();
        }
    }

    public void addFlow(Flow flow) {
        if (flow.getId() != null && flowIdMap.get(flow.getId()) != null) {
            logger.logMessage(LogLevel.ERROR, "flow:[id:{0}]已经存在！",
                    flow.getId());
        }
        if (flow.getName() != null && flowIdMap.get(flow.getName()) != null) {
            logger.logMessage(LogLevel.ERROR, "flow:[name:{0}]已经存在！",
                    flow.getName());
        }
        if (flow.getId() != null) {
            logger.logMessage(LogLevel.INFO, "添加flow:[id:{0}]",
                    flow.getId());
            flowIdMap.put(flow.getId(), flow);
        }
        if (flow.getName() != null) {
            logger.logMessage(LogLevel.INFO, "添加flow:[Name:{0}]",
                    flow.getName());
            flowIdMap.put(flow.getName(), flow);
        }
        flowIdVersionMap.put(getKey(flow), flow);
        flow.setFlowExecutor(this);

    }

    public void removeFlow(Flow flow) {
        logger.logMessage(LogLevel.INFO, "移除flow:[id:{0}]",
                flow.getId());

        flowIdMap.remove(flow.getId());
        flowIdVersionMap.remove(getKey(flow));
    }

    public void removeFlow(String flowId) {
        Flow flow = getFlow(flowId);
        removeFlow(flow);
    }

    public void removeFlow(String flowId, String version) {
        Flow flow = getFlow(flowId, version);
        removeFlow(flow);
    }

    private String getKey(Flow flow) {
        return flow.getId();
    }


    public String getEngineVersion() {
        return ENGINE_VERSION;
    }

    public void execute(String flowId, String nodeId, String version,
                        Context context) {
        Flow flow = flowIdVersionMap.get(flowId);
        if (nodeId == null) {
            nodeId = "begin";
        }
        if (flow != null) {
            Node node = flow.getNodeMap().get(nodeId);
            execute(flow, node, context);
        }
    }

    public Map<String, Flow> getFlowIdVersionMap() {
        return flowIdVersionMap;
    }

    public Flow getFlow(String flowId) {

        return flowIdMap.get(flowId);
    }

    public Flow getFlow(String flowId, String version) {
        if (version == null || "".equals(version)) {
            return getFlow(flowId);
        }
        return flowIdVersionMap.get(flowId);
    }

    public void addComponents(ComponentDefines components) {
        containers.addComponents(components);
    }

    public void removeComponents(ComponentDefines components) {
        containers.removeComponents(components);
    }

    /**
     * 根据流程组件信息实例化组件
     *
     * @param componentName 流程组件名
     * @return
     */
    public ComponentInterface getComponentInstance(String componentName) {
        ComponentInterface componentInstance = null;
        if (componentName != null && !"".equals(componentName)) {
            componentInstance = containers.getComponentInstance(componentName);
            return componentInstance;
        }
        throw new FlowRuntimeException("flow.componentNotExist", componentName);
        // throw new FlowRuntimeException("组件名称:" + componentName + "找不到");
    }

    public Context getInputContext(Flow flow, Context context) {
        return getContext(flow.getInputParameters(), context);
    }

    private Context getContext(List<Parameter> parameters, Context context) {
        Context result = new ContextImpl();
        if (parameters != null) {
            for (Parameter parameter : parameters) {
                result.put(parameter.getName(),
                        context.get(parameter.getName()));
            }
        }
        return result;
    }

    public Context getOutputContext(Flow flow, Context context) {
        return getContext(flow.getOutputParameters(), context);
    }

    public void addComponent(ComponentDefine component) {
        containers.addComponent(component);
    }

    public void removeComponent(ComponentDefine component) {
        containers.removeComponent(component);
    }

    public ComponentDefine getComponentDefine(String componentName) {
        return containers.getComponentDefine(componentName);
    }

}
