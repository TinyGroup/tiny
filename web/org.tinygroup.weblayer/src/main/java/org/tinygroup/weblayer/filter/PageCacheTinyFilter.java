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
package org.tinygroup.weblayer.filter;

import java.util.List;

import org.tinygroup.cache.Cache;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.config.ConfigurationManager;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.parser.filter.NameFilter;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.weblayer.AbstractTinyFilter;
import org.tinygroup.weblayer.WebContext;
import org.tinygroup.weblayer.webcontext.cache.CacheOperater;
import org.tinygroup.weblayer.webcontext.cache.PageCacheWebContext;
import org.tinygroup.weblayer.webcontext.cache.impl.PageCacheWebContextImpl;
import org.tinygroup.weblayer.webcontext.util.WebContextUtil;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * 
 * 功能说明: 缓存页面的过滤器

 * 开发人员: renhui <br>
 * 开发时间: 2013-8-21 <br>
 * <br>
 */
public class PageCacheTinyFilter extends AbstractTinyFilter {
	
	private static final String DEFAULT_CACHE_BEAN_NAME = "cacheFactoryBean";

	private static final String CACHE_BEAN_NAME="cacheBeanName";

	private static final String PAGE_CACHE_CONFIG = "page-cache-config";
	
	CacheOperater operater;
	
	
	public void initTinyFilter() {
		super.initTinyFilter();
		
		init();
	}

	
    private void init() {
		String cacheBeanName=StringUtil.defaultIfEmpty(get(CACHE_BEAN_NAME), DEFAULT_CACHE_BEAN_NAME);
		Cache cache=SpringUtil.getBean(cacheBeanName);
        operater=new CacheOperater(cache);
        ConfigurationManager appConfigManager = ConfigurationUtil.getConfigurationManager();
		XmlNode parserNode = appConfigManager.getApplicationConfig().getSubNode(
				PAGE_CACHE_CONFIG);
		if(parserNode!=null){
			parserExtraConfig(parserNode);
		}
	}


	public void preProcess(WebContext context) {
		PageCacheWebContext webContext=(PageCacheWebContext)context;
		String accessPath=webContext.get(WebContextUtil.TINY_REQUEST_URI);
		if(webContext.isCached(accessPath)){
			webContext.cacheOutputPage(accessPath);
		}

	}

	public void postProcess(WebContext context) {
		PageCacheWebContext webContext=(PageCacheWebContext)context;
		String accessPath=webContext.get(WebContextUtil.TINY_REQUEST_URI);
		webContext.putCachePage(accessPath);

	}

	
	public WebContext getAlreadyWrappedContext(WebContext wrappedContext) {
		PageCacheWebContext webContext=new PageCacheWebContextImpl(wrappedContext);
		webContext.setCacheOperater(operater);
		return webContext;
	}

	
	protected void parserExtraConfig(XmlNode parserNode) {
		    NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(parserNode);
			List<XmlNode> mappings=nameFilter.findNodeList("cache-mapping");
			for (XmlNode xmlNode : mappings) {
				String patternStr=xmlNode.getAttribute("pattern");
				Long timeToLived=Long.parseLong(xmlNode.getAttribute("time-live"));
				XmlNode paramNode=xmlNode.getSubNode("param");
				String param=null;
				if(paramNode!=null){
					param=paramNode.getAttribute("name");
				}
				operater.addMapping(patternStr,param, timeToLived);
			}
		
	}
	
}
