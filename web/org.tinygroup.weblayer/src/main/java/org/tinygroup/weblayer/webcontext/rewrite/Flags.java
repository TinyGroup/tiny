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
package org.tinygroup.weblayer.webcontext.rewrite;

import static org.tinygroup.commons.tools.ArrayUtil.*;
import static org.tinygroup.commons.tools.Assert.*;
import static org.tinygroup.commons.tools.BasicConstant.*;
import static org.tinygroup.commons.tools.StringUtil.*;

import org.tinygroup.commons.tools.ToStringBuilder.CollectionBuilder;


public class Flags {
    private final String[] flags;

    public Flags() {
        this((String[]) null);
    }

    public Flags(String... flags) {
        this.flags = flags == null ? EMPTY_STRING_ARRAY : flags;
    }

    public boolean isEmpty() {
        return isEmptyArray(flags);
    }

    protected String[] getFlags() {
        return flags;
    }

    /** 检查flags，如果存在，则返回<code>true</code>。 */
    protected boolean hasFlags(String... names) {
        assertTrue(!isEmptyArray(names), "names");

        for (String flag : flags) {
            for (String name : names) {
                if (flag.startsWith(name)) {
                    // flag或F=*
                    if (flag.length() == name.length() || flag.charAt(name.length()) == '=') {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /** 如果flag不存在，则返回<code>null</code>。如果flag无值，则返回空字符串。否则返回值。 */
    protected String getFlagValue(String... names) {
        assertTrue(!isEmptyArray(names), "names");

        for (String flag : flags) {
            for (String name : names) {
                if (flag.startsWith(name)) {
                    if (flag.length() == name.length()) {
                        return "";
                    } else if (flag.charAt(name.length()) == '=') {
                        return trimToEmpty(flag.substring(name.length() + 1));
                    }
                }
            }
        }

        return null;
    }

    
    public String toString() {
        return new CollectionBuilder().setOneLine(true).appendAll(flags).toString();
    }
}
