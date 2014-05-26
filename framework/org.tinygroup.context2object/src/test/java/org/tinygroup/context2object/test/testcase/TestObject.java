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
package org.tinygroup.context2object.test.testcase;

import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.context2object.test.bean.People;

public class TestObject extends BastTestCast{
	
	public void testRun(){
		Context context = new ContextImpl();
		context.put("people.name", "name1");
		context.put("people.age", 11);
		context.put("people.grade", 15);
		context.put("people.grade.cc", "c");
		context.put("people.bb","c".getBytes()[0]);
		context.put("people.dd", 2.2);
		context.put("people.ff", 3.3);
		context.put("people.ll", 111);
		context.put("people.ss", 222);
		context.put("people.bo", true);
		
		context.put("people.tomCat.name", "tomcat");
		context.put("people.tomCat.coller", "red");
		
		People people = (People) generator.getObject(null,null,People.class.getName(), context);
		assertEquals(people.getAge(), 11);
		assertEquals(true, people.isBo());
		assertEquals("c".getBytes()[0], people.getBb());
		assertEquals(2.2, people.getDd());
		assertEquals(3.3f, people.getFf());
		assertEquals(111, people.getLl());
		assertEquals(222, people.getSs());
		assertEquals(people.getTomCat().getName(), "tomcat");
	}
	public void testRunWithVarName(){
		Context context = new ContextImpl();
		context.put("a.name", "name1");
		context.put("a.age", 11);
		context.put("a.grade", 15);
		context.put("a.tomCat.name", "tomcat");
		context.put("a.tomCat.coller", "red");
		People people = (People) generator.getObject("a",null,People.class.getName(), context);
		assertEquals(people.getAge(), 11);
		assertEquals(people.getTomCat().getName(), "tomcat");
	}
	
	public void testRun1(){
		Context context = new ContextImpl();
		context.put("people_name", "name1");
		context.put("people_age", 11);
		context.put("people_grade", 15);
		context.put("people_tomCat_name", "tomcat");
		context.put("people_tomCat_coller", "red");
		People people = (People) generator.getObject(null,null,People.class.getName(), context);
		assertEquals(people.getAge(), 11);
		assertEquals(people.getTomCat().getName(), "tomcat");
	}
	
	public void testRun1WithVarName(){
		Context context = new ContextImpl();
		context.put("a_name", "name1");
		context.put("a_age", 11);
		context.put("a_grade", 15);
		context.put("a_tomCat_name", "tomcat");
		context.put("a_tomCat_coller", "red");
		People people = (People) generator.getObject("a",null,People.class.getName(), context);
		assertEquals(people.getAge(), 11);
		assertEquals(people.getTomCat().getName(), "tomcat");
	}
	
	public void testRun2(){
		Context context = new ContextImpl();
		context.put("people_name", "name1");
		context.put("people_age", 11);
		context.put("people_grade", 15);
		context.put("people.tomCat.name", "tomcat");
		context.put("people.tomCat.coller", "red");
		People people = (People) generator.getObject(null,null,People.class.getName(), context);
		assertEquals(people.getAge(), 11);
		assertEquals(people.getTomCat().getName(), "tomcat");
	}
	
	public void testRun2WithVarName(){
		Context context = new ContextImpl();
		context.put("a_name", "name1");
		context.put("a_age", 11);
		context.put("a_grade", 15);
		context.put("a.tomCat.name", "tomcat");
		context.put("a.tomCat.coller", "red");
		People people = (People) generator.getObject("a",null,People.class.getName(), context);
		assertEquals(people.getAge(), 11);
		assertEquals(people.getTomCat().getName(), "tomcat");
	}
}
