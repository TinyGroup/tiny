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
package org.tinygroup.jsqlparser.test.insert;

import java.io.StringReader;
import static junit.framework.Assert.assertEquals;

import junit.framework.TestCase;
import org.tinygroup.jsqlparser.JSQLParserException;
import org.tinygroup.jsqlparser.expression.DoubleValue;
import org.tinygroup.jsqlparser.expression.JdbcParameter;
import org.tinygroup.jsqlparser.expression.LongValue;
import org.tinygroup.jsqlparser.expression.StringValue;
import org.tinygroup.jsqlparser.expression.operators.relational.ExpressionList;
import org.tinygroup.jsqlparser.parser.CCJSqlParserManager;
import org.tinygroup.jsqlparser.schema.Column;
import org.tinygroup.jsqlparser.schema.Table;
import org.tinygroup.jsqlparser.statement.insert.Insert;
import org.tinygroup.jsqlparser.statement.select.PlainSelect;
import org.tinygroup.jsqlparser.statement.select.SubSelect;
import static org.tinygroup.jsqlparser.test.TestUtils.*;

public class InsertTest extends TestCase {

	CCJSqlParserManager parserManager = new CCJSqlParserManager();

	public InsertTest(String arg0) {
		super(arg0);
	}

	public void testRegularInsert() throws JSQLParserException {
		String statement = "INSERT INTO mytable (col1, col2, col3) VALUES (?, 'sadfsd', 234)";
		Insert insert = (Insert) parserManager.parse(new StringReader(statement));
		assertEquals("mytable", insert.getTable().getName());
		assertEquals(3, insert.getColumns().size());
		assertEquals("col1", ((Column) insert.getColumns().get(0)).getColumnName());
		assertEquals("col2", ((Column) insert.getColumns().get(1)).getColumnName());
		assertEquals("col3", ((Column) insert.getColumns().get(2)).getColumnName());
		assertEquals(3, ((ExpressionList) insert.getItemsList()).getExpressions().size());
		assertTrue(((ExpressionList) insert.getItemsList()).getExpressions().get(0) instanceof JdbcParameter);
		assertEquals("sadfsd",
				((StringValue) ((ExpressionList) insert.getItemsList()).getExpressions().get(1)).getValue());
		assertEquals(234, ((LongValue) ((ExpressionList) insert.getItemsList()).getExpressions().get(2)).getValue());
		assertEquals(statement, "" + insert);

		statement = "INSERT INTO myschema.mytable VALUES (?, ?, 2.3)";
		insert = (Insert) parserManager.parse(new StringReader(statement));
		assertEquals("myschema.mytable", insert.getTable().getFullyQualifiedName());
		assertEquals(3, ((ExpressionList) insert.getItemsList()).getExpressions().size());
		assertTrue(((ExpressionList) insert.getItemsList()).getExpressions().get(0) instanceof JdbcParameter);
		assertEquals(2.3, ((DoubleValue) ((ExpressionList) insert.getItemsList()).getExpressions().get(2)).getValue(),
				0.0);
		assertEquals(statement, "" + insert);

	}

	public void testInsertWithKeywordValue() throws JSQLParserException {
		String statement = "INSERT INTO mytable (col1) VALUE ('val1')";
		Insert insert = (Insert) parserManager.parse(new StringReader(statement));
		assertEquals("mytable", insert.getTable().getName());
		assertEquals(1, insert.getColumns().size());
		assertEquals("col1", ((Column) insert.getColumns().get(0)).getColumnName());
		assertEquals("val1",
				((StringValue) ((ExpressionList) insert.getItemsList()).getExpressions().get(0)).getValue());
		assertEquals("INSERT INTO mytable (col1) VALUES ('val1')", insert.toString());
	}

	public void testInsertFromSelect() throws JSQLParserException {
		String statement = "INSERT INTO mytable (col1, col2, col3) SELECT * FROM mytable2";
		Insert insert = (Insert) parserManager.parse(new StringReader(statement));
		assertEquals("mytable", insert.getTable().getName());
		assertEquals(3, insert.getColumns().size());
		assertEquals("col1", ((Column) insert.getColumns().get(0)).getColumnName());
		assertEquals("col2", ((Column) insert.getColumns().get(1)).getColumnName());
		assertEquals("col3", ((Column) insert.getColumns().get(2)).getColumnName());
		assertTrue(insert.getItemsList() instanceof SubSelect);
		assertEquals("mytable2",
				((Table) ((PlainSelect) ((SubSelect) insert.getItemsList()).getSelectBody()).getFromItem()).getName());

		// toString uses brakets
		String statementToString = "INSERT INTO mytable (col1, col2, col3) (SELECT * FROM mytable2)";
		assertEquals(statementToString, "" + insert);
	}

	public void testInsertMultiRowValue() throws JSQLParserException {
		assertSqlCanBeParsedAndDeparsed("INSERT INTO mytable (col1, col2) VALUES (a, b), (d, e)");
	}

	public void testInsertMultiRowValueDifferent() throws JSQLParserException {
		try {
			assertSqlCanBeParsedAndDeparsed("INSERT INTO mytable (col1, col2) VALUES (a, b), (d, e, c)");
		} catch (Exception e) {
			return;
		}

		fail("should not work");
	}

	public void testSimpleInsert() throws JSQLParserException {
		assertSqlCanBeParsedAndDeparsed("INSERT INTO example (num, name, address, tel) VALUES (1, 'name', 'test ', '1234-1234')");
	}
}
