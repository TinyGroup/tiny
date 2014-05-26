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
package org.tinygroup.tinypc.dfile;

import org.tinygroup.tinypc.Foreman;
import org.tinygroup.tinypc.JobCenter;
import org.tinygroup.tinypc.Warehouse;
import org.tinygroup.tinypc.impl.ForemanSelectOneWorker;
import org.tinygroup.tinypc.impl.JobCenterLocal;
import org.tinygroup.tinypc.impl.WarehouseDefault;
import org.tinygroup.tinypc.impl.WorkDefault;

import java.io.File;
import java.io.IOException;

/**
 * Created by luoguo on 14-1-8.
 */
public class Test {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        JobCenter jobCenter = new JobCenterLocal();

        Warehouse inputWarehouse = new WarehouseDefault();
        inputWarehouse.put("fileName", "d:\\");
        WorkDefault work = new WorkDefault("file", inputWarehouse);
        jobCenter.registerWork(work);
        jobCenter.registerWorker(new WorkerFile("c:"));
        jobCenter.registerWorker(new WorkerFile("d:"));
        jobCenter.registerWorker(new WorkerFile("e:"));

        Foreman helloForeman = new ForemanSelectOneWorker("file");
        jobCenter.registerForeman(helloForeman);

        Warehouse outputWarehouse = jobCenter.doWork(work);
        File[] subFiles = outputWarehouse.get("subFiles");
        for (File file : subFiles) {
            System.out.println(file.getAbsolutePath());
        }

        jobCenter.stop();
    }
}
