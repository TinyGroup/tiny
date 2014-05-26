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
package org.tinygroup.jspengine.common.util.logging;

import java.util.logging.Logger;

/**
 * Class LogDomains
 */
public class LogDomains
{

    /**
     * DOMAIN_ROOT the prefix for the logger name. This is public only
     * so it can be accessed w/in the ias package space.
     */
    public static final String DOMAIN_ROOT = "javax.";

    /**
     * RESOURCE_BUNDLE the name of the logging resource bundles.
     */
    public static final String RESOURCE_BUNDLE = "LogStrings";

    /**
     * Package where the resource bundle is located
     */
    public static final String PACKAGE = "com.sun.common.util.logging.";
    /**
     * Field
     */
    public static final String CMN_LOGGER = DOMAIN_ROOT + "enterprise.system.util.common";

    /**
     * Method getLogger
     *
     *
     * @param name
     *
     * @return
     */
    public static Logger getLogger(String name) {
        return Logger.getLogger(name, PACKAGE + RESOURCE_BUNDLE);
    }
}
