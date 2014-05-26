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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.tinygroup.dbrouter.config.Partition;

/**
 * 分片规则
 *
 * @author luoguo
 */
@XStreamAlias("shard-rule")
public interface ShardRule {

    /**
     * 返回是否属于当前分片处理
     *
     * @param partition      所属的分区
     * @param sql            要判断的SQL
     * @param preparedParams Prepared Statement的参数
     * @return
     */
    boolean isMatch(Partition partition, String sql, Object... preparedParams);

    /**
     * 返回替换好的SQL语句，对于在同一个schema中的用多个表进行分表的话，就需要替换SQL脚本
     *
     * @param sql
     * @return
     */
    String getReplacedSql(String sql);

}
