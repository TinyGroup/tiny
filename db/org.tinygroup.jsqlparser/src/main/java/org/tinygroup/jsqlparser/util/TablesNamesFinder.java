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

import org.tinygroup.jsqlparser.expression.*;
import org.tinygroup.jsqlparser.expression.operators.arithmetic.*;
import org.tinygroup.jsqlparser.expression.operators.conditional.AndExpression;
import org.tinygroup.jsqlparser.expression.operators.conditional.OrExpression;
import org.tinygroup.jsqlparser.expression.operators.relational.*;
import org.tinygroup.jsqlparser.schema.Column;
import org.tinygroup.jsqlparser.schema.Table;
import org.tinygroup.jsqlparser.statement.delete.Delete;
import org.tinygroup.jsqlparser.statement.insert.Insert;
import org.tinygroup.jsqlparser.statement.replace.Replace;
import org.tinygroup.jsqlparser.statement.select.*;
import org.tinygroup.jsqlparser.statement.update.Update;

import java.util.ArrayList;
import java.util.List;

/**
 * Find all used tables within an select statement.
 */
public class TablesNamesFinder implements SelectVisitor, FromItemVisitor, ExpressionVisitor, ItemsListVisitor {

	private List<String> tables;
	/**
	 * There are special names, that are not table names but are parsed as
	 * tables. These names are collected here and are not included in the tables
	 * - names anymore.
	 */
	private List<String> otherItemNames;

	/**
	 * Main entry for this Tool class. A list of found tables is returned.
	 *
	 * @param delete
	 * @return
	 */
	public List<String> getTableList(Delete delete) {
		init();
		tables.add(delete.getTable().getName());
		delete.getWhere().accept(this);

		return tables;
	}

	/**
	 * Main entry for this Tool class. A list of found tables is returned.
	 *
	 * @param insert
	 * @return
	 */
	public List<String> getTableList(Insert insert) {
		init();
		tables.add(insert.getTable().getName());
		if (insert.getItemsList() != null) {
			insert.getItemsList().accept(this);
		}

		return tables;
	}

	/**
	 * Main entry for this Tool class. A list of found tables is returned.
	 *
	 * @param replace
	 * @return
	 */
	public List<String> getTableList(Replace replace) {
		init();
		tables.add(replace.getTable().getName());
		if (replace.getExpressions() != null) {
			for (Expression expression : replace.getExpressions()) {
				expression.accept(this);
			}
		}
		if (replace.getItemsList() != null) {
			replace.getItemsList().accept(this);
		}

		return tables;
	}

	/**
	 * Main entry for this Tool class. A list of found tables is returned.
	 *
	 * @param select
	 * @return
	 */
	public List<String> getTableList(Select select) {
		init();
		if (select.getWithItemsList() != null) {
			for (WithItem withItem : select.getWithItemsList()) {
				withItem.accept(this);
			}
		}
		select.getSelectBody().accept(this);

		return tables;
	}

	/**
	 * Main entry for this Tool class. A list of found tables is returned.
	 *
	 * @param update
	 * @return
	 */
	public List<String> getTableList(Update update) {
		init();
		tables.add(update.getTable().getName());
		if (update.getExpressions() != null) {
			for (Expression expression : update.getExpressions()) {
				expression.accept(this);
			}
		}

		if (update.getFromItem() != null) {
			update.getFromItem().accept(this);
		}

		if (update.getJoins() != null) {
			for (Join join : update.getJoins()) {
				join.getRightItem().accept(this);
			}
		}

		if (update.getWhere() != null) {
			update.getWhere().accept(this);
		}

		return tables;
	}


	public void visit(WithItem withItem) {
		otherItemNames.add(withItem.getName().toLowerCase());
		withItem.getSelectBody().accept(this);
	}


	public void visit(PlainSelect plainSelect) {
		plainSelect.getFromItem().accept(this);

		if (plainSelect.getJoins() != null) {
			for (Join join : plainSelect.getJoins()) {
				join.getRightItem().accept(this);
			}
		}
		if (plainSelect.getWhere() != null) {
			plainSelect.getWhere().accept(this);
		}

	}


	public void visit(Table tableName) {
		String tableWholeName = tableName.getFullyQualifiedName();
		if (!otherItemNames.contains(tableWholeName.toLowerCase())
				&& !tables.contains(tableWholeName)) {
			tables.add(tableWholeName);
		}
	}


	public void visit(SubSelect subSelect) {
		subSelect.getSelectBody().accept(this);
	}


	public void visit(Addition addition) {
		visitBinaryExpression(addition);
	}


	public void visit(AndExpression andExpression) {
		visitBinaryExpression(andExpression);
	}


	public void visit(Between between) {
		between.getLeftExpression().accept(this);
		between.getBetweenExpressionStart().accept(this);
		between.getBetweenExpressionEnd().accept(this);
	}


	public void visit(Column tableColumn) {
	}


	public void visit(Division division) {
		visitBinaryExpression(division);
	}


	public void visit(DoubleValue doubleValue) {
	}


	public void visit(EqualsTo equalsTo) {
		visitBinaryExpression(equalsTo);
	}


	public void visit(Function function) {
	}


	public void visit(GreaterThan greaterThan) {
		visitBinaryExpression(greaterThan);
	}


	public void visit(GreaterThanEquals greaterThanEquals) {
		visitBinaryExpression(greaterThanEquals);
	}


	public void visit(InExpression inExpression) {
		inExpression.getLeftExpression().accept(this);
		inExpression.getRightItemsList().accept(this);
	}


	public void visit(SignedExpression signedExpression) {
		signedExpression.getExpression().accept(this);
	}


	public void visit(IsNullExpression isNullExpression) {
	}


	public void visit(JdbcParameter jdbcParameter) {
	}


	public void visit(LikeExpression likeExpression) {
		visitBinaryExpression(likeExpression);
	}


	public void visit(ExistsExpression existsExpression) {
		existsExpression.getRightExpression().accept(this);
	}


	public void visit(LongValue longValue) {
	}


	public void visit(MinorThan minorThan) {
		visitBinaryExpression(minorThan);
	}


	public void visit(MinorThanEquals minorThanEquals) {
		visitBinaryExpression(minorThanEquals);
	}


	public void visit(Multiplication multiplication) {
		visitBinaryExpression(multiplication);
	}


	public void visit(NotEqualsTo notEqualsTo) {
		visitBinaryExpression(notEqualsTo);
	}


	public void visit(NullValue nullValue) {
	}


	public void visit(OrExpression orExpression) {
		visitBinaryExpression(orExpression);
	}


	public void visit(Parenthesis parenthesis) {
		parenthesis.getExpression().accept(this);
	}


	public void visit(StringValue stringValue) {
	}


	public void visit(Subtraction subtraction) {
		visitBinaryExpression(subtraction);
	}

	public void visitBinaryExpression(BinaryExpression binaryExpression) {
		binaryExpression.getLeftExpression().accept(this);
		binaryExpression.getRightExpression().accept(this);
	}


	public void visit(ExpressionList expressionList) {
		for (Expression expression : expressionList.getExpressions()) {
			expression.accept(this);
		}

	}


	public void visit(DateValue dateValue) {
	}


	public void visit(TimestampValue timestampValue) {
	}


	public void visit(TimeValue timeValue) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.tinygroup.jsqlparser.expression.ExpressionVisitor#visit(org.tinygroup.jsqlparser.expression.CaseExpression)
	 */

	public void visit(CaseExpression caseExpression) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.tinygroup.jsqlparser.expression.ExpressionVisitor#visit(org.tinygroup.jsqlparser.expression.WhenClause)
	 */

	public void visit(WhenClause whenClause) {
	}


	public void visit(AllComparisonExpression allComparisonExpression) {
		allComparisonExpression.getSubSelect().getSelectBody().accept(this);
	}


	public void visit(AnyComparisonExpression anyComparisonExpression) {
		anyComparisonExpression.getSubSelect().getSelectBody().accept(this);
	}


	public void visit(SubJoin subjoin) {
		subjoin.getLeft().accept(this);
		subjoin.getJoin().getRightItem().accept(this);
	}


	public void visit(Concat concat) {
		visitBinaryExpression(concat);
	}


	public void visit(Matches matches) {
		visitBinaryExpression(matches);
	}


	public void visit(BitwiseAnd bitwiseAnd) {
		visitBinaryExpression(bitwiseAnd);
	}


	public void visit(BitwiseOr bitwiseOr) {
		visitBinaryExpression(bitwiseOr);
	}


	public void visit(BitwiseXor bitwiseXor) {
		visitBinaryExpression(bitwiseXor);
	}


	public void visit(CastExpression cast) {
		cast.getLeftExpression().accept(this);
	}


	public void visit(Modulo modulo) {
		visitBinaryExpression(modulo);
	}


	public void visit(AnalyticExpression analytic) {
	}


	public void visit(SetOperationList list) {
		for (PlainSelect plainSelect : list.getPlainSelects()) {
			visit(plainSelect);
		}
	}


	public void visit(ExtractExpression eexpr) {
	}


	public void visit(LateralSubSelect lateralSubSelect) {
		lateralSubSelect.getSubSelect().getSelectBody().accept(this);
	}


	public void visit(MultiExpressionList multiExprList) {
		for (ExpressionList exprList : multiExprList.getExprList()) {
			exprList.accept(this);
		}
	}


	public void visit(ValuesList valuesList) {
	}

	private void init() {
		otherItemNames = new ArrayList<String>();
		tables = new ArrayList<String>();
	}


	public void visit(IntervalExpression iexpr) {
	}


    public void visit(JdbcNamedParameter jdbcNamedParameter) {
    }


	public void visit(OracleHierarchicalExpression oexpr) {
	}


	public void visit(RegExpMatchOperator rexpr) {
		visitBinaryExpression(rexpr);
	}
}
