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
package org.tinygroup.cepcore.test.hellormi;

import java.rmi.Naming;

public class HelloClient1 {
	public static void main(String args[]) throws Exception {
		IHello chello = (IHello) Naming
				.lookup("rmi://192.168.84.57:8888/RHello");
		long start = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			chello.sayHelloToSomeBody("luog");
		}
		long end = System.currentTimeMillis();

		System.out.println(end - start);
	}
}
