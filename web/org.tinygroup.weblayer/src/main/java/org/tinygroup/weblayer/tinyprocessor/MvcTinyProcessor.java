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
package org.tinygroup.weblayer.tinyprocessor;

import javax.servlet.http.HttpServletRequest;

import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.weblayer.AbstractTinyProcessor;
import org.tinygroup.weblayer.WebContext;
import org.tinygroup.weblayer.mvc.HandlerExecutionChain;
import org.tinygroup.weblayer.mvc.HandlerMapping;
import org.tinygroup.weblayer.mvc.MappingModelExecute;


/**
 * 
 * 功能说明:tiny-mvc 

 * 开发人员: renhui <br>
 * 开发时间: 2013-4-23 <br>
 * <br>
 */
public class MvcTinyProcessor extends AbstractTinyProcessor {
	
	
	private HandlerMapping handler;
	
	private MappingModelExecute execute;
	
	private static final Logger logger = LoggerFactory
				.getLogger(MvcTinyProcessor.class);

	public HandlerMapping getHandler() {
		return handler;
	}


	public void setHandler(HandlerMapping handler) {
		this.handler = handler;
	}
	
	public MappingModelExecute getExecute() {
		return execute;
	}


	public void setExecute(MappingModelExecute execute) {
		this.execute = execute;
	}


	
	public void init() {
		super.init();
		if(handler==null){
			handler=SpringUtil.getBean("annotationHandlerMapping");
		}
		if(execute==null){
			execute=SpringUtil.getBean("mappingModelExecute");
		}
		handler.init();
	}



	
	public void reallyProcess(String urlString, WebContext context) {
		HttpServletRequest request=context.getRequest();
		logger.logMessage(LogLevel.DEBUG,
					"mvc-tiny-processor processing:[{}] request for [{}]",request.getMethod(),urlString);
		try {
			HandlerExecutionChain chain= handler.getHandler(urlString);
			if(chain!=null){
			  execute.execute(chain, context);
			}else{
				logger.logMessage(LogLevel.ERROR,"请求路径:{}找不到处理映射",urlString);
			}
		} catch (Exception e) {
			logger.errorMessage("tiny-mvc执行出错",e);
			throw new RuntimeException(e);
		}
	}

}
