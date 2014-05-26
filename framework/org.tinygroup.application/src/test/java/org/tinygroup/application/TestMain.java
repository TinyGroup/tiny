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
package org.tinygroup.application;

import org.tinygroup.application.impl.ApplicationDefault;
import org.tinygroup.commons.tools.FileUtil;

public class TestMain {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String content = FileUtil.readStreamContent(
				TestMain.class.getResourceAsStream("/application.xml"), "UTF-8");
		Application application = new ApplicationDefault(content);
		application.start();
		System.out.println("============Yes==============");
		application.stop();
	}

}
