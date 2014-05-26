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

import java.util.List;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.context.Context;
import org.tinygroup.context.util.ContextFactory;
import org.tinygroup.event.Event;
import org.tinygroup.event.Parameter;
import org.tinygroup.event.ServiceInfo;
import org.tinygroup.event.ServiceRequest;
import org.tinygroup.springutil.SpringUtil;

public class CEPCoreExecuteUtil {
	private static CEPCore cepcore;

	public static Object execute(String serviceId, Object[] paramArray) {
		if (cepcore == null) {
			cepcore = SpringUtil.getBean(CEPCore.CEP_CORE_BEAN);
		}
		// 获取服务信息
		ServiceInfo serviceInfo = cepcore.getServiceInfo(serviceId);
		// 将服务参数值与key映射起来,组装一个context出来
		List<Parameter> params = serviceInfo.getParameters();
		Context context = ContextFactory.getContext();
		for (int i = 0; i < params.size(); i++) {
			context.put(params.get(i).getName(), paramArray[i]);
		}
		// 组装Event
		Event event = new Event();
		ServiceRequest request = new ServiceRequest();
		request.setServiceId(serviceId);
		request.setContext(context);
		event.setServiceRequest(request);
		cepcore.process(event);

		List<Parameter> result = serviceInfo.getResults();
		if (result.size() == 0)
			return null;
		return event.getServiceRequest().getContext()
				.get(result.get(0).getName());
	}
}
