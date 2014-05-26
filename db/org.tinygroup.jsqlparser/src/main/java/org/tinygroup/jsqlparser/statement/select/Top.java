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

/**
 * A top clause in the form [TOP (row_count) or TOP row_count]
 */
public class Top {

    private long rowCount;
    private boolean rowCountJdbcParameter = false;
    private boolean hasParenthesis = false;
    private boolean isPercentage = false;

    public long getRowCount() {
        return rowCount;
    }

    // TODO instead of a plain number, an expression should be added, which could be a NumberExpression, a GroupedExpression or a JdbcParameter
    public void setRowCount(long rowCount) {
        this.rowCount = rowCount;
    }

    public boolean isRowCountJdbcParameter() {
        return rowCountJdbcParameter;
    }

    public void setRowCountJdbcParameter(boolean rowCountJdbcParameter) {
        this.rowCountJdbcParameter = rowCountJdbcParameter;
    }

    public boolean hasParenthesis() {
        return hasParenthesis;
    }

    public void setParenthesis(boolean hasParenthesis) {
        this.hasParenthesis = hasParenthesis;
    }

    public boolean isPercentage() {
        return isPercentage;
    }

    public void setPercentage(boolean percentage) {
        this.isPercentage = percentage;
    }


    public String toString() {
        String result = "TOP ";

        if (hasParenthesis) {
            result += "(";
        }

        result += rowCountJdbcParameter ? "?"
                : rowCount;

        if (hasParenthesis) {
            result += ")";
        }

        if (isPercentage) {
            result += " PERCENT";
        }

        return result;
    }
}
