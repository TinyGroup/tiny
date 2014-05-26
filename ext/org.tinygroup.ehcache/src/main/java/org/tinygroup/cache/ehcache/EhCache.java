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
package org.tinygroup.cache.ehcache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.tinygroup.cache.Cache;
import org.tinygroup.cache.exception.CacheException;

/**
 * 
 * 分组的支持
 * 
 */
public class EhCache implements Cache {
	private static final String GROUP_MAP = "group_map";
	private CacheManager manager = new CacheManager(
			EhCache.class.getResourceAsStream("/ehcache.xml"));
	private net.sf.ehcache.Cache cache;

	public EhCache() {
	}

	private String getKey(String group, String key) {
		return String.format("%s:%s", group, key);
	}

	public void setCache(net.sf.ehcache.Cache cache) {
		this.cache = cache;
	}

	public void init(String region) {
		cache = manager.getCache(region);
	}

	public Object get(String key) {
		Element element = cache.get(key);
		if (element == null) {
			throw new CacheException(String.format("key:<%s> not found.", key));
		}
		return element.getObjectValue();
	}

	public void put(String key, Object object) {
		Element element = new Element(key, object);
		cache.put(element);
	}

	public void putSafe(String key, Object object) {
		if (cache.get(key) != null) {
			throw new CacheException("");
		}
		Element element = new Element(key, object);
		cache.put(element);
	}

	public void put(String groupName, String key, Object object) {
		Element element = cache.get(GROUP_MAP);
		Map<String, Set<String>> groupMap = null;
		if (element == null) {
			groupMap = new HashMap<String, Set<String>>();
		} else {
			groupMap = (Map<String, Set<String>>) element.getObjectValue();
		}
		Set<String> keysSet = groupMap.get(groupName);
		if (keysSet == null) {
			keysSet = new HashSet<String>();
			groupMap.put(groupName, keysSet);
		}
		keysSet.add(key);
		put(getKey(groupName, key), object);
		put(GROUP_MAP, groupMap);
	}

	public Object get(String groupName, String key) {
		return get(getKey(groupName, key));
	}

	public Set<String> getGroupKeys(String group) {
		Map<String, Set<String>> groupMap = (Map<String, Set<String>>) cache
				.get(GROUP_MAP).getObjectValue();
		return groupMap.get(group);
	}

	public void cleanGroup(String group) {
		Set<String> groupKeys = getGroupKeys(group);
		if (groupKeys != null) {
			for (String key : groupKeys) {
				remove(group, key);
			}
		}
	}

	public void clear() {
		cache.removeAll();
	}

	public void remove(String key) {
		cache.remove(key);
	}

	public void remove(String group, String key) {
		Set<String> groupKeys = getGroupKeys(group);
		if (groupKeys == null) {
			throw new CacheException(String.format("group <%s> not found.",
					group));
		}
		cache.remove(getKey(group, key));
	}

	public String getStats() {
		return cache.getStatistics().toString();
	}

	public int freeMemoryElements(int numberToFree) {
		throw new CacheException("ehcache does not support this feature.");
	}

	public void destory() {
		cache.dispose();
	}

}
