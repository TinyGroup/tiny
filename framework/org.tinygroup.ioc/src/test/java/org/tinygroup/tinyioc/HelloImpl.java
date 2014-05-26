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

import org.tinygroup.tinyioc.impl.BeanContainerImpl;

/**
 * Created by luoguo on 13-12-27.
 */
public class HelloImpl implements Hello {
    public void sayHello(String name) {
        System.out.println("Hello:" + name);
    }

    public static void main(String[] args) {
        BeanContainer container = new BeanContainerImpl();
        container.registerClass(HelloImpl.class);
        Hello hello = container.getBeanByType(Hello.class);
        hello.sayHello("abc");
    }
}
