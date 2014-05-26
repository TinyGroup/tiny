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
package org.tinygroup.weblayer.webcontext.rewrite;

import java.util.regex.MatchResult;

import javax.servlet.http.HttpServletRequest;

import org.tinygroup.commons.tools.MatchResultSubstitution;
import org.tinygroup.format.Formater;
import org.tinygroup.format.PatternDefine;
import org.tinygroup.format.impl.DefaultPatternDefine;
import org.tinygroup.format.impl.FormaterImpl;
import org.tinygroup.weblayer.webcontext.parser.ParserWebContext;
import org.tinygroup.weblayer.webcontext.util.WebContextUtil;



/**
 * 和rewrite相关的工具类。
 *
 * @author renhui
 */
public class RewriteUtil {
   private static Formater formater;
   private static RewriteFormatProvider provider;
	static{
		formater=new FormaterImpl();
		provider=new RewriteFormatProvider();
		PatternDefine define=new  DefaultPatternDefine();
		define.setPrefixPatternString("%{");
		define.setPostfixPatternString("}");
		define.setSplitChar('#');
		define.setPatternString("([%]+[{]+[a-zA-Z0-9[.[_[:[/[#]]]]]]+[}])");
		formater.setPatternHandle(define);
		formater.addFormatProvider("", provider);
	}
	
    public static boolean isFullURL(String path) {
        return path.matches("^\\w+:.*");
    }

    public static MatchResultSubstitution getMatchResultSubstitution(MatchResult ruleMatchResult,
                                                                     MatchResult conditionMatchResult) {
        return new MatchResultSubstitution("$%", ruleMatchResult, conditionMatchResult);
    }

    public static String getSubstitutedTestString(String testString, MatchResult ruleMatchResult,
                                                  MatchResult conditionMatchResult, HttpServletRequest request) {
        testString = format(testString, request);

        return getMatchResultSubstitution(ruleMatchResult, conditionMatchResult).substitute(testString);
    }

    public static String format(String testString, HttpServletRequest request) {
    	provider.setRequest(request);
		return formater.format(null, testString);
	}

    /**
     * 展开变量。
     *
     * @return 注意，如果返回null，表示按原样显示，例如：%{XYZ}
     */
    public static String expand(String varName, HttpServletRequest request) {
        boolean valid = true;
        String result;

        // =====================================================
        //  Client side of the IP connection
        // =====================================================

        if ("REMOTE_HOST".equals(varName)) {
            result = request.getRemoteHost();
        }
        //
        else if ("REMOTE_ADDR".equals(varName)) {
            result = request.getRemoteAddr();
        }
        //
        else if ("REMOTE_USER".equals(varName)) {
            result = request.getRemoteUser();
        }
        //
        else if ("REQUEST_METHOD".equals(varName)) {
            result = request.getMethod();
        }
        //
        else if ("QUERY_STRING".equals(varName)) {
            if ("post".equalsIgnoreCase(request.getMethod())) {
                ParserWebContext parserRequestContext = WebContextUtil.findWebContext(request,
                                                                                                 ParserWebContext.class);

                result = parserRequestContext.getParameters().toQueryString();
            } else {
                result = request.getQueryString();
            }
        }
        //
        else if (varName.startsWith("QUERY:")) {
        	ParserWebContext parserRequestContext = WebContextUtil.findWebContext(request,
            		ParserWebContext.class);

            result = parserRequestContext.getParameters().getString(varName.substring("QUERY:".length()).trim());
        }
        //
        else if ("AUTH_TYPE".equals(varName)) {
            result = request.getAuthType();
        }

        // =====================================================
        //  HTTP layer details extracted from HTTP headers
        // =====================================================

        else if ("SERVER_NAME".equals(varName)) {
            result = request.getServerName();
        }
        //
        else if ("SERVER_PORT".equals(varName)) {
            result = String.valueOf(request.getServerPort());
        }
        //
        else if ("SERVER_PROTOCOL".equals(varName)) {
            result = request.getProtocol();
        }

        // =====================================================
        //  HTTP headers
        // =====================================================

        else if ("HTTP_USER_AGENT".equals(varName)) {
            result = request.getHeader("User-Agent");
        }
        //
        else if ("HTTP_REFERER".equals(varName)) {
            result = request.getHeader("Referer");
        }
        //
        else if ("HTTP_HOST".equals(varName)) {
            result = request.getHeader("Host");
        }
        //
        else if ("HTTP_ACCEPT".equals(varName)) {
            result = request.getHeader("Accept");
        }
        //
        else if ("HTTP_COOKIE".equals(varName)) {
            result = request.getHeader("Cookie");
        }

        // =====================================================
        //  Others
        // =====================================================

        else if ("REQUEST_URI".equals(varName)) {
            result = request.getRequestURI();
        } else {
            result = null;
            valid = false;
        }

        // 如果变量合法，但值为null，则返回""
        if (valid && result == null) {
            result = "";
        }

        return result;
    }
}
