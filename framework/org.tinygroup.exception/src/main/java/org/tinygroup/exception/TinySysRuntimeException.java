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
package org.tinygroup.exception;

import java.util.Locale;

import org.tinygroup.context.Context;

public class TinySysRuntimeException extends BaseRuntimeException {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1694628104824119327L;

	public TinySysRuntimeException(Throwable throwable) {
		super(throwable);
	}
	public TinySysRuntimeException(Throwable throwable, String code) {
		super(throwable,code);
	}

	public TinySysRuntimeException(Throwable throwable, String code,Object...args) {
		super(throwable,code,args);
	}

	public TinySysRuntimeException(String code, Object... args) {
		super(code, args);
	}

	public TinySysRuntimeException(String code) {
		super(code);
	}

	public TinySysRuntimeException(String code, Locale locale) {
		super(code, locale);
	}

	public TinySysRuntimeException(String code, Locale locale, Object... args) {
		super(code, locale, args);
	}

	public TinySysRuntimeException(String code, Context context, Locale locale) {
		super(code, context, locale);
	}

	public TinySysRuntimeException(String code, Context context) {
		super(code, context);
	}

}
