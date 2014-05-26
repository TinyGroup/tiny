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
package org.tinygroup.dbrouterjdbc3.jdbc.performance;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import org.enhydra.jdbc.standard.StandardXADataSource;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.threadgroup.AbstractProcessor;
import org.tinygroup.threadgroup.MultiThreadProcessor;
import org.tinygroup.threadgroup.Processor;

public class CommonJDBCPerformance {

	private static Logger logger = LoggerFactory.getLogger(CommonJDBCPerformance.class);

	public static String driverName = "com.mysql.jdbc.Driver";
	public static String url = "jdbc:mysql://mysqldb:3306/test5";
	public static String user = "root";
	public static String password = "123456";
	public static StandardXADataSource dataSource = new StandardXADataSource();

	public static int QUERY_SUM = 100000; // 查询总次数
	public static int QUERY_COUNT = 0; // 已查询次数
	public static Random rand = new Random();
	public static int THREAD_COUNT = 10;
	public static int COUNT = 0;
	public static int SUM = 100000;

	static {
		try {
			dataSource.setUrl(url);
			dataSource.setDriverName(driverName);
			dataSource.setUser(user);
			dataSource.setPassword(password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Connection connect() throws SQLException {
		return dataSource.getXAConnection().getConnection();
	}

	public static int getId() throws SQLException {
		return Math.abs(rand.nextInt());
	}

	public static void close(Connection conn, Statement st, ResultSet rs) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		MultiThreadProcessor processors = new MultiThreadProcessor(
				"thread-group1");

		// for (int i = 0; i < THREAD_COUNT; i++) {
		// Processor processor = new QueryThread("thread" + i); // 添加线程
		// processors.addProcessor(processor);
		// }

		for (int i = 0; i < 5; i++) {
			Processor processor = new InsertThread("thread" + i); // 添加线程
			processors.addProcessor(processor);
		}

		for (int i = 0; i < 5; i++) {
			Processor processor = new QueryThread("thread" + i); // 添加线程
			processors.addProcessor(processor);
		}

		processors.start(); // 启动线程组
	}

	static class InsertThread extends AbstractProcessor {

		public InsertThread(String name) {
			super(name);
		}

		protected void action() throws Exception {
			Connection conn = connect();
			Statement st = conn.createStatement();
			int id = 0;
			long start = 0;
			for (; COUNT < SUM;) {
				id = COUNT++;
				id = rand.nextInt(SUM);
				st.executeUpdate("insert into student(id,name) values(" + id
						+ ",'zhang')");
				logger.logMessage(LogLevel.INFO, "插入数据，id=" + id + "，花费时间="
						+ (System.currentTimeMillis() - start));
			}
			close(conn, st, null);
		}
	}

	static class QueryThread extends AbstractProcessor {

		public QueryThread(String name) {
			super(name);
		}

		protected void action() throws Exception {
			Connection conn = connect();
			Statement st = conn.createStatement();
			ResultSet rs = null;
			int id = 0;
			long start = 0;
			for (; QUERY_COUNT++ < QUERY_SUM;) {
				id = rand.nextInt(SUM);
				start = System.currentTimeMillis();
				rs = st.executeQuery("select count(*) from student where id="
						+ id);
				if (rs.first()) {
					logger.logMessage(LogLevel.INFO,
							"查询数据，id=" + id + "，count=" + rs.getInt(1)
									+ "，花费时间="
									+ (System.currentTimeMillis() - start));
				} else {
					logger.logMessage(
							LogLevel.INFO,
							"查询数据，id=" + id + "，未找到该记录" + "，花费时间="
									+ (System.currentTimeMillis() - start));
				}
			}
		}
	}

}
