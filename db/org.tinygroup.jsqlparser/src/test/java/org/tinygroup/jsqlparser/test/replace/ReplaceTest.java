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
package org.tinygroup.jsqlparser.test.replace;

import java.io.StringReader;

import junit.framework.TestCase;
import org.tinygroup.jsqlparser.JSQLParserException;
import org.tinygroup.jsqlparser.expression.JdbcParameter;
import org.tinygroup.jsqlparser.expression.LongValue;
import org.tinygroup.jsqlparser.expression.StringValue;
import org.tinygroup.jsqlparser.expression.operators.relational.ExpressionList;
import org.tinygroup.jsqlparser.parser.CCJSqlParserManager;
import org.tinygroup.jsqlparser.schema.Column;
import org.tinygroup.jsqlparser.statement.replace.Replace;
import org.tinygroup.jsqlparser.statement.select.SubSelect;

public class ReplaceTest extends TestCase {

	CCJSqlParserManager parserManager = new CCJSqlParserManager();

	public ReplaceTest(String arg0) {
		super(arg0);
	}

	public void testReplaceSyntax1() throws JSQLParserException {
		String statement = "REPLACE mytable SET col1='as', col2=?, col3=565";
		Replace replace = (Replace) parserManager.parse(new StringReader(statement));
		assertEquals("mytable", replace.getTable().getName());
		assertEquals(3, replace.getColumns().size());
		assertEquals("col1", ((Column) replace.getColumns().get(0)).getColumnName());
		assertEquals("col2", ((Column) replace.getColumns().get(1)).getColumnName());
		assertEquals("col3", ((Column) replace.getColumns().get(2)).getColumnName());
		assertEquals("as", ((StringValue) replace.getExpressions().get(0)).getValue());
		assertTrue(replace.getExpressions().get(1) instanceof JdbcParameter);
		assertEquals(565, ((LongValue) replace.getExpressions().get(2)).getValue());
		assertEquals(statement, "" + replace);

	}

	public void testReplaceSyntax2() throws JSQLParserException {
		String statement = "REPLACE mytable (col1, col2, col3) VALUES ('as', ?, 565)";
		Replace replace = (Replace) parserManager.parse(new StringReader(statement));
		assertEquals("mytable", replace.getTable().getName());
		assertEquals(3, replace.getColumns().size());
		assertEquals("col1", ((Column) replace.getColumns().get(0)).getColumnName());
		assertEquals("col2", ((Column) replace.getColumns().get(1)).getColumnName());
		assertEquals("col3", ((Column) replace.getColumns().get(2)).getColumnName());
		assertEquals("as", ((StringValue) ((ExpressionList) replace.getItemsList()).getExpressions().get(0)).getValue());
		assertTrue(((ExpressionList) replace.getItemsList()).getExpressions().get(1) instanceof JdbcParameter);
		assertEquals(565, ((LongValue) ((ExpressionList) replace.getItemsList()).getExpressions().get(2)).getValue());
		assertEquals(statement, "" + replace);
	}

	public void testReplaceSyntax3() throws JSQLParserException {
		String statement = "REPLACE mytable (col1, col2, col3) SELECT * FROM mytable3";
		Replace replace = (Replace) parserManager.parse(new StringReader(statement));
		assertEquals("mytable", replace.getTable().getName());
		assertEquals(3, replace.getColumns().size());
		assertEquals("col1", ((Column) replace.getColumns().get(0)).getColumnName());
		assertEquals("col2", ((Column) replace.getColumns().get(1)).getColumnName());
		assertEquals("col3", ((Column) replace.getColumns().get(2)).getColumnName());
		assertTrue(replace.getItemsList() instanceof SubSelect);
		// TODO:
		// assertEquals(statement, ""+replace);
	}
}
