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
import java.sql.DriverManager;
import java.sql.Statement;

import org.tinygroup.dbrouter.RouterManager;
import org.tinygroup.dbrouter.config.Router;
import org.tinygroup.dbrouter.factory.RouterManagerBeanFactory;

public class TestRouterInsertToDiffrentTable {
    public static void main(String[] args) throws Throwable {
        RouterManager routerManager = RouterManagerBeanFactory.getManager();
        Router router = TestRouterUtil.getSameSchemaDiffrentTableRouter();
        routerManager.addRouter(router);
        Class.forName("org.tinygroup.dbrouterjdbc3.jdbc.TinyDriver");
        Connection conn = DriverManager.getConnection("jdbc:dbrouter://router1", "luog", "123456");
        Statement stmt = conn.createStatement();
        String sql;
        //插入100条数据
        for (int i = 0; i < 100; i++) {
            sql = "insert into aaa(id,aaa) values (" + routerManager.getPrimaryKey(router, "aaa") + ",'ppp')";
            boolean result = stmt.execute(sql);
        }
    }
}
