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

import org.tinygroup.dbrouter.balance.ShardBalance;
import org.tinygroup.dbrouter.config.Router;
import org.tinygroup.dbrouter.config.Routers;
import org.tinygroup.dbrouter.config.Partition;
import org.tinygroup.dbrouter.config.Shard;
import org.tinygroup.jsqlparser.statement.Statement;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 集群管理器，用于对当前JVM中的数据库集群进行管理，并提供相关处理的接口
 */
public interface RouterManager {
    /**
     * 返回是否是分片语句
     *
     * @param partition
     * @param sql
     * @param preparedParams prepared参数,用于preparedStatement
     * @return
     */
    boolean isShardSql(Partition partition, String sql, Object... preparedParams);

    /**
     * 添加语句处理器
     *
     * @param statementProcessor
     */
    void addStatementProcessor(StatementProcessor statementProcessor);

    /**
     * 返回所有语句处理器列表
     *
     * @return
     */
    List<StatementProcessor> getStatementProcessorList();

    /**
     * 给某个集群的数据表产生主键
     *
     * @param router
     * @param tableName
     * @param <T>
     * @return
     */
    <T> T getPrimaryKey(Router router, String tableName);

    /**
     * 返回SQL对应的Statement
     *
     * @param sql
     * @return
     */
    Statement getSqlStatement(String sql);

    /**
     * 添加集群
     *
     * @param router
     */
    void addRouter(Router router);

    /**
     * 添加一组集群
     *
     * @param routers
     */
    void addRouters(Routers routers);

    /**
     * 获取集群
     *
     * @param routerId
     * @return
     */
    Router getRouter(String routerId);

    /**
     * 获取集群配置Map
     *
     * @return
     */
    Map<String, Router> getRouterMap();

    /**
     * 返回某个分区与sql是否匹配
     *
     * @param partition
     * @param sql
     * @return
     */
    boolean isMatch(Partition partition, String sql);

    /**
     * 返回某个分片是否匹配
     *
     * @param shard
     * @param sql
     * @param preparedParams prepared参数,用于preparedStatement
     * @return
     */
    boolean isMatch(Partition partition, Shard shard, String sql, Object... preparedParams);

    /**
     * 返回分片执行语句
     *
     * @param partition
     * @param shard
     * @param sql
     * @param preparedParams prepared参数
     * @return
     */
    String getSql(Partition partition, Shard shard, String sql, Object... preparedParams);

    /**
     * 获取匹配的分区<br>
     *
     * @param routerId
     * @param sql
     * @return
     */
    Collection<Partition> getPartitions(String routerId, String sql);

    /**
     * 获取匹配的首个分区
     *
     * @param routerId
     * @param sql
     * @return
     */
    Partition getPartition(String routerId, String sql);

    /**
     * 获取匹配的首个分区
     *
     * @param router
     * @param sql
     * @return
     */
    Partition getPartition(Router router, String sql);

    /**
     * 获取匹配的分区
     *
     * @param router
     * @param sql
     * @return
     */
    List<Partition> getPartitions(Router router, String sql);

    /**
     * 获取匹配的分片
     *
     * @param partition
     * @param sql
     * @param preparedParams prepared参数,用于preparedStatement
     * @return
     */
    List<Shard> getShards(Partition partition, String sql, Object... preparedParams);

    /**
     * 返回分片均衡器
     *
     * @return
     */
    ShardBalance getShardBalance();

    /**
     * 设置分片均衡器
     *
     * @param balance
     */
    void setShardBalance(ShardBalance balance);

    /**
     * 根据资源路径加载配置
     *
     * @param routerFilePath
     */
    void addRouters(String routerFilePath);

    /**
     * 根据
     *
     * @param inputStream
     */
    void addRouters(InputStream inputStream);
}