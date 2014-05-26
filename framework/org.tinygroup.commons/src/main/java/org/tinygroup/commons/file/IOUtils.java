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
package org.tinygroup.commons.file;

import java.io.InputStream;
import java.io.OutputStream;

public class IOUtils {
	public static String readFromInputStream(InputStream inputStream,
			String encode) throws Exception {
		byte[] buf = new byte[inputStream.available()];
		inputStream.read(buf);
		return new String(buf, encode);
	}

	public static void writeToOutputStream(OutputStream outputStream,
			String content, String encode) throws Exception {
		outputStream.write(content.getBytes(encode));
	}

}
