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
package org.tinygroup.flowbasiccomponent;

import org.tinygroup.context.Context;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.operator.DBOperator;

/**
 * 根据主键值查询
 * 
 * @author renhui
 * 
 */
public class TinydbQueryServiceWithId<T> extends AbstractTinydbService {

	private T primaryKey;

	public T getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(T primaryKey) {
		this.primaryKey = primaryKey;
	}

	public void tinyService(Bean bean,Context context, DBOperator operator) {
		Bean primaryBean = operator.getBean(primaryKey);
		if (primaryBean != null) {
			context.put(resultKey, primaryBean);
		} else {
			logger.logMessage(LogLevel.WARN,
					"根据主键查询不到记录，beantype:[{0}],主键值:[{1}]", beanType, primaryKey);
		}
	}

}
