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
package org.tinygroup.service.config;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("service-method")
public class ServiceMethod {
	@XStreamAsAttribute
	@XStreamAlias("local-name")
	private String localName;
	@XStreamAsAttribute
	@XStreamAlias("service-id")
	private String serviceId;
	@XStreamAsAttribute
	private String description;
	@XStreamAlias("service-parameters")
	private List<ServiceParameter> serviceParameters;
	@XStreamAlias("service-result")
	private ServiceResult serviceResult;
	@XStreamAsAttribute
	@XStreamAlias("method-name")
	private String methodName;
	@XStreamAsAttribute
	private boolean cacheable = false;

	public boolean isCacheable() {
		return cacheable;
	}

	public void setCacheable(boolean cacheable) {
		this.cacheable = cacheable;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public List<ServiceParameter> getServiceParameters() {
		if (serviceParameters == null)
			serviceParameters = new ArrayList<ServiceParameter>();
		return serviceParameters;
	}

	public void setServiceParameters(List<ServiceParameter> serviceParameters) {
		this.serviceParameters = serviceParameters;
	}

	public ServiceResult getServiceResult() {
		return serviceResult;
	}

	public void setServiceResult(ServiceResult serviceResult) {
		this.serviceResult = serviceResult;
	}

	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
