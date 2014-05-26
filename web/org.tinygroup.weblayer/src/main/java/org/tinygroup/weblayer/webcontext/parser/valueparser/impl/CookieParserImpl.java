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
package org.tinygroup.weblayer.webcontext.parser.valueparser.impl;

import static org.tinygroup.commons.tools.BasicConstant.EMPTY_STRING;
import static org.tinygroup.commons.tools.StringUtil.defaultIfEmpty;
import static org.tinygroup.commons.tools.StringUtil.split;

import javax.servlet.http.Cookie;

import org.tinygroup.logger.LogLevel;
import org.tinygroup.weblayer.webcontext.parser.ParserWebContext;
import org.tinygroup.weblayer.webcontext.parser.valueparser.AbstractValueParser;
import org.tinygroup.weblayer.webcontext.parser.valueparser.CookieParser;

/**
 * <code>CookieParser</code>是用来解析和添加HTTP请求中的cookies的接口。
 * <p>
 * 注意：<code>CookieParser</code>永远使用<code>ISO-8859-1</code>编码来处理cookie的名称和值。
 * </p>
 * 
 * @author renhui
 */
public class CookieParserImpl extends AbstractValueParser implements
		CookieParser {

	/** 从request中创建新的cookies。 */
	public CookieParserImpl(ParserWebContext webContext) {
		super(webContext);

		Cookie[] cookies = webContext.getRequest().getCookies();

		if (cookies != null) {
			logger.logMessage(LogLevel.DEBUG, "Number of Cookies {}",
					cookies.length);

			for (Cookie cookie : cookies) {
				String name = cookie.getName();
				String value = cookie.getValue();

				logger.logMessage(LogLevel.DEBUG, "Adding {}  =  {}", name,
						value);

				add(name, value);
			}
		}
	}

	/**
	 * Set a cookie that will be stored on the client for the duration of the
	 * session.
	 */
	public void setCookie(String name, String value) {
		setCookie(name, value, AGE_SESSION);
	}

	/**
	 * Set a persisten cookie on the client that will expire after a maximum age
	 * (given in seconds).
	 */
	public void setCookie(String name, String value, int seconds_age) {
		Cookie cookie = new Cookie(name, value);

		// 设置cookie作用时间、domain和path。
		cookie.setMaxAge(seconds_age);
		cookie.setDomain(getCookieDomain());
		cookie.setPath(getCookiePath());

		webContext.getResponse().addCookie(cookie);
	}

	/**
	 * 取得cookie的domain。
	 * 
	 * @return cookie的domain
	 */
	protected String getCookieDomain() {
		String domain = defaultIfEmpty(webContext.getRequest().getServerName(),
				EMPTY_STRING);
		String[] parts = split(domain, ".");
		int length = parts.length;

		if (length < 2) {
			return domain;
		} else {
			// 只取最后两部分，这是最普遍的情形
			return "." + parts[length - 2] + "." + parts[length - 1];
		}
	}

	/**
	 * 取得cookie的path。
	 * 
	 * @return cookie的path
	 */
	protected String getCookiePath() {
		return defaultIfEmpty(webContext.getRequest().getContextPath(), "/");
	}

	/** Remove a previously set cookie from the client machine. */
	public void removeCookie(String name) {
		setCookie(name, " ", AGE_DELETE);
	}
}
