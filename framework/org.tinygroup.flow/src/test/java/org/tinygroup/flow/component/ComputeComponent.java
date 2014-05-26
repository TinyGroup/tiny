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
package org.tinygroup.flow.component;

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;

public class ComputeComponent implements ComponentInterface {

	private String key;
	private String key2;
	private String resultKey;
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey2() {
		return key2;
	}

	public void setKey2(String key2) {
		this.key2 = key2;
	}

	public String getResultKey() {
		return resultKey;
	}

	public void setResultKey(String resultKey) {
		this.resultKey = resultKey;
	}

	public void execute(Context context) {
		int a = (Integer)context.get(key);
		int b = (Integer)context.get(key2);
		int result = a+b;
		context.put(resultKey, result);
		System.out.println(key+":"+a+"+"+key2+":"+b+"="+resultKey+":"+result);
	}

}
