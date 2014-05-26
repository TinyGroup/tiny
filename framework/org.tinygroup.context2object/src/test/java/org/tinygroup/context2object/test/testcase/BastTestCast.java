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

import junit.framework.TestCase;

import org.tinygroup.context2object.impl.ClassNameObjectGenerator;
import org.tinygroup.context2object.impl.DateTypeConverter;
import org.tinygroup.context2object.test.bean.PartInterface;
import org.tinygroup.context2object.test.bean.PartMent;
import org.tinygroup.tinytestutil.AbstractTestUtil;

public class BastTestCast extends TestCase{
	protected ClassNameObjectGenerator generator = new ClassNameObjectGenerator();
	protected ClassNameObjectGenerator newgenerator = new ClassNameObjectGenerator();

	protected void setUp() {
		generator.addTypeCreator(new ListCreator());
		generator.addTypeConverter(new DateTypeConverter());
		AbstractTestUtil.init(null, true);
	}
	public void testOther() {
		assertEquals(true, PartMent.class.isAssignableFrom(PartMent.class));
		assertEquals(true, Object.class.isAssignableFrom(PartMent.class));
		assertEquals(true, PartInterface.class.isAssignableFrom(PartMent.class));
	}
}
