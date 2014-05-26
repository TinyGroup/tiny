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
package org.tinygroup.database.customesql.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.database.config.SqlBody;
import org.tinygroup.database.config.customsql.CustomSql;
import org.tinygroup.database.config.customsql.CustomSqls;
import org.tinygroup.database.customesql.CustomSqlProcessor;

public class CustomSqlProcessorImpl implements CustomSqlProcessor {
	//Map<language,Map<type(before,after),sql>>
	private Map<String,Map<String,List<String>>> sqlsMap = new HashMap<String,Map<String,List<String>>>();
	
	public List<String> getCustomSqls(String type, String language) {

		return sqlsMap.get(language).get(type);
	}

	public void addCustomSqls(CustomSqls customsqls) {
		if(customsqls==null)
			return ;
		if(customsqls.getCustomSqlList()==null)
			return ; 
		for(CustomSql sql:customsqls.getCustomSqlList()){
			String type = sql.getType();
			for(SqlBody body:sql.getSqlBodyList()){
				String language = body.getDialectTypeName();
				String sqlStr = body.getContent();
				addSql(language, sqlStr, type);
			}
		}
	}
	
	private void addSql(String language,String sql,String type){
		if(!sqlsMap.containsKey(language)){
			Map<String,List<String>> map = new HashMap<String, List<String>>();
			sqlsMap.put(language,map);
		}
		Map<String,List<String>> map = sqlsMap.get(language);
		if(!map.containsKey(type)){
			List<String> sqlList = new ArrayList<String>();
			map.put(type, sqlList);
		}
		List<String> sqlList = map.get(type);
		sqlList.add(sql);
	}

	public void removeCustomSqls(CustomSqls customsqls) {
		if(customsqls==null)
			return ;
		if(customsqls.getCustomSqlList()==null)
			return ; 
		for(CustomSql sql:customsqls.getCustomSqlList()){
			String type = sql.getType();
			for(SqlBody body:sql.getSqlBodyList()){
				String language = body.getDialectTypeName();
				String sqlStr = body.getContent();
				removeSql(language, sqlStr, type);
			}
		}
	}

	private void removeSql(String language,String sql,String type){
		Map<String,List<String>> map = sqlsMap.get(language);
		if(!CollectionUtil.isEmpty(map)){
			List<String> sqlList=map.get(type);
			if(!CollectionUtil.isEmpty(sqlList)){
				sqlList.remove(sql);
			}
		}
	}
}
