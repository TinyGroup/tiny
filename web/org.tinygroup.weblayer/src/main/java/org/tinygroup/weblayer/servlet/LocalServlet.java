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
package org.tinygroup.weblayer.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class LocalServlet extends javax.servlet.http.HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5512812746406209310L;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response)
			throws javax.servlet.ServletException, IOException {
//		String serviceId = allocateRequestId(request);
//		Event event = new Event();
//		event.setEventId("11112");
//		ServiceRequest serviceRequest = new ServiceRequest();
//		WebContext context = new WebContextImpl();
//		context.setRequest(request);
//		serviceRequest.setContext(context);
//		serviceRequest.setServiceId(serviceId);
//		event.setServiceRequest(serviceRequest);
//		PluginManager pluginManager = PluginManagerFactory.getPluginManager();
//		SimpleChannel channel = (SimpleChannel) pluginManager.getService(pluginManager.getPluginInfo("simplechannel"), "simplechannel");
//		channel.sendEvent(event);
	}

	protected void doGet(javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws javax.servlet.ServletException, IOException {
		doPost(request, response);
	}
	
	public String allocateRequestId(HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        int i = requestPath.indexOf(request.getContextPath());
        int j = requestPath.lastIndexOf(".");
        String requestId = requestPath.substring(
                i + request.getContextPath().length() + 1, j - i).replace("/",
                ".");
        return requestId;
    }
}
