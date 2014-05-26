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
package org.tinygroup.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleCommander extends Thread {
	CommandSystem commandSystem;
	boolean stop = false;
	public ConsoleCommander(CommandSystem commandSystem) {
		this.commandSystem = commandSystem;
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public void run() {
		if(!stop){
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			String str = "";
			while (str != null) {
				try {
					System.out.print(">");
					str = in.readLine();
					if (str.equals("exit") || str.equals("quit")) {
						commandSystem.println("Byebye");
						return;
					}
					commandSystem.execute(str);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void stopRead(){
		stop = true;
	}
}
