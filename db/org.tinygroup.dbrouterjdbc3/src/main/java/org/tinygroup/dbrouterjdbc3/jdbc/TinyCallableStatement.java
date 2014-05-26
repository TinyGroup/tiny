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
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import org.tinygroup.dbrouter.config.Router;
import org.tinygroup.jsqlparser.statement.select.Select;

/**
 * 
 * 功能说明:存储过程statement的实现 

 * 开发人员: renhui <br>
 * 开发时间: 2014-1-14 <br>
 * <br>
 */
public class TinyCallableStatement extends TinyPreparedStatement implements
		CallableStatement {

	public TinyCallableStatement(Router router,
			TinyConnection tinyConnection, int resultSetType,
			int resultSetConcurrency, boolean closedByResultSet,
			boolean autoCommit, String sql) throws SQLException {
		super(router, tinyConnection, resultSetType, resultSetConcurrency,
				closedByResultSet, autoCommit, sql);
	}

	

	public int executeUpdate() throws SQLException {
		org.tinygroup.jsqlparser.statement.Statement statement = routerManager
		.getSqlStatement(sqlStatement);
		if (statement instanceof Select) {
			super.executeQuery();
			return 0;
		}
		return super.executeUpdate();
	}


	public void registerOutParameter(int parameterIndex, int sqlType)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void registerOutParameter(int parameterIndex, int sqlType, int scale)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public boolean wasNull() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public String getString(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean getBoolean(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public byte getByte(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public short getShort(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getInt(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public long getLong(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public float getFloat(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getDouble(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public BigDecimal getBigDecimal(int parameterIndex, int scale)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public byte[] getBytes(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Date getDate(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Time getTime(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Timestamp getTimestamp(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getObject(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getObject(int i, Map<String, Class<?>> map)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Ref getRef(int i) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Blob getBlob(int i) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Clob getClob(int i) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Array getArray(int i) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Timestamp getTimestamp(int parameterIndex, Calendar cal)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void registerOutParameter(int paramIndex, int sqlType,
			String typeName) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void registerOutParameter(String parameterName, int sqlType)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void registerOutParameter(String parameterName, int sqlType,
			int scale) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void registerOutParameter(String parameterName, int sqlType,
			String typeName) throws SQLException {
		// TODO Auto-generated method stub

	}

	public URL getURL(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setURL(String parameterName, URL val) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setNull(String parameterName, int sqlType) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setBoolean(String parameterName, boolean x) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setByte(String parameterName, byte x) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setShort(String parameterName, short x) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setInt(String parameterName, int x) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setLong(String parameterName, long x) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setFloat(String parameterName, float x) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setDouble(String parameterName, double x) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setBigDecimal(String parameterName, BigDecimal x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setString(String parameterName, String x) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setBytes(String parameterName, byte[] x) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setDate(String parameterName, Date x) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setTime(String parameterName, Time x) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setTimestamp(String parameterName, Timestamp x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setAsciiStream(String parameterName, InputStream x, int length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setBinaryStream(String parameterName, InputStream x, int length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setObject(String parameterName, Object x, int targetSqlType,
			int scale) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setObject(String parameterName, Object x, int targetSqlType)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setObject(String parameterName, Object x) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setCharacterStream(String parameterName, Reader reader,
			int length) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setDate(String parameterName, Date x, Calendar cal)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setTime(String parameterName, Time x, Calendar cal)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setTimestamp(String parameterName, Timestamp x, Calendar cal)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setNull(String parameterName, int sqlType, String typeName)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public String getString(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean getBoolean(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public byte getByte(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public short getShort(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getInt(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public long getLong(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public float getFloat(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getDouble(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public byte[] getBytes(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Date getDate(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Time getTime(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Timestamp getTimestamp(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getObject(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public BigDecimal getBigDecimal(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getObject(String parameterName, Map<String, Class<?>> map)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Ref getRef(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Blob getBlob(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Clob getClob(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Array getArray(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Date getDate(String parameterName, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Time getTime(String parameterName, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Timestamp getTimestamp(String parameterName, Calendar cal)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public URL getURL(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
