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
package org.tinygroup.cache;

import junit.framework.TestCase;

import org.tinygroup.cache.exception.CacheException;
import org.tinygroup.fileresolver.FileResolverFactory;
import org.tinygroup.fileresolver.impl.I18nFileProcessor;
import org.tinygroup.fileresolver.impl.SpringBeansFileProcessor;
import org.tinygroup.fileresolver.impl.XStreamFileProcessor;
import org.tinygroup.springutil.SpringUtil;

public class CacheTest extends TestCase {
	Cache cache;
	static {
		FileResolverFactory.getFileResolver().addFileProcessor(
				new XStreamFileProcessor());
		FileResolverFactory.getFileResolver().addFileProcessor(
				new I18nFileProcessor());
		FileResolverFactory.getFileResolver().addFileProcessor(
				new SpringBeansFileProcessor());
		FileResolverFactory.getFileResolver().resolve();
	}

	protected void setUp() throws Exception {
		super.setUp();
		cache = SpringUtil.getBean("jcsCache");
		cache.init("testCache1");
		//cache.clear();
	}
	
	

	
	protected void tearDown() throws Exception {
		super.tearDown();
//		cache.destory();
	}



	public void testGetString() throws CacheException {
		cache.put("aa", "123");
		assertEquals("123", cache.get("aa"));
	}

	public void testPutSafe() {
		try {
			cache.putSafe("aa", 123);
			cache.putSafe("aa", "bb");
			fail();
		} catch (CacheException e) {
		}
	}

	public void testPutStringStringObject() throws CacheException {
		cache.put("group", "aa", "123");
		assertEquals("123", cache.get("group", "aa"));
	}

	public void testGetGroupKeys() throws CacheException {
		cache.put("groupa", "aa1", "123");
		cache.put("groupa", "aa2", "123");
		cache.put("groupa", "aa3", "123");
		assertEquals(3, cache.getGroupKeys("groupa").size());
	}

	public void testCleanGroup() throws CacheException {
		cache.put("bb", "123");
		cache.put("group", "aa1", "123");
		cache.put("group", "aa2", "123");
		cache.put("group", "aa3", "123");
		cache.cleanGroup("group");
		try {
			cache.get("group", "aa1");
			fail();
		} catch (Exception e) {

		}
	}

	public void testClear() throws CacheException {
		cache.put("bb", "123");
		assertEquals("123", cache.get("bb"));
		cache.clear();
		try {
			cache.get("bb");
			fail();
		} catch (Exception e) {

		}

	}

	public void testRemoveStringString() throws CacheException {
		cache.put("group", "bb", "123");
		assertEquals("123", cache.get("group", "bb"));
		cache.remove("group", "bb");
		try {
			cache.get("group", "bb");
			fail();
		} catch (Exception e) {

		}
	}

	public void testRemove() throws CacheException {
		cache.put("bb", "123");
		assertEquals("123", cache.get("bb"));
		cache.remove("bb");
		try {
			cache.get("bb");
			fail();
		} catch (Exception e) {

		}
	}

	public void testGetStats() {
		System.out.println(System.getProperty("user.dir"));
		System.out.println(cache.getStats());
	}

	public void testFreeMemoryElements() throws CacheException {
		cache.put("aa", "aa");
		for (int i = 0; i < 100; i++) {
			cache.put("aa" + i, i);
		}
		for (int j = 0; j < 500; j++) {
			cache.get("aa");
		}
		try {
			cache.freeMemoryElements(100);
			assertEquals("aa", cache.get("aa"));
			cache.get("aa1");
//			/fail();
		} catch (Exception e) {

		}

	}

}
