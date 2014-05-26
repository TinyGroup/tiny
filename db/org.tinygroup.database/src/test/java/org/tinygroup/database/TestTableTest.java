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

public class TestTableTest extends TestCase {
	static {
		TestInit.init();

	}
	TableProcessor tableProcessor;

	protected void setUp() throws Exception {
		super.setUp();
		tableProcessor = SpringUtil.getBean(DataBaseUtil.TABLEPROCESSOR_BEAN);
	}

	public void testGetTableStringString() {
		assertNotNull(tableProcessor.getTable("org.tinygroup.test",
				"user"));
	}

	public void testGetTableString() {
		assertNotNull(tableProcessor.getTable("user"));
	}

	public void testGetCreateSqlStringStringString() {
		List<String> tableSql = tableProcessor.getCreateSql("user",
				"org.tinygroup.test", "mysql");
		System.out.println(tableSql);

	}
}
