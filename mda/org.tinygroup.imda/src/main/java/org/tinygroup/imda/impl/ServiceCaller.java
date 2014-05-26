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
package org.tinygroup.imda.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.UUID;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceRequest;
import org.tinygroup.imda.ModelManager;
import org.tinygroup.imda.config.CustomizeStageConfig;
import org.tinygroup.imda.config.ModelProcessorStage;
import org.tinygroup.imda.tinyprocessor.ModelRequestInfo;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.springutil.SpringUtil;

public class ServiceCaller {
	private static Logger logger = LoggerFactory.getLogger(ServiceCaller.class);
	ModelManager modelManager;

	public ModelManager getModelManager() {
		return modelManager;
	}

	public void setModelManager(ModelManager modelManager) {
		this.modelManager = modelManager;
	}

	private ModelRequestInfo getModelRequestInfo(String name) {
		ModelRequestInfo modelRequestInfo = new ModelRequestInfo();
		modelRequestInfo.setRequestName(name);
		String infos[] = name.split("_");
		if (infos.length < 4) {
			throw new RuntimeException(name + "不是一个合法的模型请求参数");
		}
		modelRequestInfo.setModelTypeName(infos[0]);
		modelRequestInfo.setOperationType(infos[1]);
		modelRequestInfo.setModelId(infos[2]);
		modelRequestInfo.setProcessorId(infos[3]);
		if (infos.length == 5) {
			modelRequestInfo.setStageName(infos[4]);
		}
		return modelRequestInfo;
	}

	public void callService(Context context, String serviceId, String parameters) {
		logger.log(LogLevel.INFO, "开始服务调用：[服务ID：{}，参数：{}]", serviceId,
				parameters);
		String requestName = null;
        Context newContext=new ContextImpl();
        newContext.putSubContext("webContext",context);
		if (parameters != null && parameters.length() > 0) {
			for (String parameter : parameters.split("&")) {
				String[] keyValuePair = parameter.split("=");
				if (keyValuePair.length == 2) {
					try {
						String key = URLDecoder.decode(keyValuePair[0],
								"ISO-8859-1");
						String value = URLDecoder.decode(keyValuePair[1],
								"ISO-8859-1");
                        newContext.put(key, value);
						if (key.equals("requestName")) {
							requestName = value;
						}
					} catch (UnsupportedEncodingException e) {
						throw new RuntimeException(e);
					}
				} else {
					throw new RuntimeException("无效的参数：" + parameter
							+ ",正确的格式是:[a=b]方式，仅中括号中的内容");
				}
			}
		}
		if (requestName != null) {
			ModelRequestInfo modelRequestInfo = getModelRequestInfo(requestName);
            newContext.put("modelRequestInfo", modelRequestInfo);
			ModelProcessorStage stage = modelManager
					.getModelProcessorStage(modelRequestInfo);
			if (modelRequestInfo.getStageName() == null) {
				modelRequestInfo.setStageName(stage.getName());
			}
			String parameterBuilderBean;
			CustomizeStageConfig customizeConfig = modelManager
					.getCustomizeStageConfig(modelManager
							.getModel(modelRequestInfo.getModelId()),
							modelRequestInfo.getProcessorId(), modelRequestInfo
									.getStageName());
			if (customizeConfig != null
					&& customizeConfig.getParameterBuilderBean() != null
					&& customizeConfig.getParameterBuilderBean().length() > 0) {
				parameterBuilderBean = customizeConfig
						.getParameterBuilderBean();
			} else {
				parameterBuilderBean = stage.getParameterBuilderBean();
			}
			Context c = modelManager.processParameter(modelRequestInfo,
					parameterBuilderBean, newContext);
            newContext.put("modelRequestContext", c);

		}
		CEPCore cepCore = (CEPCore)SpringUtil.getBean("cepcore");

		Event event = getEvent(newContext, serviceId);
		cepCore.process(event);
		logger.log(LogLevel.INFO, "完成服务调用：[服务ID：{}，参数：{}]", serviceId,
				parameters);
	}

	public void copyObject(Context context, String sourceKey, String destKey) {
		Object object = context.get(sourceKey);
		context.put(destKey, object);
	}

	private Event getEvent(Context context, String serviceId) {
		Event event = new Event(UUID.randomUUID().toString()
				.replaceAll("-", ""));
		ServiceRequest serviceRequest = new ServiceRequest();
		event.setServiceRequest(serviceRequest);
		serviceRequest.setServiceId(serviceId);
		serviceRequest.setContext(context);
		return event;
	}
}
