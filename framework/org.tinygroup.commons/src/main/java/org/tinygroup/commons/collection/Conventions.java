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
package org.tinygroup.commons.collection;

import java.io.Externalizable;
import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.tinygroup.commons.tools.Assert;
import org.tinygroup.commons.tools.ClassUtil;

/**
 * Provides methods to support various naming and other conventions used
 * throughout the framework. Mainly for internal use within the framework.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 2.0
 */
public abstract class Conventions {

	/**
	 * Suffix added to names when using arrays.
	 */
	private static final String PLURAL_SUFFIX = "List";


	/**
	 * Set of interfaces that are supposed to be ignored
	 * when searching for the 'primary' interface of a proxy.
	 */
	private static final Set<Class<?>> ignoredInterfaces = new HashSet<Class<?>>();

	static {
		ignoredInterfaces.add(Serializable.class);
		ignoredInterfaces.add(Externalizable.class);
		ignoredInterfaces.add(Cloneable.class);
		ignoredInterfaces.add(Comparable.class);
	}


	/**
	 * Determine the conventional variable name for the supplied
	 * <code>Object</code> based on its concrete type. The convention
	 * used is to return the uncapitalized short name of the <code>Class</code>,
	 * according to JavaBeans property naming rules: So,
	 * <code>com.myapp.Product</code> becomes <code>product</code>;
	 * <code>com.myapp.MyProduct</code> becomes <code>myProduct</code>;
	 * <code>com.myapp.UKProduct</code> becomes <code>UKProduct</code>.
	 * <p>For arrays, we use the pluralized version of the array component type.
	 * For <code>Collection</code>s we attempt to 'peek ahead' in the
	 * <code>Collection</code> to determine the component type and
	 * return the pluralized version of that component type.
	 * @param value the value to generate a variable name for
	 * @return the generated variable name
	 */
	public static String getVariableName(Object value) {
		Assert.assertNotNull(value, "Value must not be null");
		Class<?> valueClass;
		boolean pluralize = false;

		if (value.getClass().isArray()) {
			valueClass = value.getClass().getComponentType();
			pluralize = true;
		}
		else if (value instanceof Collection) {
			Collection<?> collection = (Collection<?>) value;
			if (collection.isEmpty()) {
				throw new IllegalArgumentException("Cannot generate variable name for an empty Collection");
			}
			Object valueToCheck = peekAhead(collection);
			valueClass = getClassForValue(valueToCheck);
			pluralize = true;
		}
		else {
			valueClass = getClassForValue(value);
		}

		String name = ClassUtil.getShortNameAsProperty(valueClass);
		return (pluralize ? pluralize(name) : name);
	}




	/**
	 * Return an attribute name qualified by the supplied enclosing {@link Class}. For example,
	 * the attribute name '<code>foo</code>' qualified by {@link Class} '<code>com.myapp.SomeClass</code>'
	 * would be '<code>com.myapp.SomeClass.foo</code>'
	 */
	public static String getQualifiedAttributeName(Class<?> enclosingClass, String attributeName) {
		Assert.assertNotNull(enclosingClass, "'enclosingClass' must not be null");
		Assert.assertNotNull(attributeName, "'attributeName' must not be null");
		return enclosingClass.getName() + "." + attributeName;
	}


	/**
	 * Determines the class to use for naming a variable that contains
	 * the given value.
	 * <p>Will return the class of the given value, except when
	 * encountering a JDK proxy, in which case it will determine
	 * the 'primary' interface implemented by that proxy.
	 * @param value the value to check
	 * @return the class to use for naming a variable
	 */
	private static Class<?> getClassForValue(Object value) {
		Class<?> valueClass = value.getClass();
		if (Proxy.isProxyClass(valueClass)) {
			Class<?>[] ifcs = valueClass.getInterfaces();
			for (Class<?> ifc : ifcs) {
				if (!ignoredInterfaces.contains(ifc)) {
					return ifc;
				}
			}
		}
		else if (valueClass.getName().lastIndexOf('$') != -1 && valueClass.getDeclaringClass() == null) {
			// '$' in the class name but no inner class -
			// assuming it's a special subclass (e.g. by OpenJPA)
			valueClass = valueClass.getSuperclass();
		}
		return valueClass;
	}

	/**
	 * Pluralize the given name.
	 */
	private static String pluralize(String name) {
		return name + PLURAL_SUFFIX;
	}

	/**
	 * Retrieves the <code>Class</code> of an element in the <code>Collection</code>.
	 * The exact element for which the <code>Class</code> is retreived will depend
	 * on the concrete <code>Collection</code> implementation.
	 */
	private static Object peekAhead(Collection<?> collection) {
		Iterator<?> it = collection.iterator();
		if (!it.hasNext()) {
			throw new IllegalStateException(
					"Unable to peek ahead in non-empty collection - no element found");
		}
		Object value = it.next();
		if (value == null) {
			throw new IllegalStateException(
					"Unable to peek ahead in non-empty collection - only null element found");
		}
		return value;
	}

}
