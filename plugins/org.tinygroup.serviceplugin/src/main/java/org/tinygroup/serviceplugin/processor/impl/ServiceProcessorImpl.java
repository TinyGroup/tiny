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
package org.tinygroup.serviceplugin.processor.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.impl.AbstractEventProcessor;
import org.tinygroup.context.Context;
import org.tinygroup.context.util.ContextFactory;
import org.tinygroup.event.Event;
import org.tinygroup.event.Parameter;
import org.tinygroup.event.ServiceInfo;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.service.Service;
import org.tinygroup.service.ServiceProviderInterface;
import org.tinygroup.service.registry.ServiceRegistryItem;
import org.tinygroup.serviceplugin.ServicePlugin;
import org.tinygroup.serviceplugin.processor.ServiceProcessor;
import org.tinygroup.xmlparser.node.XmlNode;

public class ServiceProcessorImpl extends AbstractEventProcessor implements
		ServiceProcessor {
	private static Logger logger = LoggerFactory
			.getLogger(ServiceProcessorImpl.class);

	private List<ServiceProviderInterface> providers = new ArrayList<ServiceProviderInterface>();
	private List<ServiceInfo> infos = new ArrayList<ServiceInfo>();

	public void process(Event event) {
		String serviceId = event.getServiceRequest().getServiceId();
		Service service = null;
		ServiceProviderInterface serviceProvider = null;

		for (ServiceProviderInterface provider : providers) {
			service = provider.getService(serviceId);
			if (service != null) {
				serviceProvider = provider;
				break;
			}
		}

		if (service != null) {
			serviceProvider.execute(service, event.getServiceRequest()
					.getContext());
			// 回写Context,event返回时
			ServiceRegistryItem item = serviceProvider
					.getServiceRegistryItem(service);
			Context oldC = event.getServiceRequest().getContext();
			Context c = ContextFactory.getContext();
			for (Parameter p : item.getResults()) {
				if (("void").equals(p.getTitle()) || ("").equals(p.getTitle())
						|| p.getType() == null) {
				} else {
					String name = p.getName();
					c.put(name, oldC.get(name));
				}

			}
			event.getServiceRequest().setContext(c);
		} else {
			logger.logMessage(LogLevel.ERROR, "未找到合适的Service[id:{0}]",
					serviceId);
		}
	}

	public void setCepCore(CEPCore cepCore) {

	}

	public void addServiceProvider(ServiceProviderInterface provider) {
		providers.add(provider);
		Collection<ServiceRegistryItem> collection = provider
				.getServiceRegistory().getServiceRegistryItems();
		if (collection != null) {
			for (ServiceRegistryItem item : collection) {
				if (!infos.contains(item))
					infos.add(item);
			}
		}

	}

	public List<ServiceInfo> getServiceInfos() {
		return infos;
	}

	public void setConfig(XmlNode config) {

	}

}
