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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.tinygroup.commons.i18n.LocaleUtil;
import org.tinygroup.context.Context;
import org.tinygroup.format.Formater;
import org.tinygroup.i18n.I18nMessage;
import org.tinygroup.i18n.I18nMessageFactory;

public class I18nMessageImpl implements I18nMessage {
	private static final String CONTEXT_LOCALE_KEY = "contextLocale";
	private static Pattern pattern = Pattern.compile("[{](.)*?[}]");
	private Formater formater;
	private String localeKey = CONTEXT_LOCALE_KEY;

	public String getLocaleKey() {
		return localeKey;
	}

	public void setLocaleKey(String localeKey) {
		this.localeKey = localeKey;
	}

	public Formater getFormater() {
		return formater;
	}

	public void setFormater(Formater formater) {
		this.formater = formater;
	}

	private String format(String message, Object... args) {
		Matcher matcher = pattern.matcher(message);
		StringBuffer stringBuffer = new StringBuffer();
		int start = 0;
		int count = 0;
		while (matcher.find(start)) {
			stringBuffer.append(message.substring(start, matcher.start()));
			stringBuffer.append(args[count++]);
			start = matcher.end();
			if (count == args.length) {
				break;
			}
		}
		stringBuffer.append(message.substring(start, message.length()));
		return stringBuffer.toString();
	}

	public String getMessage(String code, Object... args) {
		return getMessage(code, LocaleUtil.getContext().getLocale(), args);
	}

	public String getMessage(String code, Locale locale, Object... args) {
		String message = I18nMessageFactory.getMessage(locale, code);
		if (message == null) {
			return null;
		}
		return format(message, args);
	}

	public String getMessage(String code) {
		return I18nMessageFactory.getMessage(code);
	}

	public String getMessage(String code, Locale locale) {
		return I18nMessageFactory.getMessage(locale, code);
	}

	public String getMessage(String code, Context context) {
		String string = I18nMessageFactory.getMessage(LocaleUtil.getContext().getLocale(), code);
		return formater.format(context, string);
	}

	public String format(String message, Context context) {
		return formater.format(context, message);
	}

	public String getMessage(String code, Context context, Locale locale) {
		String string = I18nMessageFactory.getMessage(locale, code);
		return formater.format(context, string);

	}

	public Locale getContextLocale(Context context) {
		Locale locale = context.get(localeKey);
		if (locale == null) {
			locale = LocaleUtil.getContext().getLocale();
		}
		return locale;
	}

	public String getMessage(String code, String defaultMessage, Object... args) {
		String message=getMessage(code,args);
		if(message==null){
			message=defaultMessage;
		}
		return message;
	}

	public String getMessage(String code, Locale locale, String defaultMessage,
			Object... args) {
		String message=getMessage(code, locale,args);
		if(message==null){
			message=defaultMessage;
		}
		return message;
	}

	public String getMessage(String code, String defaultMessage) {
		String message=getMessage(code);
		if(message==null){
			message=defaultMessage;
		}
		return message;
	}

	public String getMessage(String code, Locale locale, String defaultMessage) {
		String message=getMessage(code,locale);
		if(message==null){
			message=defaultMessage;
		}
		return message;
	}

	public String getMessage(String code, String defaultMessage, Context context) {
		String message=getMessage(code,context);
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
