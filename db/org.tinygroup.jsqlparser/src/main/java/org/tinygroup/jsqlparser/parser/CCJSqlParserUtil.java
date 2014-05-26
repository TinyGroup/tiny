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
package org.tinygroup.jsqlparser.parser;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import org.tinygroup.jsqlparser.JSQLParserException;
import org.tinygroup.jsqlparser.expression.Expression;
import org.tinygroup.jsqlparser.statement.Statement;
import org.tinygroup.jsqlparser.statement.Statements;

/**
 * Toolfunctions to start and use JSqlParser.
 * @author toben
 */
public final class CCJSqlParserUtil {
	public static Statement parse(Reader statementReader) throws JSQLParserException {
		CCJSqlParser parser = new CCJSqlParser(statementReader);
		try {
			return parser.Statement();
		} catch (Exception ex) {
			throw new JSQLParserException(ex);
		} 
	}
	
	public static Statement parse(String sql) throws JSQLParserException {
		CCJSqlParser parser = new CCJSqlParser(new StringReader(sql));
		try {
			return parser.Statement();
		} catch (Exception ex) {
			throw new JSQLParserException(ex);
		} 
	}
	
	public static Statement parse(InputStream is) throws JSQLParserException {
		CCJSqlParser parser = new CCJSqlParser(is);
		try {
			return parser.Statement();
		} catch (Exception ex) {
			throw new JSQLParserException(ex);
		} 
	}
	
	public static Statement parse(InputStream is, String encoding) throws JSQLParserException {
		CCJSqlParser parser = new CCJSqlParser(is,encoding);
		try {
			return parser.Statement();
		} catch (Exception ex) {
			throw new JSQLParserException(ex);
		} 
	}
	
	/**
	 * Parse an expression.
	 * @param expression
	 * @return
	 * @throws JSQLParserException 
	 */
	public static Expression parseExpression(String expression) throws JSQLParserException {
		CCJSqlParser parser = new CCJSqlParser(new StringReader(expression));
		try {
			return parser.SimpleExpression();
		} catch (Exception ex) {
			throw new JSQLParserException(ex);
		} 
	}
    
    /**
	 * Parse an conditional expression. This is the expression after a where clause.
	 * @param condExpr
	 * @return
	 * @throws JSQLParserException 
	 */
	public static Expression parseCondExpression(String condExpr) throws JSQLParserException {
		CCJSqlParser parser = new CCJSqlParser(new StringReader(condExpr));
		try {
			return parser.Expression();
		} catch (Exception ex) {
			throw new JSQLParserException(ex);
		} 
	}
    
    /**
     * Parse a statement list.
     */
    public static Statements parseStatements(String sqls) throws JSQLParserException {
		CCJSqlParser parser = new CCJSqlParser(new StringReader(sqls));
		try {
			return parser.Statements();
		} catch (Exception ex) {
			throw new JSQLParserException(ex);
		} 
	}

	private CCJSqlParserUtil() {
	}
}
