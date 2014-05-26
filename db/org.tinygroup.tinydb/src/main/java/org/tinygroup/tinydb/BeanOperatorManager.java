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
package org.tinygroup.tinydb;

import org.tinygroup.tinydb.config.SchemaConfig;
import org.tinygroup.tinydb.config.TableConfiguration;
import org.tinygroup.tinydb.config.TableConfigurationContainer;
import org.tinygroup.tinydb.operator.DBOperator;
import org.tinygroup.tinydb.relation.Relation;
import org.tinygroup.tinydb.relation.Relations;

/**
 * Bean操作管理器
 * 
 * @author renhui
 * 
 */
public interface BeanOperatorManager {
	String OPERATOR_MANAGER_BEAN = "beanOperatorManager";
	String XSTEAM_PACKAGE_NAME = "tinydb";
	String NULLABLE = "NULLABLE";
	String TYPE_NAME = "TYPE_NAME";
	String COLUMN_SIZE = "COLUMN_SIZE";
	String DECIMAL_DIGITS = "DECIMAL_DIGITS";
	String COLUMN_NAME = "COLUMN_NAME";
	String PK_NAME = "COLUMN_NAME";
	String DATA_TYPE = "DATA_TYPE";
	
	String TABLE_NAME="TABLE_NAME";
	/**
	 * 设置默认的schema
	 * @param schema
	 */
	void setMainSchema(String schema);
	
	String getMainSchema();
	
	/**
	 * 获取数据操作器
	 * @param schema
	 * @param beanType
	 * @return
	 */
	DBOperator<?> getDbOperator(String schema,String beanType);
	
	/**
	 * 获取数据操作器
	 * @param beanType
	 * @return
	 */
	DBOperator<?> getDbOperator(String beanType);

	/**
	 * @param schemaConfig
	 */
	void registerSchemaConfig(SchemaConfig schemaConfig);

	/**
	 * 根据表名获取表配置信息
	 * 
	 * @param beanType
	 * @param schema
	 * @return
	 */
	TableConfiguration getTableConfiguration(String beanType,String schema);
	
	/**
	 * 根据表名获取表配置信息
	 * 
	 * @param beanType
	 * @return
	 */
	TableConfiguration getTableConfiguration(String beanType);
	
	/**
	 * 
	 * 要处理的所有schema列表
	 */
	 void loadTablesFromSchemas();
	
	/**
	 * 获取表配置信息容器对象
	 * @return
	 */
	 TableConfigurationContainer getTableConfigurationContainer();
	
	
	/**
	 * 
	 * 添加数据库表关联关系
	 * @param relations
	 */
	void addRelationConfigs(Relations relations);
	
	/**
	 * 
	 * 移除数据库表关联关系
	 * @param relations
	 */
	void removeRelationConfigs(Relations relations);
	
	/**
	 * 
	 * 获取关联描述信息
	 * @param id 描述信息的唯一标识符
	 * @return
	 */
	Relation getRelationById(String id);
	
	/**
	 * 
	 * 获取关联描述信息
	 * @param beanType
	 * @return
	 */
	Relation getRelationByBeanType(String beanType);
	/**
	 * 是否存在参数描述的数据库表
	 * @param beanType
	 * @param schema
	 * @return
	 */
	boolean existsTableByType(String beanType,String schema);
	
	/**
	 * 设置bean名称转换对象
	 * @param beanDbNameConverter
	 */
	void setBeanDbNameConverter(BeanDbNameConverter beanDbNameConverter);
	
	BeanDbNameConverter getBeanDbNameConverter();

}
