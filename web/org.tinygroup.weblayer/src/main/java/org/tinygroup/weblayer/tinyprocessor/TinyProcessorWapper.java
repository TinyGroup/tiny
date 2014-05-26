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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.weblayer.AbstractTinyProcessor;
import org.tinygroup.weblayer.WebContext;
import org.tinygroup.weblayer.impl.TinyServletConfig;

/**
 * 把Servlet包装成TinyProcessor
 * 
 * @author luoguo
 * 
 */
public class TinyProcessorWapper extends AbstractTinyProcessor {
	private HttpServlet servlet;
	private static final Logger logger = LoggerFactory
			.getLogger(TinyProcessorWapper.class);

	
	public void init() {
		super.init();
		String servletBeanName = getInitParamMap().get(
				TinyServletConfig.SERVLET_BEAN);
		if (servletBeanName != null) {
			servlet = SpringUtil.getBean(servletBeanName);
			if (servlet != null) {
				try {
					servlet.init(new TinyServletConfig(getInitParamMap()));
				} catch (ServletException e) {
					logger.errorMessage("初始化servlet:{}出错", e, servletBeanName);
					throw new RuntimeException("初始化servlet出错", e);
				}
			} else {
				throw new RuntimeException("找不到bean名称：{}对应的servlet");
			}
		}
	}

	
	public void reallyProcess(String urlString, WebContext context) {

		HttpServletRequest request = context.getRequest();
		HttpServletResponse response = context.getResponse();
		try {
			servlet.service(request, response);
		} catch (Exception e) {
			logger.errorMessage("servlet:{}执行出错", e, servlet.getServletName());
			throw new RuntimeException("servlet执行出错", e);
		}

	}

	
	public void destory() {
		super.destory();
		servlet.destroy();
	}

	
}
