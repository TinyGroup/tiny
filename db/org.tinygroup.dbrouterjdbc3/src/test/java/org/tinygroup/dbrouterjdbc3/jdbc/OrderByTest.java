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

public class OrderByTest extends TestCase {

	static Connection conn;
	static Statement stmt;
	static {
		try {
			prepareRecord();
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	protected void setUp() throws Exception {
		stmt.executeUpdate("delete from aaa");
		for (int i = 1; i <= 20; i++) {
			stmt.executeUpdate("insert into aaa(id,aaa) values(" + i
					+ ",'aaa')");
		}
	}


	private static void prepareRecord() throws Exception {
		RouterManager routerManager = RouterManagerBeanFactory.getManager();
		routerManager.addRouters("/differentSchemaShard.xml");
		Class.forName("org.tinygroup.dbrouterjdbc3.jdbc.TinyDriver");
		conn = DriverManager.getConnection(
				"jdbc:dbrouter://diffSchemaShard", "luog", "123456");
		stmt = conn.createStatement();
	}


	public void testAsc() throws SQLException {
		String sql = "select * from aaa order by id";
		ResultSet resultSet = stmt.executeQuery(sql);
		if (resultSet.next()) {
			assertEquals(1, resultSet.getInt("id"));
		}
	}

	public void testDesc() throws SQLException {
		String sql = "select * from aaa order by id desc";
		ResultSet resultSet = stmt.executeQuery(sql);
		if (resultSet.next()) {
			assertEquals(20, resultSet.getInt("id"));
		}
	}

	public void testAscFromLast() throws SQLException {
		String sql = "select * from aaa order by id";
		ResultSet resultSet = stmt.executeQuery(sql);
		resultSet.afterLast();
		if (resultSet.previous()) {
			assertEquals(20, resultSet.getInt("id"));
		}
	}

	public void testDescFromLast() throws SQLException {
		String sql = "select * from aaa order by id desc";
		ResultSet resultSet = stmt.executeQuery(sql);
		resultSet.afterLast();
		if (resultSet.previous()) {
			assertEquals(1, resultSet.getInt("id"));
		}
	}

	public void testDescAbsolute() throws SQLException {
		String sql = "select * from aaa order by id desc";
		ResultSet resultSet = stmt.executeQuery(sql);
		resultSet.absolute(-10);
		assertEquals(10, resultSet.getInt("id"));
		resultSet.next();
		assertEquals(9, resultSet.getInt("id"));
	}

	public void testAscAbsolute() throws SQLException {
		String sql = "select * from aaa order by id";
		ResultSet resultSet = stmt.executeQuery(sql);
		resultSet.absolute(10);
		assertEquals(10, resultSet.getInt("id"));
		resultSet.previous();
		assertEquals(9, resultSet.getInt("id"));
	}

	public void testFirstAndLast() throws SQLException {
		String sql = "select * from aaa order by id";
		ResultSet resultSet = stmt.executeQuery(sql);
		resultSet.absolute(10);
		assertEquals(10, resultSet.getInt("id"));
		resultSet.first();
		assertTrue(resultSet.isFirst());
		assertEquals(1, resultSet.getInt("id"));
		resultSet.last();
		assertTrue(resultSet.isLast());
		assertEquals(20, resultSet.getInt("id"));

	}

}
