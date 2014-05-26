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

import java.lang.reflect.Type;

/**
 * Created by luoguo on 13-12-27.
 */
public class TestSuperClass {

    public static void main(String[] args) {
        System.out.println(isSubClass(B.class, A.class));
        System.out.println(isSubClass(C.class, A.class));
        System.out.println(isSubClass(C.class, I1.class));
    }

    public static boolean isSubClass(Class a, Class b) {
        Type genericSuperclass = a.getGenericSuperclass();
        for (Type type : a.getGenericInterfaces()) {
            if (type.equals(b)) {
                return true;
            }
            boolean is = isSubClass((Class) type, b);
            if (is) {
                return true;
            }
        }
        if (genericSuperclass != null) {
            if (genericSuperclass.equals(b)) {
                return true;
            } else {
                boolean is = isSubClass((Class) genericSuperclass, b);
                if (is) {
                    return true;
                }
            }
        }
        return false;
    }


    class A implements I2 {

    }

    class B extends A {

    }

    class C extends B {

    }

    interface I1 {

    }

    interface I2 extends I1 {

    }
}
