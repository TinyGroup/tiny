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
package org.tinygroup.cepcore.test.aop;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.EventProcessor;
import org.tinygroup.cepcore.test.aop.exception.ExceptionService;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceInfo;

public class EventProcessor1 implements EventProcessor{
	List<ServiceInfo> list = new ArrayList<ServiceInfo>();
	public void process(Event event) {
		System.out.println("EventProcessor1 execute");
		if("exceptionService".equals(event.getServiceRequest().getServiceId())){
			ExceptionService e = new ExceptionService();
			e.deal();
		}
	}

	public void setCepCore(CEPCore cepCore) {
		
	}

	public List<ServiceInfo> getServiceInfos() {
		return list;
	}

	public String getId() {
		return EventProcessor1.class.getName();
	}

	public int getType() {
		
		return 0;
	}

}
