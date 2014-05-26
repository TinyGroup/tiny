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
package org.tinygroup.jsqlparser.expression.operators.relational;

import org.tinygroup.jsqlparser.expression.ExpressionVisitor;

public class NotEqualsTo extends OldOracleJoinBinaryExpression {

    private final String operator;

    public NotEqualsTo() {
        operator = "<>";
    }

    public NotEqualsTo(String operator) {
        this.operator = operator;
        if (!"!=".equals(operator) && !"<>".equals(operator)) {
            throw new IllegalArgumentException("only <> or != allowed");
        }
    }


    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }


    public String getStringExpression() {
        return operator;
    }
}
