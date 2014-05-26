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
package org.tinygroup.jsqlparser.statement.select;

import org.tinygroup.jsqlparser.expression.Expression;

/**
 * An element (column reference) in an "ORDER BY" clause.
 */
public class OrderByElement {

    public enum NullOrdering {

        NULLS_FIRST,
        NULLS_LAST
    }

    private Expression expression;
    private boolean asc = true;
    private NullOrdering nullOrdering;
    private boolean ascDesc = false;

    public boolean isAsc() {
        return asc;
    }

    public NullOrdering getNullOrdering() {
        return nullOrdering;
    }

    public void setNullOrdering(NullOrdering nullOrdering) {
        this.nullOrdering = nullOrdering;
    }

    public void setAsc(boolean b) {
        asc = b;
    }
    
    public void setAscDescPresent(boolean b) {
        ascDesc = b;
    }

    public boolean isAscDescPresent() {
        return ascDesc;
    }

    public void accept(OrderByVisitor orderByVisitor) {
        orderByVisitor.visit(this);
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }


    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(expression.toString());
        
        if (!asc) {
            b.append(" DESC");
        } else if (ascDesc) {
            b.append(" ASC");
        }
        
        if (nullOrdering != null) {
            b.append(' ');
            b.append(nullOrdering == NullOrdering.NULLS_FIRST ? "NULLS FIRST" : "NULLS LAST");
        }
        return b.toString();
    }
}
