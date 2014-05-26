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
package org.tinygroup.dbrouterjdbc3.jdbc.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileUtil {

	/**
	 * 删除某个文件夹下的所有文件夹和文件
	 * 
	 * @param delpath
	 *            String
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @return boolean
	 */
	public static void deleteFile(String delPath) {
		if (delPath == null || delPath.trim().length() == 0) {
			return;
		}
		String pathname = delPath.replaceAll("\\\\", "/");
		File file = new File(pathname);
		if (file.isDirectory()) {
			String[] fileList = file.list();
			for (int i = 0; i < fileList.length; i++) {
				deleteFile(pathname + "/" + fileList[i]);
			}
		}
		// System.out.println(file.getAbsolutePath() + ",删除成功");
		file.delete();
	}

}
