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
package org.tinygroup.jsqlparser.test.update;

import java.io.StringReader;
import static junit.framework.Assert.assertEquals;

import junit.framework.TestCase;
import org.tinygroup.jsqlparser.JSQLParserException;
import org.tinygroup.jsqlparser.expression.JdbcParameter;
import org.tinygroup.jsqlparser.expression.LongValue;
import org.tinygroup.jsqlparser.expression.StringValue;
import org.tinygroup.jsqlparser.expression.operators.relational.GreaterThanEquals;
import org.tinygroup.jsqlparser.parser.CCJSqlParserManager;
import org.tinygroup.jsqlparser.schema.Column;
import org.tinygroup.jsqlparser.statement.update.Update;
import static org.tinygroup.jsqlparser.test.TestUtils.*;

public class UpdateTest extends TestCase {

	CCJSqlParserManager parserManager = new CCJSqlParserManager();

	public UpdateTest(String arg0) {
		super(arg0);
	}

	public void testUpdate() throws JSQLParserException {
		String statement = "UPDATE mytable set col1='as', col2=?, col3=565 Where o >= 3";
		Update update = (Update) parserManager.parse(new StringReader(statement));
		assertEquals("mytable", update.getTable().getName());
		assertEquals(3, update.getColumns().size());
		assertEquals("col1", ((Column) update.getColumns().get(0)).getColumnName());
		assertEquals("col2", ((Column) update.getColumns().get(1)).getColumnName());
		assertEquals("col3", ((Column) update.getColumns().get(2)).getColumnName());
		assertEquals("as", ((StringValue) update.getExpressions().get(0)).getValue());
		assertTrue(update.getExpressions().get(1) instanceof JdbcParameter);
		assertEquals(565, ((LongValue) update.getExpressions().get(2)).getValue());

		assertTrue(update.getWhere() instanceof GreaterThanEquals);
	}

	public void testUpdateWAlias() throws JSQLParserException {
		String statement = "UPDATE table1 A SET A.columna = 'XXX' WHERE A.cod_table = 'YYY'";
		Update update = (Update) parserManager.parse(new StringReader(statement));
	}
	
	public void testUpdateWithDeparser() throws JSQLParserException {
		assertSqlCanBeParsedAndDeparsed("UPDATE table1 AS A SET A.columna = 'XXX' WHERE A.cod_table = 'YYY'");
	}
	
	public void testUpdateWithFrom() throws JSQLParserException {
		assertSqlCanBeParsedAndDeparsed("UPDATE table1 SET columna = 5 FROM table1 LEFT JOIN table2 ON col1 = col2");
	}
}
