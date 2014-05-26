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
package org.tinygroup.tinydb.operator;

import org.tinygroup.tinydb.Bean;

import java.util.List;
import java.util.Map;

/**
 * DB相关的批量操作
 *
 * @author luoguo
 */
public interface DbSqlOperator<K> {

    // 下面是根据SQL
    Bean[] getBeans(String sql);

    Bean[] getPagedBeans(String sql, int start, int limit);

    Bean[] getBeans(String sql, Map<String, Object> parameters);

    Bean[] getPagedBeans(String sql, int start, int limit, Map<String, Object> parameters);

    Bean[] getBeans(String sql, Object... parameters);

    Bean[] getBeans(String sql, List<Object> parameters);

    Bean[] getPagedBeans(String sql, int start, int limit, Object... parameters);

    // 读取单一个值
    Bean getSingleValue(String sql);

    Bean getSingleValue(String sql, Map<String, Object> parameters);

    Bean getSingleValue(String sql, Object... parameters);

    Bean getSingleValue(String sql, List<Object> parameters);

    /**
     * 执行带参数的sql语句
     *
     * @param sql
     * @param parameters
     * @return
     */
    int execute(String sql, Map<String, Object> parameters);

    /**
     * 执行带参数的sql语句
     *
     * @param sql
     * @param parameters
     * @return
     */
    int execute(String sql, Object... parameters);

    int execute(String sql, List<Object> parameters);

    //查询总记录数
    int account(String sql, Object... parameters);

    int account(String sql, List<Object> parameters);

    int account(String sql, Map<String, Object> parameters);
}
