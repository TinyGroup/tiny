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
import org.tinygroup.context2object.CatChild;

public class TestObjectExtends extends BastTestCast{
	public void testObjectArray() {
		Context context = new ContextImpl();
		context.put("name", "name1");
		context.put("nickName", "nickName");
		CatChild c = (CatChild) generator.getObject(null,null, CatChild.class.getName(), context);
		assertEquals(c.getNickName(), "nickName");
		System.out.println(c.getNickName());
	}
}
