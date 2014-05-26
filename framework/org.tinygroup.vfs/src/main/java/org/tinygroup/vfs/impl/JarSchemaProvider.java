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
package org.tinygroup.vfs.impl;

import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.SchemaProvider;

public class JarSchemaProvider implements SchemaProvider {

    public static final String JAR = ".jar";
    public static final String JAR_PROTOCOL = "jar:";

    public FileObject resolver(String resourceResolve) {
        String resource = resourceResolve;
        if (resource.startsWith(JAR_PROTOCOL)) {
            resource = resource.substring(JAR_PROTOCOL.length());
        } else if (resource.startsWith(FileSchemaProvider.FILE_PROTOCOL)) {
            resource = resource.substring(FileSchemaProvider.FILE_PROTOCOL.length());
        }
        if (resource.startsWith(FileSchemaProvider.FILE_PROTOCOL)) {
            resource = resource.substring(FileSchemaProvider.FILE_PROTOCOL.length());
        }
        return new JarFileObject(this, resource);
    }

    public boolean isMatch(String resource) {
        String lowerCase = resource.toLowerCase();
        return lowerCase.startsWith(JAR_PROTOCOL) || lowerCase.endsWith(JAR);
    }

    public String getSchema() {
        return JAR_PROTOCOL;
    }

}
