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
package org.tinygroup.cepcore.test;

import org.tinygroup.cache.Cache;
import org.tinygroup.cache.jcs.JcsCache;
import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;

public class CacheTest {
	public static void main(String[] args) {
		Cache cache = new JcsCache();
		cache.init("DC");
		Context context = new ContextImpl();
		long start = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			for (int j = 0; j < 100; j++) {
				context.put("abcdef" + j, "value" + j);
			}

			cache.put("a", context);
		}
		long end = System.currentTimeMillis();

		System.out.println(end - start);
	}
}
