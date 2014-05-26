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
package org.tinygroup.jsqlparser.test;

import org.junit.Test;
import org.tinygroup.jsqlparser.JSQLParserException;
import org.tinygroup.jsqlparser.expression.Expression;
import org.tinygroup.jsqlparser.parser.CCJSqlParserUtil;
import org.tinygroup.jsqlparser.statement.Statement;
import org.tinygroup.jsqlparser.util.deparser.ExpressionDeParser;
import org.tinygroup.jsqlparser.util.deparser.SelectDeParser;
import org.tinygroup.jsqlparser.util.deparser.StatementDeParser;

import java.io.StringReader;

import static junit.framework.Assert.assertEquals;

/**
 *
 * @author toben
 */
public class TestUtils {

    public static void assertSqlCanBeParsedAndDeparsed(String statement) throws JSQLParserException {
        assertSqlCanBeParsedAndDeparsed(statement, false);
    }

    /**
     * Tries to parse and deparse the given statement.
     *
     * @param statement
     * @param laxDeparsingCheck removes all linefeeds from the original and
     * removes all double spaces. The check is caseinsensitive.
     * @throws JSQLParserException
     */
    public static void assertSqlCanBeParsedAndDeparsed(String statement, boolean laxDeparsingCheck) throws JSQLParserException {
        Statement parsed = CCJSqlParserUtil.parse(new StringReader(statement));
        assertStatementCanBeDeparsedAs(parsed, statement, laxDeparsingCheck);
    }

    public static void assertStatementCanBeDeparsedAs(Statement parsed, String statement) {
        assertStatementCanBeDeparsedAs(parsed, statement, false);
    }

    public static void assertStatementCanBeDeparsedAs(Statement parsed, String statement, boolean laxDeparsingCheck) {
        assertEquals(buildSqlString(statement, laxDeparsingCheck), 
                buildSqlString(parsed.toString(), laxDeparsingCheck));

        StatementDeParser deParser = new StatementDeParser(new StringBuilder());
        parsed.accept(deParser);
        assertEquals(buildSqlString(statement, laxDeparsingCheck), 
                buildSqlString(deParser.getBuffer().toString(), laxDeparsingCheck));
    }

    public static String buildSqlString(String sql, boolean laxDeparsingCheck) {
        if (laxDeparsingCheck) {
            return sql.replaceAll("\\s", " ").replaceAll("\\s+", " ").replaceAll("\\s*([/,()=+\\-*|\\]<>])\\s*", "$1").toLowerCase().trim();
        } else {
            return sql;
        }
    }
    
    @Test
    public void testBuildSqlString() {
        assertEquals("select col from test", buildSqlString("   SELECT   col FROM  \r\n \t  TEST \n", true));
        assertEquals("select  col  from test", buildSqlString("select  col  from test", false));
    }

    public static void assertExpressionCanBeDeparsedAs(final Expression parsed, String expression) {
        ExpressionDeParser expressionDeParser = new ExpressionDeParser();
        StringBuilder stringBuilder = new StringBuilder();
        expressionDeParser.setBuffer(stringBuilder);
        SelectDeParser selectDeParser = new SelectDeParser(expressionDeParser, stringBuilder);
        expressionDeParser.setSelectVisitor(selectDeParser);
        parsed.accept(expressionDeParser);

        assertEquals(expression, stringBuilder.toString());
    }
}
