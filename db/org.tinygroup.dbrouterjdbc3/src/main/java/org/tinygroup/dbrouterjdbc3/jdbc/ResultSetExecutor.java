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

import java.sql.ResultSet;
import java.sql.SQLException;

import org.tinygroup.dbrouter.RouterManager;
import org.tinygroup.dbrouter.config.Partition;
import org.tinygroup.dbrouter.config.Shard;
import org.tinygroup.dbrouter.factory.RouterManagerBeanFactory;
import org.tinygroup.dbrouter.util.OrderByProcessor;
import org.tinygroup.dbrouter.util.OrderByProcessor.OrderByValues;
import org.tinygroup.dbrouter.util.SortOrder;
import org.tinygroup.jsqlparser.statement.select.PlainSelect;
import org.tinygroup.jsqlparser.statement.select.Select;
import org.tinygroup.jsqlparser.statement.select.SelectBody;

public class ResultSetExecutor {

	private ResultSet resultSet;

	private String executeSql;

	private String originalSql;

	private boolean isAfterLast;

	private boolean isBeforeFirst;

	private OrderByProcessor orderByProcessor;

	private Shard shard;
	
	private Partition partition;

	private RouterManager routerManager = RouterManagerBeanFactory.getManager();

	public ResultSetExecutor(ResultSet resultSet, String executeSql,
			String originalSql, Shard shard,Partition partition) throws SQLException {
		super();
		this.resultSet = resultSet;
		this.executeSql = executeSql;
		this.originalSql = originalSql;
		this.shard = shard;
		this.partition=partition;
		org.tinygroup.jsqlparser.statement.Statement sqlStatement = routerManager
				.getSqlStatement(executeSql);
		if (sqlStatement instanceof Select) {
			Select select = (Select) sqlStatement;
			SelectBody body = select.getSelectBody();
			if (body instanceof PlainSelect) {
				PlainSelect plainSelect = (PlainSelect) body;
				orderByProcessor = new OrderByProcessor(plainSelect, resultSet);
			}
		} else {
			throw new RuntimeException("must be a query sql");
		}
	}

	public SortOrder getSortOrder() {
		if (orderByProcessor == null) {
			return null;
		}
		return orderByProcessor.getSortOrder();
	}

	public ResultSet getResultSet() {
		return resultSet;
	}

	public String getExecuteSql() {
		return executeSql;
	}

	public boolean[] getOrderTypes() {
		if (orderByProcessor != null) {
			return orderByProcessor.getOrderTypes();
		}
		return null;
	}

	public int[] getOrderByIndexs() {
		if (orderByProcessor != null) {
			return orderByProcessor.getOrderByIndexs();
		}
		return null;
	}

	public boolean next() throws SQLException {
		if (!isAfterLast) {
			return resultSet.next();
		}
		return false;

	}

	public boolean previous() throws SQLException {
		if (!isBeforeFirst) {
			return resultSet.previous();
		}
		return false;
	}

	public OrderByValues getOrderByValuesFromResultSet() throws SQLException {
		orderByProcessor.setValues(resultSet);
		return orderByProcessor.getValueCache();
	}

	public OrderByValues getValueCache() {
		return orderByProcessor.getValueCache();
	}

	public void setValueCache(OrderByValues valueCache) {
		orderByProcessor.setValueCache(valueCache);
	}

	public boolean isAfterLast() {
		return isAfterLast;
	}

	public void setAfterLast(boolean isAfterLast) {
		this.isAfterLast = isAfterLast;
	}

	public boolean isBeforeFirst() {
		return isBeforeFirst;
	}

	public void setBeforeFirst(boolean isBeforeFirst) {
		this.isBeforeFirst = isBeforeFirst;
	}

	public void beforeFirst() throws SQLException {
		resultSet.beforeFirst();
		orderByProcessor.clearValueCache();
		isBeforeFirst = true;
		isAfterLast = false;
	}

	public void afterLast() throws SQLException {
		resultSet.afterLast();
		orderByProcessor.clearValueCache();
		isAfterLast = true;
		isBeforeFirst = false;
	}

	public void first() throws SQLException {
		resultSet.first();
		orderByProcessor.clearValueCache();
		isAfterLast = false;
		isBeforeFirst = false;
	}

	public void last() throws SQLException {
		resultSet.last();
		orderByProcessor.clearValueCache();
		isAfterLast = false;
		isBeforeFirst = false;
	}

	public Shard getShard() {
		return shard;
	}
	

	public Partition getPartition() {
		return partition;
	}

	public String getOriginalSql() {
		return originalSql;
	}

}