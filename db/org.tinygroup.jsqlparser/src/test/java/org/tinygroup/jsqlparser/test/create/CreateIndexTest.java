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

import junit.framework.TestCase;
import org.tinygroup.jsqlparser.JSQLParserException;
import org.tinygroup.jsqlparser.parser.CCJSqlParserManager;
import org.tinygroup.jsqlparser.statement.create.index.CreateIndex;
import static org.tinygroup.jsqlparser.test.TestUtils.*;

/**
 * @author Raymond Augé
 */
public class CreateIndexTest extends TestCase {

	CCJSqlParserManager parserManager = new CCJSqlParserManager();

	public CreateIndexTest(String arg0) {
		super(arg0);
	}

	public void testCreateIndex() throws JSQLParserException {
		String statement =
			"CREATE INDEX myindex ON mytab (mycol, mycol2)";
		CreateIndex createIndex = (CreateIndex) parserManager.parse(new StringReader(statement));
		assertEquals(2, createIndex.getIndex().getColumnsNames().size());
		assertEquals("myindex", createIndex.getIndex().getName());
		assertNull(createIndex.getIndex().getType());
		assertEquals("mytab", createIndex.getTable().getFullyQualifiedName());
		assertEquals("mycol", createIndex.getIndex().getColumnsNames().get(0));
		assertEquals(statement, ""+createIndex);
	}

	public void testCreateIndex2() throws JSQLParserException {
		String statement =
			"CREATE mytype INDEX myindex ON mytab (mycol, mycol2)";
		CreateIndex createIndex = (CreateIndex) parserManager.parse(new StringReader(statement));
		assertEquals(2, createIndex.getIndex().getColumnsNames().size());
		assertEquals("myindex", createIndex.getIndex().getName());
		assertEquals("mytype", createIndex.getIndex().getType());
		assertEquals("mytab", createIndex.getTable().getFullyQualifiedName());
		assertEquals("mycol2", createIndex.getIndex().getColumnsNames().get(1));
		assertEquals(statement, ""+createIndex);
	}

	public void testCreateIndex3() throws JSQLParserException {
		String statement =
			"CREATE mytype INDEX myindex ON mytab (mycol ASC, mycol2, mycol3)";
		CreateIndex createIndex = (CreateIndex) parserManager.parse(new StringReader(statement));
		assertEquals(3, createIndex.getIndex().getColumnsNames().size());
		assertEquals("myindex", createIndex.getIndex().getName());
		assertEquals("mytype", createIndex.getIndex().getType());
		assertEquals("mytab", createIndex.getTable().getFullyQualifiedName());
		assertEquals("mycol3", createIndex.getIndex().getColumnsNames().get(2));
	}

	public void testCreateIndex4() throws JSQLParserException {
		String statement =
			"CREATE mytype INDEX myindex ON mytab (mycol ASC, mycol2 (75), mycol3)";
		CreateIndex createIndex = (CreateIndex) parserManager.parse(new StringReader(statement));
		assertEquals(3, createIndex.getIndex().getColumnsNames().size());
		assertEquals("myindex", createIndex.getIndex().getName());
		assertEquals("mytype", createIndex.getIndex().getType());
		assertEquals("mytab", createIndex.getTable().getFullyQualifiedName());
		assertEquals("mycol3", createIndex.getIndex().getColumnsNames().get(2));
	}

	public void testCreateIndex5() throws JSQLParserException {
		String statement =
			"CREATE mytype INDEX myindex ON mytab (mycol ASC, mycol2 (75), mycol3) mymodifiers";
		CreateIndex createIndex = (CreateIndex) parserManager.parse(new StringReader(statement));
		assertEquals(3, createIndex.getIndex().getColumnsNames().size());
		assertEquals("myindex", createIndex.getIndex().getName());
		assertEquals("mytype", createIndex.getIndex().getType());
		assertEquals("mytab", createIndex.getTable().getFullyQualifiedName());
		assertEquals("mycol3", createIndex.getIndex().getColumnsNames().get(2));
	}

	public void testCreateIndex6() throws JSQLParserException {
		String stmt= "CREATE INDEX myindex ON mytab (mycol, mycol2)";
		assertSqlCanBeParsedAndDeparsed(stmt);
	}
}
