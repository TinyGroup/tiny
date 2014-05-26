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
package org.tinygroup.tinydb.config;

public class SchemaConfig {
	private String keyType;//主键类型，若为SchemaConfigContainer.INCREASE_KEY,则表明主键是自增类型
	private String schema;//一般是用户名
	private String operatorBeanName; //对应的operator的beanName
	private String tableNamePattern; //表名不符合此正则的表格会被此过滤
	
	public SchemaConfig(String schema, String operatorBeanName,String keyType, 
			String tableNamePattern) {
		super();
		this.keyType = keyType;
		this.schema = schema;
		this.operatorBeanName = operatorBeanName;
		this.tableNamePattern = tableNamePattern;
	}

	public String getKeyType() {
		return keyType;
	}

	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getOperatorBeanName() {
		return operatorBeanName;
	}

	public void setOperatorBeanName(String operatorBeanName) {
		this.operatorBeanName = operatorBeanName;
	}

	public String getTableNamePattern() {
		return tableNamePattern;
	}

	public void setTableNamePattern(String tableNamePattern) {
		this.tableNamePattern = tableNamePattern;
	}
	
	
}
