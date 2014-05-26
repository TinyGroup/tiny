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
package org.tinygroup.weblayer.listener;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.tinygroup.commons.tools.Assert;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.commons.tools.Enumerator;
import org.tinygroup.fileresolver.FullContextFileRepository;
import org.tinygroup.vfs.FileObject;

/**
 * tiny框架 ServletContext实现
 * @author renhui
 *
 */
public class TinyServletContext implements ServletContext {
	
    private ServletContext originalContext;
    
    private FullContextFileRepository fullContextFileRepository;

    private Map<String,String> parameters=CollectionUtil.createHashMap();
    
	public TinyServletContext(ServletContext servletContext){
		Assert.assertNotNull(servletContext, "servletContext must not null");
		this.originalContext=servletContext;
	}
	
	public String getContextPath() {
		return originalContext.getContextPath();
	}

	public ServletContext getContext(String uripath) {
		return originalContext.getContext(uripath);
	}

	public int getMajorVersion() {
		return originalContext.getMajorVersion();
	}

	public int getMinorVersion() {
		return originalContext.getMinorVersion();
	}

	public String getMimeType(String file) {
		return originalContext.getMimeType(file);
	}

	public Set getResourcePaths(String path) {
		return originalContext.getResourcePaths(path);
	}

	public URL getResource(String path) throws MalformedURLException {
		
		if(fullContextFileRepository!=null){
			FileObject fileObject = fullContextFileRepository
					.getFileObject(path);
			if(fileObject != null && fileObject.isExist()){
				return fileObject.getURL();
			}
		}
		return originalContext.getResource(path);
	}

	public InputStream getResourceAsStream(String path) {
		if(fullContextFileRepository!=null){
			FileObject fileObject = fullContextFileRepository
					.getFileObject(path);
			if(fileObject != null && fileObject.isExist()){
				return fileObject.getInputStream();
			}
		}
		return originalContext.getResourceAsStream(path);
	}

	public RequestDispatcher getRequestDispatcher(String path) {
		return originalContext.getRequestDispatcher(path);
	}

	public RequestDispatcher getNamedDispatcher(String name) {
		return originalContext.getNamedDispatcher(name);
	}
    @Deprecated
	public Servlet getServlet(String name) throws ServletException {
		return originalContext.getServlet(name);
	}

	@Deprecated
	public Enumeration getServlets() {
		return originalContext.getServlets();
	}

	public Enumeration getServletNames() {
		return originalContext.getServletNames();
	}

	public void log(String msg) {
         originalContext.log(msg);
	}
    @Deprecated
	public void log(Exception exception, String msg) {
		originalContext.log(exception, msg);

	}

	public void log(String message, Throwable throwable) {
		originalContext.log(message, throwable);

	}

	public String getRealPath(String path) {
		if(fullContextFileRepository!=null){
			FileObject fileObject = fullContextFileRepository
					.getFileObject(path);
			if(fileObject != null && fileObject.isExist()){
				return fileObject.getAbsolutePath();
			}
		}
	    return originalContext.getRealPath(path);
	}

	public String getServerInfo() {
		return originalContext.getServerInfo();
	}

	public String getInitParameter(String name) {
		String value= parameters.get(name);
		if(value==null){
			value= originalContext.getInitParameter(name);
		}
		return value;
	}

	public Enumeration getInitParameterNames() {
		Enumeration enumeration=originalContext.getInitParameterNames();
		Set<String> parameterSet=parameters.keySet();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			parameterSet.add(name);
		}
		return new Enumerator(parameterSet);
	}

	public Object getAttribute(String name) {
		return originalContext.getAttribute(name);
	}

	public Enumeration getAttributeNames() {
		return originalContext.getAttributeNames();
	}

	public void setAttribute(String name, Object object) {
		originalContext.setAttribute(name, object);
	}

	public void removeAttribute(String name) {
		originalContext.removeAttribute(name);
	}

	public String getServletContextName() {
		return originalContext.getServletContextName();
	}

	public void setInitParameter(String name,String value){
		parameters.put(name, value);
	}

	public void setFullContextFileRepository(
			FullContextFileRepository fullContextFileRepository) {
		this.fullContextFileRepository = fullContextFileRepository;
	}
	
}
