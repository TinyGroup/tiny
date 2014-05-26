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

public class TestObjectWithVarName extends BastTestCast{
	
	
	public void testRun(){
		Context context = new ContextImpl();
		
		context.put("a.name", "name1");
		context.put("a.age", 10);
		context.put("a.grade", 11);
		context.put("a.tomCat.name", "tomcat");
		context.put("a.tomCat.coller", "red");
		
		context.put("b.name", "name1");
		context.put("b.age", 12);
		context.put("b.grade", 13);
		context.put("b.tomCat.name", "tomcat2");
		context.put("b.tomCat.coller", "red2");
		
		People people = (People) generator.getObject("a",null,People.class.getName(), context);
		assertEquals(people.getAge(), 10);
		assertEquals(people.getGrade(), 11);
		assertEquals(people.getTomCat().getName(), "tomcat");
		assertEquals(people.getTomCat().getColler(), "red");
		
		People people2 = (People) generator.getObject("b",null,People.class.getName(), context);
		assertEquals(people2.getAge(), 12);
		assertEquals(people2.getGrade(), 13);
		assertEquals(people2.getTomCat().getName(), "tomcat2");
		assertEquals(people2.getTomCat().getColler(), "red2");
	}
	
}
