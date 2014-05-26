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

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.test.BaseTest;


public class TestBatchOperator extends BaseTest {

	private String[] getBeanIds(Bean[] beans){
		String[] ids = new String[2];
		ids[0]=String.valueOf(beans[0].getProperty("id"));
		ids[1]=String.valueOf(beans[1].getProperty("id"));
		return ids;
	}
	
	private List<String> getBeanIdList(Bean[] beans){
		List<String> ids = new ArrayList<String>();
		ids.add(String.valueOf(beans[0].getProperty("id")));
		ids.add(String.valueOf(beans[1].getProperty("id")));
		return ids;
	}
	
	private Bean[] getBeans(){
		Bean bean = new Bean(ANIMAL);
		bean.setProperty("id","beanId");
		bean.setProperty("name","1234");
		bean.setProperty("length","1234");
		
		Bean bean2 = new Bean(ANIMAL);
		bean2.setProperty("id","beanId2");
		bean2.setProperty("name","12345");
		bean2.setProperty("length","12345");
		Bean[] beans = new Bean[2];
		beans[0]=bean;
		beans[1]=bean2;
		return beans;
	}
	
	private List<Bean> getBeanList(){
		Bean bean = new Bean(ANIMAL);
		bean.setProperty("id","beanId");
		bean.setProperty("name","1234");
		bean.setProperty("length","1234");
		
		Bean bean2 = new Bean(ANIMAL);
		bean2.setProperty("id","beanId2");
		bean2.setProperty("name","12345");
		bean2.setProperty("length","12345");
		
		List<Bean> list = new ArrayList<Bean>();
		list.add(bean2);
		list.add(bean);
		return list;
		
	}
	
	public void testArrayDelete(){
		Bean[] beans = getBeans();
		getOperator().batchDelete(beans);
		assertEquals(2, getOperator().batchInsert(beans).length);
		assertEquals(2,getOperator().batchDelete(beans).length);
	}
	
	public void testArrayUpdate(){
		Bean[] beans = getBeans();
		getOperator().batchInsert(beans);
		beans[0].setProperty("name","123456");
		beans[1].setProperty("name","123456");
		assertEquals(2,getOperator().batchUpdate(beans).length);
		assertEquals(2,getOperator().batchDelete(beans).length);
		
	}
	
	public void testArrayQuery(){
		Bean[] beans = getBeans();
		beans=getOperator().batchInsert(beans);
		String[] ids = getBeanIds(beans);
		Bean[] beans2 =getOperator().getBeansById(ids);
		assertEquals(2, beans2.length);
		getOperator().batchDelete(beans);
	}
	
	public void testArrayDeleteById(){
		Bean[] beans = getBeans();
		beans=getOperator().batchInsert(beans);
		String[] ids = getBeanIds(beans);
		assertEquals(2,getOperator().deleteById(ids).length);
		
	}
	
	public void testCollectionDelete(){
		List<Bean> beans = getBeanList();
		assertEquals(2,getOperator().batchInsert(beans).length);
		assertEquals(2,getOperator().batchDelete(beans).length);
	}
	
	public void testCollectionUpdate(){
		List<Bean> beans = getBeanList();
		getOperator().batchInsert(beans);
		beans.get(0).setProperty("name","123456");
		beans.get(1).setProperty("name","123456");
		assertEquals(2,getOperator().batchUpdate(beans).length);
		getOperator().batchDelete(beans);
		
	}
	
	public void testCollectionQuery(){
		List<Bean> beans = getBeanList();
		Bean[] insertBeans=getOperator().batchInsert(beans);
		List<String> ids = getBeanIdList(insertBeans);
		Bean[] beans2 =getOperator().getBeansById(ids);
		assertEquals(2, beans2.length);
		getOperator().batchDelete(beans);
	}
	
	public void testCollectionDeleteById(){
		List<Bean> beans = getBeanList();
		Bean[] insertBeans=getOperator().batchInsert(beans);
		List<String> ids = getBeanIdList(insertBeans);
		assertEquals(2,getOperator().deleteById(ids).length);
	}
	
	public void testInsertDiffType(){
		List<Bean> beans = getBeanDiffList();
		try {
			getOperator().batchInsert(beans);
			getOperator().batchDelete(beans);
		} catch (Exception e) {
			assertTrue(true);
		}
		assertEquals(2, getOperator().insertBean(beans.toArray(new Bean[0])).length);
		assertEquals(2, getOperator().deleteBean(beans.toArray(new Bean[0])).length);
		
	
	}

	private List<Bean> getBeanDiffList() {
		Bean bean = new Bean(ANIMAL);
		bean.setProperty("id","beanId");
		bean.setProperty("name","1234");
		bean.setProperty("length","1234");
		
		Bean bean2 = new Bean(BRANCH);
		bean2.setProperty("branchId","branch1");
		bean2.setProperty("branchName","branchNmae");
		
		List<Bean> list = new ArrayList<Bean>();
		list.add(bean);
		list.add(bean2);
		return list;
	}
}
