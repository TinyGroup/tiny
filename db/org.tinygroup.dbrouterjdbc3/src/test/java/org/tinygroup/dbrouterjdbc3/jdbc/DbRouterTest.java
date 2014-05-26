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
import org.tinygroup.dbrouter.config.Router;
import org.tinygroup.dbrouter.factory.RouterManagerBeanFactory;
import org.tinygroup.dbrouterjdbc3.jdbc.sample.TestRouterUtil;

public class DbRouterTest extends TestCase {


    public void testTableShard() throws Exception {
        RouterManager routerManager = RouterManagerBeanFactory.getManager();
        routerManager.addRouters("/sameSchemaDiffTableWithShard.xml");
        Router router=routerManager.getRouter("tableShard");
        Class.forName("org.tinygroup.dbrouterjdbc3.jdbc.TinyDriver");
        Connection conn = DriverManager.getConnection("jdbc:dbrouter://tableShard", "luog", "123456");
        Statement stmt = conn.createStatement();
        String sql;
        //先删除数据
        stmt.executeUpdate("delete from aaa");
        //插入10条数据
        for (int i = 0; i < 10; i++) {
        	   sql = "insert into aaa(id,aaa) values (" + routerManager.getPrimaryKey(router, "aaa") + ",'ppp')";
        	   stmt.executeUpdate(sql);
        }
        sql="select count(*) from aaa";
        ResultSet rs=stmt.executeQuery(sql);
        rs.next();
        assertEquals(10, rs.getInt(1));
        rs.close();
        stmt.close();
        conn.close();
    }


    public void testPrimarySlaveRouter() throws Exception {
        RouterManager routerManager = RouterManagerBeanFactory.getManager();
        routerManager.addRouters("/differentSchemaPrimarySlave.xml");
        Class.forName("org.tinygroup.dbrouterjdbc3.jdbc.TinyDriver");
        Router router=routerManager.getRouter("diffPrimarySlave");
        Connection conn = DriverManager.getConnection("jdbc:dbrouter://diffPrimarySlave", "luog", "123456");
        Statement stmt = conn.createStatement();
        String sql;
        conn.setAutoCommit(false);
        //先删除数据
        stmt.executeUpdate("delete from aaa");
        //插入10条数据
        for (int i = 0; i < 10; i++) {
        	   sql = "insert into aaa(id,aaa) values (" + routerManager.getPrimaryKey(router, "aaa") + ",'ppp')";
        	   stmt.executeUpdate(sql);
        }
        conn.commit();
        stmt.close();
        conn.close();

    }

    public void testPrepareStatement() throws Exception {
        RouterManager routerManager = RouterManagerBeanFactory.getManager();
        routerManager.addRouters("/differentSchemaShard.xml");
        Class.forName("org.tinygroup.dbrouterjdbc3.jdbc.TinyDriver");
        Connection conn = DriverManager.getConnection("jdbc:dbrouter://diffSchemaShard", "luog", "123456");
        Statement stmt = conn.createStatement();
        //先删除数据
        stmt.executeUpdate("delete from aaa");
        String sql;
        //插入10条数据
        for (int i = 1; i <= 3; i++) {
        	   sql = "insert into aaa(id,aaa) values (" + i + ",'ppp')";
        	   stmt.executeUpdate(sql);
        }
        PreparedStatement statement = conn.prepareStatement("select * from aaa where id in(?,?,?) order by id desc");
        statement.setInt(1, 1);
        statement.setInt(2, 2);
        statement.setInt(3, 3);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        assertEquals(3,  resultSet.getInt(1));
        resultSet.close();
        statement.close();
        conn.close();

    }


    public void testAutoInsertPrimaryWithDiffSchema() throws Exception {
        RouterManager routerManager = RouterManagerBeanFactory.getManager();
        Router router = TestRouterUtil.getDifferentSchemaRouter();
        routerManager.addRouter(router);
        Class.forName("org.tinygroup.dbrouterjdbc3.jdbc.TinyDriver");
        Connection conn = DriverManager.getConnection("jdbc:dbrouter://router1", "luog", "123456");
        Statement stmt = conn.createStatement();
        stmt.execute("delete from aaa");
        String sql;
        //插入100条数据
        for (int i = 0; i < 10; i++) {
            sql = "insert into aaa(aaa) values ('ppp')";
            stmt.execute(sql);
        }
        sql="select count(*) from aaa";
        ResultSet rs=stmt.executeQuery(sql);
        rs.next();
        assertEquals(10, rs.getInt(1));
        rs.close();
        stmt.close();
        conn.close();
    }

    
    public void testAutoInsertPrimaryWithSameSchema() throws Exception {
    	RouterManager routerManager = RouterManagerBeanFactory.getManager();
        routerManager.addRouters("/sameSchemaDiffTableWithShard.xml");
        Class.forName("org.tinygroup.dbrouterjdbc3.jdbc.TinyDriver");
        Connection conn = DriverManager.getConnection("jdbc:dbrouter://tableShard", "luog", "123456");
        Statement stmt = conn.createStatement();
        stmt.execute("delete from aaa");
        String sql;
        //插入100条数据
        for (int i = 0; i < 10; i++) {
            sql = "insert into aaa(aaa) values ('ppp')";
            stmt.execute(sql);
        }
        sql="select count(*) from aaa";
        ResultSet rs=stmt.executeQuery(sql);
        rs.next();
        assertEquals(10, rs.getInt(1));
        rs.close();
        stmt.close();
        conn.close();
    }
    
    public void testDeleteCount() throws ClassNotFoundException, SQLException {
        RouterManager routerManager = RouterManagerBeanFactory.getManager();
        Router router = TestRouterUtil.getDifferentSchemaRouter();
        routerManager.addRouter(router);
        Class.forName("org.tinygroup.dbrouterjdbc3.jdbc.TinyDriver");
        Connection conn = DriverManager.getConnection("jdbc:dbrouter://router1", "luog", "123456");
        Statement stmt = conn.createStatement();
        //先删除完存在的数据
        String sql = "delete from aaa";
        stmt.execute(sql);
        //测试之前，插入10条数据
        for (int i = 0; i < 10; i++) {
            String insertSql = "insert into aaa(id,aaa) values ("
                    + routerManager.getPrimaryKey(router, "aaa") + ",'ppp')";
            stmt.execute(insertSql);
        }
        int record = stmt.executeUpdate(sql);//再次执行删除语句，比较此次删除的记录数
        assertEquals(10, record);
        stmt.close();
        conn.close();
    }


}
