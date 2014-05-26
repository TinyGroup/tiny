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
import org.tinygroup.jsqlparser.expression.BinaryExpression;
import org.tinygroup.jsqlparser.expression.operators.arithmetic.Addition;
import org.tinygroup.jsqlparser.expression.operators.arithmetic.Concat;
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
public class ConnectExpressionsVisitorTest {

	public ConnectExpressionsVisitorTest() {
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

	@Test
	public void testVisit_PlainSelect_concat() throws JSQLParserException {
		String sql = "select a,b,c from test";
		Select select = (Select) parserManager.parse(new StringReader(sql));
		ConnectExpressionsVisitor instance = new ConnectExpressionsVisitor() {

			protected BinaryExpression createBinaryExpression() {
				return new Concat();
			}
		};
		select.getSelectBody().accept(instance);

		assertEquals("SELECT a || b || c AS expr FROM test", select.toString());
	}

	@Test
	public void testVisit_PlainSelect_addition() throws JSQLParserException {
		String sql = "select a,b,c from test";
		Select select = (Select) parserManager.parse(new StringReader(sql));
		ConnectExpressionsVisitor instance = new ConnectExpressionsVisitor("testexpr") {

			protected BinaryExpression createBinaryExpression() {
				return new Addition();
			}
		};
		select.getSelectBody().accept(instance);

		assertEquals("SELECT a + b + c AS testexpr FROM test", select.toString());
	}
}
