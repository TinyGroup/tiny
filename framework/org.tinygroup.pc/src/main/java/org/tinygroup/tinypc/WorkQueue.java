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

import org.tinygroup.rmi.RemoteObject;

import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by luoguo on 14-1-23.
 */
public interface WorkQueue extends RemoteObject {

    /**
     * 设置对象仓库
     *
     * @param objectStorage
     */
    void setObjectStorage(ObjectStorage objectStorage) throws RemoteException;

    /**
     * 添加一个工作
     *
     * @param work
     * @throws RemoteException
     */
    void add(Work work) throws RemoteException;

    /**
     * 更新工作状态
     *
     * @param work
     * @throws RemoteException
     */
    void updateWorkStatus(Work work, WorkStatus workStatus) throws RemoteException;

    /**
     * 获取工作状态
     *
     * @param work
     * @return
     * @throws RemoteException
     */
    WorkStatus getWorkStatus(Work work) throws RemoteException;

    /**
     * 删除一个工作
     *
     * @param work
     * @throws RemoteException
     */

    void remove(Work work) throws RemoteException;

    /**
     * 返回工作总数
     *
     * @return
     */
    int size() throws RemoteException;

    /**
     * 返回所有的工作列表
     *
     * @return
     * @throws RemoteException
     */
    List<Work> getWorkList() throws RemoteException;

    /**
     * 返回一个空闲工作
     *
     * @return
     * @throws RemoteException
     */
    Work getIdleWork() throws RemoteException;

    /**
     * 返回指定类型指定工作状态的工作列表
     *
     * @param type
     * @param workStatus
     * @return
     * @throws RemoteException
     */
    List<Work> getWorkList(String type, WorkStatus workStatus) throws RemoteException;

    /**
     * 返回指定工作状态的工作列表
     *
     * @param workStatus
     * @return
     * @throws RemoteException
     */
    List<Work> getWorkList(WorkStatus workStatus) throws RemoteException;

    /**
     * 去掉一个旧工作，添加一个新工作
     *
     * @param oldWork
     * @param newWork
     * @throws RemoteException
     */
    void replace(Work oldWork, Work newWork) throws RemoteException;

    /**
     * 把工作移动到工作队列最后
     *
     * @param work
     */
    void moveToLast(Work work) throws RemoteException;

}
