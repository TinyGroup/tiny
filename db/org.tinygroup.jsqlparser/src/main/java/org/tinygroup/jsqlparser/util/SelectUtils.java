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
package org.tinygroup.jsqlparser.util;

import java.util.ArrayList;
import java.util.List;
import org.tinygroup.jsqlparser.JSQLParserException;
import org.tinygroup.jsqlparser.expression.Expression;
import org.tinygroup.jsqlparser.parser.CCJSqlParserUtil;
import org.tinygroup.jsqlparser.schema.Table;
import org.tinygroup.jsqlparser.statement.select.AllColumns;
import org.tinygroup.jsqlparser.statement.select.Join;
import org.tinygroup.jsqlparser.statement.select.PlainSelect;
import org.tinygroup.jsqlparser.statement.select.Select;
import org.tinygroup.jsqlparser.statement.select.SelectExpressionItem;
import org.tinygroup.jsqlparser.statement.select.SelectItem;
import org.tinygroup.jsqlparser.statement.select.SelectVisitor;
import org.tinygroup.jsqlparser.statement.select.SetOperationList;
import org.tinygroup.jsqlparser.statement.select.WithItem;

/**
 * Utility function for select statements.
 *
 * @author toben
 */
public final class SelectUtils {

	private SelectUtils() {
	}
	
	/**
	 * Builds select expr1, expr2 from table.
	 * @param table
	 * @param expr
	 * @return 
	 */
	public static Select buildSelectFromTableAndExpressions(Table table, Expression ... expr) {
		SelectItem[] list = new SelectItem[expr.length];
		for (int i=0;i<expr.length;i++) {
			list[i]=new SelectExpressionItem(expr[i]);
		}
		return buildSelectFromTableAndSelectItems(table, list);
	}
	
	/**
	 * Builds select expr1, expr2 from table.
	 * @param table
	 * @param expr
	 * @return 
	 * @throws org.tinygroup.jsqlparser.JSQLParserException
	 */
	public static Select buildSelectFromTableAndExpressions(Table table, String ... expr) throws JSQLParserException {
		SelectItem[] list = new SelectItem[expr.length];
		for (int i=0;i<expr.length;i++) {
			list[i]=new SelectExpressionItem(CCJSqlParserUtil.parseExpression(expr[i]));
		}
		return buildSelectFromTableAndSelectItems(table, list);
	}
	
	public static Select buildSelectFromTableAndSelectItems(Table table, SelectItem ... selectItems) {
		Select select = new Select();
		PlainSelect body = new PlainSelect();
		body.addSelectItems(selectItems);
		body.setFromItem(table);
		select.setSelectBody(body);
		return select;
	}
	
	/**
	 * Builds select * from table.
	 * @param table
	 * @return 
	 */
	public static Select buildSelectFromTable(Table table) {
		return buildSelectFromTableAndSelectItems(table, new AllColumns());
	}

	/**
	 * Adds an expression to select statements. E.g. a simple column is an
	 * expression.
	 *
	 * @param select
	 * @param expr
	 */
	public static void addExpression(Select select, final Expression expr) {
		select.getSelectBody().accept(new SelectVisitor() {


			public void visit(PlainSelect plainSelect) {
				plainSelect.getSelectItems().add(new SelectExpressionItem(expr));
			}


			public void visit(SetOperationList setOpList) {
				throw new UnsupportedOperationException("Not supported yet.");
			}


			public void visit(WithItem withItem) {
				throw new UnsupportedOperationException("Not supported yet.");
			}
		});
	}

	/**
	 * Adds a simple join to a select statement. The introduced join is returned for
	 * more configuration settings on it (e.g. left join, right join).
	 * @param select
	 * @param table
	 * @param onExpression
	 * @return 
	 */
	public static Join addJoin(Select select, final Table table, final Expression onExpression) {
		if (select.getSelectBody() instanceof PlainSelect) {
			PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
			List<Join> joins = plainSelect.getJoins();
			if (joins == null) {
				joins = new ArrayList<Join>();
				plainSelect.setJoins(joins);
			}
			Join join = new Join();
			join.setRightItem(table);
			join.setOnExpression(onExpression);
			joins.add(join);
			return join;
		}
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
