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
package org.tinygroup.cepcore.util;

import org.tinygroup.cepcore.exception.CEPRunException;
import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.context2object.util.Context2ObjectUtil;
import org.tinygroup.event.Event;
import org.tinygroup.event.Parameter;
import org.tinygroup.event.ServiceInfo;
import org.tinygroup.event.central.Node;
import org.tinygroup.exceptionhandler.ExceptionHandlerManager;
import org.tinygroup.springutil.SpringUtil;

public final class CEPCoreUtil {
	private static ExceptionHandlerManager manager;

	static {
		manager = SpringUtil.getBean(ExceptionHandlerManager.MANAGER_BEAN);
	}

	private CEPCoreUtil() {
	}

	public static boolean handle(Throwable e,Event event) {
		return manager.handle(e,event);
	}

	public static boolean isNull(String s) {
		return s == null || "".equals(s);
	}

	public static String getNodeKey(Node node) {
		String nodeName = node.getNodeName();
		if (nodeName == null) {
			nodeName = "";
		}
		return String
				.format("%s_%s_%s", node.getIp(), node.getPort(), nodeName);
	}

	public static Context getContext(Event event, ServiceInfo info) {
		Context c = new ContextImpl();
		Context oldContext = event.getServiceRequest().getContext();
		if (info.getParameters() == null) {
			return c;
		}
		for (Parameter p : info.getParameters()) {
			Object value = Context2ObjectUtil.getObject(p, oldContext);
			if (value != null && !(value instanceof java.io.Serializable)) {
				throw new CEPRunException("cepcore.paramNotSerializable", event
						.getServiceRequest().getServiceId(), p.getName());
			}
			c.put(p.getName(), value);
		}
		return c;
	}

}
