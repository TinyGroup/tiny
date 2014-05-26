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
package org.tinygroup.jsqlparser.test.create;

import java.io.StringReader;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

import junit.framework.TestCase;
import org.tinygroup.jsqlparser.JSQLParserException;
import org.tinygroup.jsqlparser.parser.CCJSqlParserManager;
import org.tinygroup.jsqlparser.schema.Table;
import org.tinygroup.jsqlparser.statement.create.view.CreateView;
import org.tinygroup.jsqlparser.statement.select.PlainSelect;
import static org.tinygroup.jsqlparser.test.TestUtils.*;

public class CreateViewTest extends TestCase {

	CCJSqlParserManager parserManager = new CCJSqlParserManager();

	public CreateViewTest(String arg0) {
		super(arg0);
	}

	public void testCreateView() throws JSQLParserException {
		String statement = "CREATE VIEW myview AS SELECT * FROM mytab";
		CreateView createView = (CreateView) parserManager.parse(new StringReader(statement));
		assertFalse(createView.isOrReplace());
		assertEquals("myview", createView.getView().getName());
		assertEquals("mytab", ((Table) ((PlainSelect) createView.getSelectBody()).getFromItem()).getName());
		assertEquals(statement, createView.toString());
	}

	public void testCreateView2() throws JSQLParserException {
		String stmt = "CREATE VIEW myview AS SELECT * FROM mytab";
		assertSqlCanBeParsedAndDeparsed(stmt);
	}

	public void testCreateView3() throws JSQLParserException {
		String stmt = "CREATE OR REPLACE VIEW myview AS SELECT * FROM mytab";
		assertSqlCanBeParsedAndDeparsed(stmt);
	}

	public void testCreateView4() throws JSQLParserException {
		String stmt = "CREATE OR REPLACE VIEW view2 AS SELECT a, b, c FROM testtab INNER JOIN testtab2 ON testtab.col1 = testtab2.col2";
		assertSqlCanBeParsedAndDeparsed(stmt);
	}
	
	public void testCreateViewWithColumnNames1() throws JSQLParserException {
		String stmt = "CREATE OR REPLACE VIEW view1(col1, col2) AS SELECT a, b FROM testtab";
		assertSqlCanBeParsedAndDeparsed(stmt);
	}
	
	public void testCreateView5() throws JSQLParserException {
		String statement = "CREATE VIEW myview AS (SELECT * FROM mytab)";
		String statement2 = "CREATE VIEW myview AS SELECT * FROM mytab";
		CreateView createView = (CreateView) parserManager.parse(new StringReader(statement));
		assertFalse(createView.isOrReplace());
		assertEquals("myview", createView.getView().getName());
		assertEquals("mytab", ((Table) ((PlainSelect) createView.getSelectBody()).getFromItem()).getName());
		assertEquals(statement2, createView.toString());
	}
	
	public void testCreateViewUnion() throws JSQLParserException {
		String stmt = "CREATE VIEW view1 AS (SELECT a, b FROM testtab) UNION (SELECT b, c FROM testtab2)";
		assertSqlCanBeParsedAndDeparsed(stmt);
	}
	
	public void testCreateMaterializedView() throws JSQLParserException {
		String stmt = "CREATE MATERIALIZED VIEW view1 AS SELECT a, b FROM testtab";
		assertSqlCanBeParsedAndDeparsed(stmt);
	}
}
