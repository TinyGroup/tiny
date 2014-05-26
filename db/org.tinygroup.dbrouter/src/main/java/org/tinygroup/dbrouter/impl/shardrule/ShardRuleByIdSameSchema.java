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
package org.tinygroup.dbrouter.impl.shardrule;

import java.util.HashMap;
import java.util.Map;

import org.tinygroup.dbrouter.util.DbRouterUtil;

/**
 * Created by luoguo on 13-12-15.
 */
public class ShardRuleByIdSameSchema extends ShardRuleByIdAbstract {

	public ShardRuleByIdSameSchema() {

	}

	public ShardRuleByIdSameSchema(String tableName,
			String primaryKeyFieldName, int remainder) {
		super(tableName, primaryKeyFieldName, remainder);
	}


	public String getReplacedSql(String sql) {
		Map<String, String> tableMapping = new HashMap<String, String>();
		tableMapping.put(getTableName(), getTableName() + getRemainder());
		return DbRouterUtil.transformSqlWithTableName(sql, tableMapping);
	}
}
