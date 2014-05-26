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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.tinygroup.jsqlparser.expression.Expression;

/**
 * A list of ExpressionList items. e.g. multi values of insert statements. This
 * one allows only equally sized ExpressionList.
 *
 * @author toben
 */
public class MultiExpressionList implements ItemsList {

	private List<ExpressionList> exprList;

	public MultiExpressionList() {
		this.exprList = new ArrayList<ExpressionList>();
	}


	public void accept(ItemsListVisitor itemsListVisitor) {
		itemsListVisitor.visit(this);
	}

	public List<ExpressionList> getExprList() {
		return exprList;
	}

	public void addExpressionList(ExpressionList el) {
		if (!exprList.isEmpty()
				&& exprList.get(0).getExpressions().size() != el.getExpressions().size()) {
			throw new IllegalArgumentException("different count of parameters");
		}
		exprList.add(el);
	}

	public void addExpressionList(List<Expression> list) {
		addExpressionList(new ExpressionList(list));
	}

	public void addExpressionList(Expression expr) {
		addExpressionList(new ExpressionList(Arrays.asList(expr)));
	}


	public String toString() {
		StringBuilder b = new StringBuilder();
		for (Iterator<ExpressionList> it = exprList.iterator(); it.hasNext();) {
			b.append(it.next().toString());
			if (it.hasNext()) {
				b.append(", ");
			}
		}
		return b.toString();
	}
}
