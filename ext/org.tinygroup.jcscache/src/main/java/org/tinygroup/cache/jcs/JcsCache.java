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
package org.tinygroup.cache.jcs;

import java.io.Serializable;
import java.util.Set;

import org.apache.jcs.JCS;
import org.apache.jcs.engine.control.CompositeCache;
import org.tinygroup.cache.Cache;
import org.tinygroup.cache.exception.CacheException;

public class JcsCache implements Cache {
	private JCS jcs;

	public JcsCache() {
	}

	private void checkSerializable(Object object) {
		if (!(object instanceof Serializable)) {
			throw new RuntimeException("对象必须实现Serializable接口");
		}
	}

	public void setJcs(JCS jcs) {
		this.jcs = jcs;
	}

	public Object get(String key) {
		Object object = jcs.get(key);
		if (object == null) {
			throw new CacheException(String.format("key <%s> not found.", key));
		}
		return object;
	}

	public void put(String key, Object object) {
		checkSerializable(object);
		try {
			jcs.put(key, object);
		} catch (Exception e) {
			throw new CacheException(e);
		}
	}

	public void putSafe(String key, Object object) {
		checkSerializable(object);
		try {
			jcs.putSafe(key, object);
		} catch (Exception e) {
			throw new CacheException(e);
		}
	}

	public void put(String groupName, String key, Object object)
			{
		checkSerializable(object);
		try {
			jcs.putInGroup(key, groupName, object);
		} catch (Exception e) {
			throw new CacheException(e);
		}
	}

	public Object get(String groupName, String key) {
		try {
			Object object = jcs.getFromGroup(key, groupName);
			if (object == null) {
				throw new CacheException(String.format("key <%s> not found.",
						key));
			}
			return object;
		} catch (Exception e) {
			throw new CacheException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public Set<String> getGroupKeys(String group) {
		return jcs.getGroupKeys(group);
	}

	public void cleanGroup(String group) {
		jcs.invalidateGroup(group);
	}

	public void clear() {
		try {
			jcs.clear();
		} catch (Exception e) {
			throw new CacheException(e);
		}
	}

	public void remove(String key) {
		try {
			jcs.remove(key);
		} catch (Exception e) {
			throw new CacheException(e);
		}

	}

	public void remove(String group, String key) {
		try {
			jcs.remove(key, group);
		} catch (Exception e) {
			throw new CacheException(e);
		}

	}

	public String getStats() {
		return jcs.getStats();
	}

	public int freeMemoryElements(int numberToFree) {
		try {
			return jcs.freeMemoryElements(numberToFree);
		} catch (org.apache.jcs.access.exception.CacheException e) {
			throw new CacheException(e);
		}
	}

	public void init(String region) {
		try {
			jcs= JCS.getInstance(region);
		} catch (Exception e) {
			throw new CacheException(e);
		}
	}

	public void destory() {
		jcs.dispose();
		CompositeCache.elementEventQ.destroy();
	}

}
