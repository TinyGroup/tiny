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
package org.tinygroup.weblayer.util;

import org.tinygroup.commons.tools.StringUtil;
import org.springframework.util.AntPathMatcher;


/**
 * 
 * 功能说明: 

 * 开发人员: renhui <br>
 * 开发时间: 2013-5-15 <br>
 * <br>
 */
public class TinyPathMatcher {
	
	AntPathMatcher pathMatcher=new AntPathMatcher();
	
	/**
	 * Combines two patterns into a new pattern that is returned.
	 * <p>This implementation simply concatenates the two patterns, unless the first pattern
	 * contains a file extension match (such as {@code *.html}. In that case, the second pattern
	 * should be included in the first, or an {@code IllegalArgumentException} is thrown.
	 * <p>For example: <table>
	 * <tr><th>Pattern 1</th><th>Pattern 2</th><th>Result</th></tr> <tr><td>/hotels</td><td>{@code
	 * null}</td><td>/hotels</td></tr> <tr><td>{@code null}</td><td>/hotels</td><td>/hotels</td></tr>
	 * <tr><td>/hotels</td><td>/bookings</td><td>/hotels/bookings</td></tr> <tr><td>/hotels</td><td>bookings</td><td>/hotels/bookings</td></tr>
	 * <tr><td>/hotels/*</td><td>/bookings</td><td>/hotels/bookings</td></tr> <tr><td>/hotels/&#42;&#42;</td><td>/bookings</td><td>/hotels/&#42;&#42;/bookings</td></tr>
	 * <tr><td>/hotels</td><td>{hotel}</td><td>/hotels/{hotel}</td></tr> <tr><td>/hotels/*</td><td>{hotel}</td><td>/hotels/{hotel}</td></tr>
	 * <tr><td>/hotels/&#42;&#42;</td><td>{hotel}</td><td>/hotels/&#42;&#42;/{hotel}</td></tr>
	 * <tr><td>/*.html</td><td>/hotels.html</td><td>/hotels.html</td></tr> <tr><td>/*.html</td><td>/hotels</td><td>/hotels.html</td></tr>
	 * <tr><td>/*.html</td><td>/*.txt</td><td>IllegalArgumentException</td></tr> </table>
	 * @param pattern1 the first pattern
	 * @param pattern2 the second pattern
	 * @return the combination of the two patterns
	 * @throws IllegalArgumentException when the two patterns cannot be combined
	 */
	public String combine(String pattern1, String pattern2) {
		if (StringUtil.isEmpty(pattern1) && StringUtil.isEmpty(pattern2)) {
			return "";
		}
		else if (StringUtil.isEmpty(pattern1)) {
			return pattern2;
		}
		else if (StringUtil.isEmpty(pattern2)) {
			return pattern1;
		}
		else if (pathMatcher.match(pattern1, pattern2)) {
			return pattern2;
		}
		else if (pattern1.endsWith("/*")) {
			if (pattern2.startsWith("/")) {
				// /hotels/* + /booking -> /hotels/booking
				return pattern1.substring(0, pattern1.length() - 1) + pattern2.substring(1);
			}
			else {
				// /hotels/* + booking -> /hotels/booking
				return pattern1.substring(0, pattern1.length() - 1) + pattern2;
			}
		}
		else if (pattern1.endsWith("/**")) {
			if (pattern2.startsWith("/")) {
				// /hotels/** + /booking -> /hotels/**/booking
				return pattern1 + pattern2;
			}
			else {
				// /hotels/** + booking -> /hotels/**/booking
				return pattern1 + "/" + pattern2;
			}
		}
		else {
			int dotPos1 = pattern1.indexOf('.');
			if (dotPos1 == -1) {
				// simply concatenate the two patterns
				if (pattern1.endsWith("/") || pattern2.startsWith("/")) {
					return pattern1 + pattern2;
				}
				else {
					return pattern1 + "/" + pattern2;
				}
			}
			String fileName1 = pattern1.substring(0, dotPos1);
			String extension1 = pattern1.substring(dotPos1);
			String fileName2;
			String extension2;
			int dotPos2 = pattern2.indexOf('.');
			if (dotPos2 != -1) {
				fileName2 = pattern2.substring(0, dotPos2);
				extension2 = pattern2.substring(dotPos2);
			}
			else {
				fileName2 = pattern2;
				extension2 = "";
			}
			String fileName = fileName1.endsWith("*") ? fileName2 : fileName1;
			String extension = extension1.startsWith("*") ? extension2 : extension1;

			return fileName + extension;
		}
	}

}
