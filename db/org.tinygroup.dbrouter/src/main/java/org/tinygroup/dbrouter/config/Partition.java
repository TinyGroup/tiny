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
package org.tinygroup.dbrouter.config;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.dbrouter.PartitionRule;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 分区
 *
 * @author luoguo
 */
@XStreamAlias("partition")
public class Partition {
    /**
     * 分区标识
     */
	@XStreamAsAttribute
	private String id;
    /**
     * 分区命中规则
     */
	@XStreamAlias("partition-rules")
    private List<PartitionRule> partitionRules;

    /**
     * 一个分区由多个分片组成
     */
	@XStreamAlias("shards")
    private List<Shard> shards;

    /**
     * 返回分区类型 PrimarySlave
     */
	@XStreamAsAttribute
	private int mode;
    /**
     * 主从模式，主从模式中的数据全部一样，通过读写分离来进行分流
     */
    public static final int MODE_PRIMARY_SLAVE = 1;
    /**
     * 分片模式，每个分片中的数据都不一样，通过分片可以减少单个库表中的记录系数
     */
    public static final int MODE_SHARD = 2;
    transient private List<Shard> writableShardList;
    transient private List<Shard> readableShardList;
    transient private Router router;

    public Partition() {

    }

    /**
     * 构造方法
     *
     * @param id   分区标识
     * @param mode 分区类型
     */
    public Partition(String id, int mode) {
        this.id = id;
        this.mode = mode;
    }


    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<PartitionRule> getPartitionRules() {
        return partitionRules;
    }

    public List<Shard> getWritableShardList() {
        if (writableShardList == null) {
            writableShardList = new ArrayList<Shard>();
            for (Shard shard : shards) {
                if (shard.isWriteAble()) {
                    writableShardList.add(shard);
                }
            }
        }
        return writableShardList;
    }

    public List<Shard> getReadShardList() {
        if (readableShardList == null) {
            readableShardList = new ArrayList<Shard>();
            for (Shard shard : shards) {
                if (shard.getReadWeight() > 0) {
                    readableShardList.add(shard);
                }
            }
        }
        return readableShardList;
    }

    public void addReadAndWriteShard(Shard shard){
    	if (readableShardList == null) {
    		readableShardList = new ArrayList<Shard>(); 	
    	}
    	readableShardList.add(shard);
    	if (writableShardList == null) {
            writableShardList = new ArrayList<Shard>();
    	}
    	writableShardList.add(shard);
    }
    
    public void setPartitionRules(List<PartitionRule> partitionRules) {
        this.partitionRules = partitionRules;
    }

    public List<Shard> getShards() {
        return shards;
    }

    public void setShards(List<Shard> shards) {
        this.shards = shards;
    }

	public Router getRouter() {
		return router;
	}

	public void setRouter(Router router) {
		this.router = router;
	}

    
}
