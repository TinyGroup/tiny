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
package org.tinygroup.weblayer.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.weblayer.TinyFilterHandler;
import org.tinygroup.weblayer.WebContext;

/**
 * 过滤器链接口的默认实现，不再往下传递调用,直接返回
 * 
 * @author renhui
 * 
 */
public class TinyFilterChain implements FilterChain {
	private List<Filter> filters = new ArrayList<Filter>();
	private int size;
	private int currentPosition = 0;
	private static final Logger logger = LoggerFactory
			.getLogger(TinyFilterChain.class);

	private TinyFilterHandler hander;

	public TinyFilterChain(List<Filter> filters, TinyFilterHandler hander) {
		this.filters = filters;
		size = filters.size();
		this.hander = hander;
	}

	public void doFilter(ServletRequest request, ServletResponse response)
			throws IOException, ServletException {
		if (currentPosition <size) {
			Filter nextFilter = filters.get(currentPosition);
			logger.logMessage(LogLevel.DEBUG, "firing Filter:'{}'", nextFilter
					.getClass().getSimpleName());
			currentPosition++;
			nextFilter.doFilter(request, response, this);
		} else {
			initWebContext(request, response);//重新初始化webcontext中保存的request和response对象
			hander.tinyFilterProcessor((HttpServletRequest)request,(HttpServletResponse)response);
		}
	}

	private void initWebContext(ServletRequest request, ServletResponse response) {
		WebContext webContext=hander.getContext();
		webContext.setRequest((HttpServletRequest)request);
		webContext.setResponse((HttpServletResponse)response);
	}

}
