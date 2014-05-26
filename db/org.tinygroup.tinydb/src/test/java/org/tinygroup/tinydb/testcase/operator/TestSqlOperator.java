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

import java.util.HashMap;
import java.util.Map;

public class TestSqlOperator extends BaseTest{
	
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
	
//	BeanType[] getPagedBeans(String beanType,String sql);
	public void testGetBeansBySql(){
		
		Bean[] insertBeans = getBeans(25);
		getOperator().batchDelete(insertBeans);
		getOperator().batchInsert(insertBeans);
		String sql = "select * from ANIMAL";
		Bean[] beans =getOperator().getBeans(sql);
		assertEquals(25, beans.length);
		
		getOperator().batchDelete(insertBeans);
	}
	
//	BeanType[] getPagedBeans(String beanType,String sql, int start, int limit);
	public void testPagingGetBeansBySql(){
		
		//getOperator().delete(getPagedBeans());
		Bean[] insertBeans = getBeans(25);
		getOperator().batchDelete(insertBeans);
		getOperator().batchInsert(insertBeans);
		
		String sql = "select * from ANIMAL";
		Bean[] beans = getOperator().getPagedBeans(sql, 0, 10);
		assertEquals(10, beans.length);
		beans = getOperator().getPagedBeans(sql, 11, 10);
		assertEquals(10, beans.length);
		beans = getOperator().getPagedBeans(sql, 21, 10);
		assertEquals(5, beans.length);
		
		getOperator().batchDelete(insertBeans);
	}
	
//	BeanType[] getPagedBeans(String beanType,String sql, Map<String, Object> parameters);
	public void testGetBeansBySqlAndMap(){
		
		Bean[] insertBeans = getBeans(25);
		getOperator().batchDelete(insertBeans);
		getOperator().batchInsert(insertBeans);
		String sql = "select * from ANIMAL  where name=@name";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("name", "testSql");
		Bean[] beans =getOperator().getBeans(sql, parameters);
		assertEquals(25, beans.length);
		getOperator().batchDelete(insertBeans);
	}
	
//	BeanType[] getPagedBeans(String beanType,String sql, int start, int limit,
//			Map<String, Object> parameters);
	public void testPagingGetBeansBySqlAndMap(){
		
		Bean[] insertBeans = getBeans(25);
		getOperator().batchInsert(insertBeans);
		String sql = "select * from ANIMAL  where name=@name";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("name", "testSql");
		
		Bean[] beans =getOperator().getPagedBeans(sql, 0, 10, parameters);
		assertEquals(10, beans.length);
		beans =getOperator().getPagedBeans(sql, 11, 10, parameters);
		assertEquals(10, beans.length);
		beans =getOperator().getPagedBeans(sql, 21, 10, parameters);
		assertEquals(5, beans.length);
		
		getOperator().batchDelete(insertBeans);
	}
	
//	BeanType[] getPagedBeans(String beanType,String sql, Object... parameters);
	public void testGetBeansBySqlAndArray(){
		Bean[] insertBeans = getBeans(25);
		getOperator().batchDelete(insertBeans);
		getOperator().batchInsert(insertBeans);
		String sql = "select * from ANIMAL  where name=? and length=?";
		Bean[] beans =getOperator().getBeans(sql, "testSql",1234);
//		System.out.println(beans[0].get("length").getClass());
//		assertEquals(12345, beans[0].get("length"));
		assertEquals(25, beans.length);
		getOperator().batchDelete(insertBeans);
	}
	
//	BeanType[] getPagedBeans(String beanType,String sql, int start, int limit, Object... parameters);
	public void testPagingGetBeansBySqlAndArray(){
		Bean[] insertBeans = getBeans(25);
		getOperator().batchDelete(insertBeans);
		getOperator().batchInsert(insertBeans);
		String sql = "select * from ANIMAL  where name=? and length=?";
		Bean[] beans =getOperator().getPagedBeans(sql, 0, 10, "testSql", 1234);
		assertEquals(10, beans.length);
		beans =getOperator().getPagedBeans(sql, 11, 10, "testSql", 1234);
		assertEquals(10, beans.length);
		beans =getOperator().getPagedBeans(sql, 21, 10, "testSql", 1234);
		assertEquals(5, beans.length);
		getOperator().batchDelete(insertBeans);
	}
}
