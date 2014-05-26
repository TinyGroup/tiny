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
package org.tinygroup.pageflowbasiccomponent;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.tinygroup.context.Context;
import org.tinygroup.fileresolver.FullContextFileRepository;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.velocity.VelocityHelper;
import org.tinygroup.velocity.impl.VelocityHelperImpl;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.weblayer.WebContext;

public class FullContextUrlRedirect implements ComponentInterface {
	private final static Logger logger = LoggerFactory
			.getLogger(FullContextUrlRedirect.class);
	private String path;
	private static final String PAGELET_EXT_FILE_NAME = ".pagelet";
	private static final String PAGE_EXT_FILE_NAME = ".page";
	private String templeteWithLayout = PAGE_EXT_FILE_NAME;
	private String template = PAGELET_EXT_FILE_NAME;
	private FullContextFileRepository fullContextFileRepository;
	private VelocityHelper velocityHelper;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}


	public void init() {
		fullContextFileRepository = SpringUtil
				.getBean("fullContextFileRepository");
		VelocityHelperImpl velocityHelperImpl = SpringUtil
				.getBean("velocityHelper");
		velocityHelperImpl
				.setFullContextFileRepository(fullContextFileRepository);
		velocityHelper = velocityHelperImpl;
		// 初始化时候对xml文档的内容进行读取 //这个是重写超类方法
		if (templeteWithLayout == null || templeteWithLayout.length() == 0) {
			templeteWithLayout = PAGE_EXT_FILE_NAME;
		}
		if (template == null || template.length() == 0) {
			template = PAGELET_EXT_FILE_NAME;
		}
	}

	public void doExecute(Context context) throws FileNotFoundException,
			IOException, Exception {

		boolean isPagelet = false;
		if (path.endsWith(PAGELET_EXT_FILE_NAME)) {
			isPagelet = true;
			path = path.substring(0,
					path.length() - PAGELET_EXT_FILE_NAME.length())
					+ PAGE_EXT_FILE_NAME;
		}
		FileObject fileObject = fullContextFileRepository.getFileObject(path);
		WebContext webContent = null;
		if (context instanceof WebContext)
			webContent = (WebContext) context;
		else
			return;
		if (fileObject != null&&fileObject.isExist()) {
			webContent
					.put("uiengine", SpringUtil.getBean("uiComponentManager"));

			if (isPagelet) {
				velocityHelper.processTempleate(webContent, webContent
						.getResponse().getWriter(), path);
			} else {
				velocityHelper.processTempleateWithLayout(webContent,
						webContent.getResponse().getWriter(), path);
			}
			return;
		} else {
			webContent.getResponse()
					.sendError(HttpServletResponse.SC_NOT_FOUND);
		}

	}

	public void execute(Context context) {

		try {
			init();
			doExecute(context);
		} catch (Exception e) {
			logger.errorMessage("页面跳转出错", e);
		}
	}

}
