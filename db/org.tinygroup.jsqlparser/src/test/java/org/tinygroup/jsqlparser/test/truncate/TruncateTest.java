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
package org.tinygroup.jsqlparser.test.truncate;

import java.io.StringReader;

import junit.framework.TestCase;
import org.tinygroup.jsqlparser.parser.CCJSqlParserManager;
import org.tinygroup.jsqlparser.statement.truncate.Truncate;

public class TruncateTest extends TestCase {

	private CCJSqlParserManager parserManager = new CCJSqlParserManager();

	public TruncateTest(String arg0) {
		super(arg0);
	}

	public void testTruncate() throws Exception {
		String statement = "TRUncATE TABLE myschema.mytab";
		Truncate truncate = (Truncate) parserManager.parse(new StringReader(statement));
		assertEquals("myschema", truncate.getTable().getSchemaName());
		assertEquals("myschema.mytab", truncate.getTable().getFullyQualifiedName());
		assertEquals(statement.toUpperCase(), truncate.toString().toUpperCase());

		statement = "TRUncATE   TABLE    mytab";
		String toStringStatement = "TRUncATE TABLE mytab";
		truncate = (Truncate) parserManager.parse(new StringReader(statement));
		assertEquals("mytab", truncate.getTable().getName());
		assertEquals(toStringStatement.toUpperCase(), truncate.toString().toUpperCase());
	}
}
