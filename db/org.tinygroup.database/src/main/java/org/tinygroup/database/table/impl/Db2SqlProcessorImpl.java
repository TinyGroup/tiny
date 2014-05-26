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
package org.tinygroup.database.table.impl;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.tinygroup.database.config.table.Table;
import org.tinygroup.database.config.table.TableField;
import org.tinygroup.database.util.DataBaseUtil;


public class Db2SqlProcessorImpl extends SqlProcessorImpl {

	protected String getDatabaseType() {
		return "db2";
	}

	String appendIncrease() {
		return "";
	}
	
	public boolean checkTableExist(Table table, String catalog,
			DatabaseMetaData metadata) {

		ResultSet r = null;
		try {
			String schema = DataBaseUtil.getSchema(table, metadata);
			r = metadata.getTables(catalog, schema.toUpperCase(), table.getNameWithOutSchema().toUpperCase(),
					new String[] { "TABLE" });

			if (r.next()) {
				return true;
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			DataBaseUtil.closeResultSet(r);
		}

		return false;
	}

	
	protected List<String> dealExistFields(
			Map<String, TableField> existInTable,
			Map<String, Map<String, String>> dbColumns, Table table) {
		return null;
	}

}
