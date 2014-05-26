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

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * 
 * 功能说明: hashcode的工具方法 开发人员: renhui <br>
 * 开发时间: 2014-2-24 <br>
 */
public class HashCodeUtil {

	private static ThreadLocal registry = new ThreadLocal() {
		protected Object initialValue() {
			// The HashSet implementation is not synchronized,
			// which is just what we need here.
			return new HashSet();
		}
	};

	public static int reflectionHashCode(int initialNonZeroOddNumber,
			int multiplierNonZeroOddNumber, Object object) {
		return HashCodeBuilder.reflectionHashCode(initialNonZeroOddNumber,
				multiplierNonZeroOddNumber, object);
	}

	public static int reflectionHashCode(int initialNonZeroOddNumber,
			int multiplierNonZeroOddNumber, Object object,
			boolean testTransients) {
		return HashCodeBuilder.reflectionHashCode(initialNonZeroOddNumber,
				multiplierNonZeroOddNumber, object, testTransients);
	}

	public static int reflectionHashCode(int initialNonZeroOddNumber,
			int multiplierNonZeroOddNumber, Object object,
			boolean testTransients, Class reflectUpToClass) {
		return HashCodeBuilder.reflectionHashCode(initialNonZeroOddNumber,
				multiplierNonZeroOddNumber, object, testTransients,
				reflectUpToClass);
	}

	public static int reflectionHashCode(int initialNonZeroOddNumber,
			int multiplierNonZeroOddNumber, Object object,
			boolean testTransients, Class reflectUpToClass,
			String[] excludeFields) {
		return HashCodeBuilder.reflectionHashCode(initialNonZeroOddNumber,
				multiplierNonZeroOddNumber, object, testTransients,
				reflectUpToClass, excludeFields);
	}

	public static int reflectionHashCode(Object object) {
		return HashCodeBuilder.reflectionHashCode(object);
	}

	public static int reflectionHashCode(Object object, boolean testTransients) {
		return HashCodeBuilder.reflectionHashCode(object, testTransients);
	}

	public static int reflectionHashCode(Object object, String[] excludeFields) {
		return HashCodeBuilder.reflectionHashCode(object, excludeFields);
	}

	public static int reflectionHashCode(Object object,
			Collection /* String */excludeFields) {
		return HashCodeBuilder.reflectionHashCode(object, excludeFields);
	}

	public static int reflectionCompareHashCode(Object object,
			String[] compareFields) {
		return reflectionCompareHashCode(17, 37, object, false, null,
				compareFields);
	}

	public static int reflectionCompareHashCode(Object object,
			Collection compareFields) {
		return reflectionCompareHashCode(17, 37, object, false, null,
				CollectionUtil.toNoNullStringArray(compareFields));
	}

	public static int reflectionCompareHashCode(int initialNonZeroOddNumber,
			int multiplierNonZeroOddNumber, Object object,
			boolean testTransients, Class reflectUpToClass,
			String[] excludeFields) {

		if (object == null) {
			throw new IllegalArgumentException(
					"The object to build a hash code for must not be null");
		}
		HashCodeBuilder builder = new HashCodeBuilder(initialNonZeroOddNumber,
				multiplierNonZeroOddNumber);
		Class clazz = object.getClass();
		reflectionAppend(object, clazz, builder, testTransients, excludeFields);
		while (clazz.getSuperclass() != null && clazz != reflectUpToClass) {
			clazz = clazz.getSuperclass();
			reflectionAppend(object, clazz, builder, testTransients,
					excludeFields);
		}
		return builder.toHashCode();
	}

	private static void reflectionAppend(Object object, Class clazz,
			HashCodeBuilder builder, boolean useTransients,
			String[] excludeFields) {
		if (isRegistered(object)) {
			return;
		}
		try {
			register(object);
			Field[] fields = clazz.getDeclaredFields();
			List excludedFieldList = excludeFields != null ? Arrays
					.asList(excludeFields) : Collections.EMPTY_LIST;
			AccessibleObject.setAccessible(fields, true);
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				if (!excludedFieldList.contains(field.getName())
						&& (field.getName().indexOf('$') == -1)
						&& (useTransients || !Modifier.isTransient(field
								.getModifiers()))
						&& (!Modifier.isStatic(field.getModifiers()))) {
					try {
						Object fieldValue = field.get(object);
						builder.append(fieldValue);
					} catch (IllegalAccessException e) {
						// this can't happen. Would get a Security exception
						// instead
						// throw a runtime exception in case the impossible
						// happens.
						throw new InternalError(
								"Unexpected IllegalAccessException");
					}
				}
			}
		} finally {
			unregister(object);
		}
	}

	static boolean isRegistered(Object value) {
		return getRegistry().contains(toIdentityHashCodeInteger(value));
	}

	static Set getRegistry() {
		return (Set) registry.get();
	}

	static void unregister(Object value) {
		getRegistry().remove(toIdentityHashCodeInteger(value));
	}

	static void register(Object value) {
		getRegistry().add(toIdentityHashCodeInteger(value));
	}

	private static Integer toIdentityHashCodeInteger(Object value) {
		return new Integer(System.identityHashCode(value));
	}

}
