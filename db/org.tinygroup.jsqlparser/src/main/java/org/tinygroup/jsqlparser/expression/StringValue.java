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

/**
 * A string as in 'example_string'
 */
public class StringValue implements Expression {

    private String value = "";

    public StringValue() {

    }

    public StringValue(String escapedValue) {
        // romoving "'" at the start and at the end
        value = escapedValue.substring(1, escapedValue.length() - 1);
    }

    public String getValue() {
        return value;
    }

    public String getNotExcapedValue() {
        StringBuilder buffer = new StringBuilder(value);
        int index = 0;
        int deletesNum = 0;
        while ((index = value.indexOf("''", index)) != -1) {
            buffer.deleteCharAt(index - deletesNum);
            index += 2;
            deletesNum++;
        }
        return buffer.toString();
    }

    public void setValue(String string) {
        value = string;
    }


    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }


    public String toString() {
        return "'" + value + "'";
    }
}
