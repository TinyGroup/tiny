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

public class TestListObject extends BastTestCast {

	public void testRun() {
		Context context = new ContextImpl();
		context.put("partMent.name", "name1");
		context.put("partMent.num", 11);
		String[] names = { "tomcat", "name1", "name2" };
		String[] colors = { "red", "coller", "coller2" };
		context.put("partMent.cats.name", names);
		context.put("partMent.cats.coller", colors);
		context.put("partMent.catsArray.name", names);
		context.put("partMent.catsArray.coller", colors);
		PartMent part = (PartMent) generator.getObject(null,null,PartMent.class.getName(), context);
		assertEquals(true, part.getNum() == 11);
		assertEquals(3, part.getCats().size());
		assertEquals(true, part.getCats().get(0).getName().equals("tomcat"));
		assertEquals(true, part.getCats().get(1).getName().equals("name1"));
		assertEquals(true, part.getCats().get(2).getName().equals("name2"));
		assertEquals(true, part.getCats().get(0).getColler().equals("red"));
		assertEquals(true, part.getCats().get(1).getColler().equals("coller"));
		assertEquals(true, part.getCats().get(2).getColler().equals("coller2"));
		assertEquals(3, part.getCatsArray().length);
	}
}
