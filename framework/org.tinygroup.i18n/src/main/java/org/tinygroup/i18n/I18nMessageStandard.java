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

/**
 * 标准的i18n消息访问接口
 * 
 * @author luoguo
 * 
 */
public interface I18nMessageStandard extends I18nMessageBase {
	/**
	 * 采用默认区域获取信息
	 * 
	 * @param code
	 *            信息代码
	 * @param args
	 *            参数列表
	 * @return
	 */
	String getMessage(String code, Object... args);
	/**
	 * 采用默认区域获取信息,不存在则返回默认值
	 * 
	 * @param code
	 *            信息代码
	 * @param defaultMessage
	 *            默认值           
	 * @param args
	 *            参数列表
	 * @return
	 */
	String getMessage(String code,String defaultMessage, Object... args);

	/**
	 * 采用指定区域获取信息
	 * 
	 * @param code 信息代码
	 * @param args 参数
	 * @param locale 区域
	 * @return
	 */
	String getMessage(String code, Locale locale, Object... args);
	
	/**
	 * 采用指定区域获取信息
	 * 
	 * @param code 信息代码
	 * @param args 参数
	 * @param defaultMessage 默认值
	 * @param locale 区域
	 * @return
	 */
	String getMessage(String code, Locale locale,String defaultMessage ,Object... args);

}
