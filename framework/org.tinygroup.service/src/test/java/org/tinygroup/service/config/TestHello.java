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

import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.service.ServiceProviderInterface;
import org.tinygroup.service.impl.ServiceProviderImpl;
import org.tinygroup.service.registry.ServiceRegistry;
import org.tinygroup.service.registry.impl.ServiceRegistryImpl;

public class TestHello {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		ServiceProviderInterface serviceProvider = new ServiceProviderImpl();
		ServiceRegistry serviceRegistry = new ServiceRegistryImpl();
		serviceProvider.setServiceRegistory(serviceRegistry);
		new XmlConfigLoader().loadService(serviceRegistry);
		System.out.println(serviceRegistry.size());

		Context Context = new ContextImpl();
		Context.put("name", "abc");
		serviceProvider.execute("hello", Context);
		System.out.println(Context.get("helloResult"));
	}
}
