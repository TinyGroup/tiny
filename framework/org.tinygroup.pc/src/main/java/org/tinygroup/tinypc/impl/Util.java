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
package org.tinygroup.tinypc.impl;

import java.util.Random;
import java.util.UUID;

/**
 * Created by luoguo on 14-1-8.
 */
public final class Util {
    private Util() {
    }

    private static Random random = new Random(System.currentTimeMillis());

    public static String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String getRemoteName(String type, String id) {
        return String.format("%s|%s", type, id);
    }

    public static int randomIndex(int size) {
        int index = random.nextInt();
        if (index < 0) {
            index = -index;
        }
        index = (index % size);
        return index;
    }
}
