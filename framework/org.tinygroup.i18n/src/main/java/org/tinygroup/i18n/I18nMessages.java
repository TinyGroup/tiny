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
package org.tinygroup.i18n;

import java.util.List;
import java.util.Locale;

import org.tinygroup.commons.i18n.LocaleUtil;
import org.tinygroup.context.Context;
import org.tinygroup.format.Formater;

/**
 * I18n消息访问类
 * 
 * @author luoguo
 * 
 */
public final class I18nMessages implements I18nMessage {
	private List<I18nMessageStandard> i18nMessageStandards;
	private List<I18nMessageContext> i18nMessageContexts;
	private Formater formater;

	public List<I18nMessageStandard> getI18nMessageStandards() {
		return i18nMessageStandards;
	}

	public void setI18nMessageStandards(
			List<I18nMessageStandard> i18nMessageStandards) {
		this.i18nMessageStandards = i18nMessageStandards;
	}

	public List<I18nMessageContext> getI18nMessageContexts() {
		return i18nMessageContexts;
	}

	public void setI18nMessageContexts(
			List<I18nMessageContext> i18nMessageContexts) {
		this.i18nMessageContexts = i18nMessageContexts;
		for (I18nMessageContext messageContext : i18nMessageContexts) {
			if (messageContext.getFormater() == null) {
				messageContext.setFormater(formater);
			}
		}
	}

	public String getMessage(String code, Locale locale, Object... args) {
		String message = null;
		if (i18nMessageStandards != null) {
			for (I18nMessageStandard m : i18nMessageStandards) {
				message = m.getMessage(code, locale, args);
				if (message != null) {
					break;
				}
			}
		}
		return message;
	}

	public String getMessage(String code, Object... args) {
		return getMessage(code, LocaleUtil.getContext().getLocale(), args);
	}

	public String getMessage(String code, Context context) {
		return getMessage(code, context, LocaleUtil.getContext().getLocale());
	}

	public String getMessage(String code, Context context, Locale locale) {
		String message = null;
		if (i18nMessageContexts != null) {
			for (I18nMessageContext m : i18nMessageContexts) {
				message = m.getMessage(code, context, locale);
				if (message != null) {
					break;
				}
			}
		}
		return message;
	}

	public String getMessage(String code) {
		return getMessage(code, LocaleUtil.getContext().getLocale());
	}

	public String getMessage(String code, Locale locale) {
		String message = null;
		message = getStandardMessage(code, locale);
		if (message != null) {
			return message;
		}
		message = getMapMessage(code, locale);
		if (message != null) {
			return message;
		}
		message = getContextMessage(code, locale);
		if (message != null) {
			return message;
		}
		return null;
	}

	private String getStandardMessage(String code, Locale locale) {
		String message = null;
		if (i18nMessageStandards != null) {
			for (I18nMessageBase m : i18nMessageStandards) {
				message = m.getMessage(code, locale);
				if (message != null) {
					break;
				}
			}
		}
		return message;
	}

	private String getMapMessage(String code, Locale locale) {
		String message = null;
		if (i18nMessageStandards != null) {
			for (I18nMessageBase m : i18nMessageStandards) {
				message = m.getMessage(code, locale);
				if (message != null) {
					break;
				}
			}
		}
		return message;
	}

	private String getContextMessage(String code, Locale locale) {
		String message = null;
		if (i18nMessageContexts != null) {
			for (I18nMessageBase m : i18nMessageContexts) {
				message = m.getMessage(code, locale);
				if (message != null) {
					break;
				}
			}
		}
		return message;
	}

	public String format(String msg, Context context) {
		String message = null;
		if (i18nMessageContexts != null) {
			for (I18nMessageContext m : i18nMessageContexts) {
				message = m.format(msg, context);
				if (message != null) {
					break;
				}
			}
		}
		return message;
	}

	public void setFormater(Formater formater) {
		this.formater = formater;
	}

	public Formater getFormater() {
		return formater;
	}

	public String getMessage(String code, String defaultMessage, Object... args) {
		String message = getMessage(code, args);
		if (message == null) {
			message = defaultMessage;
		}
		return message;
	}

	public String getMessage(String code, Locale locale, String defaultMessage,
			Object... args) {
		String message = getMessage(code, locale, args);
		if (message == null) {
			message = defaultMessage;
		}
		return message;
	}

	public String getMessage(String code, String defaultMessage) {
		String message = getMessage(code);
		if (message == null) {
			message = defaultMessage;
		}
		return message;
	}

	public String getMessage(String code, Locale locale, String defaultMessage) {
		String message = getMessage(code, locale);
		if (message == null) {
			message = defaultMessage;
		}
		return message;
	}

	public String getMessage(String code, String defaultMessage, Context context) {
		String message = getMessage(code, context);
		if (message == null) {
			message = defaultMessage;
		}
		return message;
	}

	public String getMessage(String code, String defaultMessage,
			Context context, Locale locale) {
		String message = getMessage(code, context, locale);
		if (message == null) {
			message = defaultMessage;
		}
		return message;
	}

}
