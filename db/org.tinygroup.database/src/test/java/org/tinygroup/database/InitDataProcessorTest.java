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

import org.tinygroup.database.initdata.InitDataProcessor;
import org.tinygroup.database.util.DataBaseUtil;
import org.tinygroup.springutil.SpringUtil;

public class InitDataProcessorTest extends TestCase {
	static {
		TestInit.init();
	}
	private InitDataProcessor initDataProcessor;

	protected void setUp() throws Exception {
		super.setUp();
		initDataProcessor = SpringUtil.getBean(DataBaseUtil.INITDATA_BEAN);
	}

	public void testGetInitSql() {
		System.out.println(initDataProcessor.getInitSql("oracle").toString());
		assertEquals(2, initDataProcessor.getInitSql("oracle").size());
	}
	
	public void testGetDeInitSql() {
		System.out.println(initDataProcessor.getDeinitSql("oracle").toString());
		assertEquals(2, initDataProcessor.getDeinitSql("oracle").size());
	}

	public void testGetInitSqlByTable(){
		
		List<String> list =initDataProcessor.getInitSql("aa","oracle");
		System.out.println("testGetInitSqlByTable,aa:");
		System.out.println(list.toString());
		assertEquals(1, list.size());
	}
	
	public void testGetInitSqlByTableAndPackage(){
		List<String> list =initDataProcessor.getInitSql("aa.bb" ,"aa","oracle");
		System.out.println(list.toString());
		assertEquals(1, list.size());
	}
	
	public void testGetInitDataByTable(){
		assertFalse(initDataProcessor.getInitData("aa")==null);
		assertEquals("aa.bb",initDataProcessor.getInitData("aa").getPackageName());
		assertEquals("aatable",initDataProcessor.getInitData("aa").getTableId());
	}
	public void testGetInitDataByTable1(){
		assertFalse(initDataProcessor.getInitData("aa.bb" ,"aa")==null);
		assertEquals("aa.bb",initDataProcessor.getInitData("aa.bb" ,"aa").getPackageName());
		assertEquals("aatable",initDataProcessor.getInitData("aa.bb" ,"aa").getTableId());
	}
}
