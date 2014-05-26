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
import org.tinygroup.tinypc.WorkSplitter;
import org.tinygroup.tinypc.Worker;
import org.tinygroup.tinypc.impl.WarehouseDefault;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luoguo on 14-1-28.
 */
public class SecondWorkSplitter implements WorkSplitter {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6743732710202761942L;

	public List<Warehouse> split(Work work, List<Worker> workers)
			throws RemoteException {
		List<Warehouse> list = new ArrayList<Warehouse>();
		for (Worker w : workers) {
			list.add(getWareHouse(work.getInputWarehouse(), w.getType()));
		}

		
		return list;
	}

	private Warehouse getWareHouse(Warehouse inputWarehouse, String stepClass) {
		Warehouse warehouse = new WarehouseDefault();
		warehouse.put("class", stepClass);
		warehouse.putSubWarehouse(inputWarehouse);
		return warehouse;
	}
}
