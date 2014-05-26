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
package org.tinygroup.tinypc.dlock;

import org.tinygroup.rmi.RemoteObject;

import java.rmi.RemoteException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 分布式锁对象
 * Created by luoguo on 14-1-9.
 */
public interface DistributedLock extends RemoteObject {

    /**
     * 一直等直到获得锁为止
     * 
     * @return
     * @throws RemoteException
     * @throws TimeoutException
     */
    long lock() throws RemoteException, TimeoutException;

    /**
     * 在time*unit时间内尝试lock
     * 
     * @param time 
     * @param unit
     * @return
     * @throws RemoteException
     * @throws TimeoutException
     */
    long tryLock(long time, TimeUnit unit) throws RemoteException, TimeoutException;

    void unlock(long token) throws RemoteException;

}
