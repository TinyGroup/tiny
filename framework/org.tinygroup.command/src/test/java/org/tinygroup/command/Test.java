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
package org.tinygroup.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
	Pattern commandPattern = Pattern.compile("(\\w|[:]|[-])+");
	Pattern parameterPattern = Pattern
			.compile("(\\b(\\w|[\u4e00-\u9fa5]|[/]|[.]|[-])+\\s*=\"[^\"]*\")|(\\b(\\w|[\u4e00-\u9fa5]|[/]|[.]|[-])+='[^\']*')|(\\b(\\w|[\u4e00-\u9fa5]|[/]|[.]|[-])+=(\\w|[\u4e00-\u9fa5]|[.]|[-])+)");

	public static void main(String[] args) {

		new Test().test("abc:ca a=b c=d e='a d e'");
		new Test().test("abc a=\"ab c d\" e=f");
		new Test().test("abc:ca-a");
		new Test().test("abc:ca_a");
	}

	private void test(String string) {
		Matcher m = commandPattern.matcher(string);
		if (m.find()) {
			System.out.println(m.group());
		} else {
			return;
		}
		int s = m.end();
		m = parameterPattern.matcher(string);
		while (m.find(s)) {
			System.out.println("\t" + m.group());
			s = m.end();
		}
	}
}
