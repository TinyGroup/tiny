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

import org.tinygroup.tinypc.Foreman;
import org.tinygroup.tinypc.JobCenter;
import org.tinygroup.tinypc.Warehouse;
import org.tinygroup.tinypc.Work;
import org.tinygroup.tinypc.impl.ForemanSelectOneWorker;
import org.tinygroup.tinypc.impl.JobCenterLocal;
import org.tinygroup.tinypc.impl.WarehouseDefault;
import org.tinygroup.tinypc.impl.WorkDefault;

import java.io.IOException;
import java.util.List;

/**
 * Created by luoguo on 14-1-14.
 */
public class TestSerialWorkAuto {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        JobCenter jobCenter = new JobCenterLocal();

        Warehouse inputWarehouse1 = new WarehouseDefault();
        inputWarehouse1.put("name", "luoguo1");
        Work work1 = new WorkDefault("hello", inputWarehouse1);
        work1.setNextWork(new WorkDefault("hello")).setNextWork(new WorkDefault("hello"));
        
        Warehouse inputWarehouse = new WarehouseDefault();
        inputWarehouse.put("name", "luoguo");
        Work work = new WorkDefault("hello", inputWarehouse);
        work.setNextWork(new WorkDefault("hello")).setNextWork(new WorkDefault("hello"));

        
        for (int i = 0; i < 5; i++) {
            jobCenter.registerWorker(new WorkerHello());
        }

        Foreman helloForeman = new ForemanSelectOneWorker("hello");
        jobCenter.registerForeman(helloForeman);
        jobCenter.registerWork(work);
        jobCenter.registerWork(work1);
        
        List<Work> workList = jobCenter.getWorkList();
        System.out.println(workList.size());
        try {
        	 while (workList.size() > 0) {
                 jobCenter.autoMatch();
                 Thread.sleep(100);
                 workList = jobCenter.getWorkList();
             }
		} catch (Exception e) {
			e.printStackTrace();
		}
       
        Thread.sleep(10000);
        jobCenter.stop();
    }
}
