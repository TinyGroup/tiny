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
 * 功能说明:缓存匹配器 

 * 开发人员: renhui <br>
 * 开发时间: 2013-8-22 <br>
 * <br>
 */
public interface CacheMatcher {
    /**
     * 
     * 访问路径是否匹配
     * @param accessPath
     * @param webContext
     * @return
     */
	boolean isMatch(String accessPath,WebContext webContext);
	/**
	 * 
	 * 根据访问路径获取存入缓存的key
	 * @param accessPath
	 * @return
	 */
	String getCacheKey(String accessPath);
	
}
