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

import org.tinygroup.commons.tools.Assert;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;

/**
 * 
 * 功能说明: 创建cache对象的工厂bean
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-5-22 <br>
 * <br>
 */
public class CacheFactoryBean implements FactoryBean,DisposableBean {

	private Cache cache;

	private String region;

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public Cache getCache() {
		return cache;
	}

	public void setCache(Cache cache) {
		this.cache = cache;
	}

	public Object getObject() throws Exception {
		Assert.assertNotNull(cache, "cache must not null");
		Assert.assertNotNull(region, "region must not null");
		cache.init(region);
		return cache;
	}

	public Class<?> getObjectType() {
		return Cache.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public void destroy() throws Exception {
		cache.destory();
	}

}
