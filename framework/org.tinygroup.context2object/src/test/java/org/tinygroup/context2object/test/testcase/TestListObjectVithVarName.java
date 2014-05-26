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
import org.tinygroup.context2object.test.bean.PartMent;

public class TestListObjectVithVarName extends BastTestCast {

	public void testRun() {
		Context context = new ContextImpl();
		
		context.put("a.name", "name1");
		context.put("a.num", 11);
		String[] names = { "tomcat", "name11", "name12" };
		String[] colors = { "red", "red11", "red12" };
		context.put("a.cats.name", names);
		context.put("a.cats.coller", colors);
		context.put("a.catsArray.name", names);
		context.put("a.catsArray.coller", colors);
		
		context.put("b.name", "name2");
		context.put("b.num", 12);
		String[] names2 = { "tomcat2", "name21", "name22" };
		String[] colors2 = { "red2", "red21", "red22" };
		context.put("b.cats.name", names2);
		context.put("b.cats.coller", colors2);
		context.put("b.catsArray.name", names2);
		context.put("b.catsArray.coller", colors2);
		
		PartMent part = (PartMent) generator.getObject("a",null,PartMent.class.getName(), context);
		assertEquals(true, part.getNum() == 11);
		assertEquals(3, part.getCats().size());
		assertEquals(true, part.getCats().get(0).getName().equals("tomcat"));
		assertEquals(true, part.getCats().get(1).getName().equals("name11"));
		assertEquals(true, part.getCats().get(2).getName().equals("name12"));
		assertEquals(true, part.getCats().get(0).getColler().equals("red"));
		assertEquals(true, part.getCats().get(1).getColler().equals("red11"));
		assertEquals(true, part.getCats().get(2).getColler().equals("red12"));
		assertEquals(3, part.getCatsArray().length);
		
		PartMent part2 = (PartMent) generator.getObject("b",null,PartMent.class.getName(), context);
		assertEquals(true, part2.getNum() == 12);
		assertEquals(3, part2.getCats().size());
		assertEquals(true, part2.getCats().get(0).getName().equals("tomcat2"));
		assertEquals(true, part2.getCats().get(1).getName().equals("name21"));
		assertEquals(true, part2.getCats().get(2).getName().equals("name22"));
		assertEquals(true, part2.getCats().get(0).getColler().equals("red2"));
		assertEquals(true, part2.getCats().get(1).getColler().equals("red21"));
		assertEquals(true, part2.getCats().get(2).getColler().equals("red22"));
		assertEquals(3, part.getCatsArray().length);
	}
}
