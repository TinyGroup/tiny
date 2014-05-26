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

import org.tinygroup.jsqlparser.schema.Column;

import java.util.List;

public class Pivot {

    private List<FunctionItem> functionItems;

    private List<Column> forColumns;

    private List<SelectExpressionItem> singleInItems;

    private List<ExpressionListItem> multiInItems;

    public void accept(PivotVisitor pivotVisitor) {
        pivotVisitor.visit(this);
    }

    public List<SelectExpressionItem> getSingleInItems() {
        return singleInItems;
    }

    public void setSingleInItems(List<SelectExpressionItem> singleInItems) {
        this.singleInItems = singleInItems;
    }

    public List<ExpressionListItem> getMultiInItems() {
        return multiInItems;
    }

    public void setMultiInItems(List<ExpressionListItem> multiInItems) {
        this.multiInItems = multiInItems;
    }

    public List<FunctionItem> getFunctionItems() {
        return functionItems;
    }

    public void setFunctionItems(List<FunctionItem> functionItems) {
        this.functionItems = functionItems;
    }

    public List<Column> getForColumns() {
        return forColumns;
    }

    public void setForColumns(List<Column> forColumns) {
        this.forColumns = forColumns;
    }

    public List<?> getInItems() {
        return singleInItems == null ? multiInItems : singleInItems;
    }


    public String toString() {
        return "PIVOT (" +
                PlainSelect.getStringList(functionItems) +
                " FOR " + PlainSelect.getStringList(forColumns, true, forColumns != null && forColumns.size() > 1) +
                " IN " + PlainSelect.getStringList(getInItems(), true, true) + ")";
    }
}
