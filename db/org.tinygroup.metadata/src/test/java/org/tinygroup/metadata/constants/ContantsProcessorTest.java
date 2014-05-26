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
package org.tinygroup.metadata.constants;

import junit.framework.TestCase;

import org.tinygroup.metadata.TestInit;
import org.tinygroup.metadata.util.MetadataUtil;
import org.tinygroup.springutil.SpringUtil;

public class ContantsProcessorTest extends TestCase {
	ConstantProcessor contantsProcessor;
	static {
		TestInit.init();
	}

	protected void setUp() throws Exception {
		super.setUp();
		contantsProcessor = SpringUtil.getBean(MetadataUtil.CONSTANTPROCESSOR_BEAN);
	}

	public void testGetIntValueStringString() {
		assertEquals(122, contantsProcessor.getIntValue("a5"));
	}

	public void testGetBooleanValueStcontantsProcessorringString() {
		boolean actual = contantsProcessor.getBooleanValue("a8");
		assertEquals(true, actual);
	}

	public void testGetDoubleValueStringString() {
		assertEquals(112.1d, contantsProcessor.getDoubleValue("a2"));
	}

	public void testGetFloatValueStringString() {
		assertEquals(1.02f, contantsProcessor.getFloatValue("a1"));
	}

	public void testGetCharValueStringString() {
		assertEquals('中', contantsProcessor.getCharValue("a7"));
	}

	public void testGetShortValueStringString() {
		assertEquals(12, contantsProcessor.getShortValue("a4"));
	}

	public void testGetbyteValueStringString() {
		assertEquals(65, contantsProcessor.getByteValue("a3"));
	}

	public void testGetLongValueStringString() {
		assertEquals(11231, contantsProcessor.getLongValue("a6"));
	}

	public void testGetStringValueStringString() {
		assertEquals("true", contantsProcessor.getStringValue("a9"));
	}

}
