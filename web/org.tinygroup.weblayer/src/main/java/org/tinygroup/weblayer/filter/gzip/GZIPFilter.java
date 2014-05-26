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
package org.tinygroup.weblayer.filter.gzip;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.GZIPOutputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.tinygroup.commons.tools.Assert;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.config.Configuration;
import org.tinygroup.xmlparser.node.XmlNode;

//TODO增加跳过文件过滤
public class GZIPFilter implements Filter, Configuration {

	private static final String GZIP_FILTER_NODE_PATH = "/application/gzip-filter";
	protected XmlNode applicationConfig;
	private int maxContentLength;
	private Set<String> excludeContentTypes=new HashSet<String>();

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		if (req instanceof HttpServletRequest) {
			HttpServletRequest request = (HttpServletRequest) req;
			HttpServletResponse response = (HttpServletResponse) res;
			String ae = request.getHeader("accept-encoding");
			if (ae != null && ae.indexOf("gzip") != -1) {
				GZIPResponseWrapper wrappedResponse = new GZIPResponseWrapper(response);
				chain.doFilter(req, wrappedResponse);
				// GZIP压缩：
				byte[] buff = wrappedResponse.getBufferedBytes();
				if(buff.length>0){
					// 创建缓存容器：
					boolean isLessContentLength=maxContentLength==0||maxContentLength>=buff.length;
					boolean isExcludeType=isExcluedType(response);
					if(isLessContentLength&&!isExcludeType){
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						GZIPOutputStream gzip = new GZIPOutputStream(baos);
						gzip.write(buff);
						gzip.close();
						buff = baos.toByteArray();
						// 设置响应头；
						response.setHeader("Content-Encoding", "gzip");
						response.setContentLength(buff.length);
					}
					response.getOutputStream().write(buff);
				}
				return;
			}
			chain.doFilter(req, res);
		}
	}
	
	private boolean isExcluedType(HttpServletResponse response){
		boolean isExcludeType=false;
		String contentType=response.getContentType();
		if(contentType!=null){
			for (String type : excludeContentTypes) {
				if(contentType.indexOf(type)>=0){
					isExcludeType=true;
				}
			}
		}
		return isExcludeType;
	}

	public void init(FilterConfig filterConfig) {
		// noop
	}

	public void destroy() {
		// noop
	}

	public String getApplicationNodePath() {
		return GZIP_FILTER_NODE_PATH;
	}

	public String getComponentConfigPath() {
		return null;
	}

	public void config(XmlNode applicationConfig, XmlNode componentConfig) {
		this.applicationConfig = applicationConfig;
		if(applicationConfig!=null){
	    	maxContentLength=Integer.parseInt(StringUtil.defaultIfBlank(applicationConfig.getAttribute("max-content-length"),"0"));
	    	Assert.assertTrue(maxContentLength>=0,"文件长度值必须是大于0的正整数");
	    	XmlNode excludeNode=applicationConfig.getSubNode("exclude-content-type");
	    	if(excludeNode!=null){
	    		String value=excludeNode.getContent();
	    		if(value!=null){
	    			String[] types=value.split(";");
		    		for (String type : types) {
						excludeContentTypes.add(type);
					}
	    		}
	    	}
	    }
	}

	public XmlNode getComponentConfig() {
		return null;
	}

	public XmlNode getApplicationConfig() {
		return applicationConfig;
	}
}
