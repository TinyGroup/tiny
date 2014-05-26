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
package org.tinygroup.jsqlparser.util;

import java.io.StringReader;
import org.tinygroup.jsqlparser.JSQLParserException;
import org.tinygroup.jsqlparser.parser.CCJSqlParserManager;
import org.tinygroup.jsqlparser.statement.select.Select;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tw
 */
public class AddAliasesVisitorTest {

	public AddAliasesVisitorTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}
	CCJSqlParserManager parserManager = new CCJSqlParserManager();

	/**
	 * Test of visit method, of class AddAliasesVisitor.
	 */
	@Test
	public void testVisit_PlainSelect() throws JSQLParserException {
		String sql = "select a,b,c from test";
		Select select = (Select) parserManager.parse(new StringReader(sql));
		final AddAliasesVisitor instance = new AddAliasesVisitor();
		select.getSelectBody().accept(instance);

		assertEquals("SELECT a AS A1, b AS A2, c AS A3 FROM test", select.toString());
	}

	@Test
	public void testVisit_PlainSelect_duplicates() throws JSQLParserException {
		String sql = "select a,b as a1,c from test";
		Select select = (Select) parserManager.parse(new StringReader(sql));
		final AddAliasesVisitor instance = new AddAliasesVisitor();
		select.getSelectBody().accept(instance);

		assertEquals("SELECT a AS A2, b AS a1, c AS A3 FROM test", select.toString());
	}

	@Test
	public void testVisit_PlainSelect_expression() throws JSQLParserException {
		String sql = "select 3+4 from test";
		Select select = (Select) parserManager.parse(new StringReader(sql));
		final AddAliasesVisitor instance = new AddAliasesVisitor();
		select.getSelectBody().accept(instance);

		assertEquals("SELECT 3 + 4 AS A1 FROM test", select.toString());
	}

	/**
	 * Test of visit method, of class AddAliasesVisitor.
	 */
	@Test
	public void testVisit_SetOperationList() throws JSQLParserException {
		String sql = "select 3+4 from test union select 7+8 from test2";
		Select setOpList = (Select) parserManager.parse(new StringReader(sql));
		final AddAliasesVisitor instance = new AddAliasesVisitor();
		setOpList.getSelectBody().accept(instance);

		assertEquals("(SELECT 3 + 4 AS A1 FROM test) UNION (SELECT 7 + 8 AS A1 FROM test2)", setOpList.toString());
	}
}
