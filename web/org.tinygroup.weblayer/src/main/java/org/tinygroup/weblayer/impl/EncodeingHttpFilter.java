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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 设置编码格式的httpfilter实现方式
 * 
 */
public class EncodeingHttpFilter implements Filter {

	private static final Logger logger = LoggerFactory
			.getLogger(EncodeingHttpFilter.class);
	private List<String> extNames = new ArrayList<String>();
	private String encoding;// 编码类型
	private boolean enable = true;// 是否启用
	private String contentType;// 媒体类型

	public List<String> getExtNames() {
		return extNames;
	}

	public void setExtNames(List<String> extNames) {
		this.extNames = extNames;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		extNames.addAll(Arrays.asList("page,pagelet,html,htm,css,flow,js"
				.split(",")));
	}

	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		try {
			if (encoding != null && enable) {
				String uri = request.getRequestURI();
				for (String extName : extNames) {
					if (uri.endsWith("." + extName)) {
						request.setCharacterEncoding(encoding);
						logger.logMessage(LogLevel.DEBUG, "编码设置为：{}", encoding);
						response.setContentType(contentType);
						break;
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			logger.errorMessage("设置编码时出错。", e);
		}
		chain.doFilter(request, response);
	}

	public void destroy() {

	}

}
