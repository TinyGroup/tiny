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
 * 结果合并者
 * 工作进行分解后，分解执行后的结果如果需要进行合并，这个时候可以用结果合并者来完成
 * Created by luoguo on 14-1-8.
 */
public interface WorkCombiner extends RemoteObject {

    /**
     * 把所有工人的执行结果再放到work的输出仓库中
     *
     * @param warehouseList
     */
    Warehouse combine(List<Warehouse> warehouseList) throws RemoteException;
}
