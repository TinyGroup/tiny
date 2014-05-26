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
import java.util.HashMap;
import java.util.Map;

public class RemoteExecuteClient {
	public static void main(String args[]) throws Exception {
		RemoteExecute remoteExecute = (RemoteExecute) Naming
				.lookup("rmi://localhost:8888/RemoteExecute");
		long start = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			Map<String, Object> context = new HashMap<String, Object>();
			for (int j = 0; j < 100; j++) {
				context.put("abcdef" + j, "value" + j);
			}
			remoteExecute.execute(context);
		}
		long end = System.currentTimeMillis();

		System.out.println(end - start);
	}
}
