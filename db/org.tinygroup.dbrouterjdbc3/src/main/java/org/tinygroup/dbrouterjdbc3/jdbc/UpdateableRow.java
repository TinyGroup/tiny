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

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.dbrouter.config.Shard;

import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;

/**
 * 功能说明: 用于可更新的resultset insetrow、updaterow、deleterow操作
 * <p/>
 * <p/>
 * 开发人员: renhui <br>
 * 开发时间: 2014-1-8 <br>
 * <br>
 */
public class UpdateableRow {
	
	private TinyConnection tinyConnection;
	private ResultSetMetaData metaData;
	private String schemaName;
	private String tableName;
	private int columnCount;
	private Connection connection;
	private ArrayList<String> key;// 主键字段

	public UpdateableRow(TinyConnection tinyConnection,
			Shard shard,ResultSet currentResultSet) throws SQLException {
		this.tinyConnection = tinyConnection;
		connection = shard.getConnection(tinyConnection);
		metaData = currentResultSet.getMetaData();
		columnCount = metaData.getColumnCount();
		if (columnCount > 0) {
			schemaName = metaData.getSchemaName(1);
			tableName = metaData.getTableName(1);
		} else {
			throw new RuntimeException("no column in table");
		}
		final DatabaseMetaData meta = connection.getMetaData();
		ResultSet rs = meta.getTables(null,
				StringUtil.escapeMetaDataPattern(schemaName),
				StringUtil.escapeMetaDataPattern(tableName),
				new String[] { "TABLE" });
		if (!rs.next()) {
			return;
		}
		String table = rs.getString("TABLE_NAME");
		boolean toUpper = !table.equals(tableName)
				&& table.equalsIgnoreCase(tableName);
		key = new ArrayList<String>();
		rs = meta.getPrimaryKeys(null,
				StringUtil.escapeMetaDataPattern(schemaName), tableName);
		while (rs.next()) {
			String c = rs.getString("COLUMN_NAME");
			key.add(toUpper ? c.toUpperCase(Locale.ENGLISH) : c);
		}

	}

	public void insertRow(Object[] values) throws SQLException {
		StatementBuilder buff = new StatementBuilder();
		buff.append("INSERT INTO ");
		appendTableName(buff);
		buff.append('(');
		appendColumnList(buff, false);
		buff.append(")VALUES(");
		buff.resetCount();
		for (int i = 0; i < columnCount; i++) {
			buff.appendExceptFirst(",");
			buff.append('?');
		}
		buff.append(')');
		PreparedStatement prep = null;
        if(tinyConnection!=null){
        	 prep = tinyConnection
			.prepareStatement(buff.toString());
        }else{
        	prep=connection.prepareStatement(buff.toString());
        }
		
		for (int i = 0; i < columnCount; i++) {
			prep.setObject(i + 1, values[i]);
		}
		int count = prep.executeUpdate();
		if (count != 1) {
			throw new SQLException("no data insert");
		}

	}

	private void appendTableName(StatementBuilder buff) {
		if (schemaName != null && schemaName.length() > 0) {
			buff.append(schemaName).append('.');
		}
		buff.append(tableName);
	}

	private void appendColumnList(StatementBuilder buff, boolean set)
			throws SQLException {
		buff.resetCount();
		for (int i = 0; i < columnCount; i++) {
			buff.appendExceptFirst(",");
			String col = metaData.getColumnName(i+1);
			buff.append(col);
			if (set) {
				buff.append("=? ");
			}
		}
	}

	private void appendKeyCondition(StatementBuilder buff) {
		buff.append(" WHERE ");
		buff.resetCount();
		for (String k : key) {
			buff.appendExceptFirst(" AND ");
			buff.append(k).append("=?");
		}
	}

	public void updateRow(Object[] current, Object[] updateValues)
			throws SQLException {
		StatementBuilder buff = new StatementBuilder("UPDATE ");
		appendTableName(buff);
		buff.append(" SET ");
		appendColumnList(buff, true);
		// updatable result set: we could add all current values to the
		// where clause
		// - like this optimistic ('no') locking is possible
		appendKeyCondition(buff);
		PreparedStatement prep = connection.prepareStatement(buff.toString());
		int j = 1;
		for (int i = 0; i < columnCount; i++) {
			Object v = updateValues[i];
			if (v == null) {
				v = current[i];
			}
			prep.setObject(i + 1, v);
			j++;
		}
		setKey(prep, j, current);
		int count = prep.executeUpdate();
		if (count != 1) {
			throw new SQLException("no data update");
		}

	}

	private void setKey(PreparedStatement prep, int start, Object[] current)
			throws SQLException {
		for (int i = 0, size = key.size(); i < size; i++) {
			String col = key.get(i);
			int idx = getColumnIndex(col);
			Object v = current[idx];
			if (v == null) {
				// rows with a unique key containing NULL are not supported,
				// as multiple such rows could exist
				throw new SQLException("primary key column must not null");
			}
			prep.setObject(start + i, v);
		}
	}

	private int findColumnIndex(String columnName) throws SQLException {
		for (int i = 0; i < columnCount; i++) {
			String col = metaData.getColumnName(i+1);
			if (columnName.equalsIgnoreCase(col)) {
				return i;
			}
			String label = metaData.getColumnLabel(i);
			if (columnName.equalsIgnoreCase(label)) {
				return i;
			}
		}
		return -1;
	}

	private int getColumnIndex(String columnName) throws SQLException {
		int index = findColumnIndex(columnName);
		if (index < 0) {
			throw new SQLException("not found columnName:" + columnName);
		}
		return index;
	}

	public void deleteRow(Object[] current) throws SQLException {
		StatementBuilder buff = new StatementBuilder("DELETE FROM ");
		appendTableName(buff);
		appendKeyCondition(buff);
		PreparedStatement prep = connection.prepareStatement(buff.toString());
		setKey(prep, 1, current);
		int count = prep.executeUpdate();
		if (count != 1) {
			throw new SQLException("no data delete");
		}
	}

	 /**
     * Re-reads a row from the database and updates the values in the array.
     *
     * @param current
     * @return the row
     */
    public ResultSet readRow(Object[] current) throws SQLException {
        StatementBuilder buff = new StatementBuilder("SELECT ");
        appendColumnList(buff, false);
        buff.append(" FROM ");
        appendTableName(buff);
        appendKeyCondition(buff);
        PreparedStatement prep = connection.prepareStatement(buff.toString());
        setKey(prep, 1, current);
        ResultSet rs = prep.executeQuery();
        if (!rs.next()) {
            throw new SQLException("no data select");
        }
        return rs;
    }

    public int getColumnCount() {
        return columnCount;
    }

}
