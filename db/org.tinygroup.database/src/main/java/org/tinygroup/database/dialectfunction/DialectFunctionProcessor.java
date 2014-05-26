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
package org.tinygroup.database.dialectfunction;

import org.tinygroup.database.config.dialectfunction.Dialect;
import org.tinygroup.database.config.dialectfunction.DialectFunction;
import org.tinygroup.database.config.dialectfunction.DialectFunctions;
/**
 * 
 * 功能说明:方言函数接口 

 * 开发人员: renhui <br>
 * 开发时间: 2013-8-20 <br>
 * <br>
 */
public interface DialectFunctionProcessor {
	
	String DATABASE_TYPE="databaseType";
	/**
	 * 
	 * 增加方言函数配置信息
	 * @param functions
	 */
	void addDialectFunctions(DialectFunctions functions);
	
	/**
	 * 
	 * 删除方言函数配置信息
	 * @param functions
	 */
	void removeDialectFunctions(DialectFunctions functions);
	
	/**
	 * 
	 * 根据方言函数名获取方言函数配置
	 * @param functionName
	 * @return
	 */
	DialectFunction getDialectFunction(String functionName);
	
	/**
	 * 
	 * 根据函数名称获取以及数据库类型，获取方言函数信息
	 * @param functionName
	 * @param databaseType
	 * @return
	 */
	Dialect getDialectWithDatabaseType(String functionName,String databaseType);
	/**
	 * 
	 * 返回经过方言函数替换后的sql
	 * @param originalSql
	 * @param databaseType
	 * @return
	 */
	String getFuntionSql(String originalSql,String databaseType);
}
