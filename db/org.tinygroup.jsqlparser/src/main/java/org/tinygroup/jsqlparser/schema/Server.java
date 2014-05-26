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
package org.tinygroup.jsqlparser.schema;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Server implements MultiPartName {
    public static final Pattern SERVER_PATTERN = Pattern.compile("\\[([^\\]]+?)(?:\\\\([^\\]]+))?\\]");

    private String serverName;
    private String instanceName;

    public Server(){

    }
    public Server(String serverAndInstanceName) {
        if (serverAndInstanceName != null) {
            final Matcher matcher = SERVER_PATTERN.matcher(serverAndInstanceName);
            if (!matcher.find()) {
                throw new IllegalArgumentException(String.format("%s is not a valid database reference", serverAndInstanceName));
            }
            setServerName(matcher.group(1));
            setInstanceName(matcher.group(2));
        }
    }

    public Server(String serverName, String instanceName) {
        setServerName(serverName);
        setInstanceName(instanceName);
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }


    public String getFullyQualifiedName() {
        if (serverName != null && !(serverName == null || serverName.length() == 0) && instanceName != null && !(instanceName == null || instanceName.length() == 0)) {
            return String.format("[%s\\%s]", serverName, instanceName);
        } else if (serverName != null && !(serverName == null || serverName.length() == 0)) {
            return String.format("[%s]", serverName);
        } else {
            return "";
        }
    }


    public String toString() {
        return getFullyQualifiedName();
    }
}
