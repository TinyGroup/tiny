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
package org.tinygroup.tinydb.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.tinygroup.context.Context;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.BeanOperatorManager;
import org.tinygroup.tinydb.config.ColumnConfiguration;
import org.tinygroup.tinydb.config.TableConfiguration;
import org.tinygroup.tinydb.operator.DBOperator;

/**
 * 工具方法
 * 
 * @author luoguo
 * 
 */
public final class TinyDBUtil {

	private static BeanOperatorManager manager;

	static {
		manager = SpringUtil.getBean(BeanOperatorManager.OPERATOR_MANAGER_BEAN);
	}

	private TinyDBUtil() {
	}

	public static String getSeqName(String param) {
		return "seq_" + param;
	}

	public static <T extends Object> T[] listToArray(List<T> list) {
		if (list == null || list.size() == 0) {
			return null;
		}

		T[] array = (T[]) Array
				.newInstance(list.get(0).getClass(), list.size());
		int i = 0;
		for (Object obj : list) {
			array[i++] = (T) obj;
		}
		return array;
	}

	@SuppressWarnings("unchecked")
	public static <T extends Object> T[] collectionToArray(
			Collection<T> collection) {
		if (collection == null || collection.size() == 0) {
			return null;
		}

		T[] array = (T[]) Array.newInstance(collection.iterator().next()
				.getClass(), collection.size());
		int i = 0;
		for (Object obj : collection) {
			array[i++] = (T) obj;
		}
		return array;
	}

	public static TableConfiguration getTableConfig(String tableName,
			String schema) {
		String beanType = manager.getBeanDbNameConverter()
				.dbTableNameToTypeName(tableName);
		return manager.getTableConfiguration(beanType, schema);
	}

	public static DBOperator<?> getDBOperator(String schema, String beanType) {
		return manager.getDbOperator(schema, beanType);
	}

	public static TableConfiguration getTableConfigByBean(String beanType,
			String schema) {
		return manager.getTableConfiguration(beanType, schema);
	}

	/**
	 * 获取表信息
	 * 
	 * 表schema
	 * 
	 * @param columnType
	 *            表名
	 * @return 表信息
	 */

	public static String getColumnJavaType(String columnType) {
		return null;
	}

	public static List<String> getBeanProperties(String beanType, String schame) {
		TableConfiguration tableConfig = getTableConfigByBean(beanType, schame);
		List<String> properties = new ArrayList<String>();
		if (tableConfig != null) {
			for (ColumnConfiguration c : tableConfig.getColumns()) {
				String columnName = c.getColumnName();
				String propertyName = manager.getBeanDbNameConverter()
						.dbFieldNameToPropertyName(columnName);
				properties.add(propertyName);
			}
		}
		return properties;
	}

	public static Bean getBeanInstance(String beanType, String schame) {
		return new Bean(beanType);
	}

	public static Bean context2Bean(Context c, String beanType, String schame) {
		List<String> properties = getBeanProperties(beanType, schame);
		return context2Bean(c, beanType, properties, schame);
	}

	public static Bean context2Bean(Context c, String beanType) {
		List<String> properties = getBeanProperties(beanType, null);
		return context2Bean(c, beanType, properties, null);
	}

	public static Bean context2Bean(Context c, String beanType,
			List<String> properties, String schame) {
		Bean bean = getBeanInstance(beanType, schame);
		for (String property : properties) {
			if(c.exist(property)){
				bean.put(property, c.get(property));
			}
		}	
		return bean;
	}
}
