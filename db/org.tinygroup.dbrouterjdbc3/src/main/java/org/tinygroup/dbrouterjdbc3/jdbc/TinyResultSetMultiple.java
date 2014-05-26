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
import java.sql.PreparedStatement;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.tinygroup.commons.tools.Assert;
import org.tinygroup.dbrouter.RouterManager;
import org.tinygroup.dbrouter.config.Partition;
import org.tinygroup.dbrouter.config.Router;
import org.tinygroup.dbrouter.config.Shard;
import org.tinygroup.dbrouter.factory.RouterManagerBeanFactory;
import org.tinygroup.dbrouter.util.OrderByProcessor.OrderByValues;
import org.tinygroup.dbrouter.util.ParamObjectBuilder;
import org.tinygroup.dbrouter.util.SortOrder;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 多个记录集合并的记录集 Created by luoguo on 13-12-13.
 */
public class TinyResultSetMultiple implements ResultSet {
	private final boolean scrollable;
	private final boolean updatable;
	/**
	 * 记录集列表
	 */
	private List<ResultSet> resultSets = new ArrayList<ResultSet>();

	private List<ResultSetExecutor> resultSetExecutors;
	/**
	 * 所属的集群
	 */
	private final Router router;

	private final String sql;
	/**
	 * 当前的记录集
	 */
	private ResultSet currentResultSet = null;
	/**
	 * 当前行号
	 */
	private int row = 0;// 当前行号,第一行行号为1
	/**
	 * 总行数
	 */
	private int totalRows = 0;// 总行数

	private SortOrder sortOrder;

	private boolean isClosed;

	private Logger logger = LoggerFactory
			.getLogger(TinyResultSetMultiple.class);

	private TinyStatement statement;

	private TinyConnection tinyConnection;
	/**
	 * 第一个原始的result，用于获取resultsetmetadata
	 */
	private ResultSetExecutor firstExecutor;

	private ParamObjectBuilder insertRow;

	private ParamObjectBuilder updateRow;

	private int columnCount;

	private ResultSetMetaData resultSetMetaData;

	private boolean isNext = true;
	
	private RouterManager routerManager = RouterManagerBeanFactory
	.getManager();

	public TinyResultSetMultiple(String sql, Router router,
			List<ResultSetExecutor> resultSetExecutors,
			TinyStatement statement, TinyConnection tinyConnection) {
		this.router = router;
		this.sql = sql;
		this.statement = statement;
		this.tinyConnection = tinyConnection;
		boolean scrollable = statement.resultSetType != ResultSet.TYPE_FORWARD_ONLY;
		boolean updatable = statement.resultSetConcurrency == ResultSet.CONCUR_UPDATABLE;
		this.scrollable = scrollable;
		this.updatable = updatable;
		Assert.assertNotNull(resultSetExecutors, "resultSets must not null");
		this.resultSetExecutors = resultSetExecutors;
		if (resultSetExecutors.size() > 0) {
			firstExecutor = resultSetExecutors.get(0);
			try {
				resultSetMetaData = firstExecutor.getResultSet().getMetaData();
				columnCount = resultSetMetaData.getColumnCount();
				for (int i = 0; i < resultSetExecutors.size(); i++) {
					ResultSetExecutor executor = resultSetExecutors.get(i);
					Shard shard=executor.getShard();
					Partition partition=executor.getPartition();
					Object[] params=statement.getPreparedParams();
					String realSql = routerManager.getSql(partition, shard, sql,params);
					String countSql = "select count(0) from ( " + realSql+" ) as tinycounttemp";
					Statement countStatement=statement.getNewStatement(countSql,shard);
					if(countStatement instanceof PreparedStatement){
						PreparedStatement preparedStatement=(PreparedStatement)countStatement;
						ResultSet countSet = preparedStatement.executeQuery();
						if (countSet.next()) {
							totalRows += countSet.getInt(1);
						}
					}else{
						ResultSet countSet = countStatement.executeQuery(countSql);
						if (countSet.next()) {
							totalRows += countSet.getInt(1);
						}
					}
					ResultSet resultSet = executor.getResultSet();
					resultSets.add(resultSet);
					if (sortOrder == null) {
						sortOrder = executor.getSortOrder();
					}
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		} else {
			throw new RuntimeException("must contain one resultset");
		}

	}

	public boolean next() throws SQLException {
		checkClosed();
		if (sortOrder != null) {
			detectNextStatus();
			List<OrderByValues> values = new ArrayList<OrderByValues>();
			for (ResultSetExecutor resultSetExecutor : resultSetExecutors) {
				OrderByValues value = resultSetExecutor.getValueCache();
				if (reacquireValue(value)) {
					if (resultSetExecutor.next()) {
						value = resultSetExecutor
								.getOrderByValuesFromResultSet();
						values.add(value);
						resultSetExecutor.setValueCache(value);
					} else {
						resultSetExecutor.setAfterLast(true);
					}
				} else {
					values.add(value);
				}
			}
			if (values.size() > 0) {
				Collections.sort(values, sortOrder);
				OrderByValues value = values.get(0);
				currentResultSet = value.getResultSet();
				value.clearValueCache();// 清除保存当前result结果集数据
				row++;
				return true;
			} else {
				if (row <= totalRows) {// 至多到after last
					row++;
				}
				return false;
			}
		} else {
			if (row <= 0) {
				row = 0;
			}
			for (ResultSetExecutor resultSetExecutor : resultSetExecutors) {
				if (resultSetExecutor.next()) {
					currentResultSet = resultSetExecutor.getResultSet();
					row++;
					return true;
				} else {
					resultSetExecutor.setAfterLast(true);
					if (row <= totalRows) {// 至多到after last
						row++;
					}
				}
			}
			return false;
		}
	}

	/**
	 * 
	 * 检测next方法的状态，如果之前没有调用previous方法，那么清楚currentResultSet对应的OrderByValues对象的排序值
	 * 。 反之，清除其他OrderByValues对象的排序值
	 * 
	 * @throws SQLException
	 */
	private void detectNextStatus() throws SQLException {
		if (currentResultSet != null) {
			if (isNext) {
				for (ResultSetExecutor resultSetExecutor : resultSetExecutors) {
					OrderByValues value = resultSetExecutor.getValueCache();
					if (value != null
							&& value.isCurrentResult(currentResultSet)) {
						value.clearValueCache();
					}
				}
			} else {
				for (ResultSetExecutor resultSetExecutor : resultSetExecutors) {
					OrderByValues value = resultSetExecutor.getValueCache();
					if (value != null
							&& !value.isCurrentResult(currentResultSet)) {
						value.clearValueCache();
					}
				}
			}
		}
		isNext = true;
	}

	private ResultSetExecutor getCurrentExecutor(ResultSet currentResultSet) {
		for (ResultSetExecutor executor : resultSetExecutors) {
			if (executor.getResultSet() == currentResultSet) {
				return executor;
			}
		}
		throw new RuntimeException("no exist executor with resultset");
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

	protected void checkClosed() throws SQLException {
		if (isClosed) {
			throw new SQLException("result is closed");
		}
	}

	private void checkUpdatable() throws SQLException {
		checkClosed();
		if (!updatable) {
			throw new SQLException("resultset is readonly can not update");
		}
	}

	private void checkColumnIndex(int columnIndex) throws SQLException {
		if (columnIndex < 1 || columnIndex > columnCount) {
			throw new SQLException("columnIndex is invalid");
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
			return new TinyResultSetMetaData(sql,
					currentResultSet.getMetaData());
		}
		if (resultSetMetaData != null) {
			return new TinyResultSetMetaData(sql, resultSetMetaData);
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
		return row > totalRows;
	}

	public boolean isFirst() throws SQLException {
		checkClosed();
		return row == 1;
	}

	public boolean isLast() throws SQLException {
		checkClosed();
		return row == totalRows;
	}

	public void beforeFirst() throws SQLException {
		checkClosed();
		for (ResultSetExecutor resultSetExecutor : resultSetExecutors) {
			resultSetExecutor.beforeFirst();
		}
		currentResultSet = null;
		row = 0;
	}

	public void afterLast() throws SQLException {
		checkClosed();
		for (ResultSetExecutor resultSetExecutor : resultSetExecutors) {
			resultSetExecutor.afterLast();
		}
		currentResultSet = null;
		row = totalRows + 1;
	}

	public boolean first() throws SQLException {
		checkClosed();
		for (ResultSetExecutor resultSetExecutor : resultSetExecutors) {
			resultSetExecutor.beforeFirst();
		}
		currentResultSet = null;
		next();// 获取下一个真正执行的resultset
		row = 1;// 重设row为1
		return true;
	}

	public boolean last() throws SQLException {
		checkClosed();
		for (ResultSetExecutor resultSetExecutor : resultSetExecutors) {
			resultSetExecutor.afterLast();
		}
		currentResultSet = null;
		previous();// 获取下一个真正执行的resultset
		row = totalRows;
		return true;
	}

	public int getRow() throws SQLException {
		checkClosed();
		return row;
	}

	/**
	 * rowNumber不能为0,rowNumber:-1移到最后一行.rowNumber:-2before the last row.
	 * rowNumber:大于总行数则移到 after the last row. rowNumber:少于当前行则往前移到row-rowNuber。
	 * rowNumber:大于当前行则往后移到row-rowNuber。
	 */
	public boolean absolute(int rowNumber) throws SQLException {
		checkClosed();
		if (rowNumber < 0) {
			rowNumber = totalRows + rowNumber + 1;
		} else if (rowNumber > totalRows + 1) {
			rowNumber = totalRows + 1;
		}
		if (rowNumber <= 0) {
			rowNumber = 0;
		}
		while (rowNumber < row) {
			previous();
		}
		while (row < rowNumber) {
			next();
		}
		this.row = rowNumber;
		return row > 0 && row <= totalRows;
	}

	public boolean relative(int rows) throws SQLException {
		checkClosed();
		int rowNumber = row + rows;
		if (rowNumber <= 0) {
			rowNumber = 1;
		} else if (rowNumber > totalRows) {
			rowNumber = totalRows;
		}
		return absolute(rowNumber);
	}

	public boolean previous() throws SQLException {
		checkClosed();
		if (sortOrder != null) {
			detectPreviousStatus();
			List<OrderByValues> values = new ArrayList<OrderByValues>();
			for (ResultSetExecutor resultSetExecutor : resultSetExecutors) {
				OrderByValues value = resultSetExecutor.getValueCache();
				if (reacquireValue(value)) {
					if (resultSetExecutor.previous()) {
						value = resultSetExecutor
								.getOrderByValuesFromResultSet();
						values.add(value);
						resultSetExecutor.setValueCache(value);
					} else {
						resultSetExecutor.setBeforeFirst(true);
					}
				} else {
					values.add(value);
				}
			}
			if (values.size() > 0) {
				Collections.sort(values, sortOrder);
				OrderByValues value = values.get(values.size() - 1);// 获取最后一个
				currentResultSet = value.getResultSet();
				value.clearValueCache();// 清除保存当前result结果集数据
				row--;
				return true;
			} else {
				if (row >= 0) {
					row--;
				}
				return false;
			}
		} else {
			if (row >= totalRows) {
				row = totalRows;
			}
			for (int i = resultSetExecutors.size() - 1; i >= 0; i--) {
				ResultSetExecutor resultSetExecutor = resultSetExecutors.get(i);
				if (resultSetExecutor.previous()) {
					currentResultSet = resultSetExecutor.getResultSet();
					row--;
					return true;
				} else {
					resultSetExecutor.setBeforeFirst(true);
					if (row >= 0) {
						row--;
					}
				}
			}
			return false;
		}

	}

	private void detectPreviousStatus() throws SQLException {
		if (currentResultSet != null) {
			if (!isNext) {
				for (ResultSetExecutor resultSetExecutor : resultSetExecutors) {
					OrderByValues value = resultSetExecutor.getValueCache();
					if (value != null
							&& value.isCurrentResult(currentResultSet)) {
						value.clearValueCache();
					}
				}
			} else {
				for (ResultSetExecutor resultSetExecutor : resultSetExecutors) {
					OrderByValues value = resultSetExecutor.getValueCache();
					if (value != null
							&& !value.isCurrentResult(currentResultSet)) {
						value.clearValueCache();
					}
				}
			}
		}
		isNext = false;
	}

	private boolean reacquireValue(OrderByValues value) {
		return value == null || value.getValues() == null;
	}

	public void setFetchDirection(int direction) throws SQLException {
		throw new SQLException("not support setFetchDirection");
	}

	public int getFetchDirection() throws SQLException {
		checkClosed();
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
		return statement.resultSetType;
	}

	public int getConcurrency() throws SQLException {
		checkClosed();
		return statement.resultSetConcurrency;
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
		updateValue(columnIndex, null);
	}

	private void updateValue(int columnIndex, Object value) throws SQLException {
		checkUpdatable();
		checkColumnIndex(columnIndex);
		if (insertRow != null) {
			insertRow.addParamterObject(columnIndex, value);
		} else {
			if (updateRow == null) {
				updateRow = new ParamObjectBuilder(columnCount);
			}
			updateRow.addParamterObject(columnIndex, value);
		}

	}

	private void updateValue(String columnLabel, Object value)
			throws SQLException {
		int columnIndex = getColumnIndex(columnLabel);
		updateValue(columnIndex, value);
	}

	private void updateStreamValue(String columnLabel, InputStream value,
			int length, boolean asciiStream) throws SQLException {
		int columnIndex = getColumnIndex(columnLabel);
		updateStreamValue(columnIndex, value, length, asciiStream);
	}

	private void updateReaderValue(String columnLabel, Reader value, int length)
			throws SQLException {
		int columnIndex = getColumnIndex(columnLabel);
		updateReadValue(columnIndex, value, length);
	}

	private int getColumnIndex(String columnLabel) throws SQLException {
		for (int i = 1; i <= columnCount; i++) {
			String columnName = resultSetMetaData.getColumnName(i);
			if (columnName.equalsIgnoreCase(columnLabel)) {
				return i;
			}
			String columnLabelName = resultSetMetaData.getColumnLabel(i);
			if (columnLabelName.equalsIgnoreCase(columnLabel)) {
				return i;
			}
		}
		throw new SQLException("non-existing column:" + columnLabel);
	}

	private void updateStreamValue(int columnIndex, InputStream value,
			int length, boolean asciiStream) throws SQLException {
		checkUpdatable();
		checkColumnIndex(columnIndex);
		if (insertRow != null) {
			insertRow.addInputStreamParamterObject(columnIndex, value, length,
					asciiStream);
		} else {
			if (updateRow == null) {
				updateRow = new ParamObjectBuilder(columnCount);
			}
			updateRow.addInputStreamParamterObject(columnIndex, value, length,
					asciiStream);
		}
	}

	private void updateReadValue(int columnIndex, Reader value, int length)
			throws SQLException {
		checkUpdatable();
		checkColumnIndex(columnIndex);
		if (insertRow != null) {
			insertRow.addReaderParamterObject(columnIndex, value, length);
		} else {
			if (updateRow == null) {
				updateRow = new ParamObjectBuilder(columnCount);
			}
			updateRow.addReaderParamterObject(columnIndex, value, length);
		}
	}

	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
		updateValue(columnIndex, x);
	}

	public void updateByte(int columnIndex, byte x) throws SQLException {
		updateValue(columnIndex, x);
	}

	public void updateShort(int columnIndex, short x) throws SQLException {
		updateValue(columnIndex, x);
	}

	public void updateInt(int columnIndex, int x) throws SQLException {
		updateValue(columnIndex, x);
	}

	public void updateLong(int columnIndex, long x) throws SQLException {
		updateValue(columnIndex, x);
	}

	public void updateFloat(int columnIndex, float x) throws SQLException {
		updateValue(columnIndex, x);
	}

	public void updateDouble(int columnIndex, double x) throws SQLException {
		updateValue(columnIndex, x);
	}

	public void updateBigDecimal(int columnIndex, BigDecimal x)
			throws SQLException {
		updateValue(columnIndex, x);
	}

	public void updateString(int columnIndex, String x) throws SQLException {
		updateValue(columnIndex, x);
	}

	public void updateBytes(int columnIndex, byte[] x) throws SQLException {
		updateValue(columnIndex, x);
	}

	public void updateDate(int columnIndex, Date x) throws SQLException {
		updateValue(columnIndex, x);
	}

	public void updateTime(int columnIndex, Time x) throws SQLException {
		updateValue(columnIndex, x);
	}

	public void updateTimestamp(int columnIndex, Timestamp x)
			throws SQLException {
		updateValue(columnIndex, x);
	}

	public void updateAsciiStream(int columnIndex, InputStream x, int length)
			throws SQLException {
		updateStreamValue(columnIndex, x, length, true);
	}

	public void updateBinaryStream(int columnIndex, InputStream x, int length)
			throws SQLException {
		updateStreamValue(columnIndex, x, length, false);
	}

	public void updateCharacterStream(int columnIndex, Reader x, int length)
			throws SQLException {
		updateReadValue(columnIndex, x, length);
	}

	public void updateObject(int columnIndex, Object x, int scale)
			throws SQLException {
		updateValue(columnIndex, x);
	}

	public void updateObject(int columnIndex, Object x) throws SQLException {
		updateValue(columnIndex, x);
	}

	public void updateNull(String columnName) throws SQLException {
		checkClosed();
		updateValue(columnName, null);
	}

	public void updateBoolean(String columnName, boolean x) throws SQLException {
		updateValue(columnName, x);
	}

	public void updateByte(String columnName, byte x) throws SQLException {
		updateValue(columnName, x);
	}

	public void updateShort(String columnName, short x) throws SQLException {
		updateValue(columnName, x);
	}

	public void updateInt(String columnName, int x) throws SQLException {
		updateValue(columnName, x);
	}

	public void updateLong(String columnName, long x) throws SQLException {
		updateValue(columnName, x);
	}

	public void updateFloat(String columnName, float x) throws SQLException {
		updateValue(columnName, x);
	}

	public void updateDouble(String columnName, double x) throws SQLException {
		updateValue(columnName, x);
	}

	public void updateBigDecimal(String columnName, BigDecimal x)
			throws SQLException {
		updateValue(columnName, x);
	}

	public void updateString(String columnName, String x) throws SQLException {
		updateValue(columnName, x);
	}

	public void updateBytes(String columnName, byte[] x) throws SQLException {
		updateValue(columnName, x);
	}

	public void updateDate(String columnName, Date x) throws SQLException {
		updateValue(columnName, x);
	}

	public void updateTime(String columnName, Time x) throws SQLException {
		updateValue(columnName, x);
	}

	public void updateTimestamp(String columnName, Timestamp x)
			throws SQLException {
		updateValue(columnName, x);
	}

	public void updateAsciiStream(String columnName, InputStream x, int length)
			throws SQLException {
		updateStreamValue(columnName, x, length, true);
	}

	public void updateBinaryStream(String columnName, InputStream x, int length)
			throws SQLException {
		updateStreamValue(columnName, x, length, false);
	}

	public void updateCharacterStream(String columnName, Reader reader,
			int length) throws SQLException {
		updateReaderValue(columnName, reader, length);
	}

	public void updateObject(String columnName, Object x, int scale)
			throws SQLException {
		updateValue(columnName, x);
	}

	public void updateObject(String columnName, Object x) throws SQLException {
		updateValue(columnName, x);
	}

	private UpdateableRow getUpdateableRow(ResultSetExecutor executor)
			throws SQLException {
		return new UpdateableRow(tinyConnection, executor.getShard(),
				executor.getResultSet());
	}

	public void insertRow() throws SQLException {
		checkUpdatable();
		getUpdateableRow(firstExecutor)
				.insertRow(insertRow.getPreparedParams());
		insertRow = null;
	}

	public void updateRow() throws SQLException {
		checkUpdatable();
		if (insertRow != null) {
			throw new SQLException("not on update row");
		}
		if (currentResultSet == null) {
			throw new SQLException("not locate row");
		}
		if (updateRow != null) {
			ResultSetExecutor executor = getCurrentExecutor(currentResultSet);
			UpdateableRow row = getUpdateableRow(executor);
			Object[] current = new Object[columnCount];
			Object[] updateValues = updateRow.getPreparedParams();
			for (int i = 0; i < updateValues.length; i++) {
				current[i] = currentResultSet.getObject(i + 1);
			}
			row.updateRow(current, updateValues);
			// 内部查询一次赋值给currentResultSet
			currentResultSet = row.readRow(current);
			updateRow = null;
		}
	}

	public void deleteRow() throws SQLException {
		checkUpdatable();
		if (insertRow != null) {
			throw new SQLException("not on update row");
		}
		if (currentResultSet == null) {
			throw new SQLException("not locate row");
		}
		Object[] current = new Object[columnCount];
		for (int i = 0; i < columnCount; i++) {
			current[i] = currentResultSet.getObject(i + 1);
		}
		getUpdateableRow(getCurrentExecutor(currentResultSet)).deleteRow(
				current);
		updateRow = null;
	}

	public void refreshRow() throws SQLException {
		checkClosed();
		if (insertRow != null) {
			throw new SQLException("not on update row");
		}
		updateRow = null;
	}

	public void cancelRowUpdates() throws SQLException {
		checkClosed();
		if (insertRow != null) {
			throw new SQLException("not on update row");
		}
		updateRow = null;
	}

	public void moveToInsertRow() throws SQLException {
		checkUpdatable();
		insertRow = new ParamObjectBuilder(columnCount);
	}

	public void moveToCurrentRow() throws SQLException {
		checkUpdatable();
		insertRow = null;
	}

	public Statement getStatement() throws SQLException {
		checkClosed();
		return statement;
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
		updateValue(columnIndex, x);
	}

	public void updateRef(String columnName, Ref x) throws SQLException {
		updateValue(columnName, x);
	}

	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		updateValue(columnIndex, x);
	}

	public void updateBlob(String columnName, Blob x) throws SQLException {
		updateValue(columnName, x);
	}

	public void updateClob(int columnIndex, Clob x) throws SQLException {
		updateValue(columnIndex, x);
	}

	public void updateClob(String columnName, Clob x) throws SQLException {
		updateValue(columnName, x);
	}

	public void updateArray(int columnIndex, Array x) throws SQLException {
		updateValue(columnIndex, x);
	}

	public void updateArray(String columnName, Array x) throws SQLException {
		updateValue(columnName, x);
	}

}
