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
package org.tinygroup.tinypc.helloforeach;

import org.tinygroup.tinypc.Warehouse;
import org.tinygroup.tinypc.WorkCombiner;
import org.tinygroup.tinypc.impl.WarehouseDefault;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luoguo on 14-1-8.
 */
public class HelloWorkCombiner implements WorkCombiner {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6237840266892435199L;

	public Warehouse combine(List<Warehouse> warehouseList) throws RemoteException {
        Warehouse warehouse = new WarehouseDefault();
        List<String> helloList = new ArrayList<String>();
        for (Warehouse w : warehouseList) {
            helloList.add((String) w.get("helloInfo"));
        }
        warehouse.put("helloInfo", helloList);
        return warehouse;
    }
}
