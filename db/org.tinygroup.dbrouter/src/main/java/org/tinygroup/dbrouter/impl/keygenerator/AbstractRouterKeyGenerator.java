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
package org.tinygroup.dbrouter.impl.keygenerator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.tinygroup.dbrouter.RouterKeyGenerator;
import org.tinygroup.dbrouter.config.KeyGeneratorConfig;
import org.tinygroup.dbrouter.config.Router;
import org.tinygroup.dbrouter.exception.DbrouterRuntimeException;
import org.tinygroup.dbrouter.util.DbRouterUtil;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 功能说明:集群主键生成器的抽象类
 * <p/>
 * <p/>
 * 开发人员: renhui <br>
 * 开发时间: 2014-1-6 <br>
 * <br>
 */
public abstract class AbstractRouterKeyGenerator<T extends Number> implements
		RouterKeyGenerator<T> {

	protected static final String END_NUMBER = "end_number";

	transient protected Router router;

	transient protected KeyGeneratorConfig keyConfig;

	transient private Connection connection = null;
	// 表名：主键配置
	protected Map<String, KeyConfigArea> caches = new HashMap<String, KeyConfigArea>();

	private static Logger logger = LoggerFactory
			.getLogger(AbstractRouterKeyGenerator.class);

	public T getKey(String tableName) {
		KeyConfigArea area = caches.get(tableName);
		if (area == null) {
			area = new KeyConfigArea();
			updateKey(tableName, area, new WithNoResultCallBack() {

				public void callback(String tableName, Statement statement)
						throws SQLException {
					String sql = "insert into " + keyConfig.getKeyTableName()
							+ "(end_number,table_name) values("
							+ keyConfig.getStep() + ",'" + tableName + "')";
					statement.executeUpdate(sql);
				}
			});
			caches.put(tableName, area);
		}
		if (area.checkUpdateKey()) {
			updateKey(tableName, area, new WithNoResultCallBack() {

				public void callback(String tableName, Statement statement)
						throws SQLException {
					throw new DbrouterRuntimeException("集群主键表:"
							+ keyConfig.getKeyTableName() + "查询不到" + tableName
							+ "的记录");
				}
			});
		}
		long nowCurrentNumber = area.getCurrentNumber()
				+ keyConfig.getIncrement();
		area.setCurrentNumber(nowCurrentNumber);
		return generatorNextKey(nowCurrentNumber);
	}

	private synchronized void updateKey(String tableName, KeyConfigArea area,
			WithNoResultCallBack callback) {
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			if (connection == null || connection.isClosed()) {
				connection = DbRouterUtil.createConnection(router
						.getDataSourceConfig(keyConfig.getDataSourceId()));
			}
			String generatorTableName = keyConfig.getKeyTableName();
			statement = connection.createStatement();
			resultSet = statement.executeQuery("select * from "
					+ generatorTableName + " where table_name='" + tableName
					+ "'");
			if (resultSet.next()) {
				long oldEndNumber = resultSet.getLong(END_NUMBER);
				long newEndNumber = oldEndNumber + keyConfig.getStep();
				String sql = "update " + generatorTableName
						+ " set end_number=" + newEndNumber
						+ " where table_name='" + tableName + "'";
				statement.executeUpdate(sql);
				area.setCurrentNumber(oldEndNumber);
				area.setEndNumber(newEndNumber);
			} else {
				callback.callback(tableName, statement);
				area.setEndNumber(keyConfig.getStep());
				area.setCurrentNumber(0);
			}
		} catch (SQLException e) {
			logger.errorMessage("获取表" + tableName + "的主键时发生异常", e);
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException ex) {
					logger.errorMessage("关闭连接时发生异常！", ex);
					throw new DbrouterRuntimeException(ex);
				}
			}
			throw new DbrouterRuntimeException(e);
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (resultSet != null) {
					resultSet.close();
				}
			} catch (SQLException ex) {
				logger.errorMessage(ex.getMessage(), ex);
				throw new DbrouterRuntimeException(ex);
			}
		}
	}

	/**
	 * 获取下一个key值
	 * 
	 * @param currentNumber
	 * @return
	 */
	protected abstract T generatorNextKey(Long currentNumber);

	public void setRouter(Router router) {
		this.router = router;
		keyConfig = router.getKeyConfig();
	}

	class KeyConfigArea {
		private long currentNumber;// 当前key值
		private long endNumber;// 范围

		public long getCurrentNumber() {
			return currentNumber;
		}

		public void setCurrentNumber(long currentNumber) {
			this.currentNumber = currentNumber;
		}

		public long getEndNumber() {
			return endNumber;
		}

		public void setEndNumber(long endNumber) {
			this.endNumber = endNumber;
		}

		public boolean checkUpdateKey() {
			return currentNumber + keyConfig.getIncrement() - endNumber > 0;
		}

	}

	/**
	 * 
	 * 查询记录不存在的回调操作
	 */
	interface WithNoResultCallBack {
		void callback(String tableName, Statement statement)
				throws SQLException;
	}

}
