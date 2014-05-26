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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileDealUtil {
	public static void write(File file,String content) throws IOException{
		
		FileOutputStream outputStream = new FileOutputStream(file);
		outputStream.write(content.getBytes("UTF-8"));//utf8的方式保存文件（即文件时UTF-8的，即里面的文字就是UTF-8的）
		outputStream.close();
//		
//		FileWriter fw = new FileWriter(file);
//		OutputStream
//		fw.write(content.getBytes("UTF-8"));
//		fw.close();
	}
}
