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
package org.tinygroup.commons.beanutil;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.tinygroup.commons.tools.ClassUtil;

public class BeanUtil {
	public static String[] getMethodParameterName(Class<?> clazz, Method method) {
		try {
			ClassPool pool = ClassPool.getDefault();
			pool.insertClassPath(new ClassClassPath(clazz));
			CtClass cc = pool.get(clazz.getName());
			CtMethod cm = cc.getDeclaredMethod(method.getName(),
					getParaClasses(pool, cc, method));

			// 使用javaassist的反射方法获取方法的参数名
			MethodInfo methodInfo = cm.getMethodInfo();
			CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
			LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute
					.getAttribute(LocalVariableAttribute.tag);
			String[] paramNames = new String[cm.getParameterTypes().length];
			int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
			for (int i = 0; i < paramNames.length; i++) {
				paramNames[i] = attr.variableName(i + pos);
			}
			return paramNames;

		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private static CtClass[] getParaClasses(ClassPool pool, CtClass cc,
			Method method) throws NotFoundException {
		CtClass[] paType = null;
		Class<?>[] parameterTypes = method.getParameterTypes();
		if (parameterTypes != null && parameterTypes.length > 0) {
			paType = new CtClass[parameterTypes.length];
			for (int i = 0; i < parameterTypes.length; i++) {
				paType[i] = pool.get(parameterTypes[i].getName());
			}
		}
		return paType;
	}

	public static Object deepCopy(Object orig) throws Exception {
		Object dest = orig.getClass().newInstance();
		PropertyDescriptor[] origDescriptors = PropertyUtils
				.getPropertyDescriptors(orig);
		for (PropertyDescriptor propertyDescriptor : origDescriptors) {
			String name = propertyDescriptor.getName();
			if (PropertyUtils.isReadable(orig, name)
					&& PropertyUtils.isWriteable(dest, name)) {
				Object value = PropertyUtils.getSimpleProperty(orig, name);
				Object valueDest = null;
				if (value != null && canDeepCopyObject(value)) {
					if(value instanceof Collection){
						Collection coll=(Collection)value;
						Collection newColl=createApproximateCollection(value);
						Iterator it= coll.iterator();
						while(it.hasNext()){
							newColl.add(deepCopy(it.next()));
						}
						valueDest=newColl;
					}else if(value.getClass().isArray()){
						Object[] values=(Object[])value;
						Object[] newValues=new Object[values.length];
						for (int i = 0; i < newValues.length; i++) {
							newValues[i]=deepCopy(values[i]);
						}
						valueDest=newValues;
					}else if(value instanceof Map){
						Map map=(Map)value;
						Map newMap=createApproximateMap(map);
						for (Object key : map.keySet()) {
							newMap.put(key, deepCopy(map.get(key)));
						}
						valueDest=newMap;
					}else{
						valueDest=deepCopy(value);
					}
				} else {
					valueDest = value;
				}
				PropertyUtils.setSimpleProperty(dest, name, valueDest);
			}

		}
		return dest;

	}

	private static boolean canDeepCopyObject(Object value) {
		if (ClassUtil.getPrimitiveType(value.getClass()) != null) {
			return false;
		}
		if (value instanceof String) {
			return false;
		}
		return true;
	}

	public static Collection createApproximateCollection(Object collection) {
		if (collection instanceof LinkedList) {
			return new LinkedList();
		}
		else if (collection instanceof List) {
			return new ArrayList();
		}
		else if (collection instanceof SortedSet) {
			return new TreeSet(((SortedSet) collection).comparator());
		}
		else {
			return new LinkedHashSet();
		}
	}
	
	public static Map createApproximateMap(Object map) {
		if (map instanceof SortedMap) {
			return new TreeMap(((SortedMap) map).comparator());
		}
		else {
			return new LinkedHashMap();
		}
	}
}
