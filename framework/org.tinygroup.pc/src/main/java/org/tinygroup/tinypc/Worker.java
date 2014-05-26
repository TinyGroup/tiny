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

import java.rmi.RemoteException;

/**
 * 工人，用于干具体的工作
 * Created by luoguo on 14-1-8.
 */
public interface Worker extends ParallelObject {
    /**
     * 执行工作
     *
     * @return
     */
    Warehouse work(Work work) throws RemoteException;

    /**
     * 是否接受工作
     * 即使是同样类型的工人，有可能对工作也挑三捡四，这里给了工人一定的灵活性
     *
     * @param work
     * @return true表示接受，false表示不接受
     */
    boolean acceptWork(Work work) throws RemoteException;

      /**
     * 返回类型
     *
     * @return
     */
    String getType() throws RemoteException;

}
