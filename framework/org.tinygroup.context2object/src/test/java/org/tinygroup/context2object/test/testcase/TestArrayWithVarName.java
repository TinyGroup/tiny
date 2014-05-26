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
import org.tinygroup.context2object.test.bean.CatInterface;
import org.tinygroup.context2object.test.bean.SmallCat;

public class TestArrayWithVarName extends BastTestCast{
	

	public void testObjectArray() {
		Context context = new ContextImpl();
		String[] names = { "tomcat", "tomcat1", "tomcat2" };
		String[] colors = { "red", "red1", "red2" };
		context.put("a.name", names);
		context.put("a.coller", colors);
		
		String[] names2 = { "tomcat2", "tomcat21", "tomcat22" };
		String[] colors2 = { "red2", "red21", "red22" };
		context.put("b.name", names2);
		context.put("b.coller", colors2);
		
		SmallCat[] parts = (SmallCat[]) generator.getObject("a",null, SmallCat[].class.getName(), context);
		assertEquals(3, parts.length);
		assertEquals(true, parts[0].getName().equals("tomcat"));
		assertEquals(true, parts[1].getName().equals("tomcat1"));
		assertEquals(true, parts[2].getName().equals("tomcat2"));
		assertEquals(true, parts[0].getColler().equals("red"));
		assertEquals(true, parts[1].getColler().equals("red1"));
		assertEquals(true, parts[2].getColler().equals("red2"));
		
		
		SmallCat[] parts2 = (SmallCat[]) generator.getObject("b",null, SmallCat[].class.getName(), context);
		assertEquals(3, parts2.length);
		assertEquals(true, parts2[0].getName().equals("tomcat2"));
		assertEquals(true, parts2[1].getName().equals("tomcat21"));
		assertEquals(true, parts2[2].getName().equals("tomcat22"));
		assertEquals(true, parts2[0].getColler().equals("red2"));
		assertEquals(true, parts2[1].getColler().equals("red21"));
		assertEquals(true, parts2[2].getColler().equals("red22"));
	}

	public void testInterfaceArray() {
		Context context = new ContextImpl();
		String[] names = { "tomcat", "name1", "name2" };
		String[] colors = { "red", "coller", "coller2" };
		context.put("a.name", names);
		context.put("a.coller", colors);
		CatInterface[] parts = (CatInterface[]) generator.getObject("a", null,CatInterface[].class.getName(), context);
		assertEquals(3, parts.length);
		assertEquals(true, parts[0].getName().equals("tomcat"));
		assertEquals(true, parts[1].getName().equals("name1"));
		assertEquals(true, parts[2].getName().equals("name2"));
	}
	
	public void testStringArray() {
		Context context = new ContextImpl();
		context.put("a", new String[]{"tomcat","name"});
		String[] s = (String[]) generator.getObject("a",null, String[].class.getName(), context);
		assertEquals(2, s.length);
		assertEquals("tomcat", s[0]);
		assertEquals("name", s[1]);
	}
	
}
