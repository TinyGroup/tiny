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

public class TestSingleOperator extends BaseTest{
	
	private Bean getBean(){
		Bean bean = new Bean(ANIMAL);
		bean.setProperty("id","aaaaaa");
		bean.setProperty("name","123");
		bean.setProperty("length","123");
		return bean;
	}
	
	
	
	public void testUpdate(){
		Bean bean = getBean();
		getOperator().delete(bean);
		getOperator().insert(bean);
		bean.setProperty("name","1235");
		bean.setProperty("length","1235");
		assertEquals(1, getOperator().update(bean));
		getOperator().delete(bean);
	}
	
	public void testUpdateWithCondition(){
		Bean bean = getBean();
		getOperator().delete(bean);
		getOperator().insert(bean);
		bean.setProperty("name","1235");
		bean.setProperty("length","123");
		List<String> conditions=new ArrayList<String>();
		conditions.add("LENGTH");
		assertEquals(1, getOperator().update(bean,conditions));
		getOperator().delete(bean);
	}
	
	
	public void testDelete(){
		Bean bean = getBean();
		getOperator().delete(bean);
		getOperator().insert(bean);
		assertEquals(1, getOperator().delete(bean));
	}
	
	public void testQuery(){
		Bean bean = getBean();
		getOperator().delete(bean);
		getOperator().insert(bean);
		Bean b = getOperator().getBean(String.valueOf(bean.getProperty("id")));
		getOperator().delete(b );
	}
	
}
