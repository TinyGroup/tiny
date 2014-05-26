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
package org.tinygroup.dbrouter.util;

import java.io.InputStream;
import java.io.Reader;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.commons.tools.CollectionUtil;

/**
 * 
 * 功能说明:用于prepare以及resultset curd操作参数赋值
 * <p>
 * 系统版本: v1.0<br>
 * 开发人员: renhui <br>
 * 开发时间: 2014-1-9 <br>
 * <br>
 */
public class ParamObjectBuilder {

	private List<ParameterObject> params = new ArrayList<ParameterObject>();
	
	private Map<Integer, Object> indexParamValue=new HashMap<Integer, Object>();
	
	private Object[] paramsCache;
	
	private int paramNumber;
	
	public ParamObjectBuilder(int paramNumber) {
		super();
		this.paramNumber = paramNumber;
		paramsCache=new Object[paramNumber];
	}
	
	private void checkColumnIndex(int columnIndex)throws SQLException {
		if (columnIndex < 1 || columnIndex > paramNumber) {
			throw new SQLException("columnIndex is invalid");
		}
	}
	
	
	public void addParamterObject(int parameterIndex, Object value) throws SQLException {
		checkColumnIndex(parameterIndex);
		paramsCache[parameterIndex-1]=value;
		params.add(new ParameterObject(parameterIndex, value));
		indexParamValue.put(parameterIndex, value);
	}

	public void addNullParamterObject(int parameterIndex, int sqlType) throws SQLException {
		checkColumnIndex(parameterIndex);
		paramsCache[parameterIndex-1]=null;
		params.add(new ParameterObject(parameterIndex, sqlType, null, true));
		indexParamValue.put(parameterIndex, null);
	}

	public void addNullParamterObject(int parameterIndex, int sqlType, String typeName) throws SQLException {
		checkColumnIndex(parameterIndex);
		paramsCache[parameterIndex-1]=null;
		params.add(new ParameterObject(parameterIndex, sqlType, typeName, true));
		indexParamValue.put(parameterIndex, null);
	}

	public void addParamterObject(int parameterIndex, Object value, int targetSqlType) throws SQLException {
		checkColumnIndex(parameterIndex);
		paramsCache[parameterIndex-1]=value;
		params.add(new ParameterObject(parameterIndex, value, targetSqlType));
		indexParamValue.put(parameterIndex, value);
	}

	public void addInputStreamParamterObject(int parameterIndex, InputStream value,
			int length,boolean asciiStream) throws SQLException {
		checkColumnIndex(parameterIndex);
		paramsCache[parameterIndex-1]=value;
		params.add(new ParameterObject(parameterIndex, value, length,asciiStream));
		indexParamValue.put(parameterIndex, value);
	}

	public void addReaderParamterObject(int parameterIndex, Reader value, int length) throws SQLException {
		checkColumnIndex(parameterIndex);
		paramsCache[parameterIndex-1]=value;
		params.add(new ParameterObject(parameterIndex, value, length));
		indexParamValue.put(parameterIndex, value);
	}

	public void addDateParamterObject(int parameterIndex, Date value, Calendar cal) throws SQLException {
		checkColumnIndex(parameterIndex);
		paramsCache[parameterIndex-1]=value;
		params.add(new ParameterObject(parameterIndex, value, cal));
		indexParamValue.put(parameterIndex, value);
	}

	public void addTimeParamterObject(int parameterIndex, Time value, Calendar cal) throws SQLException {
		checkColumnIndex(parameterIndex);
		paramsCache[parameterIndex-1]=value;
		params.add(new ParameterObject(parameterIndex, value, cal));
		indexParamValue.put(parameterIndex, value);
	}

	public void addTimestampParamterObject(int parameterIndex, Timestamp value,
			Calendar cal) throws SQLException {
		checkColumnIndex(parameterIndex);
		paramsCache[parameterIndex-1]=value;
		params.add(new ParameterObject(parameterIndex, value, cal));
		indexParamValue.put(parameterIndex, value);
	}

	public void addParamterObject(int parameterIndex, Object value, int targetSqlType,
			int scale) throws SQLException {
		checkColumnIndex(parameterIndex);
		paramsCache[parameterIndex-1]=value;
		params.add(new ParameterObject(parameterIndex, value, targetSqlType,
				scale));
		indexParamValue.put(parameterIndex, value);
	}
	

	class ParameterObject {
		private int index;
		private Object value;
		private Integer targetSqlType;
		private Integer sqlType;
		private Integer scale;
		private String typeName;
		private int length;
		private Calendar cal;
		private boolean nullParam;// setNull方法标识
		private boolean asciiStream;//区分setAsciiStream与setBinaryStream
		

		public ParameterObject(int index, Object value) {
			super();
			this.index = index;
			this.value = value;
		}

		public ParameterObject(int index, Integer sqlType, String typeName,
				boolean nullParam) {
			super();
			this.index = index;
			this.sqlType = sqlType;
			this.nullParam = nullParam;
			this.typeName = typeName;
		}

		public ParameterObject(int index, Object value, int targetSqlType) {
			this(index, value);
			this.targetSqlType = targetSqlType;
		}

		public ParameterObject(int index, InputStream value, int length,boolean asciiStream) {
			this(index, value);
			this.asciiStream=asciiStream;
			this.length = length;
		}

		public ParameterObject(int index, Reader value, int length) {
			this(index, value);
			this.length = length;
		}

		public ParameterObject(int index, Date value, Calendar cal) {
			this(index, value);
			this.cal = cal;
		}

		public ParameterObject(int index, Time value, Calendar cal) {
			this(index, value);
			this.cal = cal;
		}

		public ParameterObject(int index, Timestamp value, Calendar cal) {
			this(index, value);
			this.cal = cal;
		}

		public ParameterObject(int index, Object value, int targetSqlType,
				Integer scale) {
			this(index, value, targetSqlType);
			this.scale = scale;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

		public void setParamter(PreparedStatement preparedStatement)
				throws SQLException {
			if (value instanceof InputStream) {
				if(asciiStream){
					preparedStatement.setAsciiStream(index, (InputStream) value,
							length);
				}else{
					preparedStatement.setBinaryStream(index, (InputStream) value,
							length);
				}
				
			} else if (value instanceof Reader) {
				preparedStatement.setCharacterStream(index, (Reader) value,
						length);
			} else if (value instanceof Time) {
				if (cal != null) {
					preparedStatement.setTime(index, (Time) value, cal);
				} else {
					preparedStatement.setTime(index, (Time) value);
				}

			} else if (value instanceof Timestamp) {
				if (cal != null) {
					preparedStatement.setTimestamp(index, (Timestamp) value,
							cal);
				} else {
					preparedStatement.setTimestamp(index, (Timestamp) value);
				}
			} else {
				if (nullParam) {
					if (typeName != null) {
						preparedStatement.setNull(index, sqlType, typeName);
					} else {
						preparedStatement.setNull(index, sqlType);
					}
				} else {
					if (targetSqlType != null) {
						if (scale != null) {
							preparedStatement.setObject(index, value,
									targetSqlType, scale);
						} else {
							preparedStatement.setObject(index, value,
									targetSqlType);
						}
					} else {
						preparedStatement.setObject(index, value);
					}
				}
			}

		}

	}

	public void clear() {
		params.clear();
        paramsCache=null;
	}
	
	public void setParamters(PreparedStatement preparedStatement) throws SQLException{
		if(!CollectionUtil.isEmpty(params)){
			for (ParameterObject param : params) {
				param.setParamter(preparedStatement);
			}
		}
	}

	public Object[] getPreparedParams() {
		return paramsCache;
	}
	
	public int getParameterCount(){
		return params.size();
	}
	/**
	 * 
	 * 获取字段对应的参数值
	 * @param columnIndex 列序号 如1、2、3
	 * @return
	 */
	public Object getParamValue(int columnIndex){
		return indexParamValue.get(columnIndex);
	}
}
