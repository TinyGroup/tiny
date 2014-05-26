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
package org.tinygroup.tinypc;

import java.io.Serializable;

/**
 * 工作，也可以是一个工作序列
 * 当是工作序列的时候，前一个工作的输出就是下一个工作的输入
 * 工作不可以是环形的，串行的，否则会造成死循环，框架不会做相关检测
 * Created by luoguo on 14-1-8.
 */
public interface Work extends Serializable {
    /**
     * 返回工作类型，每个工作都有一个工作类型，包工头及工人只能处理同样类型的工作
     *
     * @return
     */
    String getType();

    /**
     * 返回对应的包工头类型，如果没有包工头类型，则使用默认类型
     *
     * @return
     */
    String getForemanType();

    /**
     * 返回唯一ID
     *
     * @return
     */
    String getId();

    /**
     * 返回后续步骤的工作，如果有，说明是复合工作，如果没有，说明是简单工作
     *
     * @return
     */
    Work getNextWork();

    /**
     * 设置后续步骤工作
     *
     * @param nextWork 后续工作
     * @return 返回后续工作
     */
    Work setNextWork(Work nextWork);

    /**
     * 是否需要序列化
     *
     * @return true表示工作永不丢失，false表示容器关闭即丢失
     */
    boolean isNeedSerialize();

    /**
     * 设置是否需要进行序列化，如果要用到MQ，则需要设置为需要序列化
     *
     * @param needSerialize true表示工作永不丢失，false表示容器关闭即丢失
     */
    void setNeedSerialize(boolean needSerialize);

    /**
     * 返回输入仓库
     *
     * @return
     */
    Warehouse getInputWarehouse();


    /**
     * 设置输入仓库
     *
     * @param inputWarehouse
     */
    void setInputWarehouse(Warehouse inputWarehouse);

    /**
     * 设置工作状态
     *
     * @param workStatus
     */
    void setWorkStatus(WorkStatus workStatus);

    /**
     * 获取工作状态
     *
     * @return
     */
    WorkStatus getWorkStatus();
}
