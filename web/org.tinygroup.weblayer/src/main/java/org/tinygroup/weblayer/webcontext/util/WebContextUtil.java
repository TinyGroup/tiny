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
package org.tinygroup.weblayer.webcontext.util;

import javax.servlet.http.HttpServletRequest;

import org.tinygroup.weblayer.WebContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 和<code>WebContext</code>相关的辅助类。
 *
 * @author renhui
 */
public class WebContextUtil {
    private static final String REQUEST_CONTEXT_KEY = "_outer_webx3_request_context_";
    public static final String TINY_CONTEXT_PATH="TINY_CONTEXT_PATH";
    public static final String TINY_REQUEST_URI="TINY_REQUEST_URI";
    public static final String TINY_SERVLET_PATH="TINY_SERVLET_PATH";

    /**
     * 取得和当前request相关联的<code>WebContext</code>对象。
     *
     * @param request 要检查的request
     * @return <code>WebContext</code>对象，如果没找到，则返回<code>null</code>
     */
    public static WebContext getWebContext(HttpServletRequest request) {
        return (WebContext) request.getAttribute(REQUEST_CONTEXT_KEY);
    }

    /**
     * 将<code>WebContext</code>对象和request相关联。
     *
     * @param WebContext <code>WebContext</code>对象
     */
    public static void setWebContext(WebContext WebContext) {
        HttpServletRequest request = WebContext.getRequest();
        request.setAttribute(REQUEST_CONTEXT_KEY, WebContext);
    }
    
    /** 将<code>RequestContext</code>对象和request脱离关联。 */
    public static void removeWebContext(HttpServletRequest request) {
        request.removeAttribute(REQUEST_CONTEXT_KEY);
    }

    /**
     * 在指定的request context及其级联的request context中找到一个指定类型的request context。
     *
     * @param request                 从该<code>HttpServletRequest</code>中取得request context
     * @param WebContextInterface 要查找的类
     * @return <code>WebContext</code>对象，如果没找到，则返回<code>null</code>
     */
    public static <R extends WebContext> R findWebContext(HttpServletRequest request, Class<R> WebContextInterface) {
        return findWebContext(getWebContext(request), WebContextInterface);
    }

    /**
     * 在指定的request context及其级联的request context中找到一个指定类型的request context。
     *
     * @param webContext          要搜索的request context
     * @param webContextInterface 要查找的类
     * @return <code>WebContext</code>对象，如果没找到，则返回<code>null</code>
     */
    public static <R extends WebContext> R findWebContext(WebContext webContext, Class<R> webContextInterface) {
        do {
            if (webContextInterface.isInstance(webContext)) {
                break;
            }

            webContext = webContext.getWrappedWebContext();
        } while (webContext != null);

        return webContextInterface.cast(webContext);
    }

    /**
     * 注册spring <code>ServletRequestAttributes</code>中的析构回调方法，这些方法将在request
     * context被提交之后依次调用。
     */
    public static void registerRequestDestructionCallback(String name, Runnable callback) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        attributes.registerDestructionCallback(name, callback, RequestAttributes.SCOPE_REQUEST);
    }
}
