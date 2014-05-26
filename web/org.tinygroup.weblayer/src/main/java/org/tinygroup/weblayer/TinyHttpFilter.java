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
package org.tinygroup.weblayer;

import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.weblayer.impl.WebContextImpl;
import org.tinygroup.weblayer.listener.ServletContextHolder;
import org.tinygroup.weblayer.webcontext.util.WebContextUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TinyHttpFilter implements Filter {
	private static final String EXCLUDE_PATH = "excludePath";
	private static final Logger logger = LoggerFactory
			.getLogger(TinyHttpFilter.class);
	private TinyProcessorManager tinyProcessorManager;
	private TinyFilterManager tinyFilterManager;

	private List<Pattern> excludePatterns = new ArrayList<Pattern>();

	private FilterWrapper wrapper;

	public void destroy() {
		destoryTinyProcessors();
		destoryTinyFilters();
		wrapper = null;
	}

	private void destoryTinyFilters() {
		tinyFilterManager.destoryTinyResources();
		tinyFilterManager = null;
	}

	/**
	 * 销毁tiny-processors
	 */
	private void destoryTinyProcessors() {
		tinyProcessorManager.destoryTinyResources();
		tinyProcessorManager = null;
	}

	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		WebContext context = new WebContextImpl();
		context.put("springUtil", SpringUtil.class);
		context.put("context", context);
		context.putSubContext("applicationproperties", new ContextImpl(ConfigurationUtil.getConfigurationManager().getApplicationPropertiesMap()));
		putRequstInfo(request, context);

		context.init(request, response,
				ServletContextHolder.getServletContext());
		String servletPath = context.get(WebContextUtil.TINY_SERVLET_PATH);
        if(servletPath.endsWith("/")){
            servletPath=servletPath+"index.page";
        }
		if (isExcluded(servletPath)) {
			logger.logMessage(LogLevel.DEBUG, "请求路径:<{}>,被拒绝", servletPath);
			filterChain.doFilter(request, response);
			return;
		}
		TinyFilterHandler hander = new TinyFilterHandler(servletPath,
				filterChain, context, tinyFilterManager, tinyProcessorManager);
		if (wrapper != null) {
			wrapper.filterWrapper(context, hander);
		} else {
			hander.tinyFilterProcessor(request,response);
		}
	}

	private void putRequstInfo(HttpServletRequest request, WebContext context) {
		context.put(WebContextUtil.TINY_CONTEXT_PATH, request.getContextPath());
		context.put(WebContextUtil.TINY_REQUEST_URI, request.getRequestURI());
		String servletPath = request.getServletPath();
		if (servletPath == null || servletPath.length() == 0) {
			servletPath = request.getPathInfo();
		}
		context.put(WebContextUtil.TINY_SERVLET_PATH, servletPath);
	}

	/**
	 * 请求是否被排除
	 * 
	 * @param servletPath
	 * @return
	 */
	private boolean isExcluded(String servletPath) {
		for (Pattern pattern : excludePatterns) {
			if (pattern.matcher(servletPath).matches()) {
				return true;
			}
		}
		return false;
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		logger.logMessage(LogLevel.INFO, "filter初始化开始...");

		initExcludePattern(filterConfig);

		initTinyFilters();

		initTinyFilterWrapper();

		initTinyProcessors();
		logger.logMessage(LogLevel.INFO, "filter初始化结束...");

	}

	private void initTinyFilterWrapper() {
		wrapper = tinyFilterManager.getFilterWrapper();
	}

	private void initExcludePattern(FilterConfig filterConfig) {
		excludePatterns.clear();//先清空
		String excludePath = filterConfig.getInitParameter(EXCLUDE_PATH);
		if (excludePath != null) {
			String[] excludeArray = excludePath.split(",");
			for (String path : excludeArray) {
				excludePatterns.add(Pattern.compile(path));
			}
		}
	}

	private void initTinyFilters() {
		tinyFilterManager = SpringUtil
				.getBean(TinyFilterManager.TINY_FILTER_MANAGER);
		tinyFilterManager.initTinyResources();
	}

	/**
	 * tiny-processors初始化
	 */
	private void initTinyProcessors() {
		tinyProcessorManager = SpringUtil
				.getBean(TinyProcessorManager.TINY_PROCESSOR_MANAGER);
		tinyProcessorManager.initTinyResources();
	}
}
