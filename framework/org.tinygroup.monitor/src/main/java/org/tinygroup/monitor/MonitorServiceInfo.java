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
package org.tinygroup.monitor;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.event.Parameter;
import org.tinygroup.event.ServiceInfo;

public class MonitorServiceInfo implements ServiceInfo{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4140356973662445850L;
	private String serviceId;
	private String serviceName;
	
	public MonitorServiceInfo(String id,String name){
		this.serviceId = id;
		this.serviceName  = name;
	}
	
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public List<Parameter> getParameters() {
		return new ArrayList<Parameter>();
	}
	public List<Parameter> getResults() {
		return new ArrayList<Parameter>();
	}

	public int compareTo(ServiceInfo o) {
		return o.getServiceId().compareTo(serviceId);
	}
}
