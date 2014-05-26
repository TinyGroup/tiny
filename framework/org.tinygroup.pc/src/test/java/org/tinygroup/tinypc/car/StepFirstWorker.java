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
package org.tinygroup.tinypc.car;

import org.tinygroup.tinypc.Warehouse;
import org.tinygroup.tinypc.Work;
import org.tinygroup.tinypc.impl.AbstractWorker;

import java.rmi.RemoteException;

/**
 * Created by luoguo on 14-1-28.
 */
public class StepFirstWorker extends AbstractWorker {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3631711170608988710L;

	public StepFirstWorker() throws RemoteException {
        super("first");
    }


    protected Warehouse doWork(Work work) throws RemoteException {
        System.out.println(String.format("%s 构建底盘完成.", work.getInputWarehouse().get("carType")));
        Warehouse outputWarehouse = work.getInputWarehouse();
        outputWarehouse.put("baseInfo", "something about baseInfo");
        return outputWarehouse;
    }
}
