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

import org.tinygroup.jsqlparser.schema.Column;
import org.tinygroup.jsqlparser.statement.select.OrderByElement;

import java.util.List;

/**
 * Analytic function. The name of the function is variable but the parameters
 * following the special analytic function path. e.g. row_number() over (order
 * by test). Additional there can be an expression for an analytical aggregate
 * like sum(col) or the "all collumns" wildcard like count(*).
 *
 * @author tw
 */
public class AnalyticExpression implements Expression {

	private List<Column> partitionByColumns;
	private List<OrderByElement> orderByElements;
	private String name;
	private Expression expression;
    private Expression offset;
    private Expression defaultValue;
	private boolean allColumns = false;
    private WindowElement windowElement;


	public void accept(ExpressionVisitor expressionVisitor) {
		expressionVisitor.visit(this);
	}

	public List<OrderByElement> getOrderByElements() {
		return orderByElements;
	}

	public void setOrderByElements(List<OrderByElement> orderByElements) {
		this.orderByElements = orderByElements;
	}

	public List<Column> getPartitionByColumns() {
		return partitionByColumns;
	}

	public void setPartitionByColumns(List<Column> partitionByColumns) {
		this.partitionByColumns = partitionByColumns;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Expression getExpression() {
		return expression;
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}

    public Expression getOffset() {
        return offset;
    }

    public void setOffset(Expression offset) {
        this.offset = offset;
    }

    public Expression getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Expression defaultValue) {
        this.defaultValue = defaultValue;
    }

    public WindowElement getWindowElement() {
        return windowElement;
    }

    public void setWindowElement(WindowElement windowElement) {
        this.windowElement = windowElement;
    }


	public String toString() {
		StringBuilder b = new StringBuilder();

		b.append(name).append("(");
		if (expression != null) {
			b.append(expression.toString());
            if (offset != null) {
                b.append(", ").append(offset.toString());
                if (defaultValue != null) {
                    b.append(", ").append(defaultValue.toString());
                }
            }
		} else if (isAllColumns()) {
			b.append("*");
		}
		b.append(") OVER (");
		
		toStringPartitionBy(b);
		toStringOrderByElements(b);

		b.append(")");

		return b.toString();
	}

	public boolean isAllColumns() {
		return allColumns;
	}

	public void setAllColumns(boolean allColumns) {
		this.allColumns = allColumns;
	}

	private void toStringPartitionBy(StringBuilder b) {
		if (partitionByColumns != null && !partitionByColumns.isEmpty()) {
			b.append("PARTITION BY ");
			for (int i = 0; i < partitionByColumns.size(); i++) {
				if (i > 0) {
					b.append(", ");
				}
				b.append(partitionByColumns.get(i).toString());
			}
			b.append(" ");
		}
	}

	private void toStringOrderByElements(StringBuilder b) {
		if (orderByElements != null && !orderByElements.isEmpty()) {
			b.append("ORDER BY ");
			for (int i = 0; i < orderByElements.size(); i++) {
				if (i > 0) {
					b.append(", ");
				}
				b.append(orderByElements.get(i).toString());
			}
            
            if(windowElement != null){
                b.append(' ');
                b.append(windowElement);
            }
		}
	}
}
