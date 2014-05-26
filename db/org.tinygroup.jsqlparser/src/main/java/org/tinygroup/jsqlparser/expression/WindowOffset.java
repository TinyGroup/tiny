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

public class WindowOffset {

    public enum Type {

        PRECEDING,
        FOLLOWING,
        CURRENT,
        EXPR
    }
    
    private Expression expression;
    private Type type;

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }


    public String toString() {
        StringBuilder buffer = new StringBuilder();
        if (expression != null) {
            buffer.append(' ').append(expression);
            if (type != null) {
                buffer.append(' ');
                buffer.append(type);
            }
        } else {
            switch (type) {
                case PRECEDING:
                    buffer.append(" UNBOUNDED PRECEDING");
                    break;
                case FOLLOWING:
                    buffer.append(" UNBOUNDED FOLLOWING");
                    break;
                case CURRENT:
                    buffer.append(" CURRENT ROW");
                    break;
                default:
                    break;
            }
        }
        return buffer.toString();
    }
}
