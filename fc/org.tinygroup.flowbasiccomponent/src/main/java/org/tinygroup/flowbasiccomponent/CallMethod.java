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
package org.tinygroup.flowbasiccomponent;

import java.lang.reflect.Method;

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flow.util.FlowElUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.springutil.SpringUtil;

public class CallMethod implements ComponentInterface {
	private Logger logger = LoggerFactory.getLogger(CallMethod.class);
	/**
	 * 方法所在类名，进行静态方法调用时需要
	 */
	private String className;
	/**
	 * 方法所在bean id，进行bean方法调用时需要
	 */
	private String beanName;
	/**
	 * 方法名
	 */
	private String methodName;

	/**
	 * 方法调用所需参数在上下文中的key值，多个值由,分割
	 */
	private String params;

	/**
	 * 方法返回值放入上下文的key
	 */
	private String resultKey;

	public String getResultKey() {
		return resultKey;
	}

	public void setResultKey(String resultKey) {
		this.resultKey = resultKey;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public void execute(Context context) {

		Object[] args = getArguments(context);
		Object result = null;
		if (beanName != null && !"".equals(beanName)) {
			result = execBeanMethod(args);
		} else if (className != null || !"".equals(beanName)) {
			result = execClassMethod(args);
		}
		if (resultKey != null && !"".equals(resultKey)) {
			context.put(resultKey, result);
		}
	}

	private Object execBeanMethod(Object[] args) {
		Object obj = SpringUtil.getBean(beanName);
		if (obj == null) {
			String info = String.format("找不到bean::%s", beanName);
			logger.logMessage(LogLevel.ERROR, info);
			throw new RuntimeException(info);
		}
		Class<?> clazz = obj.getClass();
		Method method = getMethod(clazz, args);

		if (method == null) {
			String info = String.format("bean:%s中没有方法%s", beanName, methodName);
			logger.logMessage(LogLevel.ERROR, info);
			throw new RuntimeException(info);
		}

		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			String info = String.format("执行bean:%s中方法%s时出错", beanName,
					methodName);
			logger.logMessage(LogLevel.ERROR, info);
			throw new RuntimeException(e);
		}
	}

	private Object execClassMethod(Object[] args) {
		Class<?> clazz = null;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			logger.errorMessage("获取类:{className}失败", e, className);
			throw new RuntimeException(e);
		}
		Method method = getMethod(clazz, args);
		if (method == null) {
			String info = String.format("class:%s中没有方法%s", className,
					methodName);
			logger.logMessage(LogLevel.ERROR, info);
			throw new RuntimeException(info);
		}
		try {
			return method.invoke(clazz, args);
		} catch (Exception e) {
			String info = String.format("执行class:%s中方法%s时出错", className,
					methodName);
			logger.logMessage(LogLevel.ERROR, info);
			throw new RuntimeException(e);
		}
	}

	private Method getMethod(Class<?> clazz, Object[] args) {
		for (Method m : clazz.getMethods()) {
			if ((methodName.equals(m.getName()))
					&& (m.getParameterTypes().length == args.length)) {
				return m;
			}
		}
		return null;
	}

	private Object[] getArguments(Context context) {
		Object[] args = null;
		if (params == null)
			return args;
		String[] paramArray = params.split(",");
		args = new Object[paramArray.length];
		for (int i = 0; i < paramArray.length; i++) {
			args[i] = FlowElUtil.execute(paramArray[i], context);
		}
		return args;
	}

}
