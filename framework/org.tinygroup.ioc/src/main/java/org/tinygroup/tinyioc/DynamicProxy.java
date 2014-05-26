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
package org.tinygroup.tinyioc;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by luoguo on 13-12-30.
 */
public class DynamicProxy implements MethodInterceptor {
    List<AopDefine> aopDefineList = new ArrayList<AopDefine>();

    public void addAop(AopDefine aopDefine) {
        aopDefineList.add(aopDefine);
    }

    public <T> boolean isMatchClassName(String className) {
        className = className.split("[$][$]")[0];
        for (AopDefine aopDefine : aopDefineList) {
            Matcher matcher = aopDefine.getClassPattern().matcher(className);
            if (matcher.find() && matcher.group().length() == className.length()) {
                return true;
            }
        }
        return false;
    }


    public <T> T getProxyObject(Class<T> cls) {
        return (T) Enhancer.create(cls, this);
    }

    public Object intercept(Object target, Method method, Object[] args, MethodProxy methodProxy)
            throws Throwable {
        before(target, method, args);
        Object result = null;
        try {
            result = methodProxy.invokeSuper(target, args);
        } catch (Exception e) {
            exception(target, method, e, args);
            throw e;
        }
        after(target, method, args);
        return result;
    }


    private void exception(Object target, Method method, Throwable e, Object[] args) {
        if (!isMatchClassName(target.getClass().getName())) {
            return;
        }
        for (AopDefine aopDefine : aopDefineList) {
            String exceptionClassName = e.getClass().getName();
            Matcher matcher = aopDefine.getExceptionPattern().matcher(exceptionClassName);
            Object interceptor = BeanContainerFactory.getBeanContainer().getBeanByType(aopDefine.getInterceptorTypeName());
            if ((interceptor instanceof InterceptorException) && (matcher.find()
                    && matcher.group().length() == exceptionClassName.length())) {
                InterceptorException interceptorException = (InterceptorException) interceptor;
                interceptorException.exception(target, method, e, args);
            }
        }
    }

    private void after(Object target, Method method, Object[] args) {
        if (!isMatchClassName(target.getClass().getName())) {
            return;
        }
        for (AopDefine aopDefine : aopDefineList) {
            String methodName = method.getName();
            Matcher matcher = aopDefine.getMethodPattern().matcher(methodName);
            Object interceptor = BeanContainerFactory.getBeanContainer().getBeanByType(aopDefine.getInterceptorTypeName());
            if ((interceptor instanceof InterceptorAfter) && (matcher.find()
                    && matcher.group().length() == methodName.length())) {
                InterceptorAfter interceptorAfter = (InterceptorAfter) interceptor;
                interceptorAfter.after(target, method, args);
            }
        }
    }


    private void before(Object target, Method method, Object[] args) {
        if (!isMatchClassName(target.getClass().getName())) {
            return;
        }
        for (AopDefine aopDefine : aopDefineList) {
            String methodName = method.getName();
            Matcher matcher = aopDefine.getMethodPattern().matcher(methodName);
            Object interceptor = BeanContainerFactory.getBeanContainer().getBeanByType(aopDefine.getInterceptorTypeName());
            if ((interceptor instanceof InterceptorBefore) && (matcher.find()
                    && matcher.group().length() == methodName.length())) {
                InterceptorBefore interceptorBefore = (InterceptorBefore) interceptor;
                interceptorBefore.before(target, method, args);
            }
        }
    }
}
