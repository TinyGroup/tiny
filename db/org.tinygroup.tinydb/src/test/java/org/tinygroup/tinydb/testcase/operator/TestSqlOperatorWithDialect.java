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
package org.tinygroup.tinydb.testcase.operator;

import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.test.BaseTest;

/**
 * 
 * 功能说明: 带方言函数的sql语句测试用例

 * 开发人员: renhui <br>
 * 开发时间: 2013-8-21 <br>
 * <br>
 */
public class TestSqlOperatorWithDialect  extends BaseTest{
	

	private Bean getBean(String id){
		Bean bean = new Bean(ANIMAL);
		bean.setProperty("id",id);
		bean.setProperty("name","testSql");
		bean.setProperty("length","1234");
		return bean;
	}
	
	private Bean[] getBeans(int length){
		Bean[] insertBeans = new Bean[length];
		for(int i = 0 ; i < length ; i++ ){
			insertBeans[i] = getBean(i+"");
		}
		return insertBeans;
	}

	public void testDialectFunction(){
		Bean[] insertBeans = getBeans(25);
		getOperator().batchDelete(insertBeans);
		getOperator().batchInsert(insertBeans);
		String sql = "select #{count(*)} as acount from ANIMAL";
		Bean value =getOperator().getSingleValue(sql);
		assertEquals(25, value.getProperty("acount"));
		
		sql="select #{concat(name,'con')} as con from ANIMAL";
		Bean[] beans= getOperator().getBeans(sql);
		
		assertEquals("testSqlcon", beans[0].getProperty("con"));
		
		getOperator().batchDelete(insertBeans);
	}
	public void testMutiDialectFunction(){
		Bean[] insertBeans = getBeans(25);
		getOperator().batchDelete(insertBeans);
		getOperator().batchInsert(insertBeans);
		String sql="select #{substr(#{nvl(name,'')},1,2)} as aname from ANIMAL";
		Bean[] beans= getOperator().getBeans(sql);
		assertEquals("te", beans[0].getProperty("aname"));
		getOperator().batchDelete(insertBeans);
		
	}
}
