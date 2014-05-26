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
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * 
 * 功能说明: 对象比较工具方法
 * 开发人员: renhui <br>
 * 开发时间: 2014-2-24 <br>
 */
public class EqualsUtil {

	 public static boolean reflectionEquals(Object lhs, Object rhs) {
		 return EqualsBuilder.reflectionEquals(lhs, rhs);
	 }
	
	 public static boolean reflectionEquals(Object lhs, Object rhs, boolean testTransients) {
	     return EqualsBuilder.reflectionEquals(lhs, rhs, testTransients);
	 }
	 
	 public static boolean reflectionEquals(Object lhs, Object rhs, boolean testTransients, Class reflectUpToClass) {
		 return EqualsBuilder.reflectionEquals(lhs, rhs, testTransients, reflectUpToClass);  
	 }
	 
	 public static boolean reflectionEquals(Object lhs, Object rhs, boolean testTransients, Class reflectUpToClass,
	            String[] excludeFields) {
		 return EqualsBuilder.reflectionEquals(lhs, rhs, testTransients, reflectUpToClass, excludeFields);
	 }
	 
	 public static boolean reflectionEquals(Object lhs, Object rhs, String[] excludeFields) {
		 return EqualsBuilder.reflectionEquals(lhs, rhs, excludeFields);
	 }
	 
	 public static boolean reflectionEquals(Object lhs, Object rhs, Collection /*String*/ excludeFields) {
		 return EqualsBuilder.reflectionEquals(lhs, rhs, excludeFields);
	 }
	 
	 public static boolean reflectionCompareEquals(Object lhs, Object rhs,String[] compareFields){
		 return reflectionCompareEquals(lhs, rhs, false, null, compareFields);
	 }
	 
	 public static boolean reflectionCompareEquals(Object lhs, Object rhs,Collection compareFields){
		 return reflectionCompareEquals(lhs, rhs, false, null, CollectionUtil.toNoNullStringArray(compareFields));
	 }
	 
	 public static boolean reflectionCompareEquals(Object lhs, Object rhs,boolean testTransients,Class reflectUpToClass,String[] compareFields) {
		 if (lhs == rhs) {
	            return true;
	        }
	        if (lhs == null || rhs == null) {
	            return false;
	        }
	        // Find the leaf class since there may be transients in the leaf 
	        // class or in classes between the leaf and root.
	        // If we are not testing transients or a subclass has no ivars, 
	        // then a subclass can test equals to a superclass.
	        Class lhsClass = lhs.getClass();
	        Class rhsClass = rhs.getClass();
	        Class testClass;
	        if (lhsClass.isInstance(rhs)) {
	            testClass = lhsClass;
	            if (!rhsClass.isInstance(lhs)) {
	                // rhsClass is a subclass of lhsClass
	                testClass = rhsClass;
	            }
	        } else if (rhsClass.isInstance(lhs)) {
	            testClass = rhsClass;
	            if (!lhsClass.isInstance(rhs)) {
	                // lhsClass is a subclass of rhsClass
	                testClass = lhsClass;
	            }
	        } else {
	            // The two classes are not related.
	            return false;
	        }
	        EqualsBuilder equalsBuilder = new EqualsBuilder();
	        try {
	        	reflectionCompareAppend(lhs, rhs, testClass, equalsBuilder, testTransients, compareFields);
	            while (testClass.getSuperclass() != null && testClass != reflectUpToClass) {
	                testClass = testClass.getSuperclass();
	                reflectionCompareAppend(lhs, rhs, testClass, equalsBuilder, testTransients, compareFields);
	            }
	        } catch (IllegalArgumentException e) {
	            // In this case, we tried to test a subclass vs. a superclass and
	            // the subclass has ivars or the ivars are transient and 
	            // we are testing transients.
	            // If a subclass has ivars that we are trying to test them, we get an
	            // exception and we know that the objects are not equal.
	            return false;
	        }
	        return equalsBuilder.isEquals();
	 }
	 
	 /**
	     * <p>Appends the fields and values defined by the given object of the
	     * given Class.</p>
	     * 
	     * @param lhs  the left hand object
	     * @param rhs  the right hand object
	     * @param clazz  the class to append details of
	     * @param builder  the builder to append to
	     * @param useTransients  whether to test transient fields
	     * @param excludeFields  array of field names to exclude from testing
	     */
	    private static void reflectionCompareAppend(
	        Object lhs,
	        Object rhs,
	        Class clazz,
	        EqualsBuilder builder,
	        boolean useTransients,
	        String[] compareFields) {
	        Field[] fields = clazz.getDeclaredFields();
	        List compareFieldList = compareFields != null ? Arrays.asList(compareFields) : Collections.EMPTY_LIST;
	        AccessibleObject.setAccessible(fields, true);
	        for (int i = 0; i < fields.length && builder.isEquals(); i++) {
	            Field f = fields[i];
	            if (compareFieldList.contains(f.getName())
	                && (f.getName().indexOf('$') == -1)
	                && (useTransients || !Modifier.isTransient(f.getModifiers()))
	                && (!Modifier.isStatic(f.getModifiers()))) {
	                try {
	                    builder.append(f.get(lhs), f.get(rhs));
	                } catch (IllegalAccessException e) {
	                    //this can't happen. Would get a Security exception instead
	                    //throw a runtime exception in case the impossible happens.
	                    throw new InternalError("Unexpected IllegalAccessException");
	                }
	            }
	        }
	    }
	    
}
