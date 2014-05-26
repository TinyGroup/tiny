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

/*
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2013 JSQLParser
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */

import junit.framework.*;

public class ServerTest extends TestCase {

    public void testServerNameParsing() throws Exception {
        final String serverName = "LOCALHOST";

        final String fullServerName = String.format("[%s]", serverName);
        final Server server = new Server(fullServerName);

        Assert.assertEquals(serverName, server.getServerName());
        Assert.assertEquals(fullServerName, server.toString());
    }

    public void testServerNameAndInstanceParsing() throws Exception {
        final String serverName = "LOCALHOST";
        final String serverInstanceName = "SQLSERVER";

        final String fullServerName = String.format("[%s\\%s]", serverName, serverInstanceName);
        final Server server = new Server(fullServerName);

        Assert.assertEquals(serverName, server.getServerName());
        Assert.assertEquals(serverInstanceName, server.getInstanceName());
        Assert.assertEquals(fullServerName, server.toString());

    }
}
