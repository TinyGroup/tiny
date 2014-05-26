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
package org.tinygroup.tinydb.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.tinygroup.exception.TinySysRuntimeException;
import org.tinygroup.tinydb.Bean;

/**
 * 
 * 功能说明: bean对象与普通pojo对象相互转换
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-7-30 <br>
 * <br>
 */
public final class TinyBeanUtil {
	
	private TinyBeanUtil() {
	}

	@SuppressWarnings("rawtypes")
	public static <T> T bean2Object(Map bean, Class<T> type) {
		try {
			T target = type.newInstance();
			BeanUtils.populate(target, bean);
			return target;
		} catch (Exception e) {
			throw new TinySysRuntimeException(e);
		}
	}
	
	public static <T> T bean2Object(Map bean, T target) {
		try {
			BeanUtils.populate(target, bean);
			return target;
		} catch (Exception e) {
			throw new TinySysRuntimeException(e);
		}
	}

	public static <T> Bean object2Bean(T object) {
		String className = object.getClass().getSimpleName();
		Bean bean = new Bean(className);
		PropertyDescriptor[] descriptors = org.springframework.beans.BeanUtils
				.getPropertyDescriptors(object.getClass());
		for (PropertyDescriptor descriptor : descriptors) {
			bean.setProperty(descriptor.getName(), getValue(descriptor, object));
		}
		return bean;
	}

	/**
	 * 
	 * 设置bean属性
	 * 
	 * @param object
	 * @param bean
	 * @return
	 */
	public static <T> Bean object2Bean(T object, Bean bean) {
		PropertyDescriptor[] descriptors = org.springframework.beans.BeanUtils
				.getPropertyDescriptors(object.getClass());
		for (PropertyDescriptor descriptor : descriptors) {
			bean.setProperty(descriptor.getName(), getValue(descriptor, object));
		}
		return bean;
	}

	private static <T> Object getValue(PropertyDescriptor descriptor, T obejct) {
		if (descriptor.getReadMethod() != null) {
			Method readMethod = descriptor.getReadMethod();
			if (!Modifier.isPublic(readMethod.getDeclaringClass()
					.getModifiers())) {
				readMethod.setAccessible(true);
			}
			try {
				return readMethod.invoke(obejct, new Object[0]);
			} catch (Exception e) {
				throw new TinySysRuntimeException(e);
			}
		}
		return null;
	}

}
