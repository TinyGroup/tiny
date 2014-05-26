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
package org.tinygroup.weblayer.webcontext.cache;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.cache.Cache;
import org.tinygroup.weblayer.WebContext;

/**
 * 
 * 功能说明:
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-8-22 <br>
 * <br>
 */
public class CacheOperater {

	public static final String DEFAULT_CACHE_GROUP_NAME = "pageCached";

	private Cache cache;
	/**
	 * 映射对象列表
	 */
	private List<CacheMapping> cacheMappings = new ArrayList<CacheMapping>();

	public CacheOperater(Cache cache) {
		this.cache = cache;
	}

	public void addMapping(String patternStr, String paramNames,
			Long timeToLived) {
		CacheMapping cacheMapping = new CacheMapping(patternStr, paramNames,
				timeToLived);
		cacheMappings.add(cacheMapping);
	}

	/**
	 * 
	 * 把请求路径对应的页面内容放入缓存
	 * 
	 * @param accessPath
	 *            访问路径不带参数信息
	 * @param output
	 *            页面内容
	 * @param webcontext
	 */
	public void putCache(String accessPath, String output, WebContext webcontext) {
		CacheMapping cacheMapping = getCacheMapping(accessPath);
		if (cacheMapping != null) {
			String cacheKey = getCacheKey(accessPath, webcontext);
			Long timeAlive = cacheMapping.getTimeToLived();
			cache.put(DEFAULT_CACHE_GROUP_NAME, cacheKey, new PageCacheObject(
					output, System.currentTimeMillis() + timeAlive * 1000));
		}
	}

	private String getCacheKey(String accessPath, WebContext webcontext) {
		String cacheKey = accessPath;
		CacheMapping cacheMapping = getCacheMapping(accessPath);
		if (cacheMapping != null) {
			cacheKey = cacheMapping.getCacheKey(accessPath, webcontext);
		}
		return cacheKey;
	}

	// private String getPatternPath(String accessPath) {
	// String patternPath=accessPath;
	// int index=accessPath.indexOf("?");
	// if(index!=-1){
	// patternPath=accessPath.substring(0, index);
	// }
	// return patternPath;
	// }

	public boolean isInValid(String cacheKey) {
		PageCacheObject cacheObject = getPageCacheObject(cacheKey);
		if (cacheObject != null) {
			Long time = cacheObject.getTimeToLived();
			if (time != null && time < System.currentTimeMillis()) {
				return true;
			}
		}

		return false;
	}

	private PageCacheObject getPageCacheObject(String cacheKey) {
		try {
			return (PageCacheObject) cache.get(DEFAULT_CACHE_GROUP_NAME,
					cacheKey);
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * 
	 * 获取访问路径的缓存内容; 1、判断请求路径是否可以缓存 2、请求路径对应的缓存是否已经失效 3、获取请求路径对应的缓存内容
	 * 
	 * @param accessPath
	 * @return 缓存内容
	 */
	public String getCacheContent(String accessPath, WebContext webcontext) {
		if (!isCachePath(accessPath)) {
			return null;
		}
		String cacheKey = getCacheKey(accessPath, webcontext);
		if (isInValid(cacheKey)) {
			cache.remove(DEFAULT_CACHE_GROUP_NAME, cacheKey);
			return null;
		}
		PageCacheObject cacheObject = getPageCacheObject(cacheKey);
		if (cacheObject != null) {
			return cacheObject.getContent();
		}
		return null;
	}

	/**
	 * 
	 * 此请求路径是否可以被缓存
	 * 
	 * @param accessPath
	 * @return
	 */
	public boolean isCachePath(String accessPath) {
		return getCacheMapping(accessPath) != null;
	}

	private CacheMapping getCacheMapping(String accessPath) {
		for (CacheMapping cacheMapping : cacheMappings) {
			if (cacheMapping.matches(accessPath)) {
				return cacheMapping;
			}
		}
		return null;
	}

}
