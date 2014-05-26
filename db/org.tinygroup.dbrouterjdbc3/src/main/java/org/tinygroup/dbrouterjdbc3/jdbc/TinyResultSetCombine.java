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

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.tinygroup.commons.tools.Assert;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 
 * 功能说明: 多个resultset组装的resultset,
 *  只支持向前遍历
 * 
 * 开发人员: renhui <br>
 * 开发时间: 2014-1-10 <br>
 * <br>
 */
public class TinyResultSetCombine implements ResultSet {
	/**
	 * 记录集列表
	 */
	private final List<ResultSet> resultSets;

	/**
	 * 当前的记录集
	 */
	private ResultSet currentResultSet = null;
	/**
	 * 当前行号
	 */
	private int row = 0;// 当前行号,第一行行号为1
	/**
	 * 记录集数据
	 */
	private List<ResultSetData> resultSetDataList = new ArrayList<ResultSetData>();

	private boolean isClosed;

	private Logger logger = LoggerFactory
			.getLogger(TinyResultSetCombine.class);

	public TinyResultSetCombine(List<ResultSet> resultSets) {
		Assert.assertNotNull(resultSets, "resultSets must not null");
		this.resultSets = resultSets;
		for (int i = 0; i < resultSets.size(); i++) {
			ResultSet resultSet = resultSets.get(i);
			try {
				ResultSetData resultSetData = new ResultSetData(i, resultSet);
				resultSetDataList.add(resultSetData);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * 重设结果集
	 * 
	 * @throws SQLException
	 */
	private void reset() throws SQLException {
		for (ResultSet resultSet : resultSets) {
			resultSet.beforeFirst();
		}
	}

	public boolean next() throws SQLException {
		if (row <= 0) {
			row = 0;
		}
		for (ResultSetData resultSetData : resultSetDataList) {
			if (resultSetData.next()) {
				currentResultSet = resultSetData.getResultSet();
				row++;
				return true;
			}
		}
		return false;
	}

	public void close() throws SQLException {
		StringBuffer buffer = new StringBuffer();
		boolean noError = true;
		for (ResultSet resultSet : resultSets) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				buffer.append(String
						.format("resultSet close error,errorcode:%s,sqlstate:%s,message:%s \n",
								e.getErrorCode(), e.getSQLState(),
								e.getMessage()));
				noError = false;
				logger.errorMessage("result close error", e);
			}
		}
		isClosed = true;
		if (!noError) {
			throw new SQLException(buffer.toString());
		}

	}

	public boolean wasNull() throws SQLException {
		checkClosed();
		return currentResultSet.wasNull();
	}

	public String getString(int columnIndex) throws SQLException {
		checkClosed();
		return currentResultSet.getString(columnIndex);
	}

	public boolean getBoolean(int columnIndex) throws SQLException {
		checkClosed();
		return currentResultSet.getBoolean(columnIndex);
	}

	public byte getByte(int columnIndex) throws SQLException {
		checkClosed();
		return currentResultSet.getByte(columnIndex);
	}

	public short getShort(int columnIndex) throws SQLException {
		checkClosed();
		return currentResultSet.getShort(columnIndex);
	}

	public int getInt(int columnIndex) throws SQLException {
		checkClosed();
		return currentResultSet.getInt(columnIndex);
	}

	public long getLong(int columnIndex) throws SQLException {
		checkClosed();
		return currentResultSet.getLong(columnIndex);
	}

	public float getFloat(int columnIndex) throws SQLException {
		checkClosed();
		return currentResultSet.getFloat(columnIndex);
	}

	public double getDouble(int columnIndex) throws SQLException {
		checkClosed();
		return currentResultSet.getDouble(columnIndex);
	}

	public BigDecimal getBigDecimal(int columnIndex, int scale)
			throws SQLException {
		checkClosed();
		return currentResultSet.getBigDecimal(columnIndex, scale);
	}

	public byte[] getBytes(int columnIndex) throws SQLException {
		checkClosed();
		return currentResultSet.getBytes(columnIndex);
	}

	public Date getDate(int columnIndex) throws SQLException {
		checkClosed();
		return currentResultSet.getDate(columnIndex);
	}

	public Time getTime(int columnIndex) throws SQLException {
		checkClosed();
		return currentResultSet.getTime(columnIndex);
	}

	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		checkClosed();
		return currentResultSet.getTimestamp(columnIndex);
	}

	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		checkClosed();
		return currentResultSet.getAsciiStream(columnIndex);
	}

	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		checkClosed();
		return currentResultSet.getUnicodeStream(columnIndex);
	}

	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		checkClosed();
		return currentResultSet.getBinaryStream(columnIndex);
	}

	public String getString(String columnName) throws SQLException {
		checkClosed();
		return currentResultSet.getString(columnName);
	}

	public boolean getBoolean(String columnName) throws SQLException {
		checkClosed();
		return currentResultSet.getBoolean(columnName);
	}

	public byte getByte(String columnName) throws SQLException {
		checkClosed();
		return currentResultSet.getByte(columnName);
	}

	public short getShort(String columnName) throws SQLException {
		checkClosed();
		return currentResultSet.getShort(columnName);
	}

	public int getInt(String columnName) throws SQLException {
		checkClosed();
		return currentResultSet.getInt(columnName);
	}

	public long getLong(String columnName) throws SQLException {
		checkClosed();
		return currentResultSet.getLong(columnName);
	}

	public float getFloat(String columnName) throws SQLException {
		checkClosed();
		return currentResultSet.getFloat(columnName);
	}

	public double getDouble(String columnName) throws SQLException {
		checkClosed();
		return currentResultSet.getDouble(columnName);
	}

	public BigDecimal getBigDecimal(String columnName, int scale)
			throws SQLException {
		checkClosed();
		return currentResultSet.getBigDecimal(columnName, scale);
	}

	public byte[] getBytes(String columnName) throws SQLException {
		checkClosed();
		return currentResultSet.getBytes(columnName);
	}

	public Date getDate(String columnName) throws SQLException {
		checkClosed();
		return currentResultSet.getDate(columnName);
	}

	public Time getTime(String columnName) throws SQLException {
		checkClosed();
		return currentResultSet.getTime(columnName);
	}

	public Timestamp getTimestamp(String columnName) throws SQLException {
		checkClosed();
		return currentResultSet.getTimestamp(columnName);
	}

	public InputStream getAsciiStream(String columnName) throws SQLException {
		checkClosed();
		return currentResultSet.getAsciiStream(columnName);
	}

	public InputStream getUnicodeStream(String columnName) throws SQLException {
		checkClosed();
		return currentResultSet.getUnicodeStream(columnName);
	}

	public InputStream getBinaryStream(String columnName) throws SQLException {
		checkClosed();
		return currentResultSet.getBinaryStream(columnName);
	}

	public SQLWarning getWarnings() throws SQLException {
		checkClosed();
		return currentResultSet.getWarnings();
	}

	protected void checkClosed() throws SQLException {
		if (isClosed) {
			throw new SQLException("result is closed");
		}
	}

	public void clearWarnings() throws SQLException {
		checkClosed();
		StringBuffer buffer = new StringBuffer();
		boolean noError = true;
		for (ResultSet resultSet : resultSets) {
			try {
				resultSet.clearWarnings();
			} catch (SQLException e) {
				buffer.append(String
						.format("resultSet clearWarnings error,errorcode:%s,sqlstate:%s,message:%s \n",
								e.getErrorCode(), e.getSQLState(),
								e.getMessage()));
				noError = false;
			}
		}
		if (!noError) {
			throw new SQLException(buffer.toString());
		}
	}

	public String getCursorName() throws SQLException {
		throw new SQLException("not support cursorName");
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		checkClosed();
		if (currentResultSet != null) {
			return new TinyResultSetMetaData(null,
					currentResultSet.getMetaData());
		}
		return null;
	}

	public Object getObject(int columnIndex) throws SQLException {
		checkClosed();
		return currentResultSet.getObject(columnIndex);
	}

	public Object getObject(String columnName) throws SQLException {
		checkClosed();
		return currentResultSet.getObject(columnName);
	}

	public int findColumn(String columnName) throws SQLException {
		checkClosed();
		return currentResultSet.findColumn(columnName);
	}

	public Reader getCharacterStream(int columnIndex) throws SQLException {
		checkClosed();
		return currentResultSet.getCharacterStream(columnIndex);
	}

	public Reader getCharacterStream(String columnName) throws SQLException {
		checkClosed();
		return currentResultSet.getCharacterStream(columnName);
	}

	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		checkClosed();
		return currentResultSet.getBigDecimal(columnIndex);
	}

	public BigDecimal getBigDecimal(String columnName) throws SQLException {
		checkClosed();
		return currentResultSet.getBigDecimal(columnName);
	}

	public boolean isBeforeFirst() throws SQLException {
		checkClosed();
		return row < 1;
	}

	public boolean isAfterLast() throws SQLException {
		checkClosed();
		ResultSet lastResult=resultSets.get(resultSets.size()-1);
		return lastResult.isAfterLast();
	}

	public boolean isFirst() throws SQLException {
		checkClosed();
		return row == 1;
	}

	public boolean isLast() throws SQLException {
		checkClosed();
		ResultSet lastResult=resultSets.get(resultSets.size()-1);
		return lastResult.isLast();
	}

	public void beforeFirst() throws SQLException {
		checkClosed();
		reset();
		row = 0;
	}

	public void afterLast() throws SQLException {
		checkClosed();
		for (ResultSet resultSet : resultSets) {
			resultSet.afterLast();
		}
	}

	public boolean first() throws SQLException {
		checkClosed();
		for (ResultSet resultSet : resultSets) {
			resultSet.first();
		}
		row = 1;
		return true;
	}

	public boolean last() throws SQLException {
		checkClosed();
		int totalRows = getTotalRows();
		row = totalRows;
		return true;
	}

	private int getTotalRows() throws SQLException {
		int totalRows=0;
		for (ResultSet resultSet : resultSets) {
			resultSet.last();
			totalRows+=resultSet.getRow();
		}
		return totalRows;
	}

	public int getRow() throws SQLException {
		checkClosed();
		return row;
	}

	public boolean absolute(int row) throws SQLException {
		checkClosed();
		this.row = row;
		ResultSetData resultSetData=getResultSetData(row);
		if(resultSetData!=null){
			return resultSetData.absolute(row);
		}
		return false;
	}

	public boolean relative(int rows) throws SQLException {
		checkClosed();
		int totalRows=getTotalRows();
		this.row = row + rows;
		if (row <= 0) {
			row = 1;
		} else if (row > totalRows) {
			row = totalRows;
		}
		return absolute(row);
	}

	public boolean previous() throws SQLException {
		return relative(-1);
	}

	public void setFetchDirection(int direction) throws SQLException {
		throw new SQLException("not support setFetchDirection");
	}

	public int getFetchDirection() throws SQLException {
		return ResultSet.FETCH_FORWARD;
	}

	public void setFetchSize(int rows) throws SQLException {
		checkClosed();
		currentResultSet.setFetchSize(rows);
	}

	public int getFetchSize() throws SQLException {
		checkClosed();
		int fetchSize = 0;
		for (ResultSet resultSet : resultSets) {
			fetchSize += resultSet.getFetchSize();
		}
		return fetchSize;
	}

	public int getType() throws SQLException {
		checkClosed();
		return currentResultSet.getType();
	}

	public int getConcurrency() throws SQLException {
		checkClosed();
		return currentResultSet.getConcurrency();
	}

	public boolean rowUpdated() throws SQLException {
		checkClosed();
		return currentResultSet.rowUpdated();
	}

	public boolean rowInserted() throws SQLException {
		checkClosed();
		return currentResultSet.rowInserted();
	}

	public boolean rowDeleted() throws SQLException {
		checkClosed();
		return currentResultSet.rowDeleted();
	}

	public void updateNull(int columnIndex) throws SQLException {
		checkClosed();
		currentResultSet.updateNull(columnIndex);
	}

	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
		checkClosed();
		currentResultSet.updateBoolean(columnIndex, x);
	}

	public void updateByte(int columnIndex, byte x) throws SQLException {
		checkClosed();
		currentResultSet.updateByte(columnIndex, x);
	}

	public void updateShort(int columnIndex, short x) throws SQLException {
		checkClosed();
		currentResultSet.updateShort(columnIndex, x);
	}

	public void updateInt(int columnIndex, int x) throws SQLException {
		checkClosed();
		currentResultSet.updateInt(columnIndex, x);
	}

	public void updateLong(int columnIndex, long x) throws SQLException {
		checkClosed();
		currentResultSet.updateLong(columnIndex, x);
	}

	public void updateFloat(int columnIndex, float x) throws SQLException {
		checkClosed();
		currentResultSet.updateFloat(columnIndex, x);
	}

	public void updateDouble(int columnIndex, double x) throws SQLException {
		checkClosed();
		currentResultSet.updateDouble(columnIndex, x);
	}

	public void updateBigDecimal(int columnIndex, BigDecimal x)
			throws SQLException {
		checkClosed();
		currentResultSet.updateBigDecimal(columnIndex, x);
	}

	public void updateString(int columnIndex, String x) throws SQLException {
		checkClosed();
		currentResultSet.updateString(columnIndex, x);
	}

	public void updateBytes(int columnIndex, byte[] x) throws SQLException {
		checkClosed();
		currentResultSet.updateBytes(columnIndex, x);
	}

	public void updateDate(int columnIndex, Date x) throws SQLException {
		checkClosed();
		currentResultSet.updateDate(columnIndex, x);
	}

	public void updateTime(int columnIndex, Time x) throws SQLException {
		checkClosed();
		currentResultSet.updateTime(columnIndex, x);
	}

	public void updateTimestamp(int columnIndex, Timestamp x)
			throws SQLException {
		checkClosed();
		currentResultSet.updateTimestamp(columnIndex, x);
	}

	public void updateAsciiStream(int columnIndex, InputStream x, int length)
			throws SQLException {
		checkClosed();
		currentResultSet.updateAsciiStream(columnIndex, x, length);
	}

	public void updateBinaryStream(int columnIndex, InputStream x, int length)
			throws SQLException {
		checkClosed();
		currentResultSet.updateBinaryStream(columnIndex, x, length);
	}

	public void updateCharacterStream(int columnIndex, Reader x, int length)
			throws SQLException {
		checkClosed();
		currentResultSet.updateCharacterStream(columnIndex, x, length);
	}

	public void updateObject(int columnIndex, Object x, int scale)
			throws SQLException {
		checkClosed();
		currentResultSet.updateObject(columnIndex, x, scale);
	}

	public void updateObject(int columnIndex, Object x) throws SQLException {
		checkClosed();
		currentResultSet.updateObject(columnIndex, x);
	}

	public void updateNull(String columnName) throws SQLException {
		checkClosed();
		currentResultSet.updateNull(columnName);
	}

	public void updateBoolean(String columnName, boolean x) throws SQLException {
		checkClosed();
		currentResultSet.updateBoolean(columnName, x);
	}

	public void updateByte(String columnName, byte x) throws SQLException {
		checkClosed();
		currentResultSet.updateByte(columnName, x);
	}

	public void updateShort(String columnName, short x) throws SQLException {
		checkClosed();
		currentResultSet.updateShort(columnName, x);
	}

	public void updateInt(String columnName, int x) throws SQLException {
		checkClosed();
		currentResultSet.updateInt(columnName, x);
	}

	public void updateLong(String columnName, long x) throws SQLException {
		checkClosed();
		currentResultSet.updateLong(columnName, x);
	}

	public void updateFloat(String columnName, float x) throws SQLException {
		checkClosed();
		currentResultSet.updateFloat(columnName, x);
	}

	public void updateDouble(String columnName, double x) throws SQLException {
		checkClosed();
		currentResultSet.updateDouble(columnName, x);
	}

	public void updateBigDecimal(String columnName, BigDecimal x)
			throws SQLException {
		checkClosed();
		currentResultSet.updateBigDecimal(columnName, x);
	}

	public void updateString(String columnName, String x) throws SQLException {
		checkClosed();
		currentResultSet.updateString(columnName, x);
	}

	public void updateBytes(String columnName, byte[] x) throws SQLException {
		checkClosed();
		currentResultSet.updateBytes(columnName, x);
	}

	public void updateDate(String columnName, Date x) throws SQLException {
		checkClosed();
		currentResultSet.updateDate(columnName, x);
	}

	public void updateTime(String columnName, Time x) throws SQLException {
		checkClosed();
		currentResultSet.updateTime(columnName, x);
	}

	public void updateTimestamp(String columnName, Timestamp x)
			throws SQLException {
		checkClosed();
		currentResultSet.updateTimestamp(columnName, x);
	}

	public void updateAsciiStream(String columnName, InputStream x, int length)
			throws SQLException {
		checkClosed();
		currentResultSet.updateAsciiStream(columnName, x, length);
	}

	public void updateBinaryStream(String columnName, InputStream x, int length)
			throws SQLException {
		checkClosed();
		currentResultSet.updateBinaryStream(columnName, x, length);
	}

	public void updateCharacterStream(String columnName, Reader reader,
			int length) throws SQLException {
		checkClosed();
		currentResultSet.updateCharacterStream(columnName, reader, length);
	}

	public void updateObject(String columnName, Object x, int scale)
			throws SQLException {
		checkClosed();
		currentResultSet.updateObject(columnName, x, scale);
	}

	public void updateObject(String columnName, Object x) throws SQLException {
		checkClosed();
		currentResultSet.updateObject(columnName, x);
	}

	public void insertRow() throws SQLException {
		checkClosed();
		currentResultSet.insertRow();
	}

	public void updateRow() throws SQLException {
		checkClosed();
		currentResultSet.updateRow();
	}

	public void deleteRow() throws SQLException {
		checkClosed();
		currentResultSet.deleteRow();
	}

	public void refreshRow() throws SQLException {
		checkClosed();
		currentResultSet.refreshRow();
	}

	public void cancelRowUpdates() throws SQLException {
		checkClosed();
		currentResultSet.cancelRowUpdates();
	}

	public void moveToInsertRow() throws SQLException {
		checkClosed();
		currentResultSet.moveToInsertRow();
	}

	public void moveToCurrentRow() throws SQLException {
		checkClosed();
		currentResultSet.moveToCurrentRow();
	}

	public Statement getStatement() throws SQLException {
		checkClosed();
		return currentResultSet.getStatement();
	}

	public Object getObject(int i, Map<String, Class<?>> map)
			throws SQLException {
		checkClosed();
		return currentResultSet.getObject(i, map);
	}

	public Ref getRef(int i) throws SQLException {
		checkClosed();
		return currentResultSet.getRef(i);
	}

	public Blob getBlob(int i) throws SQLException {
		checkClosed();
		return currentResultSet.getBlob(i);
	}

	public Clob getClob(int i) throws SQLException {
		checkClosed();
		return currentResultSet.getClob(i);
	}

	public Array getArray(int i) throws SQLException {
		checkClosed();
		return currentResultSet.getArray(i);
	}

	public Object getObject(String colName, Map<String, Class<?>> map)
			throws SQLException {
		checkClosed();
		return currentResultSet.getObject(colName, map);
	}

	public Ref getRef(String colName) throws SQLException {
		checkClosed();
		return currentResultSet.getRef(colName);
	}

	public Blob getBlob(String colName) throws SQLException {
		checkClosed();
		return currentResultSet.getBlob(colName);
	}

	public Clob getClob(String colName) throws SQLException {
		checkClosed();
		return currentResultSet.getClob(colName);
	}

	public Array getArray(String colName) throws SQLException {
		checkClosed();
		return currentResultSet.getArray(colName);
	}

	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		checkClosed();
		return currentResultSet.getDate(columnIndex, cal);
	}

	public Date getDate(String columnName, Calendar cal) throws SQLException {
		checkClosed();
		return currentResultSet.getDate(columnName, cal);
	}

	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		checkClosed();
		return currentResultSet.getTime(columnIndex, cal);
	}

	public Time getTime(String columnName, Calendar cal) throws SQLException {
		checkClosed();
		return currentResultSet.getTime(columnName, cal);
	}

	public Timestamp getTimestamp(int columnIndex, Calendar cal)
			throws SQLException {
		checkClosed();
		return currentResultSet.getTimestamp(columnIndex, cal);
	}

	public Timestamp getTimestamp(String columnName, Calendar cal)
			throws SQLException {
		checkClosed();
		return currentResultSet.getTimestamp(columnName, cal);
	}

	public URL getURL(int columnIndex) throws SQLException {
		checkClosed();
		return currentResultSet.getURL(columnIndex);
	}

	public URL getURL(String columnName) throws SQLException {
		checkClosed();
		return currentResultSet.getURL(columnName);
	}

	public void updateRef(int columnIndex, Ref x) throws SQLException {
		checkClosed();
		currentResultSet.updateRef(columnIndex, x);
	}

	public void updateRef(String columnName, Ref x) throws SQLException {
		checkClosed();
		currentResultSet.updateRef(columnName, x);
	}

	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		checkClosed();
		currentResultSet.updateBlob(columnIndex, x);
	}

	public void updateBlob(String columnName, Blob x) throws SQLException {
		checkClosed();
		currentResultSet.updateBlob(columnName, x);
	}

	public void updateClob(int columnIndex, Clob x) throws SQLException {
		checkClosed();
		currentResultSet.updateClob(columnIndex, x);
	}

	public void updateClob(String columnName, Clob x) throws SQLException {
		checkClosed();
		currentResultSet.updateClob(columnName, x);
	}

	public void updateArray(int columnIndex, Array x) throws SQLException {
		checkClosed();
		currentResultSet.updateArray(columnIndex, x);
	}

	public void updateArray(String columnName, Array x) throws SQLException {
		checkClosed();
		currentResultSet.updateArray(columnName, x);
	}
	
	/**
	 * 获取当前行数所在的结果集
	 * @param rowIndex
	 * @return
	 */
	public ResultSetData getResultSetData(int rowIndex){
		int currentRowCount=0;
		for (ResultSetData resultSetData : resultSetDataList) {
			int startRowIndex=currentRowCount+1;
			currentRowCount+=resultSetData.getRowCount();
			if(currentRowCount>=rowIndex){
				resultSetData.setStartRowIndex(startRowIndex);
				return resultSetData;
			}
		}
		return null;
	}

	class ResultSetData {
		private ResultSet resultSet;// 保存一个结果集
		private int resultIndex;
		private int rowCount;// 该结果集的行数
		private int startRowIndex;

		public ResultSetData(int resultIndex, ResultSet resultSet)
				throws SQLException {
			super();
			this.resultSet = resultSet;
			this.resultIndex = resultIndex;
		}

		public boolean next() throws SQLException {
			if (resultSet.next()) {
				rowCount++;
				return true;
			}
			return false;
		}

		public boolean absolute(int rowIndex) throws SQLException {
			if (!CollectionUtil.isEmpty(resultSetDataList)) {
				return resultSet.absolute(rowIndex - startRowIndex + 1);
			}
			return false;
		}

		public int getRowCount() {
			return rowCount;
		}

		public ResultSet getResultSet() {
			return resultSet;
		}


		public void setStartRowIndex(int startRowIndex) {
			this.startRowIndex = startRowIndex;
		}

		public int getResultIndex() {
			return resultIndex;
		}
		

	}

}
