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
package org.tinygroup.tinypc.helloforeach.rmi;

import org.tinygroup.tinypc.JobCenter;
import org.tinygroup.tinypc.hellosingle.WorkerHello;
import org.tinygroup.tinypc.impl.JobCenterRemote;

public class TestWorkerClient {
	private static String SERVERIP = "192.168.84.52";

	public static void main(String[] args) {
				try {
			JobCenter jobCenter = new JobCenterRemote(SERVERIP, 8888);
			for (int i = 0; i < 5; i++) {
				jobCenter.registerWorker(new WorkerHello());
			}

			TestWorkerClient c = new TestWorkerClient();
			c.run();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void run() {
		MyThread t = new MyThread();
		t.run();
	}

	class MyThread extends Thread {
		boolean end = false;

		public void run() {
			while (!end) {

				try {
					sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}
}
