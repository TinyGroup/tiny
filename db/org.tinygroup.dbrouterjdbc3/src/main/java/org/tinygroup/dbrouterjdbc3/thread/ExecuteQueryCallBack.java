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
package org.tinygroup.dbrouterjdbc3.thread;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.tinygroup.dbrouterjdbc3.jdbc.RealStatementExecutor;
import org.tinygroup.dbrouterjdbc3.jdbc.ResultSetExecutor;

public class ExecuteQueryCallBack implements StatementProcessorCallBack<ResultSetExecutor> {

	public ResultSetExecutor callBack(RealStatementExecutor statement)
			throws SQLException {
		ResultSet resultSet = statement.executeQuery();
		return new ResultSetExecutor(resultSet, statement.getExecuteSql(),statement.getOriginalSql(), statement.getShard(),statement.getPartition());
	}

}
