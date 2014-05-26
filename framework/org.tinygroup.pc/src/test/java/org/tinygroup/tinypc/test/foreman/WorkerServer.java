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
package org.tinygroup.tinypc.test.foreman;

import java.io.IOException;

import org.tinygroup.tinypc.JobCenter;
import org.tinygroup.tinypc.impl.JobCenterLocal;

public class WorkerServer {

	public static void main(String[] args) {
		try {
			JobCenter jobCenter = new JobCenterLocal(8888);
			jobCenter.getRmiServer();
		} catch (IOException e) {
			e.printStackTrace();
		}

		WorkerServer r = new WorkerServer();
		r.runThread();
	}

	public void runThread() {
		MyThread t = new WorkerServer.MyThread();
		t.run();
	}

	class MyThread extends Thread {
		private boolean end = false;

		public void run() {
			while (!end) {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
