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
package org.tinygroup.cepcore.test.aop.util;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.EventProcessor;
import org.tinygroup.cepcore.aop.CEPCoreAopManager;
import org.tinygroup.cepcore.test.aop.exception.ExceptionHanlder1;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceInfo;
import org.tinygroup.exceptionhandler.ExceptionHandlerManager;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.tinytestutil.AbstractTestUtil;

public class AopTestUtil {
	static ExceptionHandlerManager handlerManager = null;
	static CEPCoreAopManager manager = null;
	static CEPCore cep = null;
	private static boolean init = false;

	public static void registerEventProcessor(EventProcessor processor) {
		getCep().registerEventProcessor(processor);
	}
	
	public static CEPCore getCep(){
		init();
		if (cep == null)
			cep = SpringUtil.getBean(CEPCore.CEP_CORE_BEAN);
		return cep;
	}

	private static void init(){
		if(init)
			return;
		init = true;
		AbstractTestUtil.init(null, true);
		manager = SpringUtil.getBean(CEPCoreAopManager.CEPCORE_AOP_BEAN);
		manager.addAopAdapter(CEPCoreAopManager.BEFORE_LOCAL, "aopTestAdapter","aop.*");
		manager.addAopAdapter(CEPCoreAopManager.BEFORE_LOCAL, "requestParamValidate",null);
		
		handlerManager = SpringUtil.getBean(ExceptionHandlerManager.MANAGER_BEAN);
		try {
			handlerManager.addHandler("org.tinygroup.exception.TinySysRuntimeException", new ExceptionHanlder1());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		registerEventProcessor(ToolUtil.getEventProcessor1());
		
	}

	public static void execute(Event event) {
		init();
		getCep().process(event);
	}

	public static ServiceInfo getService(String id){
		return getCep().getServiceInfo(id);
	}
}
