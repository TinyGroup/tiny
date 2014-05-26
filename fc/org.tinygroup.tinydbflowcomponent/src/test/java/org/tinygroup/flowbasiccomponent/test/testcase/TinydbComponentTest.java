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
package org.tinygroup.flowbasiccomponent.test.testcase;

import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.tinydb.Bean;


public class TinydbComponentTest extends BaseTest {


    public void setUp() {
        super.setUp();
        getOperator().execute("delete from animal");//删除所有记录
        //		for (int i = 0; i < 3; i++) {
        //			Bean bean = new Bean(ANIMAL);
        //			bean.setProperty("id",i+"");
        //			bean.setProperty("name","name"+i);
        //			bean.setProperty("length","length"+i);
        //			getOperator().insert(bean);
        //		}
    }


    private Bean getBean() {
        Bean bean = new Bean(ANIMAL);
        bean.setProperty("id", "aaaaaa");
        bean.setProperty("name", "name123");
        bean.setProperty("length", "123");
        return bean;
    }


    public void testAdd() {
        Context context = new ContextImpl();
        context.put("beanType", ANIMAL);
        context.put("id", "aaaaaa");
        context.put("name", "name123");
        context.put("length", "123");
        flowExecutor.execute("addService", context);
        Bean result = context.get("result");
        assertEquals("aaaaaa", result.get("id"));
    }

	public void testUpdate() {
		Bean bean=getBean();
		getOperator().insert(bean);
		Context context=new ContextImpl();
		context.put("beanType", ANIMAL);
		context.put("name", "updateName");
		context.put("id", "aaaaaa");
		flowExecutor.execute("updateService", context);
        Integer record=context.get("result");
        assertEquals(1, record.intValue());
	}

	public void testDelete() {
		Bean bean=getBean();
		getOperator().insert(bean);
		Context context=new ContextImpl();
		context.put("beanType", ANIMAL);
		context.put("id", "aaaaaa");
		flowExecutor.execute("deleteService", context);
		Integer record=context.get("result");
        assertEquals(1, record.intValue());
	}

    public void testQuery() {
        for (int i = 0; i < 3; i++) {
            Bean bean = new Bean(ANIMAL);
            bean.setProperty("id", i + "");
            bean.setProperty("name", "name");
            bean.setProperty("length", 100 + i);
            getOperator().insert(bean);
        }
        Context context = new ContextImpl();
        context.put("beanType", ANIMAL);
        context.put("name", "name");
        flowExecutor.execute("queryService", context);
        Bean[] beans = context.get("result");
        assertEquals(3, beans.length);
    }

    public void testQueryWithId() {
        Bean bean = getBean();
        getOperator().insert(bean);
        Context context = new ContextImpl();
        context.put("beanType", ANIMAL);
        context.put("primaryKey", "aaaaaa");
        flowExecutor.execute("queryWithIdService", context);
        Bean result = context.get("result");
        assertEquals(123, result.get("length"));
    }

}
