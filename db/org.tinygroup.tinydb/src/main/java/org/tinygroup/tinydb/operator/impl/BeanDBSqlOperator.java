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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.exception.DBRuntimeException;
import org.tinygroup.tinydb.operator.DBOperator;
import org.tinygroup.tinydb.order.OrderBean;
import org.tinygroup.tinydb.query.Conditions;
import org.tinygroup.tinydb.select.SelectBean;
import org.tinygroup.tinydb.util.TinyDBUtil;

public class BeanDBSqlOperator<K> extends BeanDBBatchOperator<K> implements
		DBOperator<K> {

	public BeanDBSqlOperator(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate);
	}

	public Bean[] getBeans(String sql) {
		try {
			return TinyDBUtil
					.collectionToArray(queryBean(buildSqlFuction(sql)));
		} catch (SQLException e) {
			throw new DBRuntimeException(e);
		}
	}

	public Bean[] getPagedBeans(String sql, int start, int limit) {
		return getPagedBeans(buildSqlFuction(sql), start, limit,
				new HashMap<String, Object>());
	}

	public Bean[] getBeans(String sql, Map<String, Object> parameters) {
		List<Bean> beans = findBeansByMap(buildSqlFuction(sql), getBeanType(),
				getSchema(), parameters, new ArrayList<Integer>());
		for (Bean bean : beans) {
			processRelation(bean, getRelation(), new QueryRelationCallBack());
		}
		return TinyDBUtil.collectionToArray(beans);
	}

	public Bean[] getPagedBeans(String sql, int start, int limit,
			Map<String, Object> parameters) {
		List<Bean> beans = findBeansByMapForPage(buildSqlFuction(sql),
				getBeanType(), getSchema(), start, limit, parameters,
				new ArrayList<Integer>());
		for (Bean bean : beans) {
			processRelation(bean, getRelation(), new QueryRelationCallBack());
		}
		return TinyDBUtil.collectionToArray(beans);
	}

	public Bean[] getBeans(String sql, Object... parameters) {
		List<Bean> beans = findBeans(buildSqlFuction(sql), getBeanType(),
				getSchema(), parameters);
		for (Bean bean : beans) {
			processRelation(bean, getRelation(), new QueryRelationCallBack());
		}
		return TinyDBUtil.collectionToArray(beans);
	}

	public Bean[] getBeans(String sql, List<Object> parameters) {
		List<Bean> beans = findBeansByList(buildSqlFuction(sql), getBeanType(),
				getSchema(), parameters);
		for (Bean bean : beans) {
			processRelation(bean, getRelation(), new QueryRelationCallBack());
		}
		return TinyDBUtil.collectionToArray(beans);
	}

	public Bean[] getPagedBeans(String sql, int start, int limit,
			Object... parameters) {
		List<Object> params = new ArrayList<Object>();
		if (parameters != null) {
			for (Object obj : parameters) {
				params.add(obj);
			}
		}

		List<Bean> beans = findBeansByListForPage(buildSqlFuction(sql),
				getBeanType(), getSchema(), start, limit, params);
		for (Bean bean : beans) {
			processRelation(bean, getRelation(), new QueryRelationCallBack());
		}
		return TinyDBUtil.collectionToArray(beans);
	}

	public Bean getSingleValue(String sql) {
		Bean bean = (Bean) queryObject(buildSqlFuction(sql), getBeanType(),
				getSchema());
		processRelation(bean, getRelation(), new QueryRelationCallBack());
		return bean;
	}

	public Bean getSingleValue(String sql, Map<String, Object> parameters) {
		Bean bean = (Bean) queryObjectByMap(buildSqlFuction(sql),
				getBeanType(), getSchema(), parameters, null);
		processRelation(bean, getRelation(), new QueryRelationCallBack());
		return bean;
	}

	public Bean getSingleValue(String sql, Object... parameters) {
		Bean bean = (Bean) queryObject(buildSqlFuction(sql), getBeanType(),
				getSchema(), parameters);
		processRelation(bean, getRelation(), new QueryRelationCallBack());
		return bean;
	}

	public Bean getSingleValue(String sql, List<Object> parameters) {
		Bean bean = (Bean) queryObject(buildSqlFuction(sql), getBeanType(),
				getSchema(), parameters.toArray());
		processRelation(bean, getRelation(), new QueryRelationCallBack());
		return bean;
	}

	private List<Bean> queryBean(String sql) throws SQLException {
		List<Object> params = new ArrayList<Object>();
		List<Bean> beans = findBeansByList(sql, getBeanType(), getSchema(),
				params);
		for (Bean bean : beans) {
			processRelation(bean, getRelation(), new QueryRelationCallBack());
		}
		return beans;
	}

	/**
	 * 产生SQL语句
	 * 
	 * @param queryBean
	 * @param stringBuffer
	 *            用于存放SQL
	 * @param valueList
	 *            用于存放值列表
	 */
	public void generateQuerySqlClause(Conditions conditions,
			StringBuffer stringBuffer, List<Object> valueList) {
		stringBuffer.append(conditions.getSegmentWithVariable());
		valueList.addAll(conditions.getParamterList());
	}

	/**
	 * 生成查询部分的sql片段
	 * 
	 * @param selectBeans
	 * @param stringBuffer
	 */
	public void generateSelectSqlClause(SelectBean[] selectBeans,
			StringBuffer stringBuffer) {
		if (selectBeans != null && selectBeans.length > 0) {
			for (int i = 0; i < selectBeans.length; i++) {
				SelectBean selectBean = selectBeans[i];
				stringBuffer.append(selectBean.getSelectClause());
				if (i < selectBeans.length - 1) {
					stringBuffer.append(",");
				}
			}
		} else {
			stringBuffer.append("*");
		}
	}

	/**
	 * 生成order by 子句
	 * 
	 * @param orderBeans
	 * @param stringBuffer
	 */
	public void generateOrderSqlClause(OrderBean[] orderBeans,
			StringBuffer stringBuffer) {
		if (orderBeans != null && orderBeans.length > 0) {
			stringBuffer.append(" order by ");
			for (int i = 0; i < orderBeans.length; i++) {
				OrderBean orderBean = orderBeans[i];
				stringBuffer
						.append(getBeanDbNameConverter()
								.propertyNameToDbFieldName(
										orderBean.getPropertyName()))
						.append(" ").append(orderBean.getOrderMode())
						.append(" ");
				if (i < orderBeans.length - 1) {
					stringBuffer.append(",");
				}
			}
		}
	}

	/**
	 * 生成sql语句
	 * 
	 * @param selectBeans
	 * @param queryBean
	 * @param orderBeans
	 * @param valueList
	 */
	public String generateSqlClause(SelectBean[] selectBeans,
			Conditions conditions, OrderBean[] orderBeans,
			List<Object> valueList) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("select ");
		generateSelectSqlClause(selectBeans, stringBuffer);
		stringBuffer.append(" from ").append(
				getBeanDbNameConverter().typeNameToDbTableName(getBeanType()));
		stringBuffer.append(" where ");
		generateQuerySqlClause(conditions, stringBuffer, valueList);
		generateOrderSqlClause(orderBeans, stringBuffer);
		return stringBuffer.toString();
	}

	public String generateSqlClause(String selectClause, Conditions conditions,
			OrderBean[] orderBeans, List<Object> valueList) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("select ").append(selectClause);
		stringBuffer.append(" from ").append(
				getBeanDbNameConverter().typeNameToDbTableName(getBeanType()));
		stringBuffer.append(" where ");
		generateQuerySqlClause(conditions, stringBuffer, valueList);
		generateOrderSqlClause(orderBeans, stringBuffer);
		return stringBuffer.toString();
	}

	public int execute(String sql, Map<String, Object> parameters) {
		return executeByMap(buildSqlFuction(sql), parameters, null);
	}

	public int execute(String sql, Object... parameters) {
		return executeByArray(buildSqlFuction(sql), parameters);
	}

	public int execute(String sql, List<Object> parameters) {
		return executeByList(buildSqlFuction(sql), parameters, null);
	}

	private String buildSqlFuction(String sql) {
		return getDialect().buildSqlFuction(sql);
	}

	public int account(String sql, Object... parameters) {
		return queryForInt(buildSqlFuction(sql), parameters);
	}

	public int account(String sql, List<Object> parameters) {
		return queryForIntByList(buildSqlFuction(sql), parameters);
	}

	public int account(String sql, Map<String, Object> parameters) {
		return queryForIntByMap(buildSqlFuction(sql), parameters);
	}

	public Bean[] getBeans(SelectBean[] selectBeans, Conditions conditions,
			OrderBean[] orderBeans) {
		List<Object> valueList = new ArrayList<Object>();
		String sql = this.generateSqlClause(selectBeans, conditions,
				orderBeans, valueList);
		return getBeans(sql, TinyDBUtil.collectionToArray(valueList));
	}

	public Bean[] getBeans(SelectBean[] selectBeans, Conditions conditions,
			OrderBean[] orderBeans, int start, int limit) {
		List<Object> valueList = new ArrayList<Object>();
		String sql = this.generateSqlClause(selectBeans, conditions,
				orderBeans, valueList);
		return getPagedBeans(sql, start, limit,
				TinyDBUtil.collectionToArray(valueList));
	}

	public Bean[] getBeans(Conditions conditions, OrderBean[] orderBeans,
			int start, int limit) {
		SelectBean[] selectBeans = new SelectBean[0];
		return getBeans(selectBeans, conditions, orderBeans, start, limit);
	}

	public Bean getSingleValue(Conditions conditions) {
		SelectBean[] selectBeans = new SelectBean[0];
		return (Bean) getSingleValue(selectBeans, conditions);
	}

	public Bean getSingleValue(SelectBean[] selectBeans, Conditions conditions) {
		List<Object> valueList = new ArrayList<Object>();
		String sql = this.generateSqlClause(selectBeans, conditions, null,
				valueList);
		Bean bean = (Bean) queryObject(sql, getBeanType(), getSchema(),
				valueList.toArray());
		processRelation(bean, getRelation(), new QueryRelationCallBack());
		return bean;
	}

	public Bean[] getBeans(String selectClause, Conditions conditions,
			OrderBean[] orderBeans) {
		List<Object> valueList = new ArrayList<Object>();
		String sql = this.generateSqlClause(selectClause, conditions,
				orderBeans, valueList);
		return getBeans(sql, TinyDBUtil.collectionToArray(valueList));
	}

	public Bean[] getBeans(String selectClause, Conditions conditions,
			OrderBean[] orderBeans, int start, int limit) {
		List<Object> valueList = new ArrayList<Object>();
		String sql = this.generateSqlClause(selectClause, conditions,
				orderBeans, valueList);
		return getPagedBeans(sql, start, limit,
				TinyDBUtil.collectionToArray(valueList));
	}

	public Bean getSingleValue(String selectClause, Conditions conditions) {
		List<Object> valueList = new ArrayList<Object>();
		String sql = this.generateSqlClause(selectClause, conditions, null,
				valueList);
		Bean bean = (Bean) queryObject(sql, getBeanType(), getSchema(),
				valueList.toArray());
		processRelation(bean, getRelation(), new QueryRelationCallBack());
		return bean;
	}

	public Bean[] getBeans(Conditions conditions, OrderBean[] orderBeans) {
		SelectBean[] selectBeans = new SelectBean[0];
		return getBeans(selectBeans, conditions, orderBeans);
	}

}
