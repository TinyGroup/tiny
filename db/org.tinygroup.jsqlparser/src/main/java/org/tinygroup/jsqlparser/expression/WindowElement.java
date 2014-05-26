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
package org.tinygroup.jsqlparser.expression;

public class WindowElement {

    public enum Type {

        ROWS,
        RANGE
    }

    private Type type;
    private WindowOffset offset;
    private WindowRange range;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public WindowOffset getOffset() {
        return offset;
    }

    public void setOffset(WindowOffset offset) {
        this.offset = offset;
    }

    public WindowRange getRange() {
        return range;
    }

    public void setRange(WindowRange range) {
        this.range = range;
    }


    public String toString() {
        StringBuilder buffer = new StringBuilder(type.toString());
        
        if (offset != null) {
            buffer.append(offset.toString());
        } else if (range != null) {
            buffer.append(range.toString());
        }

        return buffer.toString();
    }
}
