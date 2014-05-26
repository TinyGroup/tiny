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

import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.tinygroup.commons.tools.ToStringBuilder;
import org.tinygroup.commons.tools.ToStringBuilder.MapBuilder;
import org.tinygroup.weblayer.TinyFilterHandler;
import org.tinygroup.weblayer.WebContext;

/**
 * 
 * 功能说明: 实现了<code>WebContext</code>接口，包含request、response和servletContext的信息。
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-5-7 <br>
 * <br>
 */
public class SimpleWebContext extends CommitMonitor implements WebContext {

	/**
	 * 创建一个新的<code>RequestContext</code>对象。
	 * 
	 * @param webContext
	 *            当前请求所在的<code>ServletContext</code>
	 * @param request
	 *            <code>HttpServletRequest</code>对象
	 * @param response
	 *            <code>HttpServletResponse</code>对象
	 */
	public SimpleWebContext(WebContext webContext, TinyFilterHandler handler, HttpServletRequest request,
			HttpServletResponse response) {
		super(webContext, handler);
		setRequest(new RequestWrapper(request));
		setResponse(new CommittingAwareResponse(response, this));
	}

	/** 开始一个请求。 */
	public void prepare() {
	}

	/** 结束一个请求。 */
	public void commit() {
	}

	/**
	 * 显示当前<code>RequestContext</code>的内容。
	 * 
	 * @return 字符串表示
	 */
	
	public String toString() {
		MapBuilder mb = new MapBuilder();

		mb.append("request", getRequest());
		mb.append("response", getResponse());
		mb.append("webapp", getServletContext());

		return new ToStringBuilder().append(getClass().getSimpleName())
				.append(mb).toString();
	}

	private class RequestWrapper extends AbstractRequestWrapper {

		public RequestWrapper(HttpServletRequest request) {
			super(SimpleWebContext.this, request);
		}

		
		public Object getAttribute(String name) {
			Object object = super.getAttribute(name);
			if (object == null) {
				 WebContext topWebContext= getTopWebContext();
				if(topWebContext!=null){
					object = getFromWrapperContext(name, getTopWebContext());
				}
			}
			return object;
		}

		
		public Enumeration getAttributeNames() {
			return super.getAttributeNames();
		}

		
		public void setAttribute(String name, Object o) {
			super.setAttribute(name, o);
		}


		
		public void removeAttribute(String name) {
			super.removeAttribute(name);
		}
		
		
		public RequestDispatcher getRequestDispatcher(String path) {
			return super.getRequestDispatcher(path);
		}
		

	}

}
