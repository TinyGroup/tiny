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
package org.tinygroup.cepcore;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import junit.framework.TestCase;

public class ConcurrentHashMapTest extends TestCase {

	private static Map<Integer, String> map = new ConcurrentHashMap<Integer, String>();
	private static final int length = 10;
	private byte[] bytes = new byte[0];
	static {
		for (int i = 0; i < length; i++) {
			map.put(i, i + "");
		}
	}

	public void testMap() {

		for (int i = 0; i < length; i++) {
			new Remove(i).start();
			new Put(i).start();
		}
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
		}

	}

	class Remove extends Thread {
		private int i;
		

		public Remove(int i) {
			this.i = i;
		}


		public void run() {
//			synchronized (bytes) {
			map.remove(i);
			synchronized (bytes) {
			System.out.println("---remove----");
			System.out.println(map.get(i));
			System.out.println(map.size());
			}
//				System.out.println("----hhaa------");
//				for (Integer key : map.keySet()) {
//					System.out.println(map.get(key));
//				}
//				System.out.println("----hhaassss------");
//			}
			
			
		}

	}
	class Put extends Thread {
		private int i;
		

		public Put(int i) {
			this.i = i;
		}

		public void run() {
//			synchronized (bytes) {
			map.put(i, i + "");
			synchronized (bytes) {
			System.out.println("---put----");
			System.out.println(map.get(i));
			System.out.println(map.size());
			}
//				System.out.println("----hhaa------");
//				for (Integer key : map.keySet()) {
//					System.out.println(map.get(key));
//				}
//				System.out.println("----hhaassss------");
//			}
			
			
		}

	}

}
