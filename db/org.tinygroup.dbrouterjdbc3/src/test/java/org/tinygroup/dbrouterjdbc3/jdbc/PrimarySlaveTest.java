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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import junit.framework.TestCase;

import org.tinygroup.dbrouter.RouterManager;
import org.tinygroup.dbrouter.factory.RouterManagerBeanFactory;
import org.tinygroup.dbrouterjdbc3.jdbc.util.FileUtil;

public class PrimarySlaveTest extends TestCase {

	private static final String TINY_DRIVER = "org.tinygroup.dbrouterjdbc3.jdbc.TinyDriver"; // tiny驱动
	private static final String DERBY_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver"; // derby驱动

	private static final String ROUTER_CONFIG = "/primarySlave.xml"; // tiny路由配置
	private static final String URL = "jdbc:dbrouter://primarySlave"; // url
	private static final String USERNAME = "luog"; // 用户名
	private static final String PASSWORD = "123456"; // 密码

	private static final String DERBY_DBPATH = "derbydb"; // derby数据库存放目录
	private static final String[] DERBY_DBS = { "db01", "db02", "db03" }; // derby数据库

	private static RouterManager routerManager; // tiny路由管理器
	private static boolean inited; // 是否已初始化

	protected void setUp() throws Exception {
		super.setUp();
		// init(); // 初始化derby数据库和表
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		// destroy(); // 关闭derby数据库，并清理对应文件夹
	}

	public static void main(String[] args) {
		try {
			init(); // 初始化derby数据库和表
			PrimarySlaveTest test = new PrimarySlaveTest();
			test.commonTest();
		} catch (Exception e) {
			throw new RuntimeException("测试失败", e);
		} finally {
			destroy(); // 关闭derby数据库，并清理对应文件夹
		}
	}

	public void test() {
		// if (!inited) { // 未初始化
		// return;
		// }
		//
		// try {
		// commonTest();
		// } catch (Exception e) {
		// destroy(); // 关闭derby数据库，并清理对应文件夹
		// throw new RuntimeException("测试失败", e);
		// }
	}

	private void commonTest() throws Exception {
		Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);

		// 准备数据
		try {
			conn.setAutoCommit(false);
			st.executeUpdate("delete from teacher"); // 清空表teacher
			st.executeUpdate("insert into teacher(id,name) values(1,'zhang')");
			st.executeUpdate("insert into teacher(id,name) values(2,'qian')");
			st.executeUpdate("insert into teacher(id,name) values(3,'sun')");
			st.executeUpdate("insert into teacher(id,name) values(4,'wang')");
			st.executeUpdate("insert into teacher(id,name) values(5,'chen')");
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			throw new RuntimeException("表teacher数据初始化失败!");
		}

		ResultSet rs = st.executeQuery("select count(*) from teacher");
		rs.first();
		assertEquals(5, rs.getInt(1));

		rs = st.executeQuery("select avg(id),sum(id),max(id),min(id) from teacher");
		rs.first();
		assertEquals(3, rs.getInt(1));
		assertEquals(15, rs.getInt(2));
		assertEquals(5, rs.getInt(3));
		assertEquals(1, rs.getInt(4));

		rs = st.executeQuery("select avg(id),sum(id),max(id),min(id) from teacher where id>1 group by id having id<3");
		rs.first();
		assertEquals(2, rs.getInt(1));
		assertEquals(2, rs.getInt(2));
		assertEquals(2, rs.getInt(3));
		assertEquals(2, rs.getInt(4));

		conn.setAutoCommit(false);
		try {
			st = conn.createStatement();
			st.executeUpdate("delete from teacher where id=1");
			rs = st.executeQuery("select count(*) from teacher");
			rs.first();
			assertEquals(4, rs.getInt(1));
		} catch (Exception e) {
			conn.rollback(); // 回滚
		} finally {
			close(conn, st, rs); // 关闭连接
		}

	}

	/**
	 * 初始化derby数据库和表
	 * 
	 * @throws Exception
	 */
	private static void init() {
		if (inited) { // 已初始化
			return;
		}

		try {
			Class.forName(TINY_DRIVER); // 加载tiny数据库驱动
			Class.forName(DERBY_DRIVER); // 加载derby数据库驱动
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("数据库驱动加载失败!", e);
		}

		// 初始化routerManager
		routerManager = RouterManagerBeanFactory.getManager();
		routerManager.addRouters(ROUTER_CONFIG);

		FileUtil.deleteFile(DERBY_DBPATH); // 删除derby数据库路径

		// 建表sql
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE TABLE ");
		sb.append("teacher").append("(");
		sb.append("ID int not null,");
		sb.append("NAME varchar(20))");
		String sql = sb.toString();

		// 在每个数据库中建表
		String derbyDbpath = DERBY_DBPATH;
		if (!derbyDbpath.endsWith("/") && !derbyDbpath.endsWith("\\")) {
			derbyDbpath = derbyDbpath + "/";
		}
		for (String derbyDB : DERBY_DBS) {
			StringBuffer url = new StringBuffer("jdbc:derby:");
			url.append(derbyDbpath + derbyDB);
			url.append(";create=true");

			Connection conn = null;
			Statement st = null;
			try {
				conn = DriverManager.getConnection(url.toString());
				st = conn.createStatement();
				st.execute(sql);
			} catch (SQLException e) {
				destroy(); // 关闭derby数据库，并清理对应文件夹
				throw new RuntimeException("建表失败!", e);
			} finally {
				close(conn, st, null);
			}
		}

		inited = true;
		System.out.println("数据库和表初始化完成！");
	}

	/**
	 * 关闭数据库资源
	 * 
	 * @param conn
	 * @param st
	 * @param rs
	 */
	private static void close(Connection conn, Statement st, ResultSet rs) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
			}
		}
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
			}
		}
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
			}
		}
	}

	/**
	 * 清理derby数据库
	 */
	private static void destroy() {
		try {
			DriverManager.getConnection("jdbc:derby:;shutdown=true"); // 关闭derby数据库
		} catch (SQLException e) {
			// 关闭失败,忽略之
		}

		FileUtil.deleteFile("derby.log"); // 删除derby日志文件
		FileUtil.deleteFile(DERBY_DBPATH); // 删除derby数据库文件夹

		System.out.println("derby数据库清理完成！");
	}

}
