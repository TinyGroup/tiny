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

import com.thoughtworks.xstream.XStream;
import org.tinygroup.fileresolver.FullContextFileRepository;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.velocity.VelocityHelper;
import org.tinygroup.velocity.config.VelocityContextConfig;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.weblayer.AbstractTinyProcessor;
import org.tinygroup.weblayer.WebContext;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xstream.XStreamFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class VelocityLayoutViewTinyProcessor extends AbstractTinyProcessor {

	private static final Logger logger = LoggerFactory
			.getLogger(VelocityLayoutViewTinyProcessor.class);
	private static final String PAGELET_EXT_FILE_NAME = ".pagelet";
	private static final String PAGE_EXT_FILE_NAME = ".page";
	private String templeteWithLayout = PAGE_EXT_FILE_NAME;
	private String template = PAGELET_EXT_FILE_NAME;
	private VelocityHelper velocityHelper;
	private FullContextFileRepository fullContextFileRepository;

//	private static final String VELOCITY_CONFIGS = "velocity-context-config";

	
	public void init() {
		super.init();
		fullContextFileRepository = SpringUtil
				.getBean("fullContextFileRepository");
		VelocityHelper velocityHelperImpl = SpringUtil
				.getBean("velocityHelper");
		velocityHelper = velocityHelperImpl;
		// 初始化时候对xml文档的内容进行读取 //这个是重写超类方法
		templeteWithLayout = getInitParamMap().get(
				"templeteWithLayoutExtFileName");
		template = getInitParamMap().get("templateExtFileName");
		if (templeteWithLayout == null || templeteWithLayout.length() == 0) {
			templeteWithLayout = PAGE_EXT_FILE_NAME;
		}
		if (template == null || template.length() == 0) {
			template = PAGELET_EXT_FILE_NAME;
		}
		initVelocityConfig();

	}

	/**
	 * 初始化application.xml文件中配置的velocity属性
	 */
	private void initVelocityConfig() {
		VelocityContextConfiguration configuration=SpringUtil.getBean(VelocityContextConfiguration.class);
		XmlNode velocityConfig=configuration.getCombineNode();
		XStream stream = XStreamFactory
				.getXStream(VelocityHelper.XSTEAM_PACKAGE_NAME);
		if (velocityConfig != null) {
			VelocityContextConfig contextConfig = (VelocityContextConfig) stream
					.fromXML(velocityConfig.toString());
			velocityHelper.setVelocityContextConfig(contextConfig);
		}
	}

	
	public void reallyProcess(String servletPath, WebContext context) {
		HttpServletResponse response = context.getResponse();
		try {
			long startTime = System.currentTimeMillis();
			boolean isPagelet = false;
			if (servletPath.endsWith(PAGELET_EXT_FILE_NAME)) {
				isPagelet = true;
				servletPath = servletPath.substring(0, servletPath.length()
						- PAGELET_EXT_FILE_NAME.length())
						+ PAGE_EXT_FILE_NAME;
			}
			FileObject fileObject = fullContextFileRepository
					.getFileObjectDetectLocale(servletPath);

			if (fileObject != null && fileObject.isExist()) {
				context.put("uiengine",
						SpringUtil.getBean("uiComponentManager"));
				if (isPagelet) {
					velocityHelper.processTempleate(context,
							response.getWriter(), servletPath);
				} else {
					velocityHelper.processTempleateWithLayout(context,
							response.getWriter(), servletPath);
				}
				long endTime = System.currentTimeMillis();
				logger.logMessage(LogLevel.DEBUG, "路径<{}>处理时间：{}ms",
						servletPath, endTime - startTime);
				return;
			} else {
				logger.logMessage(LogLevel.ERROR, "路径<{}>对应的资源不能被找到。",
						servletPath);
			}

		} catch (Exception e) {
			logger.errorMessage(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
		}
		try {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		} catch (IOException e) {
			logger.errorMessage("写入响应信息出错", e);
			throw new RuntimeException(e);
		}

	}

}
