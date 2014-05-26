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
package org.tinygroup.weblayer.exceptionhandler.impl;

import java.io.IOException;

import javax.servlet.ServletException;

import org.tinygroup.weblayer.WebContext;
import org.tinygroup.weblayer.exceptionhandler.WebExceptionHandler;

/**
 * 
 * 功能说明: 默认的页面异常处理，采用往上包装成http-servlet体系可以识别的异常抛出

 * 开发人员: renhui <br>
 * 开发时间: 2013-9-22 <br>
 * <br>
 */
public class DefaultWebExceptionHandler implements WebExceptionHandler {

	public void handler(Throwable e, WebContext webContext) throws IOException,
			ServletException {
		if (e instanceof ServletException) {
			throw (ServletException) e;
		} else if (e instanceof IOException) {
			throw (IOException) e;
		} else if (e instanceof RuntimeException) {
			throw (RuntimeException) e;
		} else if (e instanceof Error) {
			throw (Error) e;
		} else {
			throw new ServletException(e);
		}

	}

}
