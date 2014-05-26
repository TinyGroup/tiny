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
package org.tinygroup.weblayer.webcontext.parser.impl;

import org.tinygroup.commons.cryptor.Cryptor;
import org.tinygroup.commons.cryptor.DefaultCryptor;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.exception.TinySysRuntimeException;
import org.tinygroup.weblayer.WebContext;

/**
 * 
 * 功能说明: 对参数值进行解密

 * 开发人员: renhui <br>
 * 开发时间: 2013-10-14 <br>
 * <br>
 */
public class ParamValueDecryptFilter extends AbstractParamValueFilter {

	public String valueFilter(String value, WebContext webContext) {
		Cryptor cryptor=new DefaultCryptor();
		try {
			if(!StringUtil.isBlank(value)){
				return cryptor.decrypt(value);
			}
		} catch (Exception e) {
			throw new TinySysRuntimeException(e);
		}
		return value;
	}

}
