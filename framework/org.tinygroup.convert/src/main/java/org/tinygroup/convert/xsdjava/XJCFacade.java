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

import org.tinygroup.convert.ConvertException;

import java.lang.reflect.Method;

/**
 * A shabby driver to invoke XJC1 or XJC2 depending on the command line switch.
 * <p/>
 * <p/>
 * This class is compiled with -source 1.2 so that we can report a nice
 * user-friendly "you require Tiger" error message.
 *
 * @author Kohsuke Kawaguchi
 */
public final class XJCFacade {
    private XJCFacade() {
    }

    @SuppressWarnings("rawtypes")
    public static void execute(String[] args) throws ConvertException {

        try {
            ClassLoader cl = SecureLoader.getClassClassLoader(XJCFacade.class);

            Class driver = cl
                    .loadClass("org.tinygroup.convert.xsdjava.DriverWrapper");
            Method mainMethod = driver.getDeclaredMethod("main",
                    new Class[]{String[].class});
            mainMethod.invoke(null, new Object[]{args});
        } catch (Exception e) {
            throw new ConvertException(e);
        }
    }


}
