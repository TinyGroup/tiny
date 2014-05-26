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
package org.tinygroup.flow;

import java.util.Map;

import org.tinygroup.context.Context;
import org.tinygroup.flow.config.ComponentDefine;
import org.tinygroup.flow.config.ComponentDefines;
import org.tinygroup.flow.config.Flow;
import org.tinygroup.flow.config.Node;

/**
 * 流程执行器
 * 
 * @author luoguo
 * 
 */
public interface FlowExecutor {
	String FLOW_XSTREAM_PACKAGENAME = "flow";
	String FLOW_BEAN = "flowExecutor";
	String PAGE_FLOW_BEAN = "pageFlowExecutor";

	/**
	 * 流程执行接口方法
	 * 
	 * @param flow
	 *            要执行的流程
	 * @param nodeId
	 *            要执行的节点标识
	 * @param context
	 *            要执行的环境
	 */
	void execute(String flowId, String nodeId, Context context);

	void execute(String flowId, String nodeId, String version, Context context);

	/**
	 * 表示从开始节点开始执行
	 * 
	 * @param flowId
	 * @param context
	 */
	void execute(String flowId, Context context);

	String getEngineVersion();

	/**
	 * 执行指定流程的指定节点
	 * 
	 * @param flow
	 * @param node
	 * @param context
	 */
	void execute(Flow flow, Node node, Context context);

	/**
	 * 返回流程Map
	 * 
	 * @return
	 */
	Map<String, Flow> getFlowIdMap();

	Map<String, Flow> getFlowIdVersionMap();

	Flow getFlow(String flowId);

	Flow getFlow(String flowId, String version);

	Context getInputContext(Flow flow, Context context);

	Context getOutputContext(Flow flow, Context context);

	/**
	 * 添加流程
	 * 
	 * @param flow
	 */
	void addFlow(Flow flow);

	/**
	 * 组装flow，主要用于处理继承关系
	 */
	void assemble();

	/**
	 * 增加组件定义信息
	 * 
	 * @param components
	 */
	void addComponents(ComponentDefines components);
	
	/**
	 * 增加组件定义信息
	 * 
	 * @param components
	 */
	void addComponent(ComponentDefine component);

	void removeComponents(ComponentDefines components);
	
	/**
	 * 移除组件定义信息
	 * 
	 * @param components
	 */
	void removeComponent(ComponentDefine component);

	/**
	 * 根据组件名称获取组件实例
	 * 
	 * @param componentName
	 * @return
	 * @throws Exception
	 */
	ComponentInterface getComponentInstance(String componentName)
			throws Exception;

	void removeFlow(Flow flow);

	void removeFlow(String flowId, String version);

	void removeFlow(String flowId);
	
	ComponentDefine getComponentDefine(String componentName);
}
