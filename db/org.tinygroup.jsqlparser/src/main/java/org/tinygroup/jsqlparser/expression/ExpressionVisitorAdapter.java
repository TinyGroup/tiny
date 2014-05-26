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

import org.tinygroup.jsqlparser.expression.operators.arithmetic.*;
import org.tinygroup.jsqlparser.expression.operators.conditional.AndExpression;
import org.tinygroup.jsqlparser.expression.operators.conditional.OrExpression;
import org.tinygroup.jsqlparser.expression.operators.relational.*;
import org.tinygroup.jsqlparser.schema.Column;
import org.tinygroup.jsqlparser.statement.select.OrderByElement;
import org.tinygroup.jsqlparser.statement.select.SubSelect;

public class ExpressionVisitorAdapter implements ExpressionVisitor, ItemsListVisitor {

    public void visit(NullValue value) {

    }


    public void visit(Function function) {

    }


    public void visit(SignedExpression expr) {
        expr.getExpression().accept(this);
    }


    public void visit(JdbcParameter parameter) {

    }


    public void visit(JdbcNamedParameter parameter) {

    }


    public void visit(DoubleValue value) {

    }


    public void visit(LongValue value) {

    }


    public void visit(DateValue value) {

    }


    public void visit(TimeValue value) {

    }


    public void visit(TimestampValue value) {

    }


    public void visit(Parenthesis parenthesis) {
        parenthesis.getExpression().accept(this);
    }


    public void visit(StringValue value) {

    }


    public void visit(Addition expr) {
        visitBinaryExpression(expr);
    }


    public void visit(Division expr) {
        visitBinaryExpression(expr);
    }


    public void visit(Multiplication expr) {
        visitBinaryExpression(expr);
    }


    public void visit(Subtraction expr) {
        visitBinaryExpression(expr);
    }


    public void visit(AndExpression expr) {
        visitBinaryExpression(expr);
    }


    public void visit(OrExpression expr) {
        visitBinaryExpression(expr);
    }


    public void visit(Between expr) {
        expr.getLeftExpression().accept(this);
        expr.getBetweenExpressionStart().accept(this);
        expr.getBetweenExpressionEnd().accept(this);
    }


    public void visit(EqualsTo expr) {
        visitBinaryExpression(expr);
    }


    public void visit(GreaterThan expr) {
        visitBinaryExpression(expr);
    }


    public void visit(GreaterThanEquals expr) {
        visitBinaryExpression(expr);
    }


    public void visit(InExpression expr) {
        expr.getLeftExpression().accept(this);
        expr.getLeftItemsList().accept(this);
        expr.getRightItemsList().accept(this);
    }


    public void visit(IsNullExpression expr) {
        expr.getLeftExpression().accept(this);
    }


    public void visit(LikeExpression expr) {
        visitBinaryExpression(expr);
    }


    public void visit(MinorThan expr) {
        visitBinaryExpression(expr);
    }


    public void visit(MinorThanEquals expr) {
        visitBinaryExpression(expr);
    }


    public void visit(NotEqualsTo expr) {
        visitBinaryExpression(expr);
    }


    public void visit(Column column) {  

    }


    public void visit(SubSelect subSelect) {
        
    }


    public void visit(CaseExpression expr) {
        expr.getSwitchExpression().accept(this);
        for (Expression x : expr.getWhenClauses()) {
            x.accept(this);
        }
        expr.getElseExpression().accept(this);
    }


    public void visit(WhenClause expr) {
        expr.getWhenExpression().accept(this);
        expr.getThenExpression().accept(this);
    }


    public void visit(ExistsExpression expr) {
        expr.getRightExpression().accept(this);
    }


    public void visit(AllComparisonExpression expr) {

    }


    public void visit(AnyComparisonExpression expr) {

    }


    public void visit(Concat expr) {
        visitBinaryExpression(expr);
    }


    public void visit(Matches expr) {
        visitBinaryExpression(expr);
    }


    public void visit(BitwiseAnd expr) {
        visitBinaryExpression(expr);
    }


    public void visit(BitwiseOr expr) {
        visitBinaryExpression(expr);
    }


    public void visit(BitwiseXor expr) {
        visitBinaryExpression(expr);
    }


    public void visit(CastExpression expr) {
        expr.getLeftExpression().accept(this);
    }


    public void visit(Modulo expr) {
        visitBinaryExpression(expr);
    }


    public void visit(AnalyticExpression expr) {
        expr.getExpression().accept(this);
        expr.getDefaultValue().accept(this);
        expr.getOffset().accept(this);
        for (OrderByElement element : expr.getOrderByElements()) {
            element.getExpression().accept(this);
        }

        expr.getWindowElement().getRange().getStart().getExpression().accept(this);
        expr.getWindowElement().getRange().getEnd().getExpression().accept(this);
        expr.getWindowElement().getOffset().getExpression().accept(this);
    }


    public void visit(ExtractExpression expr) {
        expr.getExpression().accept(this);
    }


    public void visit(IntervalExpression expr) {

    }


    public void visit(OracleHierarchicalExpression expr) {
        expr.getConnectExpression().accept(this);
        expr.getStartExpression().accept(this);
    }


    public void visit(RegExpMatchOperator expr) {
        visitBinaryExpression(expr);
    }


    public void visit(ExpressionList expressionList) {
        for (Expression expr : expressionList.getExpressions()) {
            expr.accept(this);
        }
    }


    public void visit(MultiExpressionList multiExprList) {
        for (ExpressionList list : multiExprList.getExprList()) {
            visit(list);
        }
    }

    protected void visitBinaryExpression(BinaryExpression expr) {
        expr.getLeftExpression().accept(this);
        expr.getRightExpression().accept(this);
    }
}
