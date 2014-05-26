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
package org.tinygroup.tinydb.impl;

import org.tinygroup.commons.namestrategy.impl.CamelCaseStrategy;
import org.tinygroup.tinydb.BeanDbNameConverter;

/**
 * 默认数据库名称与Bean名称转换器
 * 
 * @author luoguo
 * 
 */
public class DefaultNameConverter implements BeanDbNameConverter {

	private CamelCaseStrategy strategy = new CamelCaseStrategy();

	public String dbFieldNameToPropertyName(String dbName) {
		return strategy.getPropertyName(dbName);
	}

	public String propertyNameToDbFieldName(String propertyName) {
		return strategy.getFieldName(propertyName);
	}

	public String dbTableNameToTypeName(String dbName) {
		String property = dbFieldNameToPropertyName(dbName);
		if (property.length() == 1){
			return property.toUpperCase();
		}	
		return property.substring(0, 1).toUpperCase() + property.substring(1);
	}

	public String typeNameToDbTableName(String pojoName) {

		if (pojoName.length() == 1) {
			return propertyNameToDbFieldName(pojoName.toLowerCase());
		}
		String propertyName = pojoName.substring(0, 1).toLowerCase()
				+ pojoName.substring(1);
		return propertyNameToDbFieldName(propertyName);

	}

}
