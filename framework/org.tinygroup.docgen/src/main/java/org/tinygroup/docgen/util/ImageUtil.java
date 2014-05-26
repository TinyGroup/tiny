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
package org.tinygroup.docgen.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.thoughtworks.xstream.core.util.Base64Encoder;

public class ImageUtil {

	public static String streamToBase64(InputStream inputStream)
			throws Exception {
		byte[] buffer = new byte[inputStream.available()];
		inputStream.read(buffer);
		inputStream.close();
		return new Base64Encoder().encode(buffer);
	}

	public static String fileToBase64(String fileName) throws Exception {
		return fileToBase64(new File(fileName));
	}

	public static String fileToBase64(File file) throws Exception {
		InputStream inputStream = new FileInputStream(file);
		return streamToBase64(inputStream);
	}

}
