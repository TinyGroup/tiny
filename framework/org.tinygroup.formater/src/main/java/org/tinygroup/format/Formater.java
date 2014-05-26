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
package org.tinygroup.format;

import java.util.Map;

/**
 * 格式化的接口
 * 
 * @author luoguo
 * 
 */
public interface Formater extends FormatProvider {

	/**
	 * 设置正则表达式,如果不想用默认正则表达式，可以通过此方法自行定义
	 * 
	 * @param patternHandle
	 */
	void setPatternHandle(PatternDefine patternHandle);

	/**
	 * 设置格式化提供者
	 * 
	 * @param formatProviders
	 *            Key为匹配范围符
	 */
	void setFormatProviders(Map<String, FormatProvider> formatProviders);

	/**
	 * 添加格式化提供者
	 * @param prefix 前缀
	 * @param formatProvider
	 */
	void addFormatProvider(String prefix, FormatProvider formatProvider);
}
