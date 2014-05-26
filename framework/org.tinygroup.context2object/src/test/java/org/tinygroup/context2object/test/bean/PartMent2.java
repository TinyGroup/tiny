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
package org.tinygroup.context2object.test.bean;

import java.util.List;

/**
 * @author chenjiao
 *
 */
public class PartMent2 {
	String name;
	List<CatInterface> cats;
	CatInterface cat;
	CatInterface[] catsArray;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<CatInterface> getCats() {
		return cats;
	}
	public void setCats(List<CatInterface> cats) {
		this.cats = cats;
	}
	public CatInterface getCat() {
		return cat;
	}
	public void setCat(CatInterface cat) {
		this.cat = cat;
	}
	public CatInterface[] getCatsArray() {
		return catsArray;
	}
	public void setCatsArray(CatInterface[] catsArray) {
		this.catsArray = catsArray;
	}
	
}
