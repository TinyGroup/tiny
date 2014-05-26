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
package org.tinygroup.context2object.util;

import org.tinygroup.context.Context;
import org.tinygroup.context2object.fileresolver.GeneratorFileProcessor;
import org.tinygroup.context2object.impl.ClassNameObjectGenerator;
import org.tinygroup.event.Parameter;
import org.tinygroup.springutil.SpringUtil;

public final class Context2ObjectUtil {
	private Context2ObjectUtil() {

	}

	public static Object getObject(Parameter p, Context context) {
		if(context.exist(p.getName()))
			return context.get(p.getName());
//		Object o = context.get(p.getName());
//		if (o != null) {
//			return o;
//		}
		return getObjectByGenerator(p, context);
	}

	private static boolean isNull(String s) {
		return s == null || "".equals(s);
	}

	public static Object getObjectByGenerator(Parameter parameter,
			Context context) {
		String collectionType = parameter.getCollectionType();// 集合类型
		String paramName = parameter.getName();
		String paramType = parameter.getType();
		ClassNameObjectGenerator generator = SpringUtil
				.getBean(GeneratorFileProcessor.CLASSNAME_OBJECT_GENERATOR_BEAN);
		if (!isNull(collectionType)) {// 如果集合类型非空
			return generator.getObjectCollection(paramName, collectionType,
					paramType, context);
		} else if (parameter.isArray()) {// 如果是数组
			return generator.getObjectArray(paramName, paramType, context);
		}
		// 否则就是对象
		return generator.getObject(paramName, paramName, paramType, context);
	}
}
