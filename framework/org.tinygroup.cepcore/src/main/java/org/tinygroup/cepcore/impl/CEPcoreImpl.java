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
package org.tinygroup.cepcore.impl;

import static org.tinygroup.logger.LogLevel.DEBUG;
import static org.tinygroup.logger.LogLevel.INFO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.CEPCoreNodeManager;
import org.tinygroup.cepcore.CEPCoreWatch;
import org.tinygroup.cepcore.EventProcessor;
import org.tinygroup.cepcore.aop.CEPCoreAopManager;
import org.tinygroup.cepcore.exception.RequestNotFoundException;
import org.tinygroup.cepcore.util.CEPCoreUtil;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceInfo;
import org.tinygroup.event.ServiceRequest;
import org.tinygroup.event.central.Node;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.parser.filter.NameFilter;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * 
 * @author luoguo
 * 
 */
public class CEPcoreImpl implements CEPCore, CEPCoreWatch {

	/**
	 * Map<ServiceId,List<EventProcessor>>
	 */
	private Map<String, List<EventProcessor>> serviceIdMap = new HashMap<String, List<EventProcessor>>();
//	/**
//	 * Map<ServiceName,List<EventProcessor>>
//	 */
//	private Map<String, List<EventProcessor>> serviceNameMap = new HashMap<String, List<EventProcessor>>();
	/**
	 * Map<processorId,EventProcessor>
	 */
	private Map<String, EventProcessor> processorMap = new HashMap<String, EventProcessor>();

	private String nodeName = null;
	/**
	 * 本地服务列表
	 */
	private List<ServiceInfo> cepServiceInfos = new ArrayList<ServiceInfo>();
	/**
	 * 本地服务Map,Map<ServiceId,ServiceInfo>
	 */
	private Map<String, ServiceInfo> serviceInfoIdMap = new HashMap<String, ServiceInfo>();
//	/**
//	 * 本地服务Map,Map<ServiceName,ServiceInfo>
//	 */
//	private Map<String, ServiceInfo> serviceInfoNameMap = new HashMap<String, ServiceInfo>();
	/**
	 * 节点管理器，sc和as(ar)采用不同的管理器
	 */
	private CEPCoreNodeManager manager;
	/**
	 * 是否开启远程,默认false
	 */
	private boolean enableRemote = false;

	private static final Logger logger = LoggerFactory
			.getLogger(CEPcoreImpl.class);

	/**
	 * 注册事件处理器
	 * 
	 * @param eventProcessor
	 */
	public void registerEventProcessor(EventProcessor eventProcessor) {
		eventProcessor.setCepCore(this);
		List<ServiceInfo> services = eventProcessor.getServiceInfos();
		if (services != null) {
			cepServiceInfos.addAll(services);
			for (ServiceInfo info : services) {
				addServiceMap(info.getServiceId(), info, eventProcessor,
						serviceIdMap, serviceInfoIdMap);
			}
		}
		processorMap.put(eventProcessor.getId(), eventProcessor);
		logger.logMessage(INFO, "注册事件处理器[id:{0}]", eventProcessor.getId());
	}

	/**
	 * 存储服务信息
	 * 
	 * @param service
	 *            id/name
	 * @param info
	 *            服务信息
	 * @param eventProcessor
	 *            处理器
	 * @param map
	 *            id及处理器 Map/name及处理器 Map
	 * @param serviceInfoMap
	 *            idMap/nameMap
	 */
	private void addServiceMap(String service, ServiceInfo info,
			EventProcessor eventProcessor,
			Map<String, List<EventProcessor>> map,
			Map<String, ServiceInfo> serviceInfoMap) {
		if(service==null)
			return;
		serviceInfoMap.put(service, info);
		List<EventProcessor> list = map.get(service);
		if (list == null) {
			list = new ArrayList<EventProcessor>();
			map.put(service, list);
		}
		list.add(eventProcessor);
	}

	/**
	 * 注销事件处理器
	 * 
	 * @param eventProcessor
	 */
	public void unregisterEventProcessor(EventProcessor eventProcessor) {
		eventProcessor.setCepCore(null);
		List<ServiceInfo> serviceInfos = eventProcessor.getServiceInfos();
		if (serviceInfos != null) {
			for (ServiceInfo info : serviceInfos) {
				cepServiceInfos.remove(info);
				removerServiceMap(info.getServiceId(), eventProcessor,
						serviceIdMap);
			}
		}
		processorMap.remove(eventProcessor.getId());
		logger.logMessage(DEBUG, "注销事件处理器[id:{0}]", eventProcessor.getId());
	}

	private void removerServiceMap(String service,
			EventProcessor eventProcessor, Map<String, List<EventProcessor>> map) {
		List<EventProcessor> list = map.get(service);
		if (list == null) {
			return;
		}
		list.remove(eventProcessor);
	}

	/**
	 * 处理事件
	 */
	public void process(Event event) {
		ServiceRequest request = event.getServiceRequest();
		logger.logMessage(INFO, "CEP开始处理请求:serviceId:{0}",
				request.getServiceId());
		EventProcessor eventProcessor = getEventProcessor(request);
		String eventNodeName = event.getServiceRequest().getNodeName();
		logger.logMessage(INFO, "当前请求指定的执行节点为:{eventNodeName}", eventNodeName);
		// 若没指定node
		boolean hasNotNodeName = (eventNodeName == null || ""
				.equals(eventNodeName));
		// 若指定的为当前node
		boolean mustDoInThisNode = false;
		if (eventNodeName != null) {
			mustDoInThisNode = eventNodeName.equals(nodeName);
		}
		CEPCoreAopManager aopMananger = SpringUtil
				.getBean(CEPCoreAopManager.CEPCORE_AOP_BEAN);
		// 前置Aop
		aopMananger.beforeHandle(event);
		if (eventProcessor != null && (hasNotNodeName || mustDoInThisNode)) {// 如果本地存在处理器
			// local前置Aop
			aopMananger.beforeLocalHandle(event);
			try {
				// local处理
				deal(eventProcessor, event);
			} catch (RuntimeException e) {
				dealException(e, event);
				throw e;
			} catch (java.lang.Error e) {
				dealException(e, event);
				throw e;
			}
			// local后置Aop
			aopMananger.afterLocalHandle(event);
		} else if (isEnableRemote() && (hasNotNodeName || !mustDoInThisNode)) {
			// remote前置Aop
			aopMananger.beforeRemoteHandle(event);
			try {
				// remote处理
				remoteProcess(event);
			} catch (RuntimeException e) {
				dealException(e, event);
				throw e;
			} catch (java.lang.Error e) {
				dealException(e, event);
				throw e;
			}
			// remote后置Aop
			aopMananger.afterRemoteHandle(event);
		} else {
			serviceNotExist(request);
		}
		// 后置Aop
		aopMananger.afterHandle(event);
	}

	/**
	 * 异常处理
	 * 
	 * @param e
	 * @param event
	 */
	private void dealException(Throwable e, Event event) {
		CEPCoreUtil.handle(e, event);
		Throwable t = e.getCause();
		while (t != null) {
			CEPCoreUtil.handle(t, event);
			t = t.getCause();
		}
	}

	private void deal(EventProcessor eventProcessor, Event event) {
		logger.logMessage(DEBUG, "本地存在该请求,在本地进行处理");
		// 逻辑处理器与通道已经没有分别
		logger.log(INFO, "cepcore.process_logicProcessor", event.getEventId(),
				eventProcessor.getId());
		eventProcessor.process(event);
		logger.logMessage(DEBUG, "本地处理完毕");
	}

	public Event remoteProcess(Event event) {
		logger.logMessage(DEBUG, "本地不存在该请求,进行远端处理");
		Event resultEvent = manager.remoteProcess(event);
		event.getServiceRequest()
				.getContext()
				.putSubContext(event.getEventId(),
						resultEvent.getServiceRequest().getContext());
		logger.logMessage(DEBUG, "远端处理完毕");
		return event;
	}

	private void serviceNotExist(ServiceRequest request) {
		// 抛出异常
		// 1:本地不存在该服务，且不支持远程调用
		// 2:本地不存在该服务，指定本地调用
		throw new RequestNotFoundException(request.getServiceId());
	}

	/**
	 * 在本地服务器上找
	 * 
	 * @param serviceRequest
	 * @return
	 */
	private EventProcessor getEventProcessor(ServiceRequest serviceRequest) {
		List<EventProcessor> list = serviceIdMap.get(serviceRequest
				.getServiceId());
		if (list == null || list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	public void start() {
		logger.log(INFO, "cepcore.start");
		startCEPCore(this);
	}

	public void stop() {
		logger.log(INFO, "cepcore.stop");
		stopCEPCore(this);
	}

	public int getProcessorCount() {
		return processorMap.size();
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public List<ServiceInfo> getServiceInfos() {
		return cepServiceInfos;
	}

	public void registeNode(Node node, Node sc) {
		manager.registeNode(node, sc);
	}

	public void registeNode(List<Node> nodes, Node sc) {
		manager.registeNode(nodes, sc);
	}

	public void unregisteNode(Node node, Node sourceNode) {
		manager.unregisteNode(node, sourceNode);
	}

	public void unregisteNode(List<Node> nodes, Node sourceNode) {
		manager.unregisteNode(nodes, sourceNode);
	}

	public void addCentralNodes(List<Node> centralNodes) {
		manager.addCentralNodes(centralNodes);
	}

	public void addCentralNode(Node centralNode) {
		manager.addCentralNode(centralNode);
	}

	public Event remoteprocess(Event event, Node remoteNode) {
		return manager.remoteprocess(event, remoteNode);
	}

	public void startCEPCore(CEPCore cep) {
		if (isEnableRemote()) {
			manager.startCEPCore(cep);
		}
	}

	public void setManager(CEPCoreNodeManager manager) {
		this.manager = manager;
	}

	public void stopCEPCore(CEPCore cep) {
		if (isEnableRemote()) {
			manager.stopCEPCore(cep);
		}
	}

	public void setConfig(XmlNode config) {
		if (config == null) {
			return;
		}
		NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(config);
		XmlNode nodeConfig = nameFilter.findNode("node-config");
		if (nodeConfig != null) {
			String managerBeanName = nodeConfig.getAttribute("mananger-bean");
			if (CEPCoreUtil.isNull(managerBeanName)) {
				setManager((CEPCoreNodeManager) SpringUtil
						.getBean(CEPCoreNodeManager.DEFAULT_NODE_BEAN));
			} else {
				setManager((CEPCoreNodeManager) SpringUtil
						.getBean(managerBeanName));
			}
			String lEnableRemote = nodeConfig.getAttribute("enable-remote");
			if (!CEPCoreUtil.isNull(lEnableRemote)) {
				setEnableRemote(Boolean.parseBoolean(lEnableRemote));
			}
			setNodeName(nodeConfig.getAttribute("name"));

		} else {
			setManager((CEPCoreNodeManager) SpringUtil
					.getBean(CEPCoreNodeManager.DEFAULT_NODE_BEAN));
		}
		manager.setConfig(config);
	}

	public boolean isEnableRemote() {
		return enableRemote;
	}

	public void setEnableRemote(boolean enableRemote) {
		this.enableRemote = enableRemote;
	}

	public boolean check(Node node) {
		return manager.check(node);
	}

	public ServiceInfo getServiceInfo(String serviceId) {
		ServiceInfo info = getLocalServiceInfo(serviceId);
		if (info == null) {
			if(enableRemote==false){//如果本地查询服务没有查询到，且未开启远程调用，则抛出服务未找到异常
				throw new RequestNotFoundException(serviceId);
			}
			info = manager.getServiceInfo(serviceId);
		}
		return info;
	}

	/**
	 * 本地服务中找寻请求对应的服务信息
	 * 
	 * @param serviceId
	 *            服务id
	 * @param serviceName
	 *            服务name
	 * @return
	 */
	private ServiceInfo getLocalServiceInfo(String serviceId) {
		ServiceInfo info = serviceInfoIdMap.get(serviceId);
		return info;
	}
}
