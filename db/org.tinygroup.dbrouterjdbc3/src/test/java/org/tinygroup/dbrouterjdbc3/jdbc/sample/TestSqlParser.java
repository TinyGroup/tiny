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

import org.tinygroup.dbrouter.factory.RouterManagerBeanFactory;
import org.tinygroup.jsqlparser.statement.Statement;

/**
 * Created by luoguo on 13-12-17.
 */
public class TestSqlParser {
    public static void main(String[] args) {
        Statement statement = RouterManagerBeanFactory.getManager().getSqlStatement("select * from " +
                "aaa  where id in (select id from bbb)");
        System.out.println(statement);
    }
}
