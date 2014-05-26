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

import java.util.LinkedHashMap;
import java.util.Map;

public class AnnotationAttributes extends LinkedHashMap<String, Object> {

	/**
	 * Create a new, empty {@link AnnotationAttributes} instance.
	 */
	public AnnotationAttributes() {
	}

	/**
	 * Create a new, empty {@link AnnotationAttributes} instance with the given initial
	 * capacity to optimize performance.
	 * @param initialCapacity initial size of the underlying map
	 */
	public AnnotationAttributes(int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * Create a new {@link AnnotationAttributes} instance, wrapping the provided map
	 * and all its key/value pairs.
	 * @param map original source of annotation attribute key/value pairs to wrap
	 * @see #fromMap(Map)
	 */
	public AnnotationAttributes(Map<String, Object> map) {
		super(map);
	}

	/**
	 * Return an {@link AnnotationAttributes} instance based on the given map; if the map
	 * is already an {@code AnnotationAttributes} instance, it is casted and returned
	 * immediately without creating any new instance; otherwise create a new instance by
	 * wrapping the map with the {@link #AnnotationAttributes(Map)} constructor.
	 * @param map original source of annotation attribute key/value pairs
	 */
	public static AnnotationAttributes fromMap(Map<String, Object> map) {
		if (map == null) {
			return null;
		}

		if (map instanceof AnnotationAttributes) {
			return (AnnotationAttributes) map;
		}

		return new AnnotationAttributes(map);
	}

	public String getString(String attributeName) {
		return doGet(attributeName, String.class);
	}

	public String[] getStringArray(String attributeName) {
		return doGet(attributeName, String[].class);
	}

	public boolean getBoolean(String attributeName) {
		return doGet(attributeName, Boolean.class);
	}

	@SuppressWarnings("unchecked")
	public <N extends Number> N getNumber(String attributeName) {
		return (N) doGet(attributeName, Integer.class);
	}

	@SuppressWarnings("unchecked")
	public <E extends Enum<?>> E getEnum(String attributeName) {
		return (E) doGet(attributeName, Enum.class);
	}

	@SuppressWarnings("unchecked")
	public <T> Class<? extends T> getClass(String attributeName) {
		return (Class<T>)doGet(attributeName, Class.class);
	}

	public Class<?>[] getClassArray(String attributeName) {
		return doGet(attributeName, Class[].class);
	}

	public AnnotationAttributes getAnnotation(String attributeName) {
		return doGet(attributeName, AnnotationAttributes.class);
	}

	public AnnotationAttributes[] getAnnotationArray(String attributeName) {
		return doGet(attributeName, AnnotationAttributes[].class);
	}

	@SuppressWarnings("unchecked")
	private <T> T doGet(String attributeName, Class<T> expectedType) {
		Assert.hasText(attributeName, "attributeName must not be null or empty");
		Object value = this.get(attributeName);
		Assert.assertNotNull(value, String.format("Attribute '%s' not found", attributeName));
		Assert.isAssignable(expectedType, value.getClass(),
				String.format("Attribute '%s' is of type [%s], but [%s] was expected. Cause: ",
						attributeName, value.getClass().getSimpleName(), expectedType.getSimpleName()));
		return (T) value;
	}
}