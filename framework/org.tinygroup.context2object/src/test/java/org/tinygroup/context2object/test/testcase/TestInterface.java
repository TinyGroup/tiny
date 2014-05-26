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
import org.tinygroup.context2object.test.bean.PartMent2;

public class TestInterface extends BastTestCast {
	

	public void testPropertyInterfaceList() {
		Context context = new ContextImpl();
		context.put("partMent2.name", "name1");
		context.put("partMent2.num", 11);
		String[] names = { "tomcat", "name1", "name2" };
		String[] colors = { "red", "coller", "coller2" };
		context.put("partMent2.cat.name", "tomcat");
		context.put("partMent2.cat.coller", "red");
		context.put("partMent2.cats.name", names);
		context.put("partMent2.cats.coller", colors);
		context.put("partMent2.catsArray.name", names);
		context.put("partMent2.catsArray.coller", colors);
		PartMent2 part = (PartMent2) generator.getObject(null,null,PartMent2.class.getName(), context);
		assertEquals(3, part.getCats().size());
		assertEquals("tomcat", part.getCats().get(0).getName());
		assertEquals("name1", part.getCats().get(1).getName());
		assertEquals("name2", part.getCats().get(2).getName());
		assertEquals(3, part.getCatsArray().length);
	}

	
}
