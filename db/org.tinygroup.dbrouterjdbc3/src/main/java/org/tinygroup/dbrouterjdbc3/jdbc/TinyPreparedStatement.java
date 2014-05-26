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
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.dbrouter.config.Router;
import org.tinygroup.dbrouter.config.Partition;
import org.tinygroup.dbrouter.config.Shard;
import org.tinygroup.dbrouter.util.DbRouterUtil;
import org.tinygroup.dbrouter.util.ParamObjectBuilder;

/**
 * 
 * 功能说明:
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-12-19 <br>
 * <br>
 */
public class TinyPreparedStatement extends TinyStatement implements
		PreparedStatement {

	protected final String sqlStatement;
	protected ParamObjectBuilder builder;
	protected int paramSize;
	protected ParameterMetaData parameterMetaData;
	protected TinyParameterMetaData metaData;
    
	public TinyPreparedStatement(Router router,
			TinyConnection tinyConnection, int resultSetType,
			int resultSetConcurrency, boolean closedByResultSet, boolean autoCommit, String sql) throws SQLException {
		super(router, tinyConnection, resultSetType, resultSetConcurrency,
				closedByResultSet,autoCommit);
		this.sqlStatement = sql;
		paramSize=DbRouterUtil.getSqlParamSize(sqlStatement);
		builder=new ParamObjectBuilder(paramSize);
		getOriginalParameterMetadata(router, sql);
	}

	private void getOriginalParameterMetadata(Router router, String sql)
			throws SQLException {
		Partition partition = routerManager.getPartition(router, sql);
		List<Shard> shards=partition.getShards();
		if(CollectionUtil.isEmpty(shards)){
			throw new RuntimeException("not found shard in the partition with sql:"+sql);
		}
		Shard shard=shards.get(0);//获取sql对应的分片信息。
		PreparedStatement preparedStatement=(PreparedStatement) getStatement(shard);
		parameterMetaData=preparedStatement.getParameterMetaData();
	}

	public ResultSet executeQuery() throws SQLException {
		ResultSet resultSet= super.executeQuery(sqlStatement);
		clearParameters();
		return resultSet;
	}

	
	protected Statement getStatement(Shard shard) throws SQLException {
		Statement statement = statementMap.get(shard);
		if(tinyConnection.getAutoCommit()!=autoCommit){//有调用过tinyconnection.setAutoCommit(),重写创建statement
			statement = shard.getConnection(tinyConnection).prepareStatement(sqlStatement,
					resultSetType, resultSetConcurrency,
					getResultSetHoldability());
			setStatementProperties(statement);
			statementMap.put(shard, statement);
		}else{
			if (statement == null) {
				statement = shard.getConnection(tinyConnection).prepareStatement(sqlStatement,
						resultSetType, resultSetConcurrency,
						getResultSetHoldability());
				setStatementProperties(statement);
				statementMap.put(shard, statement);
			}
		}
		setParamters((PreparedStatement) statement);//设置参数
		return statement;
	}	
	
	protected Statement getNewStatement(String sql,Shard shard) throws SQLException {
		Statement statement = shard.getConnection(tinyConnection).prepareStatement(sql,
				resultSetType, resultSetConcurrency,
				getResultSetHoldability());
		setStatementProperties(statement);
		setParamters((PreparedStatement) statement);//设置参数
		return statement;
	}


	protected Object[] getPreparedParams() {
	    return builder.getPreparedParams();
	}

	public int executeUpdate() throws SQLException {
		int count=super.executeUpdate(sqlStatement);
		clearParameters();
		return count;
	}

	
	public void close() throws SQLException {
		super.close();
	}

	public void addBatch() throws SQLException {
		checkClosed();
		super.addBatch(sqlStatement);
		clearParameters();
	}

	
	public void addBatch(String sql) throws SQLException {
		checkClosed();
		throw new SQLException("builder.addBatch(String sql) not allow for PreparedStatement");
	}

	
	public int[] executeBatch() throws SQLException {
		checkClosed();
		return super.executeBatch();
	}

	
	public ResultSet executeQuery(String sql) throws SQLException {
		 throw new SQLException("executeQuery(String sql) not allow for PreparedStatement");
	}

	
	public int executeUpdate(String sql) throws SQLException {
		 throw new SQLException("executeUpdate(String sql) not allow for PreparedStatement");
	}

	
	public boolean execute(String sql) throws SQLException {
		throw new SQLException("execute(String sql) not allow for PreparedStatement");
	}

	
	public int executeUpdate(String sql, int autoGeneratedKeys)
			throws SQLException {
		throw new SQLException("execute(String sql,int autoGeneratedKeys) not allow for PreparedStatement");
	}

	
	public int executeUpdate(String sql, int[] columnIndexes)
			throws SQLException {
		throw new SQLException("execute(String sql, int[] columnIndexes) not allow for PreparedStatement");
	}

	
	public int executeUpdate(String sql, String[] columnNames)
			throws SQLException {
		throw new SQLException("executeUpdate(String sql, String[] columnNames) not allow for PreparedStatement");
	}

	
	public boolean execute(String sql, int autoGeneratedKeys)
			throws SQLException {
		throw new SQLException("execute(String sql, int autoGeneratedKeys) not allow for PreparedStatement");
	}

	
	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		throw new SQLException("execute(String sql, int[] columnIndexes) not allow for PreparedStatement");
	}	

	
	public boolean execute(String sql, String[] columnNames)
			throws SQLException {
		throw new SQLException("execute(String sql, String[] columnNames) not allow for PreparedStatement");
	}
	
	
	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		checkClosed();
		builder.addNullParamterObject(parameterIndex, sqlType);
	}
	
	protected void setParamters(PreparedStatement preparedStatement) throws SQLException{
		builder.setParamters(preparedStatement);
	}

	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		checkClosed();
		builder.addParamterObject(parameterIndex, x);
	}

	public void setByte(int parameterIndex, byte x) throws SQLException {
		checkClosed();
		builder.addParamterObject(parameterIndex, x);
	}

	public void setShort(int parameterIndex, short x) throws SQLException {
		checkClosed();
		builder.addParamterObject(parameterIndex, x);
	}

	public void setInt(int parameterIndex, int x) throws SQLException {
		checkClosed();
		builder.addParamterObject(parameterIndex, x);
	}

	public void setLong(int parameterIndex, long x) throws SQLException {
		checkClosed();
		builder.addParamterObject(parameterIndex, x);
	}

	public void setFloat(int parameterIndex, float x) throws SQLException {
		checkClosed();
		builder.addParamterObject(parameterIndex, x);
	}

	public void setDouble(int parameterIndex, double x) throws SQLException {
		checkClosed();
		builder.addParamterObject(parameterIndex, x);
	}

	public void setBigDecimal(int parameterIndex, BigDecimal x)
			throws SQLException {
		checkClosed();
		builder.addParamterObject(parameterIndex, x);
	}

	public void setString(int parameterIndex, String x) throws SQLException {
		checkClosed();
		builder.addParamterObject(parameterIndex, x);
	}

	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		checkClosed();
		builder.addParamterObject(parameterIndex, x);
	}

	public void setDate(int parameterIndex, Date x) throws SQLException {
		checkClosed();
		builder.addParamterObject(parameterIndex, x);
	}

	public void setTime(int parameterIndex, Time x) throws SQLException {
		checkClosed();
		builder.addParamterObject(parameterIndex, x);
	}

	public void setTimestamp(int parameterIndex, Timestamp x)
			throws SQLException {
		checkClosed();
		builder.addParamterObject(parameterIndex, x);
	}

	public void setAsciiStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		checkClosed();
		builder.addInputStreamParamterObject(parameterIndex, x, length,true);
	}

	public void setUnicodeStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		throw new SQLException("setUnicodeStream is not supported please use setCharacterStream");
	}

	public void setBinaryStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		checkClosed();
		builder.addInputStreamParamterObject(parameterIndex, x, length,false);
	}

	public void clearParameters() throws SQLException {
		checkClosed();
		builder.clear();
	}

	public void setObject(int parameterIndex, Object x, int targetSqlType,
			int scale) throws SQLException {
		checkClosed();
		builder.addParamterObject(parameterIndex, x, targetSqlType, scale);
	}

	public void setObject(int parameterIndex, Object x, int targetSqlType)
			throws SQLException {
		checkClosed();
		builder.addParamterObject(parameterIndex, x, targetSqlType);
	}

	public void setObject(int parameterIndex, Object x) throws SQLException {
		checkClosed();
		builder.addParamterObject(parameterIndex, x);
	}

	public boolean execute() throws SQLException {
		boolean execute=super.execute(sqlStatement);
		clearParameters();
		return execute;
	}

	public void setCharacterStream(int parameterIndex, Reader reader, int length)
			throws SQLException {
		checkClosed();
		builder.addReaderParamterObject(parameterIndex, reader, length);
	}

	public void setRef(int i, Ref x) throws SQLException {
		checkClosed();
		builder.addParamterObject(i, x);
	}

	public void setBlob(int i, Blob x) throws SQLException {
		checkClosed();
		builder.addParamterObject(i, x);
	}

	public void setClob(int i, Clob x) throws SQLException {
		checkClosed();
		builder.addParamterObject(i, x);
	}

	public void setArray(int i, Array x) throws SQLException {
		checkClosed();
		builder.addParamterObject(i, x);
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		return new TinyResultSetMetaData(sqlStatement, executeQuery().getMetaData());
	}

	public void setDate(int parameterIndex, Date x, Calendar cal)
			throws SQLException {
		checkClosed();
		builder.addDateParamterObject(parameterIndex, x,cal);
	}

	public void setTime(int parameterIndex, Time x, Calendar cal)
			throws SQLException {
		checkClosed();
		builder.addTimeParamterObject(parameterIndex, x, cal);
	}

	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
			throws SQLException {
		checkClosed();
		builder.addTimestampParamterObject(parameterIndex, x, cal);
	}

	public void setNull(int paramIndex, int sqlType, String typeName)
			throws SQLException {
		checkClosed();
		builder.addNullParamterObject(paramIndex, sqlType, typeName);
	}

	public void setURL(int parameterIndex, URL x) throws SQLException {
		checkClosed();
		builder.addParamterObject(parameterIndex, x);
	}

	public ParameterMetaData getParameterMetaData() throws SQLException {
		if(metaData==null){
			metaData=new TinyParameterMetaData(parameterMetaData);
		}
		return metaData;
	}
	
	public ParamObjectBuilder getParamBuilder(){
		return builder;
	}
}
