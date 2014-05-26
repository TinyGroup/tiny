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
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.XAConnection;
import javax.transaction.UserTransaction;

import org.enhydra.jdbc.standard.StandardXADataSource;
import org.objectweb.jotm.Jotm;


/**
 * Created by luoguo on 13-12-20.
 */
public class JotmTest {
    public static void main(String[] args) throws Exception {
        Jotm jotm = new Jotm(true, false);
        UserTransaction userTransaction = jotm.getUserTransaction();
        Connection[] connections = new Connection[3];
        for (int i = 0; i < 3; i++) {
            StandardXADataSource dataSource = new StandardXADataSource();
            dataSource.setUrl("jdbc:mysql://mysqldb:3306/test" + i);
            dataSource.setDriverName("com.mysql.jdbc.Driver");
            dataSource.setUser("root");
            dataSource.setPassword("123456");
            dataSource.setTransactionManager(jotm.getTransactionManager());
            XAConnection xaConnection = dataSource.getXAConnection();
            connections[i] = xaConnection.getConnection();
        }
        try {
        	userTransaction.begin();
            //TODO 添加事务相关内容
            for (Connection connection : connections) {
            	  Statement statement=connection.createStatement();
                  statement.executeUpdate("delete from aaa");
    		}
            connections[0].createStatement().executeUpdate("insert into aaa(id,aaa) values (7,'ppp')");
            for (Connection connection : connections) {
          	  Statement statement=connection.createStatement();
               for (int i = 0; i < 10; i++) {
            	   statement.executeUpdate("insert into aaa(id,aaa) values ("+i+",'ppp')");
    		   }
    		}
            userTransaction.commit();
		} catch (Exception e) {
			userTransaction.rollback();
		}
		  ResultSet rs=connections[0].createStatement().executeQuery("select count(*) from aaa");
          if(rs.next()){
          	System.out.println(rs.getInt(1));
          }
        
        jotm.stop();
        
    }
}
