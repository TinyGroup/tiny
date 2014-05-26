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

import org.tinygroup.weblayer.WebContext;

/**
 * 
 * 功能说明:处理缓存页面过滤的上下文 

 * 开发人员: renhui <br>
 * 开发时间: 2013-8-22 <br>
 * <br>
 */
public interface PageCacheWebContext extends WebContext {
   /**
    * 
    * 判断访问路径是否已经被缓存过
    * @param accessPath
    * @return
    */
	 boolean isCached(String accessPath);
	
	/**
	 * 
	 * 对访问页面进行缓存
	 * @param accessPath
	 */
	void cacheOutputPage(String accessPath);

    /**
     * 
     * 设置缓存映射操作对象
     * @param operater
     */
	void setCacheOperater(CacheOperater operater);
	/**
	 * 
	 * 缓存页面内容
	 * @param accessPath
	 */
	void putCachePage(String accessPath);
	
	/**
	 * 
	 * 访问路径是否需要进行缓存
	 * @param accessPath
	 * @return
	 */
	boolean isCachePath(String accessPath);
	
}
