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
package org.tinygroup.commons.namestrategy.impl;

import org.tinygroup.commons.namestrategy.NameStrategy;

public class CamelCaseStrategy implements NameStrategy {

	/* (non-Javadoc)
	 * @see org.tinygroup.commons.namestrategy.NameStrategy#getPropertyName(java.lang.String)
	 */
	public String getPropertyName(String fieldName) {
		fieldName = fieldName.toLowerCase();
		String[] sa = fieldName.split("_");
		StringBuffer sb = new StringBuffer();
		sb.append(sa[0]);
		for (int i = 1; i < sa.length; i++) {
			sb.append(uppercaseFirstChar(sa[i]));
		}
		return sb.toString();

	}

	public String getFieldName(String propertyName) {
		StringBuffer ret = new StringBuffer();
		ret.append(propertyName.substring(0, 1).toLowerCase());
		for (int i = 1; i < propertyName.length(); i++) {
			if (propertyName.charAt(i) >= 'A' && propertyName.charAt(i) <= 'Z') {
				ret.append("_").append(
						(char) (propertyName.charAt(i) - 'A' + 'a'));
			} else {
				ret.append(propertyName.charAt(i));
			}
		}
		return ret.toString();
	}

	private String uppercaseFirstChar(String str) {
		String ret = str;
		int c = str.charAt(0);
		if (c <= 'z' && c >= 'a') {
			c = c - 'a' + 'A';
			ret = (char) c + str.substring(1);
		}
		return ret;
	}

}
