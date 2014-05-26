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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.tinygroup.context.Context;

/**
 * 
 * 功能说明:定义web上下文接口 

 * 开发人员: renhui <br>
 * 开发时间: 2013-4-28 <br>
 * <br>
 */
public interface WebContext extends Context {
	/**
	 * 
	 * 初始化web上下文方法
	 * @param request
	 * @param response
	 * @param servletContext
	 */
	void init(HttpServletRequest request, HttpServletResponse response,ServletContext servletContext);

	HttpServletRequest getRequest();

	void setRequest(HttpServletRequest request);

	void setResponse(HttpServletResponse response);

	HttpServletResponse getResponse();
	
	/**
	 * 获取包装的WebContext
	 * @return
	 */
	WebContext getWrappedWebContext();
	/**
	 * 取得servletContext对象
	 * @return
	 */
	ServletContext getServletContext();
	/**
	 * 
	 * 设置servletContext对象
	 * @param servletContext
	 */
	void setServletContext(ServletContext servletContext);
	/**
	 * 
	 * 把对象设置到scope指定的范围内
	 * @param scope
	 * @param key
	 * @param value
	 */
	void setObject(String scope,String key,Object value);
	
	/**
	 * 
	 * 获取指定范围内的对象
	 * @param scope
	 * @param key
	 * @return
	 */
	Object getObject(String scope,String key);
	
}
