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

import java.util.List;
import java.util.ArrayList;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("service-components")
public class ServiceComponents {
	@XStreamImplicit
	private List<ServiceComponent> serviceComponents;

	public List<ServiceComponent> getServiceComponents() {
		if (serviceComponents == null) {
			serviceComponents = new ArrayList<ServiceComponent>();
		}
		return serviceComponents;
	}

	public void setServiceComponents(List<ServiceComponent> serviceComponents) {
		this.serviceComponents = serviceComponents;
	}

}
