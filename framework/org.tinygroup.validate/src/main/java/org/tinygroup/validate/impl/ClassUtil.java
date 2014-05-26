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
package org.tinygroup.validate.impl;

import java.util.Collection;
import java.util.Map;

public class ClassUtil {

	/**
	 * 判断该类型是不是包装类型
	 * @param clazz
	 * @return
	 */
	public static boolean isBasicClass(Class<?> clazz) {
		boolean isPrimitive = false;
		try {
			if (clazz.isPrimitive() || clazz.isAssignableFrom(String.class)) {
				isPrimitive = true;
			} else {
				isPrimitive = ((Class<?>) clazz.getField("TYPE").get(null))
						.isPrimitive();
			}
		} catch (Exception e) {
			isPrimitive = false;
		}
		return isPrimitive;
	}

	/**
	 * 判断对象是不是集合类型
	 * @param <T>
	 * @param object
	 * @return
	 */
	public static <T> boolean isCollectTypes(T object) {

		if (object instanceof Collection) {
			return true;
		}
		return false;

	}

	/**
	 * 判断对象是不是映射类型
	 * @param <T>
	 * @param object
	 * @return
	 */
	public static <T> boolean isMapTypes(T object) {

		if (object instanceof Map) {
			return true;
		}
		return false;

	}

	/**
	 * 判断对象是不是集合类型
	 * @param <T>
	 * @param object
	 * @return
	 */
	public static <T> boolean isArray(Class<T> clazz) {

		return clazz.isArray();

	}

	/**
	 * 判断对象是不是集合类型
	 * @param <T>
	 * @param object
	 * @return
	 */
	public static <T> boolean arrayElementIsObjectType(Class<T> clazz) {

		return isArray(clazz) && !isBasicClass(clazz.getComponentType());

	}

	/**
	 * 判断对象是不是集合类型
	 * @param <T>
	 * @param object
	 * @return
	 */
	public static <T> boolean collectionElementIsObjectType(T object) {

		if (isCollectTypes(object)) {
			Class<T> clazz = (Class<T>) object.getClass();
			Collection coll = (Collection) object;
			for (Object object2 : coll) {
				return isBasicClass(object2.getClass());
			}

		}
		return false;

	}

	/**
	 * 采取驼峰规则
	 * 
	 * @param simpleName
	 * @return
	 */
	public static <T> String humpString(Class<T> clazz) {
		String simpleName = clazz.getSimpleName();
		String first = simpleName.charAt(0) + "";
		return first.toLowerCase() + simpleName.substring(1);
	}

}
