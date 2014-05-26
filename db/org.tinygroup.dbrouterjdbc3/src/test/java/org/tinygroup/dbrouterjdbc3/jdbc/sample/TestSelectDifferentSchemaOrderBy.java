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
package org.tinygroup.dbrouterjdbc3.jdbc.sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.tinygroup.dbrouter.RouterManager;
import org.tinygroup.dbrouter.config.Router;
import org.tinygroup.dbrouter.factory.RouterManagerBeanFactory;

public class TestSelectDifferentSchemaOrderBy {
	public static void main(String[] args) throws Throwable {
		testOrderByAscAbsolute(10);
	}

	private static void testOrderByDesc() throws ClassNotFoundException,
			SQLException {
		RouterManager routerManager = RouterManagerBeanFactory.getManager();
		Router router = TestRouterUtil.getDifferentSchemaRouter();
		routerManager.addRouter(router);
		Class.forName("org.tinygroup.dbrouterjdbc3.jdbc.TinyDriver");
		Connection conn = DriverManager.getConnection(
				"jdbc:dbrouter://router1", "luog", "123456");
		Statement stmt = conn.createStatement();
		String sql = "select * from aaa order by id desc";
		ResultSet resultSet = stmt.executeQuery(sql);
		while (resultSet.next()) {
			System.out.printf(" id: %d, aaa: %s \n", resultSet.getInt(1),
					resultSet.getString(2));
		}
	}

	private static void testOrderByAsc() throws ClassNotFoundException,
			SQLException {
		RouterManager routerManager = RouterManagerBeanFactory.getManager();
		Router router = TestRouterUtil.getDifferentSchemaRouter();
		routerManager.addRouter(router);
		Class.forName("org.tinygroup.dbrouterjdbc3.jdbc.TinyDriver");
		Connection conn = DriverManager.getConnection(
				"jdbc:dbrouter://router1", "luog", "123456");
		Statement stmt = conn.createStatement();
		String sql = "select * from aaa order by id";
		ResultSet resultSet = stmt.executeQuery(sql);
		while (resultSet.next()) {
			System.out.printf(" id: %d, aaa: %s \n", resultSet.getInt(1),
					resultSet.getString(2));
		}
	}

	private static void testOrderByAscFromLast() throws ClassNotFoundException,
			SQLException {
		RouterManager routerManager = RouterManagerBeanFactory.getManager();
		Router router = TestRouterUtil.getDifferentSchemaRouter();
		routerManager.addRouter(router);
		Class.forName("org.tinygroup.dbrouterjdbc3.jdbc.TinyDriver");
		Connection conn = DriverManager.getConnection(
				"jdbc:dbrouter://router1", "luog", "123456");
		Statement stmt = conn.createStatement();
		String sql = "select * from aaa order by id";
		ResultSet resultSet = stmt.executeQuery(sql);
		resultSet.afterLast();
		while (resultSet.previous()) {
			System.out.printf(" id: %d, aaa: %s \n", resultSet.getInt(1),
					resultSet.getString(2));
		}
	}

	private static void testOrderByDescFromLast()
			throws ClassNotFoundException, SQLException {
		RouterManager routerManager = RouterManagerBeanFactory.getManager();
		Router router = TestRouterUtil.getDifferentSchemaRouter();
		routerManager.addRouter(router);
		System.out.println("Hello, World.");
		Class.forName("org.tinygroup.dbrouterjdbc3.jdbc.TinyDriver");
		Connection conn = DriverManager.getConnection(
				"jdbc:dbrouter://router1", "luog", "123456");
		Statement stmt = conn.createStatement();
		String sql = "select * from aaa order by id desc";
		ResultSet resultSet = stmt.executeQuery(sql);
		resultSet.afterLast();
		while (resultSet.previous()) {
			System.out.printf(" id: %d, aaa: %s \n", resultSet.getInt(1),
					resultSet.getString(2));
		}
	}

	private static void testOrderByDescAbsolute(int rowNumber)
			throws ClassNotFoundException, SQLException {
		RouterManager routerManager = RouterManagerBeanFactory.getManager();
		Router router = TestRouterUtil.getDifferentSchemaRouter();
		routerManager.addRouter(router);
		Class.forName("org.tinygroup.dbrouterjdbc3.jdbc.TinyDriver");
		Connection conn = DriverManager.getConnection(
				"jdbc:dbrouter://router1", "luog", "123456");
		Statement stmt = conn.createStatement();
		String sql = "select * from aaa order by id desc";
		ResultSet resultSet = stmt.executeQuery(sql);
		resultSet.absolute(rowNumber);
		System.out.printf(" id: %d, aaa: %s \n", resultSet.getInt(1),
				resultSet.getString(2));
		while (resultSet.next()) {
			System.out.printf(" id: %d, aaa: %s \n", resultSet.getInt(1),
					resultSet.getString(2));
		}
		while (resultSet.previous()) {
			System.out.printf(" id: %d, aaa: %s \n", resultSet.getInt(1),
					resultSet.getString(2));
		}
	}

	private static void testOrderByAscAbsolute(int rowNumber)
			throws ClassNotFoundException, SQLException {
		RouterManager routerManager = RouterManagerBeanFactory.getManager();
		Router router = TestRouterUtil.getDifferentSchemaRouter();
		routerManager.addRouter(router);
		Class.forName("org.tinygroup.dbrouterjdbc3.jdbc.TinyDriver");
		Connection conn = DriverManager.getConnection(
				"jdbc:dbrouter://router1", "luog", "123456");
		Statement stmt = conn.createStatement();
		String sql = "select * from aaa order by id";
		ResultSet resultSet = stmt.executeQuery(sql);
		resultSet.absolute(rowNumber);
		System.out.printf(" id: %d, aaa: %s \n", resultSet.getInt(1),
				resultSet.getString(2));
		while (resultSet.next()) {
			System.out.printf(" id: %d, aaa: %s \n", resultSet.getInt(1),
					resultSet.getString(2));
		}
		while (resultSet.previous()) {
			System.out.printf(" id: %d, aaa: %s \n", resultSet.getInt(1),
					resultSet.getString(2));
		}
	}

	public static void testMysql(int rowNumber) throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection(
				"jdbc:mysql://192.168.154.90:3306/test0", "root", "123456");
		Statement stmt = conn.createStatement();
		String sql = "select * from aaa order by id desc";
		ResultSet resultSet = stmt.executeQuery(sql);
		resultSet.absolute(rowNumber);
		System.out.printf(" id: %d, aaa: %s \n", resultSet.getInt(1),
				resultSet.getString(2));
		while (resultSet.next()) {
			System.out.printf(" id: %d, aaa: %s \n", resultSet.getInt(1),
					resultSet.getString(2));
		}
		while (resultSet.previous()) {
			System.out.printf(" id: %d, aaa: %s \n", resultSet.getInt(1),
					resultSet.getString(2));
		}
	}
}