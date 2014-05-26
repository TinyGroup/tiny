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
package org.tinygroup.factory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import org.tinygroup.factory.config.Beans;
import org.tinygroup.factory.impl.SimpleFactory;
import org.tinygroup.xstream.XStreamFactory;

/**
 * 
 * 功能说明: beanfactory的管理类
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-2-25 <br>
 * <br>
 */
public final class BeanFactory {
	private static Map<String, Factory> factoryMap = new HashMap<String, Factory>();
	static {
		XStreamFactory.getXStream().processAnnotations(Beans.class);
	}

	public static Map<String, Factory> getFactoryMap() {
		return factoryMap;
	}

	private BeanFactory() {
	}

	public static Factory getFactory() {
		return getFactory("");
	}

	public static String[] getMethodParameterName(Class<?> clazz, Method method) {
		try {
			ClassPool pool = ClassPool.getDefault();
			pool.insertClassPath(new ClassClassPath(clazz));
			CtClass cc = pool.get(clazz.getName());
			CtMethod cm = cc.getDeclaredMethod(method.getName(),
					getParaClasses(pool, method));

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

	private static CtClass[] getParaClasses(ClassPool pool, Method method)
			throws NotFoundException {
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

	public static Factory getFactory(String name) {
		String tmpName = "";
		if (name != null) {
			tmpName = name;
		}
		Factory factory = factoryMap.get(tmpName);
		if (factory == null) {
			factory = new SimpleFactory();
			factoryMap.put(tmpName, factory);
		}
		return factory;
	}

}
