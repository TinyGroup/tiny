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
package org.tinygroup.weblayer.webcontext.basic.interceptor;

import static org.tinygroup.commons.tools.BasicConstant.EMPTY_STRING;
import static org.tinygroup.commons.tools.ObjectUtil.defaultIfNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.tinygroup.commons.tools.HumanReadableSize;
import org.tinygroup.commons.tools.StringEscapeUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.weblayer.webcontext.basic.exception.ResponseHeaderRejectedException;
import org.tinygroup.weblayer.webcontext.util.CookieSupport;

/**
 * 过滤header中的crlf，将status message用HTML entities转义，限制cookie的总大小。
 * 
 * @author renhui
 */
public class ResponseHeaderSecurityFilter implements
		WebContextLifecycleInterceptor, HeaderNameInterceptor,
		HeaderValueInterceptor, CookieInterceptor,
		CookieHeaderValueInterceptor, StatusMessageInterceptor,
		RedirectLocationInterceptor {
	public static final HumanReadableSize MAX_SET_COOKIE_SIZE_DEFAULT = new HumanReadableSize(
			"7k");
	private static final String COOKIE_LENGTH_ATTR = "_COOKIE_LENGTH_";
	private static final Pattern crlf = Pattern.compile("\\r\\n|\\r|\\n");
	private final Logger log = LoggerFactory
			.getLogger(ResponseHeaderSecurityFilter.class);
	private final CookieLengthAccumulator cookieLengthAccumulator;
	private HumanReadableSize maxCookieSize;

	public ResponseHeaderSecurityFilter() {
		this(null);
	}

	public ResponseHeaderSecurityFilter(HttpServletRequest request) {
		if (request == null) {
			cookieLengthAccumulator = new ThreadLocalBasedCookieLengthAccumulator();
		} else {
			cookieLengthAccumulator = new RequestBasedCookieLengthAccumulator(
					request);
		}
	}

	public HumanReadableSize getMaxCookieSize() {
		return maxCookieSize == null || maxCookieSize.getValue() <= 0 ? MAX_SET_COOKIE_SIZE_DEFAULT
				: maxCookieSize;
	}

	public void setMaxCookieSize(HumanReadableSize maxSetCookieSize) {
		this.maxCookieSize = maxSetCookieSize;
	}

	public void prepare() {
	}

	public void commitHeaders() {
		cookieLengthAccumulator.reset();
	}

	public void commit() {
	}

	public String checkHeaderName(String name) {
		if (containsCRLF(name)) {
			String msg = "Invalid response header: "
					+ StringEscapeUtil.escapeJava(name);
			log.logMessage(LogLevel.ERROR, msg);
			throw new ResponseHeaderRejectedException(msg);
		}

		return name;
	}

	public String checkHeaderValue(String name, String value) {
		return defaultIfNull(filterCRLF(value, "header " + name), value);
	}

	public Cookie checkCookie(Cookie cookie) {
		String name = cookie.getName();

		if (containsCRLF(name)) {
			log.logMessage(LogLevel.ERROR, "Invalid cookie name: "
					+ StringEscapeUtil.escapeJava(name));
			return null;
		}

		String value = cookie.getValue();
		String filteredValue = filterCRLF(value, "cookie " + name);

		if (filteredValue == null) {
			return cookie;
		} else {
			CookieSupport newCookie = new CookieSupport(cookie);
			newCookie.setValue(filteredValue);
			return newCookie;
		}
	}

	public String checkCookieHeaderValue(String name, String value,
			boolean setHeader) {
		if (value != null) {
			int maxCookieSize = (int) getMaxCookieSize().getValue();
			int length = cookieLengthAccumulator.getLength();

			if (length + value.length() > maxCookieSize) {
				log.logMessage(
						LogLevel.ERROR,
						"Cookie size exceeds the max value: {} + {} > maxSize {}.  Cookie is ignored: {}",
						new Object[] { length, value.length(),
								getMaxCookieSize(), value });

				return EMPTY_STRING;
			} else {
				if (setHeader) {
					cookieLengthAccumulator.setCookie(value);
				} else {
					cookieLengthAccumulator.addCookie(value);
				}
			}
		}

		return value;
	}

	public String checkStatusMessage(int sc, String msg) {
		return StringEscapeUtil.escapeHtml(msg);
	}

	public String checkRedirectLocation(String location) {
		return defaultIfNull(filterCRLF(location, "redirectLocation"), location);
	}

	private boolean containsCRLF(String str) {
		if (str != null) {
			for (int i = 0; i < str.length(); i++) {
				switch (str.charAt(i)) {
				case '\r':
				case '\n':
					return true;
				}
			}
		}

		return false;
	}

	/** 如果不包含CRLF，则返回<code>null</code>，否则除去所有CRLF，替换成空格。 */
	private String filterCRLF(String value, String logInfo) {
		if (containsCRLF(value)) {
			log.logMessage(LogLevel.WARN, "Found CRLF in {}: {}", logInfo,
					StringEscapeUtil.escapeJava(value));

			StringBuffer sb = new StringBuffer();
			Matcher m = crlf.matcher(value);

			while (m.find()) {
				m.appendReplacement(sb, " ");
			}

			m.appendTail(sb);

			return sb.toString();
		}

		return null;
	}

	private static abstract class CookieLengthAccumulator {
		public final void addCookie(String cookie) {
			setLength(getLength() + cookie.length());
		}

		public final void setCookie(String cookie) {
			setLength(cookie.length());
		}

		public abstract int getLength();

		protected abstract void setLength(int length);

		protected abstract void reset();
	}

	private final class ThreadLocalBasedCookieLengthAccumulator extends
			CookieLengthAccumulator {
		private final ThreadLocal<Integer> cookieLengthHolder = new ThreadLocal<Integer>();

		
		public int getLength() {
			Object value = cookieLengthHolder.get();

			if (value instanceof Integer) {
				return (Integer) value;
			} else {
				return 0;
			}
		}

		
		protected void setLength(int length) {
			cookieLengthHolder.set(length);
		}

		
		protected void reset() {
			cookieLengthHolder.remove();
		}
	}

	private final class RequestBasedCookieLengthAccumulator extends
			CookieLengthAccumulator {
		private final HttpServletRequest request;

		private RequestBasedCookieLengthAccumulator(HttpServletRequest request) {
			this.request = request;
		}

		
		public int getLength() {
			Object value = request.getAttribute(COOKIE_LENGTH_ATTR);

			if (value instanceof Integer) {
				return (Integer) value;
			} else {
				return 0;
			}
		}

		
		protected void setLength(int length) {
			request.setAttribute(COOKIE_LENGTH_ATTR, length);
		}

		
		protected void reset() {
			request.removeAttribute(COOKIE_LENGTH_ATTR);
		}
	}

}
