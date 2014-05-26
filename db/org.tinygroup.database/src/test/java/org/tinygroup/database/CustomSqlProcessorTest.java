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

import junit.framework.TestCase;

import org.tinygroup.database.customesql.CustomSqlProcessor;
import org.tinygroup.database.util.DataBaseUtil;
import org.tinygroup.springutil.SpringUtil;

public class CustomSqlProcessorTest extends TestCase {
	static {
		TestInit.init();
	}
	private CustomSqlProcessor customSqlProcessor;

	protected void setUp() throws Exception {
		super.setUp();
		customSqlProcessor = SpringUtil.getBean(DataBaseUtil.CUSTOMESQL_BEAN);
	}

	public void testBefore() {
		System.out.println(customSqlProcessor.getCustomSqls("before", "oracle"));
		assertEquals(1, customSqlProcessor.getCustomSqls("before", "oracle").size());
	}
	public void testAfter() {
		System.out.println(customSqlProcessor.getCustomSqls("after", "oracle"));
		assertEquals(3, customSqlProcessor.getCustomSqls("after", "oracle").size());
	}
}
