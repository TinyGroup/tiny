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
package org.tinygroup.weblayer.webcontext.basic.impl;

import static org.tinygroup.commons.tools.ArrayUtil.isEmptyArray;

import org.tinygroup.commons.tools.HumanReadableSize;
import org.tinygroup.weblayer.WebContext;
import org.tinygroup.weblayer.webcontext.AbstractWebContextWrapper;
import org.tinygroup.weblayer.webcontext.WebContextException;
import org.tinygroup.weblayer.webcontext.basic.BasicWebContext;
import org.tinygroup.weblayer.webcontext.basic.interceptor.ResponseHeaderSecurityFilter;
import org.tinygroup.weblayer.webcontext.basic.response.BasicResponseImpl;

/**
 * 过滤header中的crlf，将status message用HTML entities转义，限制cookie的总大小。
 * @author renhui
 *
 */
public class BasicWebContextImpl extends AbstractWebContextWrapper implements BasicWebContext {
    private Object[] interceptors;
    
    private String maxCookieSize;

    public BasicWebContextImpl(WebContext wrappedContext) {
        super(wrappedContext);
    }

        
    public String getMaxCookieSize() {
		return maxCookieSize;
	}


	public void setMaxCookieSize(String maxSetCookieSize) {
		this.maxCookieSize = maxSetCookieSize;
	}



	public Object[] getResponseHeaderInterceptors() {
        return interceptors.clone();
    }


    public void commitHeaders() throws WebContextException {
        ((BasicResponseImpl) getResponse()).commitHeaders();
    }

    public void prepareResponse(){
    	  ((BasicResponseImpl) getResponse()).prepareResponse();
    }
    

    public void commitResponse(){
    	  ((BasicResponseImpl) getResponse()).commitResponse();
    }
  
    private Object[] addDefaultInterceptors(Object[] interceptors) {
    	ResponseHeaderSecurityFilter defaultInterceotor= new ResponseHeaderSecurityFilter();
    	if(maxCookieSize!=null){
    		defaultInterceotor.setMaxCookieSize(new HumanReadableSize(maxCookieSize));
    	}
        if (isEmptyArray(interceptors)) {
            return new Object[] { defaultInterceotor };
        }

        for (Object interceptor : interceptors) {
            if (interceptor instanceof ResponseHeaderSecurityFilter) {
                return interceptors;
            }
        }

        // appending response-header-security-filter
        Object[] newInterceptors = new Object[interceptors.length + 1];
        System.arraycopy(interceptors, 0, newInterceptors, 0, interceptors.length);
        newInterceptors[interceptors.length] = defaultInterceotor;

        return newInterceptors;
    }

	public void initContext(Object[] interceptors) {
		  this.interceptors = addDefaultInterceptors(interceptors);
	      setResponse(new BasicResponseImpl(this, getWrappedWebContext().getResponse(), this.interceptors));
	}
}
