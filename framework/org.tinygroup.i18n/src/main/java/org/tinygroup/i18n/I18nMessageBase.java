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
 * 基本的信息获取接口
 * 
 * @author luoguo
 * 
 */
interface I18nMessageBase {
	/**
	 * 根据代码获取
	 * 
	 * @param code
	 * @return
	 */
	String getMessage(String code);

	/**
	 * 根据代码和位置获取
	 * 
	 * @param code
	 * @param locale
	 * @return
	 */
	String getMessage(String code, Locale locale);

	/**
	 * 根据代码获取，如果获取不到，则返回默认信息
	 * 
	 * @param code
	 * @param defaultMessage
	 * @return
	 */
	String getMessage(String code, String defaultMessage);
	/**
	 * 根据代码和位置获取，如果获取不到，则返回默认信息
	 * @param code
	 * @param locale
	 * @param defaultMessage
	 * @return
	 */
	String getMessage(String code, Locale locale, String defaultMessage);

}
