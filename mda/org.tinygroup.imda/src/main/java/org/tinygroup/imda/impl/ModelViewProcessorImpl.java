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

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import org.tinygroup.context.Context;
import org.tinygroup.imda.ModelManager;
import org.tinygroup.imda.config.ModelProcessorDefine;
import org.tinygroup.imda.processor.ModelViewProcessor;
import org.tinygroup.imda.tinyprocessor.ModelRequestInfo;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.weblayer.WebContext;

/**
 * 默认的视图显示类
 * 
 * @author luoguo
 * 
 */
public class ModelViewProcessorImpl implements ModelViewProcessor<Object> {
	Logger logger = LoggerFactory.getLogger(ModelViewProcessor.class);

	public void process(ModelRequestInfo modelRequestInfo, Context context,
			Writer writer) {
		ModelManager modelManager = SpringUtil.getBean("modelManager");
		ModelProcessorDefine processDefine = modelManager
				.getModelProcessorDefine(modelRequestInfo.getModelTypeName(),
						modelRequestInfo.getOperationType());
		WebContext webContext = (WebContext) context;
		HttpServletRequest request = webContext.getRequest();
		String path = getViewPath(modelRequestInfo, processDefine);
		try {
			logger.logMessage(LogLevel.INFO, "重新转向到地址：{}", path);
			request.getRequestDispatcher(path).forward(request,
					webContext.getResponse());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 路径规则:<br>
	 * /model/模型类型/操作类型.page，如果只有一个阶段<br>
	 * /model/模型类型/操作类型_阶段名.page，如果有多个阶段<br>
	 * @param modelRequestInfo
	 * @param processDefine
	 * @return
	 */
	private String getViewPath(ModelRequestInfo modelRequestInfo,
			ModelProcessorDefine processDefine) {
		StringBuffer sb = new StringBuffer("/model/");
		sb.append(modelRequestInfo.getModelTypeName());
		sb.append("/");
		sb.append(modelRequestInfo.getOperationType());
		if (modelRequestInfo.getStageName() != null) {
			if (processDefine.getProcessorStages().size() > 1) {
				sb.append("_");
				sb.append(modelRequestInfo.getStageName());
			}
		}
		if (modelRequestInfo.isPagelet()) {
			sb.append(".pagelet");
		} else {
			sb.append(".page");
		}
		return sb.toString();
	}
}
