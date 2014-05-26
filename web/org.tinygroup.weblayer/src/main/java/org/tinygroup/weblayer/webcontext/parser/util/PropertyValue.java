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
package org.tinygroup.weblayer.webcontext.parser.util;

import java.beans.PropertyDescriptor;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyValues;

/**
 * Object to hold information and value for an individual bean property.
 * Using an object here, rather than just storing all properties in
 * a map keyed by property name, allows for more flexibility, and the
 * ability to handle indexed properties etc in an optimized way.
 *
 * <p>Note that the value doesn't need to be the final required type:
 * A {@link BeanWrapper} implementation should handle any necessary conversion,
 * as this object doesn't know anything about the objects it will be applied to.
 *
 * @author Rod Johnson
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 13 May 2001
 * @see PropertyValues
 * @see BeanWrapper
 */
public class PropertyValue extends org.springframework.beans.PropertyValue {

	private Object source;

	/** Package-visible field that indicates whether conversion is necessary */
	volatile Boolean conversionNecessary;

	/** Package-visible field for caching the resolved property path tokens */
	volatile Object resolvedTokens;

	/** Package-visible field for caching the resolved PropertyDescriptor */
	volatile PropertyDescriptor resolvedDescriptor;


	/**
	 * Create a new PropertyValue instance.
	 * @param name the name of the property (never <code>null</code>)
	 * @param value the value of the property (possibly before type conversion)
	 */
	public PropertyValue(String name, Object value) {
		super(name, value);
	}

	/**
	 * Copy constructor.
	 * @param original the PropertyValue to copy (never <code>null</code>)
	 */
	public PropertyValue(PropertyValue original) {
		super(original);
	}

	/**
	 * Constructor that exposes a new value for an original value holder.
	 * The original holder will be exposed as source of the new holder.
	 * @param original the PropertyValue to link to (never <code>null</code>)
	 * @param newValue the new value to apply
	 */
	public PropertyValue(PropertyValue original, Object newValue) {
		super(original, newValue);
	}

	/**
	 * Return the original PropertyValue instance for this value holder.
	 * @return the original PropertyValue (either a source of this
	 * value holder or this value holder itself).
	 */
	public PropertyValue getOriginalPropertyValue() {
		PropertyValue original = this;
		while (original.source instanceof PropertyValue && original.source != original) {
			original = (PropertyValue) original.source;
		}
		return original;
	}

}
