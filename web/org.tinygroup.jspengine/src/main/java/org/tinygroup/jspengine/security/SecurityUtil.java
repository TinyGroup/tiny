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
package org.tinygroup.jspengine.security;

import org.tinygroup.jspengine.Constants;

/**
 * Util class for Security related operations.
 *
 * @author Jean-Francois Arcand
 */

public final class SecurityUtil{
    
    private static boolean packageDefinitionEnabled =  
         System.getProperty("package.definition") == null ? false : true;
    
    /**
     * Return the <code>SecurityManager</code> only if Security is enabled AND
     * package protection mechanism is enabled.
     */
    public static boolean isPackageProtectionEnabled(){
        if (packageDefinitionEnabled && Constants.IS_SECURITY_ENABLED){
            return true;
        }
        return false;
    }
    
    
}
