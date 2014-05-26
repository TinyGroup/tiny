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
package org.tinygroup.tinydb.operator;

import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.order.OrderBean;
import org.tinygroup.tinydb.query.Conditions;
import org.tinygroup.tinydb.select.SelectBean;

/**
 * DB相关的查询操作
 * 
 * @author luoguo
 * 
 */
public interface DbSqlQueryOperator<K> {

	Bean[] getBeans(SelectBean[] selectBeans, Conditions conditions,
			OrderBean[] orderBeans);
	
	Bean[] getBeans(Conditions conditions,
			OrderBean[] orderBeans);

	Bean[] getBeans(SelectBean[] selectBeans, Conditions conditions,
			OrderBean[] orderBeans, int start, int limit);
	
	Bean[] getBeans(Conditions conditions,
			OrderBean[] orderBeans, int start, int limit);
	
	Bean getSingleValue(Conditions conditions);
	
	Bean getSingleValue(SelectBean[] selectBeans, Conditions conditions);

	Bean[] getBeans(String selectClause, Conditions conditions,
			OrderBean[] orderBeans);

	Bean[] getBeans(String selectClause, Conditions conditions,
			OrderBean[] orderBeans, int start, int limit);

	Bean getSingleValue(String selectClause, Conditions conditions);
}
