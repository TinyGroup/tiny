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
package org.tinygroup.commons.exec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public final class CommandExecutor {
	private CommandExecutor() {
	}

	public static String execute(String command) throws IOException {
		Process process = Runtime.getRuntime().exec(command);
		// 记录dos命令的返回信息
		StringBuffer stringBuffer = new StringBuffer();
		// 获取返回信息的流
		InputStream in = process.getInputStream();
		Reader reader = new InputStreamReader(in, "GBK");
		BufferedReader bReader = new BufferedReader(reader);
		String res = bReader.readLine();
		while (res != null) {
			stringBuffer.append(res);
			stringBuffer.append("\n");
			res = bReader.readLine();
		}
		bReader.close();
		reader.close();
		return stringBuffer.toString();
	}
}
