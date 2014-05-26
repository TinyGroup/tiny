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

import com.sun.istack.tools.MaskingClassLoader;
import com.sun.istack.tools.ParallelWorldClassLoader;

import javax.xml.bind.JAXBContext;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Creates a class loader configured to run XJC 1.0/2.0 safely without
 * interference with JAXB 2.0 API in Mustang.
 *
 * @author Kohsuke Kawaguchi
 */
public final class ClassLoaderBuilder {
    private ClassLoaderBuilder() {
    }

    /**
     * Escape hatch in case this class loader hack breaks.
     */
    public static final boolean NO_HACK = Boolean.getBoolean(XJCFacade.class.getName() + ".nohack");

    /**
     * Creates a new class loader that eventually delegates to the given {@link ClassLoader}
     * such that XJC can be loaded by using this classloader.
     *
     * @param string Either "1.0" or "2.0", indicating the version of the -source value.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected static ClassLoader createProtectiveClassLoader(ClassLoader cl, String string) throws ClassNotFoundException, MalformedURLException {
        ClassLoader classLoader = cl;
        // provide an escape hatch
        if (NO_HACK) {
            return classLoader;
        }

        boolean mustang = false;

        if (SecureLoader.getClassClassLoader(JAXBContext.class) == null) {
            // JAXB API is loaded from the bootstrap. We need to override one with ours
            mustang = true;

            List mask = new ArrayList(Arrays.asList(maskedPackages));
            mask.add("javax.xml.bind.");

            classLoader = new MaskingClassLoader(classLoader, mask);

            URL apiUrl = classLoader.getResource("javax/xml/bind/JAXBPermission.class");
            if (apiUrl == null) {
                throw new ClassNotFoundException("There's no JAXB 2.2 API in the classpath");
            }
            classLoader = new URLClassLoader(new URL[]{ParallelWorldClassLoader.toJarUrl(apiUrl)}, classLoader);
        }

        //Leave XJC2 in the publicly visible place
        // and then isolate XJC1 in a child class loader,
        // then use a MaskingClassLoader
        // so that the XJC2 classes in the parent class loader
        //  won't interfere with loading XJC1 classes in a child class loader

        if (string.equals("1.0")) {
            if (!mustang) {
                // if we haven't used Masking ClassLoader, do so now.
                classLoader = new MaskingClassLoader(classLoader, toolPackages);
            }
            classLoader = new ParallelWorldClassLoader(classLoader, "1.0/");
        } else {
            if (mustang) {
                // the whole RI needs to be loaded in a separate class loader
                classLoader = new ParallelWorldClassLoader(classLoader, "");
            }
        }

        return classLoader;
    }


    /**
     * The list of package prefixes we want the
     * {@link MaskingClassLoader} to prevent the parent
     * classLoader from loading
     */
    private static String[] maskedPackages = new String[]{
            // toolPackages + alpha
            "com.sun.tools.",
            "com.sun.codemodel.",
            "com.sun.relaxng.",
            "com.sun.xml.xsom.",
            "com.sun.xml.bind.",
    };

    private static String[] toolPackages = new String[]{
            "com.sun.tools.",
            "com.sun.codemodel.",
            "com.sun.relaxng.",
            "com.sun.xml.xsom."
    };

}
