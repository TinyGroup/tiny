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
package org.tinygroup.dbrouterjdbc3.jdbc;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.tinygroup.dbrouter.util.DbRouterUtil;

/**
 * 
 * 功能说明: 只修改了getTableName方法，其他方法都委托原生的resultsetmetadata

 * 开发人员: renhui <br>
 * 开发时间: 2014-1-10 <br>
 * <br>
 */
public class TinyResultSetMetaData implements ResultSetMetaData {
	
	private final ResultSetMetaData resultSetMetaData;
	
	private  String tableName;
	
	
	public TinyResultSetMetaData(String sql,ResultSetMetaData resultSetMetaData) {
		super();
		this.resultSetMetaData = resultSetMetaData;
		if(sql!=null){
			tableName=DbRouterUtil.getSelectTableName(sql);
		}
	}

	public int getColumnCount() throws SQLException {
		return resultSetMetaData.getColumnCount();
	}

	public boolean isAutoIncrement(int column) throws SQLException {
		return resultSetMetaData.isAutoIncrement(column);
	}

	public boolean isCaseSensitive(int column) throws SQLException {
		return resultSetMetaData.isCaseSensitive(column);
	}

	public boolean isSearchable(int column) throws SQLException {
		return resultSetMetaData.isSearchable(column);
	}

	public boolean isCurrency(int column) throws SQLException {
		return resultSetMetaData.isCurrency(column);
	}

	public int isNullable(int column) throws SQLException {
		return resultSetMetaData.isNullable(column);
	}

	public boolean isSigned(int column) throws SQLException {
		return resultSetMetaData.isSigned(column);
	}

	public int getColumnDisplaySize(int column) throws SQLException {
		return resultSetMetaData.getColumnDisplaySize(column);
	}

	public String getColumnLabel(int column) throws SQLException {
		return resultSetMetaData.getColumnLabel(column);
	}

	public String getColumnName(int column) throws SQLException {
		return resultSetMetaData.getColumnName(column);
	}

	public String getSchemaName(int column) throws SQLException {
		return resultSetMetaData.getSchemaName(column);
	}

	public int getPrecision(int column) throws SQLException {
		return resultSetMetaData.getPrecision(column);
	}

	public int getScale(int column) throws SQLException {
		return resultSetMetaData.getScale(column);
	}

	public String getTableName(int column) throws SQLException {
		if(tableName==null){
			tableName=resultSetMetaData.getTableName(column);
		}
		return tableName;
	}

	public String getCatalogName(int column) throws SQLException {
		return resultSetMetaData.getCatalogName(column);
	}

	public int getColumnType(int column) throws SQLException {
		return resultSetMetaData.getColumnType(column);
	}

	public String getColumnTypeName(int column) throws SQLException {
		return resultSetMetaData.getColumnTypeName(column);
	}

	public boolean isReadOnly(int column) throws SQLException {
		return resultSetMetaData.isReadOnly(column);
	}

	public boolean isWritable(int column) throws SQLException {
		return resultSetMetaData.isWritable(column);
	}

	public boolean isDefinitelyWritable(int column) throws SQLException {
		return resultSetMetaData.isDefinitelyWritable(column);
	}

	public String getColumnClassName(int column) throws SQLException {
		return resultSetMetaData.getColumnClassName(column);
	}
	
}
