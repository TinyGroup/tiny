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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.context2object.test.bean.CatInterface;
import org.tinygroup.context2object.test.bean.SmallCat;

public class TestListWithVarName extends BastTestCast{

	public void testRun() {
		Context context = new ContextImpl();
		String[] names = { "tomcat", "name1", "name2" };
		String[] colors = { "red", "coller", "coller2" };
		context.put("a.name", names);
		context.put("a.coller", colors);
		Collection<Object> parts = generator.getObjectCollection("a",List.class.getName(), SmallCat.class.getName(), context);
		assertEquals(3, parts.size());
		Iterator<Object> iterator = parts.iterator();
		String catNames = "";
		String catColors = "";
		while(iterator.hasNext()){
			SmallCat cat = (SmallCat) iterator.next();
			catNames = catNames+cat.getName();
			catColors = catColors+cat.getColler();
		}
		System.out.println(catNames);
		System.out.println(catColors);
		assertEquals(true, catNames.contains("tomcat")&&catNames.contains("name1")&&catNames.contains("name2"));
		assertEquals(true, catColors.contains("red")&&catColors.contains("coller")&&catColors.contains("coller2"));
	}

	public void testRun1() {
		Context context = new ContextImpl();
		String[] names = { "tomcat", "name1", "name2" };
		String[] colors = { "red", "coller", "coller2" };
		context.put("a.name", names);
		context.put("a.coller", colors);
		Collection<Object> parts = generator.getObjectCollection("a",List.class.getName(), CatInterface.class.getName(), context);
		assertEquals(3, parts.size());
		Iterator<Object> iterator = parts.iterator();
		String catNames = "";
		while(iterator.hasNext()){
			CatInterface cat = (CatInterface) iterator.next();
			catNames = catNames+cat.getName();
					}
		System.out.println(catNames);
		assertEquals(true, catNames.contains("tomcat")&&catNames.contains("name1")&&catNames.contains("name2"));
	}

	
}
