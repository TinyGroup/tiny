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
package org.tinygroup.tinypc.sum;

import org.tinygroup.tinypc.Warehouse;
import org.tinygroup.tinypc.Work;
import org.tinygroup.tinypc.impl.AbstractWorker;
import org.tinygroup.tinypc.impl.WarehouseDefault;

import java.rmi.RemoteException;

/**
 * Created by luoguo on 14-1-8.
 */
public class WorkerSum extends AbstractWorker {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5873591269002335547L;

	public WorkerSum() throws RemoteException {
        super("sum");
    }

    public Warehouse doWork(Work work) throws RemoteException {
        long start = (Long) work.getInputWarehouse().get("start");
        long end = (Long) work.getInputWarehouse().get("end");
        long sum = 0;
        for (long i = start; i <= end; i++) {
            sum += i;
        }
        Warehouse outputWarehouse = new WarehouseDefault();
        outputWarehouse.put("sum", sum);
        return outputWarehouse;
    }

}
