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
package org.tinygroup.commons.version;

import junit.framework.TestCase;

public class VersionTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testCompareVersion() {
		assertEquals(0, VersionCompareUtil.compareVersion("1.0", "1.0"));
		assertEquals(-1, VersionCompareUtil.compareVersion("1.0", "1.0.1"));
		assertEquals(1, VersionCompareUtil.compareVersion("1.0.1", "1.0"));
		assertEquals(1, VersionCompareUtil.compareVersion("1.0", "1.0-snapshot"));
		assertEquals(-1, VersionCompareUtil.compareVersion("1.0-snapshot", "1.0"));
		assertEquals(1, VersionCompareUtil.compareVersion("2.0", "1.0"));
		assertEquals(1, VersionCompareUtil.compareVersion("2.0", "1.9.1"));
		assertEquals(0, VersionCompareUtil.compareVersion("2.0.0", "2.0.0"));
		assertEquals(0, VersionCompareUtil.compareVersion("1.0", "1.0"));
		try {
			assertEquals(0, VersionCompareUtil.compareVersion("1.2", "1.2-abc"));
			fail();
		} catch (Exception e) {

		}
		try {
			assertEquals(0, VersionCompareUtil.compareVersion("1.2-aaa", "1.2"));
			fail();
		} catch (Exception e) {

		}
	}

}
