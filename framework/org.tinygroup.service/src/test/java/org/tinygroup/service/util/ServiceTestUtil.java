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
package org.tinygroup.service.util;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.aop.CEPCoreAopManager;
import org.tinygroup.context.Context;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceRequest;
import org.tinygroup.service.ServiceProviderInterface;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.tinytestutil.AbstractTestUtil;

public class ServiceTestUtil {
	private static boolean init = false;

//	static ExceptionHandlerManager manager = null;

//	public static void initManager() {
//		if(manager!=null){
//			return ;
//		}
//		manager = SpringUtil.getBean(ExceptionHandlerManager.MANAGER_BEAN);;
//		addHandler(
//				"org.tinygroup.service.test.exception.ExceptionA",
//				new HandlerA());
//		addHandler(
//				"org.tinygroup.service.test.exception.ExceptionB",
//				new HandlerB());
//		addHandler(
//				"org.tinygroup.service.test.exception.ExceptionC",
//				new HandlerC());
//	}
//
//	private static void addHandler(String excption, ExceptionHandler<?> handler) {
//		try {
//			manager.addHandler(excption, handler);
//		} catch (ClassNotFoundException e) {
//			System.out.println(String.format("添加Handler时出错,Exception:%s未找到",
//					excption));
//		}
//	}

	public static void init(){
		if(!init){
			AbstractTestUtil.init("application.xml", true);
			initAopManager();
			init = true;
		}
		
	}
	
	private static void initAopManager(){
		CEPCoreAopManager aopManager = SpringUtil.getBean(CEPCoreAopManager.CEPCORE_AOP_BEAN);
		aopManager.addAopAdapter(CEPCoreAopManager.BEFORE_LOCAL, "requestParamValidate",null);
		CEPCore core = SpringUtil.getBean(CEPCore.CEP_CORE_BEAN);
		EventProcessorForValidate ep = new EventProcessorForValidate();
		ServiceProviderInterface provider = SpringUtil.getBean("service");
		ep.getServiceInfos().addAll(provider.getServiceRegistory().getServiceRegistryItems());
		core.registerEventProcessor(ep);
	}

	public static void execute(String serviceId, Context context) {
		init();
		ServiceProviderInterface provider = SpringUtil.getBean("service");
		provider.execute(serviceId, context);
	}
	public static void executeForValidate(String serviceId, Context context) {
		init();
		Event e = new Event();
		ServiceRequest sr = new ServiceRequest();
		e.setServiceRequest(sr);
		sr.setServiceId(serviceId);
		sr.setContext(context);
		CEPCore core = SpringUtil.getBean(CEPCore.CEP_CORE_BEAN);
		core.process(e);
		
	}

}
