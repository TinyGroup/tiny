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
package org.tinygroup.net.test;

import java.util.Date;

import org.tinygroup.net.daemon.DaemonRunnable;
import org.tinygroup.net.daemon.DaemonUtils;

public class DeamonThreadTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DaemonRunnable daemonRunnable = new DaemonRunnable() {

			
			protected void stopAction() {
				System.out.println("stop");
			}

			
			protected void startAction() {
				System.out.println(new Date() + "-action...");
			}
		};
		DaemonUtils.daemon("testDaemon", daemonRunnable);
		try {
			System.out.println("wait begin:" + new Date());
			Thread.sleep(50000);
			System.out.println("wait end:" + new Date());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		daemonRunnable.stop();
	}

}
