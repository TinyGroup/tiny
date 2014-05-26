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
import org.tinygroup.i18n.I18nMessage;
import org.tinygroup.i18n.I18nMessageFactory;

public class BaseRuntimeException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1141168272047460629L;
	private static I18nMessage i18nMessage = I18nMessageFactory.getI18nMessages();// 需要在启动的时候注入进来
	private String errorCode;
	private String errorMsg;
	
	
	
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	public static void setI18nMessage(I18nMessage i18nMessage) {
		BaseRuntimeException.i18nMessage = i18nMessage;
	}
	
	
	public BaseRuntimeException(Throwable throwable) {
		super(throwable);
	}
	public BaseRuntimeException(Throwable throwable, String code) {
		super(i18nMessage.getMessage(code),throwable);
	}

	public BaseRuntimeException(Throwable throwable, String code,Object...args) {
		super(i18nMessage.getMessage(code,args),throwable);
	}
	
	
	public BaseRuntimeException(String code, Object... args) {
		super(i18nMessage.getMessage(code, args));
		
	}

	public BaseRuntimeException(String code) {
		super(i18nMessage.getMessage(code));
	}

	public BaseRuntimeException(String code, Locale locale) {
		super(i18nMessage.getMessage(code, locale));
	}

	public BaseRuntimeException(String code, Locale locale, Object... args) {
		super(i18nMessage.getMessage(code, locale, args));
	}

	public BaseRuntimeException(String code, Context context, Locale locale) {
		super(i18nMessage.getMessage(code, context, locale));
	}

	public BaseRuntimeException(String code, Context context) {
		super(i18nMessage.getMessage(code, context));
	}

}
