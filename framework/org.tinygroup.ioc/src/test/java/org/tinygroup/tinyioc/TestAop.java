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

/**
 * Created by luoguo on 13-12-30.
 */
public class TestAop {
    public static void main(String[] args) {
        BeanContainer container = BeanContainerFactory.getBeanContainer();
        container.registerClass(InterceptorImpl.class);
        container.registerClass(HelloImpl.class);
        container.addAop(new AopDefine(".*HelloImpl", "sayHello", ".*", InterceptorImpl.class.getName()));
        Hello hello = container.getBeanByType(Hello.class);
        hello.sayHello("abc");
    }

    public static void main1(String[] args) {
        BeanContainer container = BeanContainerFactory.getBeanContainer();
        container.registerClass(InterceptorImpl.class);
        container.registerClass(HelloImpl.class);
        container.registerClass(Hello1Impl.class);
        container.registerClass(HelloHelperImpl.class);
        container.addAop(new AopDefine(".*Hello1Impl", "sayHello", ".*", InterceptorImpl.class.getName()));
        //        container.addAop(new AopDefine(".*", "set.*", ".*", InterceptorImpl.class.getName()));
        HelloHelper helloFactory = container.getBeanByType(HelloHelper.class);
        helloFactory.sayHello("abc");
        System.out.println(helloFactory.getHelloList().size());
    }
}


