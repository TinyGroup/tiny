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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.BeanDbNameConverter;
import org.tinygroup.tinydb.impl.DefaultNameConverter;

public class BeanRowMapper implements RowMapper {
	private BeanDbNameConverter converter = new DefaultNameConverter();
	private String beanType;
	private String schema;

	public String getBeanType() {
		return beanType;
	}

	public void setBeanType(String beanType) {
		this.beanType = beanType;
	}

	public BeanRowMapper(String beanType, String schema) {
		this.beanType = beanType;
		this.schema = schema;
	}

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		Bean bean = TinyDBUtil.getBeanInstance(beanType, schema);
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();

		for (int index = 1; index <= columnCount; index++) {
			String columnName = JdbcUtils.lookupColumnName(rsmd, index)
					.toLowerCase();
			String propertyName = converter
					.dbFieldNameToPropertyName(columnName);
			bean.put(propertyName, rs.getObject(columnName));

		}
		return bean;
	}

}
