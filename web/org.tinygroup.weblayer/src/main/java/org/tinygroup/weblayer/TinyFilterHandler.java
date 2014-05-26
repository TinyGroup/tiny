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

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.tinygroup.commons.tools.Assert;
import org.tinygroup.commons.tools.ExceptionUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.weblayer.exceptionhandler.WebExceptionHandlerManager;
import org.tinygroup.weblayer.webcontext.CommitMonitor;
import org.tinygroup.weblayer.webcontext.SimpleWebContext;
import org.tinygroup.weblayer.webcontext.TwoPhaseCommitWebContext;
import org.tinygroup.weblayer.webcontext.buffered.BufferedWebContext;
import org.tinygroup.weblayer.webcontext.cache.PageCacheWebContext;
import org.tinygroup.weblayer.webcontext.lazycommit.LazyCommitWebContext;
import org.tinygroup.weblayer.webcontext.util.WebContextUtil;

/**
 * 完成filter处理的中间类
 * 
 * @author renhui
 * 
 */
public class TinyFilterHandler {

	private static final Logger logger = LoggerFactory
			.getLogger(TinyFilterHandler.class);
	private TinyFilterManager tinyFilterManager;
	private TinyProcessorManager tinyProcessorManager;
	private FilterChain filterChain;
	private String servletPath;
	private WebContext context;
	/** 在request中保存TinyFilterHandler的键名。 */
	private static final String WEB_CONTEXT_OWNER_KEY = "_web_context_owner_";

	public TinyFilterHandler(String servletPath, FilterChain filterChain,
			WebContext context, TinyFilterManager tinyFilterManager,
			TinyProcessorManager tinyProcessorManager) {
		super();
		this.context = context;
		this.filterChain = filterChain;
		this.servletPath = servletPath;
		this.tinyFilterManager = tinyFilterManager;
		this.tinyProcessorManager = tinyProcessorManager;
	}
	
	
	public WebContext getContext() {
		return context;
	}


	public void setContext(WebContext context) {
		this.context = context;
	}



	/**
	 * 采用tiny-filter方式来处理
	 * 
	 * @param filterChain
	 * @param request
	 * @param response
	 * @param servletPath
	 * @throws Exception
	 */
	public void tinyFilterProcessor(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		logger.logMessage(LogLevel.DEBUG, "请求路径：<{}>", servletPath);
		List<TinyFilter> tinyFilters = tinyFilterManager
				.getTinyFiltersWithUrl(servletPath);
		WebContext wrapperContext = null;
		try {
			wrapperContext = getWebContext(context, tinyFilters, request,
					response);
			// 如果请求已经结束，则不执行进一步的处理。例如，当requestContext已经被重定向了，则立即结束请求的处理。
			if (isRequestFinished(wrapperContext)
					|| isPageCached(wrapperContext)) {
				return;
			}

			if (!tinyProcessorManager.execute(servletPath, wrapperContext)) {
				giveUpControl(wrapperContext);
				return;
			}
			processFlow(wrapperContext);
		} catch (Exception e) {
			logger.errorMessage("执行WebContext处理流程时出错，原因：{}", e, e.getMessage());
			handleException(wrapperContext, e, context.getRequest(),
					context.getResponse());
		} finally {
			postProcess(wrapperContext, tinyFilters);
		}
	}

	private boolean isPageCached(WebContext wrapperContext) {
		PageCacheWebContext pageCacheWebContext = WebContextUtil
				.findWebContext(wrapperContext, PageCacheWebContext.class);
		String accessPath = wrapperContext.get(WebContextUtil.TINY_REQUEST_URI);
		if (pageCacheWebContext != null) {
			return pageCacheWebContext.isCached(accessPath);
		}
		return false;

	}

	private void giveUpControl(WebContext wrapperContext) throws IOException,
			ServletException {
		logger.logMessage(LogLevel.DEBUG,
				"放弃控制，将控制权返回给servlet engine,请求路径：<{}>", servletPath);
		// 1. 关闭buffering
		BufferedWebContext brc = WebContextUtil.findWebContext(wrapperContext,
				BufferedWebContext.class);
		if (brc != null) {
			try {
				brc.setBuffering(false);
			} catch (IllegalStateException e) {
				// getInputStream或getWriter已经被调用了，不能更改buffering参数。
			}
		}
		// 2. 取消contentType的设置
		try {
			wrapperContext.getResponse().setContentType(null);
		} catch (Exception e) {
			// ignored, 有可能有的servlet engine不支持null参数
		}
		filterChain.doFilter(wrapperContext.getRequest(),
				wrapperContext.getResponse());
	}

	private WebContext getWebContext(WebContext context,
			List<TinyFilter> tinyFilters, HttpServletRequest request,
			HttpServletResponse response) {
		WebContext webContext = WebContextUtil.getWebContext(context
				.getRequest());
		if (webContext == null) {
			webContext = createWrapperContext(context, tinyFilters, request,
					response);
			context.getRequest().setAttribute(WEB_CONTEXT_OWNER_KEY, this);
		}
		return webContext;
	}

	private WebContext createWrapperContext(WebContext context,
			List<TinyFilter> tinyFilters, HttpServletRequest request,
			HttpServletResponse response) {
		SimpleWebContext innerWebContext = new SimpleWebContext(context, this,request,response);
		WebContext wrapperedContext = innerWebContext;
		WebContextUtil.setWebContext(wrapperedContext);
		logger.logMessage(LogLevel.DEBUG, "tiny-filter开始进行前置处理操作");
		for (TinyFilter filter : tinyFilters) {
			wrapperedContext = filter.wrapContext(wrapperedContext);
			if (filter != null) {
				logger.logMessage(LogLevel.DEBUG, "tiny-filter<{}>进行前置处理",
						filter.getClass().getName());
				filter.preProcess(wrapperedContext);
				WebContextUtil.setWebContext(wrapperedContext);
			}
		}
		innerWebContext.setTopWebContext(wrapperedContext);
		logger.logMessage(LogLevel.DEBUG, "tiny-filter前置处理操作结束");
		logger.logMessage(LogLevel.DEBUG, "Created a new web context: {}",
				wrapperedContext);
		return wrapperedContext;
	}

	/**
	 * 处理异常e的过程
	 * 
	 * @param e
	 * @param webContextStack
	 */
	private void handleException(WebContext webContext, Throwable e,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (webContext != null) {
			request = webContext.getRequest();
			response = webContext.getResponse();
		}
		try {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} catch (Exception ee) {
			// ignore this exception
		}
		clearBuffer(response);
		// 记录异常日志信息
		Throwable rootCause = ExceptionUtil.getRootCause(e);
		String originalExceptionMessage = rootCause.getClass().getSimpleName()
				+ ": " + rootCause.getMessage();
		logger.errorMessage("Full stack trace of the error "
				+ originalExceptionMessage, e);
		// 进行自定义异常处理
		WebExceptionHandlerManager exceptionHandlerManager = SpringUtil
				.getBean(WebExceptionHandlerManager.MANAGER_BEAN);
		exceptionHandlerManager.handler(rootCause, webContext);
	}

	/**
	 * 清除buffer
	 * 
	 * @param response
	 */
	private void clearBuffer(HttpServletResponse response) {
		if (!response.isCommitted()) {
			response.resetBuffer();
		}
	}

	/**
	 * 判断请求是否已经结束。如果请求被重定向了，则表示请求已经结束。
	 * 
	 * @param webContext
	 * @return
	 */
	private boolean isRequestFinished(WebContext webContext) {
		LazyCommitWebContext lcrc = WebContextUtil.findWebContext(webContext,
				LazyCommitWebContext.class);
		return lcrc != null && lcrc.isRedirected();
	}

	private void processFlow(WebContext context) {
		// TODO 添加此代码
		// if (contextProcessFlowArray != null) {
		// FlowExecutor flowExecutor = SpringUtil.getBean(
		// FlowExecutor.FLOW_BEAN);
		// for (String flowName : contextProcessFlowArray) {
		// flowExecutor.execute(flowName, context);
		// }
		// }
	}

	private void postProcess(WebContext webContext, List<TinyFilter> tinyFilters) {
		if (webContext == null) {
			return;
		}
		try {
			if (this == webContext.getRequest().getAttribute(
					WEB_CONTEXT_OWNER_KEY)) {
				webContext.getRequest().removeAttribute(WEB_CONTEXT_OWNER_KEY);
				commitWebRequest(webContext, tinyFilters);
			}
		} catch (Exception e) {
			logger.errorMessage("Exception occurred while commit rundata", e);
		}

	}

	/**
	 * 
	 * 提交web请求操作
	 * 
	 * @param webContext
	 * @param tinyFilters
	 */
	private void commitWebRequest(WebContext webContext,
			List<TinyFilter> tinyFilters) {
		logger.logMessage(LogLevel.DEBUG, "tiny-filter开始进行后置处理操作");
		CommitMonitor monitor = getCommitMonitor(webContext);
		synchronized (monitor) {
			if (!monitor.isCommitted()) {
				boolean doCommitHeaders = !monitor.isHeadersCommitted();
				monitor.setCommitted(true);
				HttpServletRequest request = webContext.getRequest();
				int size = tinyFilters.size() - 1;
				WebContext wrapperedContext = webContext;
				for (int i = size; i >= 0; i--) {
					TinyFilter filter = tinyFilters.get(i);
					if (filter != null) {
						logger.logMessage(LogLevel.DEBUG,
								"tiny-filter<{}>进行后置处理", filter.getClass()
										.getName());
						if (wrapperedContext instanceof TwoPhaseCommitWebContext
								&& doCommitHeaders) {
							((TwoPhaseCommitWebContext) wrapperedContext)
									.commitHeaders();
						}
						filter.postProcess(wrapperedContext);
					}
					wrapperedContext = wrapperedContext.getWrappedWebContext();

				}
				// 将request和requestContext断开
				WebContextUtil.removeWebContext(request);
				logger.logMessage(LogLevel.DEBUG, "Committed request: {}",
						request);
			}
		}
		logger.logMessage(LogLevel.DEBUG, "tiny-filter后置处理操作结束");
	}

	public String getServletPath() {
		return servletPath;
	}

	/**
	 * 
	 * 头部提交，保证内容只提交一次
	 * 
	 * @param wrapperedContext
	 */
	public void commitHeaders(WebContext wrapperedContext) {
		CommitMonitor monitor = getCommitMonitor(wrapperedContext);

		synchronized (monitor) {
			if (!monitor.isHeadersCommitted()) {
				monitor.setHeadersCommitted(true);

				for (WebContext rc = wrapperedContext; rc != null; rc = rc
						.getWrappedWebContext()) {
					if (rc instanceof TwoPhaseCommitWebContext) {
						TwoPhaseCommitWebContext tpc = (TwoPhaseCommitWebContext) rc;
						logger.logMessage(LogLevel.TRACE,
								"Committing headers: {}", tpc.getClass()
										.getSimpleName());
						tpc.commitHeaders();
					}
				}
			}
		}

	}

	private CommitMonitor getCommitMonitor(WebContext webContext) {
		CommitMonitor monitor = WebContextUtil.findWebContext(webContext,
				SimpleWebContext.class);
		return Assert.assertNotNull(monitor, "no monitor");
	}

}
