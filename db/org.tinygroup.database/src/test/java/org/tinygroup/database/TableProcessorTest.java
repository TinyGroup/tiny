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
package org.tinygroup.database;

import java.util.List;

import junit.framework.TestCase;

import org.tinygroup.database.table.TableProcessor;
import org.tinygroup.database.util.DataBaseUtil;
import org.tinygroup.springutil.SpringUtil;

public class TableProcessorTest extends TestCase {
	static {
		TestInit.init();

	}
	TableProcessor tableProcessor;

	protected void setUp() throws Exception {
		super.setUp();
		tableProcessor = SpringUtil.getBean(DataBaseUtil.TABLEPROCESSOR_BEAN);
	}

	public void testGetTableStringString() {
		assertNotNull(tableProcessor.getTable("database", "aa"));
	}

	public void testGetTableString() {
		assertNotNull(tableProcessor.getTable("aa"));
	}

	public void testGetCreateSqlStringStringString() {
		System.out.println("aa.bb.aa,sql:");
		List<String> tableSql= tableProcessor.getCreateSql("aa", "aa.bb", "oracle");
		System.out.println(tableSql);
//		assertEquals("CREATE TABLE aa(aa varchar(12),bb varchar(112))",
//				tableSql);
	}

	public void testGetCreateSqlStringStringString1() {
		List<String> tableSql = tableProcessor.getCreateSql("bb", "aa.bb", "oracle");
		System.out.println("aa.bb.bb,sql:");
		System.out.println(tableSql);
	}

	public void testGetCreateSqlStringString() {
		List<String> tableSql = tableProcessor.getCreateSql("aa", "oracle");
		System.out.println("aa,sql:");
		System.out.println(tableSql);
	}
	
	public void testGetCreateSqlStringStringMySql() {
		List<String> tableSql = tableProcessor.getCreateSql("aa", "mysql");
		System.out.println("aa,mysql sql:");
		System.out.println(tableSql);
	}

}
