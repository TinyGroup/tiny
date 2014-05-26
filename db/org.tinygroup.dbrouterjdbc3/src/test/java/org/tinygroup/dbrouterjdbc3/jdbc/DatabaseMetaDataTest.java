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
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import junit.framework.TestCase;

import org.tinygroup.dbrouter.RouterManager;
import org.tinygroup.dbrouter.factory.RouterManagerBeanFactory;

/**
 * 
 * 功能说明:测试 DatabaseMetaData的getTable方法
 * <p>
 * 
 * 开发人员: renhui <br>
 * 开发时间: 2014-1-10 <br>
 * <br>
 */
public class DatabaseMetaDataTest extends TestCase {


	protected void setUp() throws Exception {
		super.setUp();
	}

	private Connection getConnectonWithDiffSchema()
			throws ClassNotFoundException, SQLException {
		RouterManager routerManager = RouterManagerBeanFactory.getManager();
		routerManager.addRouters("/differentSchemaShard.xml");
		Class.forName("org.tinygroup.dbrouterjdbc3.jdbc.TinyDriver");
		Connection conn = DriverManager.getConnection(
				"jdbc:dbrouter://diffSchemaShard", "luog", "123456");
		return conn;
	}

	private Connection getConnectionWithSomeSchema()
			throws ClassNotFoundException, SQLException {
		RouterManager routerManager = RouterManagerBeanFactory.getManager();
		routerManager.addRouters("/sameSchemaDiffTableWithShard.xml");
		Class.forName("org.tinygroup.dbrouterjdbc3.jdbc.TinyDriver");
		Connection conn = DriverManager.getConnection(
				"jdbc:dbrouter://tableShard", "luog", "123456");
		return conn;
	}

	private Connection getConnectionTableMapping()
			throws ClassNotFoundException, SQLException {
		RouterManager routerManager = RouterManagerBeanFactory.getManager();
		routerManager.addRouters("/sameSchemaDiffTableMapping.xml");
		Class.forName("org.tinygroup.dbrouterjdbc3.jdbc.TinyDriver");
		Connection conn = DriverManager.getConnection(
				"jdbc:dbrouter://tableMappingShard", "luog", "123456");
		return conn;
	}


	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetTablesWithDiffSchema() throws ClassNotFoundException,
			SQLException {
		Connection connection = getConnectonWithDiffSchema();
		DatabaseMetaData metaData = connection.getMetaData();
		ResultSet rs = metaData.getTables("", "", "aaa",
				new String[] { "TABLE" });
		rs.last();
		assertEquals(3, rs.getRow());
		connection.close();
	}

	public void testGetTablesWithSomeSchemaMappingShard()
			throws ClassNotFoundException, SQLException {
		Connection connection = getConnectionTableMapping();
		DatabaseMetaData metaData = connection.getMetaData();
		ResultSet rs = metaData.getTables("", "", "aaa%",
				new String[] { "TABLE" });
		rs.last();
		assertEquals(1, rs.getRow());
		connection.close();
	}

	public void testGetTablesWithSomeSchema() throws ClassNotFoundException,
			SQLException {
		Connection connection = getConnectionWithSomeSchema();
		DatabaseMetaData metaData = connection.getMetaData();
		ResultSet rs = metaData.getTables("", "", "aaa%",
				new String[] { "TABLE" });
		rs.last();
		assertEquals(1, rs.getRow());
		connection.close();
	}
}
