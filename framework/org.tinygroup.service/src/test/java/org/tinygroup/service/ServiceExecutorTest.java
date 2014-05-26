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
package org.tinygroup.service;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.event.Parameter;
import org.tinygroup.service.exception.ServiceExecuteException;
import org.tinygroup.service.exception.ServiceNotExistException;
import org.tinygroup.service.impl.ServiceProviderImpl;
import org.tinygroup.service.registry.ServiceRegistry;
import org.tinygroup.service.registry.ServiceRegistryItem;
import org.tinygroup.service.registry.impl.ServiceRegistryImpl;

public class ServiceExecutorTest extends TestCase {
	ServiceProviderInterface serviceExecutor = new ServiceProviderImpl();
	ServiceRegistry serviceRegistry = new ServiceRegistryImpl();
	Context Context = new ContextImpl();

	protected void setUp() throws Exception {
		super.setUp();
		Context.put("aa", "abc");
		serviceExecutor.setServiceRegistory(serviceRegistry);
		serviceRegistry.clear();
		PrintContextService printContextService = new PrintContextService();
		ServiceRegistryItem item = new ServiceRegistryItem();
		item.setServiceId("aaaa");
		Parameter parameterDescriptor = new Parameter();
		parameterDescriptor.setArray(false);
		parameterDescriptor.setName("aa");
		parameterDescriptor.setType("java.lang.String");
		parameterDescriptor.setRequired(true);
		item.setService(printContextService);
		List<Parameter> parameterDescriptors = new ArrayList<Parameter>();
		parameterDescriptors.add(parameterDescriptor);
		item.setParameters(parameterDescriptors);
		item.setResults(parameterDescriptors);
		serviceRegistry.registeService(item);
	}

	public void testSetServiceRegistory() {
		serviceExecutor.setServiceRegistory(serviceRegistry);
		assertEquals(serviceRegistry, serviceExecutor.getServiceRegistory());
	}

	public void testValidateInputParameter() {

		try {
			Context Context = new ContextImpl();
			Context.put("aa", "abc");
			serviceExecutor.validateInputParameter(serviceRegistry
					.getServiceRegistryItem("aaaa").getService(), Context);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	public void testValidateOutputParameter() {
		try {

			serviceExecutor.validateOutputParameter(serviceRegistry
					.getServiceRegistryItem("aaaa").getService(), Context);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	public void testExecuteT() throws ServiceExecuteException,
			ServiceNotExistException {
		Service service = serviceRegistry.getServiceRegistryItem("aaaa")
				.getService();
		serviceExecutor.execute(service, Context);
		assertEquals("yes", Context.get("result"));
	}

	public void testExecuteStringContext() throws ServiceExecuteException,
			ServiceNotExistException {
		serviceExecutor.execute("aaaa", Context);
		assertEquals("yes", Context.get("result"));
	}

}
