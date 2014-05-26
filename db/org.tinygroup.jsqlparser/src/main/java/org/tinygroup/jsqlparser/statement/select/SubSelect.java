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

import org.tinygroup.jsqlparser.expression.Alias;
import org.tinygroup.jsqlparser.expression.Expression;
import org.tinygroup.jsqlparser.expression.ExpressionVisitor;
import org.tinygroup.jsqlparser.expression.operators.relational.ItemsList;
import org.tinygroup.jsqlparser.expression.operators.relational.ItemsListVisitor;

/**
 * A subselect followed by an optional alias.
 */
public class SubSelect implements FromItem, Expression, ItemsList {

	private SelectBody selectBody;
	private Alias alias;

    private Pivot pivot;


	public void accept(FromItemVisitor fromItemVisitor) {
		fromItemVisitor.visit(this);
	}

	public SelectBody getSelectBody() {
		return selectBody;
	}

	public void setSelectBody(SelectBody body) {
		selectBody = body;
	}


	public void accept(ExpressionVisitor expressionVisitor) {
		expressionVisitor.visit(this);
	}


	public Alias getAlias() {
		return alias;
	}


	public void setAlias(Alias alias) {
		this.alias = alias;
	}


    public Pivot getPivot() {
        return pivot;
    }


    public void setPivot(Pivot pivot) {
        this.pivot = pivot;
    }


	public void accept(ItemsListVisitor itemsListVisitor) {
		itemsListVisitor.visit(this);
	}


	public String toString() {
		return "(" + selectBody + ")" 
                + ((pivot != null) ? " " + pivot : "")
                + ((alias != null) ? alias.toString() : "");
	}
}
