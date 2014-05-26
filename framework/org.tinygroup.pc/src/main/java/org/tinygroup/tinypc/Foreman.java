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

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

/**
 * 包工头
 * 包工头用于带着一组工人并完成对应的任务
 * Created by luoguo on 14-1-8.
 */
public interface Foreman extends ParallelObject {
    /**
     * 返回执行哪种类型的工作任务
     *
     * @return
     */
    String getType() throws RemoteException;


    /**
     * 开始干活以完成工作
     */
    Warehouse work(Work work, List<Worker> workerList) throws IOException;

    /**
     * 设置工作合并器
     *
     * @param workCombiner
     */
    void setWorkCombiner(WorkCombiner workCombiner) throws RemoteException;

    /**
     * 设置工作分解器
     *
     * @param workSplitter
     */
    void setWorkSplitter(WorkSplitter workSplitter) throws RemoteException;

}
