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

import java.util.regex.Pattern;

/**
 * 模式匹配处理接口
 * 
 * @author luoguo
 * 
 */
public interface PatternDefine {
	/**
	 * 返回正则匹配
	 * 
	 * @return
	 */
	Pattern getPattern();

	/**
	 * 设置前缀
	 * 
	 * @param prefixPatternString
	 */
	void setPrefixPatternString(String prefixPatternString);

	/**
	 * 设置后缀
	 * 
	 * @param postfixPatternString
	 */
	void setPostfixPatternString(String postfixPatternString);

	/**
	 * 设置正则表达式中间部分
	 * 
	 * @param patternString
	 */
	void setPatternString(String patternString);

	/**
	 * 返回正文部分
	 * 
	 * @param string
	 * @return
	 */
	String getPureMatchText(String string);

	/**
	 * 根据正文返回完整部分
	 * 
	 * @param string
	 * @return
	 */
	String getFullMatchText(String string);

	/**
	 * 设置域分隔符
	 * 
	 * @return
	 */
	void setSplitChar(char splitChar);

	/**
	 * 返回分隔符
	 * 
	 * @return
	 */
	char getSplitChar();
}
