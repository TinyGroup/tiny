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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import junit.framework.TestCase;

import org.tinygroup.dbrouter.RouterManager;
import org.tinygroup.dbrouter.factory.RouterManagerBeanFactory;

public class FieldValueShardRuleTest extends TestCase {

	
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
		routerManager.addRouters("/diffSchemaFieldShardRule.xml");
		Class.forName("org.tinygroup.dbrouterjdbc3.jdbc.TinyDriver");
		conn = DriverManager.getConnection(
				"jdbc:dbrouter://diffSchemaFieldShard", "luog", "123456");
	    stmt = conn.createStatement();
	}

	private static void prepareRecord() throws Exception {
			stmt.executeUpdate("delete from employee");
			for (int i = 0; i <= 2; i++) {
				stmt.executeUpdate("insert into employee(id,name,grade,age) values("+i+",'aaa"+i+"','excellent',10)");
				stmt.executeUpdate("insert into employee(id,name,grade,age) values("+i+",'bbb"+i+"','good',10)");
				stmt.executeUpdate("insert into employee(id,name,grade,age) values("+i+",'ccc"+i+"','poor',10)");
			}
			stmt.executeUpdate("insert into employee(id,name,grade,age) values(10,'ddd','excellent',10)");//匹配所有
	}

	

	protected void setUp() throws Exception {
		super.setUp();
	    prepareRecord();
	}

	public void testSelect() throws SQLException{
		ResultSet rs=stmt.executeQuery("select count(*) from employee where grade='excellent'");//匹配所有
		rs.next();
		assertEquals(6, rs.getInt(1));
		rs=stmt.executeQuery("select count(*) from employee where grade='excellent' and name='aaa0'");//匹配shard0
		rs.next();
		assertEquals(1, rs.getInt(1));
	}
	
	public void testParamSelect() throws SQLException{
		PreparedStatement prepare=conn.prepareStatement("select count(*) from employee where grade=?");
        prepare.setString(1, "excellent");
        ResultSet rs=prepare.executeQuery();
        rs.next();
		assertEquals(6, rs.getInt(1));
		prepare=conn.prepareStatement("select count(*) from employee where grade=? and name=?");
	    prepare.setString(1, "excellent");
	    prepare.setString(2, "aaa0");
	    rs=prepare.executeQuery();
        rs.next();
		assertEquals(1, rs.getInt(1));
	}
	
	public void testParamUpdate() throws SQLException{
		PreparedStatement prepare=conn.prepareStatement("update employee set age=15 where grade=?");
        prepare.setString(1, "excellent");
        int count=prepare.executeUpdate();
		assertEquals(6, count);
		prepare=conn.prepareStatement("update employee set age=15 where grade=? and name=?");
	    prepare.setString(1, "good");
	    prepare.setString(2, "bbb0");
	    count=prepare.executeUpdate();
		assertEquals(1, count);
	}
	
	public void testParamInsert() throws SQLException{
		PreparedStatement prepare=conn.prepareStatement("insert into employee(id,name,grade,age) values(11,'ddd',?,10)");//匹配所有
        prepare.setString(1, "excellent");
        int count=prepare.executeUpdate();
		assertEquals(3, count);
		prepare=conn.prepareStatement("insert into employee(id,name,grade,age) values(12,?,?,10)");//匹配shard1
	    prepare.setString(1, "bbb0");
	    prepare.setString(2, "good");
	    count=prepare.executeUpdate();
		assertEquals(1, count);
	}
	
	public void testParamDelete() throws SQLException{
		PreparedStatement prepare=conn.prepareStatement("delete from employee where grade=?");//匹配所有
        prepare.setString(1, "excellent");
        int count=prepare.executeUpdate();
		assertEquals(6, count);
		prepare=conn.prepareStatement("delete from employee where name=? and grade=?");//匹配shard1
	    prepare.setString(1, "bbb0");
	    prepare.setString(2, "good");
	    count=prepare.executeUpdate();
		assertEquals(1, count);
	}
	
	
	public void testUpdate() throws SQLException{
		int count=stmt.executeUpdate("update employee set age=15 where grade='excellent'");//匹配所有的shard
		assertEquals(6, count);
		count=stmt.executeUpdate("update employee set age=15 where grade='good' and name='bbb0'");//匹配shard1
		assertEquals(1, count);
	}
	
}
