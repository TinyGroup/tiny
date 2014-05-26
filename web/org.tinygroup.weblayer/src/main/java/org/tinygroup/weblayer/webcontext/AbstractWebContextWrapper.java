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

import static org.tinygroup.commons.tools.Assert.assertNotNull;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.tinygroup.commons.tools.ObjectUtil;
import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.weblayer.WebContext;

/**
 * 功能说明:抽象的包装上下文,类属性值都是从被包装的上下文对象中获得
 * <p/>

 * 开发人员: renhui <br>
 * 开发时间: 2013-4-28 <br>
 * <br>
 */
public abstract class AbstractWebContextWrapper extends ContextImpl implements WebContext {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private ServletContext servletContext;
    private WebContext wrappedContext;

    private Map<String, WebContextScope> scopes = new HashMap<String, WebContextScope>();

    {
        scopes.put("requestScope", new RequestScope());
        scopes.put("sessionScope", new SessionScope());
        scopes.put("applicationScope", new ApplicationScope());
    }

    public AbstractWebContextWrapper() {

    }

    public AbstractWebContextWrapper(WebContext wrappedContext) {
        assertNotNull(wrappedContext, "wrappedContext");
        this.wrappedContext = wrappedContext;
        this.request = wrappedContext.getRequest();
        this.response = wrappedContext.getResponse();
        this.servletContext = wrappedContext.getServletContext();
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
        super.put("httpServletResponse", response);
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    @SuppressWarnings("unchecked")
    private <T> T findInRequset(String name) {
        if (request != null) {
            T result = (T) request.getAttribute(name);
            if (!ObjectUtil.isEmptyObject(result))
                return result;
            result = (T) request.getParameterValues(name);
            if (!ObjectUtil.isEmptyObject(result)) {
                if (result.getClass().isArray()) {// 处理字符串数组的问题
                    Object[] temp = (Object[]) result;
                    if (temp.length == 1) {
                        result = (T) temp[0];
                    }
                }
            }
            if (!ObjectUtil.isEmptyObject(result))
                return result;

            result = (T) request.getSession().getAttribute(name);
            if (!ObjectUtil.isEmptyObject(result))
                return result;
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if (cookie.getName().equals(name)) {
                        result = (T) cookie.getValue();
                        return result;
                    }
                }
            }
            result = (T) request.getHeader(name);
            if (!ObjectUtil.isEmptyObject(result)) {
                return result;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private boolean existInRequset(String name) {
        if (request != null) {
        	Enumeration<String> enumer =  request.getAttributeNames();
        	while(enumer.hasMoreElements()){
        		if(enumer.nextElement().equals(name)){
        			return true;
        		}
        	}
            Map parameterMap =  request.getParameterMap();
            if(parameterMap.containsKey(name)){
            	return true;
            }
            enumer= request.getSession().getAttributeNames();
            while(enumer.hasMoreElements()){
        		if(enumer.nextElement().equals(name)){
        			return true;
        		}
        	}
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if (cookie.getName().equals(name)) {
                        return true;
                    }
                }
            }
            enumer= request.getHeaderNames();
            while(enumer.hasMoreElements()){
        		if(enumer.nextElement().equals(name)){
        			return true;
        		}
        	}
        }
        return false;
    }
    
    
    
    /**
     * 改写get方法，使得可以从父环境中查找，同时，也可以从子环境中查找 先找自己，再找子，再找父
     */
    public <T> T get(String name) {
        T result = (T) getFromWrapperContext(name, this);
        if (!ObjectUtil.isEmptyObject(result)) {
            return result;
        }
        result = (T) findInRequset(name);
        if (!ObjectUtil.isEmptyObject(result)) {
            return result;
        }
        return null;

    }
    
	public boolean exist(String name) {
		boolean exist=existFromWrapperContext(name, this);
		if(exist){
			return true;
		}
		return existInRequset(name);
	}

	protected <T> T getFromWrapperContext(String name, WebContext webContext) {
        T result = (T) getFromSubContext(name, webContext);
        if (!ObjectUtil.isEmptyObject(result))
            return result;
        if (webContext.getWrappedWebContext() != null) {
            result = (T) getFromWrapperContext(name, webContext.getWrappedWebContext());
            if (!ObjectUtil.isEmptyObject(result)) {
                return result;
            }
        }
        return null;
    }
	
	protected boolean existFromWrapperContext(String name, WebContext webContext) {
        boolean exist= existFromSubContext(name, webContext);
        if (exist){
            return true;	
        }
        if (webContext.getWrappedWebContext() != null) {
        	exist = existFromWrapperContext(name, webContext.getWrappedWebContext());
            if (exist) {
                return true;
            }
        }
        return false;
    }


    private <T> T getFromSubContext(String name, Context context) {
        Map<Context, String> nodeMap = new HashMap<Context, String>();
        return (T) findNodeMap(name, context, nodeMap);
    }
    
    private boolean existFromSubContext(String name, Context context) {
        Map<Context, String> nodeMap = new HashMap<Context, String>();
        return existNodeMap(name, context, nodeMap);
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
        super.put("httpServletRequest", request);
    }

    public void init(HttpServletRequest request, HttpServletResponse response,
                     ServletContext servletContext) {
        setRequest(request);
        setResponse(response);
        setServletContext(servletContext);
        putRequestInfo(request);
    }

    /**
     * 设置请求信息到上下文中
     *
     * @param request
     */
    private void putRequestInfo(HttpServletRequest request) {
        this.put("tinyRequestRemoteAddr", request.getRemoteAddr());
        this.put("tinyRequestRemoteHost", request.getRemoteHost());
        this.put("tinyRequestRemoteUser", request.getRemoteUser());
        this.put("tinyRequestScheme", request.getScheme());
        this.put("tinyRequestServerName", request.getServerName());
        this.put("tinyRequestServletPath", request.getServletPath());
        this.put("tinyRequestServerPort", request.getServerPort());
        this.put("tinyRequestAuthType", request.getAuthType());
        this.put("tinyRequestEncoding", request.getCharacterEncoding());
        this.put("tinyRequestContentType", request.getContentType());
        this.put("tinyRequestContextPath", request.getContextPath());
        this.put("tinyRequestLocalAddr", request.getLocalAddr());
        this.put("tinyRequestLocalName", request.getLocalName());
        this.put("tinyRequestLocalPort", request.getLocalPort());
        this.put("tinyRequestPathInfo", request.getPathInfo());
        this.put("tinyRequestLocalName", request.getPathTranslated());
        this.put("tinyRequestProtocol", request.getProtocol());
        this.put("tinyRequestQueryString", request.getQueryString());
        this.put("tinyRequestRequestURI", request.getRequestURI());
    }

    public WebContext getWrappedWebContext() {
        return wrappedContext;
    }

    public WebContext getWrappedWebContext(String contextName) {
        return (WebContext) getSubContext(contextName);
    }

    public void putSubWebContext(String contextName, WebContext webContext) {
        putContext(contextName, webContext);

    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
        super.put("httpServletContext", servletContext);
    }

    public void setObject(String scope, String key, Object value) {
        WebContextScope webContextScope = scopes.get(scope);
        if (webContextScope != null) {
            webContextScope.setObject(key, value);
            return;
        }
        throw new RuntimeException(String.format("找不到%s对应的请求范围", scope));
    }

    public Object getObject(String scope, String key) {
        WebContextScope webContextScope = scopes.get(scope);
        if (webContextScope != null) {
            return webContextScope.getObject(key);
        }
        throw new RuntimeException(String.format("找不到%s对应的请求范围", scope));
    }

    interface WebContextScope {
        String getScope();

        void setObject(String key, Object value);

        Object getObject(String key);
    }

    class RequestScope implements WebContextScope {
        private String scope = "requestScope";

        public String getScope() {
            return scope;
        }

        public void setObject(String key, Object value) {
            getRequest().setAttribute(key, value);
        }

        public Object getObject(String key) {
            return getRequest().getAttribute(key);
        }
    }

    class SessionScope implements WebContextScope {
        private String scope = "sessionScope";

        public String getScope() {
            return scope;
        }

        public void setObject(String key, Object value) {
            getRequest().getSession().setAttribute(key, value);
        }

        public Object getObject(String key) {
            return getRequest().getSession().getAttribute(key);
        }
    }

    class ApplicationScope implements WebContextScope {
        private String scope = "applicationScope";

        public String getScope() {
            return scope;
        }

        public void setObject(String key, Object value) {
            getServletContext().setAttribute(key, value);
        }

        public Object getObject(String key) {
            return getServletContext().getAttribute(key);
        }
    }
}
