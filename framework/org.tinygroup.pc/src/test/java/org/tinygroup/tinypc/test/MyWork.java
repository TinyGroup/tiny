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
package org.tinygroup.tinypc.test;

import java.util.ArrayList;
import java.util.List;

public class MyWork {
	private String type;
	private List<MyWork> subWorks = new ArrayList<MyWork>();
	private MyWork nextWork;

	public MyWork(String type) {
		this.type = type;
	}

	public MyWork getNextWork() {
		return nextWork;
	}

	public void setNextWork(MyWork nextWork) {
		this.nextWork = nextWork;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<MyWork> getSubWorks() {
		return subWorks;
	}

	public void setSubWorks(List<MyWork> subWorks) {
		this.subWorks = subWorks;
	}

}
