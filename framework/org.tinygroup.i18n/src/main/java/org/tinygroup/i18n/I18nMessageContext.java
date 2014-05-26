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

import java.util.Locale;

import org.tinygroup.context.Context;
import org.tinygroup.format.Formater;

/**
 * 支持Context的i18n消息访问接口，
 * 
 * @author luoguo
 * 
 */
public interface I18nMessageContext extends I18nMessageBase {
	/**
	 * 用指定代码获取信息，并从环境中填充占位符信息
	 * 
	 * @param code
	 * @param context
	 * @return
	 */
	String getMessage(String code, Context context);

	/**
	 * 用指定代码获取信息，并从环境中填充占位符信息，如果没有指定代码对应的信息，则用defaultMessage
	 * 
	 * @param code
	 * @param defaultMessage
	 *            默认值
	 * @param context
	 * @return
	 */
	String getMessage(String code, String defaultMessage, Context context);

	/**
	 * 设置格式化器
	 * 
	 * @param formater
	 */
	void setFormater(Formater formater);

	/**
	 * 返回格式化器
	 * 
	 * @return
	 */
	Formater getFormater();

	/**
	 * 对mesasge进行格式化
	 * 
	 * @param message
	 * @param context
	 * @return
	 */
	String format(String message, Context context);

	/**
	 * 用指定时区语言对mesasge进行格式化
	 * @param code
	 * @param context
	 * @param locale
	 * @return
	 */
	String getMessage(String code, Context context, Locale locale);

	/**
	 * 用指定时区语言对mesasge进行格式化，如果Code对应的信息不存在，则用defaultMessage
	 * @param code
	 * @param defaultMessage
	 * @param context
	 * @param locale
	 * @return
	 */
	String getMessage(String code, String defaultMessage, Context context,
			Locale locale);

}
