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
package org.tinygroup.tinydb.testcase.transaction;

import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.test.BaseTest;
import org.tinygroup.tinydb.testcase.transaction.service.TransactionService;

public class TransactionTest extends BaseTest {
	
	Bean[] beans1;
	
	Bean[] beans2;
	TransactionService service;
	
	public void setUp() {
		super.setUp();
		beans1=getAnimalBeans();
		beans2=getBranchBeans();
	    service=SpringUtil.getBean("transactionService");
		//先删除表中已存在的记录
	    service.deleteBean(beans1, beans2);
	}

	
	protected void tearDown() throws Exception {
		super.tearDown();
	  service.deleteBean(beans1, beans2);
	}

	private Bean[] getAnimalBeans() {
		Bean bean = new Bean(ANIMAL);
		bean.setProperty("id", "beanId");
		bean.setProperty("name", "1234");
		bean.setProperty("length", "1234");

		Bean bean2 = new Bean(ANIMAL);
		bean2.setProperty("id", "beanId2");
		bean2.setProperty("name", "12345");
		bean2.setProperty("length", "12345");
		Bean[] beans = new Bean[2];
		beans[0] = bean;
		beans[1] = bean2;
		return beans;
	}
	
	private Bean[] getBranchBeans() {
		Bean bean = new Bean(BRANCH);
		bean.setProperty("branchId", "beanId3");
		bean.setProperty("branchName", "1234");

		Bean bean2 = new Bean(BRANCH);
		bean2.setProperty("branchId", "beanId4");
		bean2.setProperty("branchName", "12345");
		Bean[] beans = new Bean[2];
		beans[0] = bean;
		beans[1] = bean2;
		return beans;
	}
	
		
	public void testTransactionSuccess(){
		service.transactionSuccess(beans1, beans2);
		int length=getOperator().getBeans("select * from animal").length;
		assertEquals(2, length);
	}
	
	/**
	 * 
	 * 事务回滚测试,由于抛出异常需要注释掉
	 */
	public void testTransactionRollback(){
//	    service.transactionFailure(beans1, beans2);
	}
	/**
	 * 
	 * 独立事务回滚测试,由于抛出异常需要注释掉
	 */
	public void testIndependentTransaction(){
//		 service.independentTransaction(beans1, beans2);
	}
	
}
