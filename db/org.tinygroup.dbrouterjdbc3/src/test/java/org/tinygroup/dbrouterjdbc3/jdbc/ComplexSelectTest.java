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
 * 功能说明: 复杂查询测试 开发人员: renhui <br>
 * 开发时间: 2014-2-26 <br>
 */
public class ComplexSelectTest extends TestCase {

	public void testTableShardDiffSchema() throws Exception {
		RouterManager routerManager = RouterManagerBeanFactory.getManager();
		routerManager.addRouters("/differentSchemaShard.xml");
		Class.forName("org.tinygroup.dbrouterjdbc3.jdbc.TinyDriver");
		Connection conn = DriverManager.getConnection(
				"jdbc:dbrouter://diffSchemaShard", "luog", "123456");
		Statement statement=conn.createStatement();
		prepareRecord(statement);
		String sql="select count(*) from aaa where aaa in (select aaa from aaa where id%3=0) order by id";
		ResultSet rs=statement.executeQuery(sql);
		if(rs.next()){
			assertEquals(6, rs.getInt(1));
		}
		sql="select count(*) from aaa a,employee b where a.id=b.id order by a.id";
		rs=statement.executeQuery(sql);
		if(rs.next()){
			assertEquals(20, rs.getInt(1));;
		}
		sql="select count(*) from aaa where id>3 order by id";
		rs=statement.executeQuery(sql);
		if(rs.next()){
			assertEquals(17, rs.getInt(1));;
		}
		sql="select a.aaa from aaa a where a.id>3 union select e.name from employee e where e.id>3";
		rs=statement.executeQuery(sql);
		while(rs.next()){
			System.out.println(rs.getString("aaa"));
		}
		sql="select count(*) from (select e.name from aaa a,employee e where a.id=e.id) bbb where substr(name,5,1)=1";
		rs=statement.executeQuery(sql);
		if(rs.next()){
			assertEquals(11, rs.getInt(1));
		}
		conn.close();
	}
	
	
	public void testTableShardSameSchema() throws Exception {
		RouterManager routerManager = RouterManagerBeanFactory.getManager();
		routerManager.addRouters("/sameSchemaDiffTableWithShard.xml");
		Class.forName("org.tinygroup.dbrouterjdbc3.jdbc.TinyDriver");
		Connection conn = DriverManager.getConnection(
				"jdbc:dbrouter://tableShard", "luog", "123456");
		Statement statement=conn.createStatement();
		prepareRecord(statement);
		String sql="select * from aaa where aaa in (select aaa from aaa where id%3=0)";
		ResultSet rs=statement.executeQuery(sql);
		while(rs.next()){
			System.out.println(rs.getString("aaa"));
		}
		conn.close();
	}
	
	
	private void prepareRecord(Statement statement) throws SQLException {
		statement.executeUpdate("delete from aaa");
		statement.executeUpdate("delete from employee");
		for (int i = 1; i <= 20; i++) {
			statement.executeUpdate("insert into aaa(id,aaa) values("+i+",'aaa"+i+"')");
			statement.executeUpdate("insert into employee(id,name,grade,age) values("+i+",'name"+i+"','grade"+i+"',"+(i+10)+")");
		}
	}
	

	public void testPrimarySlaveRouter() throws Exception {

	}
}
