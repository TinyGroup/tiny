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
package org.tinygroup.service.test.service;

public class InterfaceDefImpl implements InterfaceDef {
	private InterfaceDef2 ref;
	
	public InterfaceDef2 getRef() {
		return ref;
	}

	public void setRef(InterfaceDef2 ref) {
		this.ref = ref;
	}

	public String test() {
		return "1"+ref.test();
	}

}
