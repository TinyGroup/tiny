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
package org.tinygroup.service.aop;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.parser.filter.NameFilter;
import org.tinygroup.service.ServiceProxy;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.xmlparser.node.XmlNode;

public class ServiceAopManager {
	public static final String SERVICE_AOP_BEFORE = "before";
	public static final String SERVICE_AOP_AFTER = "after";
	private static Logger logger = LoggerFactory
			.getLogger(ServiceAopManager.class);
	private List<ServiceAopAdapter> beforeList = new ArrayList<ServiceAopAdapter>();
	private List<ServiceAopAdapter> beforeEnableList = new ArrayList<ServiceAopAdapter>();
	private List<ServiceAopAdapter> afterList = new ArrayList<ServiceAopAdapter>();
	private List<ServiceAopAdapter> afterEnableList = new ArrayList<ServiceAopAdapter>();
	private static ServiceAopManager manager;
	static {
		manager = new ServiceAopManager();
	}

	private ServiceAopManager() {
	}

	public static ServiceAopManager getInstance() {
		return manager;
	}

	public void setConfig(XmlNode config) {
		if (config == null)
			return;
		NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(config);
		List<XmlNode> aopList = nameFilter.findNodeList("aop-config");
		for (XmlNode node : aopList) {
			String postion = node.getAttribute("position");
			if (postion.equals(SERVICE_AOP_BEFORE)) {
				dealAopConfig(node, beforeList, beforeEnableList);
			} else {
				dealAopConfig(node, afterList, afterEnableList);
			}

		}
	}

	private void dealAopConfig(XmlNode node, List<ServiceAopAdapter> allList,
			List<ServiceAopAdapter> enbaleList) {
		String bean = node.getAttribute("bean");
		String enable = node.getAttribute("enable");
		try {
			ServiceAopAdapter adapter = SpringUtil.getBean(bean);
			allList.add(adapter);
			if (enable == null || "true".equals(enable)) {
				enbaleList.add(adapter);
			}
		} catch (Exception e) {
			logger.errorMessage("添加ServiceAopAdapter时出错,bean:{0},enable:{1}",
					e, bean, enable);
		}

	}

	public void beforeHandle(Object[] args, ServiceProxy sp) {
		logger.logMessage(LogLevel.INFO, "开始执行SerciveAop前置处理器");
		for (int i = 0; i < beforeEnableList.size(); i++) {
			ServiceAopAdapter adapter = beforeEnableList.get(i);
			logger.logMessage(LogLevel.INFO, "开始执行SerciveAop前置处理器{0}",adapter.getClass().toString());
			adapter.handle(args, sp);
			logger.logMessage(LogLevel.INFO, "执行SerciveAop前置处理器{0}完成",adapter.getClass().toString());
		}
		logger.logMessage(LogLevel.INFO, "执行SerciveAop前置处理器完成");
	}

	public void afterHandle(Object[] args, ServiceProxy sp) {
		logger.logMessage(LogLevel.INFO, "开始执行SerciveAop后置处理器");
		for (int i = 0; i < afterEnableList.size(); i++) {
			ServiceAopAdapter adapter = afterEnableList.get(i);
			logger.logMessage(LogLevel.INFO, "开始执行SerciveAop后置处理器{0}",adapter.getClass().toString());
			adapter.handle(args, sp);
			logger.logMessage(LogLevel.INFO, "执行SerciveAop后置处理器{0}完成",adapter.getClass().toString());
		}
		logger.logMessage(LogLevel.INFO, "执行SerciveAop后置处理器完成");
	}

	public List<ServiceAopAdapter> getBeforeList() {
		return beforeList;
	}

	public List<ServiceAopAdapter> getBeforeEnableList() {
		return beforeEnableList;
	}

	public List<ServiceAopAdapter> getAfterList() {
		return afterList;
	}

	public List<ServiceAopAdapter> getAfterEnableList() {
		return afterEnableList;
	}

}
