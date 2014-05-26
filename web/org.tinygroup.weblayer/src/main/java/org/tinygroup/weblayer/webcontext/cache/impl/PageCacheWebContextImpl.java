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
package org.tinygroup.weblayer.webcontext.cache.impl;

import java.io.IOException;
import java.io.PrintWriter;

import org.tinygroup.weblayer.WebContext;
import org.tinygroup.weblayer.webcontext.AbstractWebContextWrapper;
import org.tinygroup.weblayer.webcontext.buffered.BufferedWebContext;
import org.tinygroup.weblayer.webcontext.cache.CacheOperater;
import org.tinygroup.weblayer.webcontext.cache.PageCacheWebContext;
import org.tinygroup.weblayer.webcontext.cache.exception.PageCacheOutputException;
import org.tinygroup.weblayer.webcontext.util.WebContextUtil;

/**
 * 
 * 功能说明:缓存页面上下文实现类 

 * 开发人员: renhui <br>
 * 开发时间: 2013-8-22 <br>
 * <br>
 */
public class PageCacheWebContextImpl extends AbstractWebContextWrapper
		implements PageCacheWebContext {
	
	private CacheOperater operater;
	

	public PageCacheWebContextImpl(WebContext wrappedContext) {
		super(wrappedContext);
	}

	/**
	 * 是否在缓存中有，或者是否已经失效
	 */
	public boolean isCached(String accessPath) {
		  String content=operater.getCacheContent(accessPath,this);
		  if(content==null){
			  return false;
		  }
		  return true;
	}

	public void cacheOutputPage(String accessPath) {
		String content=operater.getCacheContent(accessPath,this);
		if(content!=null){
			try {
				PrintWriter writer= getResponse().getWriter();
				writer.write(content);
			} catch (IOException e) {
				throw new PageCacheOutputException(e);
			}
			
		}
	}


	public void setCacheOperater(CacheOperater operater) {
          this.operater=operater;
	}

	public void putCachePage(String accessPath) {
		if(isCachePath(accessPath)){
			BufferedWebContext buffer= WebContextUtil.findWebContext(this,BufferedWebContext.class);
			String content=buffer.peekCharBuffer();
			operater.putCache(accessPath, content, this);
		}
	}

	public boolean isCachePath(String accessPath) {
		return operater.isCachePath(accessPath);
	}

}
