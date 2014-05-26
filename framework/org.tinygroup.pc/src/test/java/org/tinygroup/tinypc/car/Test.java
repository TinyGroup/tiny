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

import org.tinygroup.tinypc.JobCenter;
import org.tinygroup.tinypc.Warehouse;
import org.tinygroup.tinypc.Work;
import org.tinygroup.tinypc.impl.*;

import java.io.IOException;

/**
 * Created by luoguo on 14-1-8.
 */
public class Test {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        JobCenter jobCenter = new JobCenterLocal();


        for (int i = 0; i < 5; i++) {
            jobCenter.registerWorker(new StepFirstWorker());
        }
        for (int i = 0; i < 5; i++) {
            jobCenter.registerWorker(new StepSecondTyreWorker());
        }
        for (int i = 0; i < 6; i++) {
            jobCenter.registerWorker(new StepSecondSeatWorker());
        }
        for (int i = 0; i < 7; i++) {
            jobCenter.registerWorker(new StepSecondEngineWorker());
        }
        for (int i = 0; i < 5; i++) {
            jobCenter.registerWorker(new StepThirdDoorWorker());
        }
        for (int i = 0; i < 6; i++) {
            jobCenter.registerWorker(new StepThirdRoofWorker());
        }

        jobCenter.registerForeman(new ForemanSelectOneWorker("first"));
        jobCenter.registerForeman(new ForemanSelectAllWorker("tyre", new SecondWorkSplitter()));
        jobCenter.registerForeman(new ForemanSelectAllWorker("engine", new SecondWorkSplitter()));
        jobCenter.registerForeman(new ForemanSelectAllWorker("seat", new SecondWorkSplitter()));
        jobCenter.registerForeman(new ForemanSelectAllWorker("door", new ThirdWorkSplitter()));
        jobCenter.registerForeman(new ForemanSelectAllWorker("roof", new ThirdWorkSplitter()));

        Warehouse inputWarehouse = new WarehouseDefault();
        inputWarehouse.put("carType", "普桑");
        Work work = new WorkDefault("first", inputWarehouse);
        work.setNextWork(new WorkDefault("tyre")).setNextWork(new WorkDefault("engine")).setNextWork(new WorkDefault("seat")).setNextWork(new WorkDefault("door")).setNextWork(new WorkDefault("roof"));

//        Warehouse warehouse = 
        	jobCenter.doWork(work);

        jobCenter.stop();
    }
}
