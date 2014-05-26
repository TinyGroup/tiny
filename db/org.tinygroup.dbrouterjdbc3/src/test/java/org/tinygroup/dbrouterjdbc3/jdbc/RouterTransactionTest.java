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
import java.sql.Statement;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.tinygroup.dbrouter.RouterManager;
import org.tinygroup.dbrouter.StatementProcessor;
import org.tinygroup.dbrouter.config.Router;
import org.tinygroup.dbrouter.factory.RouterManagerBeanFactory;
import org.tinygroup.dbrouterjdbc3.sqlprocessor.SqlProcessorFunction;

/**
 * 
 * 功能说明:
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-12-24 <br>
 * <br>
 */
public class RouterTransactionTest extends TestCase {
	Connection conn;
	RouterManager routerManager;
	Router router;

	protected void setUp() throws Exception {
		super.setUp();
		routerManager = RouterManagerBeanFactory.getManager();
		routerManager.addRouters("/differentSchemaShard.xml");
		StatementProcessor processor = new SqlProcessorFunction();
		routerManager.addStatementProcessor(processor);
		router = routerManager.getRouter("diffSchemaShard");
		Class.forName("org.tinygroup.dbrouterjdbc3.jdbc.TinyDriver");
		conn = DriverManager.getConnection("jdbc:dbrouter://diffSchemaShard", "luog",
				"123456");

	}

	protected void tearDown() throws Exception {
		conn.close();
	}

	public void testTransactionSuccess() throws Exception {
		conn.setAutoCommit(false);// 开启事务
		Statement stmt = conn.createStatement();
		try {
			String sql;
			stmt.execute("delete from aaa");
			// 插入100条数据
			for (int i = 0; i < 10; i++) {
				sql = "insert into aaa(id,aaa) values ("
						+ (routerManager.getPrimaryKey(router, "aaa"))
						+ ",'ppp')";
				stmt.execute(sql);
			}
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			throw e;
		}
		ResultSet resultSet = stmt.executeQuery("select count(*) from aaa");
		if (resultSet.next()) {
			assertEquals(10, resultSet.getInt(1));
		} else {
			Assert.fail("事务测试错误，数据未提交");
		}
	}

	public void testTransactionFailure() throws Exception {
		Statement stmt = conn.createStatement();
		try {
			stmt.execute("delete from aaa");//删除完数据
			String sql;
			stmt.execute("insert into aaa(id,aaa) values (7,'ppp')");// 先插入一条数据
			conn.setAutoCommit(false);// 开启事务
			// 插入100条数据
			for (int i = 0; i < 10; i++) {
				sql = "insert into aaa(id,aaa) values ("
						+ (i+1)
						+ ",'ppp')";
				stmt.execute(sql);
			}
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
		}
		ResultSet resultSet = stmt.executeQuery("select count(*) from aaa ");
		if (resultSet.next()) {
			assertEquals(1, resultSet.getInt(1));
		} else {
			Assert.fail("事务测试错误，数据未提交");
		}
	}
}
