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
package org.tinygroup.flow.context2object;

import java.util.List;

import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.flow.bean.FlowUser;
import org.tinygroup.flow.component.AbstractFlowComponent;

public class TestContext2Object extends AbstractFlowComponent {
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testParamsByContext2Object() {
		Context c = new ContextImpl();
		c.put("a.name", "namea");
		c.put("a.age", 1);

		c.put("b.name", new String[] { "nameb1", "nameb2" });
		c.put("b.age", new String[] { "2", "3" });

		c.put("c.name", new String[] { "namec1", "namec2" });
		c.put("c.age", new String[] { "4", "5" });

		flowExecutor.execute("context2objectTest", c);

		FlowUser a = c.get("a");
		assertEquals("namea", a.getName());
		assertEquals(1      , a.getAge());

		FlowUser[] b = c.get("b");
		assertEquals(2       , b.length);
		assertEquals("nameb1", b[0].getName());
		assertEquals(2       , b[0].getAge());
		assertEquals("nameb2", b[1].getName());
		assertEquals(3       , b[1].getAge());

		List<FlowUser> l = c.get("c");
		assertEquals(2       , l.size());
		assertEquals("namec1", l.get(0).getName());
		assertEquals(4       , l.get(0).getAge());
		assertEquals("namec2", l.get(1).getName());
		assertEquals(5       , l.get(1).getAge());

	}

}
