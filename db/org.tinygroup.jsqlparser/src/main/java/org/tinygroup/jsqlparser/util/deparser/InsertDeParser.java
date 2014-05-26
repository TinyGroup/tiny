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
package org.tinygroup.jsqlparser.util.deparser;

import java.util.Iterator;

import org.tinygroup.jsqlparser.expression.Expression;
import org.tinygroup.jsqlparser.expression.ExpressionVisitor;
import org.tinygroup.jsqlparser.expression.operators.relational.ExpressionList;
import org.tinygroup.jsqlparser.expression.operators.relational.ItemsListVisitor;
import org.tinygroup.jsqlparser.expression.operators.relational.MultiExpressionList;
import org.tinygroup.jsqlparser.schema.Column;
import org.tinygroup.jsqlparser.statement.insert.Insert;
import org.tinygroup.jsqlparser.statement.select.SelectVisitor;
import org.tinygroup.jsqlparser.statement.select.SubSelect;

/**
 * A class to de-parse (that is, tranform from JSqlParser hierarchy into a
 * string) an {@link org.tinygroup.jsqlparser.statement.insert.Insert}
 */
public class InsertDeParser implements ItemsListVisitor {

	private StringBuilder buffer;
    private ExpressionVisitor expressionVisitor;
    private SelectVisitor selectVisitor;

	public InsertDeParser() {
	}

	/**
	 * @param expressionVisitor a {@link ExpressionVisitor} to de-parse
	 * {@link org.tinygroup.jsqlparser.expression.Expression}s. It has to share the
	 * same<br>
	 * StringBuilder (buffer parameter) as this object in order to work
	 * @param selectVisitor a {@link SelectVisitor} to de-parse
	 * {@link org.tinygroup.jsqlparser.statement.select.Select}s. It has to share the
	 * same<br>
	 * StringBuilder (buffer parameter) as this object in order to work
	 * @param buffer the buffer that will be filled with the insert
	 */
	public InsertDeParser(ExpressionVisitor expressionVisitor, SelectVisitor selectVisitor, StringBuilder buffer) {
		this.buffer = buffer;
		this.expressionVisitor = expressionVisitor;
		this.selectVisitor = selectVisitor;
	}

	public StringBuilder getBuffer() {
		return buffer;
	}

	public void setBuffer(StringBuilder buffer) {
		this.buffer = buffer;
	}

	public void deParse(Insert insert) {
		buffer.append("INSERT INTO ");
		buffer.append(insert.getTable().getFullyQualifiedName());
		if (insert.getColumns() != null) {
			buffer.append(" (");
			for (Iterator<Column> iter = insert.getColumns().iterator(); iter.hasNext();) {
				Column column = iter.next();
				buffer.append(column.getColumnName());
				if (iter.hasNext()) {
					buffer.append(", ");
				}
			}
			buffer.append(")");
		}

		insert.getItemsList().accept(this);

	}


	public void visit(ExpressionList expressionList) {
		buffer.append(" VALUES (");
		for (Iterator<Expression> iter = expressionList.getExpressions().iterator(); iter.hasNext();) {
			Expression expression = iter.next();
			expression.accept(expressionVisitor);
			if (iter.hasNext()) {
				buffer.append(", ");
			}
		}
		buffer.append(")");
	}


	public void visit(MultiExpressionList multiExprList) {
		buffer.append(" VALUES ");
		for (Iterator<ExpressionList> it = multiExprList.getExprList().iterator(); it.hasNext();) {
			buffer.append("(");
			for (Iterator<Expression> iter = it.next().getExpressions().iterator(); iter.hasNext();) {
				Expression expression = iter.next();
				expression.accept(expressionVisitor);
				if (iter.hasNext()) {
					buffer.append(", ");
				}
			}
			buffer.append(")");
			if (it.hasNext()) {
				buffer.append(", ");
			}
		}
	}


	public void visit(SubSelect subSelect) {
		subSelect.getSelectBody().accept(selectVisitor);
	}

	public ExpressionVisitor getExpressionVisitor() {
		return expressionVisitor;
	}

	public SelectVisitor getSelectVisitor() {
		return selectVisitor;
	}

	public void setExpressionVisitor(ExpressionVisitor visitor) {
		expressionVisitor = visitor;
	}

	public void setSelectVisitor(SelectVisitor visitor) {
		selectVisitor = visitor;
	}
}
