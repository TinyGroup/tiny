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
package org.tinygroup.annotation;

import com.thoughtworks.xstream.annotations.XStreamAlias;


@Test(id=0,description="class")
@XStreamAlias("sd")
public class AnnotationDemo1 {
	@Test(id=1,description="field1")
	private String field1;
	@Test(id=2,description="field2")
	private String field2;
	@Test(id=3,description="method1")
	public void method1(){
		System.out.println("method1");
	}
	@Test(id=4,description="method2")
	public void method2(){
		System.out.println("method2");
	}
	public String getField1() {
		return field1;
	}
	public void setField1(String field1) {
		this.field1 = field1;
	}
	public String getField2() {
		return field2;
	}
	public void setField2(String field2) {
		this.field2 = field2;
	}

	
}
