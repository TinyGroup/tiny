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
package org.tinygroup.i18n.impl;

import java.util.Locale;

import org.tinygroup.commons.i18n.LocaleUtil;
import org.tinygroup.context.Context;
import org.tinygroup.format.Formater;
import org.tinygroup.i18n.I18nMessageContext;
import org.tinygroup.i18n.I18nMessageFactory;

/**
 * 支持通过Context和Map来取数据的实现
 * 
 * @author luoguo
 * 
 */
public class I18nMessageContextImpl implements I18nMessageContext {

	public I18nMessageContextImpl() {

	}

	private Formater formater;

	public void setFormater(Formater formater) {
		this.formater = formater;
	}

	public String getMessage(String code) {
		return getMessage(code, LocaleUtil.getContext().getLocale());
	}

	public String getMessage(String code, Locale locale) {
		return I18nMessageFactory.getMessage(locale, code);
	}

	public String getMessage(String code, Context context) {
		return getMessage(code, context, getContextLocale(context));
	}

	public String getMessage(String code, Context context, Locale locale) {
		String string = getMessage(code, locale);
		return formater.format(context, string);
	}

	public String format(String message, Context context) {
		return formater.format(context, message);
	}

	public Locale getContextLocale(Context context) {
		Locale locale = context.get("contextLocale");
		if (locale == null) {
			locale = LocaleUtil.getContext().getLocale();
		}
		return locale;
	}

	public Formater getFormater() {
		return formater;
	}

	public String getMessage(String code, String defaultMessage) {
		String message=getMessage(code);
		if(message==null){
			message=defaultMessage;
		}
		return message;
	}

	public String getMessage(String code, Locale locale, String defaultMessage) {
		String message=getMessage(code, locale);
		if(message==null){
			message=defaultMessage;
		}
		return message;
	}

	public String getMessage(String code, String defaultMessage, Context context) {
		String message=getMessage(code, context);
		if(message==null){
			message=defaultMessage;
		}
		return message;
	}

	public String getMessage(String code, String defaultMessage,
			Context context, Locale locale) {
		String message=getMessage(code, context, locale);
		if(message==null){
			message=defaultMessage;
		}
		return message;
	}

}
