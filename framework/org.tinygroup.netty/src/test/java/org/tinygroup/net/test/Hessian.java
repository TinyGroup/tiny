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

import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

public class Hessian {
	public static void main(String[] args) throws Exception {
		int time = 1000;
		long time1 = System.currentTimeMillis();
		for (int i = 0; i < time; i++) {
			HessianOutput hout = new HessianOutput(new FileOutputStream(
					"hessian.bin"));
			hout.writeObject(Simple.getSimple());
			hout.flush();
		}
		long time2 = System.currentTimeMillis();
		System.out.println(time2 - time1 + ":hession write");
		
		//===================================================
		long time22 = System.currentTimeMillis();
		for (int i = 0; i < time; i++) {
			HessianInput hin = new HessianInput(new FileInputStream(
					"hessian.bin"));
			hin.readObject();
			hin.close();
		}
		long time3 = System.currentTimeMillis();
		System.out.println(time3 - time22 + ":hession read");
		//===================================================
		
	}
}
