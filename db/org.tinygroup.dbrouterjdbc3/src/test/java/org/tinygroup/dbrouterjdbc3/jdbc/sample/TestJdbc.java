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
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestJdbc {
	public static void main(String[] args) throws Throwable {
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://mysqldb:3306/test0?generateSimpleParameterMetadata=true", "root", "123456");
//		Statement stmt = conn.createStatement();
		String sql;
//		sql = "SELECT * FROM aaa where id in (?,?,?) order by id";
//		PreparedStatement stmt=conn.prepareStatement(sql);
//		stmt.setInt(1, 1);
//		stmt.setInt(2, 6);
//		stmt.setInt(3, 3);
//		ResultSet rs = stmt.executeQuery();
//        while(rs.next()){
//            System.out.printf("%d %s \n",rs.getInt(1),rs.getString(2));
//        }
		DatabaseMetaData metaData=conn.getMetaData();
		ResultSet rs=metaData.getColumns(null, null, "aaa", "");
		while(rs.next()){
			System.out.println("---------------");
			System.out.println(rs.getString("COLUMN_SIZE"));
			System.out.println(rs.getString("COLUMN_NAME"));
			System.out.println(rs.getString("DATA_TYPE"));
			System.out.println(rs.getString("TYPE_NAME"));
			System.out.println(rs.getString("DECIMAL_DIGITS"));
			System.out.println("---------------");
		}
		
		rs.close();
	}
}
