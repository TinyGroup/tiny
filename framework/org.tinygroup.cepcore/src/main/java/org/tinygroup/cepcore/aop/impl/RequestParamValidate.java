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
package org.tinygroup.cepcore.aop.impl;

import java.util.List;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.aop.CEPCoreAopAdapter;
import org.tinygroup.cepcore.exception.RequestNotFoundException;
import org.tinygroup.cepcore.util.CEPCoreUtil;
import org.tinygroup.commons.tools.ValueUtil;
import org.tinygroup.context.Context;
import org.tinygroup.context2object.util.Context2ObjectUtil;
import org.tinygroup.event.Event;
import org.tinygroup.event.Parameter;
import org.tinygroup.event.ServiceInfo;
import org.tinygroup.event.ServiceRequest;
import org.tinygroup.event.exception.ParamIsNullException;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

public class RequestParamValidate implements CEPCoreAopAdapter {
	private static Logger logger = LoggerFactory.getLogger(RequestParamValidate.class);
	
	private CEPCore cepCore;
	
	public CEPCore getCepCore() {
		return cepCore;
	}
	
	public void setCepCore(CEPCore cepCore) {
		this.cepCore = cepCore;
	}

	public void handle(Event event) {
		ServiceRequest request = event.getServiceRequest();
		ServiceInfo info = find(request);
		Object[] args = getArguments(request.getContext(),info);
		ParameterValidator.validate(args, info.getParameters());
	}
	
	private ServiceInfo find(ServiceRequest request){
		String requestId = request.getServiceId();
		List<ServiceInfo> list = cepCore.getServiceInfos();
		ServiceInfo info = null;
		if(!CEPCoreUtil.isNull(requestId)){
			info = findById(requestId, list);
		}
		if(info==null){
			throw new RequestNotFoundException(requestId);
		}
		return info;
	}
	
	private ServiceInfo findById(String requestId,List<ServiceInfo> list){
		for(ServiceInfo info:list){
			if(requestId.equals(info.getServiceId())){
				return info;
			}
		}
		return null;
		
	}
	

	
	private Object[] getArguments(Context context,ServiceInfo info) {
		Object args[] = null;
		List<Parameter> params = info.getParameters();
		if (params != null && params.size() > 0) {
			args = new Object[params.size()];
			for (int i = 0; i < params.size(); i++) {
				args[i] = getArgument(context, params.get(i));
			}
		}
		return args;
	}
	
	private Object getArgument(Context context, Parameter param) {
		String paramName = param.getName();
		Object obj = Context2ObjectUtil.getObject(param, context);
		if (obj == null) {
			if (param.isRequired()) { // 如果输入参数是必须的,则抛出异常
				logger.logMessage(LogLevel.ERROR, "参数{paramName}未传递", paramName);
				throw new ParamIsNullException(paramName);
			} else { // 如果输出参数非必须，直接返回null
				return null;
			}
		}
		if (!(obj instanceof String)) {
			return obj;
		}
		return ValueUtil.getValue((String) obj, param.getType());
	}
}
