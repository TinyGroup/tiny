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

import org.tinygroup.jsqlparser.expression.operators.arithmetic.Addition;
import org.tinygroup.jsqlparser.expression.operators.arithmetic.BitwiseAnd;
import org.tinygroup.jsqlparser.expression.operators.arithmetic.BitwiseOr;
import org.tinygroup.jsqlparser.expression.operators.arithmetic.BitwiseXor;
import org.tinygroup.jsqlparser.expression.operators.arithmetic.Concat;
import org.tinygroup.jsqlparser.expression.operators.arithmetic.Division;
import org.tinygroup.jsqlparser.expression.operators.arithmetic.Modulo;
import org.tinygroup.jsqlparser.expression.operators.arithmetic.Multiplication;
import org.tinygroup.jsqlparser.expression.operators.arithmetic.Subtraction;
import org.tinygroup.jsqlparser.expression.operators.conditional.AndExpression;
import org.tinygroup.jsqlparser.expression.operators.conditional.OrExpression;
import org.tinygroup.jsqlparser.expression.operators.relational.Between;
import org.tinygroup.jsqlparser.expression.operators.relational.EqualsTo;
import org.tinygroup.jsqlparser.expression.operators.relational.ExistsExpression;
import org.tinygroup.jsqlparser.expression.operators.relational.GreaterThan;
import org.tinygroup.jsqlparser.expression.operators.relational.GreaterThanEquals;
import org.tinygroup.jsqlparser.expression.operators.relational.InExpression;
import org.tinygroup.jsqlparser.expression.operators.relational.IsNullExpression;
import org.tinygroup.jsqlparser.expression.operators.relational.LikeExpression;
import org.tinygroup.jsqlparser.expression.operators.relational.Matches;
import org.tinygroup.jsqlparser.expression.operators.relational.MinorThan;
import org.tinygroup.jsqlparser.expression.operators.relational.MinorThanEquals;
import org.tinygroup.jsqlparser.expression.operators.relational.NotEqualsTo;
import org.tinygroup.jsqlparser.expression.operators.relational.RegExpMatchOperator;
import org.tinygroup.jsqlparser.schema.Column;
import org.tinygroup.jsqlparser.statement.select.SubSelect;

public interface ExpressionVisitor {

	void visit(NullValue nullValue);

	void visit(Function function);

	void visit(SignedExpression signedExpression);

	void visit(JdbcParameter jdbcParameter);

    void visit(JdbcNamedParameter jdbcNamedParameter);

	void visit(DoubleValue doubleValue);

	void visit(LongValue longValue);

	void visit(DateValue dateValue);

	void visit(TimeValue timeValue);

	void visit(TimestampValue timestampValue);

	void visit(Parenthesis parenthesis);

	void visit(StringValue stringValue);

	void visit(Addition addition);

	void visit(Division division);

	void visit(Multiplication multiplication);

	void visit(Subtraction subtraction);

	void visit(AndExpression andExpression);

	void visit(OrExpression orExpression);

	void visit(Between between);

	void visit(EqualsTo equalsTo);

	void visit(GreaterThan greaterThan);

	void visit(GreaterThanEquals greaterThanEquals);

	void visit(InExpression inExpression);

	void visit(IsNullExpression isNullExpression);

	void visit(LikeExpression likeExpression);

	void visit(MinorThan minorThan);

	void visit(MinorThanEquals minorThanEquals);

	void visit(NotEqualsTo notEqualsTo);

	void visit(Column tableColumn);

	void visit(SubSelect subSelect);

	void visit(CaseExpression caseExpression);

	void visit(WhenClause whenClause);

	void visit(ExistsExpression existsExpression);

	void visit(AllComparisonExpression allComparisonExpression);

	void visit(AnyComparisonExpression anyComparisonExpression);

	void visit(Concat concat);

	void visit(Matches matches);

	void visit(BitwiseAnd bitwiseAnd);

	void visit(BitwiseOr bitwiseOr);

	void visit(BitwiseXor bitwiseXor);

	void visit(CastExpression cast);

	void visit(Modulo modulo);

	void visit(AnalyticExpression aexpr);

	void visit(ExtractExpression eexpr);

	void visit(IntervalExpression iexpr);

	void visit(OracleHierarchicalExpression oexpr);

	void visit(RegExpMatchOperator rexpr);
}
