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
package org.tinygroup.tinydb.dialect;

/**
 * 方言接口，主要用于完成分页及获取自动生成的主键值
 * 
 * @author luoguo
 * 
 */
public interface Dialect {
	/**
	 * 是否支持分页
	 * @return
	 */
	boolean supportsLimit();

	/**
	 * 获取分页查询sql
	 * @param sql 查询sql
	 * @param start 分页查询起始行数
	 * @param limit 分页查询每页条数
	 * @return
	 */
	String getLimitString(String sql, int start, int limit);

	/**
	 * 主键是自增时，获取下一个自增的下一个主键
	 * @return
	 */
	int getNextKey();

	String getCurrentDate();
	/**
	 * 
	 * 返回经过方言函数解析后的sql
	 * @param sql
	 * @return
	 */
	 String buildSqlFuction(String sql);
}
