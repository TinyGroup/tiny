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
package org.tinygroup.tinydb.operator.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.dialect.Dialect;
import org.tinygroup.tinydb.exception.DBRuntimeException;
import org.tinygroup.tinydb.util.BatchPreparedStatementSetterImpl;
import org.tinygroup.tinydb.util.BeanRowMapper;
import org.tinygroup.tinydb.util.SqlParamValuesBatchStatementSetterImpl;

public class DBSpringBaseOperator {

	private JdbcTemplate jdbcTemplate;
	private Dialect dialect;
	

	public Dialect getDialect() {
		return dialect;
	}

	public void setDialect(Dialect dialect) {
		this.dialect = dialect;
	}

	public int account(String sql) throws SQLException {
		int ret = 0;
		SqlRowSet sqlRowset = jdbcTemplate.queryForRowSet(sql);
		if (sqlRowset.next()) {
			ret = sqlRowset.getInt(1);
		}
		return ret;
	}

	public DBSpringBaseOperator(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public int executeByList(String sql, List<Object> parameters,
			List<Integer> dataTypes) {
		int[] types = getDataTypes(dataTypes);
		if(types!=null&&types.length>0){
			return jdbcTemplate.update(sql, parameters.toArray(),types);
		}else{
			return jdbcTemplate.update(sql, parameters.toArray());
		}
	
	}
	
	public int executeBySqlParameterValues(String sql, SqlParameterValue[] values) {
			return jdbcTemplate.update(sql, values);
	}
	
	public int executeBySqlParameterValue(String sql, SqlParameterValue value) {
		SqlParameterValue[] values= new SqlParameterValue[1];
		values[0]=value;
		return jdbcTemplate.update(sql,values);
    }
	
	
	

	/**
	 * 
	 * 根据参数中sql语句进行批处理操作
	 * 
	 * @param sql
	 * @param parameters
	 * @param dataTypes
	 *            key代表未赋参数值的sql语句 value代表sql语句中的参数列表信息
	 */
	protected int[] executeBatchByList(String sql,
			List<List<Object>> parameters, List<Integer> dataTypes) {
		return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetterImpl(parameters,
				dataTypes));
	}
	
	/**
	 * 
	 * 根据参数中sql语句进行批处理操作
	 * 
	 * @param sql
	 * @param parameters
	 *            key代表未赋参数值的sql语句 value代表sql语句中的参数列表信息
	 */
	protected int[] executeBatchBySqlParamterValues(String sql,
			List<SqlParameterValue[]> parameters) {
		return jdbcTemplate.batchUpdate(sql, new SqlParamValuesBatchStatementSetterImpl(parameters));
	}
	

	public List<Bean> findBeansByListForPage(String sql, String beanType,String schema,
			int start, int limit, List<Object> parameters) {
		String tempSql=sql;
		if (supportsLimit()) {
			tempSql = getLimitString(sql, start, limit);
			List<Bean> beans = jdbcTemplate.query(tempSql, parameters.toArray(),
					new BeanRowMapper(beanType,schema));
			return beans;
		}
		throw new DBRuntimeException("The db don't support this operation");
	}

	public List<Bean> findBeansByMapForPage(String sql, String beanType,String schema,
			int start, int limit, Map<String, Object> parameters,
			List<Integer> dataTypes) {
		String tempSql=sql;
		if (supportsLimit()) {
			tempSql = getLimitString(sql, start, limit);
			return findBeansByMap(tempSql, beanType,schema, parameters, dataTypes);
		}
		throw new DBRuntimeException("The db don't support this operation");
	}

	@SuppressWarnings("unchecked")
	public List<Bean> findBeans(String sql, String beanType ,String schema) throws SQLException {
		return jdbcTemplate.query(sql, new BeanRowMapper(beanType,schema));
	}

	@SuppressWarnings("unchecked")
	public List<Bean> findBeans(String sql, String beanType,String schema,
			Object... parameters) {
		return jdbcTemplate.query(sql, parameters, new BeanRowMapper(beanType,schema));
	}

	@SuppressWarnings("unchecked")
	public List<Bean> findBeansByList(String sql, String beanType,String schema,
			List<Object> parameters) {
		return jdbcTemplate.query(sql, parameters.toArray(), new BeanRowMapper(
				beanType,schema));
	}
	
	
	public Object queryObject(String sql, String beanType,String schema,
			Object... parameters) {
		return jdbcTemplate.queryForObject(sql, parameters, new BeanRowMapper(
				beanType,schema));
	}


	private boolean supportsLimit() {
		return dialect.supportsLimit();
	}

	private String getLimitString(String sql, int start, int limit) {
		return dialect.getLimitString(sql, start, limit);
	}

	@SuppressWarnings("unchecked")
	public List<Bean> findBeansByMap(String sql, String beanType,String schema,
			Map<String, Object> parameters, List<Integer> dataTypes) {
		StringBuffer buf = new StringBuffer();
		List<Object> paraList = getParamArray(sql, parameters, buf);
		int[] types = getDataTypes(dataTypes);
		if(types!=null&&types.length>0){
			return jdbcTemplate.query(buf.toString(), paraList.toArray(),types, new BeanRowMapper(
					beanType,schema));
		}else{
			return jdbcTemplate.query(buf.toString(), paraList.toArray(), new BeanRowMapper(
					beanType,schema));
		}
		
	}
	public Object queryObjectByMap(String sql, String beanType,String schema,
			Map<String, Object> parameters, List<Integer> dataTypes) {
		StringBuffer buf = new StringBuffer();
		List<Object> paraList = getParamArray(sql, parameters, buf);
		int[] types = getDataTypes(dataTypes);
		if(types!=null&&types.length>0){
			return jdbcTemplate.queryForObject(buf.toString(), paraList.toArray(),types, new BeanRowMapper(
					beanType,schema));
		}else{
			return jdbcTemplate.queryForObject(buf.toString(), paraList.toArray(), new BeanRowMapper(
					beanType,schema));
		}
		
	}
	
	public int executeByMap(String sql,Map<String, Object> parameters, List<Integer> dataTypes){
		StringBuffer buf = new StringBuffer();
		List<Object> paraList = getParamArray(sql, parameters, buf);
		int[] types = getDataTypes(dataTypes);
		if(types!=null&&types.length>0){
			return jdbcTemplate.update(sql, paraList.toArray(), types);
		}else{
			return jdbcTemplate.update(sql, paraList.toArray());
		}
	}
	
	public int executeByArray(String sql,Object... parameters){
		return jdbcTemplate.update(sql, parameters);
	}
	
	public int queryForInt(String sql,Object... parameters){
		 return jdbcTemplate.queryForInt(sql, parameters);
	}
	
	public int queryForIntByList(String sql,List<Object> parameters){
		 return jdbcTemplate.queryForInt(sql, parameters.toArray());
	}
	
	public int queryForIntByMap(String sql,Map<String, Object> parameters){
		StringBuffer buf = new StringBuffer();
		List<Object> paraList = getParamArray(sql, parameters, buf);
		return jdbcTemplate.queryForInt(sql, paraList.toArray());
	}

	private int[] getDataTypes(List<Integer> dataTypes) {
		int[] types=null;
		if(dataTypes!=null){
			types=new int[dataTypes.size()];
			for (int i = 0; i < dataTypes.size(); i++) {
				types[i]=dataTypes.get(i);
			}
		}
		return types;
	}

	private List<Object> getParamArray(String sql,
			Map<String, Object> parameters, StringBuffer buf) {
		ArrayList<Object> paraList = new ArrayList<Object>();
		String patternStr = "([\"](.*?)[\"])|([\'](.*?)[\'])|([@][a-zA-Z_$][\\w$]*)";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(sql);
		int curpos = 0;
		while (matcher.find()) {
			String replaceStr = matcher.group();
			String variable = replaceStr.substring(1, replaceStr.length());
			if (!replaceStr.startsWith("\"") && !replaceStr.startsWith("'")
					&& parameters != null && parameters.containsKey(variable)) {
				buf.append(sql.substring(curpos, matcher.start()));
				curpos = matcher.end();
				paraList.add(parameters.get(variable));
				buf.append("?");
			}
			continue;
		}
		buf.append(sql.substring(curpos));
		return paraList;
	}

}
