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

import org.tinygroup.tinypc.*;
import org.tinygroup.tinypc.impl.WarehouseDefault;
import org.tinygroup.tinypc.range.LongRangeSpliter;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luoguo on 14-1-8.
 */
public class SumSplitterCombiner implements WorkSplitterCombiner {
    /**
	 * 
	 */
	private static final long serialVersionUID = 551260312611646323L;


	public List<Warehouse> split(Work work, List<Worker> workers) throws RemoteException {
        List<Warehouse> list = new ArrayList<Warehouse>();
        LongRangeSpliter longRangeSpliter = new LongRangeSpliter();
        List<Range<Long>> rangePairList = longRangeSpliter.split((Long) work.getInputWarehouse().get("start"), (Long) work.getInputWarehouse().get("end"), workers.size());
        for (Range range : rangePairList) {
            Warehouse subInputWarehouse = new WarehouseDefault();
            subInputWarehouse.put("start", range.getStart());
            subInputWarehouse.put("end", range.getEnd());
            list.add(subInputWarehouse);
        }
        return list;
    }


    public Warehouse combine(List<Warehouse> warehouseList) throws RemoteException {
        Warehouse outputWarehouse = new WarehouseDefault();
        long sum = 0;
        for (Warehouse w : warehouseList) {
            sum += (Long) w.get("sum");
        }
        outputWarehouse.put("sum", sum);
        return outputWarehouse;
    }
}
