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
package org.tinygroup.commons.tools;

public class ValueUtil {
	
	public static Object getValue(String obj,String type){
		if ("java.lang.Integer".equals(type)
				|| "Integer".equals(type)) {
			return Integer.valueOf(obj);
		} else if ("int".equals(type)) {
			return Integer.parseInt(obj);
		} else if ("java.lang.Byte".equals(type)
				|| "Byte".equals(type)) {
			return Byte.valueOf(obj);
		} else if ("byte".equals(type)) {
			return Byte.parseByte(obj);
		} else if ("java.lang.Boolean".equals(type)
				|| "Boolean".equals(type)) {
			return Boolean.valueOf(obj);
		} else if ("boolean".equals(type)) {
			return Boolean.parseBoolean(obj);
		} else if ("java.lang.Character".equals(type)
				|| "Character".equals(type)) {
			return Character.valueOf((obj).toCharArray()[0]);
		} else if ("char".equals(type)) {
			return (obj).toCharArray()[0];
		} else if ("java.lang.Double".equals(type)
				|| "Double".equals(type)) {
			return Double.valueOf(obj);
		} else if ("double".equals(type)) {
			return Double.parseDouble(obj);
		} else if ("java.lang.Short".equals(type)
				|| "Short".equals(type)) {
			return Short.valueOf(obj);
		} else if ("short".equals(type)) {
			return Short.parseShort(obj);
		} else if ("java.lang.Long".equals(type)
				|| "Long".equals(type)) {
			return Long.valueOf(obj);
		} else if ("long".equals(type)) {
			return Long.parseLong(obj);
		} else if ("java.lang.Float".equals(type)
				|| "Float".equals(type)) {
			return Float.valueOf(obj);
		} else if ("float".equals(type)) {
			return Float.parseFloat(obj);
		} else {
			return obj;
		}
	}
}
