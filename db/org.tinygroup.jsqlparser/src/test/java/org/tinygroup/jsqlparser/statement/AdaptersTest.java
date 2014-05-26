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
package org.tinygroup.jsqlparser.statement;

import org.tinygroup.jsqlparser.JSQLParserException;
import org.tinygroup.jsqlparser.expression.BinaryExpression;
import org.tinygroup.jsqlparser.expression.ExpressionVisitorAdapter;
import org.tinygroup.jsqlparser.expression.JdbcNamedParameter;
import org.tinygroup.jsqlparser.expression.operators.conditional.AndExpression;
import org.tinygroup.jsqlparser.parser.CCJSqlParserUtil;
import org.tinygroup.jsqlparser.schema.Column;
import org.tinygroup.jsqlparser.statement.select.PlainSelect;
import org.tinygroup.jsqlparser.statement.select.Select;
import org.tinygroup.jsqlparser.statement.select.SelectVisitorAdapter;
import org.junit.Test;

import java.util.Stack;

import static org.junit.Assert.assertEquals;

/**
 * @author aalmiray
 */
public class AdaptersTest {
    /**
     * Test extracting JDBC named parameters using adapters
     */
    @Test
    public void testAdapters() throws JSQLParserException {
        String sql = "SELECT * FROM MYTABLE WHERE COLUMN_A = :paramA AND COLUMN_B <> :paramB";
        Statement stmnt = CCJSqlParserUtil.parse(sql);

        final Stack<Pair<String, String>> params = new Stack<Pair<String, String>>();
        stmnt.accept(new StatementVisitorAdapter() {

            public void visit(Select select) {
                select.getSelectBody().accept(new SelectVisitorAdapter() {

                    public void visit(PlainSelect plainSelect) {
                        plainSelect.getWhere().accept(new ExpressionVisitorAdapter() {

                            protected void visitBinaryExpression(BinaryExpression expr) {
                                if (!(expr instanceof AndExpression)) {
                                    params.push(new Pair<String, String>(null, null));
                                }
                                super.visitBinaryExpression(expr);
                            }


                            public void visit(Column column) {
                                params.push(new Pair<String, String>(column.getColumnName(), params.pop().getRight()));
                            }


                            public void visit(JdbcNamedParameter parameter) {
                                params.push(new Pair<String, String>(params.pop().getLeft(), parameter.getName()));
                            }
                        });
                    }
                });
            }
        });

        assertEquals(2, params.size());
        Pair<String, String> param2 = params.pop();
        assertEquals("COLUMN_B", param2.getLeft());
        assertEquals("paramB", param2.getRight());
        Pair<String, String> param1 = params.pop();
        assertEquals("COLUMN_A", param1.getLeft());
        assertEquals("paramA", param1.getRight());
    }

    private static class Pair<L, R> {
        private final L left;
        private final R right;

        private Pair(L left, R right) {
            this.left = left;
            this.right = right;
        }

        public L getLeft() {
            return left;
        }

        public R getRight() {
            return right;
        }

        public boolean isEmpty() {
            return left == null && right == null;
        }

        public boolean isFull() {
            return left != null && right != null;
        }


        public String toString() {
            final StringBuilder sb = new StringBuilder("Pair{");
            sb.append("left=").append(left);
            sb.append(", right=").append(right);
            sb.append('}');
            return sb.toString();
        }
    }
}
