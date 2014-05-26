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
package org.tinygroup.imda.tinyprocessor;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.context.Context;
import org.tinygroup.context.util.ContextFactory;
import org.tinygroup.convert.objectjson.jackson.ObjectToJson;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceRequest;
import org.tinygroup.imda.ModelManager;
import org.tinygroup.imda.ModelPermissionChecker;
import org.tinygroup.imda.config.CustomizeStageConfig;
import org.tinygroup.imda.config.ModelDefine;
import org.tinygroup.imda.config.ModelProcessorStage;
import org.tinygroup.imda.validate.ValidateManager;
import org.tinygroup.imda.validate.ValidateResults;
import org.tinygroup.imda.validate.ValidateRule;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.velocity.impl.VelocityHelperImpl;
import org.tinygroup.weblayer.AbstractTinyProcessor;
import org.tinygroup.weblayer.WebContext;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 页面流flow-processor
 * 
 * @author renhui
 */
public class MdaTinyProcessor extends AbstractTinyProcessor {
	ObjectToJson<Object> jsonConverter = new ObjectToJson<Object>(
			JsonSerialize.Inclusion.NON_NULL);

	private Logger logger = LoggerFactory.getLogger(MdaTinyProcessor.class);
	public final static String MODEL_TYPE_NAME = "_modelTypeName_";
	public final static String MODEL_ID = "_modelId_";
	public final static String PROCESS_ID = "_processId_";
	public final static String STAGE_NAME = "_stageName_";
	ModelManager modelManager;
	ValidateManager validateManager;

	public ValidateManager getValidateManager() {
		return validateManager;
	}

	public void setValidateManager(ValidateManager validateManager) {
		this.validateManager = validateManager;
	}

	public ModelManager getModelManager() {
		return modelManager;
	}

	public void setModelManager(ModelManager modelManager) {
		this.modelManager = modelManager;
	}

	/**
	 * @param urlString
	 *            格式用“_”分隔成几段，分别是模型类型名称，操作类型，模型ID，处理标识，阶段<br>
	 *            其中阶段可以忽略，忽略时表示第一个阶段
	 * @return
	 */
	private ModelRequestInfo getModelRequestInfo(String urlString) {
		ModelRequestInfo modelRequestInfo = new ModelRequestInfo();
		int startPos = urlString.lastIndexOf('/');
		if (startPos < 0) {
			startPos = 0;
		}
		int endPos = urlString.lastIndexOf('.');
		String name = urlString.substring(startPos + 1, endPos);
		modelRequestInfo.setRequestName(name);
		String infos[] = name.split("_");
		if (infos.length < 4) {
			throw new RuntimeException(urlString + "不是一个合法的模型请求参数");
		}
		modelRequestInfo.setModelTypeName(infos[0]);
		modelRequestInfo.setOperationType(infos[1]);
		modelRequestInfo.setModelId(infos[2]);
		modelRequestInfo.setProcessorId(infos[3]);
		if (infos.length == 5) {
			modelRequestInfo.setStageName(infos[4]);
		}
		if (urlString.endsWith(".modellet")) {
			modelRequestInfo.setPagelet(true);
		} else {
			modelRequestInfo.setPagelet(false);
		}
		return modelRequestInfo;
	}

	/**
	 * urlString格式 类型名_模型标识_处理标识_阶段名.model?_pagelet_=true
	 */

	public void reallyProcess(String urlString, WebContext context) {
		logger.logMessage(LogLevel.INFO, "{}开始处理", urlString);
		ModelRequestInfo modelRequestInfo = getModelRequestInfo(urlString);
		checkParameterAndPermission(context, modelRequestInfo);
		try {

			Object model = modelManager.getModel(modelRequestInfo.getModelId());
			context.put("modelDefine", model);
			Object operationDeclare = modelManager.getOperationDeclare(
					modelManager.getModelDefine(modelRequestInfo
							.getModelTypeName()), model, modelRequestInfo
							.getProcessorId());
			context.put("operationDefine", operationDeclare);
			ModelProcessorStage stage = modelManager
					.getModelProcessorStage(modelRequestInfo);
			if (modelRequestInfo.getStageName() == null) {
				modelRequestInfo.setStageName(stage.getName());
			}
			context.put("modelManager", modelManager);
			context.put("jsonConverter", jsonConverter);
			context.put("modelRequestInfo", modelRequestInfo);
			CustomizeStageConfig customizeConfig = modelManager
					.getCustomizeStageConfig(modelManager
							.getModel(modelRequestInfo.getModelId()),
							modelRequestInfo.getProcessorId(), modelRequestInfo
									.getStageName());
			String serviceProcessorBean;
			if (customizeConfig != null
					&& customizeConfig.getServiceProcessorBean() != null
					&& customizeConfig.getServiceProcessorBean().length() > 0) {
				serviceProcessorBean = customizeConfig
						.getServiceProcessorBean();
			} else {
				serviceProcessorBean = stage.getServiceProcessorBean();
			}
			if (serviceProcessorBean != null
					&& serviceProcessorBean.length() > 0) {
				if (stage.isNeedValidate()) {
					Map<String, List<ValidateRule>> validateMap = modelManager
							.getOperationValidateMap(model, operationDeclare);
					if (validateMap.size() > 0) {
						// 如果有校验，则去校验
						ValidateResults results = validateManager.validate(
								validateMap, context);
						if (results.getValidateResultMap().size() > 0) {
							context.put("validateResults", results);
							String path = modelManager.getModelDefine(
									modelRequestInfo.getModelTypeName())
									.getValidateErrorPage();
							forward(context, modelRequestInfo, path);
							return;
						}
					}
				}
				String parameterBuilderBean;
				if (customizeConfig != null
						&& customizeConfig.getParameterBuilderBean() != null
						&& customizeConfig.getParameterBuilderBean().length() > 0) {
					parameterBuilderBean = customizeConfig
							.getParameterBuilderBean();
				} else {
					parameterBuilderBean = stage.getParameterBuilderBean();
				}

				Context newContext = modelManager.processParameter(
						modelRequestInfo, parameterBuilderBean, context);
				CEPCore cepCore = (CEPCore) SpringUtil.getBean("cepcore");

				Event event = getEvent(modelRequestInfo, newContext);
				cepCore.process(event);
				context.putSubContext("serviceResultContext", event
						.getServiceRequest().getContext());
			}

			if (urlString.endsWith(".modeljson")) {// 返回Json
				context.getResponse()
						.getWriter()
						.write(jsonConverter.convert(context
								.get("modelProcessResult")));
			} else {// 返回页面
				if (customizeConfig != null
						&& customizeConfig.getViewPath() != null
						&& customizeConfig.getViewPath().length() > 0) {
					String viewPath = customizeConfig.getViewPath();
					forward(context, modelRequestInfo, viewPath);
				} else {
					modelManager.processView(modelRequestInfo, context
							.getResponse().getWriter(), context);
				}
			}
		} catch (Exception e) {

			logger.error(e);
			context.put("exception", e);
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			e.printStackTrace(new PrintWriter(buf, true));
			context.put("exceptionStackTrace", buf.toString());

			if (urlString.endsWith(".modeljson")) {// 返回Json
				// 如果是调用Json
				ExceptionInfo exceptionInfo = new ExceptionInfo(e.getMessage(),
						buf.toString());
				try {
					context.getResponse().getWriter()
							.write(jsonConverter.convert(exceptionInfo));
				} catch (Exception e2) {
					logger.error(e2);
				}
			} else {// 返回页面
				String path = modelManager.getModelDefine(
						modelRequestInfo.getModelTypeName()).getErrorPage();
				forward(context, modelRequestInfo, path);
			}

		}

		logger.logMessage(LogLevel.INFO, "{}处理结束", urlString);

	}

	private void forward(WebContext context, ModelRequestInfo modelRequestInfo,
			String path) {
		String[] paths = path.split("[?]");
		HttpServletRequest request = context.getRequest();
		if (modelRequestInfo.pagelet && paths[0].endsWith(".page")) {
			paths[0] += "let";
		}
		String p = paths[0];
		if (paths.length == 2) {
			p = p + "?" + paths[1];
		}
		VelocityHelperImpl velocityHelper = SpringUtil
				.getBean("velocityHelper");
		StringWriter out = new StringWriter();
		velocityHelper.evaluteString(context, out, p);
		String forwardPath = out.toString();
		logger.logMessage(LogLevel.INFO, "重新转向到地址：{}", forwardPath);
		try {
			request.getRequestDispatcher(forwardPath).forward(request,
					context.getResponse());
		} catch (Exception e1) {
			logger.error(e1);
		}

	}

	private void checkParameterAndPermission(WebContext context,
			ModelRequestInfo modelRequestInfo) {

		ModelDefine modelDefine = modelManager.getModelDefine(modelRequestInfo
				.getModelTypeName());
		if (modelDefine == null) {
			throw new RuntimeException("模型类型不能为空！");
		}
		if (modelDefine.getModelPermissionCheckerBean() != null) {
			ModelPermissionChecker checker = SpringUtil.getBean(modelDefine
					.getModelPermissionCheckerBean());
			if (!checker.isHasPermission(modelRequestInfo.getModelId(),
					modelRequestInfo.getProcessorId(), context)) {
				throw new RuntimeException("没有权限操作" + modelDefine.getTitle()
						+ "模型" + modelRequestInfo.getModelId() + "的"
						+ modelRequestInfo.getProcessorId() + "操作权限");
			}
		}
	}

	private Event getEvent(ModelRequestInfo modelRequestInfo, Context context) {
		Event event = new Event(UUID.randomUUID().toString()
				.replaceAll("-", ""));
		Context eventContext = ContextFactory.getContext();
		eventContext.put("modelRequestInfo", modelRequestInfo);
		eventContext.put("modelRequestContext", context);
		ServiceRequest serviceRequest = new ServiceRequest();
		event.setServiceRequest(serviceRequest);
		serviceRequest.setServiceId("processModelService");
		serviceRequest.setContext(eventContext);
		return event;
	}

	public String getRequestId(String urlString) {
		int lastDot = urlString.lastIndexOf(".");
		int lastSlash = urlString.lastIndexOf("/");
		return urlString.substring(lastSlash + 1, lastDot);
	}

}
