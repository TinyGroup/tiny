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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.StatementCreatorUtils;

/**
 * 
 * 功能说明: SqlParamValues参数形式批处理
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-11-6 <br>
 * <br>
 */
public class SqlParamValuesBatchStatementSetterImpl implements
		BatchPreparedStatementSetter {

	private List<SqlParameterValue[]> params;

	public SqlParamValuesBatchStatementSetterImpl(
			List<SqlParameterValue[]> params) {
		this.params = params;
	}

	public List<SqlParameterValue[]> getParams() {
		return params;
	}

	public void setParams(List<SqlParameterValue[]> params) {
		this.params = params;
	}

	public void setValues(PreparedStatement ps, int i) throws SQLException {
			SqlParameterValue[] sqlParams = params.get(i);
			doSetStatementParameters(sqlParams, ps);
	}

	public int getBatchSize() {
		return params.size();
	}

	private void doSetStatementParameters(SqlParameterValue[] values,
			PreparedStatement ps) throws SQLException {
		int colIndex = 0;
		for (SqlParameterValue paramValue : values) {
			colIndex++;
			StatementCreatorUtils.setParameterValue(ps, colIndex, paramValue,
					paramValue.getValue());
		}
	}

}
