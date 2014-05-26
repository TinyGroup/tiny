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

import org.tinygroup.cepcore.test.aop.EventProcessor1;
import org.tinygroup.cepcore.test.aop.ServiceInfo1;
import org.tinygroup.event.Parameter;

public class ToolUtil {
	
	public static EventProcessor1 getEventProcessor1(){
		EventProcessor1 ep = new EventProcessor1();
		
		ServiceInfo1 s1= new ServiceInfo1();
		s1.setServiceId("aoptest");
		
		Parameter p1 = new Parameter();
		p1.setArray(false);
		p1.setName("validateUser");
		p1.setRequired(true);
		p1.setType("org.tinygroup.cepcore.test.aop.bean.AopValidateUser");
		p1.setValidatorSence("china");
		
		Parameter p2 = new Parameter();
		p2.setArray(false);
		p2.setCollectionType("");
		p2.setName("p2");
		p2.setRequired(true);
		p2.setType("java.lang.String");
		p2.setValidatorSence("");
		
		Parameter p3 = new Parameter();
		p3.setArray(false);
		p3.setCollectionType("");
		p3.setName("p3");
		p3.setRequired(true);
		p3.setType("int");
		p3.setValidatorSence("buyNum");
		
		s1.getParameters().add(p1);
		s1.getParameters().add(p2);
		s1.getParameters().add(p3);
		
		ep.getServiceInfos().add(s1);
		
		
		ServiceInfo1 s2= new ServiceInfo1();
		s2.setServiceId("exceptionService");
		ep.getServiceInfos().add(s2);
		
		return ep;
	}
}
