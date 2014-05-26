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
package org.tinygroup.format.impl;

import java.util.regex.Pattern;

import org.tinygroup.format.PatternDefine;

/**
 * 默认的正则表达式
 * 
 * @author luoguo
 * 
 */
public class DefaultPatternDefine implements PatternDefine {

	private static final String DEFAULT_PATTERN_STRING = "([$]+[{]+[a-zA-Z0-9[.[_[:[/[#]]]]]]+[}])";
	private static final String DEFAULT_POSTFIX_PATTERN_STRING = "}";
	private static final String DEFAULT_PREFIX_PATTERN_STRING = "${";
	private static final char DEFAULT_SPLIT_CHAR = ':';
	private String prefixPatternString = DEFAULT_PREFIX_PATTERN_STRING;// 前缀
	private String postfixPatternString = DEFAULT_POSTFIX_PATTERN_STRING;// 后缀
	private String patternString = DEFAULT_PATTERN_STRING;// 中间部分
	private Pattern pattern;
	private char splitChar = DEFAULT_SPLIT_CHAR;// 域分隔符

	public Pattern getPattern() {
		if (pattern == null) {
			pattern = Pattern.compile(patternString);
		}
		return pattern;
	}

	public void setPrefixPatternString(String prefixPatternString) {
		this.prefixPatternString = prefixPatternString;
	}

	public void setPostfixPatternString(String postfixPatternString) {
		this.postfixPatternString = postfixPatternString;
	}

	public void setPatternString(String patternString) {
		this.patternString = patternString;
	}

	public String getPureMatchText(String string) {
		int startPos = prefixPatternString.length();
		int endPos = string.length() - postfixPatternString.length();
		return string.substring(startPos, endPos);
	}

	public String getFullMatchText(String string) {
		return String.format("%s%s%s", prefixPatternString, string,
				postfixPatternString);
	}

	public void setSplitChar(char splitChar) {
		this.splitChar = splitChar;
	}

	public char getSplitChar() {
		return splitChar;
	}

}
