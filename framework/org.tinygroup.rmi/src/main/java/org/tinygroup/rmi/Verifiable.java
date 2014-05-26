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
package org.tinygroup.rmi;

import java.rmi.RemoteException;

/**
 * 是否可验证,实现了此接口的类，可以进行校验
 */
public interface Verifiable {
    /**
     * 校验，如果校验时不出现异常，就表示是OK的
     *
     * @throws RemoteException
     */
    void verify() throws RemoteException;
}
