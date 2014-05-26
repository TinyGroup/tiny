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
package org.tinygroup.entity.engine;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import org.tinygroup.context.Context;
import org.tinygroup.entity.BaseModel;
import org.tinygroup.entity.common.Operation;
import org.tinygroup.entity.common.View;
import org.tinygroup.imda.ModelManager;
import org.tinygroup.imda.processor.ModelViewProcessor;
import org.tinygroup.imda.tinyprocessor.ModelRequestInfo;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.weblayer.WebContext;

public class EntitiesModelViewProcessor implements
		ModelViewProcessor<BaseModel> {
	Logger logger = LoggerFactory.getLogger(EntitiesModelViewProcessor.class);

	public void process(ModelRequestInfo modelRequestInfo, Context context,
			Writer writer) {
		// 上下文放入模型
		ModelManager modelManager = SpringUtil.getBean("modelManager");
		BaseModel model = modelManager.getModel(modelRequestInfo.getModelId());
		context.put("_modelDefine_", model);
		Object processDefine = getProcessDefine(model,
				modelRequestInfo.getProcessorId());
		context.put("_processDefine", processDefine);
		WebContext webContext = (WebContext) context;
		HttpServletRequest request = webContext.getRequest();
		String path = getViewPath(modelRequestInfo, processDefine);
		try {
			logger.logMessage(LogLevel.INFO, "重新转身到地址：{}", path);
			request.getRequestDispatcher(path).forward(request,
					webContext.getResponse());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String getViewPath(ModelRequestInfo modelRequestInfo,
			Object processDefine) {
		StringBuffer sb = new StringBuffer(modelRequestInfo.getRequestName());
		sb.append("_");
		if (processDefine instanceof View) {
			sb.append("view_");
			View view = (View) processDefine;
			sb.append(view.getType());
		}
		if (processDefine instanceof Operation) {
			sb.append("operation_");
			Operation operation = (Operation) processDefine;
			sb.append(operation.getType());
		}
		if (modelRequestInfo.getStageName() != null) {
			sb.append("_");
			sb.append(modelRequestInfo.getStageName());
		}
		if (modelRequestInfo.isPagelet()) {
			sb.append(".pagelet");
		} else {
			sb.append(".page");
		}
		return sb.toString();
	}

	private Object getProcessDefine(BaseModel model, String processorName) {
		if (model.getOperations() != null) {
			for (Operation operation : model.getOperations()) {
				if (operation.getId().equals(processorName)) {
					return operation;
				}
			}
		}
		if (model.getViews() != null) {
			for (View view : model.getViews()) {
				if (view.getId().equals(processorName)) {
					return view;
				}
			}
		}
		throw new RuntimeException(model.getTitle() + "中找不到" + processorName
				+ "对应的操作或视图。");
	}

}
