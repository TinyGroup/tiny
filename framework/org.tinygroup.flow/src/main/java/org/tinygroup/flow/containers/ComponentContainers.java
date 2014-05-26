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
package org.tinygroup.flow.containers;

import java.util.HashMap;
import java.util.Map;

import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flow.config.ComponentDefine;
import org.tinygroup.flow.config.ComponentDefines;
import org.tinygroup.flow.exception.FlowRuntimeException;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.springutil.SpringUtil;

/**
 * 组件容器
 * @author renhui
 *
 */
public class ComponentContainers {
	private static Logger logger = LoggerFactory
	.getLogger(ComponentContainers.class);
	/**
	 * 用于存储所有组件信息列表
	 */
	//private List<ComponentDefines> components=new ArrayList<ComponentDefines>();
	
     /**
      * key=beanId
      */
	private Map<String, ComponentDefine> beanIdMap=new HashMap<String, ComponentDefine>(); 
	/**
	 * key=name
	 */
	private Map<String, ComponentDefine> nameMap=new HashMap<String, ComponentDefine>(); 
	
	/**
	 * 
	 * 增加组件信息
	 */
	public void addComponents(ComponentDefines components){
		
		for (ComponentDefine component : components.getComponentDefines()) {
			addComponent(component);
		}
		
	}
	public void removeComponents(ComponentDefines components){
		for (ComponentDefine component : components.getComponentDefines()) {
			removeComponent(component);
		}
		
	}	
	public void removeComponent(ComponentDefine component) {
		if(component==null)
			return;
		logger.logMessage(LogLevel.INFO, "移除组件Component[name:{0},bean:{1}]",component.getName(),component.getBean());
		beanIdMap.remove(component.getBean());
		nameMap.remove(component.getName());
	}
	
	/**
	 * 根据流程组件名称得到组件实例
	 * @param componentName 流程组件名称
	 * @return
	 */
	public ComponentInterface getComponentInstance(String componentName) {
		if(nameMap.get(componentName)!=null){
			//20121127获取bean的源头从simpleFactory修改为spring
			return (ComponentInterface) SpringUtil.getBean(nameMap.get(componentName).getBean());
		}
		throw new FlowRuntimeException("flow.componentNotExist",componentName);
		
	}
	public void addComponent(ComponentDefine component) {
		if(component==null)
			return;
		logger.logMessage(LogLevel.INFO, "添加组件Component[name:{0},bean:{1}]",component.getName(),component.getBean());
		beanIdMap.put(component.getBean(), component);
		nameMap.put(component.getName(), component);
	}
	
	public ComponentDefine getComponentDefine(String componentName){
		return nameMap.get(componentName);
	}
}
