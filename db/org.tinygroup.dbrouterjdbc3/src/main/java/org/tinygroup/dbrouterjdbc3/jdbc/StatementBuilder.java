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
package org.tinygroup.dbrouterjdbc3.jdbc;

/**
 * 
 * 功能说明: 

 * 开发人员: renhui <br>
 * 开发时间: 2014-1-9 <br>
 * <br>
 */
public class StatementBuilder {

    private final StringBuilder builder = new StringBuilder();
    private int index;

    /**
     * Create a new builder.
     */
    public StatementBuilder() {
        // nothing to do
    }

    /**
     * Create a new builder.
     *
     * @param string the initial string
     */
    public StatementBuilder(String string) {
        builder.append(string);
    }

    /**
     * Append a text.
     *
     * @param s the text to append
     * @return itself
     */
    public StatementBuilder append(String s) {
        builder.append(s);
        return this;
    }

    /**
     * Append a character.
     *
     * @param c the character to append
     * @return itself
     */
    public StatementBuilder append(char c) {
        builder.append(c);
        return this;
    }

    /**
     * Append a number.
     *
     * @param x the number to append
     * @return itself
     */
    public StatementBuilder append(long x) {
        builder.append(x);
        return this;
    }

    /**
     * Reset the loop counter.
     *
     * @return itself
     */
    public StatementBuilder resetCount() {
        index = 0;
        return this;
    }

    /**
     * Append a text, but only if appendExceptFirst was never called.
     *
     * @param s the text to append
     */
    public void appendOnlyFirst(String s) {
        if (index == 0) {
            builder.append(s);
        }
    }

    /**
     * Append a text, except when this method is called the first time.
     *
     * @param s the text to append
     */
    public void appendExceptFirst(String s) {
        if (index++ > 0) {
            builder.append(s);
        }
    }


    public String toString() {
        return builder.toString();
    }

    /**
     * Get the length.
     *
     * @return the length
     */
    public int length() {
        return builder.length();
    }

}
