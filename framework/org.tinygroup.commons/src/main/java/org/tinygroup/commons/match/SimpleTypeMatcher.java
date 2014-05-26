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
package org.tinygroup.commons.match;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SimpleTypeMatcher {
	private static Pattern doublePattern = Pattern
			.compile("([-]?[0-9]+([.]|[0-9])*)[d|D]?");
	private static Pattern floatPattern = Pattern
			.compile("([-]?[0-9]+([.]|[0-9])*)[f|F]?");
	private static Pattern longPattern = Pattern
			.compile("[-]?((0[x|X]([0-9|a-f|A-F])+)|([0-9]+))[l|L]?");
	private static Pattern intPattern = Pattern
			.compile("[-]?((0[x|X]([0-9|a-f|A-F])+)|([0-9]+))[d|D]?");
	private static Pattern booleanPattern = Pattern.compile("true|false");
	private static Pattern charPattern = Pattern.compile("['].*[']");
	private static Pattern stringPattern = Pattern.compile("[\"].*[\"]");

	private SimpleTypeMatcher() {
	}

	public static Object matchType(String str) {
		Matcher matcher = booleanPattern.matcher(str);
		if (matcher.find() && matcher.group().length() == str.length()) {
			return Boolean.valueOf(str);
		}
		matcher = intPattern.matcher(str);
		if (matcher.find() && matcher.group().length() == str.length()) {
			return Integer.valueOf(str);
		}
		matcher = longPattern.matcher(str);
		if (matcher.find() && matcher.group().length() == str.length()) {
			return Long.valueOf(str);
		}
		matcher = floatPattern.matcher(str);
		if (matcher.find() && matcher.group().length() == str.length()) {
			return Float.valueOf(str);
		}
		matcher = doublePattern.matcher(str);
		if (matcher.find() && matcher.group().length() == str.length()) {
			return Double.valueOf(str);
		}
		matcher = charPattern.matcher(str);
		if (matcher.find() && matcher.group().length() == str.length()) {
			//只支持'x'的格式，其他格式暂时不支持
			return str.substring(1, 1);
		}
		matcher = stringPattern.matcher(str);
		if (matcher.find() && matcher.group().length() == str.length()) {
			return str.substring(1, str.length() - 1);
		}
		return str;
	}

}
