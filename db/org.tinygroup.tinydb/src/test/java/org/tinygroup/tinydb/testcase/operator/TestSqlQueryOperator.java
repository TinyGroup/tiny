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
import org.tinygroup.tinydb.order.OrderBean;
import org.tinygroup.tinydb.order.impl.OrderByBeanDefault;
import org.tinygroup.tinydb.query.Conditions;
import org.tinygroup.tinydb.select.SelectBean;
import org.tinygroup.tinydb.select.impl.SelectBeanDefault;
import org.tinygroup.tinydb.test.BaseTest;

public class TestSqlQueryOperator extends BaseTest{
	
	private Bean getBean(String id,String name){
		Bean bean = new Bean(ANIMAL);
		bean.setProperty("id",id);
		bean.setProperty("name",name);
		bean.setProperty("length","1234");
		return bean;
	}
	
	private Bean[] getBeans(int length){
		Bean[] insertBeans = new Bean[length*2];
		for(int i = 0 ; i < length ; i++ ){
			insertBeans[i] = getBean(i+"","name"+i);
		}
		for(int i = length ; i < length*2 ; i++ ){
			insertBeans[i] = getBean(i+"","bean"+i);
		}
		return insertBeans;
	}
	
//	BeanType[] getPagedBeans(String beanType, SelectBean[] selectBeans,
//	QueryBean queryBean, OrderBean[] orderBeans);
	public void testGetBeans(){
		
		Bean[] insertBeans = getBeans(5);
		getOperator().batchDelete(insertBeans);
		getOperator().batchInsert(insertBeans);
		SelectBean[] selectBeans=new SelectBean[1];
		selectBeans[0]=new SelectBeanDefault("id");
		Conditions conditions=new Conditions();
		conditions.condition("name", "like", "%bean%");
		OrderBean[] orderBeans=new OrderBean[1];
		orderBeans[0]=new OrderByBeanDefault("name", "asc");
		Bean[] beans =getOperator().getBeans(selectBeans, conditions,orderBeans);
		assertEquals(5, beans.length);
		getOperator().batchDelete(insertBeans);
	}
	
//	BeanType[] getPagedBeans(String beanType, SelectBean[] selectBeans,
//	QueryBean queryBean, OrderBean[] orderBeans, int start, int limit);
	public void testPagingGetBeans(){
		
		Bean[] insertBeans = getBeans(12);
		getOperator().batchDelete(insertBeans);
		getOperator().batchInsert(insertBeans);

		SelectBean[] selectBeans=new SelectBean[1];
		selectBeans[0]=new SelectBeanDefault("id");
		Conditions conditions=new Conditions();
		conditions.condition("name", "like", "%bean%");
		
		OrderBean[] orderBeans=new OrderBean[1];
		orderBeans[0]=new OrderByBeanDefault("name", "asc");
		
		Bean[] beans = getOperator().getBeans(selectBeans, conditions,orderBeans,0,5);
		assertEquals(5, beans.length);
		beans = getOperator().getBeans(selectBeans, conditions,orderBeans,6,5);
		assertEquals(5, beans.length);
		beans = getOperator().getBeans(selectBeans, conditions,orderBeans,11,5);
		assertEquals(2, beans.length);
		getOperator().batchDelete(insertBeans);
	}
	
//	<T> T getSingleValue(String beanType, SelectBean[] selectBeans,
//	QueryBean queryBean);
	public void testGetSingleObject(){
		
		Bean[] insertBeans = getBeans(5);
		getOperator().batchDelete(insertBeans);
		getOperator().batchInsert(insertBeans);
		SelectBean[] selectBeans=new SelectBean[2];
		selectBeans[0]=new SelectBeanDefault("id");
		selectBeans[1]=new SelectBeanDefault("name");
		Conditions conditions=new Conditions();
		conditions.condition("name", "=", "bean5");;
		Bean bean =getOperator().getSingleValue(selectBeans, conditions);
		assertEquals("bean5", bean.get("name"));
		getOperator().batchDelete(insertBeans);
	}
//	BeanType[] getPagedBeans(String beanType, String selectClause,
//			QueryBean queryBean, OrderBean[] orderBeans);
     public void testGetBeansWithSelectClause(){
		
 		Bean[] insertBeans = getBeans(5);
 		getOperator().batchDelete(insertBeans);
 		getOperator().batchInsert(insertBeans);
 		
 		String slectClause="id";
 		Conditions conditions=new Conditions();
		conditions.condition("name", "like", "%bean%");
 		
 		OrderBean[] orderBeans=new OrderBean[1];
 		orderBeans[0]=new OrderByBeanDefault("name", "asc");
 		Bean[] beans =getOperator().getBeans(slectClause, conditions,orderBeans);
 		assertEquals(5, beans.length);
 		
 		getOperator().batchDelete(insertBeans);
	}
//     BeanType[] getPagedBeans(String beanType, String selectClause,
// 			QueryBean queryBean, OrderBean[] orderBeans, int start, int limit);
	public void testPagingGetBeansWithSelectClause(){

		Bean[] insertBeans = getBeans(12);
		getOperator().batchDelete(insertBeans);
		getOperator().batchInsert(insertBeans);
	
		String slectClause="id";
		Conditions conditions=new Conditions();
		conditions.condition("name", "like", "%bean%");
		
		OrderBean[] orderBeans=new OrderBean[1];
		orderBeans[0]=new OrderByBeanDefault("name", "asc");
		
		Bean[] beans = getOperator().getBeans(slectClause, conditions,orderBeans,0,5);
		assertEquals(5, beans.length);
		beans = getOperator().getBeans(slectClause, conditions,orderBeans,6,5);
		assertEquals(5, beans.length);
		beans = getOperator().getBeans(slectClause, conditions,orderBeans,11,5);
		assertEquals(2, beans.length);
		getOperator().batchDelete(insertBeans);
	}
	
	public void testGetSingleObjectWithSelectClause(){
		
		Bean[] insertBeans = getBeans(5);
		getOperator().batchDelete(insertBeans);
		getOperator().batchInsert(insertBeans);
		String selectClause="id,name";
		Conditions conditions=new Conditions();
		conditions.condition("name", "=", "bean5");
		Bean bean =getOperator().getSingleValue(selectClause, conditions);
		assertEquals("bean5", bean.get("name"));
		getOperator().batchDelete(insertBeans);
	}
}
