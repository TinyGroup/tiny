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
package org.tinygroup.dbrouter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * 用于对SQL进行特殊处理并进行结果合并等<br>
 * <p/>
 * 比如sql语句是select count(*) from abc<br>
 * 则会到所有的shard执行，并对结果相加后返回
 *
 * @author luoguo
 */
public interface StatementProcessor {
    /**
     * 返回是否由此SQL处理器进行处理
     *
     * @param sql
     * @return
     */
    boolean isMatch(String sql);

    /**
     * 返回处理器转换过之后的SQL
     *
     * @param sql
     * @return
     */
    String getSql(String sql);

    /**
     * 对结果进行合并
     * @param sql 
     * @param results
     *
     * @return
     * @throws SQLException
     */
    ResultSet combineResult(String sql, List<ResultSet> results) throws SQLException;
}
