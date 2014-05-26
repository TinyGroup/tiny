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
package org.tinygroup.cepcore.aop.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.tinygroup.cepcore.aop.CEPCoreAopAdapter;
import org.tinygroup.cepcore.aop.CEPCoreAopManager;
import org.tinygroup.event.Event;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.parser.filter.NameFilter;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.xmlparser.node.XmlNode;

public class CEPCoreAopManagerImpl implements CEPCoreAopManager{
	private static Logger logger = LoggerFactory.getLogger(CEPCoreAopManagerImpl.class);
	private List<CEPCoreAopAdapterContainer> beforeList = new ArrayList<CEPCoreAopAdapterContainer>();
	private List<CEPCoreAopAdapterContainer> afterList = new ArrayList<CEPCoreAopAdapterContainer>();
	private List<CEPCoreAopAdapterContainer> beforeLocalList = new ArrayList<CEPCoreAopAdapterContainer>();
	private List<CEPCoreAopAdapterContainer> afterLocalList = new ArrayList<CEPCoreAopAdapterContainer>();
	private List<CEPCoreAopAdapterContainer> beforeRemoteList = new ArrayList<CEPCoreAopAdapterContainer>();
	private List<CEPCoreAopAdapterContainer> afterRemoteList = new ArrayList<CEPCoreAopAdapterContainer>();
	//private Map<String,List<CEPCoreAopAdapter>> aopMapList = new HashMap<String, List<CEPCoreAopAdapter>>();
	
	
	public void init(XmlNode config) {
		if(config==null){
			logger.logMessage(LogLevel.INFO, "AOP配置信息为空");
			return;
		}
		logger.logMessage(LogLevel.INFO, "开始初始化AOP配置");
		NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(config);
		List<XmlNode> aopList = nameFilter.findNodeList("aop-config");
		for (XmlNode node : aopList) {
			String position = node.getAttribute("position");
			String bean = node.getAttribute("bean");
			String patternString = node.getAttribute("service-id");
			addAopAdapter(position, bean,patternString);
		}
		logger.logMessage(LogLevel.INFO, "初始化AOP配置完成");
		
	}
	public void beforeLocalHandle(Event event) {
		logger.logMessage(LogLevel.DEBUG, "本地前置AOP开始处理");
		for(CEPCoreAopAdapterContainer adapterContainer:beforeLocalList){
			adapterContainer.handle(event);
		}
		logger.logMessage(LogLevel.DEBUG, "本地前置AOP处理完成");
	}
	public void afterLocalHandle(Event event) {
		logger.logMessage(LogLevel.DEBUG, "本地后置AOP开始处理");
		for(CEPCoreAopAdapterContainer adapterContainer:afterLocalList){
			adapterContainer.handle(event);
		}
		logger.logMessage(LogLevel.DEBUG, "本地后置AOP处理完成");
	}
	public void beforeRemoteHandle(Event event) {
		logger.logMessage(LogLevel.DEBUG, "远程前置AOP开始处理");
		for(CEPCoreAopAdapterContainer adapterContainer:beforeRemoteList){
			adapterContainer.handle(event);
		}
		logger.logMessage(LogLevel.DEBUG, "远程前置AOP处理完成");
	}
	public void afterRemoteHandle(Event event) {
		logger.logMessage(LogLevel.DEBUG, "远程后置AOP开始处理");
		for(CEPCoreAopAdapterContainer adapterContainer:afterRemoteList){
			adapterContainer.handle(event);
		}
		logger.logMessage(LogLevel.DEBUG, "远程后置AOP处理完成");
	}
	public void beforeHandle(Event event) {
		logger.logMessage(LogLevel.DEBUG, "请求前置AOP开始处理");
		for(CEPCoreAopAdapterContainer adapterContainer:beforeList){
			adapterContainer.handle(event);
		}
		logger.logMessage(LogLevel.DEBUG, "请求前置AOP处理完成");
	}
	public void afterHandle(Event event) {
		logger.logMessage(LogLevel.DEBUG, "请求后置AOP开始处理");
		for(CEPCoreAopAdapterContainer adapterContainer:afterList){
			adapterContainer.handle(event);
		}
		logger.logMessage(LogLevel.DEBUG, "请求后置AOP处理完成");
	}
	public void addAopAdapter(String position, String bean,String patternString) {
		logger.logMessage(LogLevel.INFO, "初始化AOP,bean:{0},position:{1}",bean,position);
		CEPCoreAopAdapter adapter = null;
		Pattern servicePattern = null;
		if(patternString!=null && !"".equals(patternString)){
			servicePattern = Pattern.compile(patternString);
		}
		try {
			adapter = SpringUtil.getBean(bean);
		} catch (Exception e) {
			logger.logMessage(LogLevel.ERROR, "初始化AOP处理器{0}出错:{1}",bean,e.getMessage());
			return;
		}
		CEPCoreAopAdapterContainer adapterContainer = new CEPCoreAopAdapterContainer(adapter, servicePattern);
		if(BEFORE.equals(position)){
			beforeList.add(adapterContainer);
		}else if(BEFORE_LOCAL.equals(position)){
			beforeLocalList.add(adapterContainer);
		}else if(BEFORE_REMOTE.equals(position)){
			beforeRemoteList.add(adapterContainer);
		}else if(AFTER.equals(position)){
			afterList.add(adapterContainer);
		}else if(AFTER_LOCAL.equals(position)){
			afterLocalList.add(adapterContainer);
		}else if(AFTER_REMOTE.equals(position)){
			afterRemoteList.add(adapterContainer);
		}  
		logger.logMessage(LogLevel.INFO, "初始化AOP,bean:{0},position:{1}完成",bean,position);
	}
	
}
