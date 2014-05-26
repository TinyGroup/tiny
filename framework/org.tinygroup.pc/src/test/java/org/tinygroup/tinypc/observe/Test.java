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
package org.tinygroup.tinypc.observe;

import org.tinygroup.tinypc.Foreman;
import org.tinygroup.tinypc.JobCenter;
import org.tinygroup.tinypc.Warehouse;
import org.tinygroup.tinypc.Work;
import org.tinygroup.tinypc.impl.ForemanSelectAllWorker;
import org.tinygroup.tinypc.impl.JobCenterLocal;
import org.tinygroup.tinypc.impl.WarehouseDefault;
import org.tinygroup.tinypc.impl.WorkDefault;

import java.io.IOException;

/**
 * Created by luoguo on 14-1-8.
 */
public class Test {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        JobCenter jobCenter = new JobCenterLocal();

        Warehouse inputWarehouse = new WarehouseDefault();
        inputWarehouse.put("title", "今日头条");
        inputWarehouse.put("content", "今日头条之精彩内容...");
        Work work = new WorkDefault("info", inputWarehouse);

        for (int i = 0; i < 5; i++) {
            jobCenter.registerWorker(new InfoWorker("info"));
        }

        Foreman infoForeman = new ForemanSelectAllWorker("info");
        jobCenter.registerForeman(infoForeman);

        jobCenter.doWork(work);
        jobCenter.stop();
    }
}
