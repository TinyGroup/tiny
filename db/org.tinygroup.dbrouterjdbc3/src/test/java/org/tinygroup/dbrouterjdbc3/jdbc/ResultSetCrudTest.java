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

import org.tinygroup.dbrouter.RouterManager;
import org.tinygroup.dbrouter.factory.RouterManagerBeanFactory;

import junit.framework.TestCase;


/**
 * 
 * 功能说明: resultset crud操作测试类

 * 开发人员: renhui <br>
 * 开发时间: 2014-1-9 <br>
 * <br>
 */
public class ResultSetCrudTest extends TestCase {
	
	static Connection conn;
	static Statement stmt;
	static {
		try {
			initConnection();
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
	
	private static void initConnection()throws Exception {
		RouterManager routerManager = RouterManagerBeanFactory.getManager();
		routerManager.addRouters("/differentSchemaShard.xml");
		Class.forName("org.tinygroup.dbrouterjdbc3.jdbc.TinyDriver");
		conn = DriverManager.getConnection(
				"jdbc:dbrouter://diffSchemaShard", "luog", "123456");
		stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, 
                ResultSet.CONCUR_UPDATABLE);
	}

	public void testInsert() throws SQLException{
		stmt.executeUpdate("delete from aaa");
		String sql = "select * from aaa order by id";
		ResultSet rs=stmt.executeQuery(sql);
		for (int i = 1; i < 10; i++) {
			rs.moveToInsertRow();
			rs.updateLong("id", i);
			rs.updateString("aaa", "ppp"+i);
			rs.insertRow();
			rs.moveToCurrentRow();
		}
		rs=stmt.executeQuery("select count(*) from aaa");
		rs.next();
		assertEquals(9, rs.getInt(1));
	}
	
	public void testUpdate() throws SQLException{
		prepareRecord();
		String sql = "select * from aaa order by id";
		ResultSet rs=stmt.executeQuery(sql);
		for (int i = 1; i < 10; i++) {
			rs.next();
			rs.updateString("aaa", "ooo"+i);
			rs.updateRow();
		}
		assertEquals("ooo9", rs.getString("aaa"));
	}


	private void prepareRecord() throws SQLException {
		stmt.executeUpdate("delete from aaa");
		for (int i = 1; i < 10; i++) {
			stmt.executeUpdate("insert into aaa(id,aaa) values("+i+",'aaa')");
		}
	}
	
	
	public void testDelete() throws SQLException{
		prepareRecord();
		String sql = "select * from aaa order by id";
		ResultSet rs=stmt.executeQuery(sql);
		for (int i = 1; i < 10; i++) {
			rs.next();
			rs.deleteRow();
		}
		rs=stmt.executeQuery("select count(*) from aaa");
		rs.next();
		assertEquals(0, rs.getInt(1));
		
	}
	
	
	
}
