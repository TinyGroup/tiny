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
package org.tinygroup.weblayer.webcontext;

import static org.tinygroup.commons.tools.Assert.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.tinygroup.weblayer.WebContext;


/**
 * 被<code>WebContext</code>支持的<code>HttpServletRequestWrapper</code>。
 *
 * @author renhui
 */
public abstract class AbstractRequestWrapper extends HttpServletRequestWrapper {
    private WebContext context;

    /**
     * 创建一个request wrapper。
     *
     * @param context request context
     * @param request request
     */
    public AbstractRequestWrapper(WebContext context, HttpServletRequest request) {
        super(request);

        this.context = assertNotNull(context, "requestContext");
    }

    /**
     * 取得当前request所处的servlet context环境。
     *
     * @return <code>ServletContext</code>对象
     */
    public ServletContext getServletContext() {
        return getWebContext().getServletContext();
    }

    /**
     * 取得支持它们的<code>WebContext</code>。
     *
     * @return <code>WebContext</code>实例
     */
    protected WebContext getWebContext() {
        return context;
    }

    /**
     * 取得字符串表示。
     *
     * @return 字符串表示
     */
    
    public String toString() {
        return "Http request within request context: " + getWebContext().toString();
    }
}
