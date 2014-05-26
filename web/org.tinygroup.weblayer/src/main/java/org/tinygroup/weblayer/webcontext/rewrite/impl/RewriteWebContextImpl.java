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
package org.tinygroup.weblayer.webcontext.rewrite.impl;

import static org.tinygroup.commons.tools.ArrayUtil.defaultIfEmptyArray;
import static org.tinygroup.commons.tools.Assert.assertNotNull;
import static org.tinygroup.commons.tools.BasicConstant.EMPTY_STRING;
import static org.tinygroup.commons.tools.ObjectUtil.defaultIfNull;
import static org.tinygroup.commons.tools.ObjectUtil.isEquals;
import static org.tinygroup.commons.tools.StringUtil.isEmpty;
import static org.tinygroup.commons.tools.StringUtil.trimToNull;
import static org.tinygroup.weblayer.webcontext.rewrite.RewriteUtil.getMatchResultSubstitution;
import static org.tinygroup.weblayer.webcontext.rewrite.RewriteUtil.isFullURL;
import static org.tinygroup.weblayer.webcontext.util.ServletUtil.startsWithPath;
import static org.tinygroup.weblayer.webcontext.util.WebContextUtil.findWebContext;

import java.io.IOException;
import java.util.regex.MatchResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.tinygroup.commons.tools.FileUtil;
import org.tinygroup.commons.tools.MatchResultSubstitution;
import org.tinygroup.commons.tools.StringEscapeUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.weblayer.WebContext;
import org.tinygroup.weblayer.webcontext.AbstractRequestWrapper;
import org.tinygroup.weblayer.webcontext.AbstractWebContextWrapper;
import org.tinygroup.weblayer.webcontext.parser.ParserWebContext;
import org.tinygroup.weblayer.webcontext.parser.valueparser.ParameterParser;
import org.tinygroup.weblayer.webcontext.rewrite.RewriteRule;
import org.tinygroup.weblayer.webcontext.rewrite.RewriteSubstitution;
import org.tinygroup.weblayer.webcontext.rewrite.RewriteSubstitutionContext;
import org.tinygroup.weblayer.webcontext.rewrite.RewriteSubstitutionHandler;
import org.tinygroup.weblayer.webcontext.rewrite.RewriteWebContext;
import org.tinygroup.weblayer.webcontext.util.ServletUtil;

/** 重写URL及参数的request context，类似于apache的mod_rewrite模块。 */
public class RewriteWebContextImpl extends AbstractWebContextWrapper implements RewriteWebContext {
    private final static Logger logger                 = LoggerFactory.getLogger(RewriteWebContext.class);
    public static final  String SERVER_SCHEME_HTTP  = "http";
    public static final  String SERVER_SCHEME_HTTPS = "https";
    public static final  int    SERVER_PORT_HTTP    = 80;
    public static final  int    SERVER_PORT_HTTPS   = 443;
    private final RewriteRule[]        rules;
    private       ParserWebContext parserWebContext;
    private       HttpServletRequest   wrappedRequest;

    /**
     * 包装一个<code>RequestContext</code>对象。
     *
     * @param wrappedContext 被包装的<code>RequestContext</code>
     * @param rewriteConfig  rewrite的配置文件信息
     */
    public RewriteWebContextImpl(WebContext wrappedContext, RewriteRule[] rules) {
        super(wrappedContext);

        this.rules = defaultIfEmptyArray(rules, null);

        // 取得parser request context，以便修改参数
        this.parserWebContext = assertNotNull(findWebContext(wrappedContext, ParserWebContext.class),
                                                  "Could not find ParserRequestContext in request context chain");

        // 保存上一层的request对象，以便取得原来的servletPath、pathInfo之类的信息
        this.wrappedRequest = wrappedContext.getRequest();
    }

    /** 开始一个请求。 */
    public void prepare() {
        if (rules == null) {
            return;
        }

        // 取得servletPath+pathInfo，忽略contextPath
        String originalPath = wrappedRequest.getServletPath()
                              + defaultIfNull(wrappedRequest.getPathInfo(), EMPTY_STRING);
        String path = originalPath;
        boolean parameterSubstituted = false;

            logger.logMessage(LogLevel.DEBUG, "Starting rewrite engine: path=\"{}\"", StringEscapeUtil.escapeJava(path));


        // 开始匹配
        int redirectCode = 0;

        for (RewriteRule rule : rules) {
            MatchResult ruleMatchResult = rule.match(path);
            MatchResult conditionMatchResult = null;
            RewriteSubstitution subs = rule.getSubstitution();

            // 如果匹配，则查看conditions
            if (ruleMatchResult != null) {
                conditionMatchResult = rule.matchConditions(ruleMatchResult, wrappedRequest);
            }

            // 如果C标志被指定，则除非匹配，否则不去判断余下的规则
            boolean chainRule = subs.getSubFlags().hasC();

            if (conditionMatchResult == null) {
                if (chainRule) {
                    break;
                } else {
                    continue;
                }
            }

            // 用rule和condition的匹配结果来替换变量
            MatchResultSubstitution resultSubs = getMatchResultSubstitution(ruleMatchResult, conditionMatchResult);

            // 替换path
            logger.logMessage(LogLevel.DEBUG,"Rule conditions have been satisfied, starting substitution to uri");

            path = subs.substitute(path, resultSubs);

            if (!isFullURL(path)) {
                path = FileUtil.normalizeAbsolutePath(path);
            }

            // 处理parameters
            parameterSubstituted |= subs.substituteParameters(parserWebContext.getParameters(), resultSubs);

            // post substitution处理
            path = firePostSubstitutionEvent(rule, path, parserWebContext, resultSubs);

            // 查看重定向标志
            redirectCode = subs.getSubFlags().getRedirectCode();

            // 如果L标志被指定，则立即结束
            boolean lastRule = subs.getSubFlags().hasL();

            if (lastRule) {
                break;
            }
        }

        // 如果path被改变了，则替换request或重定向
        if (!isEquals(originalPath, path)) {
            // 如果是重定向，则组合出新的URL
            if (redirectCode > 0) {
                StringBuffer uri = new StringBuffer();
                HttpServletRequest request = getRequest();

                if (!isFullURL(path)) {
                    uri.append(request.getScheme()).append("://").append(request.getServerName());

                    boolean isDefaultPort = false;

                    // http和80
                    isDefaultPort |= SERVER_SCHEME_HTTP.equals(request.getScheme())
                                     && request.getServerPort() == SERVER_PORT_HTTP;

                    // https和443
                    isDefaultPort |= SERVER_SCHEME_HTTPS.equals(request.getScheme())
                                     && request.getServerPort() == SERVER_PORT_HTTPS;

                    if (!isDefaultPort) {
                        uri.append(":");
                        uri.append(request.getServerPort());
                    }

                    uri.append(request.getContextPath());
                }

                uri.append(path);

                String queryString = parserWebContext.getParameters().toQueryString();

                if (!isEmpty(queryString)) {
                    uri.append("?").append(queryString);
                }

                String uriLocation = uri.toString();

                try {
                    if (redirectCode == HttpServletResponse.SC_MOVED_TEMPORARILY) {
                        getResponse().sendRedirect(uriLocation);
                    } else {
                        getResponse().setHeader("Location", uriLocation);
                        getResponse().setStatus(redirectCode);
                    }
                } catch (IOException e) {
                	 logger.logMessage(LogLevel.WARN,"Redirect to location \"" + uriLocation + "\" failed", e);
                }
            } else {
                RequestWrapper requestWrapper = new RequestWrapper(wrappedRequest);

                requestWrapper.setPath(path);

                setRequest(requestWrapper);
            }
        } else {
            if (!parameterSubstituted) {
            	 logger.logMessage(LogLevel.TRACE,"No rewrite substitution happend!");
            }
        }
    }

    private String firePostSubstitutionEvent(RewriteRule rule, String path, ParserWebContext parser,
                                             MatchResultSubstitution resultSubs) {
        for (Object handler : rule.handlers()) {
            RewriteSubstitutionContext context = null;

            if (handler instanceof RewriteSubstitutionHandler) {
                if (context == null) {
                    context = new RewriteSubstitutionContextImpl(path, parser, resultSubs);
                }
                logger.logMessage(LogLevel.TRACE,"Processing post-substitution event for \"{}\" with handler: {}",
                              StringEscapeUtil.escapeJava(path), handler);
                

                ((RewriteSubstitutionHandler) handler).postSubstitution(context);

                // path可以被改变
                String newPath = context.getPath();

                if (newPath != null && !isEquals(path, newPath)) {
                
					logger.logMessage(LogLevel.DEBUG,
							"Rewriting \"{}\" to \"{}\"",
							StringEscapeUtil.escapeJava(path),
							StringEscapeUtil.escapeJava(newPath));

				}

				path = newPath;
			}
		}

		return path;
	}

	/** 实现<code>RewriteSubstitutionContext</code>。 */
	private class RewriteSubstitutionContextImpl implements
			RewriteSubstitutionContext {
		private String path;
		private ParserWebContext parser;
		private MatchResultSubstitution resultSubs;

		public RewriteSubstitutionContextImpl(String path,
				ParserWebContext parser, MatchResultSubstitution resultSubs) {
			this.path = path;
			this.parser = parser;
			this.resultSubs = resultSubs;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public ParserWebContext getParserWebContext() {
			return parser;
		}

		public ParameterParser getParameters() {
			return parser.getParameters();
		}

		public MatchResultSubstitution getMatchResultSubstitution() {
			return resultSubs;
		}
	}

	/** 包装request。 */
	private class RequestWrapper extends AbstractRequestWrapper {
		private String path;
		private final boolean prefixMapping;
		private final String originalServletPath;

		public RequestWrapper(HttpServletRequest request) {
			super(RewriteWebContextImpl.this, request);

			// Servlet mapping有两种匹配方式：前缀匹配和后缀匹配。
			// 对于前缀匹配，例如：/turbine/aaa/bbb，servlet path为/turbine，path
			// info为/aaa/bbb
			// 对于后缀匹配，例如：/aaa/bbb.html，servlet path为/aaa/bbb.html，path info为null
			this.prefixMapping = ServletUtil.isPrefixServletMapping(request);
			this.originalServletPath = request.getServletPath();
		}

		public void setPath(String path) {
			this.path = trimToNull(path);
		}

		
		public String getServletPath() {
			if (path == null) {
				return super.getServletPath();
			} else {
				if (prefixMapping) {
					if (startsWithPath(originalServletPath, path)) {
                        return originalServletPath; // 保持原有的servletPath
                    } else {
                        return "";
                    }
                } else {
                    return path;
                }
            }
        }

        
        public String getPathInfo() {
            if (path == null) {
                return super.getPathInfo();
            } else {
                if (prefixMapping) {
                    if (startsWithPath(originalServletPath, path)) {
                        return path.substring(originalServletPath.length()); // 除去servletPath后剩下的部分
                    } else {
                        return path;
                    }
                } else {
                    return null;
                }
            }
        }
    }

}
