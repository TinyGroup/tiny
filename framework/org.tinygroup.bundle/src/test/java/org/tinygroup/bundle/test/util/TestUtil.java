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
package org.tinygroup.bundle.test.util;

import org.tinygroup.bundle.BundleManager;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.tinytestutil.AbstractTestUtil;



public class TestUtil {
	private static boolean init = false;
	public static void init(){
		if(!init){
			AbstractTestUtil.init("application.xml", true);
			init = true;
			BundleManager manager = SpringUtil.getBean(BundleManager.BEAN_NAME);
			manager.setCommonRoot(TestUtil.class.getResource("/").getPath());
			manager.setBundleRoot(TestUtil.class.getResource("/").getPath());
		}
		
	}
}
