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

import org.enhydra.jdbc.standard.StandardXADataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CommonJDBCTest {

	private static String driverName = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://mysqldb:3306/testA";
	private static String user = "root";
	private static String password = "123456";
	private static StandardXADataSource dataSource = new StandardXADataSource();

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

	private static void close(Connection conn, Statement st, ResultSet rs) {
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

	public static void main(String[] args) throws ClassNotFoundException,
			SQLException {
		StandardXADataSource dataSource = new StandardXADataSource();
		dataSource.closeFreeConnection();
	}

}
