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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
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
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.tinygroup.dbrouter.value.DataType;

/**
 * This class is a simple result set and meta data implementation. It can be
 * used in Java functions that return a result set. Only the most basic methods
 * are implemented, the others throw an exception. This implementation is
 * standalone, and only relies on standard classes. It can be extended easily if
 * required.
 * 
 * An application can create a result set using the following code:
 * 
 * <pre>
 * TinyResultSetSimple rs = new TinyResultSetSimple();
 * rs.addColumn(&quot;ID&quot;, Types.INTEGER, 10, 0);
 * rs.addColumn(&quot;NAME&quot;, Types.VARCHAR, 255, 0);
 * rs.addRow(0, &quot;Hello&quot; });
 * rs.addRow(1, &quot;World&quot; });
 * </pre>
 * 
 */
public class TinyResultSetSimple implements ResultSet, ResultSetMetaData {

	private ArrayList<Object[]> rows;
	private Object[] currentRow;
	private int rowId = -1;
	private boolean wasNull;
	private ArrayList<Column> columns = new ArrayList<TinyResultSetSimple.Column>();
	private boolean isClosed = false;

	/**
	 * This class holds the data of a result column.
	 */
	static class Column {
		/**
		 * 列号，比如1、2、3。。。。
		 */
		int columnIndex;

		/**
		 * The column label.
		 */
		String name;

		/**
		 * The SQL type.
		 */
		int sqlType;

		/**
		 * The precision.
		 */
		int precision;

		/**
		 * The scale.
		 */
		int scale;
	}

	/**
	 * A simple array implementation, backed by an object array
	 */
	public static class SimpleArray implements Array {

		private final Object[] value;

		SimpleArray(Object[] value) {
			this.value=new Object[value.length];
			System.arraycopy(value, 0, this.value, 0, value.length);
		}

		/**
		 * Get the object array.
		 * 
		 * @return the object array
		 */
		public Object getArray() {
			return value;
		}

		/**
		 * INTERNAL
		 */
		public Object getArray(Map<String, Class<?>> map) throws SQLException {
			throw getUnsupportedException();
		}

		/**
		 * INTERNAL
		 */
		public Object getArray(long index, int count) throws SQLException {
			throw getUnsupportedException();
		}

		/**
		 * INTERNAL
		 */
		public Object getArray(long index, int count, Map<String, Class<?>> map)
				throws SQLException {
			throw getUnsupportedException();
		}

		/**
		 * Get the base type of this array.
		 * 
		 * @return Types.NULL
		 */
		public int getBaseType() {
			return Types.NULL;
		}

		/**
		 * Get the base type name of this array.
		 * 
		 * @return "NULL"
		 */
		public String getBaseTypeName() {
			return "NULL";
		}

		/**
		 * INTERNAL
		 */
		public ResultSet getResultSet() throws SQLException {
			throw getUnsupportedException();
		}

		/**
		 * INTERNAL
		 */
		public ResultSet getResultSet(Map<String, Class<?>> map)
				throws SQLException {
			throw getUnsupportedException();
		}

		/**
		 * INTERNAL
		 */
		public ResultSet getResultSet(long index, int count)
				throws SQLException {
			throw getUnsupportedException();
		}

		/**
		 * INTERNAL
		 */
		public ResultSet getResultSet(long index, int count,
				Map<String, Class<?>> map) throws SQLException {
			throw getUnsupportedException();
		}

		/**
		 * INTERNAL
		 */

		public void free() {
			// nothing to do
		}

	}

	/**
	 * This constructor is used if the result set is later populated with
	 * addRow.
	 */
	public TinyResultSetSimple() {
		rows = new ArrayList<Object[]>();
	}

	public TinyResultSetSimple(ResultSet resultSet) throws SQLException {
		ResultSetMetaData metaData = resultSet.getMetaData();
		int columnCount = metaData.getColumnCount();
		for (int index = 1; index <= columnCount; index++) {
			addColumn(index, metaData.getColumnName(index),
					metaData.getColumnType(index),
					metaData.getPrecision(index), metaData.getScale(index));
		}
		rows = new ArrayList<Object[]>();
		while (resultSet.next()) {
			Object[] values = new Object[columnCount];
			for (int index = 0; index < columnCount; index++) {
				values[index] = resultSet.getObject(index + 1);
			}
			addRow(values);
		}
	}

	public TinyResultSetSimple(List<ResultSet> resultSets) throws SQLException {
		if (resultSets != null && resultSets.size() > 0) {
			ResultSetMetaData metaData = resultSets.get(0).getMetaData();
			int columnCount = metaData.getColumnCount();
			for (int index = 1; index <= columnCount; index++) {
				addColumn(index, metaData.getColumnName(index),
						metaData.getColumnType(index),
						metaData.getPrecision(index), metaData.getScale(index));
			}
			rows = new ArrayList<Object[]>();
			for (ResultSet resultSet : resultSets) {
				while (resultSet.next()) {
					Object[] values = new Object[columnCount];
					for (int index = 0; index < columnCount; index++) {
						values[index] = resultSet.getObject(index + 1);
					}
					addRow(values);
				}
			}
		}
	}

	/**
	 * Adds a column to the result set. All columns must be added before adding
	 * rows.
	 * 
	 * @param index
	 *            the index of the column
	 * @param name
	 *            null is replaced with C1, C2,...
	 * @param sqlType
	 *            the value returned in getColumnType(..) (ignored internally)
	 * @param precision
	 *            the precision
	 * @param scale
	 *            the scale
	 */
	public void addColumn(int index, String name, int sqlType, int precision,
			int scale) {
		checkClosed();
		if (rows != null && rows.size() > 0) {
			throw new IllegalStateException(
					"Cannot add a column after adding rows");
		}
		if (index <= 0) {
			throw new IllegalStateException("a column index must great than 0");
		}
		if (name == null) {
			name = "C" + (columns.size() + 1);
		}
		Column column = new Column();
		column.columnIndex = index;
		column.name = name;
		column.sqlType = sqlType;
		column.precision = precision;
		column.scale = scale;
		columns.add(column);
	}

	/**
	 * Add a new row to the result set. Do not use this method when using a
	 * RowSource.
	 * 
	 * @param row
	 *            the row as an array of objects
	 */
	public void addRow(Object... row) {
		checkClosed();
		if (rows == null) {
			throw new IllegalStateException(
					"Cannot add a row when using RowSource");
		}
		rows.add(row);
	}

	public void updateRow(int columnIndex, Object value) {
		checkClosed();
		if (currentRow != null) {
			currentRow[columnIndex - 1] = value;
		}
	}

	public void updateRow(String columnName, Object value) throws SQLException {
		int index = findColumn(columnName);
		updateRow(index, value);
	}

	/**
	 * 删除当前行
	 */
	public void removeRow() {
		checkClosed();
		if (currentRow != null) {
			rows.remove(currentRow);
			rowId--;
		} else {
			throw new IllegalStateException("the operate must after next");
		}
	}

	/**
	 * Returns ResultSet.CONCUR_READ_ONLY.
	 * 
	 * @return CONCUR_READ_ONLY
	 */

	public int getConcurrency() {
		checkClosed();
		return ResultSet.CONCUR_READ_ONLY;
	}

	/**
	 * Returns ResultSet.FETCH_FORWARD.
	 * 
	 * @return FETCH_FORWARD
	 */

	public int getFetchDirection() {
		checkClosed();
		return ResultSet.FETCH_FORWARD;
	}

	/**
	 * Returns 0.
	 * 
	 * @return 0
	 */

	public int getFetchSize() {
		checkClosed();
		return 0;
	}

	/**
	 * Returns the row number (1, 2,...) or 0 for no row.
	 * 
	 * @return 0
	 */

	public int getRow() {
		checkClosed();
		return rowId + 1;
	}

	/**
	 * Returns ResultSet.TYPE_FORWARD_ONLY.
	 * 
	 * @return TYPE_FORWARD_ONLY
	 */

	public int getType() {
		checkClosed();
		return ResultSet.TYPE_FORWARD_ONLY;
	}

	/**
	 * Closes the result set and releases the resources.
	 */

	public void close() {
		currentRow = null;
		rows = null;
		columns = null;
		rowId = -1;
		isClosed = true;
	}

	protected void checkClosed() {
		if (isClosed) {
			throw new RuntimeException("result is closed");
		}
	}

	/**
	 * Moves the cursor to the next row of the result set.
	 * 
	 * @return true if successful, false if there are no more rows
	 */

	public boolean next() throws SQLException {
		checkClosed();
		if (rows != null && rowId < rows.size()) {
			rowId++;
			if (rowId < rows.size()) {
				currentRow = rows.get(rowId);
				return true;
			}
		}
		return false;
	}

	/**
	 * Moves the current position to before the first row, that means resets the
	 * result set.
	 */

	public void beforeFirst() throws SQLException {
		checkClosed();
		rowId = -1;
	}

	/**
	 * Returns whether the last column accessed was null.
	 * 
	 * @return true if the last column accessed was null
	 */

	public boolean wasNull() {
		checkClosed();
		return wasNull;
	}

	/**
	 * Searches for a specific column in the result set. A case-insensitive
	 * search is made.
	 * 
	 * @param columnLabel
	 *            the column label
	 * @return the column index (1,2,...)
	 * @throws SQLException
	 *             if the column is not found or if the result set is closed
	 */

	public int findColumn(String columnLabel) throws SQLException {
		checkClosed();
		if (columnLabel != null && columns != null) {
			for (int i = 0, size = columns.size(); i < size; i++) {
				if (columnLabel.equalsIgnoreCase(getColumn(i).name)) {
					return i + 1;
				}
			}
		}
		throw new SQLException(String.format("non-existing column:%s",
				columnLabel));
	}

	/**
	 * Returns a reference to itself.
	 * 
	 * @return this
	 */

	public ResultSetMetaData getMetaData() {
		checkClosed();
		return this;
	}

	/**
	 * Returns null.
	 * 
	 * @return null
	 */

	public SQLWarning getWarnings() {
		checkClosed();
		return null;
	}

	/**
	 * Returns null.
	 * 
	 * @return null
	 */

	public Statement getStatement() {
		checkClosed();
		return null;
	}

	/**
	 * INTERNAL
	 */

	public void clearWarnings() {
		checkClosed();
		// nothing to do
	}

	// ---- get ---------------------------------------------

	/**
	 * Returns the value as a java.sql.Array.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return the value
	 */

	public Array getArray(int columnIndex) throws SQLException {
		Object[] o = (Object[]) get(columnIndex);
		return o == null ? null : new SimpleArray(o);
	}

	/**
	 * Returns the value as a java.sql.Array.
	 * 
	 * @param columnLabel
	 *            the column label
	 * @return the value
	 */

	public Array getArray(String columnLabel) throws SQLException {
		return getArray(findColumn(columnLabel));
	}

	/**
	 * INTERNAL
	 */

	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		throw getUnsupportedException();
	}

	/**
	 * INTERNAL
	 */

	public InputStream getAsciiStream(String columnLabel) throws SQLException {
		throw getUnsupportedException();
	}

	/**
	 * Returns the value as a java.math.BigDecimal.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return the value
	 */

	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		Object o = get(columnIndex);
		if (o != null && !(o instanceof BigDecimal)) {
			o = new BigDecimal(o.toString());
		}
		return (BigDecimal) o;
	}

	/**
	 * Returns the value as a java.math.BigDecimal.
	 * 
	 * @param columnLabel
	 *            the column label
	 * @return the value
	 */

	public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
		return getBigDecimal(findColumn(columnLabel));
	}

	/**
	 * @deprecated INTERNAL
	 */

	public BigDecimal getBigDecimal(int columnIndex, int scale)
			throws SQLException {
		throw getUnsupportedException();
	}

	/**
	 * @deprecated INTERNAL
	 */

	public BigDecimal getBigDecimal(String columnLabel, int scale)
			throws SQLException {
		throw getUnsupportedException();
	}

	/**
	 * Returns the value as a java.io.InputStream.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return the value
	 */

	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		return asInputStream(get(columnIndex));
	}

	private static InputStream asInputStream(Object o) throws SQLException {
		if (o == null) {
			return null;
		} else if (o instanceof Blob) {
			return ((Blob) o).getBinaryStream();
		}
		return (InputStream) o;
	}

	/**
	 * Returns the value as a java.io.InputStream.
	 * 
	 * @param columnLabel
	 *            the column label
	 * @return the value
	 */

	public InputStream getBinaryStream(String columnLabel) throws SQLException {
		return getBinaryStream(findColumn(columnLabel));
	}

	/**
	 * Returns the value as a java.sql.Blob. This is only supported if the
	 * result set was created using a Blob object.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return the value
	 */

	public Blob getBlob(int columnIndex) throws SQLException {
		return (Blob) get(columnIndex);
	}

	/**
	 * Returns the value as a java.sql.Blob. This is only supported if the
	 * result set was created using a Blob object.
	 * 
	 * @param columnLabel
	 *            the column label
	 * @return the value
	 */

	public Blob getBlob(String columnLabel) throws SQLException {
		return getBlob(findColumn(columnLabel));
	}

	/**
	 * Returns the value as a boolean.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return the value
	 */

	public boolean getBoolean(int columnIndex) throws SQLException {
		Object o = get(columnIndex);
		if (o != null && !(o instanceof Boolean)) {
			o = Boolean.valueOf(o.toString());
		}
		return o == null ? false : ((Boolean) o).booleanValue();
	}

	/**
	 * Returns the value as a boolean.
	 * 
	 * @param columnLabel
	 *            the column label
	 * @return the value
	 */

	public boolean getBoolean(String columnLabel) throws SQLException {
		return getBoolean(findColumn(columnLabel));
	}

	/**
	 * Returns the value as a byte.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return the value
	 */

	public byte getByte(int columnIndex) throws SQLException {
		Object o = get(columnIndex);
		if (o != null && !(o instanceof Number)) {
			o = Byte.decode(o.toString());
		}
		return o == null ? 0 : ((Number) o).byteValue();
	}

	/**
	 * Returns the value as a byte.
	 * 
	 * @param columnLabel
	 *            the column label
	 * @return the value
	 */

	public byte getByte(String columnLabel) throws SQLException {
		return getByte(findColumn(columnLabel));
	}

	/**
	 * Returns the value as a byte array.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return the value
	 */

	public byte[] getBytes(int columnIndex) throws SQLException {
		Object o = get(columnIndex);
		if (o == null || o instanceof byte[]) {
			return (byte[]) o;
		}
		try {
			return serializer(o);
		} catch (IOException e) {
			throw new RuntimeException("an object could not be serialized", e);
		}
	}

	private byte[] serializer(Object o) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(out);
		os.writeObject(o);
		return out.toByteArray();
	}

	/**
	 * Returns the value as a byte array.
	 * 
	 * @param columnLabel
	 *            the column label
	 * @return the value
	 */

	public byte[] getBytes(String columnLabel) throws SQLException {
		return getBytes(findColumn(columnLabel));
	}

	/**
	 * Returns the value as a java.io.Reader. This is only supported if the
	 * result set was created using a Clob or Reader object.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return the value
	 */

	public Reader getCharacterStream(int columnIndex) throws SQLException {
		return asReader(get(columnIndex));
	}

	private static Reader asReader(Object o) throws SQLException {
		if (o == null) {
			return null;
		} else if (o instanceof Clob) {
			return ((Clob) o).getCharacterStream();
		}
		return (Reader) o;
	}

	/**
	 * Returns the value as a java.io.Reader. This is only supported if the
	 * result set was created using a Clob or Reader object.
	 * 
	 * @param columnLabel
	 *            the column label
	 * @return the value
	 */

	public Reader getCharacterStream(String columnLabel) throws SQLException {
		return getCharacterStream(findColumn(columnLabel));
	}

	/**
	 * Returns the value as a java.sql.Clob. This is only supported if the
	 * result set was created using a Clob object.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return the value
	 */

	public Clob getClob(int columnIndex) throws SQLException {
		Clob c = (Clob) get(columnIndex);
		return c == null ? null : c;
	}

	/**
	 * Returns the value as a java.sql.Clob. This is only supported if the
	 * result set was created using a Clob object.
	 * 
	 * @param columnLabel
	 *            the column label
	 * @return the value
	 */

	public Clob getClob(String columnLabel) throws SQLException {
		return getClob(findColumn(columnLabel));
	}

	/**
	 * Returns the value as an java.sql.Date.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return the value
	 */

	public Date getDate(int columnIndex) throws SQLException {
		return (Date) get(columnIndex);
	}

	/**
	 * Returns the value as a java.sql.Date.
	 * 
	 * @param columnLabel
	 *            the column label
	 * @return the value
	 */

	public Date getDate(String columnLabel) throws SQLException {
		return getDate(findColumn(columnLabel));
	}

	/**
	 * INTERNAL
	 */

	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		throw getUnsupportedException();
	}

	/**
	 * INTERNAL
	 */

	public Date getDate(String columnLabel, Calendar cal) throws SQLException {
		throw getUnsupportedException();
	}

	/**
	 * Returns the value as an double.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return the value
	 */

	public double getDouble(int columnIndex) throws SQLException {
		Object o = get(columnIndex);
		if (o != null && !(o instanceof Number)) {
			return Double.parseDouble(o.toString());
		}
		return o == null ? 0 : ((Number) o).doubleValue();
	}

	/**
	 * Returns the value as a double.
	 * 
	 * @param columnLabel
	 *            the column label
	 * @return the value
	 */

	public double getDouble(String columnLabel) throws SQLException {
		return getDouble(findColumn(columnLabel));
	}

	/**
	 * Returns the value as a float.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return the value
	 */

	public float getFloat(int columnIndex) throws SQLException {
		Object o = get(columnIndex);
		if (o != null && !(o instanceof Number)) {
			return Float.parseFloat(o.toString());
		}
		return o == null ? 0 : ((Number) o).floatValue();
	}

	/**
	 * Returns the value as a float.
	 * 
	 * @param columnLabel
	 *            the column label
	 * @return the value
	 */

	public float getFloat(String columnLabel) throws SQLException {
		return getFloat(findColumn(columnLabel));
	}

	/**
	 * Returns the value as an int.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return the value
	 */

	public int getInt(int columnIndex) throws SQLException {
		Object o = get(columnIndex);
		if (o != null && !(o instanceof Number)) {
			o = Integer.decode(o.toString());
		}
		return o == null ? 0 : ((Number) o).intValue();
	}

	/**
	 * Returns the value as an int.
	 * 
	 * @param columnLabel
	 *            the column label
	 * @return the value
	 */

	public int getInt(String columnLabel) throws SQLException {
		return getInt(findColumn(columnLabel));
	}

	/**
	 * Returns the value as a long.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return the value
	 */

	public long getLong(int columnIndex) throws SQLException {
		Object o = get(columnIndex);
		if (o != null && !(o instanceof Number)) {
			o = Long.decode(o.toString());
		}
		return o == null ? 0 : ((Number) o).longValue();
	}

	/**
	 * Returns the value as a long.
	 * 
	 * @param columnLabel
	 *            the column label
	 * @return the value
	 */

	public long getLong(String columnLabel) throws SQLException {
		return getLong(findColumn(columnLabel));
	}

	/**
	 * INTERNAL
	 */

	public Reader getNCharacterStream(int columnIndex) throws SQLException {
		throw getUnsupportedException();
	}

	/**
	 * INTERNAL
	 */

	public Reader getNCharacterStream(String columnLabel) throws SQLException {
		throw getUnsupportedException();
	}

	/**
	 * INTERNAL
	 */

	public String getNString(int columnIndex) throws SQLException {
		return getString(columnIndex);
	}

	/**
	 * INTERNAL
	 */

	public String getNString(String columnLabel) throws SQLException {
		return getString(columnLabel);
	}

	/**
	 * Returns the value as an Object.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return the value
	 */

	public Object getObject(int columnIndex) throws SQLException {
		return get(columnIndex);
	}

	/**
	 * Returns the value as an Object.
	 * 
	 * @param columnLabel
	 *            the column label
	 * @return the value
	 */

	public Object getObject(String columnLabel) throws SQLException {
		return getObject(findColumn(columnLabel));
	}

	/**
	 * INTERNAL
	 * 
	 * @param columnIndex
	 *            the column index (1, 2, ...)
	 * @param type
	 *            the class of the returned value
	 */
	/*
	 * ## Java 1.7 ##
	 * 
	 * public <T> T getObject(int columnIndex, Class<T> type) { return null; }
	 * //
	 */

	/**
	 * INTERNAL
	 * 
	 * @param columnName
	 *            the column name
	 * @param type
	 *            the class of the returned value
	 */
	/*
	 * ## Java 1.7 ##
	 * 
	 * public <T> T getObject(String columnName, Class<T> type) { return null; }
	 * //
	 */

	/**
	 * INTERNAL
	 */

	public Object getObject(int columnIndex, Map<String, Class<?>> map)
			throws SQLException {
		throw getUnsupportedException();
	}

	/**
	 * INTERNAL
	 */

	public Object getObject(String columnLabel, Map<String, Class<?>> map)
			throws SQLException {
		throw getUnsupportedException();
	}

	/**
	 * INTERNAL
	 */

	public Ref getRef(int columnIndex) throws SQLException {
		throw getUnsupportedException();
	}

	/**
	 * INTERNAL
	 */

	public Ref getRef(String columnLabel) throws SQLException {
		throw getUnsupportedException();
	}

	/**
	 * Returns the value as a short.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return the value
	 */

	public short getShort(int columnIndex) throws SQLException {
		Object o = get(columnIndex);
		if (o != null && !(o instanceof Number)) {
			o = Short.decode(o.toString());
		}
		return o == null ? 0 : ((Number) o).shortValue();
	}

	/**
	 * Returns the value as a short.
	 * 
	 * @param columnLabel
	 *            the column label
	 * @return the value
	 */

	public short getShort(String columnLabel) throws SQLException {
		return getShort(findColumn(columnLabel));
	}

	/**
	 * Returns the value as a String.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return the value
	 */

	public String getString(int columnIndex) throws SQLException {
		Object o = get(columnIndex);
		if (o == null) {
			return null;
		}
		switch (columns.get(columnIndex - 1).sqlType) {
		case Types.CLOB:
			Clob c = (Clob) o;
			return c.getSubString(1, convertLongToInt(c.length()));
		}
		return o.toString();
	}

	/**
	 * Convert a long value to an int value. Values larger than the biggest int
	 * value is converted to the biggest int value, and values smaller than the
	 * smallest int value are converted to the smallest int value.
	 * 
	 * @param l
	 *            the value to convert
	 * @return the converted int value
	 */
	public int convertLongToInt(long l) {
		if (l <= Integer.MIN_VALUE) {
			return Integer.MIN_VALUE;
		} else if (l >= Integer.MAX_VALUE) {
			return Integer.MAX_VALUE;
		} else {
			return (int) l;
		}
	}

	/**
	 * Returns the value as a String.
	 * 
	 * @param columnLabel
	 *            the column label
	 * @return the value
	 */

	public String getString(String columnLabel) throws SQLException {
		return getString(findColumn(columnLabel));
	}

	/**
	 * Returns the value as an java.sql.Time.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return the value
	 */

	public Time getTime(int columnIndex) throws SQLException {
		return (Time) get(columnIndex);
	}

	/**
	 * Returns the value as a java.sql.Time.
	 * 
	 * @param columnLabel
	 *            the column label
	 * @return the value
	 */

	public Time getTime(String columnLabel) throws SQLException {
		return getTime(findColumn(columnLabel));
	}

	/**
	 * INTERNAL
	 */

	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		throw getUnsupportedException();
	}

	/**
	 * INTERNAL
	 */

	public Time getTime(String columnLabel, Calendar cal) throws SQLException {
		throw getUnsupportedException();
	}

	/**
	 * Returns the value as an java.sql.Timestamp.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return the value
	 */

	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		return (Timestamp) get(columnIndex);
	}

	/**
	 * Returns the value as a java.sql.Timestamp.
	 * 
	 * @param columnLabel
	 *            the column label
	 * @return the value
	 */

	public Timestamp getTimestamp(String columnLabel) throws SQLException {
		return getTimestamp(findColumn(columnLabel));
	}

	/**
	 * INTERNAL
	 */

	public Timestamp getTimestamp(int columnIndex, Calendar cal)
			throws SQLException {
		throw getUnsupportedException();
	}

	/**
	 * INTERNAL
	 */

	public Timestamp getTimestamp(String columnLabel, Calendar cal)
			throws SQLException {
		throw getUnsupportedException();
	}

	/**
	 * @deprecated INTERNAL
	 */

	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		throw getUnsupportedException();
	}

	/**
	 * @deprecated INTERNAL
	 */

	public InputStream getUnicodeStream(String columnLabel) throws SQLException {
		throw getUnsupportedException();
	}

	/**
	 * INTERNAL
	 */

	public URL getURL(int columnIndex) throws SQLException {
		throw getUnsupportedException();
	}

	/**
	 * INTERNAL
	 */

	public URL getURL(String columnLabel) throws SQLException {
		throw getUnsupportedException();
	}

	// ---- update ---------------------------------------------

	/**
	 * INTERNAL
	 */

	public void updateArray(int columnIndex, Array x) throws SQLException {
		update(columnIndex, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateArray(String columnLabel, Array x) throws SQLException {
		update(columnLabel, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateAsciiStream(int columnIndex, InputStream x)
			throws SQLException {
		update(columnIndex, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateAsciiStream(String columnLabel, InputStream x)
			throws SQLException {
		update(columnLabel, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateAsciiStream(int columnIndex, InputStream x, int length)
			throws SQLException {
		update(columnIndex, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateAsciiStream(String columnLabel, InputStream x, int length)
			throws SQLException {
		update(columnLabel, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateAsciiStream(int columnIndex, InputStream x, long length)
			throws SQLException {
		update(columnIndex, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateAsciiStream(String columnLabel, InputStream x, long length)
			throws SQLException {
		update(columnLabel, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateBigDecimal(int columnIndex, BigDecimal x)
			throws SQLException {
		update(columnIndex, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateBigDecimal(String columnLabel, BigDecimal x)
			throws SQLException {
		update(columnLabel, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateBinaryStream(int columnIndex, InputStream x)
			throws SQLException {
		update(columnIndex, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateBinaryStream(String columnLabel, InputStream x)
			throws SQLException {
		update(columnLabel, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateBinaryStream(int columnIndex, InputStream x, int length)
			throws SQLException {
		update(columnIndex, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateBinaryStream(String columnLabel, InputStream x, int length)
			throws SQLException {
		update(columnLabel, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateBinaryStream(int columnIndex, InputStream x, long length)
			throws SQLException {
		update(columnIndex, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateBinaryStream(String columnLabel, InputStream x,
			long length) throws SQLException {
		update(columnLabel, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		update(columnIndex, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateBlob(String columnLabel, Blob x) throws SQLException {
		update(columnLabel, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateBlob(int columnIndex, InputStream x) throws SQLException {
		update(columnIndex, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateBlob(String columnLabel, InputStream x)
			throws SQLException {
		update(columnLabel, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateBlob(int columnIndex, InputStream x, long length)
			throws SQLException {
		update(columnIndex, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateBlob(String columnLabel, InputStream x, long length)
			throws SQLException {
		update(columnLabel, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
		update(columnIndex, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateBoolean(String columnLabel, boolean x)
			throws SQLException {
		update(columnLabel, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateByte(int columnIndex, byte x) throws SQLException {
		update(columnIndex, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateByte(String columnLabel, byte x) throws SQLException {
		update(columnLabel, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateBytes(int columnIndex, byte[] x) throws SQLException {
		update(columnIndex, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateBytes(String columnLabel, byte[] x) throws SQLException {
		update(columnLabel, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateCharacterStream(int columnIndex, Reader x)
			throws SQLException {
		update(columnIndex, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateCharacterStream(String columnLabel, Reader x)
			throws SQLException {
		update(columnLabel, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateCharacterStream(int columnIndex, Reader x, int length)
			throws SQLException {
		update(columnIndex, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateCharacterStream(String columnLabel, Reader x, int length)
			throws SQLException {
		update(columnLabel, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateCharacterStream(int columnIndex, Reader x, long length)
			throws SQLException {
		update(columnIndex, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateCharacterStream(String columnLabel, Reader x, long length)
			throws SQLException {
		update(columnLabel, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateClob(int columnIndex, Clob x) throws SQLException {
		update(columnIndex, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateClob(String columnLabel, Clob x) throws SQLException {
		update(columnLabel, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateClob(int columnIndex, Reader x) throws SQLException {
		update(columnIndex, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateClob(String columnLabel, Reader x) throws SQLException {
		update(columnLabel, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateClob(int columnIndex, Reader x, long length)
			throws SQLException {
		update(columnIndex, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateClob(String columnLabel, Reader x, long length)
			throws SQLException {
		update(columnLabel, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateDate(int columnIndex, Date x) throws SQLException {
		update(columnIndex, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateDate(String columnLabel, Date x) throws SQLException {
		update(columnLabel, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateDouble(int columnIndex, double x) throws SQLException {
		update(columnIndex, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateDouble(String columnLabel, double x) throws SQLException {
		update(columnLabel, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateFloat(int columnIndex, float x) throws SQLException {
		update(columnIndex, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateFloat(String columnLabel, float x) throws SQLException {
		update(columnLabel, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateInt(int columnIndex, int x) throws SQLException {
		update(columnIndex, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateInt(String columnLabel, int x) throws SQLException {
		update(columnLabel, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateLong(int columnIndex, long x) throws SQLException {
		update(columnIndex, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateLong(String columnLabel, long x) throws SQLException {
		update(columnLabel, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateNCharacterStream(int columnIndex, Reader x)
			throws SQLException {
		update(columnIndex, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateNCharacterStream(String columnLabel, Reader x)
			throws SQLException {
		update(columnLabel, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateNCharacterStream(int columnIndex, Reader x, long length)
			throws SQLException {
		update(columnIndex, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateNCharacterStream(String columnLabel, Reader x, long length)
			throws SQLException {
		update(columnLabel, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateNClob(int columnIndex, Reader x) throws SQLException {
		update(columnIndex, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateNClob(String columnLabel, Reader x) throws SQLException {
		update(columnLabel, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateNClob(int columnIndex, Reader x, long length)
			throws SQLException {
		update(columnIndex, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateNClob(String columnLabel, Reader x, long length)
			throws SQLException {
		update(columnLabel, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateNString(int columnIndex, String x) throws SQLException {
		update(columnIndex, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateNString(String columnLabel, String x) throws SQLException {
		update(columnLabel, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateNull(int columnIndex) throws SQLException {
		update(columnIndex, null);
	}

	/**
	 * INTERNAL
	 */

	public void updateNull(String columnLabel) throws SQLException {
		update(columnLabel, null);
	}

	/**
	 * INTERNAL
	 */

	public void updateObject(int columnIndex, Object x) throws SQLException {
		update(columnIndex, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateObject(String columnLabel, Object x) throws SQLException {
		update(columnLabel, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateObject(int columnIndex, Object x, int scale)
			throws SQLException {
		update(columnIndex, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateObject(String columnLabel, Object x, int scale)
			throws SQLException {
		update(columnLabel, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateRef(int columnIndex, Ref x) throws SQLException {
		update(columnIndex, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateRef(String columnLabel, Ref x) throws SQLException {
		update(columnLabel, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateShort(int columnIndex, short x) throws SQLException {
		update(columnIndex, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateShort(String columnLabel, short x) throws SQLException {
		update(columnLabel, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateString(int columnIndex, String x) throws SQLException {
		update(columnIndex, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateString(String columnLabel, String x) throws SQLException {
		update(columnLabel, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateTime(int columnIndex, Time x) throws SQLException {
		update(columnIndex, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateTime(String columnLabel, Time x) throws SQLException {
		update(columnLabel, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateTimestamp(int columnIndex, Timestamp x)
			throws SQLException {
		update(columnIndex, x);
	}

	/**
	 * INTERNAL
	 */

	public void updateTimestamp(String columnLabel, Timestamp x)
			throws SQLException {
		update(columnLabel, x);
	}

	// ---- result set meta data ---------------------------------------------

	/**
	 * Returns the column count.
	 * 
	 * @return the column count
	 */

	public int getColumnCount() {
		return columns.size();
	}

	/**
	 * Returns 15.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return 15
	 */

	public int getColumnDisplaySize(int columnIndex) {
		return 15;
	}

	/**
	 * Returns the SQL type.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return the SQL type
	 */

	public int getColumnType(int columnIndex) throws SQLException {
		return getColumn(columnIndex - 1).sqlType;
	}

	/**
	 * Returns the precision.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return the precision
	 */

	public int getPrecision(int columnIndex) throws SQLException {
		return getColumn(columnIndex - 1).precision;
	}

	/**
	 * Returns the scale.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return the scale
	 */

	public int getScale(int columnIndex) throws SQLException {
		return getColumn(columnIndex - 1).scale;
	}

	/**
	 * Returns ResultSetMetaData.columnNullableUnknown.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return columnNullableUnknown
	 */

	public int isNullable(int columnIndex) {
		return ResultSetMetaData.columnNullableUnknown;
	}

	/**
	 * Returns false.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return false
	 */

	public boolean isAutoIncrement(int columnIndex) {
		return false;
	}

	/**
	 * Returns true.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return true
	 */

	public boolean isCaseSensitive(int columnIndex) {
		return true;
	}

	/**
	 * Returns false.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return false
	 */

	public boolean isCurrency(int columnIndex) {
		return false;
	}

	/**
	 * Returns false.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return false
	 */

	public boolean isDefinitelyWritable(int columnIndex) {
		return false;
	}

	/**
	 * Returns true.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return true
	 */

	public boolean isReadOnly(int columnIndex) {
		return true;
	}

	/**
	 * Returns true.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return true
	 */

	public boolean isSearchable(int columnIndex) {
		return true;
	}

	/**
	 * Returns true.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return true
	 */

	public boolean isSigned(int columnIndex) {
		return true;
	}

	/**
	 * Returns false.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return false
	 */

	public boolean isWritable(int columnIndex) {
		return false;
	}

	/**
	 * Returns null.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return null
	 */

	public String getCatalogName(int columnIndex) {
		return null;
	}

	/**
	 * Returns the Java class name if this column.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return the class name
	 */

	public String getColumnClassName(int columnIndex) throws SQLException {
		int sqlType = getColumn(columnIndex - 1).sqlType;
		int type = DataType.convertSQLTypeToValueType(sqlType);
		return DataType.getTypeClassName(type);
	}

	/**
	 * Returns the column label.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return the column label
	 */

	public String getColumnLabel(int columnIndex) throws SQLException {
		return getColumn(columnIndex - 1).name;
	}

	/**
	 * Returns the column name.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return the column name
	 */

	public String getColumnName(int columnIndex) throws SQLException {
		return getColumnLabel(columnIndex);
	}

	/**
	 * Returns the data type name of a column.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return the type name
	 */

	public String getColumnTypeName(int columnIndex) throws SQLException {
		int sqlType = getColumn(columnIndex - 1).sqlType;
		int type = DataType.convertSQLTypeToValueType(sqlType);
		return DataType.getDataType(type).getName();
	}

	/**
	 * Returns null.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return null
	 */

	public String getSchemaName(int columnIndex) {
		return null;
	}

	/**
	 * Returns null.
	 * 
	 * @param columnIndex
	 *            (1,2,...)
	 * @return null
	 */

	public String getTableName(int columnIndex) {
		return null;
	}

	// ---- unsupported / result set
	// ---------------------------------------------

	/**
	 * INTERNAL
	 */

	public void afterLast() throws SQLException {
		rowId = rows.size();
	}

	/**
	 * INTERNAL
	 */

	public void cancelRowUpdates() throws SQLException {
		throw getUnsupportedException();
	}

	/**
	 * INTERNAL
	 */

	public void deleteRow() throws SQLException {
		throw getUnsupportedException();
	}

	/**
	 * INTERNAL
	 */

	public void insertRow() throws SQLException {
		throw getUnsupportedException();
	}

	/**
	 * INTERNAL
	 */

	public void moveToCurrentRow() throws SQLException {
		throw getUnsupportedException();
	}

	/**
	 * INTERNAL
	 */

	public void moveToInsertRow() throws SQLException {
		throw getUnsupportedException();
	}

	/**
	 * INTERNAL
	 */

	public void refreshRow() throws SQLException {
		throw getUnsupportedException();
	}

	/**
	 * INTERNAL
	 */

	public void updateRow() throws SQLException {
		throw getUnsupportedException();
	}

	/**
	 * INTERNAL
	 */

	public boolean first() throws SQLException {
		checkClosed();
		rowId = 0;
		currentRow = rows.get(rowId);
		return true;
	}

	/**
	 * INTERNAL
	 */

	public boolean isAfterLast() throws SQLException {
		checkClosed();
		return rowId >= rows.size();
	}

	/**
	 * INTERNAL
	 */

	public boolean isBeforeFirst() throws SQLException {
		checkClosed();
		return rowId <= -1;
	}

	/**
	 * INTERNAL
	 */

	public boolean isFirst() throws SQLException {
		checkClosed();
		return rowId == 0;
	}

	/**
	 * INTERNAL
	 */

	public boolean isLast() throws SQLException {
		checkClosed();
		return rowId == rows.size() - 1;
	}

	/**
	 * INTERNAL
	 */

	public boolean last() throws SQLException {
		checkClosed();
		rowId = rows.size() - 1;
		currentRow = rows.get(rowId);
		return true;
	}

	/**
	 * INTERNAL
	 */

	public boolean previous() throws SQLException {
		checkClosed();
		if (rows != null) {
			rowId--;
			if (rowId >= 0 && rowId < rows.size()) {
				currentRow = rows.get(rowId);
				return true;
			}
		}
		return false;
	}

	/**
	 * INTERNAL
	 */

	public boolean rowDeleted() throws SQLException {
		throw getUnsupportedException();
	}

	/**
	 * INTERNAL
	 */

	public boolean rowInserted() throws SQLException {
		throw getUnsupportedException();
	}

	/**
	 * INTERNAL
	 */

	public boolean rowUpdated() throws SQLException {
		throw getUnsupportedException();
	}

	/**
	 * INTERNAL
	 */

	public void setFetchDirection(int direction) throws SQLException {
		throw getUnsupportedException();
	}

	/**
	 * INTERNAL
	 */

	public void setFetchSize(int rows) throws SQLException {
		throw getUnsupportedException();
	}

	/**
	 * INTERNAL
	 */

	public boolean absolute(int rowNumber) throws SQLException {
		checkClosed();
		int totalRows = rows.size();
		if (rowNumber < 0) {
			rowNumber = totalRows + rowNumber + 1;
		} else if (rowNumber > totalRows) {
			rowNumber = totalRows + 1;
		}
		rowId = rowNumber - 1;
		currentRow = rows.get(rowId);
		return rowId >= 0 && rowId < totalRows;
	}

	/**
	 * INTERNAL
	 */

	public boolean relative(int offset) throws SQLException {
		checkClosed();
		int totalRows = rows.size();
		int rowNumber = rowId + offset + 1;
		if (rowNumber <= 0) {
			rowNumber = 1;
		} else if (rowNumber > totalRows) {
			rowNumber = totalRows;
		}
		return absolute(rowNumber);
	}

	/**
	 * INTERNAL
	 */

	public String getCursorName() throws SQLException {
		throw getUnsupportedException();
	}

	// --- private -----------------------------

	private void update(int columnIndex, Object obj) throws SQLException {
		checkClosed();
		checkColumnIndex(columnIndex);
		this.currentRow[columnIndex - 1] = obj;
	}

	private void update(String columnLabel, Object obj) throws SQLException {
		checkClosed();
		this.currentRow[findColumn(columnLabel) - 1] = obj;
	}

	/**
	 * INTERNAL
	 */
	static SQLException getUnsupportedException() {
		return new SQLException("method not support");
	}

	private void checkColumnIndex(int columnIndex) throws SQLException {
		if (columnIndex < 1 || columnIndex > columns.size()) {
			throw new SQLException("columnIndex is not valid:" + columnIndex);
		}
	}

	private Object get(int columnIndex) throws SQLException {
		checkClosed();
		if (currentRow == null) {
			throw new SQLException("not on a valid row for the given operation");
		}
		checkColumnIndex(columnIndex);
		columnIndex--;
		Object o = columnIndex < currentRow.length ? currentRow[columnIndex]
				: null;
		wasNull = o == null;
		return o;
	}

	private Column getColumn(int i) throws SQLException {
		checkColumnIndex(i + 1);
		return columns.get(i);
	}

	/**
	 * Returns the current result set holdability.
	 * 
	 * @return the holdability
	 */

	public int getHoldability() {
		return ResultSet.HOLD_CURSORS_OVER_COMMIT;
	}

	/**
	 * Returns whether this result set has been closed.
	 * 
	 * @return true if the result set was closed
	 */

	public boolean isClosed() {
		return rows == null;
	}

	/**
	 * INTERNAL
	 */

	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw getUnsupportedException();
	}

	/**
	 * INTERNAL
	 */

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		throw getUnsupportedException();
	}

}
