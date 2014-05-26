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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.tinygroup.fileresolver.FullContextFileRepository;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.velocity.VelocityHelper;
import org.tinygroup.velocity.impl.VelocityHelperImpl;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.weblayer.WebContext;
import org.tinygroup.weblayer.impl.WebContextImpl;
import org.tinygroup.weblayer.listener.ServletContextHolder;

public class VelocityLayoutViewServlet extends HttpServlet {
	private static final Logger logger = LoggerFactory
			.getLogger(VelocityLayoutViewServlet.class);
	private static final String PAGELET_EXT_FILE_NAME = ".pagelet";
	private static final String PAGE_EXT_FILE_NAME = ".page";
	private String templeteWithLayout = PAGE_EXT_FILE_NAME;
	private String template = PAGELET_EXT_FILE_NAME;
	private VelocityHelper velocityHelper;
	private FullContextFileRepository fullContextFileRepository;

	
	public void init(ServletConfig config) throws ServletException {
		fullContextFileRepository = SpringUtil.getBean(
				"fullContextFileRepository");
		VelocityHelperImpl velocityHelperImpl = SpringUtil
				.getBean("velocityHelper");
		velocityHelper = velocityHelperImpl;
		// 初始化时候对xml文档的内容进行读取 //这个是重写超类方法
		templeteWithLayout = config
				.getInitParameter("templeteWithLayoutExtFileName");
		template = config.getInitParameter("templateExtFileName");
		if (templeteWithLayout == null || templeteWithLayout.length() == 0) {
			templeteWithLayout = PAGE_EXT_FILE_NAME;
		}
		if (template == null || template.length() == 0) {
			template = PAGELET_EXT_FILE_NAME;
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1514050556632003280L;

	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			long startTime = System.currentTimeMillis();
			String servletPath = request.getServletPath();
			if(servletPath==null||servletPath.length()==0){
				servletPath=request.getPathInfo();
			}
			boolean isPagelet = false;
			if (servletPath.endsWith(PAGELET_EXT_FILE_NAME)) {
				isPagelet = true;
				servletPath = servletPath.substring(0, servletPath.length()
						- PAGELET_EXT_FILE_NAME.length())
						+ PAGE_EXT_FILE_NAME;
			}
			FileObject fileObject = fullContextFileRepository
					.getFileObject(servletPath);

			if (fileObject != null) {
				WebContext webContent = new WebContextImpl();
				webContent.put("uiengine",
						SpringUtil.getBean("uiComponentManager"));
				webContent.init(request, response,ServletContextHolder.getServletContext());
				if (isPagelet) {
					velocityHelper.processTempleate(webContent,
							response.getWriter(), servletPath);
				} else {
					velocityHelper.processTempleateWithLayout(webContent,
							response.getWriter(), servletPath);
				}
				long endTime = System.currentTimeMillis();
				logger.logMessage(LogLevel.INFO, "路径<{}>处理时间：{}ms",
						servletPath, endTime - startTime);
				return;
			} else {
				logger.logMessage(LogLevel.ERROR, "路径<{}>对应的资源不能被找到。",
						servletPath);
			}

		} catch (Exception e) {
			logger.errorMessage(e.getMessage(), e);
		} finally {
		}
		response.sendError(HttpServletResponse.SC_NOT_FOUND);
	}
}
