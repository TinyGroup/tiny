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
package org.tinygroup.flowbasiccomponent;

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;

public class KeyChange implements ComponentInterface {
	// key1:value1,key2:value2,key3:value3
	private String keyValues;
	private boolean replaceModel;//如果为true，则移除原key
	
	public String getKeyValues() {
		return keyValues;
	}

	public void setKeyValues(String keyValues) {
		this.keyValues = keyValues;
	}

	public void execute(Context context) {
		if (keyValues == null || "".equals(keyValues)) {
			String[] array = keyValues.split(",");
			for (String s : array) {
				String[] keyvalue = s.split(":");
				if (keyvalue.length == 2) {
					context.put(keyvalue[1], context.get(keyvalue[0]));
					if(replaceModel)
						context.remove(keyvalue[0]);
				}
			}
		}
	}

}
