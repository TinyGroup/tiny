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
package org.tinygroup.weblayer.webcontext.rewrite;

import javax.servlet.http.HttpServletRequest;

import org.tinygroup.commons.tools.Assert;
import org.tinygroup.context.Context;
import org.tinygroup.format.FormatProvider;
import org.tinygroup.format.exception.FormatException;

/**
 * 
 * 功能说明: 为url重写模块提供字符串替换实现

 * 开发人员: renhui <br>
 * 开发时间: 2013-5-8 <br>
 * <br>
 */
public class RewriteFormatProvider implements FormatProvider {
	
	private HttpServletRequest request;
	
	
	public RewriteFormatProvider() {
		super();
	}

	public RewriteFormatProvider(HttpServletRequest request) {
		super();
		this.request = request;
	}
	
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String format(Context context, String varName) throws FormatException {
		Assert.assertNotNull(varName,"format string must not null");
		return RewriteUtil.expand(varName, request);
	}

}
