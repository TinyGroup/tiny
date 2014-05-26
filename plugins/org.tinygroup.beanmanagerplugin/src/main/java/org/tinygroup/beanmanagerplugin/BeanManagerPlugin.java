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
package org.tinygroup.beanmanagerplugin;

import java.util.List;
import java.util.Map;

import org.tinygroup.cache.Cache;
import org.tinygroup.cache.CacheInitConfig;
import org.tinygroup.config.impl.AbstractConfiguration;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.plugin.Plugin;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.tinydb.BeanOperatorManager;
import org.tinygroup.tinydb.config.SchemaConfig;
import org.tinygroup.tinydb.config.TableConfiguration;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * 
 * 功能说明:tinydb beanmanager管理接口处理插件 

 * 开发人员: renhui <br>
 * 开发时间: 2013-11-18 <br>
 * <br>
 */
public class BeanManagerPlugin extends AbstractConfiguration implements Plugin {
	
	private static final String DEFAULT_SCHEMA = "default-schema";

	private static final String KEY_TYPE = "key-type";

	private static final String TABLE_NAME_PATTERN = "table-name-pattern";

	private static final String BEAN_NAME = "bean-name";

	private static final String BEAN_MANAGER_NODE_PATH="/application/bean-manager-config";

	private static final String SCHEMA = "schema";
	
	private static final String BEAN_OPERATE_CONFIG="bean-opertate-config";
	
	private Cache cache;
	
	private BeanOperatorManager manager;
	
	public BeanOperatorManager getManager() {
		return manager;
	}

	public void setManager(BeanOperatorManager manager) {
		this.manager = manager;
	}
	

	public Cache getCache() {
		return cache;
	}

	public void setCache(Cache cache) {
		this.cache = cache;
	}

	public String getApplicationNodePath() {
		return BEAN_MANAGER_NODE_PATH;
	}

	public String getComponentConfigPath() {
		return "/beanmanager.config.xml";
	}

	public void start() {
//			String defaultSchema=ConfigurationUtil.getPropertyName(applicationConfig, componentConfig, DEFAULT_SCHEMA);
//			manager.setMainSchema(defaultSchema);
//			List<XmlNode> nodes = ConfigurationUtil.combineSubList(applicationConfig, componentConfig, BEAN_OPERATE_CONFIG, SCHEMA);
//		    for (XmlNode node : nodes) {
//				String schema=node.getAttribute(SCHEMA);
//				String beanName=node.getAttribute(BEAN_NAME);
//				String tableNamePattern=node.getAttribute(TABLE_NAME_PATTERN);
//				String keyType=node.getAttribute(KEY_TYPE);
//				manager.registerSchemaConfig(new SchemaConfig(schema, beanName, keyType, tableNamePattern));
//				manager.loadTablesFromSchemas();
//				manager.initBeansConfiguration();
//			}
//		    
//		    if(cache!=null){
//		    	CacheInitConfig config=SpringUtil.getBean("cacheInitConfig");
//		    	cache.init(config.getRegion());
//		    	Map<String, Map<String, TableConfiguration>> schemaTableConfigurations=manager.getTableConfigurations();
//		    	for (String schema : schemaTableConfigurations.keySet()) {
//					Map<String, TableConfiguration> tableConfigs=schemaTableConfigurations.get(schema);
//					for (String tableName : tableConfigs.keySet()) {
//						cache.put(schema, tableName, tableConfigs.get(tableName));
//					}
//				}
//			}
	}

	public void stop() {

	}

}
