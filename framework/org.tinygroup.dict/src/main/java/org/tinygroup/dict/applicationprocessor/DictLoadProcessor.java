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
package org.tinygroup.dict.applicationprocessor;

import org.tinygroup.application.Application;
import org.tinygroup.application.ApplicationProcessor;
import org.tinygroup.cache.CacheInitConfig;
import org.tinygroup.config.impl.AbstractConfiguration;
import org.tinygroup.dict.DictManager;
import org.tinygroup.springutil.SpringUtil;

/**
 * 
 * 功能说明:字典项加载启动插件 

 * 开发人员: renhui <br>
 * 开发时间: 2013-11-18 <br>
 * <br>
 */
public class DictLoadProcessor extends AbstractConfiguration implements ApplicationProcessor {

	private static final String DICT_NODE_PATH = "/application/dict-load-config";
	private DictManager manager;

	public DictManager getManager() {
		return manager;
	}

	public void setManager(DictManager manager) {
		this.manager = manager;
	}

	public String getApplicationNodePath() {
		return DICT_NODE_PATH;
	}

	public String getComponentConfigPath() {
		return null;
	}
	
	public void start() {
		CacheInitConfig config=SpringUtil.getBean("cacheInitConfig");
		manager.getCache().init(config.getRegion());
		manager.load();
	}

	public void stop() {

	}

	public void setApplication(Application application) {
		
	}

	public int getOrder() {
		return DEFAULT_PRECEDENCE;
	}
}
