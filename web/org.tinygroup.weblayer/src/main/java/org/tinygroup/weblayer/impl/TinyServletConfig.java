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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.tinygroup.commons.tools.Enumerator;
import org.tinygroup.weblayer.listener.ServletContextHolder;

/**
 * tiny框架中ServletConfig的实现
 * @author renhui
 *
 */
public class TinyServletConfig implements ServletConfig {
	public static final String SERVLET_BEAN = "servlet_bean";
	private Map<String, String> initParams=new HashMap<String, String>();

	public TinyServletConfig(Map<String, String> initParams){
		this.initParams=initParams;
	}
	
	
	public String getInitParameter(String s) {
		return initParams.get(s);
	}

	public Enumeration getInitParameterNames() {
		return new Enumerator(initParams.keySet());
	}

	public ServletContext getServletContext() {
		 return ServletContextHolder.getServletContext();
	}

	public String getServletName() {
		return getInitParameter(SERVLET_BEAN);
	}

}
