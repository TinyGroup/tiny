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

import org.tinygroup.rmi.RmiServer;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

/**
 * 职介所
 * 职介所是分布式处理的核心场所，所有工作相关的元素都要通过职介所进行关联
 * Created by luoguo on 14-1-8.
 */
public interface JobCenter {
    String WORK_QUEUE = "WorkQueue";
    String FOREMAN = "Foreman";
    String WORKER = "Worker";
    int DEFAULT_PORT = 3333;

    RmiServer getRmiServer();

    void setRmiServer(RmiServer rmiServer) throws RemoteException;

    /**
     * 注册工人
     *
     * @param worker
     */
    void registerWorker(Worker worker) throws RemoteException;

    /**
     * 返回工作队列对象
     *
     * @return
     */
    WorkQueue getWorkQueue();

    /**
     * 注消工人
     *
     * @param worker
     */
    void unregisterWorker(Worker worker) throws RemoteException;

    /**
     * 注册一份工作，工作情况不需要马上关注。因此也就不用等待，马上返回可以进行其它处理
     * 如果有返回结果，可以通过异步方式，，异步方式可以用后续工作的方式来指定
     *
     * @param work
     */
    void registerWork(Work work) throws IOException;


    /**
     * 取消工作，在工作没有分配出去之前，可以从职介所注消工作，如果工作已经分配出去，则无法注消
     *
     * @param work
     */
    void unregisterWork(Work work) throws RemoteException;

    /**
     * 返回指定工作的工作状态
     *
     * @param work
     * @return
     */
    WorkStatus getWorkStatus(Work work) throws RemoteException;

    /**
     * 执行一项工作，期望同步得到结果或异常
     * 如果没有合适的工人或包工头进行处理，马上会抛出异常
     *
     * @param work
     */
    Warehouse doWork(Work work) throws IOException;

    /**
     * 注册包工头
     *
     * @param foreman
     */
    void registerForeman(Foreman foreman) throws RemoteException;

    /**
     * 注销包工头
     *
     * @param foreman
     */
    void unregisterForeMan(Foreman foreman) throws RemoteException;

    /**
     * 返回具有某种类型的空闲且愿意接受工作的工人列表
     *
     * @return
     */
    List<Worker> getWorkerList(Work work) throws RemoteException;


    /**
     * 返回所有的工作列表
     *
     * @return
     */
    List<Work> getWorkList() throws RemoteException;

    /**
     * 返回某种类型的某种状态的工作列表
     *
     * @return
     */
    List<Work> getWorkList(String type, WorkStatus workStatus) throws RemoteException;


    /**
     * 返回组织某种工作的的空闲工头列表
     *
     * @param type
     * @return
     */
    List<Foreman> getForeman(String type) throws RemoteException;

    /**
     * 自动进行匹配，如果有匹配成功的，则予以触发执行
     */
    void autoMatch() throws IOException;

    /**
     * 职业介绍所关门
     *
     * @throws RemoteException
     */
    void stop() throws RemoteException;
}
