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

import org.tinygroup.jsqlparser.expression.*;
import org.tinygroup.jsqlparser.expression.operators.arithmetic.*;
import org.tinygroup.jsqlparser.expression.operators.conditional.AndExpression;
import org.tinygroup.jsqlparser.expression.operators.conditional.OrExpression;
import org.tinygroup.jsqlparser.expression.operators.relational.*;
import org.tinygroup.jsqlparser.schema.Column;
import org.tinygroup.jsqlparser.schema.Table;
import org.tinygroup.jsqlparser.statement.select.SelectVisitor;
import org.tinygroup.jsqlparser.statement.select.SubSelect;

import java.util.Iterator;

/**
 * A class to de-parse (that is, tranform from JSqlParser hierarchy into a
 * string) an {@link org.tinygroup.jsqlparser.expression.Expression}
 */
public class ExpressionDeParser implements ExpressionVisitor, ItemsListVisitor {

    private StringBuilder buffer;
    private SelectVisitor selectVisitor;
    private boolean useBracketsInExprList = true;

    public ExpressionDeParser() {
    }

    /**
     * @param selectVisitor a SelectVisitor to de-parse SubSelects. It has to
     *                      share the same<br> StringBuilder as this object in order to work, as:
     *                      <p/>
     *                      <pre>
     *                      <code>
     *                      StringBuilder myBuf = new StringBuilder();
     *                      MySelectDeparser selectDeparser = new  MySelectDeparser();
     *                      selectDeparser.setBuffer(myBuf);
     *                      ExpressionDeParser expressionDeParser = new ExpressionDeParser(selectDeparser, myBuf);
     *                      </code>
     *                      </pre>
     * @param buffer        the buffer that will be filled with the expression
     */
    public ExpressionDeParser(SelectVisitor selectVisitor, StringBuilder buffer) {
        this.selectVisitor = selectVisitor;
        this.buffer = buffer;
    }

    public StringBuilder getBuffer() {
        return buffer;
    }

    public void setBuffer(StringBuilder buffer) {
        this.buffer = buffer;
    }


    public void visit(Addition addition) {
        visitBinaryExpression(addition, " + ");
    }


    public void visit(AndExpression andExpression) {
        visitBinaryExpression(andExpression, " AND ");
    }


    public void visit(Between between) {
        between.getLeftExpression().accept(this);
        if (between.isNot()) {
            buffer.append(" NOT");
        }

        buffer.append(" BETWEEN ");
        between.getBetweenExpressionStart().accept(this);
        buffer.append(" AND ");
        between.getBetweenExpressionEnd().accept(this);

    }


    public void visit(EqualsTo equalsTo) {
        visitOldOracleJoinBinaryExpression(equalsTo, " = ");
    }


    public void visit(Division division) {
        visitBinaryExpression(division, " / ");

    }


    public void visit(DoubleValue doubleValue) {
        buffer.append(doubleValue.toString());

    }

    public void visitOldOracleJoinBinaryExpression(OldOracleJoinBinaryExpression expression, String operator) {
        if (expression.isNot()) {
            buffer.append(" NOT ");
        }
        expression.getLeftExpression().accept(this);
        if (expression.getOldOracleJoinSyntax() == EqualsTo.ORACLE_JOIN_RIGHT) {
            buffer.append("(+)");
        }
        buffer.append(operator);
        expression.getRightExpression().accept(this);
        if (expression.getOldOracleJoinSyntax() == EqualsTo.ORACLE_JOIN_LEFT) {
            buffer.append("(+)");
        }
    }


    public void visit(GreaterThan greaterThan) {
        visitOldOracleJoinBinaryExpression(greaterThan, " > ");
    }


    public void visit(GreaterThanEquals greaterThanEquals) {
        visitOldOracleJoinBinaryExpression(greaterThanEquals, " >= ");

    }


    public void visit(InExpression inExpression) {
        if (inExpression.getLeftExpression() == null) {
            inExpression.getLeftItemsList().accept(this);
        } else {
            inExpression.getLeftExpression().accept(this);
            if (inExpression.getOldOracleJoinSyntax() == SupportsOldOracleJoinSyntax.ORACLE_JOIN_RIGHT) {
                buffer.append("(+)");
            }
        }
        if (inExpression.isNot()) {
            buffer.append(" NOT");
        }
        buffer.append(" IN ");

        inExpression.getRightItemsList().accept(this);
    }


    public void visit(SignedExpression signedExpression) {
        buffer.append(signedExpression.getSign());
        signedExpression.getExpression().accept(this);
    }


    public void visit(IsNullExpression isNullExpression) {
        isNullExpression.getLeftExpression().accept(this);
        if (isNullExpression.isNot()) {
            buffer.append(" IS NOT NULL");
        } else {
            buffer.append(" IS NULL");
        }
    }


    public void visit(JdbcParameter jdbcParameter) {
        buffer.append("?");

    }


    public void visit(LikeExpression likeExpression) {
        visitBinaryExpression(likeExpression, " LIKE ");
        String escape = likeExpression.getEscape();
        if (escape != null) {
            buffer.append(" ESCAPE '").append(escape).append('\'');
        }
    }


    public void visit(ExistsExpression existsExpression) {
        if (existsExpression.isNot()) {
            buffer.append("NOT EXISTS ");
        } else {
            buffer.append("EXISTS ");
        }
        existsExpression.getRightExpression().accept(this);
    }


    public void visit(LongValue longValue) {
        buffer.append(longValue.getStringValue());

    }


    public void visit(MinorThan minorThan) {
        visitOldOracleJoinBinaryExpression(minorThan, " < ");

    }


    public void visit(MinorThanEquals minorThanEquals) {
        visitOldOracleJoinBinaryExpression(minorThanEquals, " <= ");

    }


    public void visit(Multiplication multiplication) {
        visitBinaryExpression(multiplication, " * ");

    }


    public void visit(NotEqualsTo notEqualsTo) {
        visitOldOracleJoinBinaryExpression(notEqualsTo, " " + notEqualsTo.getStringExpression() + " ");

    }


    public void visit(NullValue nullValue) {
        buffer.append("NULL");

    }


    public void visit(OrExpression orExpression) {
        visitBinaryExpression(orExpression, " OR ");

    }


    public void visit(Parenthesis parenthesis) {
        if (parenthesis.isNot()) {
            buffer.append(" NOT ");
        }

        buffer.append("(");
        parenthesis.getExpression().accept(this);
        buffer.append(")");

    }


    public void visit(StringValue stringValue) {
        buffer.append("'").append(stringValue.getValue()).append("'");

    }


    public void visit(Subtraction subtraction) {
        visitBinaryExpression(subtraction, " - ");

    }

    private void visitBinaryExpression(BinaryExpression binaryExpression, String operator) {
        if (binaryExpression.isNot()) {
            buffer.append(" NOT ");
        }
        binaryExpression.getLeftExpression().accept(this);
        buffer.append(operator);
        binaryExpression.getRightExpression().accept(this);

    }


    public void visit(SubSelect subSelect) {
        buffer.append("(");
        subSelect.getSelectBody().accept(selectVisitor);
        buffer.append(")");
    }


    public void visit(Column tableColumn) {
        final Table table = tableColumn.getTable();
        String tableName = null;
        if (table != null) {
            if (table.getAlias() != null) {
                tableName = table.getAlias().getName();
            } else {
                tableName = table.getFullyQualifiedName();
            }
        }
        if (tableName != null && !(tableName == null || tableName.length() == 0)) {
            buffer.append(tableName).append(".");
        }

        buffer.append(tableColumn.getColumnName());
    }


    public void visit(Function function) {
        if (function.isEscaped()) {
            buffer.append("{fn ");
        }

        buffer.append(function.getName());
        if (function.isAllColumns() && function.getParameters() == null) {
            buffer.append("(*)");
        } else if (function.getParameters() == null) {
            buffer.append("()");
        } else {
            boolean oldUseBracketsInExprList = useBracketsInExprList;
            if (function.isDistinct()) {
                useBracketsInExprList = false;
                buffer.append("(DISTINCT ");
            } else if (function.isAllColumns()) {
                useBracketsInExprList = false;
                buffer.append("(ALL ");
            }
            visit(function.getParameters());
            useBracketsInExprList = oldUseBracketsInExprList;
            if (function.isDistinct() || function.isAllColumns()) {
                buffer.append(")");
            }
        }

        if (function.isEscaped()) {
            buffer.append("}");
        }
    }


    public void visit(ExpressionList expressionList) {
        if (useBracketsInExprList) {
            buffer.append("(");
        }
        for (Iterator<Expression> iter = expressionList.getExpressions().iterator(); iter.hasNext(); ) {
            Expression expression = iter.next();
            expression.accept(this);
            if (iter.hasNext()) {
                buffer.append(", ");
            }
        }
        if (useBracketsInExprList) {
            buffer.append(")");
        }
    }

    public SelectVisitor getSelectVisitor() {
        return selectVisitor;
    }

    public void setSelectVisitor(SelectVisitor visitor) {
        selectVisitor = visitor;
    }


    public void visit(DateValue dateValue) {
        buffer.append("{d '").append(dateValue.getValue().toString()).append("'}");
    }


    public void visit(TimestampValue timestampValue) {
        buffer.append("{ts '").append(timestampValue.getValue().toString()).append("'}");
    }


    public void visit(TimeValue timeValue) {
        buffer.append("{t '").append(timeValue.getValue().toString()).append("'}");
    }


    public void visit(CaseExpression caseExpression) {
        buffer.append("CASE ");
        Expression switchExp = caseExpression.getSwitchExpression();
        if (switchExp != null) {
            switchExp.accept(this);
            buffer.append(" ");
        }

        for (Expression exp : caseExpression.getWhenClauses()) {
            exp.accept(this);
        }

        Expression elseExp = caseExpression.getElseExpression();
        if (elseExp != null) {
            buffer.append("ELSE ");
            elseExp.accept(this);
            buffer.append(" ");
        }

        buffer.append("END");
    }


    public void visit(WhenClause whenClause) {
        buffer.append("WHEN ");
        whenClause.getWhenExpression().accept(this);
        buffer.append(" THEN ");
        whenClause.getThenExpression().accept(this);
        buffer.append(" ");
    }


    public void visit(AllComparisonExpression allComparisonExpression) {
        buffer.append(" ALL ");
        allComparisonExpression.getSubSelect().accept((ExpressionVisitor) this);
    }


    public void visit(AnyComparisonExpression anyComparisonExpression) {
        buffer.append(" ANY ");
        anyComparisonExpression.getSubSelect().accept((ExpressionVisitor) this);
    }


    public void visit(Concat concat) {
        visitBinaryExpression(concat, " || ");
    }


    public void visit(Matches matches) {
        visitOldOracleJoinBinaryExpression(matches, " @@ ");
    }


    public void visit(BitwiseAnd bitwiseAnd) {
        visitBinaryExpression(bitwiseAnd, " & ");
    }


    public void visit(BitwiseOr bitwiseOr) {
        visitBinaryExpression(bitwiseOr, " | ");
    }


    public void visit(BitwiseXor bitwiseXor) {
        visitBinaryExpression(bitwiseXor, " ^ ");
    }


    public void visit(CastExpression cast) {
        if (cast.isUseCastKeyword()) {
            buffer.append("CAST(");
            buffer.append(cast.getLeftExpression());
            buffer.append(" AS ");
            buffer.append(cast.getType());
            buffer.append(")");
        } else {
            buffer.append(cast.getLeftExpression());
            buffer.append("::");
            buffer.append(cast.getType());
        }
    }


    public void visit(Modulo modulo) {
        visitBinaryExpression(modulo, " % ");
    }


    public void visit(AnalyticExpression aexpr) {
        buffer.append(aexpr.toString());
    }


    public void visit(ExtractExpression eexpr) {
        buffer.append(eexpr.toString());
    }


    public void visit(MultiExpressionList multiExprList) {
        for (Iterator<ExpressionList> it = multiExprList.getExprList().iterator(); it.hasNext(); ) {
            it.next().accept(this);
            if (it.hasNext()) {
                buffer.append(", ");
            }
        }
    }


    public void visit(IntervalExpression iexpr) {
        buffer.append(iexpr.toString());
    }


    public void visit(JdbcNamedParameter jdbcNamedParameter) {
        buffer.append(jdbcNamedParameter.toString());
    }


    public void visit(OracleHierarchicalExpression oexpr) {
        buffer.append(oexpr.toString());
    }


    public void visit(RegExpMatchOperator rexpr) {
        visitBinaryExpression(rexpr, " " + rexpr.getStringExpression() + " ");
    }
}
