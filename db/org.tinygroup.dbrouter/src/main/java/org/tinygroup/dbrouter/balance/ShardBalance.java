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
package org.tinygroup.dbrouter.balance;

import org.tinygroup.dbrouter.config.Partition;
import org.tinygroup.dbrouter.config.Shard;

import java.util.List;

/**
 * 集群分片负载均衡接口
 */
public interface ShardBalance {

    /**
     * 获取写入分区，在所有的写入分区都执行写入相关的指令
     *
     * @return
     */
    List<Shard> getWritableShard(Partition partition);

    /**
     * 获取读取分区
     *
     * @return
     */
    Shard getReadableShard(Partition partition);
    
    /**
     * 获取读取分区,在开启事务的情况下
     *
     * @return
     */
    Shard getReadShardWithTransaction(Partition partition);

}
