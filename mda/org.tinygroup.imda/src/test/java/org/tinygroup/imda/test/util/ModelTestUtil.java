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
package org.tinygroup.imda.test.util;

import org.tinygroup.tinytestutil.AbstractTestUtil;

public class ModelTestUtil {
	private static int value = 1;
	
	public static void  init(){
		AbstractTestUtil.init(null, true);
	}
	
	public static void setValue(int i){
		value = i;
	}
	public static int getValue(){
		return value;
	}
}
