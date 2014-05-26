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

import org.tinygroup.service.test.exception.ExceptionA;
import org.tinygroup.service.test.exception.ExceptionB;
import org.tinygroup.service.test.exception.ExceptionC;
import org.tinygroup.service.test.exception.ExceptionD;

public class ExceptionService {
	public void exception1(){
		throw new ExceptionA();
	}
	public void exception2(){
		throw new ExceptionB();
	}
	public void exception3(){
		throw new ExceptionC();
	}
	public void exception4(){
		throw new ExceptionD();
	}
}
