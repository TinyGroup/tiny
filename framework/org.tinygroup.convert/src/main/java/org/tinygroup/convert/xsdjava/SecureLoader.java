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
package org.tinygroup.convert.xsdjava;

/**
 * Class defined for safe calls of getClassLoader methods of any kind
 * (context/system/class classloader. This MUST be package private and defined
 * in every package which uses such invocations.
 *
 * @author snajper
 */
public final class SecureLoader {
    private SecureLoader() {

    }

    static ClassLoader getContextClassLoader() {
        if (System.getSecurityManager() == null) {
            return Thread.currentThread().getContextClassLoader();
        } else {
            return (ClassLoader) java.security.AccessController
                    .doPrivileged(new java.security.PrivilegedAction() {
                        public java.lang.Object run() {
                            return Thread.currentThread()
                                    .getContextClassLoader();
                        }
                    });
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    static ClassLoader getClassClassLoader(final Class c) {
        if (System.getSecurityManager() == null) {
            return c.getClassLoader();
        } else {
            return (ClassLoader) java.security.AccessController
                    .doPrivileged(new java.security.PrivilegedAction() {
                        public java.lang.Object run() {
                            return c.getClassLoader();
                        }
                    });
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    static ClassLoader getSystemClassLoader() {
        if (System.getSecurityManager() == null) {
            return ClassLoader.getSystemClassLoader();
        } else {
            return (ClassLoader) java.security.AccessController
                    .doPrivileged(new java.security.PrivilegedAction() {
                        public java.lang.Object run() {
                            return ClassLoader.getSystemClassLoader();
                        }
                    });
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    static void setContextClassLoader(final ClassLoader cl) {
        if (System.getSecurityManager() == null) {
            Thread.currentThread().setContextClassLoader(cl);
        } else {
            java.security.AccessController
                    .doPrivileged(new java.security.PrivilegedAction() {
                        public java.lang.Object run() {
                            Thread.currentThread().setContextClassLoader(cl);
                            return null;
                        }
                    });
        }
    }

}
