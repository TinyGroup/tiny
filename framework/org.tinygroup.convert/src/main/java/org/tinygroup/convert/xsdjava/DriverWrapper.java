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

import com.sun.tools.xjc.Driver;
import com.sun.tools.xjc.util.Util;
import org.tinygroup.convert.ConvertException;

/**
 * 功能说明:对原生Driver进行重写
 * <p/>
 * 开发人员: renhui <br>
 * 开发时间: 2013-8-27 <br>
 * <br>
 */
public final class DriverWrapper {
    private DriverWrapper() {

    }

    public static void main(final String[] args) throws ConvertException {
        // use the platform default proxy if available.
        // see sun.net.spi.DefaultProxySelector for details.
        try {
            System.setProperty("java.net.useSystemProxies", "true");
            if (Util.getSystemProperty(Driver.class, "noThreadSwap") != null) {
                // for the ease of debugging
                originalMain(args);
            }
            // run all the work in another thread so that the -Xss option
            // will take effect when compiling a large schema. See
            // http://developer.java.sun.com/developer/bugParade/bugs/4362291.html
            final Exception[] ex = new Exception[1];

            Thread th = new Thread() {

                public void run() {
                    try {
                        originalMain(args);
                    } catch (Exception e) {
                        ex[0] = e;
                    }
                }
            };
            th.start();
            th.join();

            if (ex[0] != null) {
                throw new ConvertException(ex[0]);
            }
        } catch (Exception e) {
            throw new ConvertException(e);
        }

    }

    private static void originalMain(String[] args) throws ConvertException {
        try {
            Driver.run(args, System.out, System.out);
        } catch (Exception e) {
            throw new ConvertException(e);
        }
    }
}
