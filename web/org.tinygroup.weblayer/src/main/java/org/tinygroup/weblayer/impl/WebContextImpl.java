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

import org.tinygroup.weblayer.WebContext;
import org.tinygroup.weblayer.webcontext.AbstractWebContextWrapper;

/**
 * 
 * web上下文接口实现
 * 
 * @author luoguo
 * 
 */
public class WebContextImpl extends AbstractWebContextWrapper implements
		WebContext {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5147765100677738872L;

	public WebContextImpl() {
		super();
	}

	public WebContextImpl(WebContext wrappedContext) {
		super(wrappedContext);
	}

}
