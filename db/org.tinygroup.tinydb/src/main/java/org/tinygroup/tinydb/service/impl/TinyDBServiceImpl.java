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
package org.tinygroup.tinydb.service.impl;

import java.util.List;

import org.tinygroup.context.Context;
import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.config.TableConfiguration;
import org.tinygroup.tinydb.service.TinyDBService;
import org.tinygroup.tinydb.util.TinyDBUtil;

public class TinyDBServiceImpl implements TinyDBService{

	public TableConfiguration getTableConfig(String tableName,String schema) {
		return TinyDBUtil.getTableConfig(tableName,schema);
	}

	public TableConfiguration getTableConfigByBean(String beanType,String schema) {
		return TinyDBUtil.getTableConfigByBean(beanType,schema);
	}

	public List<String> getBeanProperties(String beanType,String schema) {
		return TinyDBUtil.getBeanProperties(beanType,schema);
	}
	public Bean context2Bean(Context c,String beanType,String schema){
		return TinyDBUtil.context2Bean(c, beanType,schema);
	}
	public Bean context2Bean(Context c,String beanType,List<String> properties,String schema){
		return TinyDBUtil.context2Bean(c, beanType, properties,schema);
	}
	
}
