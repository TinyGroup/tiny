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
package org.tinygroup.tinypc.serialwork;

import org.tinygroup.tinypc.Warehouse;
import org.tinygroup.tinypc.Work;
import org.tinygroup.tinypc.impl.AbstractWorker;
import org.tinygroup.tinypc.impl.WarehouseDefault;

import java.rmi.RemoteException;

/**
 * Created by luoguo on 14-1-8.
 */
public class WorkerHello extends AbstractWorker {
    /**
	 * 
	 */
	private static final long serialVersionUID = -8704685836063951822L;

	public WorkerHello() throws RemoteException {
        super("hello");
    }

    public Warehouse doWork(Work work) throws RemoteException {
        String name = work.getInputWarehouse().get("name");
        System.out.println(String.format("id %s: Hello %s", getId(), name));
        Warehouse outputWarehouse = new WarehouseDefault();
        outputWarehouse.put("name", name + "_1");
        return outputWarehouse;
    }
}
